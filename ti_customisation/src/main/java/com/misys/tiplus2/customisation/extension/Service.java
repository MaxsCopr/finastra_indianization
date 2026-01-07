package com.misys.tiplus2.customisation.extension;

//com.misys.tiplus2.customisation.extension.Service
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bs.theme.transport.client.ThemeTransportClient;
import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;
import com.misys.tiplus2.customisation.entity.ExtEventAccountRealisation;
import com.misys.tiplus2.customisation.entity.ExtEventAdvanceTable;
import com.misys.tiplus2.customisation.entity.ExtEventBOECAP;
import com.misys.tiplus2.customisation.entity.ExtEventInvoiceData;
import com.misys.tiplus2.customisation.entity.ExtEventInvoicedetailsLC;
import com.misys.tiplus2.customisation.entity.ExtEventInvoicelc;
//import com.misys.tiplus2.customisation.entity.ExtEventInvoiceDetails;
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

public class Service extends ConnectionMaster {
      @SuppressWarnings("unused")
//    private static final Logger logger = Logger.getLogger(ConnectionMaster.class.getName());
      private static final Logger LOG = LoggerFactory.getLogger(Service.class);
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
            // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
            EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
            if (PROPCode != null) {

                  dailyval = PROPCode.getPropval();
                  // //Loggers.general().info(LOG,"ADDDailyTxnLimit -------->" +
                  // PROPCode.getPropval());
            } else {
                  //// Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            String eventStatus = getDriverWrapper().getEventFieldAsText("EVSS", "s", "");

            // Loggers.general().info(LOG,"Event status in service class====>" +
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
                        // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

                  }

                  String step_csm = getDriverWrapper().getEventFieldAsText("CSID", "s", "").trim();
                  String stepid = getDriverWrapper().getEventFieldAsText("CSTD", "s", "").trim();
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "Event step id====>" + step_csm);
                  }

//                EBG Field
                  try {
                        String stateCode = getPane().getSTATECOD();
                        if (!stateCode.isEmpty()) {
                              double stampDuty = getStateWiseStampDuty(stateCode);
                              // getWrapper().setEBGSTAMPDUTYCurrency("INR");

                              // getWrapper().setEBGSTAMPDUTY(getDriverWrapper().convertFromToDBFormat(stampDuty,
                              // "INR", "T") + " " + "INR");

                              getPane().setEBGSTAMPDUTY(stampDuty + " " + "INR");

                              System.out.println("The state code is ::: " + stateCode + " Stamp Amount is ::: " + stampDuty);
                        }
                  } catch (Exception e) {

                        e.getStackTrace();
                  }

//                DMS field
                  try {
                        String getDmsreferncelink = getDmslink();

                        ExtendedHyperlinkControlWrapper Dmslink = getPane().getCtlDMSReferenceIMPLCclayHyperlink();
                        Dmslink.setUrl(getDmsreferncelink);

                        ExtendedHyperlinkControlWrapper Dmslink1 = getPane().getCtlDMSReferenceILCAMDclayHyperlink();
                        Dmslink1.setUrl(getDmsreferncelink);

                        ExtendedHyperlinkControlWrapper Dmslink2 = getPane().getCtlDMSReferenceIMPLCCLAIMRECclayHyperlink();
                        Dmslink2.setUrl(getDmsreferncelink);

                        ExtendedHyperlinkControlWrapper Dmslink3 = getPane().getCtlDMSReferenceILCSETTclayHyperlink();
                        Dmslink3.setUrl(getDmsreferncelink);

                        ExtendedHyperlinkControlWrapper Dmslink4 = getPane().getCtlDMSReferenceILCCORESSlay_2_2Hyperlink();
                        Dmslink4.setUrl(getDmsreferncelink);

                        System.out.println("getDmsreferncelink exception Service class " + getDmsreferncelink);
                        Loggers.general().info(LOG, "getDmsreferncelink " + getDmsreferncelink);

                  } catch (Exception e) {
                        System.out.println("getDmsreferncelink exception Service class " + e.getStackTrace());
                        e.getStackTrace();
                  }

                  System.out.println("DMS FIELD in service class onPostInitialise");

//                            end DMS link

//                            DMS repo field
                  try {
                        String getDmsreferncelink = getRepolink();

                        ExtendedHyperlinkControlWrapper Dmslink = getPane().getCtlDMSCUSTREPOIMPLCclayHyperlink();
                        Dmslink.setUrl(getDmsreferncelink);

                        ExtendedHyperlinkControlWrapper Dmslink1 = getPane().getCtlDMSCUSTREPOILCAMDclayHyperlink();
                        Dmslink1.setUrl(getDmsreferncelink);

                        ExtendedHyperlinkControlWrapper Dmslink2 = getPane().getCtlDMSCUSTREPOIMPLCCLAIMRECclayHyperlink();
                        Dmslink2.setUrl(getDmsreferncelink);

                        ExtendedHyperlinkControlWrapper Dmslink3 = getPane().getCtlDMSCUSTREPOILCSETTclayHyperlink();
                        Dmslink3.setUrl(getDmsreferncelink);

                        ExtendedHyperlinkControlWrapper Dmslink4 = getPane().getCtlDMSCUSTREPOILCCORESSlay_2_2Hyperlink();
                        Dmslink4.setUrl(getDmsreferncelink);

                        System.out.println("getDmsReporeferncelink  exception Service class " + getDmsreferncelink);
                        Loggers.general().info(LOG, "getDmsreferncelink " + getDmsreferncelink);

                  } catch (Exception e) {
                        System.out.println("getDmsReporeferncelink exception Service class " + e.getStackTrace());
                        e.getStackTrace();
                  }

                  System.out.println("DMS repo FIELD in service class onPostInitialise");

//                                        end DMS repo link

                  // WORKFLOW CHECKLIST CSM

                  try {
                        String Hyperreferel12 = getWorkflow().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack1 = getPane().getCtlCSMCHECKLISTELCADVclayHyperlink();
                        csmreftrack1.setUrl(Hyperreferel12);

//                      ExtendedHyperlinkControlWrapper csmreftrackamd1 = getPane().getCtlCSMCHECKLISTELCDPclayHyperlink();
//                      csmreftrackamd1.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrackamd2 = getPane().getCtlCSMCHECKLISTELCADJclayHyperlink();
                        csmreftrackamd2.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrackamd4 = getPane().getCtlCSMCHECKLISTELCAMDclayHyperlink();
                        csmreftrackamd4.setUrl(Hyperreferel12);

//                      ExtendedHyperlinkControlWrapper csmreftrackamd6 = getPane().getCtlCSMCHECKLISTELCSETTclayHyperlink();
//                      csmreftrackamd6.setUrl(Hyperreferel12);

                        // -------------ODC START------------------------------------

                        ExtendedHyperlinkControlWrapper cpcreftrackamd8 = getPane()
                                    .getCtlCSMCHECKLISTOUTDOCCOLADJclayHyperlink();
                        cpcreftrackamd8.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrackamd10 = getPane()
                                    .getCtlCSMCHECKLISTOUTDOCCOLAMDclayHyperlink();
                        cpcreftrackamd10.setUrl(Hyperreferel12);

                        // -------------ODC END------------------------------------

                        // ---IDC----

                        ExtendedHyperlinkControlWrapper cpcreftrackamd12 = getPane()
                                    .getCtlCSMCHECKLISTINWDOCCOLPAYclayHyperlink();
                        cpcreftrackamd12.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrackamd14 = getPane()
                                    .getCtlCSMCHECKLISTINWDOCCOLADJclayHyperlink();
                        cpcreftrackamd14.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrackamd16 = getPane()
                                    .getCtlCSMCHECKLISTINWDOCCOLAMDclayHyperlink();
                        cpcreftrackamd16.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrack17 = getPane().getCtlCSMCHECKLISTIMPLCclayHyperlink();
                        csmreftrack17.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrackamd18 = getPane().getCtlCSMCHECKLISTILCAMDclayHyperlink();
                        csmreftrackamd18.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrackamd19 = getPane().getCtlCSMCHECKLISTIMPADVclayHyperlink();
                        csmreftrackamd19.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrackamd20 = getPane()
                                    .getCtlCSMCHECKLISTIMPLCCLAIMRECclayHyperlink();
                        csmreftrackamd20.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrackamd21 = getPane().getCtlCSMCHECKLISTILCSETTclayHyperlink();
                        csmreftrackamd21.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrackamd22 = getPane().getCtlCSMCHECKLISTILCCANHyperlink();
                        csmreftrackamd22.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrackamd23 = getPane().getCtlCSMCHECKLISTEGTADJclayHyperlink();
                        csmreftrackamd23.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrackamd24 = getPane().getCtlCSMCHECKLISTEGTAMDclayHyperlink();
                        csmreftrackamd24.setUrl(Hyperreferel12);

                        // ---IDC----

                  } catch (Exception ees) {
                        Loggers.general().info(LOG, "Exception WORKFLOW CHECKLIST===>" + ees.getMessage());
                  }

                  // For CR-143 Limit Nodes
                  try {
                        String HyperLimitNode = getLimitNode().trim();
                        ExtendedHyperlinkControlWrapper limitnode1 = getPane().getCtlUnavailablelimitnodesIMPLCclayHyperlink();
                        limitnode1.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode2 = getPane().getCtlUnavailablelimitnodesILCAMDclayHyperlink();
                        limitnode2.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode3 = getPane().getCtlUnavailablelimitnodesIMPADVclayHyperlink();
                        limitnode3.setUrl(HyperLimitNode);
//                      ExtendedHyperlinkControlWrapper limitnode4 = getPane().getCtlUnavailablelimitnodesELCADVclayHyperlink();
//                      limitnode4.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode5 = getPane().getCtlUnavailablelimitnodesELCAMDclayHyperlink();
                        limitnode5.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode6 = getPane().getCtlUnavailablelimitnodesELCADJclayHyperlink();
                        limitnode6.setUrl(HyperLimitNode);
//                      ExtendedHyperlinkControlWrapper limitnode7 = getPane().getCtlUnavailablelimitnodesELCDPclayHyperlink();
//                      limitnode7.setUrl(HyperLimitNode);
//                      ExtendedHyperlinkControlWrapper limitnode8 = getPane().getCtlUnavailablelimitnodesELCSETTclayHyperlink();
//                      limitnode8.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode9 = getPane().getCtlUnavailablelimitnodesEGTAMDclayHyperlink();
                        limitnode9.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode10 = getPane()
                                    .getCtlUnavailablelimitnodesEGTADJclayHyperlink();
                        limitnode10.setUrl(HyperLimitNode);
                  } catch (Exception e) {
                        // System.out.println("For Limit Node"+e.getMessage());

                  }

                  // REFERE TRACKING CSM
                  try {

                        String Hyperreferel = getReferealtracking().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack = getPane().getCtlCSMREFRALIMPLCclayHyperlink();
                        csmreftrack.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackamd = getPane().getCtlCSMREFRALILCAMDclayHyperlink();
                        csmreftrackamd.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackamd1 = getPane().getCtlCSMREFRALIMPADVclayHyperlink();
                        csmreftrackamd1.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackamd3 = getPane().getCtlCSMREFRALIMPLCCLAIMRECclayHyperlink();
                        csmreftrackamd3.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackamd5 = getPane().getCtlCSMREFRALILCSETTclayHyperlink();
                        csmreftrackamd5.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackamd7 = getPane().getCtlCSMREFRALILCCANHyperlink();
                        csmreftrackamd7.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackk1 = getPane().getCtlCSMREFRALELCADVclayHyperlink();
                        csmreftrackk1.setUrl(Hyperreferel);

//                      ExtendedHyperlinkControlWrapper csmreftrackk3 = getPane().getCtlCSMREFRALELCDPclayHyperlink();
//                      csmreftrackk3.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackk5 = getPane().getCtlCSMREFRALELCADJclayHyperlink();
                        csmreftrackk5.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackk7 = getPane().getCtlCSMREFRALELCAMDclayHyperlink();
                        csmreftrackk7.setUrl(Hyperreferel);

//                      ExtendedHyperlinkControlWrapper csmreftrackk9 = getPane().getCtlCSMREFRALELCSETTclayHyperlink();
//                      csmreftrackk9.setUrl(Hyperreferel);

                        // -------------ODC START------------------------------------

                        ExtendedHyperlinkControlWrapper cpcreftrackamd7 = getPane().getCtlCSMREFRALOUTDOCCOLADJclayHyperlink();
                        cpcreftrackamd7.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper cpcreftrackamd11 = getPane().getCtlCSMREFRALOUTDOCCOLAMDclayHyperlink();
                        cpcreftrackamd11.setUrl(Hyperreferel);

                        // -------------ODC END------------------------------------

                        // ---IDC----

                        ExtendedHyperlinkControlWrapper cpcreftrackamd18 = getPane().getCtlCSMREFRALINWDOCCOLPAYclayHyperlink();
                        cpcreftrackamd18.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper cpcreftrackamd14 = getPane().getCtlCSMREFRALINWDOCCOLADJclayHyperlink();
                        cpcreftrackamd14.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper cpcreftrackamd16 = getPane().getCtlCSMREFRALINWDOCCOLAMDclayHyperlink();
                        cpcreftrackamd16.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackamd23 = getPane().getCtlCSMREFRALEGTADJclayHyperlink();
                        csmreftrackamd23.setUrl(Hyperreferel);
                        ExtendedHyperlinkControlWrapper csmreftrackamd24 = getPane().getCtlCSMREFRALEGTAMDclayHyperlink();
                        csmreftrackamd24.setUrl(Hyperreferel);

                  } catch (Exception ees) {
                        Loggers.general().info(LOG, "REFERE TRACKING--------->" + ees.getMessage());
                  }

                  // Workflow CBS
                  try {
                        String Hyperreferel12 = getWorkflowCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack1 = getPane().getCtlCPCCHECKELCADVclayHyperlink();
                        cpcreftrack1.setUrl(Hyperreferel12);
//                      ExtendedHyperlinkControlWrapper cpcreftrackamd1 = getPane().getCtlCPCCHECKELCDPclayHyperlink();
//                      cpcreftrackamd1.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd3 = getPane().getCtlCPCCHECKELCADJclayHyperlink();
                        cpcreftrackamd3.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd5 = getPane().getCtlCPCCHECKELCAMDclayHyperlink();
                        cpcreftrackamd5.setUrl(Hyperreferel12);
//                      ExtendedHyperlinkControlWrapper cpcreftrackamd7 = getPane().getCtlCPCCHECKELCSETTclayHyperlink();
//                      cpcreftrackamd7.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd9 = getPane().getCtlCPCCHECKOUTDOCCOLADJclayHyperlink();
                        cpcreftrackamd9.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd11 = getPane().getCtlCPCCHECKOUTDOCCOLAMDclayHyperlink();
                        cpcreftrackamd11.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd13 = getPane().getCtlCPCCHECKINWDOCCOLPAYclayHyperlink();
                        cpcreftrackamd13.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd15 = getPane().getCtlCPCCHECKINWDOCCOLADJclayHyperlink();
                        cpcreftrackamd15.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd17 = getPane().getCtlCPCCHECKINWDOCCOLAMDclayHyperlink();
                        cpcreftrackamd17.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack21 = getPane().getCtlCPCCHECKIMPLCclayHyperlink();
                        cpcreftrack21.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd22 = getPane().getCtlCPCCHECKILCAMDclayHyperlink();
                        cpcreftrackamd22.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd23 = getPane().getCtlCPCCHECKIMPADVclayHyperlink();
                        cpcreftrackamd23.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd24 = getPane().getCtlCPCCHECKIMPLCCLAIMRECclayHyperlink();
                        cpcreftrackamd24.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd25 = getPane().getCtlCPCCHECKILCSETTclayHyperlink();
                        cpcreftrackamd25.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd26 = getPane().getCtlCPCCHECKILCCANHyperlink();
                        cpcreftrackamd26.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrackamd23 = getPane().getCtlCPCCHECKEGTADJclayHyperlink();
                        csmreftrackamd23.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrackamd24 = getPane().getCtlCPCCHECKEGTAMDclayHyperlink();
                        csmreftrackamd24.setUrl(Hyperreferel12);

                  } catch (Exception ees) {
                        Loggers.general().info(LOG, "WORKFLOW CHECKLIST WorkflowCBS--------->" + ees.getMessage());
                  }

                  // Refereal CBS

                  try {
                        String Hyperreferel12 = getReferealtrackingCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack = getPane().getCtlCPCREFERELIMPLCclayHyperlink();
                        cpcreftrack.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd = getPane().getCtlCPCREFERELILCAMDclayHyperlink();
                        cpcreftrackamd.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd2 = getPane().getCtlCPCREFERELIMPADVclayHyperlink();
                        cpcreftrackamd2.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd4 = getPane()
                                    .getCtlCPCREFERELIMPLCCLAIMRECclayHyperlink();
                        cpcreftrackamd4.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd6 = getPane().getCtlCPCREFERELILCSETTclayHyperlink();
                        cpcreftrackamd6.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd8 = getPane().getCtlCPCREFERELILCCANHyperlink();
                        cpcreftrackamd8.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrackk2 = getPane().getCtlCPCREFERELELCADVclayHyperlink();
                        csmreftrackk2.setUrl(Hyperreferel12);
//                      ExtendedHyperlinkControlWrapper csmreftrackk4 = getPane().getCtlCPCREFERELELCDPclayHyperlink();
//                      csmreftrackk4.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrackk6 = getPane().getCtlCPCREFERELELCADJclayHyperlink();
                        csmreftrackk6.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrackk8 = getPane().getCtlCPCREFERELELCAMDclayHyperlink();
                        csmreftrackk8.setUrl(Hyperreferel12);
//                      ExtendedHyperlinkControlWrapper csmreftrackk10 = getPane().getCtlCPCREFRALELCSETTclayHyperlink();
//                      csmreftrackk10.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackam = getPane().getCtlCPCREFERELOUTDOCCOLADJclayHyperlink();
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
                        ExtendedHyperlinkControlWrapper csmreftrackamd23 = getPane().getCtlCPCREFERELEGTADJclayHyperlink();
                        csmreftrackamd23.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrackamd24 = getPane().getCtlCPCREFERELEGTAMDclayHyperlink();
                        csmreftrackamd24.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        Loggers.general().info(LOG, "REFERE TRACKING REFERE CBS--------->" + ees.getMessage());
                  }
                  // FDenquiry link

                  try {

                        String FDenquiryLink = FDenquiry();
                        ExtendedHyperlinkControlWrapper fdlink = getPane().getCtlFDENQUIRYIMPLCclayHyperlink();
                        fdlink.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink1 = getPane().getCtlFDENQUIRYILCAMDclayHyperlink();
                        fdlink1.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink2 = getPane().getCtlFDENQUIRYIMPADVclayHyperlink();
                        fdlink2.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink3 = getPane().getCtlFDENQUIRYIMPLCCLAIMRECclayHyperlink();
                        fdlink3.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink4 = getPane().getCtlFDENQUIRYILCSETTclayHyperlink();
                        fdlink4.setUrl(FDenquiryLink);

                  } catch (Exception e) {
                        Loggers.general().info(LOG, "Exception FDenquiry link" + e.getMessage());
                  }

                  // TST
                  try {
                        String val = "DMSTS";
                        String TSTHyperlink = Link(val);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              // Loggers.general().info(LOG,"Transaction link called in service class");
                        }
                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlTSTIMPLCclayHyperlink();
                        dmsh.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlTSTIMPADVclayHyperlink();
                        dmsh1.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh2 = getPane().getCtlTSTILCAMDclayHyperlink();
                        dmsh2.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh3 = getPane().getCtlTSTOUTDOCCOLEXPclayHyperlink();
                        dmsh3.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh4 = getPane().getCtlTSTOUTDOCCOLADJclayHyperlink();
                        dmsh4.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh5 = getPane().getCtlTSTOUTDOCCOLAMDclayHyperlink();
                        dmsh5.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper payment = getPane().getCtlTSTINWDOCCOLPAYclayHyperlink();
                        // //Loggers.general().info(LOG,"Passing link to inw col payment" +
                        // payment);
                        payment.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh7 = getPane().getCtlTSTINWDOCCOLADJclayHyperlink();
                        dmsh7.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh8 = getPane().getCtlTSTINWDOCCOLAMDclayHyperlink();
                        dmsh8.setUrl(TSTHyperlink);
//                      ExtendedHyperlinkControlWrapper dmsh9 = getPane().getCtlTSTELCADVclayHyperlink();
//                      dmsh9.setUrl(TSTHyperlink);
//                      ExtendedHyperlinkControlWrapper dmsha = getPane().getCtlTSTELCDPclayHyperlink();
//                      dmsha.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmshb = getPane().getCtlTSTELCAMDclayHyperlink();
                        dmshb.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmshC = getPane().getCtlTSTELCADJclayHyperlink();
                        dmshC.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmshD = getPane().getCtlTSTFINEXPLCADJclayHyperlink();
                        dmshD.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmshE = getPane().getCtlTSTFINEXPLCREPclayHyperlink();
                        dmshE.setUrl(TSTHyperlink);
//                      ExtendedHyperlinkControlWrapper dmshF = getPane().getCtlTSTELCSETTclayHyperlink();
//                      dmshF.setUrl(TSTHyperlink);

                        ExtendedHyperlinkControlWrapper dmsh10 = getPane().getCtlTSTIGTINVOCclayHyperlink();
                        dmsh10.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh11 = getPane().getCtlTSTEGTAMDclayHyperlink();
                        dmsh11.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh12 = getPane().getCtlTSTEGTADJclayHyperlink();
                        dmsh12.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh13 = getPane().getCtlTSTEXPLCcanHyperlink();
                        dmsh13.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh14 = getPane().getCtlTSTELCCORRlayHyperlink();
                        dmsh14.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh15 = getPane().getCtlTSTELCMAINTAINLayHyperlink();
                        dmsh15.setUrl(TSTHyperlink);

                        ExtendedHyperlinkControlWrapper dmsh16 = getPane().getCtlTSTILCCANHyperlink();
                        dmsh16.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh17 = getPane().getCtlTSTILCEXPHyperlink();
                        dmsh17.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh18 = getPane().getCtlTSTILCMAINLayHyperlink();
                        dmsh18.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh19 = getPane().getCtlTSTIMPSTDLCCORRESlayHyperlink();
                        dmsh19.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh20 = getPane().getCtlTSTIMPLCCLAIMRECclayHyperlink();
                        dmsh20.setUrl(TSTHyperlink);

                        ExtendedHyperlinkControlWrapper dmsh21 = getPane().getCtlTSTILCSETTclayHyperlink();
                        dmsh21.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh22 = getPane().getCtlTSTILCCORESSlayHyperlink();
                        dmsh22.setUrl(TSTHyperlink);

                        ExtendedHyperlinkControlWrapper dmsh23 = getPane().getCtlTSTILCMANULlayHyperlink();
                        dmsh23.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh24 = getPane().getCtlTSTILCBENCANlayHyperlink();
                        dmsh24.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh25 = getPane().getCtlTSTILCBENAMDlayHyperlink();
                        dmsh25.setUrl(TSTHyperlink);

                  } catch (Exception ees) {
                        Loggers.general().info(LOG, "Exception Transaction link called in service" + ees.getMessage());
                  }

                  // LINK PASSING

                  try {
                        String val = "BOE";
                        String getBoeLink1 = getBoeLink(val);

                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlBOEILCSETTclayHyperlink();
                        dmsh.setUrl(getBoeLink1);
                        // //Loggers.general().info(LOG,"ILC outclaim ");
                        ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlBOEINWDOCCOLPAYclayHyperlink();
                        dmsh1.setUrl(getBoeLink1);
                        // //Loggers.general().info(LOG,"IDC PAYMENT ");
                        ExtendedHyperlinkControlWrapper dmsh2 = getPane().getCtlBOEOUTDOCCOLEXPclayHyperlink();
                        dmsh2.setUrl(getBoeLink1);
                        // //Loggers.general().info(LOG," ODC Expire ");
                        ExtendedHyperlinkControlWrapper dmsh3 = getPane().getCtlBOEELCEXPIREclayHyperlink();
                        dmsh3.setUrl(getBoeLink1);
                        // //Loggers.general().info(LOG," ELC Expire ");
                        ExtendedHyperlinkControlWrapper dmsh4 = getPane().getCtlBOEIMPLCCLAIMRECclayHyperlink();
                        dmsh4.setUrl(getBoeLink1);
                        // //Loggers.general().info(LOG," ILC claim received ");
                  } catch (Exception e) {
                        Loggers.general().info(LOG, "BOE link called in service" + e.getMessage());
                  }

                  // InwardLink
                  try {
                        String HyperInward = getINWARDREM();
                        ExtendedHyperlinkControlWrapper InwardLink = getPane().getCtlINWARDELCDPclayHyperlink();
                        InwardLink.setUrl(HyperInward);
                        ExtendedHyperlinkControlWrapper InwardLink1 = getPane().getCtlINWARDELCSETTclayHyperlink();
                        InwardLink1.setUrl(HyperInward);

                  } catch (Exception ees) {
                        Loggers.general().info(LOG, "Exception Inward Link ===>" + ees.getMessage());
                  }
                  // Get Packing Credit A/c Outstanding //200523 //vishal g
                  try {
                        String HyperInward = getACCOUNT();
                        ExtendedHyperlinkControlWrapper InwardLink = getPane().getCtlACCOUNTELCDPclayHyperlink();
                        InwardLink.setUrl(HyperInward);
                        ExtendedHyperlinkControlWrapper InwardLink1 = getPane().getCtlACCOUNTELCSETTclayHyperlink();
                        InwardLink1.setUrl(HyperInward);

                  } catch (Exception ees) {
                        ees.printStackTrace();
                        Loggers.general().info(LOG, "Exception Inward Link ===>" + ees.getMessage());
                  }

                  // WRITEOFF
                  try {
                        String val5 = "WRITEOFF";
                        String writeoff = Link(val5);
                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlWriteOffOUTDOCCOLEXPclayHyperlink();// ODC
                                                                                                                                                                  // Expire
                        dmsh.setUrl(writeoff);
                        ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlWriteOffELCEXPIREclayHyperlink();// Elc
                                                                                                                                                                  // Expire
                        dmsh1.setUrl(writeoff);
                        ExtendedHyperlinkControlWrapper dmsh2 = getPane().getCtlWriteOffELCEXPIREclayHyperlink();// Elc
                                                                                                                                                                  // Expire
                        dmsh2.setUrl(writeoff);

                  } catch (Exception e) {
                        Loggers.general().info(LOG, "Exception WRITEOFF===>" + e.getMessage());
                  }

                  // PRESHIPMENT}

                  try {

                        // String Preshipment = getHyperPreshipment();
                        String Preshipment = getHyperPreshipment();
                        ExtendedHyperlinkControlWrapper dmsh5 = getPane().getCtlPreshipmentELCSETTclayHyperlink();
                        dmsh5.setUrl(Preshipment);
                        // //Loggers.general().info(LOG,"dmsh5----->" + dmsh5);

                  } catch (Exception e) {
                        Loggers.general().info(LOG, "Exception Preshipment link" + e.getMessage());
                  }

                  try {

                        String getHyperSFMS = getIFSCSEARCH();
                        ExtendedHyperlinkControlWrapper ifscilc = getPane().getCtlSFMSIMPLCclayHyperlink();
                        ifscilc.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc1 = getPane().getCtlSFMSILCAMDclayHyperlink();
                        ifscilc1.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc2 = getPane().getCtlSFMSILCCANHyperlink();
                        ifscilc2.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc3 = getPane().getCtlSFMSILCCORESSlayHyperlink();
                        ifscilc3.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc4 = getPane().getCtlSFMSILCEXPHyperlink();
                        ifscilc4.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc5 = getPane().getCtlSFMSILCISSTAKEclayHyperlink();
                        ifscilc5.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc6 = getPane().getCtlSFMSILCMAINLayHyperlink();
                        ifscilc6.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc7 = getPane().getCtlSFMSILCSETTclayHyperlink();
                        ifscilc7.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc8 = getPane().getCtlSFMSIMPADVclayHyperlink();
                        ifscilc8.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc9 = getPane().getCtlSFMSIMPADVclayHyperlink();
                        ifscilc9.setUrl(getHyperSFMS);
//                      ExtendedHyperlinkControlWrapper ifscilc10 = getPane().getCtlSFMSIMPLCCLAIMRECclayHyperlink();
//                      ifscilc10.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc11 = getPane().getCtlSFMSILCMAINLayHyperlink();
                        ifscilc11.setUrl(getHyperSFMS);

                        // ELC
                        ExtendedHyperlinkControlWrapper sfmsExpAdvc = getPane().getCtlSFMSELCAMDclayHyperlink();
                        sfmsExpAdvc.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdvD = getPane().getCtlSFMSELCADJclayHyperlink();
                        sfmsExpAdvD.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv6 = getPane().getCtlSFMSELCEXPIREclayHyperlink();
                        sfmsExpAdv6.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv7 = getPane().getCtlSFMSELCADVclayHyperlink();
                        sfmsExpAdv7.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv8 = getPane().getCtlSFMSECLADVTAKEclayHyperlink();
                        sfmsExpAdv8.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv9 = getPane().getCtlSFMSELCCORRlayHyperlink();
                        sfmsExpAdv9.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv10 = getPane().getCtlSFMSELCDPclayHyperlink();
                        sfmsExpAdv10.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv11 = getPane().getCtlSFMSELCSETTclayHyperlink();
                        sfmsExpAdv11.setUrl(getHyperSFMS);

                        // Export guarantee
                        ExtendedHyperlinkControlWrapper sfmsExpAdv12 = getPane().getCtlSFMSIGTISSclayHyperlink();
                        sfmsExpAdv12.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv13 = getPane().getCtlSFMSEGTADJclayHyperlink();
                        sfmsExpAdv13.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv14 = getPane().getCtlSFMSEGTAMDclayHyperlink();
                        sfmsExpAdv14.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv15 = getPane().getCtlSFMSIGTINVOCclayHyperlink();
                        sfmsExpAdv15.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv16 = getPane().getCtlSFMSEGTCORRESlayHyperlink();

                        // IDC and ODC
                        ExtendedHyperlinkControlWrapper sfmsExpAdv19 = getPane().getCtlSFMSINWDOCCOLADJclayHyperlink();
                        sfmsExpAdv19.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv20 = getPane().getCtlSFMSINWDOCCOLAMDclayHyperlink();
                        sfmsExpAdv20.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv21 = getPane().getCtlSFMSINWDOCCOLPAYclayHyperlink();
                        sfmsExpAdv21.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv22 = getPane().getCtlSFMSOUTDOCCOLADJclayHyperlink();
                        sfmsExpAdv22.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv23 = getPane().getCtlSFMSOUTDOCCOLAMDclayHyperlink();
                        sfmsExpAdv23.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv24 = getPane().getCtlSFMSOUTDOCCOLEXPclayHyperlink();
                        sfmsExpAdv24.setUrl(getHyperSFMS);

                  } catch (Exception e) {
                        Loggers.general().info(LOG, "Exception IFSCSEARCH" + e.getMessage());
                  }

                  if (!step_csm.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {

                        try {
                              if (getMajorCode().equalsIgnoreCase("ELC") && getMinorCode().equalsIgnoreCase("POD")
                                          && (!step_csm.equalsIgnoreCase("Final print")
                                                      && !eventStatus.equalsIgnoreCase("Completed"))) {

                                    getPane().onNOSTROELCSETTclayButton();

                              }
                        } catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Exception in nostro button---->" + e.getMessage());

                              }
                        }

                        String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim(); // ISS001
                        String prd_typ = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();
                        String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
                        String step_Input = getDriverWrapper().getEventFieldAsText("CSTY", "s", "");
                        String step_log = getDriverWrapper().getEventFieldAsText("ECOI", "s", "").trim();
                        // //Loggers.general().info(LOG,"Step id for log CSM---->" + step_log);
                        String step_input = getDriverWrapper().getEventFieldAsText("CSID", "s", "").trim();
                        //// Loggers.general().info(LOG,"step id check for CSM ----->" +
                        //// step_input);
                        String gateway = getDriverWrapper().getEventFieldAsText("FRGI", "l", "");
                        //// Loggers.general().info(LOG,"gateway flag checking----->" +
                        //// gateway);

                        if (gateway.equalsIgnoreCase("Y")) {
                              getPane().setPERADV("");
                              getPane().setADVREC("");
                              getPane().setNETRECIV("");
                              getPane().setBILLPAY("");
                        }

                        // Loggers.general().info(LOG,"todo on forcelim");
                        // Loggers.general().info(LOG,"stepinpu" +step_input);
                        if ((step_input.equalsIgnoreCase("Create") || step_input.equalsIgnoreCase("CSM"))
                                    && (getMinorCode().equalsIgnoreCase("POD"))) {
                              // Loggers.general().info(LOG,"Get force
                              // limit"+getPane().getFORLIM());

                              getPane().setFORLIM(false);
                              // Loggers.general().info(LOG,"After force
                              // limit"+getPane().getFORLIM());
                        }

                        if ((step_input.equalsIgnoreCase("Create") || step_input.equalsIgnoreCase("CSM"))
                                    && (getMinorCode().equalsIgnoreCase("POD") || getMinorCode().equalsIgnoreCase("POC"))) {
                              // //Loggers.general().info(LOG,"Step id for log Create if for log
                              // step---->" + step_log);
                              // Loggers.general().info(LOG,"Inside if 21/01/2019");
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

                        } else {
                              // Loggers.general().info(LOG,"Step id for else step---->" +
                              // step_log);
                              // Loggers.general().info(LOG,"Step input for else step---->" +
                              // step_input);
                        }

                        String rtgsFlag = getDriverWrapper().getEventFieldAsText("cBPJ", "l", "");
                        if (rtgsFlag.equalsIgnoreCase("N") && !rtgsFlag.equalsIgnoreCase("Y")) {
                              // Loggers.general().info(LOG,"inside if");
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

                        if ((step_input.equalsIgnoreCase("Create") || step_input.equalsIgnoreCase("CSM"))
                                    && (getMinorCode().equalsIgnoreCase("POD") || getMinorCode().equalsIgnoreCase("POC"))
                                    && (!gateway.equalsIgnoreCase("Y"))) {
                              // Loggers.general().info(LOG,"inside second if");
                              /*
                               * getPane().setNOSAMT(""); getPane().setNOSTOUT("");
                               */
                              getPane().setEBRCFLAG("");

                              /*
                               * getPane().setNOSTRM(""); getPane().setNOSTMT(""); getPane().setNOSTAMT("");
                               * getPane().setNOSTDAT("");
                               *
                               * getPane().setPOOLAMT(""); getPane().setNOSTACC(""); getPane().setINWMSG("");
                               * getPane().setMTMESG("");
                               */ }

                        try {

                              String kotak_no = getDriverWrapper().getEventFieldAsText("ISS", "p", "cu").trim();
                              // Loggers.general().info(LOG,"kotak Ownlc tick--------->" +
                              // kotak_no);

                              if (getMajorCode().equalsIgnoreCase("ELC")) {
                                    if ((kotak_no.equalsIgnoreCase("INKKBK") || kotak_no.trim().equalsIgnoreCase("11908474"))) {
                                          getPane().setOWNLC(true);
                                    } else {
                                          getPane().setOWNLC(false);
                                          /*
                                           * if (dailyval_Log.equalsIgnoreCase("YES")) { //
                                           * Loggers.general().info(LOG,"kotak Ownlc tick // else------->" + kotak_no); }
                                           */
                                    }
                              } else {
                                    getPane().setOWNLC(false);

                              }
                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Exception kotak systemOutput--------->" +
                              // e.getMessage());
                        }

                        if (getMajorCode().equalsIgnoreCase("ELC")) {
                              try {

                                    getactualPenalRate();

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"Exception update" +
                                    // e.getMessage());
                              }
                        }

                        if (getMajorCode().equalsIgnoreCase("EGT")) {
                              try {

                                    getclaimExpiryDateISB();

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"Exception update" +
                                    // e.getMessage());
                              }
                        }

                        String gateWayVal = getDriverWrapper().getEventFieldAsText("FRSI", "l", "");
                        if (getMinorCode().equalsIgnoreCase("POD")
                                    && (gateWayVal.equalsIgnoreCase("Y") || !gateWayVal.equalsIgnoreCase("N"))) {
                              try {
                                    getbillNumber();
                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"Exception update" +
                                    // e.getMessage());
                              }
                        }

                        if (getMajorCode().equalsIgnoreCase("ILC")) {

                              getPane().onINSURENCEIMPLCclayButton();

                              getPeriodicAdv();
                        }

                        if (getMajorCode().equalsIgnoreCase("ELC")) {
                              getPane().onPRESHIPFINELCDPclayButton();
                              getPane().onPRESHIPFINELCSETTclayButton();

                              getPane().getExtEventLoanDetailsNew().setEnabled(false);
                              getPane().getExtEventLoanDetailsDelete().setEnabled(false);
                              getPane().getExtEventLoanDetailsUpdate().setEnabled(false);
                              getPane().getExtEventLoanDetailsUp().setEnabled(false);
                              getPane().getExtEventLoanDetailsDown().setEnabled(false);
                        }

                        String finace_event = getDriverWrapper().getEventFieldAsText("BFC", "l", "");
                        if (getMinorCode().equalsIgnoreCase("DOP")) {
                              if (finace_event.equalsIgnoreCase("Y")) {
                                    getPane().setTENORDET("DPR");
                                    getWrapper().setTENORDET("DPR");
                              } else {
                                    getPane().setTENORDET("NO");
                                    getWrapper().setTENORDET("NO");
                              }
                        } else if (getMinorCode().equalsIgnoreCase("POD")) {

                              if (finace_event.equalsIgnoreCase("Y")) {
                                    getPane().setTENORDET("POD");
                                    getWrapper().setTENORDET("POD");
                              }

                        }

                        // amount to be collect

                        if (getMajorCode().equalsIgnoreCase("ELC")
                                    || getMajorCode().equalsIgnoreCase("ODC") && (getMinorCode().equalsIgnoreCase("DOP")
                                                || getMinorCode().equalsIgnoreCase("POD") || getMinorCode().equalsIgnoreCase("CLP"))) {
                              try {
                                    // Loggers.general().info(LOG,"Net amount===="+getMinorCode());
                                    // getnetAmount();
                                    getnetAmountOdcPay();
                              } catch (Exception e) {

                              }
                        }

                        if (getMajorCode().equalsIgnoreCase("IDC") && getMinorCode().equalsIgnoreCase("NCAM")) {
                              try {

                                    getcountryCode();

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"Exception update" +
                                    // e.getMessage());
                              }

                        }

                        if (getMajorCode().equalsIgnoreCase("ILC")) {

                              String chargeVal = getDriverWrapper().getEventFieldAsText("APP", "p", "cAEF");
                              /*
                               * if (dailyval_Log.equalsIgnoreCase("YES")) {
                               *
                               * // Loggers.general().info(LOG,"Customer level charge value" + // chargeVal);
                               * }
                               */
                              getPane().setCHRAMT(chargeVal);

                        }

                        // Notional rate population
                        if (step_Input.equalsIgnoreCase("i")) {
                              try {
                                    List<ExtEventShippingTable> shipTable = (List<ExtEventShippingTable>) getWrapper()
                                                .getExtEventShippingTable();
                                    // //Loggers.general().info(LOG,"shipping table for notional
                                    // rate---->" + shipTable.size());
                                    for (int i = 0; i < shipTable.size(); i++) {

                                          ExtEventShippingTable ship = shipTable.get(i);
                                          if (getMajorCode().equalsIgnoreCase("ELC") && getMinorCode().equalsIgnoreCase("DOP")) {
                                                BigDecimal shipAmt = new BigDecimal("0");
                                                BigDecimal notVal = new BigDecimal("1");
                                                ship.setREPAYAM(shipAmt);
                                                // Loggers.general().info(LOG,"repayamount
                                                // 1===>"+ship.getREPAYAM());

                                                // Loggers.general().info(LOG,"repayamount===>
                                                // 1===>"+shipAmt);
                                                ship.setREPAYAMCurrency(ship.getSHPAMTCurrency());
                                                ship.setSHCOLAM(shipAmt);
                                                ship.setSHCOLAMCurrency(ship.getSHPAMTCurrency());
                                                ship.setNOTIONAL(notVal);
                                          }
                                          String not_str = ship.getNOTIONAL().toString();
                                          // //Loggers.general().info(LOG,"shipping table for notional
                                          // length====>" + not_str.length());

                                          if (getMinorCode().equalsIgnoreCase("POD")) {
                                                double notional = 1;
                                                BigDecimal rAmt = ship.getREPAYAM();
                                                String shiamtcuy = ship.getSHPAMTCurrency();
                                                String reyamtcuy = ship.getREPAYAMCurrency();
                                                try {
                                                      con = getConnection();
                                                      String query = "SELECT ETT_SPOTRATE_CAL('" + shiamtcuy + "','" + reyamtcuy
                                                                  + "') FROM DUAL";
                                                      //// Loggers.general().info(LOG,"Notional rate
                                                      //// function
                                                      //// " +
                                                      //// query);

                                                      ps1 = con.prepareStatement(query);
                                                      rs1 = ps1.executeQuery();
                                                      if (rs1.next()) {
                                                            notional = rs1.getDouble(1);
                                                            // Loggers.general().info(LOG,"shipping table
                                                            // notional
                                                            // length start===>" + notional);
                                                            // ship.setNOTIONAL(new
                                                            // BigDecimal(notional));
                                                      }
                                                } catch (Exception e1) {
                                                      // Loggers.general().info(LOG,"Exception Notional
                                                      // rate
                                                      // function" + e1.getMessage());
                                                }

                                                String temp_notRate = String.valueOf(notional);

                                                if (null != not_str && not_str.equalsIgnoreCase("1")) {
                                                      ship.setNOTIONAL(new BigDecimal(temp_notRate));
                                                      //// Loggers.general().info(LOG,"shipping table
                                                      //// notional
                                                      //// length if loop====>" +
                                                      //// ship.getNOTIONAL());

                                                      BigDecimal notional_big = new BigDecimal(notional);
                                                      BigDecimal equi_bill = notional_big.multiply(rAmt);
                                                      //// Loggers.general().info(LOG,"Notional rate +
                                                      //// Repament
                                                      //// amount in big decimal POD---->" +
                                                      //// equi_bill);
                                                      ship.setEQUBILL(equi_bill);
                                                      ship.setEQUBILLCurrency(ship.getSHPAMTCurrency());
                                                } else if (null != not_str && !temp_notRate.equalsIgnoreCase(not_str)) {
                                                      ship.setNOTIONAL(new BigDecimal(not_str));
                                                      //// Loggers.general().info(LOG,"shipping table
                                                      //// notional
                                                      //// length else if loop====>" +
                                                      //// ship.getNOTIONAL());
                                                      BigDecimal notional_big = new BigDecimal(not_str);
                                                      BigDecimal equi_bill = notional_big.multiply(rAmt);
                                                      //// Loggers.general().info(LOG,"Notional rate +
                                                      //// Repament
                                                      //// amount in big decimal POD---->" +
                                                      //// equi_bill);
                                                      ship.setEQUBILL(equi_bill);
                                                      ship.setEQUBILLCurrency(ship.getSHPAMTCurrency());

                                                } else {
                                                      //// Loggers.general().info(LOG,"Notional rate is
                                                      //// blank---->");
                                                }

                                          }

                                    }

                              } catch (Exception e) {
                                    //// Loggers.general().info(LOG,"Exception in validating for
                                    //// notional
                                    //// rate--->" + e.getMessage());
                              }
                        }

                        String tenstart = getDriverWrapper().getEventFieldAsText("TNRF", "s", "").trim();
                        // //Loggers.general().info(LOG,"Tenor start in dop----> " + tenstart);
                        if (tenstart.length() > 0 && (getMinorCode().equalsIgnoreCase("DOP")
                                    || getMinorCode().equalsIgnoreCase("CRC") || getMinorCode().equalsIgnoreCase("POD"))) {
                              // //Loggers.general().info(LOG,"Tenor start in if loop----> " +
                              // tenstart);
                              getPane().setTENSTRT(tenstart);
                              getWrapper().setTENSTRT(tenstart);
                        } else {
                              getPane().setTENSTRT("");
                              // //Loggers.general().info(LOG,"Tenor start in else loop----> " +
                              // tenstart);
                        }

//                      try {
//                            currencyCalc();
//                      } catch (Exception e) {
//                            if (dailyval_Log.equalsIgnoreCase("YES")) {
//                            //    Loggers.general().info(LOG,"Exception in currency value population---->" + e.getMessage());
//
//                            }
//                      }

                        if (getMajorCode().equalsIgnoreCase("ELC")) {
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

                        try {
                              getPerdChgEndDate();

                        } catch (Exception ee) {
                              //// Loggers.general().info(LOG,ee.getMessage());
                              // //Loggers.general().info(LOG,"LOB Catch");
                        }

                        try {

                              getGoodsCategory();

                        } catch (Exception ee) {
                              //// Loggers.general().info(LOG,ee.getMessage());
                              // //Loggers.general().info(LOG,"LOB Catch");
                        }

                        // Non Constituent borrower
                        String constituent = getDriverWrapper().getEventFieldAsText("BEN", "p", "no").trim();
                        // //Loggers.general().info(LOG,"Non Constituent borrower
                        // constituent--------->" + constituent);
                        if (constituent.length() > 0) {
                              // //Loggers.general().info(LOG,"Non Constituent borrower
                              // constituent--------->" + constituent);
                              String alpha_val = constituent.replaceAll("[^A-Za-z]+", "");
                              // //Loggers.general().info(LOG,"alpha_val---->" + alpha_val);
                              if (alpha_val.length() > 0) {
                                    String alpha_fin = alpha_val.substring(0, 4);
                                    // //Loggers.general().info(LOG,"Non Constituent borrower
                                    // constituent
                                    // after convert--------->" + alpha_fin);
                                    if (getMajorCode().equalsIgnoreCase("ELC")) {
                                          if (alpha_fin.equalsIgnoreCase("LCUS")) {
                                                getPane().setCONSBORR(true);
                                                // //Loggers.general().info(LOG," Non Constituent
                                                // borrower
                                                // ELC
                                                // if
                                                // loop--------->");
                                          } else {
                                                getPane().setCONSBORR(false);
                                          }
                                    } else {
                                          getPane().setCONSBORR(false);
                                          // //Loggers.general().info(LOG," Non Constituent borrower
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

                              // //Loggers.general().info(LOG," Non Constituent borrower is
                              // empty--------->");
                        }

                        // c
                        if (prd_typ.equalsIgnoreCase("ELD")) {
                              EventPane pane = (EventPane) getPane();
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
                              // //Loggers.general().info(LOG,"Product type is forign");
                              EventPane pane = (EventPane) getPane();
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

                        // Notional due date elc new
                        try {
                              if (getMajorCode().equalsIgnoreCase("ELC")) {
                                    getNotionalDueDateELC();
                              }
                        } catch (Exception e1) {
                              // Loggers.general().info(LOG,"Exception in NotionalDueDateELC" +
                              // e1.getMessage());
                        }
                        // Notional due date ILC new

                        try {
                              if (getMajorCode().equalsIgnoreCase("ILC")) {
                                    getNotionalDueDateILC();
                              }
                        } catch (Exception e1) {
                              // Loggers.general().info(LOG,"Exception in NotionalDueDate ILC" +
                              // e1.getMessage());
                        }

                        try {
                              if (getMajorCode().equalsIgnoreCase("ILC")) {
                                    getChargeBasis();
                              }
                              // //Loggers.general().info(LOG,"getChargeBasis");
                              getPane().onBENIFSCELCSETTclayButton();
                              getPane().onBENIFSCINWDOCCOLPAYclayButton();

                        } catch (Exception ee) {
                              // Loggers.general().info(LOG,ee.getMessage());
                              // //Loggers.general().info(LOG,"Service TAX Catch");
                        }

                        try {
                              // //Loggers.general().info(LOG,"getChargeBasis");
                              if (getMajorCode().equalsIgnoreCase("ILC") && getMinorCode().equalsIgnoreCase("NAMI")) {

                                    String newLia = getDriverWrapper().getEventFieldAsText("cAAX", "v", "m");
                                    String oldLia = getDriverWrapper().getEventFieldAsText("cACP", "v", "m");
                                    String newCur = getDriverWrapper().getEventFieldAsText("cAAX", "v", "c");
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          // Loggers.general().info(LOG,"New total liability" +
                                          // newLia);
                                          // Loggers.general().info(LOG,"Old total liability" +
                                          // oldLia);
                                    }
                                    BigDecimal newVal = new BigDecimal(newLia);
                                    BigDecimal oldVal = new BigDecimal(oldLia);

                                    BigDecimal newValue = newVal.subtract(oldVal);
                                    ConnectionMaster connectionMaster = new ConnectionMaster();
                                    String divideByDecimal = connectionMaster.getDecimalforCur(newCur);
                                    if (divideByDecimal.equalsIgnoreCase("1")) {

                                          DecimalFormat diff = new DecimalFormat("0");
                                          diff.setMaximumFractionDigits(1);
                                          String totalVal = diff.format(newValue);
                                          /*
                                           * if (dailyval_Log.equalsIgnoreCase("YES")) {
                                           * Loggers.general().info(LOG,"New Total liability final value<==1==>" +
                                           * totalVal + "" + newCur); }
                                           */
                                          if (newValue.compareTo(BigDecimal.ZERO) > 0) {
                                                getPane().setINTLIAMT(totalVal + " " + newCur);
                                          } else {
                                                totalVal = "0";
                                                getPane().setINTLIAMT(totalVal + " " + newCur);

                                          }

                                    }

                                    else if (divideByDecimal.equalsIgnoreCase("1000")) {
                                          DecimalFormat diff = new DecimalFormat("0.000");
                                          diff.setMaximumFractionDigits(3);
                                          String totalVal = diff.format(newValue);
                                          /*
                                           * if (dailyval_Log.equalsIgnoreCase("YES")) { //
                                           * Loggers.general().info(LOG,"New Total liability final //
                                           * value<==3==>" + totalVal + "" + newCur); }
                                           */
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
                                           * Loggers.general().info(LOG,"New Total liability final //
                                           * value<==2==>" + totalVal + "" + newCur); }
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
                              Loggers.general().info(LOG, "Exception New total liability" + e.getMessage());

                        }

                        String goods_prod = getDriverWrapper().getEventFieldAsText("PUL1", "s", "");
                        // //Loggers.general().info(LOG,"Goods category" + goods_prod);
                        if (getMajorCode().equalsIgnoreCase("ILC") && getMinorCode().equalsIgnoreCase("CRC")) {
                              getPane().setGOODCAT(goods_prod);
                              // //Loggers.general().info(LOG,"Goods category" +
                              // getPane().getGOODCAT());
                        }

                        if (getMajorCode().equalsIgnoreCase("ELC") && getMinorCode().equalsIgnoreCase("POD")) {
                              EventPane pane = (EventPane) getPane();
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
                        }
                        if (getMajorCode().equalsIgnoreCase("ELC") && getMinorCode().equalsIgnoreCase("POD")) {
                              List<ExtEventShippingTable> shipcol = (List<ExtEventShippingTable>) getWrapper()
                                          .getExtEventShippingTable();
                              if (shipcol.size() > 0) {
                                    // //Loggers.general().info(LOG,"shipdetails --->" + shipcol);
                                    EventPane pane = (EventPane) getPane();
                                    pane.getExtEventShippingTableDelete().setEnabled(false);
                              }
                        }

                        // get from and to fields to shipping details
                        // if ((getMajorCode().equalsIgnoreCase("ELC") ||
                        // getMinorCode().equalsIgnoreCase("DOP"))
                        // || (getMajorCode().equalsIgnoreCase("ILC") ||
                        // getMinorCode().equalsIgnoreCase("CRC"))) {
                        if (getMajorCode().equalsIgnoreCase("ILC") && getMinorCode().equalsIgnoreCase("CRC")
                                    && step_csm.equalsIgnoreCase("SCRUTINIZER1")) {
                              try {
                                    boolean negative = false;
                                    // //Loggers.general().info(LOG,"entered from and to population
                                    // ");

                                    String receipt = getDriverWrapper().getEventFieldAsText("SDF", "s", "");
                                    // //Loggers.general().info(LOG,"from city" + Fromcity);
                                    String destination = getDriverWrapper().getEventFieldAsText("SDT", "s", "");
                                    // //Loggers.general().info(LOG,"to city " + Tocity);
                                    String gooddesc = getDriverWrapper().getEventFieldAsText("GDCD", "s", "");
                                    // //Loggers.general().info(LOG,"goods description" + gooddesc);
                                    String goodcode = getDriverWrapper().getEventFieldAsText("GDC", "s", "");
                                    String loading = getDriverWrapper().getEventFieldAsText("PLAD", "s", "");
                                    // //Loggers.general().info(LOG,"from city" + Fromcity);
                                    String discharge = getDriverWrapper().getEventFieldAsText("PDAD", "s", "");
                                    String incoterm = getDriverWrapper().getEventFieldAsText("INCO", "s", "");
                                    String harmonized = getDriverWrapper().getEventFieldAsText("cAUH", "s", "");
                                    String Originofgoods = getDriverWrapper().getEventFieldAsText("cAHF", "s", "");
                                    String InsuranceCompany = getDriverWrapper().getEventFieldAsText("cAAC", "s", "");
                                    String InsurancePolicyNumber = getDriverWrapper().getEventFieldAsText("cADE", "s", "");
                                    String Insurancestartdate = getDriverWrapper().getEventFieldAsText("cASA", "d", "");
                                    String InsuranceFrom = getDriverWrapper().getEventFieldAsText("cADY", "s", "");
                                    String Address = getDriverWrapper().getEventFieldAsText("cASY", "s", "");
                                    String AmountInsured = getDriverWrapper().getEventFieldAsText("cADD", "v", "m");
                                    String AmountInsuredccy = getDriverWrapper().getEventFieldAsText("cADD", "v", "c");
                                    String PayableAt = getDriverWrapper().getEventFieldAsText("cBWW", "s", "");
                                    String InsuranceEndDate = getDriverWrapper().getEventFieldAsText("cADX", "d", "");
                                    String InsuranceTo = getDriverWrapper().getEventFieldAsText("cADZ", "s", "");
                                    String CurrencyandAmount = getDriverWrapper().getEventFieldAsText("cATB", "s", "");
                                    String PremiumCurrencyAmount = getDriverWrapper().getEventFieldAsText("cATC", "s", "");
                                    String CaptialGoods = getDriverWrapper().getEventFieldAsText("cBWX", "s", "");
                                    String LicenseNumber = getDriverWrapper().getEventFieldAsText("cARY", "s", "");
                                    String LicenseExpiryDate = getDriverWrapper().getEventFieldAsText("cBWY", "d", "");
                                    String NegativeList = getDriverWrapper().getEventFieldAsText("cAIO", "l", "");
                                    String insurby = getDriverWrapper().getEventFieldAsText("cBWZ", "s", "");
                                    String insurencepct = getDriverWrapper().getEventFieldAsText("cBXA", "s", "");
                                    String hspolicy = getDriverWrapper().getEventFieldAsText("cBIQ", "s", "");
                                    if (NegativeList.equalsIgnoreCase("Y")) {
                                          negative = true;
                                    }

                                    // getPane().setDESGOODS(gooddesc);
                                    getPane().setGOODT(goodcode);
                                    getPane().setPLCOFREC(receipt);
                                    getPane().setPLCDESTI(destination);
                                    getPane().setPOLOADES(loading);
                                    getPane().setPORTDESC(discharge);
                                    getPane().setINCOTER(incoterm);
                                    getPane().setHSCODE(harmonized);
                                    getPane().setCONDESP(Originofgoods);
                                    getPane().setINSCO(InsuranceCompany);
                                    getPane().setPOLNO_Name(InsurancePolicyNumber);
                                    getPane().setPOLICYDT(Insurancestartdate);
                                    getPane().setINSUFROM(InsuranceFrom);
                                    getPane().setADDRESS(Address);
                                    getPane().setPOLCYAMT(AmountInsured + " " + AmountInsuredccy);
                                    getPane().setPAYAB_Name(PayableAt);
                                    getPane().setEXIPR_Name(InsuranceEndDate);
                                    getPane().setINSURATO(InsuranceTo);
                                    getPane().setINSURENC(CurrencyandAmount);
                                    getPane().setPREMIUM(PremiumCurrencyAmount);
                                    getPane().setBENEFI(CaptialGoods);
                                    getPane().setLICENDTL(LicenseNumber);
                                    getPane().setLICENDAT(LicenseExpiryDate);
                                    getPane().setFEMADEC(insurby);
                                    getPane().setEXPAGENC(insurencepct);
                                    getPane().setNEGATIVE(negative);
                                    getPane().setHSPOLICY(hspolicy);
                                    getPane().setORGIN_Name("");
                                    getPane().setCOSIGCOU("");
                                    getPane().setPORLOD("");
                                    getPane().setTRANSPOR("");
                                    getPane().setCCNTRDTL("");
                                    getPane().setVESSEL("");
                                    getPane().setTRNDOCNO("");
                                    getPane().setIBAPPTRN("");
                                    getPane().setREPPURSL("");
                                    getPane().setPORTCOD_Name("");
                                    getPane().setDASHIP_Name("");
                                    getPane().setOPURPOS_Name("");
                                    getPane().setDESGOODS("");
                                    System.out.println("value of goods code" + goodcode + " " + negative + " " + CaptialGoods + " "
                                                + insurby + " " + insurencepct);
                                    // getPane().setSHIPFMLC(Fromcity);
                                    // getPane().setSHIPTOLC(Tocity);

                                    List<ExtEventInvoicelc> liste = (List<ExtEventInvoicelc>) getWrapper().getExtEventInvoicelc();
                                    for (int k = 0; k < liste.size(); k++) {
                                          ExtEventInvoicelc val1 = liste.get(k);
                                          val1.setINNUM("");
                                          val1.setINDTE("");
                                          val1.setINVAM(null);
                                    }

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,e.getMessage());
                              }
                        }
                        if (getMajorCode().equalsIgnoreCase("ELC") && getMinorCode().equalsIgnoreCase("DOP")
                                    && step_csm.equalsIgnoreCase("SCRUTINIZER1")) {
                              try {
                                    String incoterm = getDriverWrapper().getEventFieldAsText("INCO", "s", "");
                                    String goodcode = getDriverWrapper().getEventFieldAsText("GDC", "s", "");
                                    getPane().setINCOTER(incoterm);
                                    getPane().setGOODT(goodcode);
                                    if (prd_typ.equalsIgnoreCase("41F")) {
                                          getPane().setNATTRPRD("25");
                                    }

                              } catch (Exception e) {
                                    e.printStackTrace();
                                    // System.out.println("outside BUYERS data:"+e);

                              }
                        }
                        if ((getMajorCode().equalsIgnoreCase("ILC") || getMajorCode().equalsIgnoreCase("IDC"))) {
                              try {
                                    getPane().getExtEventBOECAPUpdate().setEnabled(false);
                                    getPane().getExtEventBOECAPNew().setEnabled(false);
                                    getPane().getExtEventBOECAPDelete().setEnabled(false);
                                    getPane().getExtEventBOECAPUp().setEnabled(false);
                                    getPane().getExtEventBOECAPDown().setEnabled(false);
                              } catch (Exception e) {
                                    e.printStackTrace();
                                    // System.out.println("outside BUYERS data:"+e);

                              }
                        } else {
                              getPane().getExtEventBOECAPNew().setEnabled(true);
                        }
                        try {

                              if (getMajorCode().equalsIgnoreCase("IDC") && !getMinorCode().equalsIgnoreCase("CLP")) {
                                    String systemDate1 = getDriverWrapper().getEventFieldAsText("RVD", "d", "");
                                    // //Loggers.general().info(LOG,"Received date --->" +
                                    // systemDate);
                                    String accpt = getDriverWrapper().getEventFieldAsText("RELS", "s", "");
                                    // //Loggers.general().info(LOG,"ACCEPTANCE VALUE --->" +
                                    // accpt);
                                    int gra = 0;
                                    if (accpt.equalsIgnoreCase("Payment")) {
                                          SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                                          Calendar cal = Calendar.getInstance();

                                          // getPane().setNATTRPRD("25");
                                          String natt_set = getWrapper().getNATTRPRD().trim();

                                          if (natt_set.length() > 0) {
                                                gra = Integer.parseInt(natt_set);
                                          }
                                          try {
                                                cal.setTime(sdf.parse(systemDate1));
                                                // //Loggers.general().info(LOG,"expdate in
                                                // issue------->
                                                // ");
                                                cal.add(Calendar.DATE, gra);
                                                String output = sdf.format(cal.getTime());
                                                // //Loggers.general().info(LOG,"output----->" +
                                                // output);
                                                getPane().setSIGVALDT(output);
                                                getWrapper().setSIGVALDT(output);

                                          } catch (Exception e) {
                                                // Loggers.general().info(LOG,"Notional value date
                                                // payment--->"
                                                // + e.getMessage());
                                          }
                                    }

                                    else {
                                          getPane().setSIGVALDT("");

                                    }
                              } else {
                                    // Loggers.general().info(LOG,"majaor code is ODC --->" +
                                    // getMajorCode());

                              }

                        } catch (Exception e) {
                              Loggers.general().info(LOG, "majaor code is IDC--->" + e.getMessage());
                        }
                        String stepID = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                        // //Loggers.general().info(LOG,"prodCode for chacklist------------>" +
                        // stepID);

                        EventPane pane = (EventPane) getPane();
                        // pane.getExtEventBOECAPNew().setEnabled(true);

                        // hs code population

                        String hscodeval = getWrapper().getHMON();
                        // //Loggers.general().info(LOG,"hscode Value---->" + hscodeval);
                        if ((!hscodeval.equalsIgnoreCase("")) && hscodeval != null) {
                              try {
                                    // //Loggers.general().info(LOG,"hscode Value in try---->" +
                                    // hscodeval);
                                    String hyperValue = "SELECT trim(HSEXPPOY) FROM EXTHMCODE WHERE HCODEE='" + hscodeval + "'";
                                    // //Loggers.general().info(LOG,"Hs code query Value---->" +
                                    // hyperValue);
                                    con = ConnectionMaster.getConnection();
                                    ps1 = con.prepareStatement(hyperValue);
                                    rs = ps1.executeQuery();
                                    while (rs.next()) {
                                          String hsploy = rs.getString(1);
                                          // //Loggers.general().info(LOG,"Policy value---->" +
                                          // hsploy);
                                          getPane().setHSPOLY(hsploy);
                                    }

                                    // con.close();
                                    // ps1.close();
                                    // rs.close();
                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"exceptio is " + e);
                              } finally {
                                    try {

                                          if (rs != null)
                                                rs.close();
                                          if (ps1 != null)
                                                ps1.close();
                                          if (con != null)
                                                con.close();

                                    } catch (SQLException e) {
                                          // Loggers.general().info(LOG,"Connection Failed! Check
                                          // output
                                          // console");
                                          e.printStackTrace();
                                    }
                              }
                        } else {
                              // //Loggers.general().info(LOG,"HS code is empty for policy ");
                        }

                        String masterRefNumber = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                        String eventPrefix = getDriverWrapper().getEventFieldAsText("EVCD", "s", "");

                        //
                        String csty_id = getDriverWrapper().getEventFieldAsText("CSTY", "s", "");

                        String step_Id = "Input";

                        // -------------**************---------------

                        String mas = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                        // String = "0172ELFX0003716";
                        String evt = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
                        // String str ="";
                        if (getMinorCode().trim().equalsIgnoreCase("DOP")) {
                              try {
                                    // //Loggers.general().info(LOG,"enter into bill generate no");
                                    String str = mas.substring(4, 16);
                                    // //Loggers.general().info(LOG,"str---->" + str);
                                    String strevt = evt.substring(0, 1);
                                    // //Loggers.general().info(LOG,"strevt ---->" + strevt);

                                    String str11 = evt.substring(3, 6);
                                    // //Loggers.general().info(LOG,"str11 ---->" + str11);
                                    String val = str + strevt + str11;
                                    // //Loggers.general().info(LOG,"Total ---->" + val);
                                    getWrapper().setBLLREFNO(val);
                                    getPane().setBLLREFNO(val);
                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"exception" + e);
                              }
                        }
                        try {

                              String refer = getDriverWrapper().getEventFieldAsText("ISS", "r", "").trim();
                              getPane().setSENDREF(refer);
                              getWrapper().setSENDREF(refer);
                        } catch (Exception e) {
                              // Loggers.general().info(LOG,e.getMessage());

                        }

                        if (getMajorCode().trim().equalsIgnoreCase("ILC") && getMinorCode().trim().equalsIgnoreCase("CRC")) {
                              String mas1 = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                              // String = "0172ELFX0003716";
                              String evt1 = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
                              try {
                                    // //Loggers.general().info(LOG,"enter into bill generate no");
                                    // String str = mas1.substring(4, 16);
                                    // //Loggers.general().info(LOG,"str---->" + str);
                                    // String strevt = evt1.substring(0, 1);
                                    // //Loggers.general().info(LOG,"strevt ---->" + strevt);

                                    // String str11 = evt1.substring(3, 6);
                                    // //Loggers.general().info(LOG,"str11 ---->" + str11);
                                    // String val = str + strevt + str11;
                                    // //Loggers.general().info(LOG,"Total ---->" + val);
                                    getWrapper().setBLLREFNO(mas1);
                                    getPane().setBLLREFNO(mas1);
                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"exception" + e);
                              }
                        }

                        try {
                              // //Loggers.general().info(LOG,"get value for Subvention");
                              getSubvention();
                        } catch (Exception ee) {
                              // Loggers.general().info(LOG,ee.getMessage());
                              // //Loggers.general().info(LOG,"getSubvention");
                        } finally {
                              // //Loggers.general().info(LOG,"finally LOB ");
                        }

                        if (getMajorCode().trim().equalsIgnoreCase("ODC") && (getMinorCode().trim().equalsIgnoreCase("NCAJ")
                                    || getMinorCode().trim().equalsIgnoreCase("NCAM"))) {

                              if ((!prd_typ.equalsIgnoreCase("OCF")) || prd_typ.equalsIgnoreCase("OCI")) {

                                    System.out.println("Service clas: Disabling pane buttons");

                                    pane.getBtnFetchshipdetOUTDOCCOLADJclay().setEnabled(false);
                                    pane.getBtnFetchShipDetOUTDOCCOLAMDclay().setEnabled(false);
                                    pane.getBtnFetchInvDetOUTDOCCOLADJclay().setEnabled(false);
                                    pane.getBtnFetchInvDetOUTDOCCOLAMDclay().setEnabled(false);

                                    pane.getExtEventShippingCollectionsNew().setEnabled(false);
                                    pane.getExtEventShippingCollectionsDelete().setEnabled(false);
                                    pane.getExtEventShippingCollectionsUpdate().setEnabled(false);
                                    pane.getExtEventShippingdetailsNew().setEnabled(false);
                                    pane.getExtEventShippingdetailsDelete().setEnabled(false);
                                    pane.getExtEventShippingdetailsUpdate().setEnabled(false);
                                    pane.getExtEventInvoiceDetailsNew().setEnabled(false);
                                    pane.getExtEventInvoiceDetailsUpdate().setEnabled(false);
                                    pane.getExtEventInvoiceDetailsDelete().setEnabled(false);
                              } else {
                                    //// Loggers.general().info(LOG,"------------>" + getMajorCode()
                                    //// +
                                    //// "" + getMinorCode());
                              }

                        }

                        // get from and to fields to shipping details
                        if ((getMajorCode().equalsIgnoreCase("ELC") || getMinorCode().equalsIgnoreCase("DOP"))

                                    && csty_id.equalsIgnoreCase("i")) {
                              try {

                                    // //Loggers.general().info(LOG,"entered from and to population
                                    // ");

                                    String Fromcity = getDriverWrapper().getEventFieldAsText("SDF", "s", "");
                                    // //Loggers.general().info(LOG,"from city" + Fromcity);
                                    String Tocity = getDriverWrapper().getEventFieldAsText("SDT", "s", "");
                                    // //Loggers.general().info(LOG,"to city " + Tocity);
                                    String gooddesc = getDriverWrapper().getEventFieldAsText("GDCD", "s", "");
                                    // //Loggers.general().info(LOG,"goods description" + gooddesc);
                                    String goodcode = getDriverWrapper().getEventFieldAsText("GDC", "s", "");
//                                  getPane().setDESGOODS(gooddesc);
//                                  getPane().setGOODT(goodcode);
//                                  getPane().setSHIPFMLC(Fromcity);
//                                  getPane().setSHIPTOLC(Tocity);

                                    // //Loggers.general().info(LOG,"hscode Value in try---->" +
                                    // hscodeval);
                                    String hyperValue = "select lcm.TAX_PAY_BY,lcm.OUR_CHGS,lcm.OVR_CHGS,lcm.LOADING,lcm.DISCHARGE from master mas, lcmaster lcm where mas.KEY97 = lcm.KEY97 and mas.MASTER_REF  = '"
                                                + masterRefNumber + "'";
                                    // //Loggers.general().info(LOG,"TI field query Value---->" +
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
                                          // //Loggers.general().info(LOG,"port code description---->"
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
                                    // Loggers.general().info(LOG,e.getMessage());
                              } finally {
                                    try {

                                          if (rs != null)
                                                rs.close();
                                          if (ps1 != null)
                                                ps1.close();
                                          if (con != null)
                                                con.close();

                                    } catch (SQLException e) {
                                          //// Loggers.general().info(LOG,"Connection Failed! Check
                                          //// output
                                          //// console");
                                          e.printStackTrace();
                                    }
                              }
                        } else {
                              //// Loggers.general().info(LOG,"--------->" + getMajorCode() +
                              //// getMinorCode());

                        }

                        // Mixed tenor date calculation

                        String expdate = getDriverWrapper().getEventFieldAsText("EXP", "d", ""); // 12/10/16
                        // //Loggers.general().info(LOG,"Expiry date" + expdate);

                        try {
                              String graceda = "0";
                              graceda = getWrapper().getMIXTEN();
                              SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yy");
                              Calendar c = Calendar.getInstance();
                              int grace = 0;

                              if (((!graceda.equalsIgnoreCase("")) || graceda != null) && graceda.length() > 0) {

                                    try {
                                          if (graceda.length() > 0 && getMajorCode().equalsIgnoreCase("ILC")) {
                                                grace = Integer.parseInt(graceda);
                                                c.setTime(sdf1.parse(expdate));
                                                // //Loggers.general().info(LOG,"expdate in
                                                // issue------->
                                                // ");
                                                c.add(Calendar.DATE, grace);
                                                // //Loggers.general().info(LOG,"DATE 1"+ c);
                                                String output = sdf1.format(c.getTime());
                                                // //Loggers.general().info(LOG,output);
                                                getPane().setMIXDATE(output);
                                          }
                                    } catch (Exception ee) {
                                          // Loggers.general().info(LOG,"Mixed tenor period" +
                                          // ee.getMessage());
                                    }
                              } else {
                                    //// Loggers.general().info(LOG,"Mixed tenor period i empty" +
                                    //// graceda);
                              }
                        } catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Exception in Mixed tenor period---->" + e.getMessage());

                              }
                        }
                        // port of destination description

                        String portdes = getWrapper().getPORTDES();
                        // //Loggers.general().info(LOG,"Port of destination Value---->" +
                        // portdes);
                        if ((!portdes.equalsIgnoreCase("")) && portdes != null) {
                              try {
                                    String portvalqury = "select trim(PODEST),trim(COUNTRY) from EXTPORTDESTINATION WHERE PODEST='"
                                                + portdes + "'";
                                    // //Loggers.general().info(LOG,"port code destination query
                                    // Value---->" +
                                    // portvalqury);
                                    con = ConnectionMaster.getConnection();
                                    ps1 = con.prepareStatement(portvalqury);
                                    rs = ps1.executeQuery();
                                    while (rs.next()) {
                                          String hsploy = rs.getString(1);
                                          String COUNTRY = rs.getString(2);
                                          // //Loggers.general().info(LOG,"port code description---->"
                                          // +
                                          // hsploy);
                                          getPane().setPODESCP(hsploy);
                                          getPane().setPODESCON(COUNTRY);
                                    }

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"exception is " + e.getMessage());
                              } finally {
                                    try {

                                          if (rs != null)
                                                rs.close();
                                          if (ps1 != null)
                                                ps1.close();
                                          if (con != null)
                                                con.close();

                                    } catch (SQLException e) {
                                          // Loggers.general().info(LOG,"Connection Failed! Check
                                          // output
                                          // console");
                                          e.printStackTrace();
                                    }
                              }
                        } else {
                              // //Loggers.general().info(LOG,"port code is destination empty");
                        }

                        try {
                              getrbiPurcodeCode();

                        } catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Exception in Purpose code---->" + e.getMessage());

                              }
                        }

                        if (getMinorCode().equalsIgnoreCase("DOP") || getMinorCode().equalsIgnoreCase("POD")) {
                              try {
                                    getrtgsNEFT();

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"Exception in purpose
                                    // code--------->"
                                    // + e.getMessage());
                              }
                        }
                        // Start of Notional due date calculation new
                        if (getMajorCode().equalsIgnoreCase("ODC") && !getMinorCode().equalsIgnoreCase("CLP")) {
                              try {
                                    // //Loggers.general().info(LOG,"Notional due date is calling");
                                    getNotionalDueDate();
                              } catch (Exception ee) {
                                    // //Loggers.general().info(LOG,"Notional due date is
                                    // calling--->" +
                                    // ee.getMessage());
                                    // //Loggers.general().info(LOG,"LOB Catch");
                              }
                        } else {
                              //// Loggers.general().info(LOG,"product type is not ODC");
                        }

                        if (getMajorCode().equalsIgnoreCase("IDC")) {
                              try {
                                    // //Loggers.general().info(LOG,"Notional due date is calling");
                                    getNotionalDueDateIDC();
                              } catch (Exception ee) {
                                    //// Loggers.general().info(LOG,"Notional due date is
                                    //// calling--->" +
                                    //// ee.getMessage());
                                    // //Loggers.general().info(LOG,"LOB Catch");
                              }
                        }

                        String payaction = getDriverWrapper().getEventFieldAsText("PYAN", "s", "");
                        // Loggers.general().info(LOG,"payment Type action---->" + payaction);
                        String tranType = getWrapper().getTRANTYP();
                        // Loggers.general().info(LOG,"Write of Type---->" + tranType +
                        // "tranType---->" + tranType.length());
                        if (tranType.equalsIgnoreCase("WRITEOFF") && payaction.equalsIgnoreCase("Pay")
                                    && prd_typ.equalsIgnoreCase("ELF") && !step_Id.equalsIgnoreCase("CSM")) {
                              getPane().setEBRCFLAG("N");
                        } else if ((tranType.equalsIgnoreCase("") || !tranType.equalsIgnoreCase("WRITEOFF"))
                                    && !payaction.equalsIgnoreCase("Pay") && prd_typ.equalsIgnoreCase("ELF")
                                    && !step_Id.equalsIgnoreCase("CSM")) {
                              getPane().setEBRCFLAG("N");
                        } else if ((tranType.equalsIgnoreCase("") || !tranType.equalsIgnoreCase("WRITEOFF"))
                                    && payaction.equalsIgnoreCase("Pay") && prd_typ.equalsIgnoreCase("ELF")
                                    && !step_Id.equalsIgnoreCase("CSM")) {
                              getPane().setEBRCFLAG("Y");

                        } else if (step_Id.equalsIgnoreCase("CSM")) {
                              getPane().setEBRCFLAG("");
                        }

                        if ((step_input.equalsIgnoreCase("Create") || step_log.equalsIgnoreCase("Log")
                                    || step_input.equalsIgnoreCase("CSM")) && getMinorCode().equalsIgnoreCase("POD")) {
                              getPane().setEBRCFLAG("");

                        }

                        // //Loggers.general().info(LOG,"Repayment Amount pay action---->" +
                        // payaction);

                        // Setting the outstanding Amount

                        //// Loggers.general().info(LOG,"Event Status " + eventStatus);
                        if (getMinorCode().equalsIgnoreCase("POD")) {
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
                                                ExtEventShippingTable ship_Obj = shipingDetails.get(i);
                                                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy");
                                                ShipAmount = String.valueOf(ship_Obj.getSHPAMT()) + " " + ship_Obj.getSHPAMTCurrency();
                                                OUTSTANDINGAMOUNT = ShipAmount;

                                                shipbillnum = ship_Obj.getBILLNUM().trim();
                                                // //Loggers.general().info(LOG,"shipbillnum.length()---->"
                                                // +
                                                // shipbillnum.length());
                                                shipbillnum = setxxforparameters(shipbillnum.trim());
                                                // //Loggers.general().info(LOG,"Shipping Bill Number "
                                                // +
                                                // shipbillnum);
                                                shippValues.add(shipbillnum);
                                                SHIPBILLDATE = format.format(ship_Obj.getBILLDAT());
                                                // //Loggers.general().info(LOG,"Shipping Bill Date " +
                                                // SHIPBILLDATE);
                                                SHIPBILLDATE = setxxforparameters(SHIPBILLDATE.trim());
                                                // //Loggers.general().info(LOG,"Shipping Bill Date " +
                                                // SHIPBILLDATE);
                                                shippValues.add(SHIPBILLDATE);
                                                if (shipbillnum.length() > 1) {

                                                      // //Loggers.general().info(LOG,"Shipping Bill
                                                      // Number "
                                                      // +
                                                      // shipbillnum);

                                                      String outStanding_Amnt = "SELECT OUTSAMT ,SHIPCCY FROM ETT_EDPMS_SHP_OUTSAMT WHERE SHIPBILLNUM='"
                                                                  + shipbillnum + "' and SHIPBILLDATE='" + SHIPBILLDATE + "'";
                                                      //// Loggers.general().info(LOG, "Repayment Amount
                                                      //// query
                                                      //// outStanding_Amnt if loop----->" +
                                                      //// outStanding_Amnt);
                                                      con = ConnectionMaster.getConnection();
                                                      ship_prepare = con.prepareStatement(outStanding_Amnt);
                                                      ship_result = ship_prepare.executeQuery();
                                                      // //Loggers.general().info(LOG,"Query
                                                      // executeQuery");
                                                      while (ship_result.next()) {

                                                            outAmount = ship_result.getDouble("OUTSAMT");
                                                            outstr = ship_result.getString("OUTSAMT");
                                                            //// Loggers.general().info(LOG,"Outstanding
                                                            //// amount
                                                            //// while loop------>" + outAmount);

                                                            if (outAmount > 0) {
                                                                  // //Loggers.general().info(LOG,"outAmount
                                                                  // is if
                                                                  // loop" + outAmount);
                                                                  currency = ship_result.getString(2);
                                                                  // //Loggers.general().info(LOG,"currency is
                                                                  // " +
                                                                  // currency);
                                                                  // outStandingAmount =
                                                                  // String.valueOf(outAmount / 10);
                                                                  OUTSTANDINGAMOUNT = outstr + " " + currency;
                                                                  // //Loggers.general().info(LOG,"Out Amount
                                                                  // is"
                                                                  // +
                                                                  // OUTSTANDINGAMOUNT);
                                                                  ship_Obj.setColumn("LOUTSAMT", OUTSTANDINGAMOUNT);
                                                                  if (payaction.equalsIgnoreCase("Pay")) {
                                                                        // ship_Obj.setColumn("REPAYAM",
                                                                        // OUTSTANDINGAMOUNT);
                                                                  } else {

                                                                        String repout = 0 + " " + currency;
                                                                        //// Loggers.general().info(LOG,"New
                                                                        //// Repayment Amount else loop"
                                                                        //// +
                                                                        //// repout);
                                                                        ship_Obj.setColumn("REPAYAM", repout);
                                                                        ship_Obj.setColumn("EQUBILL", repout);
                                                                  }
                                                            } else {
                                                                  //// Loggers.general().info(LOG,"outAmount
                                                                  //// is
                                                                  //// else loop" + outAmount);
                                                                  currency = ship_result.getString(2);
                                                                  // //Loggers.general().info(LOG,"currency is
                                                                  // " +
                                                                  // currency);
                                                                  // outStandingAmount =
                                                                  // String.valueOf(outAmount / 10);
                                                                  OUTSTANDINGAMOUNT = 0 + " " + currency;
                                                                  // Loggers.general().info(LOG,"Out Amount
                                                                  // else"
                                                                  // + OUTSTANDINGAMOUNT);
                                                                  ship_Obj.setColumn("LOUTSAMT", OUTSTANDINGAMOUNT);
                                                                  ship_Obj.setColumn("REPAYAM", OUTSTANDINGAMOUNT);
                                                                  ship_Obj.setColumn("EQUBILL", OUTSTANDINGAMOUNT);
                                                                  // //Loggers.general().info(LOG,"Repayment
                                                                  // Amount
                                                                  // collection else loop" +
                                                                  // OUTSTANDINGAMOUNT);

                                                            }

                                                            //// Loggers.general().info(LOG,"Repayment
                                                            //// Amount
                                                            //// collection if loop" +
                                                            //// OUTSTANDINGAMOUNT);
                                                      }
                                                }

                                                else {
                                                      //// Loggers.general().info(LOG,"Repayment Amount
                                                      //// collection else loop----->");
                                                      PORTCODE = ship_Obj.getPORTCODDD();
                                                      // //Loggers.general().info(LOG,"Port Code " +
                                                      // PORTCODE);
                                                      PORTCODE = setxxforparameters(PORTCODE.trim());
                                                      // //Loggers.general().info(LOG,"Port Code " +
                                                      // PORTCODE);
                                                      shippValues.add(PORTCODE);
                                                      FORMNO = ship_Obj.getFORMNUM();
                                                      // //Loggers.general().info(LOG,"Form No" + FORMNO);
                                                      FORMNO = setxxforparameters(FORMNO.trim());
                                                      // //Loggers.general().info(LOG,"Form No" + FORMNO);
                                                      shippValues.add(FORMNO);
                                                      String outStanding_Amnt = "SELECT OUTSAMT ,SHIPCCY FROM ETT_EDPMS_SHP_OUTSAMT WHERE SHIPBILLNUM='"
                                                                  + shipbillnum + "' and SHIPBILLDATE='" + SHIPBILLDATE + "' AND PORTCODE='"
                                                                  + PORTCODE + "' AND FORMNO='" + FORMNO + "'";
                                                      //// Loggers.general().info(LOG,"Repayment Amount
                                                      //// query
                                                      //// outStanding_Amnt else loop----->" +
                                                      //// outStanding_Amnt);
                                                      con = ConnectionMaster.getConnection();
                                                      ship_prepare = con.prepareStatement(outStanding_Amnt);
                                                      ship_result = ship_prepare.executeQuery();
                                                      while (ship_result.next()) {

                                                            outAmount = ship_result.getDouble("OUTSAMT");
                                                            outstr = ship_result.getString("OUTSAMT");

                                                            // //Loggers.general().info(LOG,"Outstanding
                                                            // amount
                                                            // while loop for form no------>" +
                                                            // outAmount);

                                                            if (outAmount > 0) {
                                                                  // //Loggers.general().info(LOG,"outAmount
                                                                  // is if
                                                                  // loop" + outAmount);
                                                                  currency = ship_result.getString(2);
                                                                  // //Loggers.general().info(LOG,"currency is
                                                                  // " +
                                                                  // currency);
                                                                  // outStandingAmount =
                                                                  // String.valueOf(outAmount / 10);
                                                                  OUTSTANDINGAMOUNT = outstr + " " + currency;
                                                                  //// Loggers.general().info(LOG,"Out Amount
                                                                  //// " +
                                                                  //// OUTSTANDINGAMOUNT);
                                                                  ship_Obj.setColumn("LOUTSAMT", OUTSTANDINGAMOUNT);
                                                                  if (payaction.equalsIgnoreCase("Pay")) {
                                                                        // stem.out.println("Repayment
                                                                        // Amount CURRENCY
                                                                        // Checking=======>"
                                                                        // + OUTSTANDINGAMOUNT);

                                                                        // ship_Obj.setColumn("REPAYAM",
                                                                        // OUTSTANDINGAMOUNT);
                                                                  } else {

                                                                        String repout = 0 + " " + currency;
                                                                        //// Loggers.general().info(LOG,"New
                                                                        //// Repayment Amount else loop"
                                                                        //// +
                                                                        //// repout);
                                                                        ship_Obj.setColumn("REPAYAM", repout);
                                                                        ship_Obj.setColumn("EQUBILL", repout);
                                                                  }
                                                            } else {
                                                                  // //Loggers.general().info(LOG,"outAmount
                                                                  // is
                                                                  // else loop" + outAmount);
                                                                  currency = ship_result.getString(2);
                                                                  // //Loggers.general().info(LOG,"currency is
                                                                  // " +
                                                                  // currency);
                                                                  // outStandingAmount =
                                                                  // String.valueOf(outAmount / 10);
                                                                  OUTSTANDINGAMOUNT = 0 + " " + currency;
                                                                  // //Loggers.general().info(LOG,"Out Amount
                                                                  // " +
                                                                  // OUTSTANDINGAMOUNT);
                                                                  ship_Obj.setColumn("LOUTSAMT", OUTSTANDINGAMOUNT);
                                                                  ship_Obj.setColumn("REPAYAM", OUTSTANDINGAMOUNT);
                                                                  ship_Obj.setColumn("EQUBILL", OUTSTANDINGAMOUNT);

                                                            }

                                                      }
                                                }
                                          } else {
                                                //// Loggers.general().info(LOG,"Event Status is
                                                //// Completed");
                                          }
                                    }

                              } catch (Exception e) {
                                    //// Loggers.general().info(LOG,"Exception in setting outsing
                                    //// amount
                                    //// " + e.getMessage());
                              } finally {
                                    try {

                                          if (ship_result != null)
                                                ship_result.close();
                                          if (ship_prepare != null)
                                                ship_prepare.close();
                                          if (con != null)
                                                con.close();

                                    } catch (SQLException e) {
                                          // Loggers.general().info(LOG,"Connection Failed! Check
                                          // output
                                          // console");
                                          e.printStackTrace();
                                    }
                              }
                        } else {
                              //// Loggers.general().info(LOG,"Product type for outstanding
                              //// amount-->"
                              //// + getMinorCode());
                        }

                        if (getMinorCode().equalsIgnoreCase("POD")) {
                              try {

                                    String step_Input1 = getDriverWrapper().getEventFieldAsText("CSTY", "s", "");
                                    // //Loggers.general().info(LOG,"step_Input check ----->" +
                                    // step_Input);
                                    // //Loggers.general().info(LOG,"step id check for CSM ----->" +
                                    // step_csm);
                                    String eventStatus1 = getDriverWrapper().getEventFieldAsText("EVSS", "s", "");

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
                                                String outStanding_str = String.valueOf(outStandingAmt);
                                                // //Loggers.general().info(LOG,"Outstanding Amount " +
                                                // outStandingAmt);
                                                String outStanding = ship.getLOUTSAMT().toString();
                                                outStandingAmtCurr = ship.getLOUTSAMTCurrency();
                                                // //Loggers.general().info(LOG,"Shipping Bill Number is
                                                // " +
                                                // ShipBillNo);
                                                Date ShipDate = ship.getBILLDAT();
                                                String billDate = "";
                                                SimpleDateFormat date = new SimpleDateFormat("dd-MM-yy");
                                                if (ShipDate != null) {
                                                      billDate = date.format(ShipDate);
                                                }
                                                String formNo = "";
                                                formNo = ship.getFORMNUM();
                                                // //Loggers.general().info(LOG,"Form nO is " + formNo);
                                                String portCode = "";
                                                portCode = ship.getPORTCODDD();
                                                // //Loggers.general().info(LOG,"Port Code is " +
                                                // portCode);
                                                ShipBillNo = setxxforparameters(ShipBillNo);
                                                billDate = setxxforparameters(billDate);
                                                formNo = setxxforparameters(formNo);
                                                portCode = setxxforparameters(portCode);
                                                // //Loggers.general().info(LOG,"Ship Bill Amount " +
                                                // ShipBillNo +
                                                // "Bill Date " + billDate + "Form No " + formNo
                                                // +
                                                // "portCode
                                                // " + portCode);
                                                String query = "select billamt AS BILL,billequcurr AS BCURR,repayamt AS REP,repaycurr AS RCURR,REPAYTYPE as TYPE,SHORTAMT as SHORT,SHORTCURR as SHORTCURR,REASON as REASON,NOTIONAL AS NOTIONAL from ETT_EDPMS_ALLSHP_REPAYAMT where master_ref='"
                                                            + master_refNo + "' and eventrefno='" + EventRefNo + "' and shipbillnum='"
                                                            + ShipBillNo + "' AND shipbilldate='" + billDate + "' and portcode='" + portCode
                                                            + "' and formno='" + formNo + "'";
                                                Loggers.general().info(LOG, "Resetting amount Query is " + query);
                                                pres = conne.prepareStatement(query);
                                                resSet = pres.executeQuery();
                                                if (resSet.next()) {

                                                      if (resSet.getDouble("REP") > 0 && payaction.equalsIgnoreCase("Pay")) {
                                                            //// Loggers.general().info(LOG,"Resetting REP
                                                            //// value
                                                            //// else if for loop currency==>>" +
                                                            //// resSet.getString("RCURR"));
                                                            ship.setREPAYAM(new BigDecimal(resSet.getString("REP")));
                                                            // Loggers.general().info(LOG,"repayamount"+ship.getREPAYAM());
                                                            String resSetCCY = ship.getREPAYAMCurrency().toString();
                                                            //// Loggers.general().info(LOG,"Getting the
                                                            //// repayment currency---->" +
                                                            //// resSetCCY);
                                                            ship.setREPAYAMCurrency(resSetCCY);
                                                      } else {
                                                            String reystr = "0";
                                                            ship.setREPAYAM(new BigDecimal(Double.valueOf(reystr)));
                                                            Loggers.general().info(LOG, "repayamount  2===>" + ship.getREPAYAM());

                                                            // Loggers.general().info(LOG,"repayamount
                                                            // 1===>"+shipAmt);
                                                            ship.setREPAYAMCurrency(outStandingAmtCurr);
                                                      }

                                                      if (resSet.getDouble("BILL") > 0 && payaction.equalsIgnoreCase("Pay")) {
                                                            //// Loggers.general().info(LOG,"Equlant bill
                                                            //// value
                                                            //// if loop=====>" +
                                                            //// resSet.getDouble("BILL"));
                                                            ship.setEQUBILL(new BigDecimal(resSet.getString("BILL")));
                                                            ship.setEQUBILLCurrency(resSet.getString("BCURR"));
                                                      } else {
                                                            // //Loggers.general().info(LOG,"Equlant bill
                                                            // value
                                                            // else loop=====>" +
                                                            // resSet.getDouble("BILL"));

                                                            String equ = "0";
                                                            ship.setEQUBILL(new BigDecimal(Double.valueOf(equ)));
                                                            ship.setEQUBILLCurrency(outStandingAmtCurr);
                                                      }

                                                      // //
                                                      // ship.setREPTYPE(resSet.getString("TYPE"));
                                                      // ship.setREASHRF(resSet.getString("REASON"));
                                                      // ship.setSHCOLAM(new
                                                      // BigDecimal(resSet.getString("SHORT")));
                                                      // ship.setSHCOLAMCurrency(resSet.getString("SHORTCURR"));

                                                } else {
                                                      //// Loggers.general().info(LOG,"Entered Else
                                                      //// Resetting--->" + outStandingAmt);
                                                      if (payaction.equalsIgnoreCase("Pay")) {
                                                            ship.setREPAYAM(new BigDecimal(outStanding));
                                                            // Loggers.general().info(LOG,"reystr
                                                            // if"+ship.getREPAYAM());
                                                            // Loggers.general().info(LOG,"outstanding"+outStanding);
                                                            ship.setREPAYAMCurrency(outStandingAmtCurr);
                                                      } else {
                                                            String reystr = "0";
                                                            ship.setREPAYAM(new BigDecimal(Double.valueOf(reystr)));
                                                            // Loggers.general().info(LOG,"reystr
                                                            // else"+ship.getREPAYAM());
                                                            ship.setREPAYAMCurrency(outStandingAmtCurr);
                                                      }

                                                      if (payaction.equalsIgnoreCase("Pay")) {

                                                            ship.setEQUBILL(new BigDecimal(outStanding));
                                                            ship.setEQUBILLCurrency(outStandingAmtCurr);
                                                      } else {
                                                            String equ = "0";
                                                            ship.setEQUBILL(new BigDecimal(Double.valueOf(equ)));
                                                            ship.setEQUBILLCurrency(outStandingAmtCurr);
                                                      }
                                                }

                                                if (outStandingAmt == 0 || outStandingAmt < 1) {
                                                      // //Loggers.general().info(LOG,"Entered
                                                      // outStandingAmt
                                                      // Resetting if loop--->" + outStandingAmt);
                                                      String reystr = "0";
                                                      ship.setREPAYAM(new BigDecimal(Double.valueOf(reystr)));
                                                      // Loggers.general().info(LOG,"reystr
                                                      // if==================>"+ship.getREPAYAM());
                                                      // Loggers.general().info(LOG,"outstanding"+outStanding);
                                                      ship.setREPAYAMCurrency(outStandingAmtCurr);
                                                }

                                          } else {
                                                //// Loggers.general().info(LOG,"Event is already
                                                //// Completed");
                                          }

                                    }

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"Exception e " + e.getMessage());
                              }

                        } else {
                              //// Loggers.general().info(LOG,"resetting amount ----->" +
                              //// getMinorCode());
                        }

//                      if ((step_input.equalsIgnoreCase("CSM") || step_input.equalsIgnoreCase("CBS Maker"))
//                                  && (getMinorCode().equalsIgnoreCase("POD") || getMinorCode().equalsIgnoreCase("POC"))) {
//                            try {
//                            /*    if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                        // Loggers.general().info(LOG,"Custom fields are clear
//                                        // method");
//                                  }*/
//                                  getcustomValueDelete();
//                                  getPane().onDELETEELCSETTclayButton();
//
//                            }
//
//                            catch (Exception e) {
//                                  e.getStackTrace();
//                            }
//                      }

                        if (getMinorCode().equalsIgnoreCase("POD") && step_input.equalsIgnoreCase("CSM")) {
                              try {
                                    List<ExtEventShippingTable> shipTab = (List<ExtEventShippingTable>) getWrapper()
                                                .getExtEventShippingTable();
                                    // //Loggers.general().info(LOG,"shipping table for notional
                                    // rate---->" + shipTable.size());
                                    String repval_new = "2";
                                    String notionalrate = "1";
                                    for (int i = 0; i < shipTab.size(); i++) {

                                          ExtEventShippingTable ship = shipTab.get(i);
                                          BigDecimal outStandAmt = ship.getLOUTSAMT();
                                          String outStandccy = ship.getLOUTSAMTCurrency().toString();
                                          // repval_new = ship.getREPTYPE().toString();
                                          //// Loggers.general().info(LOG,"shipping repayment in CSM
                                          // step
                                          // if====>" + outStandAmt + "" + payaction);

                                          ship.setREPAYAM(outStandAmt);
                                          ship.setREPAYAMCurrency(outStandccy);
                                          ship.setNOTIONAL(new BigDecimal(Double.valueOf(notionalrate)));
                                          //// Loggers.general().info(LOG,"Notional rate in CSM step
                                          //// if====>" + ship.getNOTIONAL());
                                          String shipcol = "0";
                                          ship.setSHCOLAM(new BigDecimal(Double.valueOf(shipcol)));
                                          ship.setSHCOLAMCurrency(outStandccy);

                                          if (repval_new.equalsIgnoreCase("2") || repval_new.equalsIgnoreCase("")
                                                      && (!repval_new.equalsIgnoreCase("1") || !repval_new.equalsIgnoreCase("Part"))) {
                                                //// Loggers.general().info(LOG,"Repayment type in CSM
                                                //// step
                                                //// if====>" + repval_new);
                                                ship.setREPTYPE(repval_new);
                                          } else {
                                                //// Loggers.general().info(LOG,"Repayment type in CSM
                                                //// step
                                                //// else loop====>" + repval_new);
                                          }
                                    }
                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"Exception e " + e.getMessage());
                              }
                        } else {
                              //// Loggers.general().info(LOG,"shipping repayment in CSM step
                              //// else====>" + payaction);
                        }

                        if (getMajorCode().equalsIgnoreCase("ELC") && getMinorCode().equalsIgnoreCase("DOP")
                                    || getMinorCode().equalsIgnoreCase("POD")) {
                              // PostingCustom post = null;
                              // if(step_csm.equalsIgnoreCase("CSM Maker 1"))
                              // String strPSID =
                              // getDriverWrapper().getPostingFieldAsText("PSID",
                              // "").trim();
                              try {

                                    String Mast = getDriverWrapper().getEventFieldAsText("MMST", "r", "").trim();
                                    String Evnt = getDriverWrapper().getEventFieldAsText("MEVR", "r", "").trim();

                                    // Loggers.general().info(LOG,"MasterReferenceNNum on
                                    // post----------->" + Mast);
                                    // Loggers.general().info(LOG,"their Reference on
                                    // post----------->" + Evnt);

                                    /*
                                     * if (dailyval_Log.equalsIgnoreCase("YES")) {
                                     *
                                     * Loggers.general().info(LOG,"MasterReference-------->" + Mast);
                                     * Loggers.general().info(LOG,"their Reference-------->" + Evnt); //
                                     * Loggers.general().info(LOG,"PSID----------->" + strPSID); }
                                     */
                                    con = getConnection();

                                    String query = "WITH MAXVAL AS (SELECT row_number() over (ORDER BY BEV.KEY97 asc) as ROWN,"
                                                + "MAS.MASTER_REF,MAS.KEY97 mas_key,"
                                                + "BEV.KEY97 bev_key,BEV.REFNO_PFIX || lpad(BEV.REFNO_SERL,3,0) bev_ref ,LIMBLK FROM"
                                                + " MASTER MAS,BASEEVENT  BEV ,EXTEVENT  EXT WHERE MAS.KEY97=BEV.MASTER_KEY AND BEV.KEY97=EXT.EVENT"
                                                + " and BEV.status<>'a' and trim(LIMBLK) is not null AND MAS.MASTER_REF='" + Mast
                                                + "'  ORDER BY BEV.KEY97)" + " SELECT LIMBLK FROM MAXVAL "
                                                + " WHERE ROWN IN (SELECT CASE WHEN '" + Evnt
                                                + "' lIKE 'DPR%' THEN ROWN ELSE ROWN-1 END  " + " FROM MAXVAL WHERE bev_ref='" + Evnt
                                                + "')";

                                    /*
                                     * if (dailyval_Log.equalsIgnoreCase("YES")) {
                                     * Loggers.general().info(LOG,"Query Result for Previous limit blocking-> " +
                                     * query);
                                     *
                                     * }
                                     */

                                    // Loggers.general().info(LOG,"Query Result for Previous limit
                                    // blocking ->" + query);

                                    ps1 = con.prepareStatement(query);

                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {

                                          /*
                                           * if (dailyval_Log.equalsIgnoreCase("YES")) { //
                                           * Loggers.general().info(LOG,"Entering While // Loop=======>");
                                           *
                                           * }
                                           */
                                          // Loggers.general().info(LOG,"Entering While
                                          // Loop========>");

                                          String prelimit = rs1.getString(1);
                                          // Loggers.general().info(LOG,"Prelimit=======>"+prelimit);

                                          getPane().setPRVLIMBL(prelimit);
                                    }
                                    Loggers.general().info(LOG, "Previous limit" + getPane().getPRVLIMBL());

                              } catch (Exception e) {
                                    e.printStackTrace();
                                    Loggers.general().info(LOG, "Exception Previous limit blocking" + e.getMessage());

                              } finally {
                                    try {
                                          if (rs1 != null)
                                                rs1.close();
                                          if (ps1 != null)
                                                ps1.close();
                                          if (con != null)
                                                con.close();
                                    } catch (SQLException e) {
                                          // Loggers.general().info(LOG,"Connection Failed! Check
                                          // output
                                          // console");
                                          e.printStackTrace();
                                    }
                              }

                        }
                        if (getMajorCode().equalsIgnoreCase("ILC") && getMinorCode().equalsIgnoreCase("POC")) {
                              // Loggers.general().info(LOG,"Inside ilc poc");

                              pane.getBtnREVERSELIENILCSETTclay().setEnabled(false);
                              getPane().getExtEventLienMarkingNew().setEnabled(false);
                              getPane().getExtEventLienMarkingUpdate().setEnabled(false);
                              getPane().getExtEventLienMarkingDelete().setEnabled(false);

                        }

                        if ((getMajorCode().equalsIgnoreCase("ILC") || getMajorCode().equalsIgnoreCase("ELC")
                                    || getMajorCode().equalsIgnoreCase("IDC") || getMajorCode().equalsIgnoreCase("OCC")
                                    || getMajorCode().equalsIgnoreCase("ODC"))
                                    && (getMinorCode().equalsIgnoreCase("POC") || getMinorCode().equalsIgnoreCase("DOP")
                                                || getMinorCode().equalsIgnoreCase("POD") || getMinorCode().equalsIgnoreCase("CLP")
                                                || getMinorCode().equalsIgnoreCase("CRC") || getMinorCode().equalsIgnoreCase("ISI")
                                                || getMinorCode().equalsIgnoreCase("NCAM"))) {
                              try {
                                    freezeMttNumber();
                                    System.out.println("INSIDE freezeMttNumber OF service");

                              } catch (Exception e) {
                                    e.printStackTrace();
                              }
                        }
                        if ((getMajorCode().equalsIgnoreCase("ILC") || getMajorCode().equalsIgnoreCase("ELC")
                                    || getMajorCode().equalsIgnoreCase("IDC") || getMajorCode().equalsIgnoreCase("OCC"))
                                    && (getMinorCode().equalsIgnoreCase("POC") || getMinorCode().equalsIgnoreCase("DOP")
                                                || getMinorCode().equalsIgnoreCase("POD") || getMinorCode().equalsIgnoreCase("CLP")
                                                || getMinorCode().equalsIgnoreCase("CRC") || getMinorCode().equalsIgnoreCase("NCAM"))
                                    && (stepid.equalsIgnoreCase("Input"))) {
                              try {
                                    getPane().setFINTRNID("");
                                    getPane().setFINTRNDT("");
                                    getPane().setLIACONID("");
                                    getPane().setLIACONDT("");
                                    System.out.println("INSIDE tran id issue resolution " + stepid);

                              } catch (Exception e) {
                                    e.printStackTrace();
                              }
                        }
                        if (getMajorCode().equalsIgnoreCase("ELC") && getMinorCode().equalsIgnoreCase("POD")) {
                              // Loggers.general().info(LOG,"5/14/19 condition------->");

                              try {
                                    String payAction = getDriverWrapper().getEventFieldAsText("PYAD", "s", "");
                                    // Loggers.general().info(LOG,"PYAD------->"+payAction);
                                    if (payAction.equalsIgnoreCase("AMD")) {
                                          // Loggers.general().info(LOG,"5/14/19 entered if");
                                          getPane().getCtlRELBNKLM().setEnabled(true);
                                          /*
                                           * getPane().setRELBNKLM(true); getCtrlREMBNKLM().setEnable(true);
                                           * getPane().getCtrlRELBNKLM().set;
                                           */

                                    } else {
                                          // Loggers.general().info(LOG,"5/14/19 entered else");
                                          getPane().getCtlRELBNKLM().setEnabled(false);
                                    }
                              } catch (Exception e) {
                                    Loggers.general().info(LOG, "Exception on payment action based scenario" + e.getMessage());
                              }
                        }
                  }

                  try {
                        PeriodicUpfront();
                  } catch (Exception e) {

                  }
                  try {
                        int cnt = 0;
                        cnt = getRepayProb();
                        // Loggers.general().info(LOG,"Repayment in subsidery" +cnt );
                  } catch (Exception ee) {
                        // Loggers.general().info(LOG,"Repayment in subsidery" + ee.getMessage());

                  }
                  // INC4431622 starts
                  try {
                        Loggers.general().info(LOG, "Entered method");
                        removeSpace();

                  } catch (Exception e) {
                        Loggers.general().info(LOG, "Exception update" + e.getMessage());
                  }
                  // INC4431622 ends

            }
            return false;

      }

      @SuppressWarnings("unchecked")
      public void onValidate(ValidationDetails validationDetails) {
            String strPropName = "MigrationDone";
            String dailyval = "";
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");
            // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
            EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
            if (PROPCode != null) {
                  dailyval = PROPCode.getPropval();

            } else {

            }
            String eventStatus = getDriverWrapper().getEventFieldAsText("EVSS", "s", "");
            if (dailyval.equalsIgnoreCase("NO")) {
                  checkGoodsDesc();
                  // Arunkumar M 14-07-2021
                  getChargeBasisAmountSplit();
                  // Arunkumar M 14-07-2021

                  String strLog = "Log";
                  String dailyval_Log = "";
                  AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                              .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
                  EXTGENCUSTPROP CodeLog = queryLog.getUnique();
                  if (CodeLog != null) {

                        dailyval_Log = CodeLog.getPropval();
                  } else {
                  }
                  try {
                        getPostingFxrate();
                        System.out.println("outside posting rate details");
                  } catch (Exception e) {
                        e.printStackTrace();
                  }
                  String step_Input = getDriverWrapper().getEventFieldAsText("CSTY", "s", "").trim();
                  String step_csm = getDriverWrapper().getEventFieldAsText("CSID", "s", "").trim();
                  String tenstart = getDriverWrapper().getEventFieldAsText("TNRF", "s", "").trim();
                  if (tenstart.length() > 0 && (getMinorCode().equalsIgnoreCase("DOP")
                              || getMinorCode().equalsIgnoreCase("CRC") || getMinorCode().equalsIgnoreCase("POD"))) {
                        getPane().setTENSTRT(tenstart);
                        getWrapper().setTENSTRT(tenstart);
                  } else {
                        getPane().setTENSTRT("");
                  }
                  // CR 37 starts
                  if (((getMajorCode().equalsIgnoreCase("ELC") && (getMinorCode().equalsIgnoreCase("POD"))))) {
                        ArrayList<String> settlementAccountList = new ArrayList<String>();
                        if (step_csm.equalsIgnoreCase("CBS Maker 1") || step_csm.equalsIgnoreCase("CBS Reject")) {
                              String mstrSetAccount = getMstrSttle();
                              if (!mstrSetAccount.isEmpty()) {
                                    settlementAccountList = getPayAccount();
                                    for (String settlementAccount : settlementAccountList) {
                                          if (!mstrSetAccount.equalsIgnoreCase(settlementAccount)) {
                                                validationDetails.addWarning(ValidationDetails.WarningType.Other,
                                                            "Account Number in settlement does not match with the account number in standing instructions [CM]");
                                          }
                                    }
                              }
                        }
                  }
//CR 37 ends
                  try {

                        String kotak_no = getDriverWrapper().getEventFieldAsText("ISS", "p", "cu").trim();
                        if (getMajorCode().equalsIgnoreCase("ELC")) {
                              if ((kotak_no.equalsIgnoreCase("INKKBK") || kotak_no.trim().equalsIgnoreCase("11908474"))) {
                                    getPane().setOWNLC(true);
                              } else {
                                    getPane().setOWNLC(false);
                              }
                        } else {
                              getPane().setOWNLC(false);

                        }
                  } catch (Exception e) {
                  }

                  try {
                        if (getMajorCode().equalsIgnoreCase("ELC") && getMinorCode().equalsIgnoreCase("POD")
                                    && (!step_csm.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed"))) {

                              getPane().onNOSTROELCSETTclayButton();

                        } else {
                        }
                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                        }
                  }

                  // amount to be collect

                  if (getMajorCode().equalsIgnoreCase("ELC")
                              && (getMinorCode().equalsIgnoreCase("DOP") || getMinorCode().equalsIgnoreCase("POD"))) {
                        try {
                              getNostroDetails();
                              System.out.println("outside nostro details");
                              getnetAmountOdcPay();
                        } catch (Exception e) {

                        }
                  }
                  // FCY Tax calculation
                  try {
                        getPane().getExtEventLienMarkingDelete().setEnabled(false);
                        System.out.println("inside fcct details");
                        getFCCTCALCULATION();
                        getFcctDetails(validationDetails);
                        System.out.println("outside fcct details");
                  } catch (Exception e) {
                        e.printStackTrace();
                  }
                  if (getMajorCode().equalsIgnoreCase("ILC")) {

                        String chargeVal = getDriverWrapper().getEventFieldAsText("APP", "p", "cAEF");
                        getPane().setCHRAMT(chargeVal);

                  }

                  try {

                        getutrNoGenerated();
                  } catch (Exception ee) {
                        // Loggers.general().info(LOG,"UTR Number getutrNoGenerated" + ee.getMessage());

                  }

                  if (getMajorCode().equalsIgnoreCase("ELC")) {
                        try {

                              getactualPenalRate();

                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Exception update" + e.getMessage());
                        }
                  }

                  if (getMajorCode().equalsIgnoreCase("EGT")) {
                        try {

                              getclaimExpiryDateISB();

                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Exception update" +
                              // e.getMessage());
                        }
                  }

                  String gateWayVal = getDriverWrapper().getEventFieldAsText("FRSI", "l", "").trim();
                  if (getMinorCode().equalsIgnoreCase("POD")
                              && (gateWayVal.equalsIgnoreCase("Y") || !gateWayVal.equalsIgnoreCase("N"))) {
                        try {
                              getbillNumber();
                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Exception update" + e.getMessage());
                        }
                  }

                  if (getMajorCode().equalsIgnoreCase("IDC") && getMinorCode().equalsIgnoreCase("NCAM")) {
                        try {

                              getcountryCode();

                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Exception update" +
                              // e.getMessage());
                        }

                  }
                  //kalpana ghorpade added 22-01-2025
                  if (getMajorCode().equalsIgnoreCase("IDC") && getMinorCode().equalsIgnoreCase("CAC")) {
                        try {
                              getLimitError(validationDetails);
                              System.out.println("Inside limit utilisation error Days Method");
                        } catch (Exception e) {
                              e.printStackTrace();
                        }

                              // TODO: handle exception
                        }

                  if (getMajorCode().equalsIgnoreCase("ELC")) {
                        getPane().onPRESHIPFINELCDPclayButton();
                        getPane().onPRESHIPFINELCSETTclayButton();
                        getPane().getExtEventLoanDetailsNew().setEnabled(false);
                        getPane().getExtEventLoanDetailsDelete().setEnabled(false);
                        getPane().getExtEventLoanDetailsUpdate().setEnabled(false);

                        // getPane().getCtlLIMTR().setEnabled(false);
                        // getPane().getCtlLIMTR().setEnabled(true);

                  }
                  // // //Loggers.general().info(LOG,"step_Input check ----->" + step_Input);
                  String step_Id = getDriverWrapper().getEventFieldAsText("CSID", "s", "").trim();
                  // if (step_Id.equalsIgnoreCase("CBS Authoriser")) {
                  // getPane().onFETCHLOANELCDPclayButton();
                  // getPane().onFETCHLOANELCSETTclayButton();
                  // }

                  // if (getMinorCode().equalsIgnoreCase("DOP")) {
                  // onFETCHLOANELCDP();
                  // } else if (getMinorCode().equalsIgnoreCase("POD")) {
                  // onFETCHLOANELCSETT();
                  // }
                  // //Loggers.general().info(LOG,"step id check for CSM ----->" + step_Id);
                  String gateway = getDriverWrapper().getEventFieldAsText("FRGI", "l", "");

                  if (gateway.equalsIgnoreCase("Y")) {
                        getPane().setPERADV("");
                        getPane().setADVREC("");
                        getPane().setNETRECIV("");
                        getPane().setBILLPAY("");
                  }
                  // Loggers.general().info(LOG,"todo on forcelim===>");
                  // Loggers.general().info(LOG,"stepId====>" +step_Id);

                  if ((step_Id.equalsIgnoreCase("Create") || step_Id.equalsIgnoreCase("CSM"))
                              && (getMinorCode().equalsIgnoreCase("POD"))) {
                        // Loggers.general().info(LOG,"Get force
                        // limit=================>"+getPane().getFORLIM());

                        getPane().setFORLIM(false);
                        // Loggers.general().info(LOG,"After force
                        // limit==============>"+getPane().getFORLIM());
                  }

                  //// Loggers.general().info(LOG,"gateway flag checking----->" + gateway);
                  if ((step_Id.equalsIgnoreCase("Create") || step_Id.equalsIgnoreCase("CSM"))
                              && (getMinorCode().equalsIgnoreCase("POD") || getMinorCode().equalsIgnoreCase("POC"))) {
                        // //Loggers.general().info(LOG,"Step id for log Create if for log
                        // step---->" + step_log);

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
                        //// Loggers.general().info(LOG,"Step id for else step---->" +
                        //// step_log);
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

                  if ((step_Id.equalsIgnoreCase("Create") || step_Id.equalsIgnoreCase("CSM"))
                              && (getMinorCode().equalsIgnoreCase("POD") || getMinorCode().equalsIgnoreCase("POC"))
                              && (!gateway.equalsIgnoreCase("Y"))) {
                        // Loggers.general().info(LOG,"to on===========================>");
//                      getPane().setNOSAMT("");
//                      getPane().setNOSTOUT("");
                        getPane().setEBRCFLAG("");

                        /*
                         * getPane().setNOSTRM(""); getPane().setNOSTMT(""); getPane().setNOSTAMT("");
                         * getPane().setNOSTDAT("");
                         *
                         * getPane().setPOOLAMT(""); getPane().setNOSTACC(""); getPane().setINWMSG("");
                         * getPane().setMTMESG("");
                         */
                  }
                  try {
                        getPane().onBENIFSCELCSETTclayButton();
                        getPane().onRTGSELCDPclayButton();
                        getPane().onBENIFSCILCSETTclayButton();
                        getPane().onBENIFSCINWDOCCOLPAYclayButton();
                        // getPane().onFETCHLOANELCSETTclayButton();

                        String getHyperSFMS = getIFSCSEARCH();
                        ExtendedHyperlinkControlWrapper ifscilc = getPane().getCtlSFMSIMPLCclayHyperlink();
                        ifscilc.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc1 = getPane().getCtlSFMSILCAMDclayHyperlink();
                        ifscilc1.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc2 = getPane().getCtlSFMSILCCANHyperlink();
                        ifscilc2.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc3 = getPane().getCtlSFMSILCCORESSlayHyperlink();
                        ifscilc3.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc4 = getPane().getCtlSFMSILCEXPHyperlink();
                        ifscilc4.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc5 = getPane().getCtlSFMSILCISSTAKEclayHyperlink();
                        ifscilc5.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc6 = getPane().getCtlSFMSILCMAINLayHyperlink();
                        ifscilc6.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc7 = getPane().getCtlSFMSILCSETTclayHyperlink();
                        ifscilc7.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc8 = getPane().getCtlSFMSIMPADVclayHyperlink();
                        ifscilc8.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc9 = getPane().getCtlSFMSIMPADVclayHyperlink();
                        ifscilc9.setUrl(getHyperSFMS);
//                      ExtendedHyperlinkControlWrapper ifscilc10 = getPane().getCtlSFMSIMPLCCLAIMRECclayHyperlink();
//                      ifscilc10.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc11 = getPane().getCtlSFMSILCMAINLayHyperlink();
                        ifscilc11.setUrl(getHyperSFMS);

                        // ELC
                        ExtendedHyperlinkControlWrapper sfmsExpAdvc = getPane().getCtlSFMSELCAMDclayHyperlink();
                        sfmsExpAdvc.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdvD = getPane().getCtlSFMSELCADJclayHyperlink();
                        sfmsExpAdvD.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv6 = getPane().getCtlSFMSELCEXPIREclayHyperlink();
                        sfmsExpAdv6.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv7 = getPane().getCtlSFMSELCADVclayHyperlink();
                        sfmsExpAdv7.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv8 = getPane().getCtlSFMSECLADVTAKEclayHyperlink();
                        sfmsExpAdv8.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv9 = getPane().getCtlSFMSELCCORRlayHyperlink();
                        sfmsExpAdv9.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv10 = getPane().getCtlSFMSELCDPclayHyperlink();
                        sfmsExpAdv10.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv11 = getPane().getCtlSFMSELCSETTclayHyperlink();
                        sfmsExpAdv11.setUrl(getHyperSFMS);
                        // Export guarantee
                        ExtendedHyperlinkControlWrapper sfmsExpAdv12 = getPane().getCtlSFMSIGTISSclayHyperlink();
                        sfmsExpAdv12.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv13 = getPane().getCtlSFMSEGTADJclayHyperlink();
                        sfmsExpAdv13.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv14 = getPane().getCtlSFMSEGTAMDclayHyperlink();
                        sfmsExpAdv14.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv15 = getPane().getCtlSFMSIGTINVOCclayHyperlink();
                        sfmsExpAdv15.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv16 = getPane().getCtlSFMSEGTCORRESlayHyperlink();
                        sfmsExpAdv16.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv17 = getPane().getCtlSFMSEXPGRNTCANLayHyperlink();
                        sfmsExpAdv17.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv18 = getPane().getCtlSFMSEXPGRNTEEOPclayHyperlink();
                        sfmsExpAdv18.setUrl(getHyperSFMS);

                        // IDC and ODC
                        ExtendedHyperlinkControlWrapper sfmsExpAdv19 = getPane().getCtlSFMSINWDOCCOLADJclayHyperlink();
                        sfmsExpAdv19.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv20 = getPane().getCtlSFMSINWDOCCOLAMDclayHyperlink();
                        sfmsExpAdv20.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv21 = getPane().getCtlSFMSINWDOCCOLPAYclayHyperlink();
                        sfmsExpAdv21.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv22 = getPane().getCtlSFMSOUTDOCCOLADJclayHyperlink();
                        sfmsExpAdv22.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv23 = getPane().getCtlSFMSOUTDOCCOLAMDclayHyperlink();
                        sfmsExpAdv23.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv24 = getPane().getCtlSFMSOUTDOCCOLEXPclayHyperlink();
                        sfmsExpAdv24.setUrl(getHyperSFMS);

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,e.getMessage());
                  }

                  // For CR-143 Limit Nodes
                  try {
                        String HyperLimitNode = getLimitNode().trim();
                        ExtendedHyperlinkControlWrapper limitnode1 = getPane().getCtlUnavailablelimitnodesIMPLCclayHyperlink();
                        limitnode1.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode2 = getPane().getCtlUnavailablelimitnodesILCAMDclayHyperlink();
                        limitnode2.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode3 = getPane().getCtlUnavailablelimitnodesIMPADVclayHyperlink();
                        limitnode3.setUrl(HyperLimitNode);
//                      ExtendedHyperlinkControlWrapper limitnode4 = getPane().getCtlUnavailablelimitnodesELCADVclayHyperlink();
//                      limitnode4.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode5 = getPane().getCtlUnavailablelimitnodesELCAMDclayHyperlink();
                        limitnode5.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode6 = getPane().getCtlUnavailablelimitnodesELCADJclayHyperlink();
                        limitnode6.setUrl(HyperLimitNode);
//                      ExtendedHyperlinkControlWrapper limitnode7 = getPane().getCtlUnavailablelimitnodesELCDPclayHyperlink();
//                      limitnode7.setUrl(HyperLimitNode);
//                      ExtendedHyperlinkControlWrapper limitnode8 = getPane().getCtlUnavailablelimitnodesELCSETTclayHyperlink();
//                      limitnode8.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode9 = getPane().getCtlUnavailablelimitnodesEGTAMDclayHyperlink();
                        limitnode9.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode10 = getPane()
                                    .getCtlUnavailablelimitnodesEGTADJclayHyperlink();
                        limitnode10.setUrl(HyperLimitNode);
                  } catch (Exception e) {
                        // System.out.println("For Limit Node"+e.getMessage());

                  }

                  // InwardLink
                  try {
                        String HyperInward = getINWARDREM();
                        ExtendedHyperlinkControlWrapper InwardLink = getPane().getCtlINWARDELCDPclayHyperlink();
                        InwardLink.setUrl(HyperInward);
                        ExtendedHyperlinkControlWrapper InwardLink1 = getPane().getCtlINWARDELCSETTclayHyperlink();
                        InwardLink1.setUrl(HyperInward);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"InwardLink exception----->" +//
                        // ees.getMessage());
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
                        //// Loggers.general().info(LOG,"getMajorCode() ILC------->" +
                        //// getMajorCode());

                  }

                  String prd_typ = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();

                  String payaction = getDriverWrapper().getEventFieldAsText("PYAN", "s", "");
                  // Loggers.general().info(LOG,"payment Type action---->" + payaction);
                  String tranType = getWrapper().getTRANTYP();
                  // Loggers.general().info(LOG,"Write of Type---->" + tranType +
                  // "tranType---->" + tranType.length());
                  if (tranType.equalsIgnoreCase("WRITEOFF") && payaction.equalsIgnoreCase("Pay")
                              && prd_typ.equalsIgnoreCase("ELF") && !step_Id.equalsIgnoreCase("CSM")) {
                        getPane().setEBRCFLAG("N");
                  } else if ((tranType.equalsIgnoreCase("") || !tranType.equalsIgnoreCase("WRITEOFF"))
                              && !payaction.equalsIgnoreCase("Pay") && prd_typ.equalsIgnoreCase("ELF")
                              && !step_Id.equalsIgnoreCase("CSM")) {
                        getPane().setEBRCFLAG("N");
                  } else if ((tranType.equalsIgnoreCase("") || !tranType.equalsIgnoreCase("WRITEOFF"))
                              && payaction.equalsIgnoreCase("Pay") && prd_typ.equalsIgnoreCase("ELF")
                              && !step_Id.equalsIgnoreCase("CSM")) {
                        getPane().setEBRCFLAG("Y");

                  } else if (step_Id.equalsIgnoreCase("CSM")) {
                        getPane().setEBRCFLAG("");
                  }

                  try {

                        String FDenquiryLink = FDenquiry();
                        ExtendedHyperlinkControlWrapper fdlink = getPane().getCtlFDENQUIRYIMPLCclayHyperlink();
                        fdlink.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink1 = getPane().getCtlFDENQUIRYILCAMDclayHyperlink();
                        fdlink1.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink2 = getPane().getCtlFDENQUIRYIMPADVclayHyperlink();
                        fdlink2.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink3 = getPane().getCtlFDENQUIRYIMPLCCLAIMRECclayHyperlink();
                        fdlink3.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink4 = getPane().getCtlFDENQUIRYILCSETTclayHyperlink();
                        fdlink4.setUrl(FDenquiryLink);

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"FDenquiry link" + e.getMessage());
                  }

                  String finace_event = getDriverWrapper().getEventFieldAsText("BFC", "l", "");
                  if (getMinorCode().equalsIgnoreCase("DOP")) {
                        if (finace_event.equalsIgnoreCase("Y")) {
                              getPane().setTENORDET("DPR");
                              getWrapper().setTENORDET("DPR");
                        } else {
                              getPane().setTENORDET("NO");
                              getWrapper().setTENORDET("NO");
                        }
                  } else if (getMinorCode().equalsIgnoreCase("POD")) {

                        if (finace_event.equalsIgnoreCase("Y")) {
                              getPane().setTENORDET("POD");
                              getWrapper().setTENORDET("POD");
                        }
                        EventPane pane = (EventPane) getPane();
                        pane.getBtnDELETEELCSETTclay().setEnabled(false);

                  }

                  if (getMinorCode().equalsIgnoreCase("POD") && step_Id.equalsIgnoreCase("CSM")) {

                        try {
                              List<ExtEventShippingTable> shipTab = (List<ExtEventShippingTable>) getWrapper()
                                          .getExtEventShippingTable();
                              // //Loggers.general().info(LOG,"shipping table for notional
                              // rate---->" + shipTable.size());
                              String repval_new = "2";
                              String notionalrate = "1";
                              for (int i = 0; i < shipTab.size(); i++) {

                                    ExtEventShippingTable ship = shipTab.get(i);
                                    BigDecimal outStandAmt = ship.getLOUTSAMT();
                                    String outStandccy = ship.getLOUTSAMTCurrency().toString();
                                    repval_new = ship.getREPTYPE().toString();
                                    //// Loggers.general().info(LOG,"shipping repayment in CSM step
                                    //// if====>" + outStandAmt + "" + payaction);

                                    ship.setREPAYAM(outStandAmt);
                                    // Loggers.general().info(LOG,"outstanding=====>"+outStandAmt);
                                    ship.setREPAYAMCurrency(outStandccy);
                                    String shipcol = "0";
                                    ship.setSHCOLAM(new BigDecimal(Double.valueOf(shipcol)));
                                    ship.setSHCOLAMCurrency(outStandccy);
                                    ship.setNOTIONAL(new BigDecimal(Double.valueOf(notionalrate)));
                                    //// Loggers.general().info(LOG,"Notional rate in CSM step
                                    //// if====>" + ship.getNOTIONAL());
                                    if (repval_new.equalsIgnoreCase("2") || repval_new.equalsIgnoreCase("")
                                                && (!repval_new.equalsIgnoreCase("1") || !repval_new.equalsIgnoreCase("Part"))) {
                                          //// Loggers.general().info(LOG,"Repayment type in CSM step
                                          //// if====>" + repval_new);
                                          ship.setREPTYPE(repval_new);
                                    } else {
                                          //// Loggers.general().info(LOG,"Repayment type in CSM step
                                          //// else loop====>" + repval_new);
                                    }
                              }
                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Exception e " + e.getMessage());
                        }
                  } else {
                        //// Loggers.general().info(LOG,"shipping repayment in CSM step
                        //// else====>" + payaction);
                  }

                  String productcode = getDriverWrapper().getEventFieldAsText("PCO", "s", "").trim(); // ILC
                  String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim(); // ISS001
                  String subproductCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();// ILF
                  String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim(); //
                  String evnt = getDriverWrapper().getEventFieldAsText("EPF", "s", "");
                  String evvcount = getDriverWrapper().getEventFieldAsText("ESQ", "i", "");
                  // //Loggers.general().info(LOG,"Event reference for evvcount----> " +
                  
                  // HS Code Validation
                  try {
                        String HScode = getDriverWrapper().getEventFieldAsText("cACO", "s", "");
                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              // Loggers.general().info(LOG,"HS Code" + HScode + "HS Code lengh" +
                              // HScode.length());
                        }
                        if ((!HScode.equalsIgnoreCase("") && HScode != null) && (step_Input.equalsIgnoreCase("i"))
                                    && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id.equalsIgnoreCase("CBS Maker 1"))
                                    && HScode.length() > 1) {
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
                                    if (ka == 0 && HScode.length() > 1) {
                                          // //Loggers.general().info(LOG,"warning of hs code ");
                                          validationDetails.addWarning(ValidationDetails.WarningType.Other,
                                                      "HS Code is Invalid (" + HScode + ")" + "[CM]");

                                    }
                              }

                        } else {
                              // Loggers.general().info(LOG,"HS code");
                        }
                  } catch (Exception e) {
                        // //Loggers.general().info(LOG,"exception caught " +e.getMessage() );
                  } finally {
                        surrenderDB(con, ps, rs);
                  }

                  // Notional due date elc new
                  try {
                        if (getMajorCode().equalsIgnoreCase("ELC")) {
                              getNotionalDueDateELC();
                        }
                  } catch (Exception e1) {

                        /*
                         * if (dailyval_Log.equalsIgnoreCase("YES")) {
                         *
                         * //Loggers.general().info(LOG,"Exception in NotionalDueDateELC" +
                         * e1.getMessage()); }
                         */
                  }
                  // Notional due date ILC new

                  try {
                        if (getMajorCode().equalsIgnoreCase("ILC")) {
                              getNotionalDueDateILC();
                        }
                  } catch (Exception e1) {

                        /*
                         * if (dailyval_Log.equalsIgnoreCase("YES")) {
                         *
                         * //Loggers.general().info(LOG,"Exception in NotionalDueDate ILC" +
                         * e1.getMessage()); }
                         */
                  }

                  if (step_Id.equalsIgnoreCase("CBS Authoriser") || step_Id.equalsIgnoreCase("CBS Maker 1")) {
                        try {
                              con = getConnection();
                              String query = "select wm.MESSAGE from master mas, BASEEVENT bev, warnmsg wm where mas.key97=BEV.MASTER_KEY and bev.key97=wm.associ30 and WM.PARTCODE = 'MIXFX' and MAS.MASTER_REF = '"
                                          + MasterReference + "' and BEV.REFNO_PFIX= '" + evnt + "' and BEV.REFNO_SERL = '" + evvcount
                                          + "'";

                              /*
                               * if (dailyval_Log.equalsIgnoreCase("YES")) {
                               *
                               * //Loggers.general().info(LOG,"FX deal message in authorization " + query); }
                               */
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

                                    /*
                                     * if (dailyval_Log.equalsIgnoreCase("YES")) {
                                     *
                                     * // Loggers.general().info(LOG,"FX deal message converted===>" + fxDeal); }
                                     */
                              }

                              if (fxDeal.equalsIgnoreCase("FX") && (step_Id.equalsIgnoreCase("CBS Authoriser")
                                          || step_Id.equalsIgnoreCase("CBS Maker 1"))) {
                                    // Loggers.general().info(LOG,"warning message in authorization
                                    // if
                                    // loop" + count3 + " and step_Id" + step_csm);
                                    validationDetails.addError(ErrorType.Other, "FX deal amount more than transaction amount [CM]");
                              }

                              else {
                                    // Loggers.general().info(LOG,"warning message in authorization
                                    // else" + count3 + " and step_Id" + step_csm);
                                    /*
                                     * if (dailyval_Log.equalsIgnoreCase("YES")) {
                                     *
                                     * //Loggers.general().info(LOG,"warning message in authorization else" +
                                     * fxDeal); }
                                     */
                              }

                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Exception" + e1.getMessage());
                              /*
                               * if (dailyval_Log.equalsIgnoreCase("YES")) {
                               *
                               * //Loggers.general().info(LOG,"Exception fx Deal warning message" +
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
                                    // Loggers.general().info(LOG,"Connection Failed! Check output
                                    // console");
                                    e.printStackTrace();
                              }
                        }
                  }

                  try {
                        con = getConnection();
                        String query = "select count(*) from master mas, BASEEVENT bev, warnmsg wm where mas.key97=BEV.MASTER_KEY and bev.key97=wm.associ30 and WM.PARTCODE = 'RtTol' and MAS.MASTER_REF = '"
                                    + MasterReference + "' and BEV.REFNO_PFIX= '" + evnt + "' and BEV.REFNO_SERL = '" + evvcount
                                    + "'";
                        // Loggers.general().info(LOG,"Query FX deal rate is outside specified
                        // tolerance " + query);
                        /*
                         * if (dailyval_Log.equalsIgnoreCase("YES")) {
                         *
                         * //Loggers.general().info(
                         * LOG,"Query FX deal rate is outside specified tolerance " + query); }
                         */
                        int count = 0;
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                              // //Loggers.general().info(LOG,"Entered while");
                              count = rs1.getInt(1);
                              // //Loggers.general().info(LOG,"value of count in while
                              // authorization" + count);
                        }

                        if (count > 0
                                    && (step_Id.equalsIgnoreCase("CBS Authoriser") || step_Id.equalsIgnoreCase("CBS Maker 1"))) {
                              // //Loggers.general().info(LOG,"warning message in authorization if
                              // loop" + count + " and step_Id" + step_Id);
                              validationDetails.addError(ErrorType.Other, "FX deal rate is outside specified tolerance [CM]");
                        }

                        else {
                              /*
                               * if (dailyval_Log.equalsIgnoreCase("YES")) {
                               *
                               * // Loggers.general().info(
                               * LOG,"FX deal rate is outside specified tolerance else " + count); }
                               */
                        }

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Exception" + e.getMessage());
                        /*
                         * if (dailyval_Log.equalsIgnoreCase("YES")) {
                         *
                         * Loggers.general().info(
                         * LOG,"Exception FX deal rate is outside specified tolerance" +
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
                              //// Loggers.general().info(LOG,"Connection Failed! Check output
                              //// console");
                              e.printStackTrace();
                        }
                  }

                  // Standing instruction link

                  // try {
                  // String Standing = getStandinglink().trim();
                  // ExtendedHyperlinkControlWrapper Standing1 =
                  // getPane().getCtlSTANDINGELCSETTclayHyperlink();
                  // Standing1.setUrl(Standing);
                  //
                  // String nostroAccountNum = getPane().getNOSTACC();
                  // String nostroCreditCCY = getWrapper().getPOOLAMTCurrency();
                  // if (dailyval_Log.equalsIgnoreCase("YES")) {
                  // Loggers.general().info(LOG,"Account number for SI Availability check - "
                  // + nostroAccountNum
                  // + " And Credit CCY - " + nostroCreditCCY);
                  // }
                  // Standing1.setVisible(isSIAvailable(nostroAccountNum,
                  // nostroCreditCCY));
                  // } catch (Exception ees) {
                  // // Loggers.general().info(LOG,"Standing in ODC--------->" +
                  // // ees.getMessage());
                  // }

                  // Setting the PreShipment Link
                  EventPane pane = (EventPane) getPane();

                  if (prd_typ.equalsIgnoreCase("ELD")) {

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
                        // //Loggers.general().info(LOG,"Product type is forign");
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
                        ExtendedHyperlinkControlWrapper csmreftrack1 = getPane().getCtlCSMCHECKLISTELCADVclayHyperlink();
                        csmreftrack1.setUrl(Hyperreferel12);

//                      ExtendedHyperlinkControlWrapper csmreftrackamd1 = getPane().getCtlCSMCHECKLISTELCDPclayHyperlink();
//                      csmreftrackamd1.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrackamd2 = getPane().getCtlCSMCHECKLISTELCADJclayHyperlink();
                        csmreftrackamd2.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrackamd4 = getPane().getCtlCSMCHECKLISTELCAMDclayHyperlink();
                        csmreftrackamd4.setUrl(Hyperreferel12);

//                      ExtendedHyperlinkControlWrapper csmreftrackamd6 = getPane().getCtlCSMCHECKLISTELCSETTclayHyperlink();
//                      csmreftrackamd6.setUrl(Hyperreferel12);

                        // -------------ODC START------------------------------------

                        ExtendedHyperlinkControlWrapper cpcreftrackamd8 = getPane()
                                    .getCtlCSMCHECKLISTOUTDOCCOLADJclayHyperlink();
                        cpcreftrackamd8.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrackamd10 = getPane()
                                    .getCtlCSMCHECKLISTOUTDOCCOLAMDclayHyperlink();
                        cpcreftrackamd10.setUrl(Hyperreferel12);

                        // -------------ODC END------------------------------------

                        // ---IDC----

                        ExtendedHyperlinkControlWrapper cpcreftrackamd12 = getPane()
                                    .getCtlCSMCHECKLISTINWDOCCOLPAYclayHyperlink();
                        cpcreftrackamd12.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrackamd14 = getPane()
                                    .getCtlCSMCHECKLISTINWDOCCOLADJclayHyperlink();
                        cpcreftrackamd14.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrackamd16 = getPane()
                                    .getCtlCSMCHECKLISTINWDOCCOLAMDclayHyperlink();
                        cpcreftrackamd16.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrack17 = getPane().getCtlCSMCHECKLISTIMPLCclayHyperlink();
                        csmreftrack17.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrackamd18 = getPane().getCtlCSMCHECKLISTILCAMDclayHyperlink();
                        csmreftrackamd18.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrackamd19 = getPane().getCtlCSMCHECKLISTIMPADVclayHyperlink();
                        csmreftrackamd19.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrackamd20 = getPane()
                                    .getCtlCSMCHECKLISTIMPLCCLAIMRECclayHyperlink();
                        csmreftrackamd20.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrackamd21 = getPane().getCtlCSMCHECKLISTILCSETTclayHyperlink();
                        csmreftrackamd21.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrackamd22 = getPane().getCtlCSMCHECKLISTILCCANHyperlink();
                        csmreftrackamd22.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrackamd23 = getPane().getCtlCSMCHECKLISTEGTADJclayHyperlink();
                        csmreftrackamd23.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrackamd24 = getPane().getCtlCSMCHECKLISTEGTAMDclayHyperlink();
                        csmreftrackamd24.setUrl(Hyperreferel12);
                        // ---IDC----

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST--------->" +
                        // ees.getMessage());
                  }

                  // REFERE TRACKING CSM
                  try {

                        String Hyperreferel = getReferealtracking().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack = getPane().getCtlCSMREFRALIMPLCclayHyperlink();
                        csmreftrack.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackamd = getPane().getCtlCSMREFRALILCAMDclayHyperlink();
                        csmreftrackamd.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackamd1 = getPane().getCtlCSMREFRALIMPADVclayHyperlink();
                        csmreftrackamd1.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackamd3 = getPane().getCtlCSMREFRALIMPLCCLAIMRECclayHyperlink();
                        csmreftrackamd3.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackamd5 = getPane().getCtlCSMREFRALILCSETTclayHyperlink();
                        csmreftrackamd5.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackamd7 = getPane().getCtlCSMREFRALILCCANHyperlink();
                        csmreftrackamd7.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackk1 = getPane().getCtlCSMREFRALELCADVclayHyperlink();
                        csmreftrackk1.setUrl(Hyperreferel);

//                      ExtendedHyperlinkControlWrapper csmreftrackk3 = getPane().getCtlCSMREFRALELCDPclayHyperlink();
//                      csmreftrackk3.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackk5 = getPane().getCtlCSMREFRALELCADJclayHyperlink();
                        csmreftrackk5.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackk7 = getPane().getCtlCSMREFRALELCAMDclayHyperlink();
                        csmreftrackk7.setUrl(Hyperreferel);

//                      ExtendedHyperlinkControlWrapper csmreftrackk9 = getPane().getCtlCSMREFRALELCSETTclayHyperlink();
//                      csmreftrackk9.setUrl(Hyperreferel);

                        // -------------ODC START------------------------------------

                        ExtendedHyperlinkControlWrapper cpcreftrackamd7 = getPane().getCtlCSMREFRALOUTDOCCOLADJclayHyperlink();
                        cpcreftrackamd7.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper cpcreftrackamd11 = getPane().getCtlCSMREFRALOUTDOCCOLAMDclayHyperlink();
                        cpcreftrackamd11.setUrl(Hyperreferel);

                        // -------------ODC END------------------------------------

                        // ---IDC----

                        ExtendedHyperlinkControlWrapper cpcreftrackamd18 = getPane().getCtlCSMREFRALINWDOCCOLPAYclayHyperlink();
                        cpcreftrackamd18.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper cpcreftrackamd14 = getPane().getCtlCSMREFRALINWDOCCOLADJclayHyperlink();
                        cpcreftrackamd14.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper cpcreftrackamd16 = getPane().getCtlCSMREFRALINWDOCCOLAMDclayHyperlink();
                        cpcreftrackamd16.setUrl(Hyperreferel);
                        ExtendedHyperlinkControlWrapper csmreftrackamd23 = getPane().getCtlCSMREFRALEGTADJclayHyperlink();
                        csmreftrackamd23.setUrl(Hyperreferel);
                        ExtendedHyperlinkControlWrapper csmreftrackamd24 = getPane().getCtlCSMREFRALEGTAMDclayHyperlink();
                        csmreftrackamd24.setUrl(Hyperreferel);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING--------->" +
                        // ees.getMessage());
                  }

                  // Workflow CBS
                  try {
                        String Hyperreferel12 = getWorkflowCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack1 = getPane().getCtlCPCCHECKELCADVclayHyperlink();
                        cpcreftrack1.setUrl(Hyperreferel12);
//                      ExtendedHyperlinkControlWrapper cpcreftrackamd1 = getPane().getCtlCPCCHECKELCDPclayHyperlink();
//                      cpcreftrackamd1.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd3 = getPane().getCtlCPCCHECKELCADJclayHyperlink();
                        cpcreftrackamd3.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd5 = getPane().getCtlCPCCHECKELCAMDclayHyperlink();
                        cpcreftrackamd5.setUrl(Hyperreferel12);
//                      ExtendedHyperlinkControlWrapper cpcreftrackamd7 = getPane().getCtlCPCCHECKELCSETTclayHyperlink();
//                      cpcreftrackamd7.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd9 = getPane().getCtlCPCCHECKOUTDOCCOLADJclayHyperlink();
                        cpcreftrackamd9.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd11 = getPane().getCtlCPCCHECKOUTDOCCOLAMDclayHyperlink();
                        cpcreftrackamd11.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd13 = getPane().getCtlCPCCHECKINWDOCCOLPAYclayHyperlink();
                        cpcreftrackamd13.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd15 = getPane().getCtlCPCCHECKINWDOCCOLADJclayHyperlink();
                        cpcreftrackamd15.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd17 = getPane().getCtlCPCCHECKINWDOCCOLAMDclayHyperlink();
                        cpcreftrackamd17.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack21 = getPane().getCtlCPCCHECKIMPLCclayHyperlink();
                        cpcreftrack21.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd22 = getPane().getCtlCPCCHECKILCAMDclayHyperlink();
                        cpcreftrackamd22.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd23 = getPane().getCtlCPCCHECKIMPADVclayHyperlink();
                        cpcreftrackamd23.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd24 = getPane().getCtlCPCCHECKIMPLCCLAIMRECclayHyperlink();
                        cpcreftrackamd24.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd25 = getPane().getCtlCPCCHECKILCSETTclayHyperlink();
                        cpcreftrackamd25.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd26 = getPane().getCtlCPCCHECKILCCANHyperlink();
                        cpcreftrackamd26.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrackamd23 = getPane().getCtlCPCCHECKEGTADJclayHyperlink();
                        csmreftrackamd23.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrackamd24 = getPane().getCtlCPCCHECKEGTAMDclayHyperlink();
                        csmreftrackamd24.setUrl(Hyperreferel12);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST WorkflowCBS--------->"
                        // + ees.getMessage());
                  }

                  // Refereal CBS

                  try {
                        String Hyperreferel12 = getReferealtrackingCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack = getPane().getCtlCPCREFERELIMPLCclayHyperlink();
                        cpcreftrack.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd = getPane().getCtlCPCREFERELILCAMDclayHyperlink();
                        cpcreftrackamd.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd2 = getPane().getCtlCPCREFERELIMPADVclayHyperlink();
                        cpcreftrackamd2.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd4 = getPane()
                                    .getCtlCPCREFERELIMPLCCLAIMRECclayHyperlink();
                        cpcreftrackamd4.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd6 = getPane().getCtlCPCREFERELILCSETTclayHyperlink();
                        cpcreftrackamd6.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd8 = getPane().getCtlCPCREFERELILCCANHyperlink();
                        cpcreftrackamd8.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrackk2 = getPane().getCtlCPCREFERELELCADVclayHyperlink();
                        csmreftrackk2.setUrl(Hyperreferel12);
//                      ExtendedHyperlinkControlWrapper csmreftrackk4 = getPane().getCtlCPCREFERELELCDPclayHyperlink();
//                      csmreftrackk4.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrackk6 = getPane().getCtlCPCREFERELELCADJclayHyperlink();
                        csmreftrackk6.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrackk8 = getPane().getCtlCPCREFERELELCAMDclayHyperlink();
                        csmreftrackk8.setUrl(Hyperreferel12);
//                      ExtendedHyperlinkControlWrapper csmreftrackk10 = getPane().getCtlCPCREFRALELCSETTclayHyperlink();
//                      csmreftrackk10.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackam = getPane().getCtlCPCREFERELOUTDOCCOLADJclayHyperlink();
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
                        ExtendedHyperlinkControlWrapper csmreftrackamd23 = getPane().getCtlCPCREFERELEGTADJclayHyperlink();
                        csmreftrackamd23.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrackamd24 = getPane().getCtlCPCREFERELEGTAMDclayHyperlink();
                        csmreftrackamd24.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING REFERE CBS--------->" +
                        // ees.getMessage());
                  }

                  // FDenquiry link

                  try {

                        String FDenquiryLink = FDenquiry().trim();
                        ExtendedHyperlinkControlWrapper fdlink = getPane().getCtlFDENQUIRYIMPLCclayHyperlink();
                        fdlink.setUrl(FDenquiryLink);
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"FDenquiry link" + e.getMessage());
                  }

                  try {

                        String Preshipment = getHyperPreshipment();
                        ExtendedHyperlinkControlWrapper dmsh5 = getPane().getCtlPreshipmentELCSETTclayHyperlink();
                        dmsh5.setUrl(Preshipment);

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Exception in setting PreShipment url " +
                        // e.getMessage());
                  }

                  if (getMajorCode().equalsIgnoreCase("ELC") && getMinorCode().equalsIgnoreCase("POD")) {
                        List<ExtEventShippingTable> shipcol = (List<ExtEventShippingTable>) getWrapper()
                                    .getExtEventShippingTable();
                        if (shipcol.size() > 0) {
                              // //Loggers.general().info(LOG,"shipdetails --->" + shipcol);

                              pane.getExtEventShippingTableDelete().setEnabled(false);
                        }
                  }

                  if (getMajorCode().equalsIgnoreCase("ELC")) {
                        // value for LOB
                        try {
                              getLob();

                        } catch (Exception ee) {
                              // Loggers.general().info(LOG,"Exception LOB Catch" + ee.getMessage());
                        }
                  }

                  try {
                        getLOBISSUE();

                  } catch (Exception ee) {
                        Loggers.general().info(LOG, ee.getMessage());
                  }

                  if ((getMinorCode().equalsIgnoreCase("DOP") || getMinorCode().equalsIgnoreCase("POD"))) {
                        try {
                              String finaceCreate = getDriverWrapper().getEventFieldAsText("BFC", "l", "");
                              String limitblocking = getDriverWrapper().getEventFieldAsText("cBNR", "s", "");
                              if (finaceCreate.equalsIgnoreCase("Y") && !finaceCreate.equalsIgnoreCase("N")) {
                                    String facility = getWrapper().getLIMITID().trim();

                                    /*
                                     * if (dailyval_Log.equalsIgnoreCase("YES")) {
                                     * //Loggers.general().info(LOG,"ELC finance limit id validation===>" +
                                     * facility); //Loggers.general().info(
                                     * LOG,"ELC LOB code finance created validation=======>" + finaceCreate); }
                                     */

//                                  if (finaceCreate.equalsIgnoreCase("Y") && (facility == null || facility.equalsIgnoreCase(""))
//                                              && (step_csm.equalsIgnoreCase("CBS Maker")
//                                                          || step_csm.equalsIgnoreCase("CBS Maker 1"))&& !limitblocking.equalsIgnoreCase("4")) {
//                                        validationDetails.addWarning(ValidationDetails.WarningType.Other,
//                                                    "Please input the LOB limit id which is attached in credit facility [CM]");
//                                  }
                                    if (facility != null && !facility.equalsIgnoreCase("")) {
                                          try {
                                                try {
                                                      int count = 0;
                                                      String query = "select count(*) from XMLAPISTO where FACILITYID='" + facility + "'";
                                                      con = getConnection();
                                                      ps1 = con.prepareStatement(query);
                                                      rs1 = ps1.executeQuery();
                                                      if (rs1.next()) {
                                                            count = rs1.getInt(1);

                                                      }

                                                      /*
                                                       * if ((count == 0 || count < 1) &&
                                                       * (step_csm.equalsIgnoreCase("CBS Authoriser"))) {
                                                       * validationDetails.addWarning(ValidationDetails.WarningType.Other,
                                                       * "Please enter valid LOB limit id [CM]"); }
                                                       */

                                                } catch (Exception e) {
                                                      Loggers.general().info(LOG,
                                                                  "Exception ODC finance limit id validation===>" + e.getMessage());
                                                }

                                                try {
                                                      String lobCreate = getWrapper().getLOB().trim();
                                                      String lobfinance = "";
                                                      String query = "SELECT exte.LOB FROM master mas, BASEEVENT bev, BASEEVENT bev1, EXTEVENT exte WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = bev1.ATTACHD_EV AND bev1.KEY97 = exte.EVENT AND trim(exte.LOB) IS NOT NULL AND mas.MASTER_REF = '"
                                                                  + MasterReference + "'";
                                                      /*
                                                       * if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                       * //Loggers.general().info(LOG,"ELC Attched finance LOB code===>" + query); }
                                                       */
                                                      con = getConnection();
                                                      ps1 = con.prepareStatement(query);
                                                      rs1 = ps1.executeQuery();
                                                      if (rs1.next()) {
                                                            lobfinance = rs1.getString(1).trim();

                                                      }

                                                } catch (Exception e) {
                                                      Loggers.general().info(LOG,
                                                                  "Exception ODC authorized validation===>" + e.getMessage());
                                                }

                                          } catch (Exception e) {
                                                Loggers.general().info(LOG, "Exception in connection closed===>" + e.getMessage());
                                          } finally {
                                                try {

                                                      if (rs1 != null)
                                                            rs1.close();
                                                      if (ps1 != null)
                                                            ps1.close();
                                                      if (con != null)
                                                            con.close();
                                                } catch (SQLException e) {
                                                      // Loggers.general().info(LOG,"Connection Failed!
                                                      // Check output
                                                      // console");
                                                      e.printStackTrace();
                                                }
                                          }
                                    } else {
                                          /*
                                           * if (dailyval_Log.equalsIgnoreCase("YES")) { //
                                           * Loggers.general().info(LOG,"ELC finance limit id blank===>"); }
                                           */
                                    }

                              } else {
                                    /*
                                     * if (dailyval_Log.equalsIgnoreCase("YES")) {
                                     * //Loggers.general().info(LOG,"ELC finance not created===>" + finaceCreate); }
                                     */
                              }

                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Exception ELC finance validation===>" +
                              // e.getMessage());
                        }

                  }

                  try {
                        getPerdChgEndDate();

                  } catch (Exception ee) {
                        Loggers.general().info(LOG, ee.getMessage());
                  }

                  try {

                        getGoodsCategory();

                  } catch (Exception ee) {
                        // Loggers.general().info(LOG,ee.getMessage());
                        // //Loggers.general().info(LOG,"LOB Catch");
                  }
}

                  try {
                        // //Loggers.general().info(LOG,"getChargeBasis");
                        if (getMajorCode().equalsIgnoreCase("ILC")) {
                              getChargeBasis();
                        }

                  } catch (Exception ee) {
                        // Loggers.general().info(LOG,ee.getMessage());
                        // //Loggers.general().info(LOG,"Service TAX Catch");
                  }

                  try {
                        // //Loggers.general().info(LOG,"getChargeBasis");
                        if (getMajorCode().equalsIgnoreCase("ILC") && getMinorCode().equalsIgnoreCase("NAMI")) {

                              String newLia = getDriverWrapper().getEventFieldAsText("cAAX", "v", "m");
                              String oldLia = getDriverWrapper().getEventFieldAsText("cACP", "v", "m");
                              String newCur = getDriverWrapper().getEventFieldAsText("cAAX", "v", "c");
                              /*
                               * if (dailyval_Log.equalsIgnoreCase("YES")) {
                               *
                               * //Loggers.general().info(LOG,"New total liability" + newLia);
                               * //Loggers.general().info(LOG,"Old total liability" + oldLia); }
                               */
                              BigDecimal newVal = new BigDecimal(newLia);
                              BigDecimal oldVal = new BigDecimal(oldLia);

                              BigDecimal newValue = newVal.subtract(oldVal);
                              ConnectionMaster connectionMaster = new ConnectionMaster();
                              String divideByDecimal = connectionMaster.getDecimalforCur(newCur);
                              if (divideByDecimal.equalsIgnoreCase("1")) {

                                    DecimalFormat diff = new DecimalFormat("0");
                                    diff.setMaximumFractionDigits(1);
                                    String totalVal = diff.format(newValue);
                                    /*
                                     * if (dailyval_Log.equalsIgnoreCase("YES")) { //
                                     * Loggers.general().info(LOG,"New Total liability final value<==1==>" +
                                     * totalVal + "" + newCur); }
                                     */
                                    if (newValue.compareTo(BigDecimal.ZERO) > 0) {
                                          getPane().setINTLIAMT(totalVal + " " + newCur);
                                    } else {
                                          totalVal = "0";
                                          getPane().setINTLIAMT(totalVal + " " + newCur);
//    Loggers.general().info(LOG,"New Total liability final value<==3==>" + totalVal + "" + newCur);
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
                                     * Loggers.general().info(LOG,"New Total liability final value<==2==>" +
                                     * totalVal + "" + newCur); }
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
                        Loggers.general().info(LOG, "Exception New total liability" + e.getMessage());

                  }

                  // Non Constituent borrower
                  try {
                        String constituent = getDriverWrapper().getEventFieldAsText("BEN", "p", "no").trim();
                        // //Loggers.general().info(LOG,"Non Constituent borrower
                        // constituent--------->" +
                        // constituent);
                        if (constituent.length() > 0) {
                              // //Loggers.general().info(LOG,"Non Constituent borrower
                              // constituent--------->" + constituent);
                              String alpha_val = constituent.replaceAll("[^A-Za-z]+", "");
                              // //Loggers.general().info(LOG,"alpha_val---->" + alpha_val);
                              if (alpha_val.length() > 0) {
                                    String alpha_fin = alpha_val.substring(0, 4);
                                    // //Loggers.general().info(LOG,"Non Constituent borrower
                                    // constituent
                                    // after convert--------->" + alpha_fin);
                                    if (getMajorCode().equalsIgnoreCase("ELC")) {
                                          if (alpha_fin.equalsIgnoreCase("LCUS")) {
                                                getPane().setCONSBORR(true);
                                                // //Loggers.general().info(LOG," Non Constituent
                                                // borrower
                                                // ELC
                                                // if
                                                // loop--------->");
                                          } else {
                                                getPane().setCONSBORR(false);
                                          }
                                    } else {
                                          getPane().setCONSBORR(false);
                                          // //Loggers.general().info(LOG," Non Constituent borrower
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

                              // //Loggers.general().info(LOG," Non Constituent borrower is
                              // empty--------->");
                        }
                  } catch (Exception e) {

                  }

                  // Invoice details validation
                  String systemDat = getDriverWrapper().getEventFieldAsText("TDY", "d", "");
                  try {
                        if ((step_Input.equalsIgnoreCase("i")) && (step_Id.equalsIgnoreCase("CBS Maker"))
                                    && getMajorCode().equalsIgnoreCase("IDC")) {
                              // //Loggers.general().info(LOG,"Invoice Validate Called");
                              List<ExtEventInvoiceData> Invoic = (List<ExtEventInvoiceData>) getWrapper()
                                          .getExtEventInvoiceData();
                              if (Invoic.size() < 1
                                          && (getMinorCode().equalsIgnoreCase("NCAJ") || getMinorCode().equalsIgnoreCase("NCAM"))) {
                                    validationDetails.addError(ErrorType.Other, "Invoice details is mandatory  [CM]");
                              }

                              else {
                                    //// Loggers.general().info(LOG,"Invoice data is not empty");
                              }
                        }

                        List<ExtEventInvoicelc> invoice1 = (List<ExtEventInvoicelc>) getWrapper().getExtEventInvoicelc();

                        if (invoice1.size() == 0 || invoice1.size() < 1) {
                              if (getMajorCode().equalsIgnoreCase("ILC") && getMinorCode().equalsIgnoreCase("CRC")) {

                                    validationDetails.addWarning(WarningType.Other, "Invoice details grid is mandatory[CM]");
                              }
                        }

                        String payamount = "";
                        double amtVal = 0;
                        double invadob = 0;
                        double final_payamountlong = 0;
                        for (int l = 0; l < invoice1.size(); l++) {
                              ExtEventInvoicelc invoicedetails = invoice1.get(l);
                              payamount = getDriverWrapper().getEventFieldAsText("AMT", "v", "m");
                              // //Loggers.general().info(LOG,"Collection amount ----->" +
                              // payamt);
                              String paycur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                              double invamt = invoicedetails.getINVAM().doubleValue();
                              // //Loggers.general().info(LOG,"Invoice grid initial amount ----->"
                              // +
                              // invamt);
                              /*
                               * if (dailyval_Log.equalsIgnoreCase("YES")) {
                               *
                               * //Loggers.general().info(LOG,"Invoice grid initial amount ----->" + invamt);
                               * }
                               */
                              String invcurr = invoicedetails.getINVAMCurrency();

                              amtVal = amtVal + invamt;
                              // //Loggers.general().info(LOG,"Invoice amount after add ----->" +
                              // amount);
                              /*
                               * if (dailyval_Log.equalsIgnoreCase("YES")) {
                               *
                               * //Loggers.general().info(LOG,"Invoice amount after ad ----->" + amtVal); }
                               */

                              ConnectionMaster connectionMaster = new ConnectionMaster();
                              double divideByDecimal = connectionMaster.getDecimalforCurrency(invcurr);
                              invadob = amtVal / divideByDecimal;

                              final_payamountlong = Double.parseDouble(payamount);
                              // //Loggers.general().info(LOG,"payamtflot for conpare ----->" +
                              // final_payamtlong + "<<");
                              // //Loggers.general().info(LOG,"invoice final amount for conpare
                              // ----->"
                              // + invadob_tot + "<<");

                              /*
                               * if (dailyval_Log.equalsIgnoreCase("YES")) {
                               *
                               * // Loggers.general().info(LOG,"payamtflot for conpare ----->" +
                               * final_payamountlong + "<<"); //
                               * Loggers.general().info(LOG,"invoice final amount for conpare ----->" +
                               * invadob + "<<"); }
                               */
                              String deteNeg = String.valueOf(invoicedetails.getINDTE());
                              // Loggers.general().info(LOG,"Invoice date " + deteNeg +" SIZE
                              // "+invoice1.size());

                              if (invoice1.size() > 0 && (!deteNeg.isEmpty()) && (getMajorCode().equalsIgnoreCase("ILC"))
                                          && (getMinorCode().equalsIgnoreCase("CRC")) && (step_Input.equalsIgnoreCase("i"))
                                          && (step_Id.equalsIgnoreCase("CBS Maker"))) {
                                    if ((deteNeg.matches("(^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$)"))) {

                                          // Loggers.general().info(LOG,"Date of invoice is valid ");
                                    } else {
                                          // //Loggers.general().info(LOG,"Date of invoice is not
                                          // valid
                                          // ");
                                          validationDetails.addError(ErrorType.Other,
                                                      "Invalid Date format(DD/MM/YYYY) in Invoice date grid  [CM]");

                                    }
                              } else {
                                    // Loggers.general().info(LOG,"Date of invoice is empty valid
                                    // ");

                              }
                              String invdate = String.valueOf(invoicedetails.getINDTE());
                              if (invoice1.size() > 0) {
                                    // Loggers.general().info(LOG,"Invoice Date---> " + invdate);
                                    try {
                                          SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yy");
                                          Date shipbillDate = (Date) formatter1.parse(invdate);
                                          Date sysDate = formatter1.parse(systemDat);
                                          // Loggers.general().info(LOG,"sysDate Date---> " +
                                          // sysDate);
                                          // Loggers.general().info(LOG,"shipbillDate -----> " +
                                          // shipbillDate);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                // Loggers.general().info(LOG,"system Date for Invoice validation---> " +
                                                // sysDate);
                                          }
                                          if (invoice1.size() > 0 && shipbillDate.after(sysDate)
                                                      && getMajorCode().equalsIgnoreCase("ILC")) {
                                                // Loggers.general().info(LOG,"date1 is before Date2");
                                                validationDetails.addError(ErrorType.Other,
                                                            "Invoice date is future date than the today date [CM]");
                                          } else {

                                                /*
                                                 * if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                 *
                                                 * // Loggers.general().info(LOG,"----date3 is after Date2 shipbillDate -----> "
                                                 * + shipbillDate); }
                                                 */
                                          }
                                    } catch (Exception e) {

                                          /*
                                           * if (dailyval_Log.equalsIgnoreCase("YES")) {
                                           *
                                           * //Loggers.general().info(LOG,"Invoice date is future date" + e.getMessage());
                                           * }
                                           */
                                    }
                              } else {
                                    // Loggers.general().info(LOG,"sysDate Date else---> ");

                              }

                        }
                        /*
                         * if (dailyval_Log.equalsIgnoreCase("YES")) {
                         *
                         * // Loggers.general().info(LOG,"invoice and master amount not equal---->" +
                         * invadob + "" + final_payamountlong); }
                         */
                        if (invadob > 0 && (invadob != final_payamountlong)) {

                              if (getMajorCode().equalsIgnoreCase("ILC") && getMinorCode().equalsIgnoreCase("CRC")
                                          && (step_Input.equalsIgnoreCase("i"))
                                          && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id.equalsIgnoreCase("CBS Maker 1"))) {

                                    validationDetails.addWarning(WarningType.Other,
                                                "Claim amount should be equal to Invoice grid amount [CM]");
                              }
                        } else {
                              /*
                               * if (dailyval_Log.equalsIgnoreCase("YES")) {
                               *
                               * // Loggers.general().info(LOG, //
                               * "invoice and master amount not equal else===>" + invadob + "" +
                               * final_payamountlong); }
                               */
                        }
                  } catch (Exception e) {
                        /*
                         * if (dailyval_Log.equalsIgnoreCase("YES")) {
                         *
                         * //Loggers.general().info(LOG,"Exception invoice and master amount not equal"
                         * + e.getMessage()); }
                         */
                  }

                  // INVOICE VALIDATION
                  try {

                        double invadob_tot = 0.0;
                        double amount = 0.0;
                        String payamt = "0";
                        double final_payamtlong = 0.0;
                        if (getMinorCode().equalsIgnoreCase("NCAJ")
                                    || getMinorCode().equalsIgnoreCase("NCAM") && getMajorCode().equalsIgnoreCase("IDC")) {

                              List<ExtEventInvoiceData> invoice = (List<ExtEventInvoiceData>) getWrapper()
                                          .getExtEventInvoiceData();

                              for (int l = 0; l < invoice.size(); l++) {
                                    ExtEventInvoiceData invoicedat = invoice.get(l);
                                    payamt = getDriverWrapper().getEventFieldAsText("AMT", "v", "m");
                                    // //Loggers.general().info(LOG,"Collection amount ----->" +
                                    // payamt);
                                    String paycur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                                    double invamt = invoicedat.getINAMOUT().doubleValue();
                                    // //Loggers.general().info(LOG,"Invoice grid initial amount
                                    // ----->"
                                    // +
                                    // invamt);
                                    String invcurr = invoicedat.getINAMOUTCurrency();

                                    amount = amount + invamt;
                                    // //Loggers.general().info(LOG,"Invoice amount after add
                                    // ----->" +
                                    // amount);

                                    ConnectionMaster connectionMaster = new ConnectionMaster();
                                    double divideByDecimal = connectionMaster.getDecimalforCurrency(invcurr);
                                    invadob_tot = amount / divideByDecimal;

                                    final_payamtlong = Double.parseDouble(payamt);
                                    // //Loggers.general().info(LOG,"payamtflot for conpare ----->"
                                    // +
                                    // final_payamtlong + "<<");
                                    // //Loggers.general().info(LOG,"invoice final amount for
                                    // conpare
                                    // ----->"
                                    // + invadob_tot + "<<");

                                    if ((!paycur.equalsIgnoreCase(invcurr)) && getMinorCode().equalsIgnoreCase("NCAJ")
                                                || getMinorCode().equalsIgnoreCase("NCAM") && getMajorCode().equalsIgnoreCase("IDC")) {
                                          // validationDetails.addError(ErrorType.Other,
                                          // "Collection
                                          // amount currency should be equal to Invoice grid
                                          // currency
                                          // [CM]");
                                    }

                              }

                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    // Loggers.general().info(LOG,
                                    // " invoice and master amount not equal amend" + invadob_tot + "" +
                                    // final_payamtlong);
                              }

                              if ((invadob_tot != final_payamtlong)) {
                                    // //Loggers.general().info(LOG," invoice and master amount not
                                    // equal
                                    // ---->1" + invadob_tot + "" + final_payamtlong);
                                    if (getMinorCode().equalsIgnoreCase("NCAJ") || getMinorCode().equalsIgnoreCase("NCAM")
                                                && getMajorCode().equalsIgnoreCase("IDC") && (step_Input.equalsIgnoreCase("i"))
                                                && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id.equalsIgnoreCase("CBS Maker 1"))) {

                                          validationDetails.addWarning(WarningType.Other,
                                                      "Collection amount should be equal to Invoice grid amount [CM]");
                                    }
                              } else {

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          // Loggers.general().info(LOG," invoice and master amount not equal amend else"
                                          // + invadob_tot + ""
                                          // + final_payamtlong);
                                    }
                              }

                        }
                        List<ExtEventInvoiceData> invoice = (List<ExtEventInvoiceData>) getWrapper().getExtEventInvoiceData();
                        for (int j = 0; j < invoice.size(); j++) {
                              ExtEventInvoiceData invoicedat = invoice.get(j);
                              String deteNeg = String.valueOf(invoicedat.getINDATE());
                              // //Loggers.general().info(LOG,"Invoice date " + deteNeg);

                              if (invoice.size() > 0 && (!deteNeg.isEmpty()) && (getMajorCode().equalsIgnoreCase("IDC"))
                                          && (step_Input.equalsIgnoreCase("i")) && (step_Id.equalsIgnoreCase("CBS Maker"))) {
                                    if ((deteNeg.matches("(^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$)"))) {

                                    } else {
                                          // //Loggers.general().info(LOG,"Date of invoice is not
                                          // valid
                                          // ");
                                          validationDetails.addError(ErrorType.Other,
                                                      "Invalid Date format(DD/MM/YYYY) in Invoice date grid  [CM]");

                                    }
                              } else {

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          // Loggers.general().info(LOG,"Date of invoice is empty valid amend");
                                    }

                              }
                              String invdate = String.valueOf(invoicedat.getINDATE());
                              if (invoice.size() > 0) {
                                    // Loggers.general().info(LOG,"Invoice Date---> " + invdate);
                                    try {
                                          SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yy");
                                          Date shipbillDate = (Date) formatter1.parse(invdate);
                                          Date sysDate = formatter1.parse(systemDat);
                                          // Loggers.general().info(LOG,"sysDate Date---> " +
                                          // sysDate);
                                          // Loggers.general().info(LOG,"shipbillDate -----> " +
                                          // shipbillDate);
                                          if (invoice.size() > 0 && shipbillDate.after(sysDate)
                                                      && (getMajorCode().equalsIgnoreCase("IDC"))) {
                                                // Loggers.general().info(LOG,"date1 is before Date2");
                                                validationDetails.addError(ErrorType.Other,
                                                            "Invoice date is future date than the today date [CM]");
                                          } else {
                                                // Loggers.general().info(LOG,"----date3 is after Date2
                                                // shipbillDate -----> " + shipbillDate + ""
                                                // + invoice.size());
                                          }
                                    } catch (Exception e) {
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                // Loggers.general().info(LOG,"Date of invoice is empty valid amend" +
                                                // e.getMessage());
                                          }
                                    }
                              } else {

                              }

                        }
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,e.getMessage());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              // Loggers.general().info(LOG,"Exception in invoice valid amend" +
                              // e.getMessage());
                        }
                  }
                  String masterRefNumber = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  String eventPrefix = getDriverWrapper().getEventFieldAsText("EVCD", "s", "");

                  // get from and to fields to shipping details
                  // if ((getMajorCode().equalsIgnoreCase("ELC") ||
                  // getMinorCode().equalsIgnoreCase("DOP"))
                  // || (getMajorCode().equalsIgnoreCase("ILC") ||
                  // getMinorCode().equalsIgnoreCase("CRC"))) {
                  if ((getMajorCode().equalsIgnoreCase("ILC") || getMinorCode().equalsIgnoreCase("CRC"))) {
                        try {

                              // hscodeval);
                              String hyperValue = "select lcm.TAX_PAY_BY,lcm.OUR_CHGS,lcm.OVR_CHGS,lcm.LOADING,lcm.DISCHARGE from master mas, lcmaster lcm where mas.KEY97 = lcm.KEY97 and mas.MASTER_REF  = '"
                                          + masterRefNumber + "'";
                              // //Loggers.general().info(LOG,"TI field query Value---->" +
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
                                    // //Loggers.general().info(LOG,"port code description---->" +

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
                              // Loggers.general().info(LOG,e.getMessage());
                        } finally {
                              try {
                                    if (rs != null)
                                          rs.close();
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
                  } else {
                        //// Loggers.general().info(LOG,"--------->" + getMajorCode() +
                        //// getMinorCode());

                  }

                  // port code population
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
                                    //// Loggers.general().info(LOG,"Connection Failed! Check output
                                    //// console");
                                    e.printStackTrace();
                              }
                        }
                  } else {
                        // //Loggers.general().info(LOG,"port code is empty");
                  }
                  String custVal = "";
                  if (getMinorCode().equalsIgnoreCase("POC")) {
                        custVal = getDriverWrapper().getEventFieldAsText("PRM", "p", "no").trim();
                  } else if (getMajorCode().equalsIgnoreCase("IDC") && getMinorCode().equalsIgnoreCase("CLP")) {
                        custVal = getDriverWrapper().getEventFieldAsText("DRE", "p", "no").trim();
                  }

                  // buyer credit field reference no checking

                  String inwardremNO = getWrapper().getPREBUYRE();
                  // //Loggers.general().info(LOG,"Buyer credit reference no ---------->" +
                  // inwardremNO);
                  if ((!inwardremNO.equalsIgnoreCase("") && inwardremNO.length() > 0)
                              && (getMinorCode().equalsIgnoreCase("CLP") || getMinorCode().equalsIgnoreCase("POC"))) {
                        try {
                              String mercht = getDriverWrapper().getEventFieldAsText("cBHW", "l", "").toString();

                              int dmT = 0;

                              String dms = "SELECT DISTINCT DECODE(trim(mas.MASTER_REF) ,NULL ,0,1)AS REMERREF FROM master mas, BASEEVENT bev, EXTEVENT exte, PRODTYPE prod WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = exte.EVENT AND mas.PRODTYPE = prod.KEY97 AND mas.MASTER_REF ='"
                                          + inwardremNO + "' AND mas.PRICUSTMNM ='" + custVal + "'";
                              // Loggers.general().info(LOG,"Master ref no valid for Import lc" +
                              // dms);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    // Loggers.general().info(LOG,"Master ref number valid query" + dms);

                              }
                              con = ConnectionMaster.getConnection();
                              ps = con.prepareStatement(dms);
                              rs = ps.executeQuery();
                              while (rs.next()) {
                                    dmT = rs.getInt(1);
                                    // Loggers.general().info(LOG,"AFTER GET THE VALUE " + dmT);

                              }
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    // Loggers.general().info(LOG,"Master ref no valid count" + dmT);

                              }
//Loggers.general().info(LOG,"ILC=====>buyers credit");
                              if ((dmT == 0 || dmT < 1) && inwardremNO.length() > 0 && mercht.equalsIgnoreCase("Y")
                                          && (step_Input.equalsIgnoreCase("i"))
                                          && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id.equalsIgnoreCase("CBS Maker 1"))) {
                                    validationDetails.addWarning(WarningType.Other,
                                                "Buyer's Credit reference number is not valid number,Kindly check the valid reference number with CRN [CM]");

                              }

                              else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          // Loggers.general().info(LOG,
                                          // "Master ref number is valid in else" + dmT + "buyer credit check box" +
                                          // mercht);

                                    }
                              }

                        } catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    // Loggers.general().info(LOG,"Exception in Master ref no validation" +
                                    // e.getMessage());
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
                                    // Loggers.general().info(LOG,"Connection Failed! Check output
                                    // console");
                                    e.printStackTrace();
                              }
                        }
                  } else {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              // Loggers.general().info(LOG,"Master ref number for BCR" + inwardremNO);
                        }

                  }

                  try {
                        // //Loggers.general().info(LOG,"IE code validation ");
                        List<ExtEventShippingdetailslc> shpcol5 = (List<ExtEventShippingdetailslc>) getWrapper()
                                    .getExtEventShippingdetailslc();

                        if (shpcol5.size() > 0) {
                              int count = 0;
                              for (int l = 0; l < shpcol5.size(); l++) {
                                    ExtEventShippingdetailslc shipCol = shpcol5.get(l);

                                    String recind = shipCol.getRECIN().trim();
                                    String shipbill = shipCol.getLCBILLNO();
                                    //// Loggers.general().info(LOG,"Record indicator is===>" +
                                    //// recind);

                                    if (recind.equalsIgnoreCase("3") && (step_Input.equalsIgnoreCase("i"))
                                                && (step_Id.equalsIgnoreCase("CBS Maker"))) {
                                          validationDetails.addError(ErrorType.Other,
                                                      "Shipping bill is already cancelled (" + shipbill + ")  [CM]");
                                    } else {
                                          //// Loggers.general().info(LOG,"recind is not 3===>" +
                                          //// recind);
                                    }

                              }

                        }

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"IE CODE exception " + e.getMessage());
                  }

                  String presendRef = getDriverWrapper().getEventFieldAsText("PRB", "r", "");
                  if (presendRef != null && presendRef.length() > 0) {
                        try {

                              String prod = getDriverWrapper().getEventFieldAsText("PCO", "s", "");

                              String query7 = "SELECT count(*) FROM BASEEVENT BSE, MASTER MAS where BSE.MASTER_KEY=MAS.KEY97 AND BSE.THEIR_REF='"
                                          + presendRef + "' and MAS.REFNO_PFIX='" + prod + "'";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    // Loggers.general().info(LOG,"Presenter Reference number query===>" + query7);
                              }
                              con = ConnectionMaster.getConnection();
                              ps = con.prepareStatement(query7);
                              rs = ps.executeQuery();
                              int count2 = 0;
                              while (rs.next()) {
                                    count2 = rs.getInt(1);
                                    // //Loggers.general().info(LOG,"Count value for REFERENCE VALUE
                                    // DUPLICATE---------->" + count2);
                              }
                              if (presendRef != null && presendRef.length() > 0 && count2 > 1
                                          && (step_Input.equalsIgnoreCase("i"))
                                          && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id.equalsIgnoreCase("CBS Maker 1"))
                                          && getMajorCode().equalsIgnoreCase("ILC") && getMinorCode().equalsIgnoreCase("CRC")) {

                                    validationDetails.addWarning(WarningType.Other,
                                                "Presenter's Reference number already exist [CM]");

                              } else {
                                    // //Loggers.general().info(LOG,"Reference number is new");

                              }

                        }

                        catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Exception Presenter Reference number===>" + e.getMessage());
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
                  }

                  // BOE validation

                  // String systemDate = getDriverWrapper().getEventFieldAsText("TDY",
                  // "d", "");
                  // //Loggers.general().info(LOG,"systemDate " + systemDate);
                  List<ExtEventBOECAP> BOECAP = (List<ExtEventBOECAP>) getWrapper().getExtEventBOECAP();
                  for (int j = 0; j < BOECAP.size(); j++) {
                        ExtEventBOECAP boeval = BOECAP.get(j);
                        String billdate = String.valueOf(boeval.getBOEDAT());
                        // //Loggers.general().info(LOG,"BOE Date " + billdate);
                        try {
                              // //Loggers.general().info(LOG,"1");
                              SimpleDateFormat parseFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                              Date date1 = parseFormat.parse(billdate);// grid enter SHIP
                                                                                                // BILL
                                                                                                // date
                              SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yy");
                              String result1 = formatter1.format(date1);
                              Date shipbillDate = (Date) formatter1.parse(result1);
                              Date sysDate = formatter1.parse(systemDat);
                              if (shipbillDate.after(sysDate) && (step_Input.equalsIgnoreCase("i"))
                                          && (step_Id.equalsIgnoreCase("CBS Maker"))) {
                                    // //Loggers.general().info(LOG,"date1 is before Date2");
                                    validationDetails.addError(ErrorType.Other, "BOE date is future date than the today date [CM]");
                              } else {
                                    // //Loggers.general().info(LOG,"----date3 is after Date2---");
                              }
                        } catch (Exception e) {
                              // Loggers.general().info(LOG,e.getMessage());
                        }
                  }

                  String grwai = getDriverWrapper().getEventFieldAsText("cAOJ", "l", "").trim();
                  //// Loggers.general().info(LOG,"GR WAIVER grwaiver---" + grwai);

                  String formtyp = getWrapper().getFORTYP();
                  String sezCust = getDriverWrapper().getEventFieldAsText("PRM", "p", "cBBI").trim();
                  // Loggers.general().info(LOG,"Form type for SEZ" + formtyp + " SEZ
                  // customer" + sezCust);
                  if (sezCust.equalsIgnoreCase("SEZ") && getMinorCode().equalsIgnoreCase("DOP")
                              && prd_typ.equalsIgnoreCase("ELF")) {
                        if (!formtyp.equalsIgnoreCase("EXEMPTED") && (step_Input.equalsIgnoreCase("i"))
                                    && (step_csm.equalsIgnoreCase("CBS Maker 1") || step_csm.equalsIgnoreCase("CBS Maker")
                                                || step_csm.equalsIgnoreCase("CBS Reject"))) {
                              validationDetails.addError(ErrorType.Other,
                                          "Please select Form type as EXEMPTED as Exporter category is SEZ [CM]");
                        } else {

                        }

                  }

                  List<ExtEventShippingTable> shipgrid = (List<ExtEventShippingTable>) getWrapper()
                              .getExtEventShippingTable();

                  if ((prd_typ.equalsIgnoreCase("ELF")) && formtyp.equalsIgnoreCase("EXEMPTED")) {
                        if (shipgrid.size() > 0
                                    && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker 1")
                                                || step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Reject"))
                                    && getMinorCode().equalsIgnoreCase("DOP")) {

                              validationDetails.addError(ErrorType.Other,
                                          "Form type is selected as 'EXEMPTED' Kindly delete the shipping bill details [CM]");
                        }
                  }

                  // //Loggers.general().info(LOG,"GR WAIVER grwai ---" + grwai);
                  // //Loggers.general().info(LOG,"product type is ---" + prd_typ);
                  String mixedpay = getDriverWrapper().getEventFieldAsText("MIX", "l", "");
                  double mixpay_tot = 0;
                  double am = 0;
                  double am_rey = 0;
                  // long repay_total = 0;
                  double short_total = 0;
                  double amountdob = 0;
                  double repay_Amt = 0.0;
                  if ((!grwai.equalsIgnoreCase("Y")) && grwai.equalsIgnoreCase("N")) {
                        if (prd_typ.equalsIgnoreCase("ELF") && (step_Input.equalsIgnoreCase("i"))
                                    && (step_csm.equalsIgnoreCase("CBS Maker 1") || step_csm.equalsIgnoreCase("CBS Maker")
                                                || step_csm.equalsIgnoreCase("CBS Reject"))
                                    && grwai.equalsIgnoreCase("N") && (!formtyp.equalsIgnoreCase("EXEMPTED"))) {

                              try {

                                    // //Loggers.general().info(LOG,"shipment validation start");
                                    // validatiommjkvdv>>>>>>>>>>>>>??????????????????? ");
                                    List<ExtEventShippingTable> shiplcd = (List<ExtEventShippingTable>) getWrapper()
                                                .getExtEventShippingTable();

                                    if (shiplcd.size() == 0 && (step_Input.equalsIgnoreCase("i"))
                                                && (step_csm.equalsIgnoreCase("CBS Maker 1") || step_csm.equalsIgnoreCase("CBS Maker")
                                                            || step_csm.equalsIgnoreCase("CBS Reject"))
                                                && getMinorCode().equalsIgnoreCase("DOP")) {

                                          // //Loggers.general().info(LOG,"size of grid inside if loop
                                          // "
                                          // + shiplcd.size());
                                          validationDetails.addError(ErrorType.Other, "Shipping Bill details is mandatory [CM]");
                                    } else {

                                          List<ExtEventShippingdetailslc> shiplc = (List<ExtEventShippingdetailslc>) getWrapper()
                                                      .getExtEventShippingdetailslc();
                                          List<ExtEventInvoicedetailsLC> invlc = (List<ExtEventInvoicedetailsLC>) getWrapper()
                                                      .getExtEventInvoicedetailsLC();

                                          if ((formtyp.trim().equalsIgnoreCase("EDI") || formtyp.trim().equalsIgnoreCase("SOFTEX"))
                                                      && shiplc.size() == 0 && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_csm.equalsIgnoreCase("CBS Maker 1")
                                                                  || step_csm.equalsIgnoreCase("CBS Maker")
                                                                  || step_csm.equalsIgnoreCase("CBS Reject"))
                                                      && getMinorCode().equalsIgnoreCase("DOP")) {

                                                // //Loggers.general().info(LOG,"Size of grid
                                                // shipping
                                                // details if loop " + shiplc.size());
                                                validationDetails.addError(ErrorType.Other,
                                                            "Please click Fetch button to get the Shipping bill details [CM]");
                                          } else if (formtyp.trim().equalsIgnoreCase("EDF") && shiplc.size() == 0
                                                      && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_csm.equalsIgnoreCase("CBS Maker 1")
                                                                  || step_csm.equalsIgnoreCase("CBS Maker")
                                                                  || step_csm.equalsIgnoreCase("CBS Reject"))
                                                      && getMinorCode().equalsIgnoreCase("DOP")) {

                                                // //Loggers.general().info(LOG,"Size of grid
                                                // shipping
                                                // details if loop " + shiplc.size());
                                                validationDetails.addError(ErrorType.Other,
                                                            "Kindly input shipping bill details manually as Form Type is 'EDF' [CM]");
                                          } else {
                                                Loggers.general().info(LOG, "Form type is" + formtyp);
                                          }

                                          if ((formtyp.trim().equalsIgnoreCase("EDI") || formtyp.trim().equalsIgnoreCase("SOFTEX"))
                                                      && invlc.size() == 0 && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_csm.equalsIgnoreCase("CBS Maker 1")
                                                                  || step_csm.equalsIgnoreCase("CBS Maker")
                                                                  || step_csm.equalsIgnoreCase("CBS Reject"))
                                                      && getMinorCode().equalsIgnoreCase("DOP")) {

                                                // //Loggers.general().info(LOG,"Size of grid
                                                // shipping
                                                // details if loop " + invlc.size());
                                                validationDetails.addError(ErrorType.Other,
                                                            "Please click Fetch button to get the Invoice details [CM]");
                                          } else if (formtyp.trim().equalsIgnoreCase("EDF") && invlc.size() == 0
                                                      && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_csm.equalsIgnoreCase("CBS Maker 1")
                                                                  || step_csm.equalsIgnoreCase("CBS Maker")
                                                                  || step_csm.equalsIgnoreCase("CBS Reject"))
                                                      && getMinorCode().equalsIgnoreCase("DOP")) {

                                                // //Loggers.general().info(LOG,"Size of grid
                                                // shipping
                                                // details if loop " + invlc.size());
                                                validationDetails.addError(ErrorType.Other,
                                                            "Kindly input Invoice bill details manually as Form Type is 'EDF' [CM]");
                                          } else {
                                                // Loggers.general().info(LOG,"Form type is" + formtyp);
                                          }

                                          String billrow = shiplcd.get(0).getBILLNUM();
                                          // //Loggers.general().info(LOG,"Shipping Number billrow
                                          // softax--->" + billrow);
                                          String formNo = shiplcd.get(0).getFORMNUM();
                                          // Loggers.general().info(LOG,"Form Number softax--->" +
                                          // formNo);

                                          if (formtyp.trim().equalsIgnoreCase("EDI") && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_csm.equalsIgnoreCase("CBS Maker 1")
                                                                  || step_csm.equalsIgnoreCase("CBS Maker")
                                                                  || step_csm.equalsIgnoreCase("CBS Reject"))
                                                      && getMinorCode().equalsIgnoreCase("DOP")) {
                                                if ((billrow.trim().equalsIgnoreCase(""))
                                                            || billrow.trim().isEmpty() && getMinorCode().equalsIgnoreCase("DOP")) {
                                                      // //Loggers.general().info(LOG,"Shipping Number
                                                      // blank
                                                      // softax--->");
                                                      validationDetails.addError(ErrorType.Other,
                                                                  "Form Type: EDI,Shipping Bill No is Mandatory in shipping details pane :[CM]");
                                                }
                                          }

                                          if (formtyp.trim().equalsIgnoreCase("SOFTEX") && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_csm.equalsIgnoreCase("CBS Maker 1")
                                                                  || step_csm.equalsIgnoreCase("CBS Maker")
                                                                  || step_csm.equalsIgnoreCase("CBS Reject"))
                                                      && getMinorCode().equalsIgnoreCase("DOP")) {
                                                if ((formNo.trim().equalsIgnoreCase(""))
                                                            || formNo.trim().isEmpty() && getMinorCode().equalsIgnoreCase("DOP")) {
                                                      validationDetails.addError(ErrorType.Other,
                                                                  "Form Type:SOFTEX,Form No is Mandatory in shipping details pane :[CM]");
                                                }
                                          }

                                          if (shiplcd.size() > 0) {
                                                //// Loggers.general().info(LOG,"shipment validation in
                                                //// else part");
                                                // //Loggers.general().info(LOG,"size shipping grid " +
                                                // shiplcd.size());
                                                String AmountS = getDriverWrapper().getEventFieldAsText("AMT", "v", "m").trim();
                                                String cur1 = getDriverWrapper().getEventFieldAsText("AMT", "v", "c").trim();

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
                                                      // //Loggers.general().info(LOG,"shipment validation
                                                      // in
                                                      // for
                                                      // loop");

                                                      shipinglc = shiplcd.get(l);
                                                      if (getMajorCode().equalsIgnoreCase("ELC")
                                                                  && getMinorCode().equalsIgnoreCase("DOP")) {
                                                            BigDecimal shipAmt = new BigDecimal("0");
                                                            BigDecimal notVal = new BigDecimal("1");
                                                            shipinglc.setREPAYAM(shipAmt);
                                                            shipinglc.setREPAYAMCurrency(shipinglc.getSHPAMTCurrency());
                                                            shipinglc.setSHCOLAM(shipAmt);
                                                            shipinglc.setSHCOLAMCurrency(shipinglc.getSHPAMTCurrency());
                                                            shipinglc.setNOTIONAL(notVal);
                                                      }

                                                      try {
                                                            String billrow1 = shipinglc.getBILLNUM();
                                                            String formNo1 = shipinglc.getFORMNUM();
                                                            if (formtyp.trim().equalsIgnoreCase("EDF") && (step_Input.equalsIgnoreCase("i"))
                                                                        && (step_csm.equalsIgnoreCase("CBS Maker 1")
                                                                                    || step_csm.equalsIgnoreCase("CBS Maker")
                                                                                    || step_csm.equalsIgnoreCase("CBS Reject"))
                                                                        && getMinorCode().equalsIgnoreCase("DOP")) {
                                                                  if ((billrow1.equalsIgnoreCase("") || billrow1 == null)
                                                                              && (formNo1.equalsIgnoreCase("") || formNo1 == null)
                                                                              && getMinorCode().equalsIgnoreCase("DOP")) {
                                                                        validationDetails.addError(ErrorType.Other,
                                                                                    "Form Type:EDF,Form No or Shipping Bill No is Mandatory in shipping details pane :[CM]");
                                                                  } else {
                                                                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                                              // Loggers.general().info(LOG,"Shiping bill number--->" + billrow1
                                                                              // + "Form number" + formNo1);
                                                                        }
                                                                  }
                                                            }
                                                      } catch (Exception e) {
                                                            if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                                  Loggers.general().info(LOG,
                                                                              "Exception Shiping bill number and form number--->"
                                                                                          + e.getMessage());
                                                            }
                                                      }

                                                      ship_Str = shipinglc.getSHPAMT().toString();
                                                      if (ship_Str == null || ship_Str.equalsIgnoreCase("")) {

                                                            ship_Str = "0";
                                                            // //Loggers.general().info(LOG,"tendays in if
                                                            // --->" +
                                                            // tendays);
                                                      }
                                                      // //Loggers.general().info(LOG,"Shiping amount--->"
                                                      // +
                                                      // ship_Str);
                                                      double ship_dob = 0.0;
                                                      try {

                                                            ship_dob = Double.valueOf(ship_Str);
                                                      } catch (Exception e) {
                                                            // Loggers.general().info(LOG,"tendays in catch"
                                                            // + e.getMessage());
                                                      }

                                                      ship_total = shipinglc.getSHPAMT().doubleValue();

                                                      if (getMinorCode().equalsIgnoreCase("POD") && getMajorCode().equalsIgnoreCase("ELC")
                                                                  && (step_Input.equalsIgnoreCase("i"))
                                                                  && (step_Id.equalsIgnoreCase("CBS Maker")
                                                                              || step_Id.equalsIgnoreCase("CBS Maker 1"))) {

                                                            acm = getDriverWrapper().getEventFieldAsText("ACM", "l", "");
                                                            acp = getDriverWrapper().getEventFieldAsText("ACP", "l", "");
                                                            // //Loggers.general().info(LOG,"Payment action
                                                            // ---->"
                                                            // + acm + "<<<>>>" + acp);
                                                            // repayAmount =
                                                            // getDriverWrapper().getEventFieldAsText("AAF",
                                                            // "*", "");
                                                            // repayAmount =
                                                            // getDriverWrapper().getEventFieldAsText("AAF",
                                                            // "*", "");
                                                            // // //Loggers.general().info(LOG,"repayment in
                                                            // // initialy---->" + repayAmount);
                                                            // BigDecimal rAmt =
                                                            // shipinglc.getREPAYAM();
                                                            // if (rAmt != null) {
                                                            // repay_Amt = rAmt.doubleValue();
                                                            // // repayAmount =
                                                            // // String.valueOf(repay_Amt);
                                                            // //// Loggers.general().info(LOG,"repayment
                                                            // // in bigdeciaml---->" + repay_Amt);
                                                            // } else {
                                                            //
                                                            // repayAmount = "";
                                                            // }

                                                            rep_total = shipinglc.getREPAYAM().doubleValue();
                                                            // Loggers.general().info(LOG,"shipmentrep_total---->" + rep_total);
                                                            String curr = shipinglc.getREPAYAMCurrency().trim();
                                                            // Loggers.general().info(LOG,"Repaycurrency" + curr);
                                                            ConnectionMaster connectionMaster = new ConnectionMaster();
                                                            double divideBy = connectionMaster.getDecimalforCurrency(curr);
                                                            repay_Amt = rep_total / divideBy;
                                                            // Loggers.general().info(LOG,"Repayamount" + repay_Amt);
                                                            am_rey = am_rey + repay_Amt;
                                                            // Loggers.general().info(LOG,"Repayamount_am_rey" + am_rey);

                                                            repval = shipinglc.getREPTYPE();
                                                            // //Loggers.general().info(LOG,"Repayment type
                                                            // if---->>>" + repval);
                                                            if (repval.equalsIgnoreCase("")
                                                                        && ((!repval.equalsIgnoreCase("1")) || !repval.equalsIgnoreCase("2"))
                                                                        && getMinorCode().equalsIgnoreCase("POD")
                                                                        && (step_Input.equalsIgnoreCase("i"))
                                                                        && (step_Id.equalsIgnoreCase("CBS Maker")
                                                                                    || step_Id.equalsIgnoreCase("CBS Maker 1"))) {
                                                                  validationDetails.addWarning(WarningType.Other,
                                                                              "Repayment type is mandatory in shipping grid [CM]");
                                                            } else {
                                                                  //// Loggers.general().info(LOG,"Repayment
                                                                  //// type in else---->>>" + repval);
                                                            }

                                                            if ((rep_total == 0.0 || rep_total < 1)
                                                                        && ((repval.equalsIgnoreCase("1")) || repval.equalsIgnoreCase("2"))
                                                                        && (step_Input.equalsIgnoreCase("i"))
                                                                        && (step_Id.equalsIgnoreCase("CBS Maker")
                                                                                    || step_Id.equalsIgnoreCase("CBS Maker 1"))
                                                                        && payaction.equalsIgnoreCase("Pay")) {
                                                                  validationDetails.addWarning(WarningType.Other,
                                                                              "Please enter the Repayment amount in shipping Bill grid [CM]");
                                                            } else {
                                                                  // Loggers.general().info(LOG,"Repayment
                                                                  // amount--->>>" + rep_total +
                                                                  // "Repayment type" + repval);
                                                            }

                                                      }
                                                      String shipamtcurr = shipinglc.getSHPAMTCurrency().trim();
                                                      /*
                                                       * String advancerec = getWrapper().getADVREC(); if(advancerec)
                                                       */
                                                      ConnectionMaster connectionMaster = new ConnectionMaster();
                                                      // Loggers.general().info(LOG,"shipamtcurr"+shipamtcurr);
                                                      double divideByDecimal = connectionMaster.getDecimalforCurrency(shipamtcurr);
                                                      // Loggers.general().info(LOG,"ship_total"+ship_total);
                                                      invadob_total = ship_total / divideByDecimal;
                                                      // Loggers.general().info(LOG,"invadob_total"+invadob_total);
                                                      am = am + invadob_total;
                                                      // Loggers.general().info(LOG,"am"+am);

                                                      equal_amt = shipinglc.getEQUBILL().doubleValue();
                                                      String equal_cur = shipinglc.getEQUBILLCurrency();
                                                      // //Loggers.general().info(LOG,"Shipping
                                                      // equaivalent
                                                      // amount " + equal_amt);
                                                      double deci = connectionMaster.getDecimalforCurrency(equal_cur);
                                                      equal_tot = equal_amt / deci;
                                                      equal_total = equal_total + equal_tot;
                                                      // //Loggers.general().info(LOG,"Shipping
                                                      // equaivalent
                                                      // amount after convert" + equal_total);
                                                      billno = shipinglc.getBILLNUM();

                                                }
                                                double claimAmountDub = 0;
                                                double addAmountDub = 0;
                                                try {
                                                      String addAmount = getDriverWrapper().getEventFieldAsText("AAC", "v", "m");
                                                      try {
                                                            addAmountDub = Double.valueOf(addAmount);

                                                      } catch (Exception e) {
                                                            addAmountDub = 0;
                                                            Loggers.general().info(LOG, "1st Exception in addAmount" + e.getMessage());
                                                      }
                                                      String claimAmount = getDriverWrapper().getEventFieldAsText("AMC", "v", "m");
                                                      //// Loggers.general().info(LOG,"Shipping
                                                      //// equaivalent
                                                      //// amount for compare" + equal_total +
                                                      //// "claimAmountDub" + claimAmountDub);
                                                      if (claimAmount.length() > 0) {
                                                            claimAmountDub = Double.parseDouble(claimAmount);
                                                      }
                                                      //// Loggers.general().info(LOG,"Shipping
                                                      //// equaivalent
                                                      //// amount for compare" + equal_total +
                                                      //// "claimAmountDub" + claimAmountDub);

                                                      if (equal_total != claimAmountDub && addAmountDub > 0
                                                                  && getMinorCode().equalsIgnoreCase("DOP")
                                                                  && (step_Input.equalsIgnoreCase("i"))
                                                                  && (step_Id.equalsIgnoreCase("CBS Maker")
                                                                              || step_Id.equalsIgnoreCase("CBS Maker 1"))) {
                                                            validationDetails.addWarning(WarningType.Other,
                                                                        "Sum of shipping bill amount should be equal to Bill lodgement amount (Presentation amt+ additional amt) [CM]");
                                                      } else if (equal_total != amountint && (step_Input.equalsIgnoreCase("i"))
                                                                  && (step_Id.equalsIgnoreCase("CBS Maker")
                                                                              || step_Id.equalsIgnoreCase("CBS Maker 1"))
                                                                  && getMinorCode().equalsIgnoreCase("DOP")) {

                                                            validationDetails.addWarning(WarningType.Other,
                                                                        "Sum of shipping bill amount should be equal to Bill lodgement amount [CM]");

                                                      } else {
                                                            //// Loggers.general().info(LOG,"Sum of
                                                            //// equivalent
                                                            //// bill amounts is equal to Bill
                                                            //// amount");
                                                      }
                                                } catch (Exception e) {
                                                      Loggers.general().info(LOG, "Exception in addAmount" + e.getMessage());
                                                }

                                                if (counter > 0 && (step_Input.equalsIgnoreCase("i"))
                                                            && (step_Id.equalsIgnoreCase("CBS Maker"))) {

                                                      notioanl = shipinglc.getNOTIONAL().doubleValue();
                                                      // //Loggers.general().info(LOG,"Notional rate is
                                                      // ----> " + notioanl);
                                                      if (notioanl < 2 && (step_Input.equalsIgnoreCase("i"))
                                                                  && (step_Id.equalsIgnoreCase("CBS Maker")
                                                                              || step_Id.equalsIgnoreCase("CBS Maker 1"))
                                                                  && getMinorCode().equalsIgnoreCase("DOP")) {
                                                            // //Loggers.general().info(LOG,"Notional rate
                                                            // is
                                                            // empty " + notioanl);
                                                            validationDetails.addWarning(WarningType.Other,
                                                                        "shipping bill currency is different from collection currency, Notional rate is mandatory [CM]");
                                                      } else {

                                                            //// Loggers.general().info(LOG,"Notional rate
                                                            //// is greater than 0 values----> ");
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
                                                            double rep_totalss = type.getREPAYAM().doubleValue();
                                                            double short_tot = type.getSHCOLAM().doubleValue();
                                                            double equl_tot = type.getEQUBILL().doubleValue();
                                                            String equl_ccy = type.getEQUBILLCurrency();
                                                            short_totals = short_tot + equl_tot;
                                                            ConnectionMaster connectionMaster = new ConnectionMaster();
                                                            double divideByDecimal = connectionMaster.getDecimalforCurrency(equl_ccy);
                                                            short_convert = short_totals / divideByDecimal;
                                                            short_totvalue = short_totvalue + short_convert;

                                                            double covertShot = short_tot / divideByDecimal;
                                                            short_total = short_total + covertShot;

                                                            repval_new = type.getREPTYPE();
                                                            // //Loggers.general().info(LOG,"shipping
                                                            // repayment
                                                            // amount" + repval);
                                                            String sbil = type.getBILLNUM();
                                                            double ship_total_ss = type.getLOUTSAMT().doubleValue();
                                                            // //Loggers.general().info(LOG,"Shipping bill
                                                            // amount_____" + rep_totalss);
                                                            double shpamt = type.getSHPAMT().doubleValue();

                                                            BigDecimal shotAmt = type.getSHCOLAM();
                                                            // Loggers.general().info(LOG,"Short collection
                                                            // amount bigdecimal---->>>" + shotAmt);
                                                            BigDecimal outAmt = type.getLOUTSAMT();
                                                            // Loggers.general().info(LOG,"Short collection
                                                            // amount bigdecimal---->>>" + outAmt);

                                                            if (shotAmt.compareTo(outAmt) > 0 && (step_Input.equalsIgnoreCase("i"))
                                                                        && (step_Id.equalsIgnoreCase("CBS Maker"))
                                                                        && getMinorCode().equalsIgnoreCase("POD")) {
                                                                  validationDetails.addError(ErrorType.Other,
                                                                              "Short collection amount should not be greater than SB outstanding amount in shipping grid [CM]");

                                                            } else {
                                                                  // Loggers.general().info(LOG,"Short
                                                                  // collection
                                                                  // amount bigdecimal else---->>>" +
                                                                  // shotAmt);
                                                            }

                                                            if ((repval_new.equalsIgnoreCase("2")) && (step_Input.equalsIgnoreCase("i"))
                                                                        && (step_Id.equalsIgnoreCase("CBS Maker"))
                                                                        && getMinorCode().equalsIgnoreCase("POD")) {

                                                                  if ((ship_total_ss == 0.0 && rep_totalss != 0.0)
                                                                              && (step_Input.equalsIgnoreCase("i"))
                                                                              && (step_Id.equalsIgnoreCase("CBS Maker")
                                                                                          || step_Id.equalsIgnoreCase("CBS Maker 1"))
                                                                              && getMinorCode().equalsIgnoreCase("POD")) {
                                                                        validationDetails.addWarning(WarningType.Other,
                                                                                    "Repayment Type is Full payment, please do not modify already paid shipping bill ("
                                                                                                + sbil + ") [CM]");
                                                                  } else {
                                                                        //// Loggers.general().info(LOG,"Repayment
                                                                        //// amount is part
                                                                        //// payment---->>>"
                                                                        //// + repval);
                                                                  }

                                                            }

                                                            else {
                                                                  //// Loggers.general().info(LOG,"rep
                                                                  //// amount--->>>" + rep_total);
                                                            }
                                                            //// Loggers.general().info(LOG,"Outstanding and
                                                            //// short+equvalent amount---->>>" +
                                                            //// ship_total_ss+ "" + short_totals);
                                                            if ((ship_total_ss == short_totals)
                                                                        && (repval_new.equalsIgnoreCase("1") || repval_new.equalsIgnoreCase(""))
                                                                        && (step_Input.equalsIgnoreCase("i"))
                                                                        && (step_Id.equalsIgnoreCase("CBS Maker")
                                                                                    || step_Id.equalsIgnoreCase("CBS Maker 1"))
                                                                        && getMinorCode().equalsIgnoreCase("POD")) {
                                                                  if (payaction.equalsIgnoreCase("Pay")) {
                                                                        validationDetails.addWarning(WarningType.Other,
                                                                                    "Sum of SB Equivalent Bill Amount and short collection amount is equal to SB outstanding amt, then select repayment type as 'Full'  ("
                                                                                                + sbil + ") [CM]");
                                                                  }

                                                            } else {
                                                                  //// Loggers.general().info(LOG,"Repay type
                                                                  //// is
                                                                  //// full" + repval_new);
                                                            }

                                                            if ((ship_total_ss > short_totals) && repval_new.equalsIgnoreCase("2")
                                                                        && (step_Input.equalsIgnoreCase("i"))
                                                                        && (step_Id.equalsIgnoreCase("CBS Maker")
                                                                                    || step_Id.equalsIgnoreCase("CBS Maker 1"))
                                                                        && getMinorCode().equalsIgnoreCase("POD")) {

                                                                  if (payaction.equalsIgnoreCase("Pay")) {
                                                                        validationDetails.addWarning(WarningType.Other,
                                                                                    "Sum of SB Equivalent Bill Amount and short collection amount is less than SB outstanding amount, then select repayment type as 'Part' ("
                                                                                                + sbil + ") [CM]");
                                                                  }

                                                            } else {
                                                                  //// Loggers.general().info(LOG,"Repay type
                                                                  //// is
                                                                  //// part" + repval_new);
                                                            }

                                                            if (((ship_total_ss == 0.0) && repval.equalsIgnoreCase("1"))
                                                                        && (step_Input.equalsIgnoreCase("i"))
                                                                        && (step_Id.equalsIgnoreCase("CBS Maker")
                                                                                    || step_Id.equalsIgnoreCase("CBS Maker 1"))
                                                                        && getMinorCode().equalsIgnoreCase("DOP")) {

                                                                  // //Loggers.general().info(LOG,"string
                                                                  // repayment
                                                                  // type_____" + repval);
                                                                  validationDetails.addWarning(WarningType.Other, "Shipping bill (" + sbil
                                                                              + ") is already paid, Please change repayment type as Full [CM]");
                                                                  //

                                                            }

                                                            else {
                                                                  //// Loggers.general().info(LOG,"ship_total_ss
                                                                  //// amount----->>>" +
                                                                  //// ship_total_ss);
                                                            }

                                                            if ((ship_total_ss < short_totals) && (step_Input.equalsIgnoreCase("i"))
                                                                        && (step_Id.equalsIgnoreCase("CBS Maker")
                                                                                    || step_Id.equalsIgnoreCase("CBS Maker 1"))
                                                                        && getMinorCode().equalsIgnoreCase("POD")) {

                                                                  // //Loggers.general().info(LOG,"string
                                                                  // repayment
                                                                  // amount ---->" + rep_totalss);
                                                                  if (payaction.equalsIgnoreCase("Pay")) {
                                                                        validationDetails.addWarning(WarningType.Other,
                                                                                    "Sum of SB Equivalent Bill Amount and short collection amount should not greater than SB Outstanding amount ("
                                                                                                + sbil + ") [CM]");
                                                                  }

                                                            }

                                                      }

                                                }

                                                catch (Exception e) {
                                                      Loggers.general().info(LOG, "Repayment modify exception" + e.getMessage());
                                                }
                                                String amountcol = getDriverWrapper().getEventFieldAsText("AMP", "v", "m").trim();

                                                if (amountcol.length() > 0) {
                                                      amountdob = Double.parseDouble(amountcol);
                                                }

                                                double addAmountdoub = 0;
                                                double addtionalAmt = 0;
                                                try {
                                                      String addtionalAmount = getDriverWrapper().getEventFieldAsText("CLAD", "v", "m")
                                                                  .trim();

                                                      try {
                                                            addtionalAmt = Double.parseDouble(addtionalAmount);

                                                      } catch (Exception e) {
                                                            addtionalAmt = 0;
                                                            Loggers.general().info(LOG, "1st Exception in addAmount" + e.getMessage());
                                                      }

                                                      String addAmountPay = getDriverWrapper().getEventFieldAsText("AMC", "v", "m")
                                                                  .trim();
                                                      if (addAmountPay.length() > 0) {
                                                            addAmountdoub = Double.parseDouble(addAmountPay);
                                                      }
                                                } catch (Exception e) {
                                                      Loggers.general().info(LOG, "Exception in addtional amount" + e.getMessage());
                                                }

                                                // Loggers.general().info(LOG,"Mixed payment
                                                // checkbox--->>>" + mixedpay + "" + amountcol);
                                                // Loggers.general().info(LOG,"Amount to be collect compare--->>>" + amountdob
                                                // + "short_totvalueequl bill amount" + short_totvalue);
                                                if (amountcol.length() > 0 && mixedpay.equalsIgnoreCase("N")
                                                            && (payaction.equalsIgnoreCase("Pay"))) {

                                                      if ((short_totvalue != addAmountdoub) && addtionalAmt > 0
                                                                  && (step_Input.equalsIgnoreCase("i"))
                                                                  && (step_Id.equalsIgnoreCase("CBS Maker")
                                                                              || step_Id.equalsIgnoreCase("CBS Maker 1"))
                                                                  && getMinorCode().equalsIgnoreCase("POD")) {
                                                            validationDetails.addWarning(WarningType.Other,
                                                                        "Sum of SB Equivalent Bill Amount and short collection amount should be equal to payment amount (Payment amt+ additional amt) [CM]");
                                                      } else if ((short_totvalue != amountdob) && (step_Input.equalsIgnoreCase("i"))
                                                                  && (step_Id.equalsIgnoreCase("CBS Maker")
                                                                              || step_Id.equalsIgnoreCase("CBS Maker 1"))
                                                                  && getMinorCode().equalsIgnoreCase("POD")) {
                                                            validationDetails.addWarning(WarningType.Other,
                                                                        "Sum of SB Equivalent Bill Amount and short collection amount should be equal to payment amount (Payment details)  [CM]");
                                                      }

                                                      else {
                                                            /*
                                                             * Loggers.general().info(LOG,"Sum of Equivalent Bill Amount else loop---->>>" +
                                                             * short_totvalue + " Amount in elc" + amountdob + "addAmountdoub" +
                                                             * addAmountdoub);
                                                             */
                                                      }
                                                }
                                                // Mixed payment validation
                                                else if (mixedpay.equalsIgnoreCase("Y") && (payaction.equalsIgnoreCase("Pay"))) {
                                                      String mixpay = "";
                                                      String mixcur = "";

                                                      con = getConnection();
                                                      String dms = "SELECT mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL, sum(pap.PAY_AMT), pap.PAY_AMTCCY FROM master mas, BASEEVENT bev, LCPAYMENT lcp, PARTPAYMNT pap WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = lcp.KEY97 AND lcp.KEY97 = pap.PAYEV_KEY AND pap.PP_STAT='P' AND pap.PAID_REJ   <>'P' AND mas.MASTER_REF = '"
                                                                  + MasterReference + "' AND bev.REFNO_PFIX = '" + evnt
                                                                  + "' AND bev.REFNO_SERL =" + evvcount
                                                                  + " group by mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL, pap.PAY_AMTCCY"; //
                                                      dmsp = con.prepareStatement(dms);
                                                      // Loggers.general().info(LOG,"Mixed payment validation query--->" + dms);
                                                      dmsr = dmsp.executeQuery();
                                                      while (dmsr.next()) {
                                                            mixpay = dmsr.getString(4);
                                                            mixcur = dmsr.getString(5).trim();

                                                      }
                                                      if (mixpay.length() > 0) {
                                                            double mixpay_dob = Double.valueOf(mixpay);
                                                            ConnectionMaster connectionMaster = new ConnectionMaster();
                                                            double dividemixcur = connectionMaster.getDecimalforCurrency(mixcur);
                                                            mixpay_tot = mixpay_dob / dividemixcur;
                                                            // Loggers.general().info(LOG,"Mixed payment value--->" + mixpay_tot);
                                                            // Loggers.general().info(LOG,"Final short_totvalue--->" + short_totvalue);
                                                            if ((short_totvalue != mixpay_tot) && (step_Input.equalsIgnoreCase("i"))
                                                                        && (step_Id.equalsIgnoreCase("CBS Maker 1"))
                                                                        && getMinorCode().equalsIgnoreCase("POD")) {
                                                                  validationDetails.addWarning(WarningType.Other,
                                                                              "Sum of SB Equivalent Bill Amount and short collection amount should be equal to payment amount (Payment details) for Mixed payment [CM]");
                                                            } else {
                                                                  // //Loggers.general().info(LOG,
                                                                  // "Sum of Equivalent Bill Amount
                                                                  // should be equal to Amount in elc
                                                                  // else loop for Mixed
                                                                  // payment---->>>"
                                                                  // + short_totvalue + " " +
                                                                  // mixpay_tot);
                                                            }

                                                      }

                                                } else {
                                                      //// Loggers.general().info(LOG,"Sum of Equivalent
                                                      //// Bill Amount and short collection amount
                                                      //// should be equal====>>>");
                                                }

                                                // equal_total

                                                // end to edit for repay
                                          }

                                    }

                              } catch (Exception e) {
                                    Loggers.general().info(LOG, "Shipment validation Error " + e.getMessage());
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
                                          //// Loggers.general().info(LOG,"Shipping bill date for
                                          //// checking valid" + billdate);

                                          SimpleDateFormat parseFormat1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                                          SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");
                                          SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yy");

                                          if (billdate.length() > 0 && billdate != null || !billdate.equalsIgnoreCase("")) {
                                                if (billdate.length() > 10) {
                                                      dateresult = formatter1.format(parseFormat1.parse(billdate));
                                                      // //Loggers.general().info(LOG,"Shipping Bill date
                                                      // after result1 if----->" + dateresult);

                                                } else {
                                                      dateresult = formatter1.format(parseFormat.parse(billdate));
                                                      // //Loggers.general().info(LOG,"Shipping Bill date
                                                      // after result1 else----->" + dateresult);

                                                }
                                          }

                                          int count2 = 0;
                                          String query2 = "select count(*) from ett_edpms_shp where SHIPBILLNO ='" + billnoo + "'";
                                          // //Loggers.general().info(LOG,"Query2---------->" +
                                          // query2);
                                          con = ConnectionMaster.getConnection();
                                          ps = con.prepareStatement(query2);
                                          rs = ps.executeQuery();
                                          while (rs.next()) {
                                                count2 = rs.getInt(1);

                                          }
                                          // Port,bill date,bill no combination validation
                                          int count4 = 0;
                                          String query4 = "SELECT count(*) FROM ett_edpms_shp WHERE shipbillno ='" + billnoo
                                                      + "' AND SHIPBILLDATE = TO_CHAR(to_date('" + dateresult
                                                      + "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE ='" + port + "'";
                                          // //Loggers.general().info(LOG,"Port,bill date,bill no
                                          // combination validation====>" + query4);
                                          // con = ConnectionMaster.getConnection();
                                          ps1 = con.prepareStatement(query4);
                                          rs1 = ps1.executeQuery();
                                          while (rs1.next()) {
                                                count4 = rs1.getInt(1);

                                          }

                                          int count3 = 0;
                                          String query3 = "select count(*) from ett_edpms_shp_softex where FORMNO ='" + formNO + "'";
                                          // con = ConnectionMaster.getConnection();
                                          ps1 = con.prepareStatement(query3);
                                          rs1 = ps1.executeQuery();
                                          while (rs1.next()) {
                                                count3 = rs1.getInt(1);

                                          }

                                          // Port,bill date,form no combination validation
                                          int count5 = 0;
                                          String query5 = "SELECT count(*) FROM ett_edpms_shp_inv_softex A WHERE FORMNO ='" + formNO
                                                      + "' AND SHIPBILLDATE = TO_CHAR(to_date('" + dateresult
                                                      + "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE ='" + port + "'";
                                          // //Loggers.general().info(LOG,"Port,bill date,form no
                                          // combination validation====>" + count5);
                                          // con = ConnectionMaster.getConnection();
                                          ps1 = con.prepareStatement(query5);
                                          rs1 = ps1.executeQuery();
                                          while (rs1.next()) {
                                                count5 = rs1.getInt(1);

                                          }
                                          String formType = getPane().getFORTYP().trim();
                                          // Loggers.general().info(LOG,"Form type===>"+formType);

                                          if ((count2 == 0 || count2 < 1) && (step_Input.equalsIgnoreCase("i"))
                                                      && (!billnoo.equalsIgnoreCase(""))
                                                      && (step_Id.equalsIgnoreCase("CBS Maker")
                                                                  || step_Id.equalsIgnoreCase("CBS Maker 1"))
                                                      && getMinorCode().equalsIgnoreCase("DOP") && !formType.equalsIgnoreCase("EDF")) {

                                                validationDetails.addWarning(WarningType.Other,
                                                            "Shipping bill no (" + billnoo + ") not found in MDF file  [CM]");

                                          } else {
                                                //// Loggers.general().info(LOG,"Count value in else
                                                //// dup---------->" + count2);
                                          }

                                          if ((count3 < 1 || count3 == 0)
                                                      && (!formNO.equalsIgnoreCase("") && billnoo.equalsIgnoreCase(""))
                                                      && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_Id.equalsIgnoreCase("CBS Maker")
                                                                  || step_Id.equalsIgnoreCase("CBS Maker 1"))
                                                      && getMinorCode().equalsIgnoreCase("DOP")) {

                                                validationDetails.addWarning(WarningType.Other,
                                                            "Shipping Form no (" + formNO + ") not found in MDF file  [CM]");

                                          } else {
                                                //// Loggers.general().info(LOG,"Count value in else
                                                //// dup---------->" + count3);
                                          }

                                          // Port,bill date,bill no combination validation

                                          if ((count4 < 1 || count4 == 0) && (!billnoo.equalsIgnoreCase(""))
                                                      && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_Id.equalsIgnoreCase("CBS Maker")
                                                                  || step_Id.equalsIgnoreCase("CBS Maker 1"))
                                                      && getMinorCode().equalsIgnoreCase("DOP") && !formType.equalsIgnoreCase("EDF")) {

                                                validationDetails.addWarning(WarningType.Other,
                                                            "Kindly enter valid Shipping bill no,bill date,port code  [CM]");

                                          } else {
                                                //// Loggers.general().info(LOG,"valid Bill
                                                //// no---------->" + count4);
                                          }

                                          // Port,bill date,form no combination validation

                                          if ((count5 < 1 || count5 == 0)
                                                      && (!formNO.equalsIgnoreCase("") && billnoo.equalsIgnoreCase(""))
                                                      && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_Id.equalsIgnoreCase("CBS Maker")
                                                                  || step_Id.equalsIgnoreCase("CBS Maker 1"))
                                                      && getMinorCode().equalsIgnoreCase("DOP")) {
                                                validationDetails.addWarning(WarningType.Other,
                                                            "Kindly enter valid Form no,bill date,port code  [CM]");

                                          } else {
                                                //// Loggers.general().info(LOG,"valid Form
                                                //// no---------->" + count5);
                                          }

                                    }
                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"shipping bill dup " +
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
                                          //// Loggers.general().info(LOG,"Connection Failed! Check
                                          //// output console");
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

                                          SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");

                                          shipBilldate = format.format(shipinglc.getBILLDAT());
                                          portCode = shipinglc.getPORTCODDD();
                                          // Loggers.general().info(LOG,"Shpping Bill date " +
                                          // shipBilldate + "and " + portCode);

                                          con = ConnectionMaster.getConnection();
                                          String query = "select count(*) from  exteventsht shc, baseevent bev, master mas where mas.KEY97 = bev.MASTER_KEY and shc.fk_event = bev.extfield AND (bev.STATUS = 'c' OR (mas.STATUS = 'LIV' AND bev.STATUS = case when bev.REFNO_PFIX in ('DPR') then 'c' else 'i' end )) and shc.BILLNUM='"
                                                      + shipBilNo + "' AND  TO_CHAR(shc.BILLDAT,'DD/MM/YY')='" + shipBilldate
                                                      + "' and shc.PORTCODD='" + portCode + "' ";

                                          ps1 = con.prepareStatement(query);
                                          rs1 = ps1.executeQuery();
                                          while (rs1.next()) {
                                                // //Loggers.general().info(LOG,"Entered while");
                                                countv = rs1.getInt(1);
                                                // //Loggers.general().info(LOG,"value of count in while
                                                // "
                                                // + countv);

                                          }
                                          // Loggers.general().info(LOG,"Shipping bill Query
                                          // authorised" + query + "countv===" + countv);

                                          String query1 = "select count(*) from  exteventsht shc, baseevent bev, master mas where mas.KEY97 = bev.MASTER_KEY and shc.fk_event = bev.extfield AND (bev.STATUS = 'c' OR (mas.STATUS = 'LIV' AND bev.STATUS = case when bev.REFNO_PFIX in ('DPR') then 'c' else 'i' end )) and shc.FORMNUM='"
                                                      + formNo + "' AND  TO_CHAR(shc.BILLDAT,'DD/MM/YY')='" + shipBilldate
                                                      + "' and shc.PORTCODD='" + portCode + "' ";

                                          // //Loggers.general().info(LOG,"Shipping bill formNo
                                          // authorised" + query1);
                                          // con = ConnectionMaster.getConnection();
                                          ps1 = con.prepareStatement(query1);
                                          rs1 = ps1.executeQuery();
                                          while (rs1.next()) {
                                                // //Loggers.general().info(LOG,"Entered while");
                                                countv1 = rs1.getInt(1);
                                                // //Loggers.general().info(LOG,"value of count in while
                                                // "
                                                // + countv);

                                          }
                                          // Loggers.general().info(LOG,"Shipping form Query
                                          // authorised" + query1 + "countv1===" + countv1);

                                          String query2 = "select count(*) from  exteventshc shc, baseevent bev, master mas where mas.KEY97 = bev.MASTER_KEY and shc.fk_event = bev.extfield AND (bev.STATUS = 'c' OR (mas.STATUS = 'LIV' AND bev.STATUS = case when bev.REFNO_PFIX in ('DPR') then 'c' else 'i' end )) and shc.CBILLNUM='"
                                                      + shipBilNo + "' AND  TO_CHAR(shc.CBILLDA,'DD/MM/YY')='" + shipBilldate
                                                      + "' and shc.CPORTCO='" + portCode + "' ";
                                          // //Loggers.general().info(LOG,"Collection bill no
                                          // used---->
                                          // " + query);
                                          // con = ConnectionMaster.getConnection();
                                          ps1 = con.prepareStatement(query2);
                                          rs1 = ps1.executeQuery();
                                          while (rs1.next()) {
                                                // //Loggers.general().info(LOG,"Entered while");
                                                countvodc = rs1.getInt(1);
                                                // //Loggers.general().info(LOG,"value of count in while
                                                // "
                                                // +
                                                // countv);
                                          }

                                          String query3 = "select count(*) from  exteventshc shc, baseevent bev, master mas where mas.KEY97 = bev.MASTER_KEY and shc.fk_event = bev.extfield AND (bev.STATUS = 'c' OR (mas.STATUS = 'LIV' AND bev.STATUS = case when bev.REFNO_PFIX in ('DPR') then 'c' else 'i' end )) and shc.CFORMN='"
                                                      + formNo + "' AND  TO_CHAR(shc.CBILLDA,'DD/MM/YY')='" + shipBilldate
                                                      + "' and shc.CPORTCO='" + portCode + "' ";
                                          // //Loggers.general().info(LOG,"Collection bill no
                                          // used---->
                                          // " +
                                          // query);
                                          // con = ConnectionMaster.getConnection();
                                          ps1 = con.prepareStatement(query3);
                                          rs1 = ps1.executeQuery();
                                          while (rs1.next()) {
                                                // //Loggers.general().info(LOG,"Entered while");
                                                countv1odc = rs1.getInt(1);
                                                // //Loggers.general().info(LOG,"value of count in while
                                                // "
                                                // +
                                                // countv);
                                          }

                                          // elc validation

                                          if (countv > 0 && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_Id.equalsIgnoreCase("CBS Maker")
                                                                  || step_Id.equalsIgnoreCase("CBS Maker 1"))
                                                      && getMinorCode().equalsIgnoreCase("DOP")) {
                                                validationDetails.addWarning(WarningType.Other,
                                                            "Entered shipping bill number already used (" + shipBilNo + ") [CM]");
                                          }

                                          if (countv1 > 0 && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_Id.equalsIgnoreCase("CBS Maker")
                                                                  || step_Id.equalsIgnoreCase("CBS Maker 1"))
                                                      && getMinorCode().equalsIgnoreCase("DOP")) {
                                                validationDetails.addWarning(WarningType.Other,
                                                            "Entered shipping form number already used (" + formNo + ") [CM]");
                                          }

                                          if (countvodc > 0 && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_Id.equalsIgnoreCase("CBS Maker")
                                                                  || step_Id.equalsIgnoreCase("CBS Maker 1"))
                                                      && getMinorCode().equalsIgnoreCase("DOP")) {
                                                validationDetails.addWarning(WarningType.Other,
                                                            "Entered shipping bill number already used (" + shipBilNo + ") [CM]");
                                          }

                                          if (countv1odc > 0 && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_Id.equalsIgnoreCase("CBS Maker")
                                                                  || step_Id.equalsIgnoreCase("CBS Maker 1"))
                                                      && getMinorCode().equalsIgnoreCase("DOP")) {
                                                validationDetails.addWarning(WarningType.Other,
                                                            "Entered shipping Form number already used (" + formNo + ") [CM]");
                                          }

                                    }

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"shipping bill exception already
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
                                          //// Loggers.general().info(LOG,"Connection Failed! Check
                                          //// output console");
                                          e.printStackTrace();
                                    }
                              }

                              // //Loggers.general().info(LOG,"Form type for shipping--->" +
                              // formtyp);
                              if (formtyp.equalsIgnoreCase("") && (step_Input.equalsIgnoreCase("i"))
                                          && (step_Id.equalsIgnoreCase("CBS Maker")) && getMinorCode().equalsIgnoreCase("DOP")
                                          && getMajorCode().equalsIgnoreCase("ELC")) {
                                    /*
                                     * if (dailyval_Log.equalsIgnoreCase("YES")) {
                                     * Loggers.general().info(LOG,"Form type if loop --->" + formtyp); }
                                     */

                                    validationDetails.addError(ErrorType.Other, "Form type is mandatory [CM]");

                              } else {
                                    /*
                                     * if (dailyval_Log.equalsIgnoreCase("YES")) {
                                     * Loggers.general().info(LOG,"Form type for shipping is blank--->" + formtyp);
                                     * }
                                     */
                              }

                        } else {

                              pane.getBtnFetchShipdetELCDPclay().setEnabled(false);
                              pane.getBtnFetchInvdetELCDPclay().setEnabled(false);
                              pane.getBtnFetchshipdetELCSETTclay().setEnabled(false);
                              pane.getBtnFetchinvdetELCSETTclay().setEnabled(false);
                              // //Loggers.general().info(LOG,"product type is not a local cur");
                        }

                        // Notional function
                        if (step_Input.equalsIgnoreCase("i")) {
                              try {
                                    List<ExtEventShippingTable> shipTable = (List<ExtEventShippingTable>) getWrapper()
                                                .getExtEventShippingTable();
                                    for (int i = 0; i < shipTable.size(); i++) {

                                          ExtEventShippingTable ship = shipTable.get(i);

                                          String not_str = ship.getNOTIONAL().toString();
                                          //// Loggers.general().info(LOG, "shipping table notional
                                          //// length
                                          //// start====>" + not_str + "==>" +
                                          //// not_str.length());
                                          String shiamtcuy = ship.getSHPAMTCurrency();
                                          String reyamtcuy = ship.getREPAYAMCurrency();

                                          BigDecimal rAmt = ship.getREPAYAM();

                                          if (getMinorCode().equalsIgnoreCase("POD")) {
                                                double notional = 1;
                                                try {
                                                      con = getConnection();
                                                      String query = "SELECT ETT_SPOTRATE_CAL('" + shiamtcuy + "','" + reyamtcuy
                                                                  + "') FROM DUAL";
                                                      //// Loggers.general().info(LOG,"Notional rate
                                                      //// function
                                                      //// " + query);

                                                      ps1 = con.prepareStatement(query);
                                                      rs1 = ps1.executeQuery();
                                                      if (rs1.next()) {
                                                            notional = rs1.getDouble(1);
                                                            //// Loggers.general().info(LOG,"shipping table
                                                            //// notional length start===>" +
                                                            //// notional);
                                                            // ship.setNOTIONAL(new
                                                            // BigDecimal(notional));
                                                      }
                                                } catch (Exception e1) {
                                                      // Loggers.general().info(LOG,"Exception Notional
                                                      // rate
                                                      // function" + e1.getMessage());
                                                }

                                                String temp_notRate = String.valueOf(notional);

                                                if (null != not_str && not_str.equalsIgnoreCase("1")) {
                                                      ship.setNOTIONAL(new BigDecimal(temp_notRate));
                                                      //// Loggers.general().info(LOG,"shipping table
                                                      //// notional
                                                      //// length if loop====>" +
                                                      //// ship.getNOTIONAL());

                                                      BigDecimal notional_big = new BigDecimal(notional);
                                                      BigDecimal equi_bill = notional_big.multiply(rAmt);
                                                      //// Loggers.general().info(LOG,"Notional rate +
                                                      //// Repament amount in big decimal
                                                      //// POD---->" +
                                                      //// equi_bill);
                                                      ship.setEQUBILL(equi_bill);
                                                      ship.setEQUBILLCurrency(ship.getSHPAMTCurrency());
                                                } else if (null != not_str && !temp_notRate.equalsIgnoreCase(not_str)) {
                                                      ship.setNOTIONAL(new BigDecimal(not_str));
                                                      //// Loggers.general().info(LOG,"shipping table
                                                      //// notional
                                                      //// length else if loop====>" +
                                                      //// ship.getNOTIONAL());
                                                      BigDecimal notional_big = new BigDecimal(not_str);
                                                      BigDecimal equi_bill = notional_big.multiply(rAmt);
                                                      //// Loggers.general().info(LOG,"Notional rate +
                                                      //// Repament amount in big decimal
                                                      //// POD---->" +
                                                      //// equi_bill);
                                                      ship.setEQUBILL(equi_bill);
                                                      ship.setEQUBILLCurrency(ship.getSHPAMTCurrency());

                                                } else {
                                                      //// Loggers.general().info(LOG,"Notional rate is
                                                      //// blank---->");
                                                }

                                          } else if (getMinorCode().equalsIgnoreCase("DOP")) {
                                                double notion = 1;
                                                notion = ship.getNOTIONAL().doubleValue();
                                                BigDecimal notion_big = new BigDecimal(notion);
                                                // double shipamt = (notion * ship_Amount);
                                                // //Loggers.general().info(LOG,"Notional rate in big
                                                // decimal DOP---->" + notion_big);
                                                String ship_str = ship.getSHPAMT().toString();
                                                BigDecimal shipamt_str = new BigDecimal(ship_str);
                                                BigDecimal shipamt_big = notion_big.multiply(shipamt_str);
                                                // //Loggers.general().info(LOG,"Notional rate +
                                                // Shipment
                                                // amount in big decimal DOP---->" +
                                                // shipamt_big);
                                                ship.setEQUBILL(shipamt_big);
                                                ship.setEQUBILLCurrency(ship.getSHPAMTCurrency());
                                                ship.setLOUTSAMT(shipamt_str);
                                                ship.setLOUTSAMTCurrency(ship.getSHPAMTCurrency());
                                          }
                                    }

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"Exception in validating for
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
                                          //// Loggers.general().info(LOG,"Connection Failed! Check
                                          //// output
                                          //// console");
                                          e.printStackTrace();
                                    }
                              }
                        }
                        try {

                              if ((!grwai.equalsIgnoreCase("Y")) && grwai.equalsIgnoreCase("N")
                                          && prd_typ.equalsIgnoreCase("ELF")) {
                                    List<ExtEventShippingTable> rep = (List<ExtEventShippingTable>) getWrapper()
                                                .getExtEventShippingTable();

                                    for (int j = 0; j < rep.size(); j++) {
                                          ExtEventShippingTable type = rep.get(j);

                                          double notionalDouble = type.getNOTIONAL().doubleValue();
                                          /*
                                           * if (dailyval_Log.equalsIgnoreCase("YES")) {
                                           *
                                           * Loggers.general().info(LOG,"Notional rate value---->" + notionalDouble); }
                                           */
                                          if ((notionalDouble == 0.0 || notionalDouble == 0 || notionalDouble < 1) && rep.size() > 0
                                                      && (step_Input.equalsIgnoreCase("i")) && (step_Id.equalsIgnoreCase("CBS Maker")
                                                                  || step_Id.equalsIgnoreCase("CBS Maker 1"))) {
                                                validationDetails.addWarning(WarningType.Other,
                                                            "Notional rate should not be '0' in shipping details grid [CM]");
                                          } else {
                                                /*
                                                 * if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                 *
                                                 * // Loggers.general().info(LOG,"Notional rate value in else---->" +
                                                 * notionalDouble); }
                                                 */
                                          }

                                          double shpamt = type.getSHPAMT().doubleValue();

                                          // //Loggers.general().info(LOG,"shipment amount table for
                                          // negative amount===>" + shpamt);
                                          if (shpamt < 0 && getMinorCode().equalsIgnoreCase("DOP")
                                                      && getMajorCode().equalsIgnoreCase("ELC") && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_Id.equalsIgnoreCase("CBS Maker"))) {
                                                validationDetails.addError(ErrorType.Other,
                                                            "Shipping bill amount should not be negative in shipping details grid [CM]");
                                          } else {
                                                //// Loggers.general().info(LOG,"New shipment for not
                                                //// negative---->" + shpamt);
                                          }
                                          double reyamt = type.getREPAYAM().doubleValue();
                                          if (reyamt < 0 && getMinorCode().equalsIgnoreCase("POD")
                                                      && getMajorCode().equalsIgnoreCase("ELC") && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_Id.equalsIgnoreCase("CBS Maker"))) {
                                                validationDetails.addError(ErrorType.Other,
                                                            "Repayment amount should not be negative in shipping details grid [CM]");
                                          } else {
                                                //// Loggers.general().info(LOG,"New shipment for not
                                                //// negative---->" + shpamt);
                                          }
                                    }
                              }
                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Exception in validating for
                              // notional
                              // rate--->" + e.getMessage());
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

                                    ExtEventInvoicedetailsLC invoice_ship = invoice_shiplc.get(l);

                                    fobAmt = fobAmt + invoice_ship.getIFOBAMT().doubleValue();
                                    // Loggers.general().info(LOG,"FOBAmount ---> " + fobAmt);
                                    String fobcur = invoice_ship.getIFOBAMTCurrency();
insAmt = insAmt + invoice_ship.getINSUAMT().doubleValue();
// Loggers.general().info(LOG,"INSAmount ----> " + insAmt);
String insccy = invoice_ship.getINSUAMTCurrency();

friAmt = friAmt + invoice_ship.getINVFRAMT().doubleValue();
// Loggers.general().info(LOG,"friAmtAmount ----> " + friAmt);
String friccy = invoice_ship.getINVFRAMTCurrency();

comm = comm + invoice_ship.getICOMMAMT().doubleValue();
// Loggers.general().info(LOG,"commision amount ----> " + comm);
String commccy = invoice_ship.getICOMMAMTCurrency();

pack = pack + invoice_ship.getIPKGAMT().doubleValue();
// Loggers.general().info(LOG,"Paking credit ----> " + pack);
String packccy = invoice_ship.getIPKGAMTCurrency();

discount = discount + invoice_ship.getIDISCAMT().doubleValue();
// Loggers.general().info(LOG,"Discount amount ----> " + discount);
dedut = dedut + invoice_ship.getIDEDUAMT().doubleValue();
// Loggers.general().info(LOG,"Deduction amount ----> " + dedut);

discal = discount + dedut;
// Loggers.general().info(LOG,"deduction and discount discal----> " + discal);

try {
      con = getConnection();
      String query = "SELECT ETT_SPOTRATE_CAL('" + fobcur + "','" + insccy + "') FROM DUAL";
      // Loggers.general().info(LOG,"insurance amount " + query);

      ps1 = con.prepareStatement(query);
      rs1 = ps1.executeQuery();
      if (rs1.next()) {
            inscuramt = rs1.getDouble(1);
            inscuramtBig = new BigDecimal(inscuramt);
            insconamt = new BigDecimal(insAmt);
            insconamt = inscuramtBig.multiply(insconamt);

      }

      String query1 = "SELECT ETT_SPOTRATE_CAL('" + fobcur + "','" + friccy + "') FROM DUAL";
      // Loggers.general().info(LOG,"freight amount " + query1);

      ps1 = con.prepareStatement(query1);
      rs1 = ps1.executeQuery();
      if (rs1.next()) {
            fricuramt = rs1.getDouble(1);
            fricuramtBig = new BigDecimal(fricuramt);
            friconamt = new BigDecimal(friAmt);
            friconamt = fricuramtBig.multiply(friconamt);

      }

      String query2 = "SELECT ETT_SPOTRATE_CAL('" + fobcur + "','" + commccy + "') FROM DUAL";
      // Loggers.general().info(LOG,"commission amount " + query2);

      ps1 = con.prepareStatement(query2);
      rs1 = ps1.executeQuery();
      if (rs1.next()) {
            commcuramt = rs1.getDouble(1);
            commcuramtBig = new BigDecimal(commcuramt);
            commconamt = new BigDecimal(comm);
            commconamt = commcuramtBig.multiply(commconamt);

      }

      String query3 = "SELECT ETT_SPOTRATE_CAL('" + fobcur + "','" + packccy + "') FROM DUAL";
      // Loggers.general().info(LOG,"package amount " + query3);

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
      // Loggers.general().info(LOG,"inscuramt"+inscuramt);
      // Loggers.general().info(LOG,"fricuramt"+fricuramt);
      // Loggers.general().info(LOG,"commcuramt"+commcuramt);
      // Loggers.general().info(LOG,"packcuramt"+packcuramt);
} catch (Exception e) {
      Loggers.general().info(LOG, "Exception in sum of fob" + e.getMessage());
} finally {
      surrenderDB(con, ps1, rs1);
}

Double totalInvoiceAmt = fobAmt + inscuramt + fricuramt + commcuramt + packcuramt;
// Loggers.general().info(LOG,"totalInvoiceAmt"+totalInvoiceAmt);
ConnectionMaster connectionMaster = new ConnectionMaster();
double divideByDecimal = connectionMaster.getDecimalforCurrency(fobcur);
// Loggers.general().info(LOG,"divideByDecimal"+divideByDecimal);
toFobamt_tot = totalInvoiceAmt / divideByDecimal;

// Loggers.general().info(LOG,"toFobamt_tot"+toFobamt_tot);

toFobamt = (toFobamt_tot - (discal / divideByDecimal));
// Loggers.general().info(LOG,"toFobamt"+toFobamt);

// Loggers.general().info(LOG,"am==="+am);
String finval = String.format("%.2f", am);
double finvalue = Double.valueOf(finval);
// Loggers.general().info(LOG,"finvalue"+finvalue);

String totval = String.format("%.2f", toFobamt);
double totvalue = Double.valueOf(totval);

// Loggers.general().info(LOG,"toFobamt"+toFobamt);
// Loggers.general().info(LOG,"totvalue"+totvalue);

if (totvalue > 0 && totvalue != finvalue && (step_Input.equalsIgnoreCase("i"))
            && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id.equalsIgnoreCase("CBS Maker 1"))
            && getMinorCode().equalsIgnoreCase("DOP")) {
      validationDetails.addWarning(WarningType.Other,
                  "Sum of FOB + freight+ insurance+commission+package amount minus (Discount + deduction amt)("
                              + totvalue + ") should be equal to shipping bill amount (" + finvalue
                              + ") [CM]");

}
}
} catch (Exception e) {
// Loggers.general().info(LOG,"Exception is Advance Table Validation
// in customer" + e.getMessage());
}
} else {
//// Loggers.general().info(LOG,"GR waivwr is ticked");
}

// Shipping bill need to delete
if ((step_Id.equalsIgnoreCase("CBS Maker 1")) && getMajorCode().equalsIgnoreCase("ELC")
&& getMinorCode().equalsIgnoreCase("DOP")) {
try {

List<ExtEventInvoicedetailsLC> invoiceDet = (List<ExtEventInvoicedetailsLC>) getWrapper()

      .getExtEventInvoicedetailsLC();
for (int l = 0; l < invoiceDet.size(); l++) {

ExtEventInvoicedetailsLC invoice_ship = invoiceDet.get(l);

try {
      String invShipBill = invoice_ship.getIShippingBillNo().trim();

      if (dailyval_Log.equalsIgnoreCase("YES")) {
            Loggers.general().info(LOG, "Invoice no shipping bill" + invShipBill);
      }

      if ((invShipBill != null && !invShipBill.equalsIgnoreCase(""))
                  && invShipBill.length() > 0) {

            /*
             * if (dailyval_Log.equalsIgnoreCase("YES")) {
             * //Loggers.general().info(LOG,"Invoice shipping bill length greater than zero"
             * ); }
             */
            int count = 0;
            String query = "SELECT COUNT(*), mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL, shc.BILLNUM FROM exteventsht shc, baseevent bev, master mas WHERE mas.KEY97 = bev.MASTER_KEY AND shc.fk_event = bev.extfield AND shc.BILLNUM ='"
                        + invShipBill
                        + "' GROUP BY mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL, shc.BILLNUM";

            /*
             * if (dailyval_Log.equalsIgnoreCase("YES")) {
             * //Loggers.general().info(LOG,"Invoice shipping bill query" + query); }
             */

            con = ConnectionMaster.getConnection();
            ps1 = con.prepareStatement(query);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                  // //Loggers.general().info(LOG,"Entered while");
                  count = rs1.getInt(1);

            }

            /*
             * if (dailyval_Log.equalsIgnoreCase("YES")) {
             * Loggers.general().info(LOG,"Invoice bill count if loop" + count); }
             */
            if ((count == 0 || count < 1) && (step_Input.equalsIgnoreCase("i"))
                        && !prd_typ.equalsIgnoreCase("ELD")
                        && (step_Id.equalsIgnoreCase("CBS Maker 1"))) {
                  validationDetails.addError(ErrorType.Other, "Please delete the Invoice number row ("
                              + invShipBill + ") which is not available in 1st Shipping bill grid [CM]");
            } else {

                  /*
                   * if (dailyval_Log.equalsIgnoreCase("YES")) { //Loggers.general().info(
                   * LOG,"Shipping bill for comparation invShipBill else===>" // + invShipBill +
                   * "And count" + count); }
                   */

            }
      } else {

      }
} catch (Exception e) {

      if (dailyval_Log.equalsIgnoreCase("YES")) {
            Loggers.general().info(LOG, "Exception Invoice number" + e.getMessage());
      }
}
try {
      String invFormBill = invoice_ship.getIFORNO();

      if (dailyval_Log.equalsIgnoreCase("YES")) {
            // Loggers.general().info(LOG,"Invoice no shipping bill" + invFormBill);
      }
      if ((invFormBill != null && !invFormBill.equalsIgnoreCase(""))
                  && invFormBill.length() > 0) {

            if (dailyval_Log.equalsIgnoreCase("YES")) {
                  // Loggers.general().info(LOG,"Invoice form no length greater than zero");
            }

            int count = 0;
            String query = "SELECT COUNT(*), mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL, shc.FORMNUM FROM exteventsht shc, baseevent bev, master mas WHERE mas.KEY97 = bev.MASTER_KEY AND shc.fk_event = bev.extfield AND shc.FORMNUM ='"
                        + invFormBill
                        + "' GROUP BY mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL, shc.FORMNUM";
            if (dailyval_Log.equalsIgnoreCase("YES")) {
                  // Loggers.general().info(LOG,"Invoice form no query" + query);
            }

            con = ConnectionMaster.getConnection();
            ps1 = con.prepareStatement(query);
            rs1 = ps1.executeQuery();
            if (rs1.next()) {
                  // //Loggers.general().info(LOG,"Entered while");
                  count = rs1.getInt(1);

                  /*
                   * if (dailyval_Log.equalsIgnoreCase("YES")) { //
                   * Loggers.general().info(LOG,"Invoice form number count if loop" + count); }
                   */
            }

            else if ((step_Input.equalsIgnoreCase("i")) && !prd_typ.equalsIgnoreCase("ELD")
                        && (step_Id.equalsIgnoreCase("CBS Maker 1"))) {
                  validationDetails.addError(ErrorType.Other,
                              "Please delete the Invoice form number row (" + invFormBill
                                          + ") which is not available in 1st Shipping bill grid [CM]");
            } else {

                  /*
                   * if (dailyval_Log.equalsIgnoreCase("YES")) { //Loggers.general().info(
                   * LOG,"Shipping bill for comparation invFormBill else===>" // + invFormBill +
                   * "And count" + count); }
                   */

            }

      } else {

      }
} catch (Exception e) {

      if (dailyval_Log.equalsIgnoreCase("YES")) {
            Loggers.general().info(LOG, "Exception Form number" + e.getMessage());
      }
}
}

} catch (Exception e) {

if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Exception in Invoice details" + e.getMessage());
}
}

// Shipping bill need to delete

try {
List<ExtEventShippingdetailslc> shipDet = (List<ExtEventShippingdetailslc>) getWrapper()
      .getExtEventShippingdetailslc();
for (int l = 0; l < shipDet.size(); l++) {

ExtEventShippingdetailslc invoice_ship = shipDet.get(l);

try {
      String invShipBill = invoice_ship.getLCBILLNO().trim();
      /*
       * if (dailyval_Log.equalsIgnoreCase("YES")) {
       * //Loggers.general().info(LOG,"2nd shipping bill" + invShipBill); }
       */
      if ((invShipBill != null && !invShipBill.equalsIgnoreCase(""))
                  && invShipBill.length() > 0) {
            int count = 0;
            String query = "SELECT COUNT(*), mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL, shc.BILLNUM FROM exteventsht shc, baseevent bev, master mas WHERE mas.KEY97 = bev.MASTER_KEY AND shc.fk_event = bev.extfield AND shc.BILLNUM ='"
                        + invShipBill
                        + "' GROUP BY mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL, shc.BILLNUM";
            if (dailyval_Log.equalsIgnoreCase("YES")) {
                  // Loggers.general().info(LOG,"2nd shipping bill query" + query);
            }

            con = ConnectionMaster.getConnection();
            ps1 = con.prepareStatement(query);
            rs1 = ps1.executeQuery();
            if (rs1.next()) {
                  // //Loggers.general().info(LOG,"Entered while");
                  count = rs1.getInt(1);

                  /*
                   * if (dailyval_Log.equalsIgnoreCase("YES")) {
                   * Loggers.general().info(LOG,"2nd shipping bill count if loop" + count); }
                   */
            }

            else if ((step_Input.equalsIgnoreCase("i")) && !prd_typ.equalsIgnoreCase("ELD")
                        && (step_Id.equalsIgnoreCase("CBS Maker 1"))) {
                  validationDetails.addError(ErrorType.Other,
                              "Please delete the Shipping bill number row (" + invShipBill
                                          + ") which is not available in 1st Shipping bill grid [CM]");
            } else {

                  /*
                   * if (dailyval_Log.equalsIgnoreCase("YES")) { // Loggers.general().info(
                   * LOG,"Shipping bill for comparation invShipBill else===>" // + invShipBill +
                   * "And count" + count); }
                   */

            }
      } else {

      }

} catch (Exception e) {

      /*
       * if (dailyval_Log.equalsIgnoreCase("YES")) {
       * //Loggers.general().info(LOG,"Exception 2nd shipping bill" + e.getMessage());
       *
       * }
       */

}
try {
      String invForm = invoice_ship.getLCFORMNO().trim();

      if (dailyval_Log.equalsIgnoreCase("YES")) {
            // Loggers.general().info(LOG,"2nd Shipping Form number" + invForm);

      }

      if ((invForm != null && !invForm.equalsIgnoreCase("")) && invForm.length() > 0) {

            int count = 0;
            String query = "SELECT COUNT(*), mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL, shc.FORMNUM FROM exteventsht shc, baseevent bev, master mas WHERE mas.KEY97 = bev.MASTER_KEY AND shc.fk_event = bev.extfield AND shc.FORMNUM ='"
                        + invForm
                        + "' GROUP BY mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL, shc.FORMNUM";

            if (dailyval_Log.equalsIgnoreCase("YES")) {
                  // Loggers.general().info(LOG,"2nd Shipping Form number query" + query);

            }

            con = ConnectionMaster.getConnection();
            ps1 = con.prepareStatement(query);
            rs1 = ps1.executeQuery();
            if (rs1.next()) {
                  // //Loggers.general().info(LOG,"Entered while");
                  count = rs1.getInt(1);
            }

            else if ((step_Input.equalsIgnoreCase("i")) && !prd_typ.equalsIgnoreCase("ELD")
                        && (step_Id.equalsIgnoreCase("CBS Maker 1"))) {
                  validationDetails.addError(ErrorType.Other,
                              "Please delete the 2nd Shipping form number row (" + invForm
                                          + ") which is not available in 1st Shipping bill grid [CM]");
            } else {

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        // Loggers.general().info(LOG,"Shipping form for comparation 2nd Shipping from
                        // else===>"
                        // + invForm + "And count" + count);

                  }
            }

      } else {

      }
} catch (Exception e) {

      if (dailyval_Log.equalsIgnoreCase("YES")) {
            Loggers.general().info(LOG, "Exception 2nd shipping form" + e.getMessage());

      }

}

}

} catch (Exception e) {

if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Exception in Shipping details" + e.getMessage());

}
}

}
// -------------****************-------------------

// Advance Table Validations

List<ExtEventAdvanceTable> liste = (List<ExtEventAdvanceTable>) getWrapper().getExtEventAdvanceTable();
// if (getMajorCode().equalsIgnoreCase("ELC") &&
// getMinorCode().equalsIgnoreCase("DOP")) {
// // ////Loggers.general().info(LOG,"Advance Table Validation for
// // DPR--->");
// try {
// String cusNo = "";
//
// for (int a = 0; a < liste.size(); a++) {
// ExtEventAdvanceTable adve = liste.get(a);
// cusNo = adve.getCUSCIFNO().trim();
//
// if ((!cusNo.equalsIgnoreCase(customer)) &&
// (step_Input.equalsIgnoreCase("i"))
// && (step_Id.equalsIgnoreCase("CBS Maker") ||
// step_Id.equalsIgnoreCase("CBS Maker 1"))) {
// validationDetails.addWarning(WarningType.Other,
// "Remittance Customer is not same as the beneficiary [CM]");
//
// }
// }
// } catch (Exception e) {
// // Loggers.general().info(LOG,"Exception is Advance Table Validation
// // in customer" + e.getMessage());
// }
// } else if (getMajorCode().equalsIgnoreCase("ELC") &&
// getMinorCode().equalsIgnoreCase("POD")) {
// try {
// //// Loggers.general().info(LOG,"Advance Table Validation for
// //// POD--->");
// String cusNo = "";
// double utlamt = 0;
// double utlamt_long = 0;
// double utlamt_total = 0;
// for (int a = 0; a < liste.size(); a++) {
// ExtEventAdvanceTable adve = liste.get(a);
// cusNo = adve.getCUSCIFNO().trim();
//
// utlamt = adve.getAMTUTIL().doubleValue();
// String advcurr = adve.getAMTUTILCurrency().trim();
// // //Loggers.general().info(LOG,"Advance Table utilization
// // amount
// // start--->" + utlamt);
// ConnectionMaster connectionMaster = new ConnectionMaster();
// double divideByDecimal =
// connectionMaster.getDecimalforCurrency(advcurr);
// utlamt_long = utlamt / divideByDecimal;
// // //Loggers.general().info(LOG,"Advance Table utilization
// // amount
// // after divided--->" + utlamt_long);
// utlamt_total = utlamt_total + utlamt_long;
// //// Loggers.general().info(LOG,"Advance Table utilization
// //// amount total--->" + utlamt_total);
//
// if ((!cusNo.equalsIgnoreCase(customer)) &&
// (step_Input.equalsIgnoreCase("i"))
// && (step_Id.equalsIgnoreCase("CBS Maker") ||
// step_Id.equalsIgnoreCase("CBS Maker 1"))) {
// validationDetails.addWarning(WarningType.Other,
// "Remittance Customer is not same as the beneficiary [CM]");
//
// }
// }
//
// if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Advance Table utilization amount &repayment
// compare--->" + utlamt_total
// + "<===>" + repay_total);
//
// }
// if (liste.size() > 0 && (utlamt_total != repay_Amt) &&
// (step_Input.equalsIgnoreCase("i"))
// && (step_Id.equalsIgnoreCase("CBS Maker") ||
// step_Id.equalsIgnoreCase("CBS Maker 1"))) {
// validationDetails.addWarning(WarningType.Other,
// "Sum of Advance payment Utillization amount and Repayment amount
// should be same [CM]");
//
// } else {
// //// Loggers.general().info(LOG,"Utilization amount & repayment
// //// compare else--->" + utlamt_total + "<===>"+
// //// repay_total);
// if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Advance Table utilization amount &repayment
// else--->" + utlamt_total
// + "<===>" + repay_total);
//
// }
// }
//
// } catch (Exception e) {
// // Loggers.general().info(LOG,"Exception is Advance Table Validation
// // in customer" + e.getMessage());
// }
//
// } else {
// //// Loggers.general().info(LOG,"Advance Table Validation is not DOP and
// //// POD--->");
// }

// Advance Table Validations
if (getMajorCode().equalsIgnoreCase("ELC") && getMinorCode().equalsIgnoreCase("POD")) {
// //Loggers.general().info(LOG,"Advance Table Validation for
// collection--->");
try {
String customer = getDriverWrapper().getEventFieldAsText("BEN", "p", "no").trim();
String advrec = getWrapper().getPERADV();

if (dailyval_Log.equalsIgnoreCase("YES")) {

// Loggers.general().info(LOG,"Advance received" + advrec);
}
if ((!advrec.equalsIgnoreCase(""))
      && (advrec.equalsIgnoreCase("Full") || advrec.equalsIgnoreCase("Part"))) {

if ((liste.size() == 0 || liste.size() < 1) && (step_Input.equalsIgnoreCase("i"))
            && (step_Id.equalsIgnoreCase("CBS Maker"))) {
      validationDetails.addError(ErrorType.Other,
                  "Advance Received selection is selected,please input Advance Remittance Grid [CM]");
}

}

if ((liste.size() > 0) && (step_Input.equalsIgnoreCase("i"))
      && step_Id.equalsIgnoreCase("CBS Maker")) {
if (advrec.equalsIgnoreCase("") || advrec.length() < 1) {
      validationDetails.addError(ErrorType.Other,
                  "Advance Remittance Grid entered,Please select Advance Received selection [CM]");
}
}

for (int a = 0; a < liste.size(); a++) {
ExtEventAdvanceTable advance = liste.get(a);

// Loggers.general().info(LOG,"RemCunntry no---------->" +
// RemCunntry);
// String RemDate = advance.getDATREM().toString();
// Loggers.general().info(LOG,"RemDate---------->" + RemDate);
String inwRem = advance.getINWARD().trim();
// Loggers.general().info(LOG,"Inward remittance no---------->"
// +
// inwRem);
String firc = advance.getFINUMB();
// Loggers.general().info(LOG,"firc no---------->" + firc);
String cusNum = advance.getCUSCIFNO();
// Loggers.general().info(LOG,"cusNum no---------->" + cusNum);
String RemName = advance.getNAMREM();
// Loggers.general().info(LOG,"RemName no---------->" +
// RemName);
String advrem = advance.getADVRECB();
// Loggers.general().info(LOG,"advrem no---------->" + advrem);
String RemCunntry = advance.getCOUNREM();

if (dailyval_Log.equalsIgnoreCase("YES")) {

      Loggers.general().info(LOG, "Inward remittance no---------->" + inwRem);
      Loggers.general().info(LOG, "firc no---------->" + firc);
}

try {

      if (!inwRem.equalsIgnoreCase("") || inwRem.length() > 0) {
            if (cusNum.equalsIgnoreCase("") && RemName.equalsIgnoreCase("")
                        && (RemCunntry.equalsIgnoreCase("")) && (step_Input.equalsIgnoreCase("i"))
                        && (step_Id.equalsIgnoreCase("CBS Maker")
                                    || step_Id.equalsIgnoreCase("CBS Maker 1"))) {
                  validationDetails.addWarning(WarningType.Other,
                              "Please fetch the advance details table [CM]");

            } else {

                  if (dailyval_Log.equalsIgnoreCase("YES")) {

                        // Loggers.general().info(LOG,
                        // "Fiels value cusNumber--------->" + cusNum + "RemName" + RemName);
                  }
            }
      } else {

            if (dailyval_Log.equalsIgnoreCase("YES")) {

                  // Loggers.general().info(LOG,"Inward remittance no---------->" + inwRem);
            }
      }

      if (!firc.equalsIgnoreCase("") || firc.length() > 0) {
            if (cusNum.equalsIgnoreCase("") && RemName.equalsIgnoreCase("")
                        && advrem.equalsIgnoreCase("") && RemCunntry.equalsIgnoreCase("")
                        && (step_Input.equalsIgnoreCase("i"))
                        && (step_Id.equalsIgnoreCase("CBS Maker"))) {
                  validationDetails.addError(ErrorType.Other,
                              "Please Input Remittance AdCode,Remitter Name,Remittance Date,Remittance country,Customer CIF No,Available amount [CM]");

            } else {

                  if (dailyval_Log.equalsIgnoreCase("YES")) {

                        // Loggers.general().info(LOG,"Fiels value cusNu--------->" + cusNum + "RemName"
                        // + RemName
                        // + "advrem" + advrem + "RemCunntry" + RemCunntry);
                  }
            }
      } else {
            // Loggers.general().info(LOG,"firc number---------->" +
            // firc);
      }
      if (!firc.equalsIgnoreCase("") || firc.length() > 0) {
            if ((!inwRem.equalsIgnoreCase("") || inwRem.length() > 0)
                        && (step_Input.equalsIgnoreCase("i"))
                        && (step_Id.equalsIgnoreCase("CBS Maker"))) {

                  validationDetails.addError(ErrorType.Other,
                              "FIRC No is inputted, Please remove the Inward remittance no [CM]");
            }
      }

      if (!inwRem.equalsIgnoreCase("") || inwRem.length() > 0) {
            if ((!firc.equalsIgnoreCase("") || firc.length() > 0)
                        && (step_Input.equalsIgnoreCase("i"))
                        && (step_Id.equalsIgnoreCase("CBS Maker"))) {

                  validationDetails.addError(ErrorType.Other,
                              "Inward remittance no is inputted, Please remove the FIRC No [CM]");
            }
      }

      String query2 = "select count(*) FROM ETTV_INWARD_REMITTANCE_AMOUNT WHERE MAS_REF='"
                  + inwRem + "'";
      con = ConnectionMaster.getConnection();
      ps = con.prepareStatement(query2);
      if (dailyval_Log.equalsIgnoreCase("YES")) {

            // Loggers.general().info(LOG,"Inward remittance number query" + query2);
      }
      rs = ps.executeQuery();
      int count1 = 0;
      // //Loggers.general().info(LOG,"result of query "+rs2);
      while (rs.next()) {
            count1 = rs.getInt(1);
            if ((liste.size() > 0) && (count1 == 0 || count1 < 1)
                        && (step_Input.equalsIgnoreCase("i")) && (step_Id.equalsIgnoreCase("CBS Maker")
                                    || step_Id.equalsIgnoreCase("CBS Maker 1"))) {
                  validationDetails.addWarning(WarningType.Other,
                              "Please enter Valid Inward remittance number (" + inwRem + ") [CM]");
            }

      }

} catch (Exception e) {

      if (dailyval_Log.equalsIgnoreCase("YES")) {

            Loggers.general().info(LOG, "Exception Inward remittance number" + e.getMessage());
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
      double divideByDecimal = connectionMaster.getDecimalforCurrency(amtutlcur);
      utlamt_long = amtutl / divideByDecimal;
      // //Loggers.general().info(LOG,"Advance Table utilization
      // amount
      // after divided--->" + utlamt_long);
      utlamt_total = utlamt_total + utlamt_long;
      // //Loggers.general().info(LOG,"Advance Table utilization
      // amount
      // total--->" + utlamt_total);

      // todo sep 5 2018
      double rep_total = 0.0;
      List<ExtEventShippingTable> shiplcd = (List<ExtEventShippingTable>) getWrapper()
                  .getExtEventShippingTable();
      ExtEventShippingTable shipinglc = null;
      shipinglc = shiplcd.get(a);
      rep_total = shipinglc.getREPAYAM().doubleValue();
      // Loggers.general().info(LOG,"shipmentrep_total!!!!!!---->" + rep_total);
      String curr = shipinglc.getREPAYAMCurrency().trim();
      // Loggers.general().info(LOG,"Repaycurrency!!!!!!!!" + curr);
      // ConnectionMaster connectionMaster = new
      // ConnectionMaster();
      double divideBy = connectionMaster.getDecimalforCurrency(curr);
      repay_Amt = rep_total / divideBy;
      // Loggers.general().info(LOG,"Repayamount!!!!!!!" + repay_Amt);
      total_rep = total_rep + repay_Amt;
      // Loggers.general().info(LOG,"Repayamount__am_rey!!!!!!!" + total_rep);

      if (dailyval_Log.equalsIgnoreCase("YES")) {

            // Loggers.general().info(LOG,"Advance Table utilization amount total--->" +
            // utlamt_total);
            // Loggers.general().info(LOG,"Repayment amount total--->" + total_rep);

            /*
             * rep_total = shipinglc.getREPAYAM().doubleValue();
             * Loggers.general().info(LOG,"shipmentrep_total---->" + rep_total);
             *
             * Loggers.general().info(LOG,"Repaycurrency"+curr);
             */

            // Loggers.general().info(LOG,"Repayamount" + repay_Amt);

            /*
             * Loggers.general().info(LOG,"Repayamount_am_rey"+ am_rey);
             */
      }

      if ((!cusNo.equalsIgnoreCase(customer)) && (step_Input.equalsIgnoreCase("i"))
                  && (step_Id.equalsIgnoreCase("CBS Maker")
                              || step_Id.equalsIgnoreCase("CBS Maker 1"))) {
            validationDetails.addWarning(WarningType.Other,
                        "Remittance Customer is not same as the Beneficiary in advance table [CM]");

      }

      balan = adve.getBALANCE().doubleValue();
      balancur = adve.getBALANCECurrency();
      if (amtutl > balan && (step_Input.equalsIgnoreCase("i"))
                  && (step_Id.equalsIgnoreCase("CBS Maker"))) {
            validationDetails.addError(ErrorType.Other,
                        "Advance payment utillization  amount should not greater than available amount in advance table [CM]");
      }
      if (!balancur.equalsIgnoreCase(amtutlcur) && (step_Input.equalsIgnoreCase("i"))
                  && (step_Id.equalsIgnoreCase("CBS Maker")
                              || step_Id.equalsIgnoreCase("CBS Maker 1"))) {
            validationDetails.addWarning(WarningType.Other,
                        "Utillization of amount currency should equal to available amount currency in advance table [CM]");
      }

}

if (liste.size() > 0 && (utlamt_total != total_rep) && (step_Input.equalsIgnoreCase("i"))
            && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id.equalsIgnoreCase("CBS Maker 1"))) {
      //// Loggers.general().info(LOG,"Advance Table
      //// utilization
      //// amount & repayment compare if loop--->"+
      //// utlamt_total + "<===>" + repay_total);
      // Loggers.general().info(LOG,"Inside SUM of" + total_rep + "Util" +
      //// utlamt_total);
      validationDetails.addWarning(WarningType.Other,
                  "Sum of Advance payment Utillization amount and Repayment amount should be same [CM]");

} else {
      // Loggers.general().info(LOG,"Utilization amount &
      // repayment
      // compare else--->" + utlamt_total + "<===>"+
      // repay_total);
}

} catch (Exception e) {

if (dailyval_Log.equalsIgnoreCase("YES")) {

      Loggers.general().info(LOG,
                  "Exception is Advance Table Validation in customer" + e.getMessage());
}
}

} catch (Exception e) {
if (dailyval_Log.equalsIgnoreCase("YES")) {

Loggers.general().info(LOG, "Exception is Advance Table Final" + e.getMessage());
}
}

} else {

if (dailyval_Log.equalsIgnoreCase("YES")) {

Loggers.general().info(LOG, "Advance Table Validation other product--->");
}
}

if (mixedpay.equalsIgnoreCase("Y") && (payaction.equalsIgnoreCase("Pay"))) {
double addAmountDub = 0;
String amountcol = "";
try {
String addAmount = getDriverWrapper().getEventFieldAsText("AAC", "v", "m").trim();

addAmountDub = Double.valueOf(addAmount);

} catch (Exception e) {
addAmountDub = 0;
Loggers.general().info(LOG, "Exception in additional Amount mixed payment" + e.getMessage());
}
if (addAmountDub > 0) {
amountcol = getDriverWrapper().getEventFieldAsText("AMC", "v", "m");
} else {
amountcol = getDriverWrapper().getEventFieldAsText("EVAM", "v", "m");
}
BigDecimal amountBig = new BigDecimal(amountcol);
// if (amountcol.length() > 0) {
// amountdob = Double.parseDouble(amountcol);
// }

if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Mixed payment Presentation amount--->" +
// amountBig);
}
String mixpay = "";
String mixcur = "";
try {

con = getConnection();
String dms = "SELECT mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL, SUM(pap.PAY_AMT), pap.PAY_AMTCCY FROM master mas, BASEEVENT bev, LCPAYMENT lcp, PARTPAYMNT pap WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = lcp.KEY97 AND lcp.KEY97 = pap.PAYEV_KEY AND mas.MASTER_REF = '"
      + MasterReference + "' AND bev.REFNO_PFIX = '" + evnt + "' AND bev.REFNO_SERL =" + evvcount
      + " GROUP BY mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL, pap.PAY_AMTCCY";

dmsp = con.prepareStatement(dms);
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Mixed payment validation for Comparation--->" + dms);
}
dmsr = dmsp.executeQuery();
while (dmsr.next()) {
mixpay = dmsr.getString(4);
mixcur = dmsr.getString(5).trim();

}
} catch (SQLException e) {

if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Mixed payment value Exception--->" + e.getMessage());
}
}
if (mixpay.length() > 0) {
// double mixpay_dob = Double.valueOf(mixpay);

BigDecimal mixpayAmt = new BigDecimal(mixpay);

if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Mixed payment Value in BigDecimal--->" + mixpayAmt);
}
ConnectionMaster connectionMaster = new ConnectionMaster();
double dividemixcur = connectionMaster.getDecimalforCurrency(mixcur);
BigDecimal divideMix = new BigDecimal(dividemixcur);

if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Mixed payment Value decimal--->" + divideMix);
}
BigDecimal mixpayBig = mixpayAmt.divide(divideMix);
// mixpay_tot = mixpay_dob / dividemixcur;

if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Mixed payment value--->" + mixpayBig +
// "Outstanding amount" + amountBig);
}
if ((amountBig.compareTo(mixpayBig) != 0) && (step_Input.equalsIgnoreCase("i"))
      && (step_Id.equalsIgnoreCase("CBS Maker 1")) && getMinorCode().equalsIgnoreCase("POD")) {
validationDetails.addError(ErrorType.Other,
            "Mixed payment is checked, Payment amount should equal to Presentation amount [CM]");
} else {

if (dailyval_Log.equalsIgnoreCase("YES")) {
      // Loggers.general().info(LOG,
      // "Mixed payment value else loop--->" + mixpayBig + "Outstanding amount" +
      // amountBig);
}
}

}

} else {

if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,
// "Sum of Equivalent Bill Amount and short collection amount should be
// equal====>>>");
}
}

// Shipping bill date checking

String paymentExt = "PAYEXT";
String paymentVal = "";
double paymentValue = 0;
try {
AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryValue = getDriverWrapper()
.createQuery("EXTGENCUSTPROP", "PROPNAME='" + paymentExt + "'");
// //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
EXTGENCUSTPROP val = queryValue.getUnique();
if (val != null) {

paymentVal = val.getPropval();

paymentValue = Double.valueOf(paymentVal);
// Loggers.general().info(LOG,"payment Value date ===>" +
// paymentValue);
}

double datedouble = 0;
String tenorExt = getWrapper().getTENEXT();
if (getMajorCode().equalsIgnoreCase("ELC") && prd_typ.equalsIgnoreCase("ELF")
&& getMinorCode().equalsIgnoreCase("POD") && payaction.equalsIgnoreCase("Pay")
&& !tenorExt.equalsIgnoreCase("Yes")
&& (formtyp.equalsIgnoreCase("SOFTEX") || formtyp.equalsIgnoreCase("EDI"))) {
List<ExtEventShippingTable> bill = (List<ExtEventShippingTable>) getWrapper()
      .getExtEventShippingTable();

SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");
SimpleDateFormat parseFormat1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
// SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yy");

List<Date> list = new ArrayList<Date>();
SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
for (int l = 0; l < bill.size(); l++) {
ExtEventShippingTable billref = bill.get(l);

// //Loggers.general().info(LOG,"Shipment date for buyer cradit
// before====>");
String billdat = billref.getBILLDAT().toString();

if (billdat.length() > 0 && billdat != null) {
      // //Loggers.general().info(LOG,"Bill referen date in
      // string----->" + billdat);
      // //Loggers.general().info(LOG,"Bill referen date in
      // string----->" + billdat.length());

      try {
            if (billdat.length() > 10) {
                  String result1 = formatter1.format(parseFormat1.parse(billdat));
                  // //Loggers.general().info(LOG,"Bill referen date
                  // after
                  // result1 if----->" + result1);
                  Date dat = (Date) formatter1.parse(result1);
                  // //Loggers.general().info(LOG,"Bill referen date
                  // after
                  // convert----->" + dat);

                  list.add(dat);
            } else {
                  String result1 = formatter1.format(parseFormat.parse(billdat));
                  // //Loggers.general().info(LOG,"Bill referen date
                  // after
                  // result1 else----->" + result1);
                  Date dat = (Date) formatter1.parse(result1);
                  // //Loggers.general().info(LOG,"Bill referen date
                  // after
                  // convert----->" + dat);

                  list.add(dat);
            }

      } catch (Exception e) {
            Loggers.general().info(LOG, "Exception Bill reference date" + e.getMessage());
      }
} else {

      //// Loggers.general().info(LOG,"Shipping bill date is
      //// empty");
}

}
// Loggers.general().info(LOG,"Payment extention======>" +
// tenorExt);
if (list.size() > 0) {
Date comprareDate = ConnectionMaster.compareAndReturnDate(list);

String resultdate = formatter1.format(comprareDate);

// Loggers.general().info(LOG,"Greater Shipping bill======>" +
// resultdate);

try {
      String d = getWrapper().getNOSTDAT();
      //// Loggers.general().info(LOG,"Nosrto date for
      //// calculation" +
      //// d);
      String d1 = resultdate;
      if (d != null && d1 != null && d1.length() > 0 && d.length() > 0) {
            String dateval = (difference(d, d1).substring(0, difference(d, d1).indexOf(".")));
            // Loggers.general().info(LOG,"Total calculation shipping bill date value====>"
            // + dateval);
            datedouble = Double.valueOf(dateval);
      } else {
            //// Loggers.general().info(LOG,"else part of date
            //// difference values");
      }
} catch (Exception e) {
      // Loggers.general().info(LOG,"exeception date difference
      // values" + e);
}

if (resultdate.length() > 0 && datedouble > paymentValue && (step_Input.equalsIgnoreCase("i"))
            && (step_Id.equalsIgnoreCase("CBS Maker")) && getMinorCode().equalsIgnoreCase("POD")
            && !tenorExt.equalsIgnoreCase("Yes")
            && (formtyp.equalsIgnoreCase("SOFTEX") || formtyp.equalsIgnoreCase("EDI"))) {

      validationDetails.addError(ErrorType.Other,
                  "Kindly make payment extension since payment receive date (Nostro value date, "
                              + datedouble + ") is exceeding " + paymentValue
                              + " days from shipping bill date  [CM]");

} else {
      // Loggers.general().info(LOG,"payment extension values" + datedouble +
      // "paymentValue" + paymentValue);
}
// Payment write off
double dateWrite = 0;
if (tranType.equalsIgnoreCase("WRITEOFF")) {
      try {
            String sysdate = getDriverWrapper().getEventFieldAsText("TDY", "d", "");
            //// Loggers.general().info(LOG,"Nosrto date for
            //// calculation" +
            //// d);
            String d1 = resultdate;

            if (sysdate != null && d1 != null && d1.length() > 0 && sysdate.length() > 0) {
                  String dateval = (difference(sysdate, d1).substring(0,
                              difference(sysdate, d1).indexOf(".")));
                  //// Loggers.general().info(LOG,"Total calculation
                  //// shipping
                  //// bill date value====>" + dateval);
                  dateWrite = Double.valueOf(dateval);
            } else {
                  //// Loggers.general().info(LOG,"else part of date
                  //// difference values");
            }
      } catch (Exception e) {
            // Loggers.general().info(LOG,"exeception date
            // difference
            // values" + e);
      }
      if (resultdate.length() > 0 && dateWrite > paymentValue
                  && (step_Input.equalsIgnoreCase("i")) && (step_Id.equalsIgnoreCase("CBS Maker"))
                  && getMinorCode().equalsIgnoreCase("POD") && !tranType.equalsIgnoreCase("WRITEOFF")
                  && !tenorExt.equalsIgnoreCase("Yes")
                  && (formtyp.equalsIgnoreCase("SOFTEX") || formtyp.equalsIgnoreCase("EDI"))) {
            // Loggers.general().info(LOG,"Inside ilcpoc 6738");
            validationDetails.addError(ErrorType.Other,
                        "Kindly make payment extension since write off date (System's todays date "
                                    + dateWrite + ") is exceeding " + paymentValue
                                    + " days from shipping bill date  [CM]");

      }

} else {
      // Loggers.general().info(LOG,"write off date else" +
      // dateWrite + "paymentValue" + paymentValue);
}

}

} else {
//// Loggers.general().info(LOG,"Subproduct type is local");
}
} catch (Exception e) {
// Loggers.general().info(LOG,"Exception for Notro values getting===>" +
// e);
}

// AdvanceTable date checking
try {
double datedoub = 0;
if (prd_typ.equalsIgnoreCase("ELF") && getMinorCode().equalsIgnoreCase("POD")) {
List<ExtEventAdvanceTable> bill = (List<ExtEventAdvanceTable>) getWrapper()
      .getExtEventAdvanceTable();
SimpleDateFormat parseFormat1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
// String ds1 = "2007-06-30";
SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");

List<Date> list = new ArrayList<Date>();
SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
for (int l = 0; l < bill.size(); l++) {
ExtEventAdvanceTable billref = bill.get(l);

//// Loggers.general().info(LOG,"Shipment date for buyer cradit
//// before====>");
String billdat = billref.getDATREM().toString();

if (billdat.length() > 0 && billdat != null) {

      try {
            if (billdat.length() > 10) {
                  String result1 = formatter1.format(parseFormat1.parse(billdat));
                  // //Loggers.general().info(LOG,"Bill referen date
                  // after
                  // result1 if----->" + result1);
                  Date dat = (Date) formatter1.parse(result1);
                  // //Loggers.general().info(LOG,"Bill referen date
                  // after
                  // convert----->" + dat);

                  list.add(dat);
            } else {
                  String result1 = formatter1.format(parseFormat.parse(billdat));
                  // //Loggers.general().info(LOG,"Bill referen date
                  // after
                  // result1 else----->" + result1);
                  Date dat = (Date) formatter1.parse(result1);
                  // //Loggers.general().info(LOG,"Bill referen date
                  // after
                  // convert----->" + dat);

                  list.add(dat);
            }

      } catch (Exception e) {
            // Loggers.general().info(LOG,"Exception Bill referen
            // date"
            // + e.getMessage());
      }
} else {

      //// Loggers.general().info(LOG,"Shipping bill date is
      //// empty");
}

}
if (list.size() > 0) {
Date comprareDate = ConnectionMaster.compareAndReturnDate(list);

String resultdate = formatter1.format(comprareDate);

//// Loggers.general().info(LOG,"Advance table Greater Shipping
//// bill======>" + resultdate);

String d = "";
try {
      con = getConnection();
      String query = "select TO_CHAR(lcp.PRES_DATE,'DD/MM/YYYY') from master mas, BASEEVENT bev,LCPAYMENT lcp where mas.KEY97 = bev.MASTER_KEY and bev.KEY97 = lcp.KEY97 and mas.REFNO_PFIX = 'ELC' and bev.REFNO_PFIX = 'DPR' and mas.MASTER_REF = '"
                  + MasterReference + "' AND PRES_DATE IS NOT NULL";
      // //Loggers.general().info(LOG,"presantation date take===>"
      // +
      // query);

      ps1 = con.prepareStatement(query);
      rs1 = ps1.executeQuery();
      if (rs1.next()) {
            d = rs1.getString(1);
            //// Loggers.general().info(LOG,"presantation date in
            //// while
            //// loop===>" + d);
      }
} catch (Exception e1) {
      // Loggers.general().info(LOG,"Exception presantation date"
      // +
      // e1.getMessage());
}

try {

      //// Loggers.general().info(LOG,"presentation date for
      //// calculation" + d);
      String d1 = resultdate;
      if (d != null && d1 != null && d1.length() > 0 && d.length() > 0) {
            String dateval = (difference(d, d1).substring(0, difference(d, d1).indexOf(".")));
            //// Loggers.general().info(LOG,"Advance table Total
            //// calculation date value====>" + dateval);
            datedoub = Double.valueOf(dateval);
      } else {
            //// Loggers.general().info(LOG,"else part of date
            //// difference values");
      }
} catch (Exception e) {
      // Loggers.general().info(LOG,"exeception date difference
      // values" + e);
}

if (resultdate.length() > 0 && datedoub > paymentValue && (step_Input.equalsIgnoreCase("i"))
            && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id.equalsIgnoreCase("CBS Maker 1"))) {

      validationDetails.addWarning(ValidationDetails.WarningType.Other,
                  "Advance payment received date is exceeding 9 months (" + datedoub + ")> "
                              + paymentValue + " [CM]");
}

} else {
//// Loggers.general().info(LOG,"Advance table Total calculation
//// date value list size less than 1====>");

}
} else {
//// Loggers.general().info(LOG,"Subproduct type is local");
}
} catch (Exception e) {
// Loggers.general().info(LOG,"Exception for Notro values getting===>" +
// e);
}

try {
String nosamt = getWrapper().getNOSAMT();
String nosacc = getWrapper().getNOSTACC();
String nosamtt = getWrapper().getNOSTAMT();
String nosDate = getWrapper().getNOSTDAT();
String nosPoolamt = getWrapper().getPOOLAMT();
String nosRef103 = getWrapper().getNOSTMT();
String nosREF950 = getWrapper().getNOSTRM();

if (liste.size() > 0 && getMinorCode().equalsIgnoreCase("POD")) {

if ((nosamt.length() > 0 || nosacc.length() > 0 || nosamtt.length() > 0 || nosDate.length() > 0
      || nosPoolamt.length() > 0 || nosRef103.length() > 0 || nosREF950.length() > 0)
      && (step_Input.equalsIgnoreCase("i")) && (step_Id.equalsIgnoreCase("CBS Maker"))) {
validationDetails.addError(ErrorType.Other,
            "Advance payment details are there kindly do not enter Nostro Utilization Amount I/O Nostro details [CM]");
} else {
//// Loggers.general().info(LOG,"Advance remittance grid using
//// this transaction else");
}

} else if ((nosamt.length() > 0 || nosacc.length() > 0 || nosamtt.length() > 0 || nosDate.length() > 0
|| nosPoolamt.length() > 0 || nosRef103.length() > 0 || nosREF950.length() > 0)
&& getMinorCode().equalsIgnoreCase("POD")) {
if (liste.size() > 0 && (step_Input.equalsIgnoreCase("i"))
      && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id.equalsIgnoreCase("CBS Maker 1"))) {
validationDetails.addWarning(WarningType.Other,
            "Notro details are there kindly do not enter Advance payment details [CM]");
} else {
//// Loggers.general().info(LOG,"Nostro values using this
//// transaction else");
}
}
} catch (Exception e) {
// Loggers.general().info(LOG,"Exception for Notro values getting===>" +
// e);
}
if (getMajorCode().trim().equalsIgnoreCase("ILC") && getMinorCode().trim().equalsIgnoreCase("CRC")) {
String mas = getDriverWrapper().getEventFieldAsText("MST", "r", "");
// String = "0172ELFX0003716";
String evt = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
try {
// //Loggers.general().info(LOG,"enter into bill generate no");
String str = mas.substring(4, 16);
// //Loggers.general().info(LOG,"str---->" + str);
String strevt = evt.substring(0, 1);
// //Loggers.general().info(LOG,"strevt ---->" + strevt);

String str11 = evt.substring(3, 6);
// //Loggers.general().info(LOG,"str11 ---->" + str11);
String val1 = str + strevt + str11;
// //Loggers.general().info(LOG,"Total ---->" + val);
getPane().setBLLREFNO(val1);
} catch (Exception e) {
// Loggers.general().info(LOG,"exception" + e);
}
}

// difference between shipment date and expiry date
try {
String d = getDriverWrapper().getEventFieldAsText("HVD", "d", "");
String accept = getDriverWrapper().getEventFieldAsText("FPP:XPT", "s", "");
String d1 = getWrapper().getDASHIP_Name();
if (d != null && d1 != null && d1.length() > 0 && d.length() > 0 && !d.trim().equals("")
&& !d1.trim().equals("") && !d.isEmpty() && !d1.isEmpty()) {
// //Loggers.general().info(LOG,"date of shipment values" + d1);
getPane().setNUMDAYS(difference(d, d1).substring(0, difference(d, d1).indexOf(".")));
getWrapper().setNUMDAYS(difference(d, d1).substring(0, difference(d, d1).indexOf(".")));
} else {
//// Loggers.general().info(LOG,"else part of date difference
//// values");
}
} catch (Exception e) {
e.printStackTrace();
}

// Category code populate based on input branch
try {
String BranchCode = getDriverWrapper().getEventFieldAsText("BIN", "b", "c");
con = ConnectionMaster.getConnection();
if (!(BranchCode.length() == 0)) {
String sql6 = "select telex from capf where cabrnm='" + BranchCode + "'";
// //Loggers.general().info(LOG,"BranchCode Query - " + sql6);
PreparedStatement ps1 = con.prepareStatement(sql6);
ResultSet rs = ps1.executeQuery();
while (rs.next()) {
String inmt = rs.getString(1);
// //Loggers.general().info(LOG,"category code - " + inmt);
getWrapper().setIMBRCODE(inmt);
getPane().setIMBRCODE(inmt);
}

}
} catch (Exception e) {
// Loggers.general().info(LOG,"Exception caught on branch code
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
//// Loggers.general().info(LOG,"Connection Failed! Check output
//// console");
e.printStackTrace();
}
}

try {

String IEcodec = getDriverWrapper().getEventFieldAsText("DRW", "p", "cBBF");
// //Loggers.general().info(LOG,"IEcodecode" + IEcodec);
List<ExtEventShippingTable> shpcol5 = (List<ExtEventShippingTable>) getWrapper()
.getExtEventShippingTable();
BigDecimal hundred = new BigDecimal(100);
BigDecimal colamtct = new BigDecimal(0.00);
System.out.println("shipamt FIELDS:" + colamtct + " " + hundred);
String colcurr = "";
for (int l = 0; l < shpcol5.size(); l++) {
ExtEventShippingTable shipCol = shpcol5.get(l);
String shipBilNo = shipCol.getBILLNUM();
// //Loggers.general().info(LOG,"ship BilNo softax ---->" +
// shipBilNo);
String formNo = shipCol.getFORMNUM();
String IEcode = shipCol.getIECOD();
String shippingBill = shipCol.getBILLNUM();
BigDecimal shipamt = shipCol.getSHPAMT();
colcurr = shipCol.getSHPAMTCurrency().trim();
System.out.println("shipamt:" + shipamt);
if ((formNo.trim().equalsIgnoreCase("") || formNo.trim().isEmpty() || formNo == null)
      && (shippingBill.trim().equalsIgnoreCase("") || shippingBill.trim().isEmpty()
                  || shippingBill == null)) {
System.out.println("shippingBill validation: " + formNo + " " + shippingBill);
validationDetails.addWarning(WarningType.Other, "Kindly fill shipping bill or form number");
}
if ((!formNo.trim().equalsIgnoreCase("") || !formNo.trim().isEmpty())
      && (!shippingBill.trim().equalsIgnoreCase("") || !shippingBill.trim().isEmpty())) {
System.out.println("shippingBill and formno validation: " + formNo + " " + shippingBill);
validationDetails.addWarning(WarningType.Other, "Kindly remove shipping bill or form number");
}
colamtct = colamtct.add(shipamt);
if (IEcode.trim().length() > 0 && (step_Input.equalsIgnoreCase("i"))
      && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id.equalsIgnoreCase("CBS Maker 1"))) {
if (IEcodec.equalsIgnoreCase("IEcode")) {
      validationDetails.addWarning(WarningType.Other,
                  "Please enter Valid IECODE (" + IEcodec + ") [CM]");
}
}

}

int shipcountSize = shpcol5.size();
System.out.println("shipcountSize: " + shipcountSize);
String value = String.valueOf(shipcountSize);
getPane().setNOOFBLLS(value);
getPane().setNOOFBILL(value);
colamtct = colamtct.divide(hundred);
colamtct = colamtct.setScale(2, BigDecimal.ROUND_HALF_EVEN);
System.out.println(colamtct);
if (colcurr != null && !colcurr.trim().equals("")) {
getPane().setTOTSBAMT(colamtct.toString() + " " + colcurr);
}
String sizeship = getWrapper().getNOOFBLLS();
if (!(shpcol5.size() == (Integer.valueOf(sizeship))) && getMinorCode().equalsIgnoreCase("DOP")
&& (step_Input.equalsIgnoreCase("i")) && (step_Id.equalsIgnoreCase("CBS Maker"))) {
validationDetails.addError(ErrorType.Other, "Number of Shipping bills(" + sizeship
      + ")is not equal to ShippingDetails grid(" + shpcol5.size() + ") :[CM]");
}

} catch (Exception e) {
// Loggers.general().info(LOG,"Exception in EDI for softax" +
// e.getMessage());
}

// cut off date validation
//
// try {
// String t = getPane().getCUTOFFDT();
// String t1 = getDriverWrapper().getEventFieldAsText("TDY", "d",
// "");
// SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
// Date date1 = sdf.parse(t1.trim());
// Date date2 = sdf.parse(t.trim());
//
// // //Loggers.general().info(LOG,sdf.format(date1));
// // //Loggers.general().info(LOG,sdf.format(date2));
// if (date1.compareTo(date2) >= 0 &&
// (step_Input.equalsIgnoreCase("i"))
// && (step_Id.equalsIgnoreCase("CBS Maker"))) {
// validationDetails.addWarning(WarningType.Other,
// "Cut off date is should earlier than the System date [CM]");
//
// // //Loggers.general().info(LOG,"Date1 is after Date2");
// } else if (date1.compareTo(date2) < 0) {
// // //Loggers.general().info(LOG,"Date1 is before Date2");
// } else if (date1.compareTo(date2) == 0) {
// // //Loggers.general().info(LOG,"Date1 is equal to Date2");
// } else {
// // //Loggers.general().info(LOG,"How to get here?");
// }
//
// } catch (Exception e) {
// //Loggers.general().info(LOG,e.getMessage());
// }

// populating from and to insurance details
try {
String from = getDriverWrapper().getEventFieldAsText("SDF", "s", "").trim();
String to = getDriverWrapper().getEventFieldAsText("SDT", "s", "").trim();
// //Loggers.general().info(LOG,"shipment from "+from+" Shipment to
// "+to);
String insform = getWrapper().getINSFROM().trim();
String insto = getWrapper().getINSTO().trim();
if (insform.equalsIgnoreCase("") || insform == null) {
getPane().setINSFROM(from);
} else {
getPane().setINSFROM(insform);
}
if (insto.equalsIgnoreCase("") || insto == null) {
getPane().setINSTO(to);
}

else {
getPane().setINSTO(insto);
}
} catch (Exception e) {
// Loggers.general().info(LOG,e.getMessage());

}

// shipping bill and port code validation for collection table
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

      String query1 = "SELECT COUNT(*) FROM EXTEVENTSHT WHERE FORMNUM= '" + formno + "'";
      // //Loggers.general().info(LOG,"Query1 for shipping bill
      // duplicate" + query1);
      int count1 = 0;
      ps2 = con.prepareStatement(query1);
      rs2 = ps2.executeQuery();
      while (rs2.next()) {
            // //Loggers.general().info(LOG,"Entered while");
            count1 = rs2.getInt(1);
            // //Loggers.general().info(LOG,"value of count in while
            // "
            // + count);
      }
      if (count1 > 1 && (step_Input.equalsIgnoreCase("i"))
                  && (step_Id.equalsIgnoreCase("CBS Maker")
                              || step_Id.equalsIgnoreCase("CBS Maker 1"))
                  && getMinorCode().equalsIgnoreCase("DOP")) {
            // //Loggers.general().info(LOG,"Count is " + count);
            validationDetails.addWarning(WarningType.Other,
                        "Entered Form Number is already Exist  [CM]");
      } else {
            //// Loggers.general().info(LOG,"Form Number is valid");
      }

} catch (Exception e1) {
      // Loggers.general().info(LOG,"Exception CHECK===>" +
      // e1.getMessage());
}
try {
      String query1 = "SELECT COUNT(*) FROM EXTEVENTSHC WHERE CFORMN= '" + formno + "'";
      // //Loggers.general().info(LOG,"Query1 for shipping bill
      // duplicate" + query1);
      int count1 = 0;
      ps2 = con.prepareStatement(query1);
      rs2 = ps2.executeQuery();
      while (rs2.next()) {
            // //Loggers.general().info(LOG,"Entered while");
            count1 = rs2.getInt(1);
            // //Loggers.general().info(LOG,"value of count in while
            // "
            // + count);
      }
      if (count1 > 1 && (step_Input.equalsIgnoreCase("i"))
                  && (step_Id.equalsIgnoreCase("CBS Maker")
                              || step_Id.equalsIgnoreCase("CBS Maker 1"))
                  && getMinorCode().equalsIgnoreCase("DOP")) {
            // //Loggers.general().info(LOG,"Count is " + count);
            validationDetails.addWarning(WarningType.Other,
                        "Entered Form Number is already Exist  [CM]");
      } else {
            //// Loggers.general().info(LOG,"Form Number is valid");
      }

} catch (Exception e) {
      // Loggers.general().info(LOG,"Exception for Form Number is
      // valid" + e);
}
} else {
//// Loggers.general().info(LOG,"Form Number is empty");
}

}
} catch (Exception e1) {
// Loggers.general().info(LOG,"Exception for form no is already used" +
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
//// Loggers.general().info(LOG,"Connection Failed! Check output
//// console");
e.printStackTrace();
}
}
// ---------------------------

// port of destination description

String portdes = getWrapper().getPORTDES().trim();
// //Loggers.general().info(LOG,"Port of destination Value---->" + portdes);
if ((!portdes.equalsIgnoreCase("")) && portdes != null) {
try {
String portvalqury = "select trim(PODEST),trim(COUNTRY) from EXTPORTDESTINATION WHERE PODEST='"
      + portdes + "'";
// //Loggers.general().info(LOG,"port code destination query
// Value---->" +
// portvalqury);
con = ConnectionMaster.getConnection();
ps1 = con.prepareStatement(portvalqury);
rs = ps1.executeQuery();
while (rs.next()) {
String hsploy = rs.getString(1);
String COUNTRY = rs.getString(2);
// //Loggers.general().info(LOG,"port code description---->" +
// hsploy);
getPane().setPODESCP(hsploy);
getPane().setPODESCON(COUNTRY);
}

// con.close();
// ps1.close();
// rs.close();
} catch (Exception e) {
// Loggers.general().info(LOG,"exception is " + e.getMessage());
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
//// Loggers.general().info(LOG,"Connection Failed! Check output
//// console");
e.printStackTrace();
}
}
} else {
// //Loggers.general().info(LOG,"port code is destination empty");
}

// --------------------

// String stepcd = getDriverWrapper().getEventFieldAsText("ECOI",
// "s", "").trim();

// SUPPLIERS CREDIT
String issDate = getDriverWrapper().getEventFieldAsText("ISS", "d", "");
String expDate = getDriverWrapper().getEventFieldAsText("EXP", "d", "");
// Loggers.general().info(LOG,"suppliers credit issdate " + issDate +
// "expDate" + expDate);
int tnr = 0;
try {

//// Loggers.general().info(LOG,"Nosrto date for calculation" +
//// d);

if (issDate != null && expDate != null && issDate.length() > 0 && expDate.length() > 0) {
String dateval = (difference(expDate, issDate).substring(0,
      difference(expDate, issDate).indexOf(".")));
// Loggers.general().info(LOG,"SUPPLIERS date differnce" + dateval);
tnr = Integer.parseInt(dateval);

} else {
//// Loggers.general().info(LOG,"else part of date
//// difference values");
}
// Loggers.general().info(LOG,"SUPPLIERS date in number" + tnr);
if (tnr >= 180) {
// //Loggers.general().info(LOG,"suppliers credit is yes ");
getWrapper().setSUPLCRE(true);
getPane().setSUPLCRE(true);

// //Loggers.general().info(LOG,"suppliers credit is yes ended ");

} else {
getWrapper().setSUPLCRE(false);
getPane().setSUPLCRE(false);
}

} catch (Exception e) {
// Loggers.general().info(LOG,"SUPPLIER exeception date difference
// values" + e);
}

// PAN number validation
try {
if (getDriverWrapper().getEventFieldAsText("EVCD", "s", " ").trim().equalsIgnoreCase("PCOC")
&& (step_Input.equalsIgnoreCase("i")) && (step_Id.equalsIgnoreCase("CBS Maker")))
;
{
double amt;
// //Loggers.general().info(LOG,"PAN VALIDATION");

String product = getDriverWrapper().getEventFieldAsText("", "r", "");
String panamt = getDriverWrapper().getEventFieldAsText("AMT", "v", "i");
// //Loggers.general().info(LOG,"panamt " +panamt);
String pannumber = getDriverWrapper().getEventFieldAsText("cAGE", "s", "");
// //Loggers.general().info(LOG,"pannumber"+pannumber);
// //Loggers.general().info(LOG,"pannumber length" +
// pannumber.length());
if (panamt.length() == 1) {

amt = 0.0;
} else {
// //Loggers.general().info(LOG,"first else block");
amt = Integer.parseInt(panamt.substring(0, panamt.length() - 3));
if (amt >= 25000 && pannumber.length() == 1) {
      // validationDetails.addWarning(ValidationDetails.WarningType.Other,
      // "Please enter the PAN Number");
      //// Loggers.general().info(LOG,"second if block");
}
}
}
} catch (Exception e) {
// Loggers.general().info(LOG,"exception caught" + e.getMessage());
}

// IFSC code validation

String Ifsc = getWrapper().getIFSCCO_Name().trim();
// //Loggers.general().info(LOG,"IFSC Code"+Ifsc);
if (Ifsc.trim().equalsIgnoreCase("") || Ifsc == null) {
// validationDetails.addWarning(WarningType.Other, "Receiver
// IFSC
// code should not be blank");
} else {
if ((!Ifsc.trim().equalsIgnoreCase("") || Ifsc != null) && (step_Input.equalsIgnoreCase("i"))
&& (step_Id.equalsIgnoreCase("CBS Maker"))) {
try {
con = getConnection();
String query = "select count(*) from EXTIFSCC where IFSC='" + Ifsc + "'";
// //Loggers.general().info(LOG,"query " + query);
int count = 0;
ps1 = con.prepareStatement(query);
rs1 = ps1.executeQuery();
while (rs1.next()) {
      // //Loggers.general().info(LOG,"Entered while");
      count = rs1.getInt(1);
      // //Loggers.general().info(LOG,"value of count in while
      // "+count);
}

if (count == 0 && (step_Input.equalsIgnoreCase("i"))
            && (step_Id.equalsIgnoreCase("CBS Maker"))) {

      // //Loggers.general().info(LOG,"If in IFSC");
      validationDetails.addError(ErrorType.Other,
                  "Invalid Beneficiary IFSC code(" + Ifsc + ")  [CM]");
}
// con.close();
// ps1.close();
// rs1.close();

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
      //// Loggers.general().info(LOG,"Connection Failed! Check
      //// output console");
      e.printStackTrace();
}
}
}

}

// Margin validation
try {
// //Loggers.general().info(LOG,"Margin validation part Entered");
// boolean isMarginAvailableForCIF =
// checkAvailblityOfMarginForCIF(con);

String facilityId = getDriverWrapper().getEventFieldAsText("cBEC", "s", "");

String margin = getWrapper().getMARAMT();
if (margin == null) {
margin = "0";
}
// //Loggers.general().info(LOG,"Current margin amount " + margin);
double marDob = Double.valueOf(margin);
// calculateMargin();
List<ExtEventLienMarking> ExtEventLienMark = (List<ExtEventLienMarking>) getWrapper()
.getExtEventLienMarking();
// //Loggers.general().info(LOG,"ExtEventLienMark ILC------> " +
// ExtEventLienMark.size());
double totalMargin = 0.0;
String dopNo = "";
int counterVal = 0;
ArrayList<Double> arr = new ArrayList<Double>();
for (int l = 0; l < ExtEventLienMark.size(); l++) {
ExtEventLienMarking eventLien = (ExtEventLienMarking) ExtEventLienMark.get(l);
// BigDecimal clearBal = eventLien.getCLEARBLC();
BigDecimal marginAmt = eventLien.getMARGAMT();
String liensta = eventLien.getLINEST();
dopNo = eventLien.getDEPOSTNO().trim();
// Loggers.general().info(LOG,"Lien status for mark----> " +
// liensta);
// double clearB = 0.0;

if (ExtEventLienMark.size() > 0 && (liensta.equalsIgnoreCase("") || liensta == null)
      && (step_Input.equalsIgnoreCase("i")) && (step_Id.equalsIgnoreCase("CBS Maker"))) {
validationDetails.addError(ErrorType.Other,
            "Lien amount is available please Mark the lien [CM]");
}

double marginDob = eventLien.getMARGAMT().doubleValue();
if ((ExtEventLienMark.size() > 0) && (marginDob == 0.0 || marginDob == 0 || marginDob < 1)
      && (step_Input.equalsIgnoreCase("i")) && (step_Id.equalsIgnoreCase("CBS Maker"))) {
validationDetails.addError(ErrorType.Other, "Lien amount should be greater than Zero [CM]");
pane.getExtEventLienMarkingUpdate().setEnabled(true);
}

if (marginAmt != null)
marginB = Double.valueOf(marginAmt.doubleValue() / 100);

totalMargin += marginB;
if (ExtEventLienMark.size() > 0 && totalMargin > marDob && (step_Input.equalsIgnoreCase("i"))
      && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id.equalsIgnoreCase("CBS Maker 1"))) {
// //Loggers.general().info(LOG,"totalMargin -----> " +
// totalMargin);
validationDetails.addWarning(WarningType.Other,
            "Grid Margin amount should not greater then the Margin Amount  [CM]");
} else {
//// Loggers.general().info(LOG,"totalMargin is less then the
//// margin amount ");
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
//// Loggers.general().info(LOG,"lien status array list
//// arr---->" + arr);

} else {
//// Loggers.general().info(LOG,"lien status in else loop---->"
//// + liensta);
}
}

if (step_Id.equalsIgnoreCase("CBS Maker 1") && ExtEventLienMark.size() > 0) {
try {

String query = "SELECT count(*),lmg.DEPOSTNO,lmg.LINEST FROM MASTER mas, BASEEVENT bas, EXTEVENTLMG lmg WHERE mas.KEY97 =bas.MASTER_KEY AND bas.EXTFIELD =lmg.FK_EVENT AND lmg.DEPOSTNO='"
            + dopNo + "' AND lmg.LINEST ='MARK SUCCEEDED' AND mas.MASTER_REF ='" + MasterReference
            + "' AND bas.REFNO_PFIX ='" + evnt + "' AND bas.REFNO_SERL=" + evvcount
            + " group by lmg.DEPOSTNO,lmg.LINEST";
if (dailyval_Log.equalsIgnoreCase("YES")) {
      Loggers.general().info(LOG, "lien status check---->" + query);
}
con = getConnection();
ps1 = con.prepareStatement(query);
rs1 = ps1.executeQuery();
while (rs1.next()) {
      counterVal = rs1.getInt(1);
      if (dailyval_Log.equalsIgnoreCase("YES")) {
            Loggers.general().info(LOG, "lien counterVal in while---->" + counterVal);
      }

}
} catch (Exception e) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
      Loggers.general().info(LOG, "Lien account no duplication" + e.getMessage());
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

if (ExtEventLienMark.size() > 0 && counterVal > 1 && (step_Id.equalsIgnoreCase("CBS Maker 1"))) {

if (dailyval_Log.equalsIgnoreCase("YES")) {
      Loggers.general().info(LOG,
                  "Multiple liens cannot be marked for same account number---->" + dopNo);
}

validationDetails.addError(ErrorType.Other,
            "Multiple liens cannot be marked for same account number (" + dopNo
                        + "). Reverse lien for account number and mark new lien [CM]");
}

else if (step_Id.equalsIgnoreCase("CBS Maker 1") && ExtEventLienMark.size() > 0) {
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
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        // Loggers.general().info(LOG,"Lien Status in else" + lienbox);
                  }
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
      if (dailyval_Log.equalsIgnoreCase("YES")) {
            // Loggers.general().info(LOG,"Themebridge finalXml for enquiry" + finalXml);
      }
      ThemeTransportClient anThemeTransportClient = new ThemeTransportClient();
      String responseXML = anThemeTransportClient.invoke("Customization", "FDLienAdd", finalXml);
      if (dailyval_Log.equalsIgnoreCase("YES")) {
            Loggers.general().info(LOG,
                        "Final Themebridge responseXML for enquiry==========>" + responseXML);
      }
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
                  {
                        // Loggers.general().info(LOG,"Lien mark Amount" + lienAmt);
                  }
                  String currentDepositNum = res_sp_line[0].trim();
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        // Loggers.general().info(LOG,"Lien response currentDepositNum" +
                        // currentDepositNum);
                  }
                  String responseValue = res_sp_line[3].trim();
                  BigDecimal responseAmount = new BigDecimal(responseValue);
                  String responseAmt = diff.format(responseAmount);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        // Loggers.general().info(LOG,"Lien response amount" + responseAmt);
                  }
                  String markVal = res_sp_line[4].trim();
                  String markValue = "MARK " + markVal;
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        // Loggers.general().info(LOG,"Lien mark response Value" + markValue);
                  }
                  String lienbox = str.getLINEST().trim();
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        // Loggers.general().info(LOG,"Lien mark grig Value" + lienbox);
                  }
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
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    // Loggers.general().info(LOG,"lien and response amount is same else" +
                                    // lienAmt
                                    // + "responseAmt" + responseAmt);
                              }
                        }
                  } else {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              // Loggers.general().info(LOG,"lien depositNum else" + depositNum
                              // + "currentDepositNum===>" + currentDepositNum);
                        }
                  }

            }

      }

      // }
} catch (Exception e) {
      if (dailyval_Log.equalsIgnoreCase("YES")) {
            Loggers.general().info(LOG,
                        "ThemeTransportClient Exception response " + e.getMessage());
      }
}
} else {

}

}

// validation mark succeed
double dob1 = 0;
for (Double dob : arr) {

dob1 = dob1 + dob;
// Loggers.general().info(LOG,"marksucceed=======>" + dob);
}

// Loggers.general().info(LOG,"marksucceed=======>" + dob1);
// Loggers.general().info(LOG,"arr.size() =======>" + arr.size() );

if (arr.size() > 0 && (step_Input.equalsIgnoreCase("i"))
&& (step_Id.equalsIgnoreCase("CBS Maker") || step_Id.equalsIgnoreCase("CBS Maker 1"))
&& getMinorCode().equalsIgnoreCase("POC")) {
Loggers.general().info(LOG, "Marginamt_log for -----> " + dob1);
validationDetails.addWarning(WarningType.Other,
      "Total Lien Mark amount is (" + dob1 + " INR), Please Release     the Lien [CM]");
} else {
//// Loggers.general().info(LOG,"Lien mark amount in else" + dob1);
}

if (marDob > 0 && ExtEventLienMark.size() == 0 && (step_Input.equalsIgnoreCase("i"))
&& (step_Id.equalsIgnoreCase("CBS Maker") || step_Id.equalsIgnoreCase("CBS Maker 1"))
&& (getMinorCode().equalsIgnoreCase("ISI") || getMinorCode().equalsIgnoreCase("NADI")
            || getMinorCode().equalsIgnoreCase("NAMI"))) {
// //Loggers.general().info(LOG,"ExtEventLienMark ILC--> " +
// ExtEventLienMark + " & marginAmt ILC ---> " + marDob);
validationDetails.addWarning(WarningType.Other,
      "Lien amount is calculated and no Lien Amount is entered in FD lien grid  [CM]");
} else {
//// Loggers.general().info(LOG," FD lien grid is entered----->");

}

// //Loggers.general().info(LOG,"totalMargin - "+totalMargin);
String expectedMarginStr = getDriverWrapper().getEventFieldAsText("cAAR", "v", "m").trim();
// Loggers.general().info(LOG,"expectedMarginStr --------- " +
// expectedMarginStr);
double expectedMarginAmt = 0.0;
if (expectedMarginStr.length() != 0) {
expectedMarginAmt = Double.parseDouble(expectedMarginStr);
// Loggers.general().info(LOG,"expectedMarginAmt - " +
// expectedMarginAmt);
}
/*
* Loggers.general().info(LOG,"expectedMarginAmt --------- " +
* expectedMarginAmt); Loggers.general().info(LOG,"totalMargin--------- " +
* totalMargin); Loggers.general().info(LOG,"ExtEventLienMark.size() --------- "
* + ExtEventLienMark.size());
*/
if ((ExtEventLienMark.size() > 0) && (totalMargin != expectedMarginAmt)
&& (step_Input.equalsIgnoreCase("i"))
&& (step_Id.equalsIgnoreCase("CBS Maker") || step_Id.equalsIgnoreCase("CBS Maker 1"))) {
validationDetails.addWarning(WarningType.Other,
      "Sum of margin amount should be equal to Required Lien Amount  [CM]");
}

} catch (Exception e) {
// Loggers.general().info(LOG,"Exception occured in margin validation -
// " + e.getMessage());
}

String cust = "";
if (getMajorCode().equalsIgnoreCase("ILC")) {
cust = getDriverWrapper().getEventFieldAsText("PRM", "p", "no").trim();
}

else if (getMajorCode().equalsIgnoreCase("ELC")) {

cust = getDriverWrapper().getEventFieldAsText("BEN", "p", "no");
}
String masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
String eventREF = getDriverWrapper().getEventFieldAsText("MEVR", "r", "").trim();

String chargecol = getDriverWrapper().getEventFieldAsText("BOTC", "l", "");
// //Loggers.general().info(LOG,"charge account collected " + chargecol);
String acctno = getDriverWrapper().getEventFieldAsText("CHA", "a", "").trim();
// Loggers.general().info(LOG,"charge account in event field======>" +
// acctno);
String custval = "";

try {
if (step_Id.equalsIgnoreCase("CBS Maker") || step_Id.equalsIgnoreCase("CBS Maker 1")) {

con = getConnection();

String dmstlmt = "select TRIM(CUS_MNM) from ETT_CUS_ACCT_SETTLE where MASTER_REF ='" + masterref
      + "' and EVENT_REF = '" + eventREF + "'";
dmsp = con.prepareStatement(dmstlmt);

dmsr = dmsp.executeQuery();
while (dmsr.next()) {

custval = dmsr.getString(1);

if (custval.length() > 0 && chargecol.equalsIgnoreCase("Y")
            && (!chargecol.equalsIgnoreCase("N"))) {

      //// Loggers.general().info(LOG,"custoemr number in query" +
      //// custval);
      //// Loggers.general().info(LOG,"custoemr number in
      //// transaction" + cust);
      validationDetails.addWarning(WarningType.Other,
                  "Account selected in Settlement for charges does not belong to the Applicant  [CM]");
} else {
      // Loggers.general().info(LOG,"charge account collected
      // matched");
}

}

String dms = "select trim(acc.BO_ACCTNO) from ACCOUNT acc where acc.CUS_MNM ='" + cust
      + "' and acc.BO_ACCTNO='" + acctno + "'";
if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"ETT_CUS_ACCT_SETTLE QUERY====> " + dms);
}
dmsp = con.prepareStatement(dms);

dmsr = dmsp.executeQuery();
if (dmsr.next()) {
//// Loggers.general().info(LOG,"custoemr number in query if
//// loop----> " + dmsr.getString(1));
custval = dmsr.getString(1);

//// Loggers.general().info(LOG,"custoemr number in transaction
//// if loop" + acctno);

}

else {
//// Loggers.general().info(LOG,"custoemr number in query else
//// loop" + custval);
//// Loggers.general().info(LOG,"custoemr number in transaction
//// else loop" + acctno);
if (acctno.length() > 0 && chargecol.equalsIgnoreCase("Y")
            && (!chargecol.equalsIgnoreCase("N"))) {

      if ((step_Id.equalsIgnoreCase("CBS Maker") || step_Id.equalsIgnoreCase("CBS Maker 1"))
                  && (step_Input.equalsIgnoreCase("i"))) {

            validationDetails.addWarning(WarningType.Other,
                        "Account selected for princial and charges does not belong to the Applicant [CM]");

      } else {
            //// Loggers.general().info(LOG,"Not CBS Maker for
            //// charge account");
      }
} else {
      //// Loggers.general().info(LOG,"charge account collected
      //// check box in else " + chargecol);
}
}

} else {
//// Loggers.general().info(LOG,"Not CBS Maker for charge account");
}

} catch (Exception e) {
// Loggers.general().info(LOG,"charge account collected----->" +
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
//// Loggers.general().info(LOG,"Connection Failed! Check output
//// console");
e.printStackTrace();
}
}
//if ((step_Input.equalsIgnoreCase("i")) && (step_Id.equalsIgnoreCase("CSM")
//|| step_Id.equalsIgnoreCase("CBS Maker") || step_Id.equalsIgnoreCase("CBS Maker 1"))
//&& getMinorCode().equalsIgnoreCase("POC")) {
//try {
//
//String dms = "select * from ETTV_BOE_PENDING_VIEW where CIF='" + cust + "'";
//if (dailyval_Log.equalsIgnoreCase("YES")) {
//Loggers.general().info(LOG,"ETTV_BOE_PENDING_VIEW QUERY ----> " + dms);
//}
//con = getConnection();
//ps1 = con.prepareStatement(dms);
//
//rs1 = ps1.executeQuery();
//while (rs1.next()) {
//
//if (getMinorCode().equalsIgnoreCase("POC")) {
//    validationDetails.addWarning(WarningType.Other,
//                "Bill of entry pending for this client [CM]");
//
//} else {
//    if (dailyval_Log.equalsIgnoreCase("YES")) {
//          //Loggers.general().info(LOG,"Bill of entry pending for this client else ----> ");
//    }
//}
//
//}
//
//} catch (Exception e) {
//if (dailyval_Log.equalsIgnoreCase("YES")) {
////Loggers.general().info(LOG,"Exception Bill of entry pending" + e.getMessage());
//}
//} finally {
//try {
//
//if (rs1 != null)
//    rs1.close();
//if (ps1 != null)
//    ps1.close();
//if (con != null)
//    con.close();
//
//} catch (SQLException e) {
//// Loggers.general().info(LOG,"Connection Failed! Check output
//// console");
//e.printStackTrace();
//}
//}
//}
// NPA customer

try {

String productVal = "N";
if (getMajorCode().equalsIgnoreCase("ILC")) {
productVal = getDriverWrapper().getEventFieldAsText("APP", "p", "cAJB");
}

else if (getMajorCode().equalsIgnoreCase("ELC") || getMajorCode().equalsIgnoreCase("EGT")) {

productVal = getDriverWrapper().getEventFieldAsText("BEN", "p", "cAJB");
} else if (getMajorCode().equalsIgnoreCase("ODC")) {
productVal = getDriverWrapper().getEventFieldAsText("DRW", "p", "cAJB");
}

if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"NPA customer-----> " + productVal);
}
if (productVal.equalsIgnoreCase("Y") && (step_Input.equalsIgnoreCase("i"))
&& (step_Id.equalsIgnoreCase("AdhocCSM") || step_Id.equalsIgnoreCase("CSM")
            || step_Id.equalsIgnoreCase("CBS Maker") || step_Id.equalsIgnoreCase("CBS Maker 1"))) {
validationDetails.addWarning(WarningType.Other, "Customer is a NPA customer [CM]");
} else {
if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Not a NPA customer-----> " + productVal);
}

}

} catch (Exception e) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Exception NPA customer-----> " + e.getMessage());
}
}

if (getMajorCode().equalsIgnoreCase("ILC") && getMinorCode().equalsIgnoreCase("CRC")) {
try {

String presentCur = getDriverWrapper().getEventFieldAsText("AMPR", "v", "c");
String presentCurVal = getDriverWrapper().getEventFieldAsText("OPCY", "s", "");

if (dailyval_Log.equalsIgnoreCase("YES")) {
/*
 * Loggers.general().info(
 * LOG,"Presentation amount currency and presentation currency" + presentCur +
 * "" + presentCurVal);
 */
}
if (presentCur.length() > 0 && presentCurVal.length() > 0
      && (!presentCur.equalsIgnoreCase(presentCurVal)) && step_Input.equalsIgnoreCase("i")
      && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id.equalsIgnoreCase("CBS Maker 1"))) {
validationDetails.addError(ErrorType.Other,
            "Presentation amount currency and presentation currency should be same [CM]");
} else {
if (dailyval_Log.equalsIgnoreCase("YES")) {
      /*
       * Loggers.general().info(
       * LOG,"Presentation amount currency and presentation currency in else" +
       * presentCur + "" + presentCurVal);
       */
}

}

} catch (Exception e) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Exception Presentation amount currency" + e.getMessage());
}
}
}

// getSubvention

try {
// //Loggers.general().info(LOG,"get value for Subvention");
getSubvention();
} catch (Exception ee) {
//// Loggers.general().info(LOG,ee.getMessage());
// //Loggers.general().info(LOG,"getSubvention");
} finally {
// //Loggers.general().info(LOG,"finally LOB ");
}

// hs code population

if (getMajorCode().equalsIgnoreCase("ELC") && getMinorCode().equalsIgnoreCase("DOP")) {
String hscodeval = getWrapper().getHMON().trim();
// //Loggers.general().info(LOG,"hscode Value---->" + hscodeval);
if ((!hscodeval.equalsIgnoreCase("")) && hscodeval != null) {
try {
// //Loggers.general().info(LOG,"hscode Value in try---->" +
// hscodeval);
String hyperValue = "SELECT trim(HSEXPPOY) FROM EXTHMCODE WHERE HCODEE='" + hscodeval + "'";
// //Loggers.general().info(LOG,"Hs code query Value---->" +
// hyperValue);
con = ConnectionMaster.getConnection();
ps1 = con.prepareStatement(hyperValue);
rs = ps1.executeQuery();
while (rs.next()) {
      String hsploy = rs.getString(1);
      // //Loggers.general().info(LOG,"Policy value---->" + hsploy);
      getPane().setHSPOLY(hsploy);
}

// con.close();
// ps1.close();
// rs.close();
} catch (Exception e) {
// Loggers.general().info(LOG,"exceptio is " + e);
} finally {
try {
      if (rs != null)
            rs.close();
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
} else {
// //Loggers.general().info(LOG,"HS code is empty for policy ");
}
String tranportDate = getWrapper().getTRADOCDT().trim();
String eventStartdate = getDriverWrapper().getEventFieldAsText("PRD", "d", "");
String received = getDriverWrapper().getEventFieldAsText("PRD", "d", "");
String noofbills = getDriverWrapper().getEventFieldAsText("cANT", "s", "");
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
Calendar cal = Calendar.getInstance();
try {
cal.setTime(sdf.parse(tranportDate));
cal.add(Calendar.DATE, 21);
getPane().setLOANDATE(sdf.format(cal.getTime()));
System.out.println("Transport Document date: " + sdf.format(cal.getTime()));

String output = sdf.format(cal.getTime());
java.util.Date tranDate = sdf.parse(output);
java.util.Date startDate = sdf.parse(eventStartdate);
java.util.Date receivedDate = sdf.parse(received);
//System.out.println("date Remmitance: " + remitDate);
if (startDate.after(tranDate)) {
getPane().setLATEDPR("YES");
} else {
getPane().setLATEDPR("");
}

long timeDiff = Math.abs(receivedDate.getTime() - tranDate.getTime());
long daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
System.out.println("The of days between dates " + daysDiff);

BigDecimal daysDiffdb = new BigDecimal(daysDiff);
BigDecimal noofbillsbd = new BigDecimal(noofbills);
BigDecimal value = new BigDecimal(91.5);
BigDecimal quarter = daysDiffdb.divide(value, RoundingMode.UP);
BigDecimal charges = (quarter.multiply(noofbillsbd));
getPane().setDELAYEDS(charges.toString());
System.out.println("The of days quarter " + quarter + " " + charges);

} catch (Exception e) {
e.printStackTrace();
}
}

// The master has already shipping guarantee issued validation
try {
// //Loggers.general().info(LOG,"Shipping guarantee");
if (getDriverWrapper().getEventFieldAsText("EVCD", "s", "").trim().equalsIgnoreCase("CRC")) {
// //Loggers.general().info(LOG,"enter into shipping guarantee
// issued");
// String masterRefNumber =
// getDriverWrapper().getEventFieldAsText("MST", "r", "");
// String stepid =
// getDriverWrapper().getEventFieldAsText("CSID", "s", "");
// //Loggers.general().info(LOG,"step is information" + stepid);
if ((step_Input.equalsIgnoreCase("i"))
      && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id.equalsIgnoreCase("CBS Maker 1"))) {
String qr = "select count(*) from master m, baseevent b where     m.key97 = b.master_key and b.refno_pfix = 'LSC' and b.status = 'c' and m.master_ref='"
            + masterRefNumber + "'";
// //Loggers.general().info(LOG,"Query " + qr);
con = ConnectionMaster.getConnection();
ps1 = con.prepareStatement(qr);
// //Loggers.general().info(LOG,qr);
rs = ps1.executeQuery();
while (rs.next()) {
      String k = rs.getString(1);
      // //Loggers.general().info(LOG,"while of count of hscode "
      // +k);
      int ka = Integer.parseInt(k);
      if (ka == 0) {

      } else {
            validationDetails.addWarning(ValidationDetails.WarningType.Other,
                        "Shipping guarantee exists for this Lc  [CM]");
      }
}
// con.close();
// peco.close();
// rs.close();
}
} else {
//// Loggers.general().info(LOG,"shipping guarantee issued");
}
} catch (Exception e) {
// //Loggers.general().info(LOG,"exception caught " +e.getMessage() );
} finally {
try {
if (rs != null)
rs.close();
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

// Account realization warning
try {
List<ExtEventAccountRealisation> Acr = (List<ExtEventAccountRealisation>) getWrapper()
.getExtEventAccountRealisation();
if (Acr.size() > 0
&& getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim().substring(0, 2) == "POD"
&& (step_Input.equalsIgnoreCase("i"))
&& (step_Id.equalsIgnoreCase("CBS Maker") || step_Id.equalsIgnoreCase("CBS Maker 1"))) {
validationDetails.addWarning(ValidationDetails.WarningType.Other,
      "Disposal instruction exist  [CM]");
}
} catch (Exception e) {
// Loggers.general().info(LOG,e.getMessage());
}

// Mixed tenor date calculation

String expdate = getDriverWrapper().getEventFieldAsText("EXP", "d", ""); // 12/10/16
try {

//// Loggers.general().info(LOG,"Expiry date" + expdate);
String graceda = "0";
graceda = getWrapper().getMIXTEN();

if (((!graceda.equalsIgnoreCase("")) || graceda != null) && graceda.length() > 0) {

SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yy");
Calendar c = Calendar.getInstance();
int grace = 0;

if (graceda.length() > 0 && getMajorCode().equalsIgnoreCase("ILC")) {
grace = Integer.parseInt(graceda);
c.setTime(sdf1.parse(expdate));
// //Loggers.general().info(LOG,"expdate in issue-------> ");
c.add(Calendar.DATE, grace);
// //Loggers.general().info(LOG,"DATE 1"+ c);
String output = sdf1.format(c.getTime());
// Loggers.general().info(LOG,output);
getPane().setMIXDATE(output);
}

} else {
//// Loggers.general().info(LOG,"Mixed tenor period i empty" +
//// graceda);
}
} catch (Exception ee) {

if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Mixed tenor period" + ee.getMessage());
}
}

// Nostro Outstanding Validation Amount
// try {
// String Nostro_Credit = "";
// String MT940_ref = "";
// double NostroCredit = 0.0;
// if (checkNostroBalance() && (step_Input.equalsIgnoreCase("i"))
// && (step_Id.equalsIgnoreCase("CBS Maker"))) {
// Nostro_Credit = getDriverWrapper().getEventFieldAsText("cANE",
// "v", "m");
// if (!Nostro_Credit.equalsIgnoreCase("")) {
// NostroCredit = Double.valueOf(Nostro_Credit);
// }
// MT940_ref = getWrapper().getNOSTRM().trim();
// /*
// * validationDetails.addError(ErrorType.Other,
// * "Nostro Utilization Amount is more than Nostro Outstanding
// Amount("
// * + getOutStandingAmount(MT940_ref, NostroCredit) + ")");
// */
// // validationDetails.addWarning(WarningType.Other, "Nostro
// // Utilization Amount is more than Nostro Outstanding
// // Amount(" +
// // getOutStandingAmount(MT940_ref, NostroCredit) + ")
// // [CM]");
// }
// } catch (Exception e) {
// //Loggers.general().info(LOG,"Exception in Nostro Outstanding Amount
// VAlidation" + e.getMessage());
// }

// String prd_typ = getDriverWrapper().getEventFieldAsText("PTP",
// "s",
// "").trim();

try {
getrbiPurcodeCode();

} catch (Exception e) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Exception in Purpose code---->" + e.getMessage());

}
}

if (getMinorCode().equalsIgnoreCase("DOP") || getMinorCode().equalsIgnoreCase("POD")) {
try {
getrtgsNEFT();

} catch (Exception e) {
// Loggers.general().info(LOG,"Exception in purpose
// code--------->"
// + e.getMessage());
}
}
try {
String rbipur = getWrapper().getOPURPOS_Name().trim();
if (rbipur.equalsIgnoreCase("") || rbipur.length() > 0) {
String str = rbipur.substring(0, 1);
if (((!str.equalsIgnoreCase("S")) || str.equalsIgnoreCase("P"))
      && (step_Input.equalsIgnoreCase("i")) && (step_Id.equalsIgnoreCase("CBS Maker"))
      && (getMajorCode().equalsIgnoreCase("ILC") || (getMajorCode().equalsIgnoreCase("IDC")
                  && getMinorCode().equalsIgnoreCase("CLP")))) {
validationDetails.addError(ErrorType.Other, "RBI purpose code should start with S  [CM]");

} else {
// Loggers.general().info(LOG,"Purpose code is P----> " + str);
}
}
} catch (Exception e) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Exception in Purpose code start with S---->" + e.getMessage());

}
}

try {
if (getMajorCode().equalsIgnoreCase("IDC") && !getMinorCode().equalsIgnoreCase("CLP")) {
String systemDate1 = getDriverWrapper().getEventFieldAsText("RVD", "d", "");
// //Loggers.general().info(LOG,"Received date --->" + systemDate);
String accpt = getDriverWrapper().getEventFieldAsText("RELS", "s", "");
// //Loggers.general().info(LOG,"ACCEPTANCE VALUE --->" + accpt);
int gra = 0;
if (accpt.equalsIgnoreCase("Payment")) {
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
Calendar cal = Calendar.getInstance();

// getPane().setNATTRPRD("10");
String natt_set = getWrapper().getNATTRPRD().trim();

if (natt_set.length() > 0) {
      gra = Integer.parseInt(natt_set);
}
try {
      cal.setTime(sdf.parse(systemDate1));
      // //Loggers.general().info(LOG,"expdate in issue------->
      // ");
      cal.add(Calendar.DATE, gra);
      String output = sdf.format(cal.getTime());
      //// Loggers.general().info(LOG,"output----->" + output);
      getPane().setSIGVALDT(output);
      getWrapper().setSIGVALDT(output);

} catch (Exception e) {
      // Loggers.general().info(LOG,"Notional value date
      // payment--->"
      // + e.getMessage());
}
}

else {
getPane().setSIGVALDT("");

}
} else {
//// Loggers.general().info(LOG,"majaor code is ODC --->" +
//// getMajorCode());

}
} catch (Exception e) {
Loggers.general().info(LOG, "Notional value date payment IDC--->" + e.getMessage());
}

// Sight value date calculation
if (getMajorCode().equalsIgnoreCase("ODC") && !getMinorCode().equalsIgnoreCase("CLP")) {
try {
// //Loggers.general().info(LOG,"Notional due date is calling");
getNotionalDueDate();
} catch (Exception ee) {
//// Loggers.general().info(LOG,"Notional due date is calling--->" +
//// ee.getMessage());
// //Loggers.general().info(LOG,"LOB Catch");
}
} else {
//// Loggers.general().info(LOG,"product type is not ODC");
}

if (getMajorCode().equalsIgnoreCase("IDC")) {
try {
// //Loggers.general().info(LOG,"Notional due date is calling");
getNotionalDueDateIDC();
} catch (Exception ee) {
//// Loggers.general().info(LOG,"Notional due date is calling--->" +
//// ee.getMessage());
// //Loggers.general().info(LOG,"LOB Catch");
}
}

// //Loggers.general().info(LOG,"step id check for CSM ----->" + step_csm);

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
// //Loggers.general().info(LOG,"Entered Validate method in CpcoPcoc
// class");
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
// connection.close();
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

//// Loggers.general().info(LOG,"step id check for CSM ----->" + step_csm);

// Connection connectio = null;
// PreparedStatement prepare = null;
// ResultSet resul = null;
// try {
// int noOfRecord = 0;
// connectio = getConnection();
// String customerId = "";
// String amount_am = "";
// String currency = "";
// String masterRef = "";
// customerId = getDriverWrapper().getEventFieldAsText("BEN", "p",
// "no").trim();
// // //Loggers.general().info(LOG,"Customer Id " + customerId);
// amount_am = getDriverWrapper().getEventFieldAsText("AMT", "v",
// "u");
// // //Loggers.general().info(LOG,"Currency is " + amount_am);
// currency = getDriverWrapper().getEventFieldAsText("AMT", "v",
// "c");
// // //Loggers.general().info(LOG,"Currency is " + currency);
// masterRef = getmasRefNo();
// // //Loggers.general().info(LOG,"Master Reference " + masterRef);
// // //Loggers.general().info(LOG,"Entered Validate method in CpcoPcoc
// // class");
// String duplicateMaster = "SELECT COUNT(*) AS COUNT FROM MASTER
// mas,DLYPRCCYCL DCL WHERE PRICUSTMNM='"
// + customerId + "' AND AMOUNT='" + amount_am + "' AND CCY='" +
// currency
// + "' AND
// TO_DATE(mas.CTRCT_DATE,'DD-MM-YY')>=TO_DATE(DCL.PROCDATE,'DD-MM-YY')-5
// AND MAS.MASTER_REF!='"
// + masterRef + "'";
// // //Loggers.general().info(LOG,"Duplicate Record Query is " +
// // duplicateMaster);
// prepare = connectio.prepareStatement(duplicateMaster);
// resul = prepare.executeQuery();
// if (resul.next()) {
// noOfRecord = resul.getInt("COUNT");
// }
// // //Loggers.general().info(LOG,"No of Existing records is " +
// // noOfRecord);
// if (noOfRecord > 0 && (step_Input.equalsIgnoreCase("i"))
// && (step_csm.equalsIgnoreCase("CBS Maker") ||
// step_Id.equalsIgnoreCase("CBS Maker 1"))
// && (getMajorCode().equalsIgnoreCase("ELC") &&
// getMinorCode().equalsIgnoreCase("ADE"))) {
// validationDetails.addWarning(WarningType.Other,
// "There is an existing transaction for the Same Customer,Amount
// and Currency");
// }
// // connectio.close();
// } catch (Exception e) {
// Loggers.general().info(LOG,"Exception in Duplicate master " +
// e.getMessage());
// } finally {
// try {
// if (connectio != null) {
// connectio.close();
// if (prepare != null)
// prepare.close();
// if (resul != null)
// resul.close();
// }
// } catch (SQLException e) {
// //// Loggers.general().info(LOG,"Connection Failed! Check output
// //// console");
// e.printStackTrace();
// }
// }

try {
List<ExtEventLoanDetails> preship = (List<ExtEventLoanDetails>) getWrapper().getExtEventLoanDetails();
String currency = "";
String amt = "";
boolean usdCheck = false;
boolean inrCheck = false;
boolean eurCheck = false;
boolean gbpcheck = false;
boolean jpyCheck = false;
// //Loggers.general().info(LOG,"Preshipment value checking====> 1" +
// inrCheck);

for (int l = 0; l < preship.size(); l++) {
ExtEventLoanDetails preshipment = preship.get(l);
if (preshipment.getREAMOUNTCurrency().equalsIgnoreCase("INR")) {
inrCheck = true;
}
if (preshipment.getREAMOUNTCurrency().equalsIgnoreCase("USD")) {
usdCheck = true;
}
if (preshipment.getREAMOUNTCurrency().equalsIgnoreCase("EUR")) {
eurCheck = true;
}
if (preshipment.getREAMOUNTCurrency().equalsIgnoreCase("GBP")) {
gbpcheck = true;
}
if (preshipment.getREAMOUNTCurrency().equalsIgnoreCase("JPY")) {
jpyCheck = true;
}

}
// //Loggers.general().info(LOG,"Preshipment value checking====> 2" +
// inrCheck);
if (!inrCheck) {
getPane().setTOTINR("0.0 INR");
}
// //Loggers.general().info(LOG,"Preshipment value checking====> 3" +
// inrCheck);
if (!usdCheck) {
getPane().setTOTUSD("0.0 USD");
}
if (!eurCheck) {
getPane().setTOTEUR("0.0 EUR");
}
if (!gbpcheck) {
getPane().setTOTGBP("0.0 GBP");
}
if (!jpyCheck) {
getPane().setTOTJPY("0.0 JPY");
}

} catch (Exception e) {
// Loggers.general().info(LOG,"Exception in Prsehipment Code" +
// e.getMessage());
}

// try {
//
// String payAmt =
// getDriverWrapper().getEventFieldAsText("FPD:sCOA", "v", "m");
// double payAmount = Double.valueOf(payAmt);
// if (dailyval_Log.equalsIgnoreCase("YES")) {
//
// Loggers.general().info(LOG,"Payment amount in ICF" + payAmount);
// }
// if (payAmount <= 1 && subproductCode.equalsIgnoreCase("ICF") &&
// (step_csm.equalsIgnoreCase("CBS Maker")
// || step_csm.equalsIgnoreCase("CBS Maker 1") ||
// step_csm.equalsIgnoreCase("CBS Reject"))) {
// validationDetails.addError(ErrorType.Other, "Payment Amount
// should be greater than 1 [CM]");
// } else {
// Loggers.general().info(LOG,"Payment amount in else ICF" + payAmount);
// }
//
// } catch (Exception e) {
// if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Exception Payment validation" +
// e.getMessage());
// }
// }

// String buycrd =getWrapper().getBUYREQ()
if (getMajorCode().equalsIgnoreCase("ILC") && getMinorCode().equalsIgnoreCase("POC")
|| getMinorCode().equalsIgnoreCase("CRC"))

{

String tenor = getDriverWrapper().getEventFieldAsText("PTN", "s", "");
String period = getDriverWrapper().getEventFieldAsText("FPP:XPTD", "s", "");
if (!tenor.equalsIgnoreCase(period)) {
validationDetails.addWarning(WarningType.Other, "Tenor of LC has changed for the event");
}
}

// NOSTRO VALIDATION
if (getMajorCode().equalsIgnoreCase("ILC") && getMinorCode().equalsIgnoreCase("POC"))

{

//String tenor = getDriverWrapper().getEventFieldAsText("PTN", "s", "");
//String period = getDriverWrapper().getEventFieldAsText("FPP:XPTD", "s", "");
//if (!tenor.equalsIgnoreCase(period)) {
//validationDetails.addWarning(WarningType.Other,"Usance period has been changed for the event");
//}
/*
* String buycrd = getDriverWrapper().getEventFieldAsText("cBHW", "l", ""); try
* {
*
* con = getConnection(); String query =
* "SELECT TRIM(pos.ACC_TYPE) FROM POSTING_BASE_VIEW pos, BASEEVENT bev, master mas, EXEMPL30 exe WHERE pos.DR_CR_FLG ='D' AND pos.EVENT_KEY = bev.KEY97 AND bev.MASTER_KEY = mas.KEY97 AND mas.EXEMPLAR = exe.KEY97 AND exe.CODE79 IN ('CPCO','CPBO','IDC','ILC') AND pos.ORIGAMTCCY NOT IN 'INR' AND pos.KEY97 IN (SELECT MAX(pos.KEY97) FROM POSTING_BASE_VIEW pos, BASEEVENT bev, master mas, EXEMPL30 exe WHERE pos.DR_CR_FLG ='D' AND pos.EVENT_KEY = bev.KEY97 AND bev.MASTER_KEY = mas.KEY97 AND mas.EXEMPLAR = exe.KEY97 AND exe.CODE79 IN ('CPCO','CPBO','IDC','ILC') AND pos.ORIGAMTCCY NOT IN 'INR' AND mas.MASTER_REF = '"
* + MasterReference + "' AND bev.REFNO_PFIX ='" + evnt +
* "' AND bev.REFNO_SERL ='" + evvcount + "' ) AND mas.MASTER_REF = '" +
* MasterReference + "' AND bev.REFNO_PFIX ='" + evnt +
* "' AND bev.REFNO_SERL ='" + evvcount + "'"; if
* (dailyval_Log.equalsIgnoreCase("YES")) {
* Loggers.general().info(LOG,"Query for warning message in NOSTRO VALIDATION "
* + query); } ps1 = con.prepareStatement(query); rs1 = ps1.executeQuery();
* while (rs1.next()) { // //Loggers.general().info(LOG,"Entered while"); String
* cn = rs1.getString(1).trim();
*
* if (cn.equalsIgnoreCase("CN") && (step_csm.equalsIgnoreCase("CBS Maker 1") ||
* step_csm.equalsIgnoreCase("CBS Authoriser")) && buycrd.equalsIgnoreCase("N")
* && (!buycrd.equalsIgnoreCase("Y"))) {
*
* validationDetails.addError(ErrorType.Other,
* "Pay and Receive should not have Nostro account  [CM]"); }
*
* else {
*
* if (dailyval_Log.equalsIgnoreCase("YES")) { Loggers.general().info(
* LOG,"Pay and Receive should not have Nostro account else--->" + cn); } }
*
* }
*
* } catch (Exception e1) { if (dailyval_Log.equalsIgnoreCase("YES")) {
* Loggers.general().info(LOG,"Exception for Nostro Pay and Receive" +
* e1.getMessage()); } }
*
* finally { try { if (rs1 != null) rs1.close(); if (ps1 != null) ps1.close();
* if (con != null) con.close(); } catch (SQLException e) { //
* Loggers.general().info(LOG,"Connection Failed! Check output // console");
* e.printStackTrace(); } }
*/

String buycrd = getDriverWrapper().getEventFieldAsText("cBHW", "l", "");
String nost_debt = getDriverWrapper().getEventFieldAsText("cAPE", "l", "");
try {

con = getConnection();
String query = "SELECT TRIM(pos.ACC_TYPE) FROM POSTING_BASE_VIEW pos, BASEEVENT bev, master mas, EXEMPL30 exe WHERE pos.DR_CR_FLG ='D' AND pos.EVENT_KEY = bev.KEY97 AND bev.MASTER_KEY = mas.KEY97 AND mas.EXEMPLAR = exe.KEY97 AND exe.CODE79 IN ('CPCO','CPBO','IDC','ILC') AND pos.ORIGAMTCCY NOT IN 'INR' AND pos.KEY97 IN (SELECT pos.KEY97 FROM POSTING_BASE_VIEW pos, BASEEVENT bev, master mas, EXEMPL30 exe WHERE pos.DR_CR_FLG ='D' AND pos.EVENT_KEY = bev.KEY97 AND bev.MASTER_KEY = mas.KEY97 AND mas.EXEMPLAR = exe.KEY97 AND exe.CODE79 IN ('CPCO','CPBO','IDC','ILC') AND pos.ORIGAMTCCY NOT IN 'INR' AND mas.MASTER_REF = '"
      + MasterReference + "' AND bev.REFNO_PFIX ='" + evnt + "' AND bev.REFNO_SERL ='" + evvcount
      + "' ) AND mas.MASTER_REF = '" + MasterReference + "' AND bev.REFNO_PFIX ='" + evnt
      + "' AND bev.REFNO_SERL ='" + evvcount
      + "' and trim(pos.NETS_INTO) is null AND trim(pos.REPLACEDBY) is null AND pos.AMOUNT<>0 and pos.ACC_TYPE='CN'";
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Query for warning message in NOSTRO VALIDATION " + query);
}
ps1 = con.prepareStatement(query);
rs1 = ps1.executeQuery();
while (rs1.next()) {
// //Loggers.general().info(LOG,"Entered while");
String cn = rs1.getString(1).trim();

if (cn.equalsIgnoreCase("CN")
            && (step_csm.equalsIgnoreCase("CBS Maker 1") || step_csm.equalsIgnoreCase("CBS Reject")
                        || step_csm.equalsIgnoreCase("CBS Authoriser"))
            && nost_debt.equalsIgnoreCase("N") && (!nost_debt.equalsIgnoreCase("Y"))) {

      validationDetails.addError(ErrorType.Other,
                  "Pay and Receive should not have Nostro account  [CM]");
}

else {

      if (dailyval_Log.equalsIgnoreCase("YES")) {
            Loggers.general().info(LOG,
                        "Pay and Receive should not have Nostro account else--->" + cn);
      }
}

}

} catch (Exception e1) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Exception for Nostro Pay and Receive" + e1.getMessage());
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
// Loggers.general().info(LOG,"Connection Failed! Check output
// console");
e.printStackTrace();
}
}

}
if (getMajorCode().equalsIgnoreCase("IDC") && getMinorCode().equalsIgnoreCase("CLP")) {

/*
* String buycrd = getDriverWrapper().getEventFieldAsText("cBHW", "l", ""); try
* {
*
* con = getConnection(); String query =
* "SELECT TRIM(pos.ACC_TYPE) FROM POSTING_BASE_VIEW pos, BASEEVENT bev, master mas, EXEMPL30 exe WHERE pos.DR_CR_FLG ='D' AND pos.EVENT_KEY = bev.KEY97 AND bev.MASTER_KEY = mas.KEY97 AND mas.EXEMPLAR = exe.KEY97 AND exe.CODE79 IN ('CPCO','CPBO','IDC','ILC') AND pos.ORIGAMTCCY NOT IN 'INR' AND pos.KEY97 IN (SELECT MAX(pos.KEY97) FROM POSTING_BASE_VIEW pos, BASEEVENT bev, master mas, EXEMPL30 exe WHERE pos.DR_CR_FLG ='D' AND pos.EVENT_KEY = bev.KEY97 AND bev.MASTER_KEY = mas.KEY97 AND mas.EXEMPLAR = exe.KEY97 AND exe.CODE79 IN ('CPCO','CPBO','IDC','ILC') AND pos.ORIGAMTCCY NOT IN 'INR' AND mas.MASTER_REF = '"
* + MasterReference + "' AND bev.REFNO_PFIX ='" + evnt +
* "' AND bev.REFNO_SERL ='" + evvcount + "' ) AND mas.MASTER_REF = '" +
* MasterReference + "' AND bev.REFNO_PFIX ='" + evnt +
* "' AND bev.REFNO_SERL ='" + evvcount + "'"; if
* (dailyval_Log.equalsIgnoreCase("YES")) { //Loggers.general().info(
* LOG,"Query for warning message in NOSTRO VALIDATION " + query); } ps1 =
* con.prepareStatement(query); rs1 = ps1.executeQuery(); while (rs1.next()) {
* // //Loggers.general().info(LOG,"Entered while"); String cn =
* rs1.getString(1).trim();
*
* if (cn.equalsIgnoreCase("CN") && (step_csm.equalsIgnoreCase("CBS Maker 1") ||
* step_csm.equalsIgnoreCase("CBS Authoriser")) && buycrd.equalsIgnoreCase("N")
* && (!buycrd.equalsIgnoreCase("Y"))) {
*
* validationDetails.addWarning(WarningType.Other,
* "Pay and Receive should not have Nostro account  [CM]"); }
*
* else {
*
* if (dailyval_Log.equalsIgnoreCase("YES")) { //Loggers.general().info(
* LOG,"Pay and Receive should not have Nostro account else--->" + cn); } }
*
* }
*
* } catch (Exception e1) { if (dailyval_Log.equalsIgnoreCase("YES")) {
* Loggers.general().info(LOG,"Exception for Nostro Pay and Receive" +
* e1.getMessage()); } }
*
* finally { try { if (rs1 != null) rs1.close(); if (ps1 != null) ps1.close();
* if (con != null) con.close(); } catch (SQLException e) { //
* Loggers.general().info(LOG,"Connection Failed! Check output // console");
* e.printStackTrace(); } }
*/

String buycrd = getDriverWrapper().getEventFieldAsText("cBHW", "l", "");
String nost_debt = getDriverWrapper().getEventFieldAsText("cAPE", "l", "");
try {

con = getConnection();
String query = "SELECT TRIM(pos.ACC_TYPE) FROM POSTING_BASE_VIEW pos, BASEEVENT bev, master mas, EXEMPL30 exe WHERE pos.DR_CR_FLG ='D' AND pos.EVENT_KEY = bev.KEY97 AND bev.MASTER_KEY = mas.KEY97 AND mas.EXEMPLAR = exe.KEY97 AND exe.CODE79 IN ('CPCO','CPBO','IDC','ILC') AND pos.ORIGAMTCCY NOT IN 'INR' AND pos.KEY97 IN (SELECT pos.KEY97 FROM POSTING_BASE_VIEW pos, BASEEVENT bev, master mas, EXEMPL30 exe WHERE pos.DR_CR_FLG ='D' AND pos.EVENT_KEY = bev.KEY97 AND bev.MASTER_KEY = mas.KEY97 AND mas.EXEMPLAR = exe.KEY97 AND exe.CODE79 IN ('CPCO','CPBO','IDC','ILC') AND pos.ORIGAMTCCY NOT IN 'INR' AND mas.MASTER_REF = '"
      + MasterReference + "' AND bev.REFNO_PFIX ='" + evnt + "' AND bev.REFNO_SERL ='" + evvcount
      + "' ) AND mas.MASTER_REF = '" + MasterReference + "' AND bev.REFNO_PFIX ='" + evnt
      + "' AND bev.REFNO_SERL ='" + evvcount
      + "' and trim(pos.NETS_INTO) is null AND trim(pos.REPLACEDBY) is null AND pos.AMOUNT<>0 and pos.ACC_TYPE='CN'";
if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Query for warning message in NOSTRO VALIDATION "
// + query);
}
ps1 = con.prepareStatement(query);
rs1 = ps1.executeQuery();
while (rs1.next()) {
// //Loggers.general().info(LOG,"Entered while");
String cn = rs1.getString(1).trim();

if (cn.equalsIgnoreCase("CN")
            && (step_csm.equalsIgnoreCase("CBS Maker 1") || step_csm.equalsIgnoreCase("CBS Reject")
                        || step_csm.equalsIgnoreCase("CBS Authoriser"))
            && nost_debt.equalsIgnoreCase("N") && (!nost_debt.equalsIgnoreCase("Y"))) {

      validationDetails.addError(ErrorType.Other,
                  "Pay and Receive should not have Nostro account  [CM]");
}

else {

      if (dailyval_Log.equalsIgnoreCase("YES")) {
            // Loggers.general().info(LOG,"Pay and Receive should not have Nostro account
            // else--->" + cn);
      }
}

}

} catch (Exception e1) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Exception for Nostro Pay and Receive" + e1.getMessage());
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
// Loggers.general().info(LOG,"Connection Failed! Check output
// console");
e.printStackTrace();
}
}

}
// ---------------------------------------------------------------------------
String prd_code = getDriverWrapper().getEventFieldAsText("PCO", "s", "").trim();
try {
/*
* String query =
* "SELECT REF.STEP_ID, COUNT(*) FROM MASTER MAS, BASEEVENT BEV, ETT_REFERRAL_TRACKING REF WHERE MAS.KEY97 = BEV.MASTER_KEY AND trim(MAS.MASTER_REF) =TRIM(REF.MASTER_REF_NO)  AND BEV.STATUS <>'a' AND TRIM(REF.MASTER_REF_NO) ='"
* + MasterReference + "' AND REF.EVENT_REF_NO = '" + eventCode +
* "' AND REF.SUB_PRODUCT_CODE = '" + subproductCode +
* "' AND (REF.REFERRAL_STATUS ='REJ' OR REF.REFERRAL_STATUS ='PEND') GROUP BY REF.STEP_ID"
* ;
*/
String query = "SELECT REF.STEP_ID, COUNT(*) FROM MASTER MAS,ETT_REFERRAL_TRACKING REF WHERE  trim(MAS.MASTER_REF) =TRIM(REF.MASTER_REF_NO)   AND TRIM(REF.MASTER_REF_NO) ='"
+ MasterReference + "' AND REF.EVENT_REF_NO = '" + eventCode + "' AND REF.SUB_PRODUCT_CODE = '"
+ subproductCode
+ "' AND (REF.REFERRAL_STATUS ='REJ' OR REF.REFERRAL_STATUS ='PEND') GROUP BY REF.STEP_ID";

if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Query ETT_REFERRAL_TRACKING count===>" + query);
}
int count = 0;
String step = null;
String concat = null;
con = getConnection();
pst = con.prepareStatement(query);
rs1 = pst.executeQuery();
while (rs1.next()) {

step = rs1.getString(1);

count = rs1.getInt(2);
// Loggers.general().info(LOG,"Entered while referal step" + step+"count" +
// count);
// //Loggers.general().info(LOG,"Entered while referal step" + step
// +
// "
// count" + count);
if (count > 0) {
if ((step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Reject"))
            && getMajorCode().equalsIgnoreCase("ILC")
            && !step_Id.equalsIgnoreCase("CBS Authoriser")) {

      String ref = null;
      String statusReferral = null;
      String warningMessage = null;
      String query6 = "SELECT  REFERRAL,REFERRAL_STATUS,STEP_ID "
                  + " FROM ETT_REFERRAL_TRACKING  WHERE  MASTER_REF_NO ='" + MasterReference + "'"
                  + " AND EVENT_REF_NO = '" + eventCode + "'" + " AND SUB_PRODUCT_CODE = '"
                  + subproductCode + "'" + " and (REFERRAL_STATUS='REJ' OR REFERRAL_STATUS='PEND')";
      if (dailyval_Log.equalsIgnoreCase("YES")) {
            // Loggers.general().info(LOG,"Query ETT_REFERRAL_TRACKING CBS Authoriser
            // 3rd===>" + query6);
      }
      pst = con.prepareStatement(query6);

      rs = pst.executeQuery();
      ArrayList<String> al = new ArrayList<String>();
      while (rs.next()) {

            // Loggers.general().info(LOG,"referral_)status------------>" +rs.getString(2));

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
      // //Loggers.general().info(LOG,"Single Warning Message " +
      // warningMessage);

      validationDetails.addWarning(WarningType.Other, warningMessage);

} else if (step_csm.equalsIgnoreCase("CBS Checker")
            || step_csm.equalsIgnoreCase("CBS Authoriser")) {
      // Loggers.general().info(LOG,"Authoriser");

      String ref = null;
      String statusReferral = null;
      String warningMessage = null;
      String query6 = "SELECT  REFERRAL,REFERRAL_STATUS,STEP_ID "
                  + " FROM ETT_REFERRAL_TRACKING  WHERE  MASTER_REF_NO ='" + MasterReference + "'"
                  + " AND EVENT_REF_NO = '" + eventCode + "'" + " AND SUB_PRODUCT_CODE = '"
                  + subproductCode + "'" + " and (REFERRAL_STATUS='REJ' OR REFERRAL_STATUS='PEND')";
      if (dailyval_Log.equalsIgnoreCase("YES")) {
            // Loggers.general().info(LOG,"Query ETT_REFERRAL_TRACKING CBS Authoriser
            // 3rd===>" + query6);
      }
      pst = con.prepareStatement(query6);

      rs = pst.executeQuery();
      ArrayList<String> al = new ArrayList<String>();
      while (rs.next()) {
            // Loggers.general().info(LOG,"referral_)status------------>" +rs.getString(2));

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
      // //Loggers.general().info(LOG,"Single Warning Message " +
      // warningMessage);

      validationDetails.addError(ErrorType.Other, warningMessage);

} else {
      //// Loggers.general().info(LOG,"referal step in step" +
      //// step_Id);
}
} else {

//// Loggers.general().info(LOG,"referal step in step" +
//// step_Id);
}
}
// //Loggers.general().info(LOG,"Entered while referal count out of
// loop"
// + count);
} catch (Exception e1) {
// Loggers.general().info(LOG,"Exception referal count" +
// e1.getMessage());
} finally {
try {
if (rs != null)
rs.close();
if (rs1 != null)
rs1.close();
if (pst != null)
pst.close();
if (con != null)
con.close();
} catch (SQLException e) {
//// Loggers.general().info(LOG,"Connection Failed! Check output
//// console");
e.printStackTrace();
}
}
// rejection validation
try {
int counter = 0;
String query5 = "SELECT  count(*) FROM ETT_WF_CHKLST_TRACKING  WHERE  MASTER_REF ='" + MasterReference
+ "'" + " AND EVENTREF= '" + eventCode + "'" + " and PROD_CODE='" + prd_code + "'"
+ " AND SUB_PRODUCT_CODE= '" + subproductCode + "'"
+ " and INIT_AT IN ('CSM','CBS Maker') and MANDATORY='REJ'  and CHECKED_YN ='Y'";
// Loggers.general().info(LOG,"ArrayList Warning Message rejection 1st"
// + query5);
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
// Loggers.general().info(LOG,"ArrayList Warning Message rejection
// 2nd" + query6);
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
//// Loggers.general().info(LOG,"Connection Failed! Check output
//// console");
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
// Loggers.general().info(LOG,"Pending referal status count if loop");
}

if (step_Input.equalsIgnoreCase("i") && (step_csm.equalsIgnoreCase("CSM")
      || step_csm.equalsIgnoreCase("AdhocCSM") || step_csm.equalsIgnoreCase("CBS Maker"))) {
validationDetails.addWarning(WarningType.Other, "Past pending deferral for same client [CM]");
}

} else {
if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"No pending referal status" + query5);
}

}

} catch (Exception e) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "ExceptionPending referal status" + e.getMessage());
}
}

// BOE mandatory for DIR
if ((subproductCode.equalsIgnoreCase("ILF") || subproductCode.equalsIgnoreCase("ICF"))
&& (getMinorCode().equalsIgnoreCase("POC") || getMinorCode().equalsIgnoreCase("CLP"))) {
try {
con = getConnection();
String query = "SELECT COUNT(*) AS BOE_COUNT FROM ETT_BOE_PAYMENT  WHERE BOE_PAYMENT_BP_PAY_REF  ='"
      + MasterReference + "' AND BOE_PAYMENT_BP_PAY_PART_REF ='" + eventCode
      + "' AND STATUS = 'P'";
// Loggers.general().info(LOG,"Query for ETT_BOE_PAYMENT " + query);
int count5 = 0;
ps1 = con.prepareStatement(query);
rs1 = ps1.executeQuery();
while (rs1.next()) {
count5 = rs1.getInt(1);
// Loggers.general().info(LOG,"value of count in while
// authorization" + count5);
if (count5 > 0 && (step_csm.equalsIgnoreCase("CBS Authoriser")
            || step_csm.equalsIgnoreCase("Authorise"))) {

      validationDetails.addError(ErrorType.Other,
                  "BOE Authorization is pending for this Entity [CM]");
}

else {
      // Loggers.general().info(LOG,"BOE link in else" + count5);
}

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
} else {
// Loggers.general().info(LOG,"BOE link not in DIR====>" + productyp);
}
try {

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

if (counter_val > 0 && (step_Input.equalsIgnoreCase("i"))
      && (step_csm.equalsIgnoreCase("CBS Maker"))) {
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

} catch (Exception e) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Exception pecial characters checking-----> " + e.getMessage());
}
}

// Notes Populated in Summary

try {
con = getConnection();
String query = "select * from master mas,NOTE, TIDATAITEM tid WHERE mas.KEY97 = tid.MASTER_KEY AND tid.KEY97 = NOTE.KEY97 AND (NOTE.TYPE NOT IN (3,129,1089) or NOTE.TYPE is null) AND note_event IS NOT NULL AND NOTE.ACTIVE = 'Y' AND mas.MASTER_REF = '"
+ MasterReference + "' ";
//// Loggers.general().info(LOG,"Notes Populated in Summary query====> "
//// + query);

ps1 = con.prepareStatement(query);
rs1 = ps1.executeQuery();
while (rs1.next()) {
String notes = rs1.getString(1);
if (notes.length() > 0 && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CSM")
      || step_csm.equalsIgnoreCase("CBS Maker") || step_Id.equalsIgnoreCase("CBS Maker 1"))) {
validationDetails.addWarning(WarningType.Other,
            "Notes Populated in Summary. Kindly check [CM]");
}

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
//// Loggers.general().info(LOG,"Connection Failed! Check output
//// console");
e.printStackTrace();
}
}

String rtgs = getWrapper().getPROREMT();
String rtgspart = getWrapper().getRTGSPART();

// //Loggers.general().info(LOG,"RTGS for initially-------->" + rtgs);

// unbalanced posting error
//try {
//if ((step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm.equalsIgnoreCase("Authorise"))
//&& (rtgs.equalsIgnoreCase("RTG") || rtgs.equalsIgnoreCase("NEF"))
//&& rtgspart.equalsIgnoreCase("FULL") && (prd_typ.equalsIgnoreCase("ICF"))) {
//// Loggers.general().info(LOG,"try Unbalanced posting valid");
//try {
//con = getConnection();
//String query = "SELECT * FROM KMB_RTGS_NEFT_ACC_VALID_VIEW WHERE MASTER_REF = '"
//          + MasterReference + "' AND REFNO_PFIX = '" + evnt + "' AND REFNO_SERL = '" + evvcount
//          + "'";
//// Loggers.general().info(LOG,"Unbalanced posting error" +
//// query);
//// int count = 0;
//ps1 = con.prepareStatement(query);
//rs1 = ps1.executeQuery();
//if (rs1.next()) {
//    // Loggers.general().info(LOG,"Unbalanced posting valid in
//    // if loop");
//} else if ((step_csm.equalsIgnoreCase("CBS Authoriser")
//          || step_csm.equalsIgnoreCase("Authorise"))
//          && (rtgs.equalsIgnoreCase("RTG") || rtgs.equalsIgnoreCase("NEF"))) {
//
//    validationDetails.addError(ErrorType.Other, "Please enter RTGS account number [CM]");
//} else {
//    // Loggers.general().info(LOG,"Unbalanced posting valid in
//    // else loop");
//}
//} catch (Exception e1) {
//// Loggers.general().info(LOG,"Exception Unbalanced posting
//// error" + e1.getMessage());
//}
//
//finally {
//try {
//    if (rs1 != null)
//          rs1.close();
//    if (ps1 != null)
//          ps1.close();
//    if (con != null)
//          con.close();
//} catch (SQLException e) {
//    // Loggers.general().info(LOG,"Connection Failed! Check
//    // output console");
//    e.printStackTrace();
//}
//}
//} else {
//// Loggers.general().info(LOG,"Please enter RTGS account in else");
//
//}
//} catch (Exception e1) {
// Loggers.general().info(LOG,"Exception Unbalanced posting===>" +
// e1.getMessage());
//}

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
      // Loggers.general().info(LOG,"NEFT value Blank-------->" + NEFTTime);
}

}
if (NEFTTime.equalsIgnoreCase("NO") && (rtgs.equalsIgnoreCase("NEF"))
      && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Authoriser")
                  || step_csm.equalsIgnoreCase("Authorise"))) {
validationDetails.addError(ErrorType.Other,
            "Today is holiday. No NEFT entry will proceed [CM]");
} else {
if (dailyval_Log.equalsIgnoreCase("YES")) {
      // Loggers.general().info(LOG,"Today is not holyday so NEFT will avilable");
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
      // Loggers.general().info(LOG,"RTGS value Blank-------->" + RTGSTime);
}

}
if (RTGSTime.equalsIgnoreCase("NO") && (rtgs.equalsIgnoreCase("RTG"))
      && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Authoriser")
                  || step_csm.equalsIgnoreCase("Authorise"))) {
validationDetails.addError(ErrorType.Other,
            "Today is holiday. No RTGS entry will proceed [CM]");
} else {
if (dailyval_Log.equalsIgnoreCase("YES")) {
      // Loggers.general().info(LOG,"Today is not holyday so RTGS will avilable");
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
//// Loggers.general().info(LOG,"RTGS time for cut off is
//// empty-------->");

}
// //Loggers.general().info(LOG,"RTGS time for cut off from static
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
//// Loggers.general().info(LOG,"RTGS cut off TIME is exceeded");
validationDetails.addError(ErrorType.Other, "RTGS cut-off time has been exceeded [CM]");
} else {
//// Loggers.general().info(LOG,"RTGS cut off TIME is available");
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
//// Loggers.general().info(LOG,"NEFTTime time for cut off is
//// empty-------->");

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
//// Loggers.general().info(LOG,"NEFT cut off TIME is avilable");
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
//// Loggers.general().info(LOG,"Bank to Bank Payment time for cut
//// off is empty-------->");

}
// //Loggers.general().info(LOG,"Bank to Bank Payment time for cut off
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
//// Loggers.general().info(LOG,"Bank to Bank Payment cut off TIME
//// is available");
}
} catch (Exception e) {
// Loggers.general().info(LOG,"Bank to Bank Payment cut off TIME
// Exception" + e.getMessage());
}

// Over due bill exists for this customer
if (step_csm.equalsIgnoreCase("CBS Maker")) {
try {
con = getConnection();
String query = "select * from ETT_OVERDUE_BILCUS_EOD where CUSTOMER_ID= '" + cust + "'";

//// Loggers.general().info(LOG,"Over due bill exists for this
//// customer " + query);
// int count = 0;
ps1 = con.prepareStatement(query);
rs1 = ps1.executeQuery();
while (rs1.next()) {
// //Loggers.general().info(LOG,"Entered while Over due bill
// exists
// for this customer");

if (step_Input.equalsIgnoreCase("i") && (step_csm.equalsIgnoreCase("CBS Maker")
            || step_Id.equalsIgnoreCase("CBS Maker 1"))) {
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
//// Loggers.general().info(LOG,"Connection Failed! Check output
//// console");
e.printStackTrace();
}
}
} else {
//// Loggers.general().info(LOG,"Step id not CMS Maker");
}

// Payment amount exceeds available amount

try {

String avalibaleAmt = getDriverWrapper().getEventFieldAsText("FOA", "v", "m");
String presentAmt = getDriverWrapper().getEventFieldAsText("AMPR", "v", "m");
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
Loggers.general().info(LOG, "Available amount of ELC" + avalibaleAmt);
Loggers.general().info(LOG, "Presented amount of ELC" + presentAmount);
}

if ((avalibaleAmount.compareTo(presentAmount) < 0) && (step_Input.equalsIgnoreCase("i"))
&& (step_csm.equalsIgnoreCase("CBS Maker")) && getMinorCode().equalsIgnoreCase("DOP")) {
validationDetails.addError(ErrorType.Other,
      "Presented amount is should not greater than the Available amount  [CM]");

} else {

if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,
            "Available amount is less" + avalibaleAmt + "presented amount===>" + presentAmount);
}
}

} catch (Exception e) {

if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,
      "Exception Available amount is less presented amount===>" + e.getMessage());
}

}

// crystallization validation
if (prd_typ.equalsIgnoreCase("ELF")) {
try {
String paymentOption = getDriverWrapper().getEventFieldAsText("FPP:XPSD", "s", "");
con = getConnection();
String sql = "SELECT mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL, mas1.MASTER_REF, bev1.REFNO_PFIX, bev1.REFNO_SERL, exte.REPCRY FROM master mas, BASEEVENT bev, BASEEVENT bev1, master mas1, EXTEVENT exte WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = bev1.ATTACHD_EV AND bev1.MASTER_KEY = mas1.KEY97 and bev1.KEY97 = exte.EVENT and bev1.REFNO_PFIX = 'RFS' AND mas.MASTER_REF = '"
      + MasterReference + "' and bev.REFNO_PFIX = '" + evnt + "' and bev.REFNO_SERL =" + evvcount
      + "";
// Loggers.general().info(LOG,"Query value for REPCRY--->" + sql);
ps = con.prepareStatement(sql);
rs = ps.executeQuery();
while (rs.next()) {
String repayCheck = rs.getString(7).trim();
// Loggers.general().info(LOG,"Repayment value===>" + repayCheck
// + "paymentOption====>" + paymentOption);
if (!paymentOption.equalsIgnoreCase("CRY") && repayCheck.equalsIgnoreCase("Y")
            && (step_csm.equalsIgnoreCase("CBS Maker 1"))) {
      validationDetails.addError(ErrorType.Other,
                  "Repayment check box is ticked in Finance,Please select payment option as Crystallisation [CM]");
} else {
      // Loggers.general().info(LOG,
      // "Repayment value else===>" + repayCheck +
      // "paymentOption====>" + paymentOption);
}

}

if (paymentOption.equalsIgnoreCase("CRY")) {

String sql1 = "SELECT doc.PAYSTSCODE, exte.REPCRY, mas.MASTER_REF, mas1.MASTER_REF, bev1.REFNO_PFIX, bev1.REFNO_SERL FROM master mas, BASEEVENT bev, BASEEVENT bev1, master mas1, extevent exte, DOCSPRE doc WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = bev1.ATTACHD_EV AND bev1.MASTER_KEY = mas1.KEY97 AND bev1.KEY97 = exte.EVENT AND bev.KEY97 = doc.KEY97 AND mas.MASTER_REF = '"
            + MasterReference + "' AND bev.REFNO_PFIX ='" + evnt + "' AND bev.REFNO_SERL ="
            + evvcount + "";
// Loggers.general().info(LOG,"Query value for
// paymentOption--->" + sql1);
con = getConnection();
ps = con.prepareStatement(sql1);
rs = ps.executeQuery();
while (rs.next()) {
      String repayCheck = rs.getString(2).trim();
      // Loggers.general().info(LOG,"Repayment value===>" +
      // repayCheck + "paymentOption====>" +
      // paymentOption);
      if (paymentOption.equalsIgnoreCase("CRY") && !repayCheck.equalsIgnoreCase("Y")
                  && (step_csm.equalsIgnoreCase("CBS Maker 1"))) {
            validationDetails.addError(ErrorType.Other,
                        "Payment option selected as Crystallisation please tick Repayment check box in Finance event [CM]");
      } else {
            // Loggers.general().info(LOG,"Repayment value else===>"
            // + repayCheck + "paymentOption====>" +
            // paymentOption);
      }

}
}

} catch (Exception e) {
//// Loggers.general().info(LOG,"Exception update" +
//// e.getMessage());
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
String nosAmt = getDriverWrapper().getEventFieldAsText("cABI", "v", "m");
if (!nosAmt.equalsIgnoreCase("") && nosAmt.length() > 0) {
nosrtoAmt = Double.parseDouble(nosAmt);
nosTotal = short_total + nosrtoAmt;
}
nosrtoAmount = new BigDecimal(nosAmt);

if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG," Nostro utilizaton amount in explc" +
// nosrtoAmount);
}
} catch (Exception e) {
nosrtoAmount = new BigDecimal("0");
}

try {
String credAmt = getDriverWrapper().getEventFieldAsText("cANE", "v", "m");
if (!credAmt.equalsIgnoreCase("") && credAmt.length() > 0) {
creditAmt = Double.parseDouble(credAmt);

}
creditAmount = new BigDecimal(credAmt);
if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG," Nostro credit amount in explc" + creditAmount);
}
} catch (Exception e) {
creditAmount = new BigDecimal("0");
}

try {
String credAmt940 = getDriverWrapper().getEventFieldAsText("cABN", "v", "m");
if (!credAmt940.equalsIgnoreCase("") && credAmt940.length() > 0) {
creditAmt940 = Double.parseDouble(credAmt940);

}

creditAmount940 = new BigDecimal(credAmt940);
if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG," Nostro credit 940 amount in explc" +
// creditAmount940);
}
} catch (Exception e) {
creditAmount940 = new BigDecimal("0");
}

try {
String nosOutAmt = getDriverWrapper().getEventFieldAsText("cBJL", "v", "m");
if (!nosOutAmt.equalsIgnoreCase("") && nosOutAmt.length() > 0) {
nosrtoOutAmt = Double.parseDouble(nosOutAmt);
}
nosrtoOutAmount = new BigDecimal(nosOutAmt);

if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG," Nostro Outstanding amount in inward remittance"
// + nosrtoOutAmount);
}
} catch (Exception e) {
nosrtoOutAmount = new BigDecimal("0");
}

if (nosrtoAmt > 0 && nosrtoOutAmt > 0 && (nosrtoOutAmount.compareTo(nosrtoAmount) < 0)
&& getMinorCode().equalsIgnoreCase("POD")
&& (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1")
            || step_csm.equalsIgnoreCase("CBS Authoriser"))) {
validationDetails.addError(ErrorType.Other,
      "Notsro utilization amount is greater than nostro Outstanding amount [CM]");
} else {
if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG," Notsro Outstanding amount is greater" +
// nosrtoOutAmount);
}
}

String advrec = getWrapper().getPERADV();

if (nosrtoAmt > 0) {

if (advrec.equalsIgnoreCase("") && nosrtoAmt > 0 && (amountdob != nosTotal)
      && (step_Input.equalsIgnoreCase("i"))
      && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id.equalsIgnoreCase("CBS Maker 1"))
      && getMinorCode().equalsIgnoreCase("POD") && !formtyp.equalsIgnoreCase("EXEMPTED")
      && !mixedpay.equalsIgnoreCase("Y") && (payaction.equalsIgnoreCase("Pay"))) {
validationDetails.addWarning(WarningType.Other,
            "Sum of short collection amount(Other bank charges) + Nostro amount should be equal to payment amount (Payment details) [CM]");
}

else {

}

if (mixedpay.equalsIgnoreCase("Y") && (payaction.equalsIgnoreCase("Pay"))) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
      /*
       * Loggers.general().info(
       * LOG,"Sum of short collection amount(Other bank charges) + Nostro amount===>"
       * + nosTotal + "Mixed paymen===>" + mixpay_tot);
       */
}
if (advrec.equalsIgnoreCase("") && nosrtoAmt > 0 && (nosTotal != mixpay_tot)
            && (step_Input.equalsIgnoreCase("i")) && (step_Id.equalsIgnoreCase("CBS Maker 1"))
            && getMinorCode().equalsIgnoreCase("POD") && !formtyp.equalsIgnoreCase("EXEMPTED")) {
      validationDetails.addWarning(WarningType.Other,
                  "Sum of short collection amount(Other bank charges) + Nostro amount should be equal to payment amount (Payment details) for Mixed payment [CM]");
} else {
      if (dailyval_Log.equalsIgnoreCase("YES")) {
            /*
             * Loggers.general().info(LOG,
             * "Sum of short collection amount(Other bank charges) + Nostro amount else loop for Mixed payment---->>>"
             * + nosTotal + " " + mixpay_tot);
             */
      }
}
} else {
//// Loggers.general().info(LOG,"Sum of Equivalent
//// Bill Amount and short collection amount
//// should be equal====>>>");
}

}

if (nostref_MT103102.length() > 0 && getMinorCode().equalsIgnoreCase("POD")) {
if (nosrtoAmt > 0 && creditAmt > 0 && (creditAmount.compareTo(nosrtoAmount) < 0)
      && (step_Input.equalsIgnoreCase("i"))
      && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1"))
      && getMinorCode().equalsIgnoreCase("POD")) {
validationDetails.addError(ErrorType.Other,
            "Notsro utilization amount is greater than nostro credit amount [CM]");
}

else if (nosrtoAmt > 0 && creditAmt > 0 && (creditAmount.compareTo(nosrtoAmount) > 0)
      && (step_Input.equalsIgnoreCase("i"))
      && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1"))
      && getMinorCode().equalsIgnoreCase("POD")) {
validationDetails.addWarning(WarningType.Other,
            "Notsro utilization amount is less than nostro credit amount [CM]");
} else {
if (dailyval_Log.equalsIgnoreCase("YES")) {
      /*
       * Loggers.general().info(LOG, "Nostro creditAmt amount else" + creditAmount +
       * "nosrtoAmt===>" + nosrtoAmount);
       */
}
}
} else if (nostref_MT940.length() > 0 && getMinorCode().equalsIgnoreCase("POD")) {
if (nosrtoAmt > 0 && creditAmt940 > 0 && (creditAmount940.compareTo(nosrtoAmount) < 0)
      && (step_Input.equalsIgnoreCase("i"))
      && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1"))
      && getMinorCode().equalsIgnoreCase("POD")) {
validationDetails.addError(ErrorType.Other,
            "Notsro utilization amount is greater than nostro credit amount [CM]");
}

else if (nosrtoAmt > 0 && creditAmt940 > 0 && (creditAmount940.compareTo(nosrtoAmount) > 0)
      && (step_Input.equalsIgnoreCase("i"))
      && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1"))
      && getMinorCode().equalsIgnoreCase("POD")) {
validationDetails.addWarning(WarningType.Other,
            "Notsro utilization amount is less than nostro credit amount [CM]");
} else {
if (dailyval_Log.equalsIgnoreCase("YES")) {
      /*
       * Loggers.general().info(LOG, "Nostro creditAmt amount else" + creditAmount940
       * + "nosrtoAmt===>" + nosrtoAmount);
       */
}
}
}

} catch (Exception ex) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG,
      "Exception short collection amount(Other bank charger) + Nostro amount" + ex.getMessage());
}

}

try {

if (getMinorCode().equalsIgnoreCase("POD") && (gateWayVal.equalsIgnoreCase("Y"))) {
String orgRefNumber = getDriverWrapper().getEventFieldAsText("CLM", "r", "no");
String billRefNumber = getDriverWrapper().getEventFieldAsText("cANV", "s", "");
if (orgRefNumber != null && billRefNumber != null) {
if (orgRefNumber != "" && billRefNumber != "") {
      String firstChar = (orgRefNumber.trim()).substring(0, 1);
      if (dailyval_Log.equalsIgnoreCase("YES")) {
            // Loggers.general().info(LOG,"org ref Laste 1 char" + firstChar);
      }
      String lastChar = (orgRefNumber.trim()).substring(orgRefNumber.length() - 3);
      if (dailyval_Log.equalsIgnoreCase("YES")) {
            // Loggers.general().info(LOG,"org ref Laste 3 char" + lastChar);
      }
      String OrgRefNumberCon = firstChar + lastChar;
      if (dailyval_Log.equalsIgnoreCase("YES")) {
            // Loggers.general().info(LOG,"org concord value" + OrgRefNumberCon);
      }
      String billReflast = (billRefNumber.trim()).substring(billRefNumber.length() - 4);
      if (dailyval_Log.equalsIgnoreCase("YES")) {
            // Loggers.general().info(LOG,"Bill Ref Laste 4 char" + billReflast);
      }
      if ((!OrgRefNumberCon.equalsIgnoreCase(billReflast))
                  && (gateWayVal.equalsIgnoreCase("Y") || !gateWayVal.equalsIgnoreCase("N"))
                  && (step_csm.equalsIgnoreCase("CBS Maker")
                              || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
            // Loggers.general().info(LOG,"MasterReference--" + MasterReference);
            if (!MasterReference.equalsIgnoreCase(billRefNumber)) {
                  validationDetails.addError(ErrorType.Other,
                              "Bill reference number doesn't match [CM]");
            }
            // validationDetails.addError(ErrorType.Other, "Bill reference number doesn't
            // match [CM]");
      } else {
            if (dailyval_Log.equalsIgnoreCase("YES")) {
                  // Loggers.general().info(LOG,"Else Bill reference number is match");
            }
      }
}
}
}

} catch (Exception e) {

if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Exception Bill reference number doesn't match");
}

}
/*
* try { String billRefNumber = getDriverWrapper().getEventFieldAsText("cANV",
* "s", ""); String checkmig = getDriverWrapper().getEventFieldAsText("ECMC",
* "s", ""); Loggers.general().info(LOG,"billRefNumber" + billRefNumber);
* Loggers.general().info(LOG,"checkmig" + checkmig); String billRefNumber =
* getDriverWrapper().getEventFieldAsText("cANV", "s", ""); String
* createMethod=null,checkmig=null; int check=0; String
* normal_Scenario="select BSV.CREATNMTHD from master MAS,BASEEVENT BSV where MAS.master_ref = '"
* +billRefNumber+"' " +" AND MAS.KEY97=BSV.MASTER_KEY "
* +" AND BSV.REFNO_PFIX IN('ADV','TEL')";
* Loggers.general().info(LOG,"query"+normal_Scenario); con =
* ConnectionMaster.getConnection(); ps = con.prepareStatement(normal_Scenario);
* rs = ps.executeQuery(); while (rs.next()) { checkmig =
* rs.getString("CREATNMTHD"); Loggers.general().info(LOG,"CHECKMIG"+checkmig);
* check++; } if(check==0 && createMethod==null) { String
* rel=billRefNumber.substring(0,billRefNumber.length()-4).trim(); String
* migarted="select BSV.CREATNMTHD from master MAS,BASEEVENT BSV where MAS.master_ref like '%"
* +rel+"%' " +" AND MAS.KEY97=BSV.MASTER_KEY "
* +" AND BSV.REFNO_PFIX IN('ADV','TEL')";
* Loggers.general().info(LOG,"query1"+migarted); ps1 =
* con.prepareStatement(migarted); rs1 = ps1.executeQuery();
*
* while (rs1.next()) { checkmig = rs1.getString("CREATNMTHD");
* Loggers.general().info(LOG,"CHECKMIG"+checkmig); } }
*
* if(checkmig.equalsIgnoreCase("M")) { if
* (getMinorCode().equalsIgnoreCase("POD") && (gateWayVal.equalsIgnoreCase("Y")
* || !gateWayVal.equalsIgnoreCase("N"))) { String orgRefNumber =
* getDriverWrapper().getEventFieldAsText("CLM", "r", "no"); //String
* billRefNumber = getDriverWrapper().getEventFieldAsText("cANV", "s", ""); if
* (orgRefNumber != null && billRefNumber != null) { if (orgRefNumber != "" &&
* billRefNumber != "") { String firstChar = (orgRefNumber.trim()).substring(0,
* 1); if (dailyval_Log.equalsIgnoreCase("YES")) {
* Loggers.general().info(LOG,"org ref Laste 1 char" + firstChar); } String
* lastChar = (orgRefNumber.trim()).substring(orgRefNumber.length() - 3); if
* (dailyval_Log.equalsIgnoreCase("YES")) {
* Loggers.general().info(LOG,"org ref Laste 3 char" + lastChar); } String
* OrgRefNumberCon = firstChar + lastChar; if
* (dailyval_Log.equalsIgnoreCase("YES")) {
* Loggers.general().info(LOG,"org concord value" + OrgRefNumberCon); } String
* billReflast = (billRefNumber.trim()).substring(billRefNumber.length() - 4);
* if (dailyval_Log.equalsIgnoreCase("YES")) {
* Loggers.general().info(LOG,"Bill Ref Laste 4 char" + billReflast); } if
* ((!OrgRefNumberCon.equalsIgnoreCase(billReflast)) &&
* (gateWayVal.equalsIgnoreCase("Y") || !gateWayVal.equalsIgnoreCase("N")) &&
* (step_csm.equalsIgnoreCase("CBS Maker") ||
* step_csm.equalsIgnoreCase("CBS Maker 1"))) {
* validationDetails.addError(ErrorType.Other,
* "Bill reference number doesn't match [CM]");//changed from error to warning
* JIRA 6579 } else { if (dailyval_Log.equalsIgnoreCase("YES")) {
* Loggers.general().info(LOG,"Else Bill reference number is match"); } } } } }
* } else if(checkmig.equalsIgnoreCase("S")||checkmig.equalsIgnoreCase("G")) {
*
* String masterre = getDriverWrapper().getEventFieldAsText("MST", "r", "");
* Loggers.general().info(LOG,"masterre" + masterre);
* if(!billRefNumber.equalsIgnoreCase(masterre)) {
* validationDetails.addError(ErrorType.Other,
* "Bill reference number doesn't match [CM]"); }
*
*
* } else{ Loggers.general().info(LOG,"last else"); }
*
* } catch (Exception e) {
*
* if (dailyval_Log.equalsIgnoreCase("YES")) {
* Loggers.general().info(LOG,"Exception Bill reference number doesn't match"+e.
* getMessage()); }
*
* } finally { try { if (rs != null) rs.close(); if (ps != null) ps.close(); if
* (rs1 != null) rs1.close(); if (ps1 != null) ps1.close(); if (con != null)
* con.close(); } catch (SQLException e) { //
* Loggers.general().info(LOG,"Connection Failed! Check output // console");
* e.printStackTrace(); } }
*/

if (getMinorCode().equalsIgnoreCase("POD") && prd_typ.equalsIgnoreCase("ELF")) {
try {

String theirRef = getDriverWrapper().getEventFieldAsText("THE", "r", "").trim();
String nostref_MT103102 = getWrapper().getNOSTMT().trim();
String nostref_MT940 = getWrapper().getNOSTRM().trim();
String query = "";
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Nostro ref no nostref_MT103102" + nostref_MT103102);
}
// String nostref_MT940950 =
// getWrapper().getNOSTRM().trim();

if (nostref_MT103102.length() > 0) {

if ((!theirRef.equalsIgnoreCase(nostref_MT103102)) && (step_Input.equalsIgnoreCase("i"))
            && step_csm.equalsIgnoreCase("CBS Maker")) {
      validationDetails.addWarning(WarningType.Other,
                  "Nostro Reference number and Received reference number should be same [CM]");

} else {
      if (dailyval_Log.equalsIgnoreCase("YES")) {
            Loggers.general().info(LOG, "Nostro ref no nostref_MT103102 else" + nostref_MT103102);
      }
}

query = "SELECT count(*) as COUNT FROM ETTV_NOSTRO_MT103_MT202_TBL WHERE MT103_MT202_REFERENCE_NUMBER='"
            + nostref_MT103102 + "'";
if (dailyval_Log.equalsIgnoreCase("YES")) {
      Loggers.general().info(LOG, "Nostro ref no nostref_MT103102 query" + query);
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
if (dailyval_Log.equalsIgnoreCase("YES")) {
      Loggers.general().info(LOG, "Nostro ref no nostref_MT103102 query" + query);
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
                  "Nostro ref no nostref_MT103102 count" + value + "value103" + value103);
}
// if ((nostref_MT103102.length() > 0) && (value == 0 && value103 == 0)
query = "select count(*) from ETT_NOSTRO_UTILITY_MT202 M where M.QUEUE_TYPE='CLSQ' and M.REFERENCE_NUMBER='"
            + nostref_MT103102 + "'";
if (dailyval_Log.equalsIgnoreCase("YES")) {
      Loggers.general().info(LOG, "Nostro ref no nostref_MT103102 query" + query);
}
con = ConnectionMaster.getConnection();
ps = con.prepareStatement(query);
rs = ps.executeQuery();
int value202 = 0;
if (rs.next()) {
      value202 = rs.getInt(1);

}

if (dailyval_Log.equalsIgnoreCase("YES")) {
      Loggers.general().info(LOG,
                  "Nostro ref no nostref_MT103102 count" + value + "value202" + value202);
}
if ((nostref_MT103102.length() > 0)
            && ((value == 0 && value103 == 0) && (value == 0 && value202 == 0))
            && (step_Input.equalsIgnoreCase("i"))
            && (step_csm.equalsIgnoreCase("BRANCHINPUT") || step_csm.equalsIgnoreCase("CSM")
                        || step_csm.equalsIgnoreCase("CBS Maker"))) {
      validationDetails.addError(ErrorType.Other,
                  "MT103/202 Nostro Reference number is not valid (" + nostref_MT103102 + ") [CM]");

      // getPane().setNOSTAMT("");
      getPane().setNOSTDAT("");
      getPane().setPOOLAMT("");
      getPane().setNOSTACC("");
      getPane().setMTMESG("");
      getPane().setINWMSG("");
      getPane().setNOSTOUT("");

} else {
      if (dailyval_Log.equalsIgnoreCase("YES")) {
            Loggers.general().info(LOG,
                        "MT103 Nostro Reference else===>" + value + "value103===>" + value103);
      }
}

} else if (nostref_MT940.length() > 0) {
query = "SELECT count(*) FROM ETTV_NOSTRO_MT103_MT202_TBL WHERE MT940_MT950_REFERENCE_NUMBER='"
            + nostref_MT940 + "'";
if (dailyval_Log.equalsIgnoreCase("YES")) {
      Loggers.general().info(LOG, "Nostro ref no nostref_MT940 query" + query);
}
con = ConnectionMaster.getConnection();
ps = con.prepareStatement(query);
rs = ps.executeQuery();
int value = 0;
while (rs.next()) {
      value = rs.getInt(1);
}
if (dailyval_Log.equalsIgnoreCase("YES")) {
      Loggers.general().info(LOG, "Nostro ref no nostref_MT940 count" + value);
}
if ((nostref_MT940.length() > 0) && value == 0 && (step_Input.equalsIgnoreCase("i"))
            && (step_csm.equalsIgnoreCase("BRANCHINPUT") || step_csm.equalsIgnoreCase("CSM")
                        || step_csm.equalsIgnoreCase("CBS Maker"))) {
      validationDetails.addError(ErrorType.Other,
                  "MT940 Nostro Reference number is not valid (" + nostref_MT940 + ") [CM]");
      // getPane().setNOSTAMT("");
      getPane().setNOSTDAT("");
      getPane().setPOOLAMT("");
      getPane().setNOSTACC("");
      getPane().setMTMESG("");
      getPane().setINWMSG("");
      getPane().setNOSTOUT("");
} else {
      if (dailyval_Log.equalsIgnoreCase("YES")) {
            Loggers.general().info(LOG, "MT940 Nostro Reference else" + value);
      }
}
}

} catch (Exception e1) {
// Loggers.general().info(LOG,"Exception for Nostro" +
// e1.getMessage());
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Exception Nostro ref no validation" + e1.getMessage());
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
// Loggers.general().info(LOG,"Connection Failed! Check output
// console");
e.printStackTrace();
}
}
}
//Merchant Trade on sep 21
//String merchanting=getDriverWrapper().getEventFieldAsText("cARQ","l","");
//String mertch = getDriverWrapper().getEventFieldAsText("cAXS", "l", "");
//if(getMajorCode().equalsIgnoreCase("ILC")||getMajorCode().equalsIgnoreCase("IDC")
//||getMajorCode().equalsIgnoreCase("ELC")||getMajorCode().equalsIgnoreCase("ODC"))
//{
//
//try{
////String merchanting=getWrapper().getMERTRA().toString();
//
//String merchref=getWrapper().getREMERREF();
//Loggers.general().info(LOG,"Merchanting------ "+merchanting);
//Loggers.general().info(LOG,"Merchanting Ref No-----"+merchref);
//if(merchanting.equalsIgnoreCase("Y"))
//{
//if(merchref==null||merchref.equalsIgnoreCase(""))
//{
//validationDetails.addError(ErrorType.Other,"Enter the merchant trade reference number");
//}
//}
//else
//{
//
//}
//}
//catch(Exception e)
//{
//Loggers.general().info(LOG,"Exception in Merchant Trade"+e.getMessage());
//}
//}

// For IDC

String advpaymentmade = getDriverWrapper().getEventFieldAsText("cAJO", "l", "");
String dateofremitt = getWrapper().getDATEREM();
String remittrefnoadv = getWrapper().getREMREFAP();
String shipdate = getWrapper().getDASHIP_Name();
String advpaymentreceived = getDriverWrapper().getEventFieldAsText("cAJR", "l", "");
//if(mertch.equalsIgnoreCase("Y")){
//
//if(getMajorCode().equalsIgnoreCase("IDC"))
//{
//try{
//if(advpaymentmade.equalsIgnoreCase("Y")){
//
//
//if(dateofremitt==null||dateofremitt.equalsIgnoreCase(""))
//{
//Loggers.general().info(LOG,"Date of remitt inside Y"+dateofremitt);
//validationDetails.addError(ErrorType.Other,
//"Please enter date of remittance [CM]");
//}
//
//
//if(remittrefnoadv==null||remittrefnoadv.equalsIgnoreCase(""))
//{
//Loggers.general().info(LOG,"remitt ref no inside Y"+remittrefnoadv);
//validationDetails.addError(ErrorType.Other,
//"Please enter remittance reference number for advance payment [CM]");
//
//}
//}
//
//
//
//SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
//Calendar c = Calendar.getInstance();
///*Loggers.general().info(LOG,"Advance Payment"+advpaymentmade);
//Loggers.general().info(LOG,"Date of remittance"+dateofremitt);
//Loggers.general().info(LOG,"Remittance Ref No"+remittrefnoadv);
//Loggers.general().info(LOG,"Shipment Date"+shipdate);*/
//
//if(advpaymentmade.equalsIgnoreCase("Y")){
//
//int gra = 90;
//try {
//if (dailyval_Log.equalsIgnoreCase("YES")) {
//    Loggers.general().info(LOG,"Merchanting Shipdate date 3m---->" + dateofremitt);
//}
//c.setTime(sdf.parse(dateofremitt));
//if (dailyval_Log.equalsIgnoreCase("YES")) {
//    Loggers.general().info(LOG,"Merchanting Completion date added with 90days---->");
//}
//c.add(Calendar.DATE, gra);
//// //Loggers.general().info(LOG,"DAE 1"+ c);
//String output = sdf.format(c.getTime());
//if (dailyval_Log.equalsIgnoreCase("YES")) {
//    Loggers.general().info(LOG,"Merchanting Completion date 3m---->" + output);
//}
////                getPane().setMERTCOMD(output);
//
//}
//catch(Exception e)
//{
//Loggers.general().info(LOG,"Exception in  Merchanting Completion date for 3m==>"+e.getMessage());
//}
//}
//else{
//
//int gra = 180;
//try {
//
//c.setTime(sdf.parse(shipdate));
//// //Loggers.general().info(LOG,"expdate in issue-------> ");
//c.add(Calendar.DATE, gra);
//// //Loggers.general().info(LOG,"DATE 1"+ c);
//String output = sdf.format(c.getTime());
//// //Loggers.general().info(LOG,output);
///*if (dailyval_Log.equalsIgnoreCase("YES")) {
//    Loggers.general().info(LOG," Merchanting Completion date 6m---->" + output);
//}*/
////                getPane().setMERTCOMD(output);
//
//}
//catch(Exception e)
//{
//Loggers.general().info(LOG,"Exception in  Merchanting Completion date for 6m===>"+e.getMessage());
//}
//
//
//}
//}
//catch(Exception e)
//{
//Loggers.general().info(LOG,"Exception in advance payment"+e.getMessage());
//}
//}
//}

// For ODC
//if(mertch.equalsIgnoreCase("Y")){
//
//if(getMajorCode().equalsIgnoreCase("ODC")&&(getMinorCode().equalsIgnoreCase("NCAM")||(getMinorCode().equalsIgnoreCase("NCAJ"))))
//{
//try{
//if(advpaymentreceived.equalsIgnoreCase("Y")){
//
//
//if(dateofremitt==null||dateofremitt.equalsIgnoreCase(""))
//{
//    //Loggers.general().info(LOG,"Date of remitt inside Y"+dateofremitt);
//validationDetails.addError(ErrorType.Other,"Please enter date of remittance [CM]");
//}
//
//
//if(remittrefnoadv==null||remittrefnoadv.equalsIgnoreCase(""))
//{
////Loggers.general().info(LOG,"remitt ref no inside Y"+remittrefnoadv);
//validationDetails.addError(ErrorType.Other,"Please enter remittance reference number for advance payment [CM]");
//
//}
//}
//
//
//
//SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
//Calendar c = Calendar.getInstance();
///*Loggers.general().info(LOG,"Advance Payment"+advpaymentmade);
//Loggers.general().info(LOG,"Date of remittance"+dateofremitt);
//Loggers.general().info(LOG,"Remittance Ref No"+remittrefnoadv);
//Loggers.general().info(LOG,"Shipment Date"+shipdate);*/
//
//if(advpaymentreceived.equalsIgnoreCase("Y")){
//
//int gra = 90;
//try {
//    /*if (dailyval_Log.equalsIgnoreCase("YES")) {
//          Loggers.general().info(LOG,"Merchanting Shipdate date 3m---->" + dateofremitt);
//    }*/
//    c.setTime(sdf.parse(dateofremitt));
//    /*if (dailyval_Log.equalsIgnoreCase("YES")) {
//          Loggers.general().info(LOG,"Merchanting Completion date added with 90days---->");
//    }*/
//    c.add(Calendar.DATE, gra);
//    // //Loggers.general().info(LOG,"DAE 1"+ c);
//    String output = sdf.format(c.getTime());
//    /*if (dailyval_Log.equalsIgnoreCase("YES")) {
//          Loggers.general().info(LOG,"Merchanting Completion date 3m---->" + output);
//    }*/
////                      getPane().setMERTCOMD(output);
//
//}
//catch(Exception e)
//{
//Loggers.general().info(LOG,"Exception in  Merchanting Completion date for 3m==>"+e.getMessage());
//}
//}
//else{
//
//int gra = 180;
//try {
//
//    c.setTime(sdf.parse(shipdate));
//    // //Loggers.general().info(LOG,"expdate in issue-------> ");
//    c.add(Calendar.DATE, gra);
//    // //Loggers.general().info(LOG,"DATE 1"+ c);
//    String output = sdf.format(c.getTime());
//    // //Loggers.general().info(LOG,output);
///*    if (dailyval_Log.equalsIgnoreCase("YES")) {
//          Loggers.general().info(LOG," Merchanting Completion date 6m---->" + output);
//    }*/
////                getPane().setMERTCOMD(output);
//
//}
//catch(Exception e)
//{
//Loggers.general().info(LOG,"Exception in  Merchanting Completion date for 6m===>"+e.getMessage());
//}
//
//
//}
//}
//catch(Exception e)
//{
//Loggers.general().info(LOG,"Exception in advance payment"+e.getMessage());
//}
//}
//}

// todo on sep 27

//if(mertch.equalsIgnoreCase("Y")){
//
//if((getMajorCode().equalsIgnoreCase("ILC")||getMajorCode().equalsIgnoreCase("ELC"))
//&&(getMinorCode().equalsIgnoreCase("CRC")||getMinorCode().equalsIgnoreCase("POC")
//    ||getMinorCode().equalsIgnoreCase("DOP")||getMinorCode().equalsIgnoreCase("POD"))){
//try{
//
//
//
///*Loggers.general().info(LOG,"advpaymentmade"+advpaymentmade);
//Loggers.general().info(LOG,"advpaymentrec"+advpaymentreceived);
//Loggers.general().info(LOG,"dateofremitt"+dateofremitt);
//Loggers.general().info(LOG,"remittrefnoadv"+remittrefnoadv);
//Loggers.general().info(LOG,"Shippment date"+shipdate);
//*/
//
//if(getMajorCode().equalsIgnoreCase("ILC")&&advpaymentmade.equalsIgnoreCase("Y")){
//
//
//if(dateofremitt==null||dateofremitt.equalsIgnoreCase(""))
//{
//validationDetails.addError(ErrorType.Other,     "Please enter date of remittance [CM]");
//}
//}
//
//if(getMajorCode().equalsIgnoreCase("ILC")&&advpaymentmade.equalsIgnoreCase("Y"))
//{
//if(remittrefnoadv==null||remittrefnoadv.equalsIgnoreCase(""))
//{
//validationDetails.addError(ErrorType.Other,"Please enter remittance reference number for advance payment [CM]");
//}
//}
//
//if(getMajorCode().equalsIgnoreCase("ELC")&&advpaymentreceived.equalsIgnoreCase("Y")){
//
//
//if(dateofremitt==null||dateofremitt.equalsIgnoreCase(""))
//{
//validationDetails.addError(ErrorType.Other,"Please enter date of remittance [CM]");
//}
//}
//
//if(getMajorCode().equalsIgnoreCase("ELC")&&advpaymentreceived.equalsIgnoreCase("Y"))
//{
//if(remittrefnoadv==null||remittrefnoadv.equalsIgnoreCase(""))
//{
//validationDetails.addError(ErrorType.Other,"Please enter remittance reference number for advance payment [CM]");
//}
//}
//
//SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
//Calendar c = Calendar.getInstance();
//
//if((advpaymentmade.equalsIgnoreCase("Y")||advpaymentreceived.equalsIgnoreCase("Y"))){
//
//int gra = 90;
//try {
//
//c.setTime(sdf.parse(dateofremitt));
//// //Loggers.general().info(LOG,"expdate in issue-------> ");
////    Loggers.general().info(LOG,"date of remitt inside y for 3m"+dateofremitt);
//c.add(Calendar.DATE, gra);
//// //Loggers.general().info(LOG,"DATE 1"+ c);
//String output = sdf.format(c.getTime());
//// //Loggers.general().info(LOG,output);
///*if (dailyval_Log.equalsIgnoreCase("YES")) {
//    Loggers.general().info(LOG," Merchanting Completion date for 3m---->" + output);
//}*/
////                getPane().setMERTCOMD(output);
//
//}
//catch(Exception e)
//{
////Loggers.general().info(LOG,"Exception in  Merchanting Completion date for 3m"+e.getMessage());
//}
//}
//else{
//
//
//
//
//int gra = 180;
//try {
//
//c.setTime(sdf.parse(shipdate));
//// //Loggers.general().info(LOG,"expdate in issue-------> ");
////    Loggers.general().info(LOG,"date of remitt inside y for 6m"+shipdate);
//c.add(Calendar.DATE, gra);
//// //Loggers.general().info(LOG,"DATE 1"+ c);
//String output = sdf.format(c.getTime());
//// //Loggers.general().info(LOG,output);
///*if (dailyval_Log.equalsIgnoreCase("YES")) {
//    Loggers.general().info(LOG," Merchanting Completion date for 6m---->" + output);
//}*/
////                            getPane().setMERTCOMD(output);
//
//}
//catch(Exception e)
//{
////Loggers.general().info(LOG,"Exception in  Merchanting Completion date for 6m"+e.getMessage());
//}
//
//
//}
//
//}catch(Exception e)
//
//{
////Loggers.general().info(LOG,"Exception in Merchant trade advance payment"+e.getMessage());
//}
//
//}
//}
// try{

//if(mertch.equalsIgnoreCase("Y"))
//{
//if((getMajorCode().equalsIgnoreCase("ILC")&&getMinorCode().equalsIgnoreCase("POC"))||(getMajorCode().equalsIgnoreCase("ELC")&&getMinorCode().equalsIgnoreCase("POD")))
//{
//
//String compbydate = getDriverWrapper().getEventFieldAsText("cAJF", "d","");
//String valuedate=getDriverWrapper().getEventFieldAsText("FPP:XSD", "d", "");
//if((valuedate!=null)&&(valuedate.compareTo(compbydate)<0)){
//validationDetails.addError(ErrorType.Other,
//          "Value date must be less than the merchanting trade completion date [CM]");
//}
//}
//}
//}
//catch(Exception e)
//{
//Loggers.general().info(LOG,"Exception in merchant and valuedate"+e.getMessage());
//}

Loggers.general().info(LOG, "Force limit on validate service" + getPane().getFORLIM());
if (getMajorCode().equalsIgnoreCase("ELC") && getMinorCode().equalsIgnoreCase("DOP")
|| getMinorCode().equalsIgnoreCase("POD")) {
// PostingCustom post = null;
// if(step_csm.equalsIgnoreCase("CSM Maker 1"))
// String strPSID =
// getDriverWrapper().getPostingFieldAsText("PSID",
// "").trim();
try {

String Mast = getDriverWrapper().getEventFieldAsText("MMST", "r", "").trim();
String Evnt = getDriverWrapper().getEventFieldAsText("MEVR", "r", "").trim();

// Loggers.general().info(LOG,"MasterReferenceNNum on post----------->" + Mast);
// Loggers.general().info(LOG,"their Reference on post----------->" + Evnt);

if (dailyval_Log.equalsIgnoreCase("YES")) {

// Loggers.general().info(LOG,"MasterReference-------->" + Mast);
// Loggers.general().info(LOG,"their Reference-------->" + Evnt);
// Loggers.general().info(LOG,"PSID----------->" + strPSID);
}
con = getConnection();

String query = "WITH MAXVAL AS (SELECT row_number() over (ORDER BY BEV.KEY97 asc) as ROWN,"
      + "MAS.MASTER_REF,MAS.KEY97 mas_key,"
      + "BEV.KEY97 bev_key,BEV.REFNO_PFIX || lpad(BEV.REFNO_SERL,3,0) bev_ref ,LIMBLK FROM"
      + " MASTER MAS,BASEEVENT  BEV ,EXTEVENT  EXT WHERE MAS.KEY97=BEV.MASTER_KEY AND BEV.KEY97=EXT.EVENT"
      + " and BEV.status<>'a' and trim(LIMBLK) is not null AND MAS.MASTER_REF='" + Mast
      + "'  ORDER BY BEV.KEY97)" + " SELECT LIMBLK FROM MAXVAL "
      + " WHERE ROWN IN (SELECT CASE WHEN '" + Evnt + "' lIKE 'DPR%' THEN ROWN ELSE ROWN-1 END  "
      + " FROM MAXVAL WHERE bev_ref='" + Evnt + "')";

if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Query Result for Previous limit blocking-> " +
// query);

}

// Loggers.general().info(LOG,"Query Result for Previous limit blocking ->" +
// query);

ps1 = con.prepareStatement(query);

rs1 = ps1.executeQuery();
while (rs1.next()) {

if (dailyval_Log.equalsIgnoreCase("YES")) {
      // Loggers.general().info(LOG,"Entering While Loop=======>");

}
// Loggers.general().info(LOG,"Entering While Loop========>");

String prelimit = rs1.getString(1);
// Loggers.general().info(LOG,"Prelimit=======>"+prelimit);

getPane().setPRVLIMBL(prelimit);
}
// Loggers.general().info(LOG,"Previous limit"+getPane().getPRVLIMBL());

} catch (Exception e) {
e.printStackTrace();
// Loggers.general().info(LOG,"Exception Previous limit blocking" +
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
// Loggers.general().info(LOG,"Connection Failed! Check
// output
// console");
e.printStackTrace();
}
}

}
if ((step_Id.equalsIgnoreCase("CBS Maker 1") || step_csm.equalsIgnoreCase("CBS Reject")
|| step_csm.equalsIgnoreCase("CBS Authoriser"))
&& ((getMajorCode().equalsIgnoreCase("ELC")
      && (getMinorCode().equalsIgnoreCase("DOP") || getMinorCode().equalsIgnoreCase("POD")))
      || (getMajorCode().equalsIgnoreCase("ILC") && (getMinorCode().equalsIgnoreCase("POC"))))) {
// String forEvent = getFORCEDEBIT();
String forFin = getFORCEDEBITFIN();

String TransForceDebit = getDriverWrapper().getEventFieldAsText("cAHW", "l", "").trim();
// String
// TransForceDebitFinance=getDriverWrapper().getEventFieldAsText("cAHW",
// "l","").trim();
// Loggers.general().info(LOG,"Force Debit in main event " + TransForceDebit);
// Loggers.general().info(LOG,"Force Debit in Finance " + forFin);

if (TransForceDebit.equalsIgnoreCase("N") && forFin.equalsIgnoreCase("Y")) {
if (getMinorCode().equalsIgnoreCase("DOP"))
validationDetails.addError(ErrorType.Other,
            "Force Debit Flag should be ticked in Documents Presented [CM]");
else if (getMinorCode().equalsIgnoreCase("POD"))
validationDetails.addError(ErrorType.Other,
            "Force Debit Flag should be ticked in Outstanding Presentation [CM]");
else if (getMinorCode().equalsIgnoreCase("POC"))
validationDetails.addError(ErrorType.Other,
            "Force Debit Flag should be ticked in Outstanding Claim [CM]");
}

}
/*
* if(getMajorCode().equalsIgnoreCase("ELC") &&
* getMinorCode().equalsIgnoreCase("POD")) { String
* outstanding=getDriverWrapper().getEventFieldAsText("OUA","v", "m"); String
* outccy=getDriverWrapper().getEventFieldAsText("OUA","v", "c");
* Loggers.general().info(LOG,"outstanding in elc onvalidate=>" + outstanding);
* Loggers.general().info(LOG,"outccy=>"+outccy);
*
* try{ List<ExtEventShippingTable> shipingDetails =
* (List<ExtEventShippingTable>) getWrapper() .getExtEventShippingTable();
*
* for (int i = 0; i < shipingDetails.size(); i++) { ExtEventShippingTable
* ship_Obj = shipingDetails.get(i); String shipbillnum =
* outstanding+" "+outccy; ship_Obj.setColumn("LOUTSAMT", shipbillnum);
* Loggers.general().info(LOG,"loutsamt in elc=>"+ship_Obj.getLOUTSAMT()); }
*
* } catch(Exception e) {
* Loggers.general().info(LOG,"Exception in ilcpoc =<"+e.getMessage()); } }
*/
if (getMajorCode().equalsIgnoreCase("ELC") && getMinorCode().equalsIgnoreCase("POD")) {
// Loggers.general().info(LOG,"5/14/19 condition1------->");

try {
String payAction = getDriverWrapper().getEventFieldAsText("PYAD", "s", "");
// Loggers.general().info(LOG,"PYAD1------->"+payAction);
if (payAction.equalsIgnoreCase("AMD")) {
// Loggers.general().info(LOG,"5/14/19 entered if1");
getPane().getCtlRELBNKLM().setEnabled(true);
} else {
// Loggers.general().info(LOG,"5/14/19 entered else1");
getPane().getCtlRELBNKLM().setEnabled(false);
}
} catch (Exception e) {
// Loggers.general().info(LOG,"Exception on payment action based
// scenario"+e.getMessage());
}
}

// swiftsfms
/*
* if(getMajorCode().equalsIgnoreCase("ODC") &&
* getMinorCode().equalsIgnoreCase("NCAJ")) { int v; try{ String
* Branch=getDriverWrapper().getEventFieldAsText("CBK", "p", "ss").trim();
* Loggers.general().info(LOG,"Branch code ---->"+Branch);
* v=getSWIFTSFMS(Branch); if (v==1) {
* validationDetails.addError(ErrorType.Other,
* "Please select SFMS in Swift/Sfms [CM]"); } if(v==2) {
* validationDetails.addError(ErrorType.Other,
* "Please select SWIFT in Swift/Sfms [CM]"); } } catch(Exception e) {
* Loggers.general().info(LOG,"Swiftsfms--->" + e.getMessage());
*
* } }
*
*
* if(getMajorCode().equalsIgnoreCase("IDC") &&
* getMinorCode().equalsIgnoreCase("NCAJ")) { try{ int v; String
* Branch=getDriverWrapper().getEventFieldAsText("PRI", "p", "ss").trim();
* Loggers.general().info(LOG,"Branch code ---->"+Branch);
* v=getSWIFTSFMS(Branch); if (v==1) {
* validationDetails.addError(ErrorType.Other,
* "Please select SFMS in Swift/Sfms [CM]"); } if(v==2) {
* validationDetails.addError(ErrorType.Other,
* "Please select SWIFT in Swift/Sfms [CM]"); } } catch(Exception e) {
* Loggers.general().info(LOG,"Same currency fx conversion" + e.getMessage());
*
* } }
*
* if(getMajorCode().equalsIgnoreCase("ELC") &&
* (getMinorCode().equalsIgnoreCase("ADE")||getMinorCode().equalsIgnoreCase(
* "NADE"))) { try{ int v; String
* Branch=getDriverWrapper().getEventFieldAsText("PRI", "p", "ss").trim();
* Loggers.general().info(LOG,"Branch code ---->"+Branch);
* v=getSWIFTSFMS(Branch); if (v==1) {
* validationDetails.addError(ErrorType.Other,
* "Please select SFMS in Swift/Sfms [CM]"); } if(v==2) {
* validationDetails.addError(ErrorType.Other,
* "Please select SWIFT in Swift/Sfms [CM]"); } } catch(Exception e) {
* Loggers.general().info(LOG,"Swiftsfms" + e.getMessage());
*
* } }
*/
/*
* if(getMajorCode().equalsIgnoreCase("ILC") &&
* (getMinorCode().equalsIgnoreCase("ISI")||getMinorCode().equalsIgnoreCase(
* "NADI"))) { int v; String
* advDirect=getDriverWrapper().getEventFieldAsText("ABD", "l", "").trim();
* if(advDirect.equalsIgnoreCase("N") && !advDirect.equalsIgnoreCase("Y")) {
* try{
*
* String Branch=getDriverWrapper().getEventFieldAsText("NPR", "p",
* "ss").trim(); Loggers.general().info(LOG,"Branch code ---->"+Branch);
* v=getSWIFTSFMS(Branch); if (v==1) {
* validationDetails.addError(ErrorType.Other,
* "Please select SFMS in Swift/Sfms [CM]"); } if(v==2) {
* validationDetails.addError(ErrorType.Other,
* "Please select SWIFT in Swift/Sfms [CM]"); } } catch(Exception e) {
* Loggers.general().info(LOG,"Swiftsfms--->" + e.getMessage());
*
* } } }
*/
try {
PeriodicUpfront();
} catch (Exception e) {

}

//if((getMajorCode().equalsIgnoreCase("ILC")) && ((getMinorCode().equalsIgnoreCase("ISI")||getMinorCode().equalsIgnoreCase("NADI")||getMinorCode().equalsIgnoreCase("NAMI"))))
//{
//String val="";
//String sts="";
//String percharup=getPane().getPERCHRAD().trim();
//
//try {
//
//con = getConnection();
//String query = "select dis_msg,status from KMB_M_CHG_VALID_MSG where trim(Master_ref)= '" + masterref + "' and trim(event_ref)='" +eventCode+"'";
//
//ps1 = con.prepareStatement(query);
//rs1 = ps1.executeQuery();
//while (rs1.next()) {
//
//val=rs1.getString(1);
//// Loggers.general().info(LOG,"Value  "+val);
//
//sts=rs1.getString(2);
//// Loggers.general().info(LOG,"Status  "+sts);
//
//}
//
//} catch (Exception e1) {
//
//}
//
//finally {
//try {
//if (rs1 != null)
//    rs1.close();
//if (ps1 != null)
//    ps1.close();
//if (con != null)
//    con.close();
//} catch (SQLException e) {
//
//e.printStackTrace();
//}
//}
//
//if((percharup.equalsIgnoreCase("Yes")&&sts.equalsIgnoreCase("N") &&step_csm.equalsIgnoreCase("CBS Authoriser")))
//{
//validationDetails.addError(ErrorType.Other, val+"[CM]");
//}
//
//
//}
/*
* if (getMajorCode().equalsIgnoreCase("ELC") &&
* getMinorCode().equalsIgnoreCase("POD")) { try{ List<ExtEventLoanDetails>
* loanDetails = (List<ExtEventLoanDetails>)
* getWrapper().getExtEventLoanDetails(); int loanCount=loanDetails.size();
* String subProd=getDriverWrapper().getEventFieldAsText("PTP", "s", ""); String
* preSub=getPane().getPRESHIP();
* Loggers.general().info(LOG,"Current subproduct----------> " + subProd);
* Loggers.general().info(LOG,"Preshipment subproduct----------> " + preSub);
*
* if(preSub!=null&&loanCount>0) { if (!subProd.equalsIgnoreCase(preSub)) {
* validationDetails.addError(ErrorType.Other,
* "You have changed the subproduct type so kindly reverse back and delete the preshipment in both grid and link [CM]"
* ); } }
*
* } catch(Exception e) { e.printStackTrace();
* Loggers.general().info(LOG,"Exception in preshipment subproduct===>" +
* e.getMessage());
*
* } }
*/
if (getMajorCode().equalsIgnoreCase("ELC") && getMinorCode().equalsIgnoreCase("POD")
&& (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1")
      || step_csm.equalsIgnoreCase("CBS Authoriser")
      || step_csm.equalsIgnoreCase("CBS Reject"))) {
// int value=0;
String loanRef = "";
BigDecimal Amount = null;
String ccy = "";
int loancount = 0;
// Loggers.general().info(LOG,"Invoice details size-------------> "+value);
try {
// Loggers.general().info(LOG,"Invoice details size-------------> "+value);
List<ExtEventLoanDetails> LoanDet = (List<ExtEventLoanDetails>) getWrapper()
      .getExtEventLoanDetails();
loancount = LoanDet.size();
// Loggers.general().info(LOG,"Invoice details size-------------> "+loancount);

for (int l = 0; l < LoanDet.size(); l++) {
ExtEventLoanDetails loandetails = LoanDet.get(l);
BigDecimal pre_out = null;
BigDecimal pre_out1 = null;
String master = null;
// invnum=invnum+invoicedetails.getINVNUMB().trim();
loanRef = loandetails.getDEALREF().trim();
Amount = loandetails.getREAMOUNT();

ccy = loandetails.getREAMOUNTCurrency();
// Loggers.general().info(LOG,"Dealreference-------------> "+loanRef);
// Loggers.general().info(LOG,"Loan Amount-------------> "+Amount);
// Loggers.general().info(LOG,"Loan Amount currency-------------> "+ccy);
con = getConnection();
String checkOut = "select AMT_O_S from master,C8PF c8"
            + " where C8.C8CCY= MASTER.CCY AND master_ref='" + loanRef + "' and refno_pfix<>'NEW'";
// Loggers.general().info(LOG,"Loan Amount outstanding query------------->
// "+checkOut);
ps1 = con.prepareStatement(checkOut);
rs1 = ps1.executeQuery();
if (rs1.next()) {
      pre_out = rs1.getBigDecimal(1);
      // Loggers.general().info(LOG,"Amount in query "+pre_out);
}
int res = 0;
res = pre_out.compareTo(Amount);
if (res == -1) {
      // Loggers.general().info(LOG,"Amount less than outstanding");
      validationDetails.addError(ErrorType.Other,
                  "Preshipment knock of amount is greater than the outstanding amount[CM]");
}
String checkOut1 = "SELECT ETT.LOAN_REF, SUM(ETT.REPAYAMT) AS OUT_AMT FROM ETT_PRESHIPMENT_APISERVER ETT ,MASTER MAS,"
            + "  BASEEVENT BEV, (SELECT BEV.KEY97 AS BEV_KEY FROM UBZONE.MASTER MAS, BASEEVENT BEV, EVENTSTEP EVS, ORCH_MAP ORM WHERE MAS.KEY97   = BEV.MASTER_KEY"
            + " AND BEV.KEY97     = EVS.EVENT_KEY AND EVS.ORCH_MAP  = ORM.KEY97 AND ORM.ORCH_STEP = 16 AND BEV.STATUS   <>'c')fp WHERE TRIM(ETT.MASREF)=TRIM(MAS.MASTER_REF) AND TRIM(ETT.EVENTREF)=TRIM(BEV.REFNO_PFIX)||LPAD(BEV.REFNO_SERL,3,0) AND MAS.KEY97  =BEV.MASTER_KEY"
            + " AND bev.key97   = fp.bev_key(+) AND BEV.STATUS ='i' AND fp.bev_key   IS NULL  AND ETT.LOAN_REF= '"
            + loanRef + "' GROUP BY ETT.LOAN_REF";

// Loggers.general().info(LOG,"Loan Amount currency query11------------->
// "+checkOut1);
ps = con.prepareStatement(checkOut1);
rs = ps.executeQuery();
if (rs.next()) {
      pre_out1 = rs.getBigDecimal(2);
      // Loggers.general().info(LOG,"Amount in query11 "+pre_out1);
}
int res1 = 0;
res1 = pre_out.compareTo(pre_out1);
if (res1 == -1) {

      // Loggers.general().info(LOG,"Amount less than outstanding");
      String query = "SELECT ETT.MASREF FROM UBZONE.ETT_PRESHIPMENT_APISERVER ETT ,UBZONE.MASTER MAS,UBZONE.BASEEVENT BEV, (SELECT BEV.KEY97 AS BEV_KEY FROM UBZONE.MASTER MAS, BASEEVENT BEV, EVENTSTEP EVS, ORCH_MAP ORM WHERE MAS.KEY97   = BEV.MASTER_KEY"
                  + " AND BEV.KEY97     = EVS.EVENT_KEY AND EVS.ORCH_MAP  = ORM.KEY97 AND ORM.ORCH_STEP = 16 AND BEV.STATUS   <>'c')fp"
                  + " WHERE TRIM(ETT.MASREF)=TRIM(MAS.MASTER_REF)"
                  + " AND TRIM(ETT.EVENTREF)=TRIM(BEV.REFNO_PFIX)||LPAD(BEV.REFNO_SERL,3,0) "
                  + " AND MAS.KEY97=BEV.MASTER_KEY AND bev.key97   = fp.bev_key(+) AND BEV.STATUS ='i' AND fp.bev_key   IS NULL  AND ETT.LOAN_REF= '"
                  + loanRef + "' and ETT.MASREF!='" + masterref + "'";
      // Loggers.general().info(LOG,"Loan Amount currency query------------->
      // "+query);
      ps2 = con.prepareStatement(query);
      rs2 = ps2.executeQuery();
      while (rs2.next()) {
            /*
             * String master=rs2.getString("MASREF"); mst=mst + " " +master;
             */

            if (master == null || master.isEmpty())
                  master = rs2.getString("MASREF");
            else
                  master = master + " " + rs2.getString("MASREF");
      }
      validationDetails.addError(ErrorType.Other,
                  "Pre shipment knocked off reference number already fetched in" + master
                              + ". Kindly check the outstanding amount [CM]");
} else {
      // Loggers.general().info(LOG,"Amount less than outstanding failure");
}

}

} catch (Exception e) {
e.printStackTrace();
// Loggers.general().info(LOG,"Exception in preshipment subproduct===>" +
// e.getMessage());
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
// Loggers.general().info(LOG,"Connection Failed! Check output
// console");
e.printStackTrace();
}
}
}
String payAction = getDriverWrapper().getEventFieldAsText("PYAD", "s", "");
if (getMajorCode().equalsIgnoreCase("ELC") && getMinorCode().equalsIgnoreCase("POD")
&& (!payAction.equalsIgnoreCase("YM"))
&& (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1")
      || step_csm.equalsIgnoreCase("CBS Authoriser")
      || step_csm.equalsIgnoreCase("CBS Reject"))) {

int cnt = 0;
int count = 0;
int query_count = 0;
try {
/*
* con = getConnection(); String query =
* "select masref from ett_preshipment_apiserver where masref='"
* +masterref+"' and eventref='"+eventREF+ "'";
*
* if (dailyval_Log.equalsIgnoreCase("YES")) {
* Loggers.general().info(LOG,"Query Result for Preshipment1----------> " +
* query);
*
* }
*
* ps = con.prepareStatement(query);
*
* rs = ps.executeQuery(); if (rs.next()) {
*/

// Loggers.general().info(LOG,"Invoice details size1-------------> ");
con = getConnection();
String query1 = "select count(*) from ett_preshipment_apiserver where masref='" + masterref
      + "'and eventref='" + eventREF + "'";

if (dailyval_Log.equalsIgnoreCase("YES")) {
// Loggers.general().info(LOG,"Query Result for Preshipment1----------> " +
// query1);

}

ps1 = con.prepareStatement(query1);

rs1 = ps1.executeQuery();
while (rs1.next()) {
query_count = rs1.getInt(1);
}
// Loggers.general().info(LOG,"Query Result for Preshipment1----------> " +
// query_count);
List<ExtEventLoanDetails> LoanDet = (List<ExtEventLoanDetails>) getWrapper()
      .getExtEventLoanDetails();
// loancount=LoanDet.size();

for (int l = 0; l < LoanDet.size(); l++) {

count = count + 1;

}
// Loggers.general().info(LOG,"Query Result for Preshipment1----------> " +
// count);

if (query_count != count) {
// Loggers.general().info(LOG,"Query Result for Preshipment1----------> " +
// count);
validationDetails.addError(ErrorType.Other, "Please fetch the preshipment details[CM] ");
}
try {
if (ps != null)
      ps.close();
if (rs != null)
      rs.close();
} catch (Exception e) {
// Loggers.general().info(LOG,"close shp---->"+e.getMessage());
}

for (int l = 0; l < LoanDet.size(); l++) {
ExtEventLoanDetails preshipment = LoanDet.get(l);
double amt = 0.0;
String cur = null;
String loanref = null;
int result;
amt = preshipment.getREAMOUNT().doubleValue();
cur = preshipment.getREAMOUNTCurrency();
loanref = preshipment.getDEALREF();

// Loggers.general().info(LOG,"Amount for Preshipment1----------> " + amt);
// Loggers.general().info(LOG,"Currency for Preshipment1----------> " + cur);
// Loggers.general().info(LOG,"Loan Reference for Preshipment1----------> " +
// loanref);
con = getConnection();
String query2 = "select count(*) from ett_preshipment_apiserver where masref='" + masterref
            + "'and eventref='" + eventREF + "' and LOAN_REF ='" + loanref + "' and REPAYAMT='"
            + amt + "' and CURR='" + cur + "'";

// Loggers.general().info(LOG,"Query Result for Preshipment2----------> " +
// query2);

ps = con.prepareStatement(query2);

rs = ps.executeQuery();
if (rs.next()) {
      result = rs.getInt(1);
      if (result == 1)
            cnt = cnt + 1;
      // Loggers.general().info(LOG,"Query Result for Preshipment2----------> " +
      // cnt);
}
}

if (cnt != count) {
// Loggers.general().info(LOG,"Query Result for Preshipment2----------> " +
// cnt);
validationDetails.addError(ErrorType.Other, "Please fetch the preshipment details[CM] ");
}

// }
}

catch (Exception e) {
e.printStackTrace();
// Loggers.general().info(LOG,"Exception in preshipment error===>" +
// e.getMessage());
} finally {
try {
if (rs1 != null)
      rs1.close();
if (ps1 != null)
      ps1.close();
surrenderDB(con, ps1, rs1);
} catch (SQLException e) {
// Loggers.general().info(LOG,"Connection Failed! Check output
// console");
e.printStackTrace();
}
}
}

String customera = getDriverWrapper().getEventFieldAsText("DRW", "p", "no").trim();
//if (getMajorCode().equalsIgnoreCase("ELC") && (getMinorCode().equalsIgnoreCase("DOP")||getMinorCode().equalsIgnoreCase("POD"))) {
//// //Loggers.general().info(LOG,"Advance Table Validation for
//// collection--->");
//try {
//String inwnum = "";
//double totalAmt = 0;
//String balanceValCurrency = ""; // String
//double balance = 0.0;
//String creditcur = "";
//long creditAmount = 0;
//long balanceAmt = 0;
//String masReference = getDriverWrapper().getEventFieldAsText("MST", "r", "");
////    String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
//String cusNo = "";
//double balan = 0.0;
//double amtutl = 0.0;
//String balancur = "";
//String amtutlcur = "";
//List<ExtEventAdvanceTable> lists = (List<ExtEventAdvanceTable>) getWrapper()
//    .getExtEventAdvanceTable();
//for (int a = 0; a < lists.size(); a++) {
//String master=null;
//ExtEventAdvanceTable adve = lists.get(a);
//cusNo = adve.getCUSCIFNO().trim();
//balan = adve.getBALANCE().doubleValue();
//amtutl = adve.getAMTUTIL().doubleValue();
//balancur = adve.getBALANCECurrency();
//amtutlcur = adve.getAMTUTILCurrency();
//inwnum = adve.getINWARD().trim();
//// //Loggers.general().info(LOG,"Advance Table Validation in
//// customer in odc--->" + cusNo + "" + customera);
//
//// Loggers.general().info(LOG,"balance  AMount " + balan);
///*if ((!cusNo.equalsIgnoreCase(customera)) && (step_Input.equalsIgnoreCase("i"))
//          && (step_csm.equalsIgnoreCase("CBS Maker")
//                      || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
//    validationDetails.addWarning(WarningType.Other,
//                "Remittance Customer is not same as the Drawer in advance table [CM]");
//
//}*/
//if (amtutl > balan && (step_Input.equalsIgnoreCase("i"))
//          && (step_csm.equalsIgnoreCase("CBS Maker")
//                      || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
//    validationDetails.addError(ErrorType.Other,
//                "Utillization of amount should not greater than available amount in advance table [CM]");
//}
///*if (!balancur.equalsIgnoreCase(amtutlcur) && (step_Input.equalsIgnoreCase("i"))
//          && (step_csm.equalsIgnoreCase("CBS Maker")
//                      || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
//    validationDetails.addWarning(WarningType.Other,
//                "Utillization of amount currency should equal to available amount currency in advance table [CM]");
//}*/
//
//if (!inwnum.equalsIgnoreCase("")) {
//
//    String inwardDetails = "SELECT TRIM(ORDCUS_CST), trim(TO_CHAR(value_date,'DD/MM/YY')), trim(REMITTER_COUNTRY), CREDIT_AMOUNT, REC_CCY, trim(CIF_NO),TRIM(BANK_ADCODE) FROM ETTV_INWARD_REMITTANCE_AMOUNT WHERE trim(MAS_REF)='"
//                + inwnum + "'";
//    // Loggers.general().info(LOG,"query for getting all fields in inward Remittance grid22 " + inwardDetails);
//    con = getConnection();
//    pst = con.prepareStatement(inwardDetails);
//    
//    rs1 = pst.executeQuery();
//    if (rs1.next()) {
//    
//          creditAmount = rs1.getLong(4);
//          creditcur = rs1.getString(5);
//    
//
//          //    Loggers.general().info(LOG,"Credit AMount22 " + creditAmount);
//
//
//    String BalAmtQuery = "SELECT SUM(NVL(INWARDAMT,0)) AS INWARDAMOUNT FROM (SELECT SUM(NVL(ext.amtutil,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventadv ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF ='"
//                + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
//                + eventCode + "') AND ext.INWARD ='" + inwnum
//                + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(NVL(ext.amtutil,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventadv ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
//                + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
//                + eventCode + "') AND ext.INWARD ='" + inwnum
//                + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(NVL(ext.amtutil,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventadv ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
//                + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) ='"
//                + eventCode + "') AND ext.INWARD ='" + inwnum
//                + "' GROUP BY mas.MASTER_REF ) INWARDAMOUNT";
//
//
//    //    Loggers.general().info(LOG,"Query for getting Inward Utilized Amount===>" + BalAmtQuery);
//
//    pst = con.prepareStatement(BalAmtQuery);
//    rs1 = pst.executeQuery();
//    if (rs1.next()) {
//
//          totalAmt = rs1.getDouble(1);
//
//          balanceValCurrency = creditcur;
//
//          double irmAmt = 0;
//          String closureQuery = "SELECT SUM(IR.AMOUNT)*POWER(10,C8.C8CED) AS IRMAMT FROM ETT_IRM_CLOSURE_TBL IR,C8PF C8 "
//                      + " WHERE IR.MASTER_REFNO = '" + inwnum + "' AND "
//                      + " IR.CURRENCY = C8.C8CCY AND IR.STATUS IN ('TP','A') GROUP BY C8.C8CED ";
//
//          if (dailyval_Log.equalsIgnoreCase("YES")) {
//                // Loggers.general().info(LOG,"IRM Clourse Query ---" + closureQuery);
//          }
//          pst = con.prepareStatement(closureQuery);
//          rs1 = pst.executeQuery();
//          if (rs1.next()) {
//                irmAmt = rs1.getDouble("IRMAMT");
//          } else {
//                irmAmt = 0;
//          }
//
//          totalAmt = totalAmt + irmAmt;
//
//          balanceAmt = (long) (creditAmount - totalAmt);
//
//          /*if (dailyval_Log.equalsIgnoreCase("YES")) {
//                // Loggers.general().info(LOG,"Balance Credit Amount22-->" + balanceAmt);
//          }*/
//          //fdwarapper1.setCUSCIFNO(cif_no);
//          if (balanceAmt > 0) {
//
//                balance = Double.valueOf(balanceAmt);
//                // Loggers.general().info(LOG,"Balance Credit Amount221-->" + balance);
//
//          } else {
//          balance=0.0;
//          }
//    } else {
//
//          double irmAmt = 0;
//          String closureQuery = "SELECT SUM(IR.AMOUNT)*POWER(10,C8.C8CED) AS IRMAMT FROM ETT_IRM_CLOSURE_TBL IR,C8PF C8 "
//                      + " WHERE IR.MASTER_REFNO = '" + inwnum + "' AND "
//                      + " IR.CURRENCY = C8.C8CCY AND IR.STATUS IN ('TP','A') GROUP BY C8.C8CED ";
//
//          if (dailyval_Log.equalsIgnoreCase("YES")) {
//                //Loggers.general().info(LOG,"IRM Clourse Query --->" + closureQuery);
//          }
//          pst = con.prepareStatement(closureQuery);
//          rs1 = pst.executeQuery();
//          if (rs1.next()) {
//                irmAmt = rs1.getDouble("IRMAMT");
//          } else {
//                irmAmt = 0;
//          }
//
//          long balan_cret = (long) (creditAmount - irmAmt);
//
//          /*if (dailyval_Log.equalsIgnoreCase("YES")) {
//                // Loggers.general().info(LOG,"Balance Credit Amount22-->" + balan_cret);
//          }*/
//
//          if (balan_cret > 0) {
//                
//                balance = Double.valueOf(balan_cret);
//          
//
//          } else {
//                balance =0.0;
//          }
//
//    
//
//    }
//    
//    BigDecimal divideByBig1 = new BigDecimal(balance);
//    BigDecimal divideByBig2 = new BigDecimal(amtutl);
//    int res1=0;
//    res1=divideByBig1.compareTo(divideByBig2);
//    
//    // Loggers.general().info(LOG,"Balance Credit Amount221-->" + balance+"bal" + amtutl );
//    if ((res1==-1)&& (step_csm.equalsIgnoreCase("CBS Authoriser")
//                || step_csm.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Reject"))){
//          // Loggers.general().info(LOG,"Amount less than outstanding12");  
//          
//          String query="SELECT mas.MASTER_REF FROM UBZONE.master mas,UBZONE.BASEEVENT bas,"
//+" UBZONE.exteventadv ext WHERE mas.KEY97      =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS  IN ('i','c')"
//+" and ext.INWARD='"+inwnum+"' and mas.master_ref!='"+masReference+"'";
//          // Loggers.general().info(LOG,"Loan Amount currency query12-------------> "+query); 
//          ps2 = con.prepareStatement(query);
//          rs2 = ps2.executeQuery();
//          while (rs2.next()) {
//                /*String master=rs2.getString("MASREF");
//                 mst=mst + " " +master;*/
//                
//                if(master ==null || master.isEmpty())
//                      master=rs2.getString("MASTER_REF");
//                else
//                      master=master + " " +rs2.getString("MASTER_REF");
//          }
//          
//          // Loggers.general().info(LOG,"Balance Credit Amount221-->" + balance);
//          validationDetails.addError(ErrorType.Other,
//                      "Available amount for XAR "+inwnum+" is used in "+master+" so available amount exceeds please check  [CM]");      
//    }
//}
//
//}
//
//
//}
//} catch (Exception e) {
//// Loggers.general().info(LOG,"Exception is Advance Table
//// Validation
//// in customer" + e.getMessage());
//}
//}

// Limit ID change error
if ((getMajorCode().equalsIgnoreCase("ILC"))
&& (getMinorCode().equalsIgnoreCase("NAMI") || getMinorCode().equalsIgnoreCase("NADI"))
&& (step_csm.equalsIgnoreCase("CBS Maker 1") || step_csm.equalsIgnoreCase("CBS Authoriser"))) {
try {
// Loggers.general().info(LOG,"result2----------9/1/2020 ");
// String facility = getDriverWrapper().getEventFieldAsText("1FID", "s",
// "").trim();
// String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
String limit = getDriverWrapper().getEventFieldAsText("PUL2", "s", "").trim();
// Loggers.general().info(LOG,"eventCode----------9/1/2020 "+eventCode);
// Loggers.general().info(LOG,"limit----------9/1/2020 "+limit);
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

// Loggers.general().info(LOG,"result----------9/1/2020 "+result);

}
// Loggers.general().info(LOG,"result----------9/1/2020 "+result);

/*
* String query1 =
* "SELECT SUBSTR(XMLTEXT,INSTR(XMLTEXT,'<id><![CDATA[') +LENGTH('<id><![CDATA['), INSTR(XMLTEXT,']]></id>')-INSTR(XMLTEXT,'<id><![CDATA[')-LENGTH('<id><![CDATA[')) AS FACILITY_ID"
* +" FROM XMLAPISTO WHERE TYPE = 'FA' AND XMLTEXT LIKE ('%"+masterref+eventCode
* +"%')";
*/
// query changed taking only live events
String query1 = "SELECT TO_CHAR(SUBSTR(XMLTEXT,INSTR(XMLTEXT,'<id><![CDATA[') +LENGTH('<id><![CDATA['), INSTR(XMLTEXT,']]></id>')-INSTR(XMLTEXT,'<id><![CDATA[')-LENGTH('<id><![CDATA['))) AS FACILITY_ID"
      + " FROM UBZONE.XMLAPISTO XMLAPI,UBZONE.posting post WHERE TYPE = 'FA' AND TO_NUMBER( TO_CHAR(SUBSTR(XMLAPI.XMLTEXT,INSTR(XMLAPI.XMLTEXT,'<postingKey>')+12,INSTR(SUBSTR(XMLAPI.XMLTEXT,INSTR(XMLAPI.XMLTEXT,'<postingKey>')+12),'</postingKey>')-1 )))= post.key97"
      + " AND XMLTEXT LIKE ('%" + masterref + eventCode + "%')";
// Loggers.general().info(LOG,"query----------9/1/2020 "+query1);
ps = con.prepareStatement(query1);
rs = ps.executeQuery();
while (rs.next()) {
result2 = rs.getString(1);

// Loggers.general().info(LOG,"result----------9/1/2020 "+result);

}

if (result != null && result2 != null && !result.equalsIgnoreCase(result2)
      && (getMinorCode().equalsIgnoreCase("NAMI"))) { // Loggers.general().info(LOG,"result2----------9/1/2020
                                                                              // "+result2);
validationDetails.addError(ErrorType.Other, "Limit node cannot be changed [CM]");
}
if (result != null && result2 != null && !result.equalsIgnoreCase(result2)
      && (getMinorCode().equalsIgnoreCase("NADI")) && (!limit.equalsIgnoreCase("Yes"))) {
// Loggers.general().info(LOG,"limit----------9/1/2020 "+limit);
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
surrenderDB(con, ps1, rs1);
} catch (SQLException e) {

e.printStackTrace();
}
}
}
/// error configured for duplicate events
try {
int cnt = 0;
// Loggers.general().info(LOG,"duplicate reference");
cnt = getduplicate();
// Loggers.general().info(LOG,"count"+cnt);
if (cnt > 1) {
// Loggers.general().info(LOG,"duplicate reference");
validationDetails.addError(ErrorType.Other,
      "The event reference no already exsist kindly abort and recreate the event [CM]");
}
} catch (Exception ee) {
ee.printStackTrace();
// Loggers.general().info(LOG,"duplicate reference" + ee.getMessage());

}
// CR 140 starts
if (getMajorCode().equalsIgnoreCase("ELC") && getMinorCode().equalsIgnoreCase("POD")) {
try {
int cnt = 0;
// Loggers.general () .info(LOG,"PCR/PCF--");
cnt = preshipWar();
// Loggers.general () .info(LOG,"count" + cnt);
String subProductype = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
// Loggers.general () .info(LOG,"subProductype ELC/POD==>" + subProductype);
if (cnt == 1 && (subProductype.equalsIgnoreCase("ELD") || subProductype.equalsIgnoreCase("ELF"))) {
// Loggers.general () .info(LOG,"duplicate reference");
validationDetails.addWarning(WarningType.Other,
            "Packing credits are outstanding for the customer. Please check if this payment is to be used to knock off exsisting PCR/PCF [CM]");
}
} catch (Exception ee) {
ee.printStackTrace();
/// Loggers.general () .info(LOG,"PCR/PCF Exception" + ee.getMessage());

}
try {
shippingAmountSum();
getUidWarning(validationDetails);
} catch (Exception e) {
e.printStackTrace();
}
try {
String stepID = getDriverWrapper().getEventFieldAsText("CSID", "s", "").trim();
String productType = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();

if ((productType.equalsIgnoreCase("06E") || productType.equalsIgnoreCase("07E"))
      && (stepID.equalsIgnoreCase("SCRUTINIZER 3") || stepID.equalsIgnoreCase("AUTHORISE1"))) {

System.out.println("inside advance table validation");
getAdvanceTableValidation(validationDetails);
}
} catch (Exception e) {
e.printStackTrace();
}

}
//CR 140 ends
String subprod = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();
String payactcode = getDriverWrapper().getEventFieldAsText("PYAD", "s", "").trim();
String ownlc = getDriverWrapper().getEventFieldAsText("cBKR", "l", "").trim();
/*
* Loggers.general().info(LOG,"ownlc" + ownlc);
* Loggers.general().info(LOG,"payactcode" + payactcode);
* Loggers.general().info(LOG,"subprod" + subprod);
*/
if ((step_csm.equalsIgnoreCase("CBS Maker 1") || step_csm.equalsIgnoreCase("CBS Authoriser")
|| step_csm.equalsIgnoreCase("CBS Reject")) && getMajorCode().equalsIgnoreCase("ELC")
&& getMinorCode().equalsIgnoreCase("POD") && subprod.equalsIgnoreCase("ELD")
&& !payactcode.equalsIgnoreCase("AMD") && ownlc.equalsIgnoreCase("N")) {
int match = 0;
// Loggers.general().info(LOG,"Entered POD");
match = getLimitMatch();
if (match == 1)
validationDetails.addError(ErrorType.Other, "Limit blocking cannot be changed [CM]");
}

try {
int cnt = 0;
cnt = getRepayProb();
// Loggers.general().info(LOG,"Repayment in subsidery" +cnt );
} catch (Exception ee) {
// Loggers.general().info(LOG,"Repayment in subsidery" + ee.getMessage());

}
try {
int cnt1 = 0;

cnt1 = amountError();
if (cnt1 == 1) {
validationDetails.addError(ErrorType.Other, "Payment Detail Amount Cannot be changed [CM]");
// Loggers.general () .info(LOG,"Repayment in subsidery" +cnt1 );
}
} catch (Exception ee) {
}
// INC4431622 starts
try {
Loggers.general().info(LOG, "Entered method");
removeSpace();

} catch (Exception e) {
Loggers.general().info(LOG, "Exception update" + e.getMessage());
}

// CR 215 starts here

//if ((getMinorCode().equalsIgnoreCase("MBI") || getMinorCode()
//          .equalsIgnoreCase("MBE"))
//          && (step_csm.equalsIgnoreCase("CBS Maker 1") || step_csm
//                      .equalsIgnoreCase("CBS Authoriser"))) {
//int getAccountFromTI = 0;
//try {
//          getAccountFromTI = getAccountFromTI();
//if (getAccountFromTI > 0) {
//                validationDetails
//                            .addError(ErrorType.Other,
//                                        "Manual book keeping entries not allowed for these GL Accounts [CM]");
//          }
//    } catch (Exception e) {
//          
////        e.printStackTrace();
//    }
//}
// INC4431622 ends
try {
// getPostingFxrate();
if ((getMajorCode().equalsIgnoreCase("ILC") || getMajorCode().equalsIgnoreCase("ELC"))
&& (getMinorCode().equalsIgnoreCase("POC") || getMinorCode().equalsIgnoreCase("POD"))) {
getNetPayableAmt();// GST- Discount Reduction
// getWarningBOE(validationDetails);
}
if ((getMajorCode().equalsIgnoreCase("IDC")) && (getMinorCode().equalsIgnoreCase("CLP"))) {
System.out.println("IDC Payment !!!");

getDateDiff(validationDetails);// Date Difference between maturity date and payment date - Vaisak
                                          // 17-11-21
// getWarningBOE(validationDetails);//Warning for BOE grid - Vaisak 17-11-21
getErrorPurposeCode(validationDetails);// Purpose Code starts with S for IDC

// FOR PURPOSE CODE - Vishal
String purSale = getDriverWrapper().getEventFieldAsText("cBCO", "s", "").trim();
String purCode = getDriverWrapper().getEventFieldAsText("cAQL", "s", "");
if (purSale.equalsIgnoreCase("YES") && purCode.equalsIgnoreCase("") || purCode == null) {
validationDetails.addError(ErrorType.Other, "Purpose code is mandatory");

}
// getTotalPayAmt();
getFCCTCALCULATION();
getFcctDetails(validationDetails);
System.out.println("fcct calculation Payment !!!");
}
} catch (Exception e) {
e.printStackTrace();
}
try {
if ((getMajorCode().equalsIgnoreCase("IDC")) && (getMinorCode().equalsIgnoreCase("CLP"))) {
System.out.println("INSIDE INVOICE DATA WARNING");
getRtgsNeftAmt();
// getWarningBOE(validationDetails);
System.out.println("outside INVOICE DATA WARNING");
String amtCcy = getDriverWrapper().getEventFieldAsText("ORA", "v", "c").trim();
String fxdeal = getDriverWrapper().getEventFieldAsText("FXD", "l", "");
String step = getDriverWrapper().getEventFieldAsText("CSDE", "s", "");
if (!amtCcy.equalsIgnoreCase("INR") && fxdeal.equalsIgnoreCase("N")
      && step.equalsIgnoreCase("SCRUTNIZER FOREIGN")) {
validationDetails.addError(ErrorType.Other, "Fx Deal is mandatory");
}
getWarningData(validationDetails);
getCountryRisk();
}
if ((getMajorCode().equalsIgnoreCase("OCC")) && (getMinorCode().equalsIgnoreCase("CLP"))) {
getCommitEndDate();
}
} catch (Exception e) {
e.printStackTrace();
}
/*
* if((getMajorCode().equalsIgnoreCase("ILC"))&&
* (getMinorCode().equalsIgnoreCase("ISI"))){ String lcAmountStr =
* getDriverWrapper().getEventFieldAsText("ORA", "v", "m"); String lcAmountCcy =
* getDriverWrapper().getEventFieldAsText("ORA", "v", "c"); String ccy="USD";
* BigDecimal lcAmount= new BigDecimal(lcAmountStr); BigDecimal advAmt = new
* BigDecimal(100000); if (lcAmount.compareTo(advAmt) == 1
* ||lcAmount.compareTo(advAmt) == 0 && lcAmountCcy.trim().equals(ccy)) {
* validationDetails.addWarning(ValidationDetails.WarningType.Other,
* "credit report on beneficiary required "); } }
*/
if ((getMajorCode().equalsIgnoreCase("ILC")) && (getMinorCode().equalsIgnoreCase("ISI"))) {
try {
getPurposeAdv();
System.out.println("Inside Advance purpose code");
} catch (Exception e) {
e.printStackTrace();
}
try {
getTenorWarning(validationDetails);
System.out.println("Inside tenor validation code");
} catch (Exception e) {
e.printStackTrace();
}

}
//KALPANA GHORPADE ADDED new line or ID in major code and cac in minor code :28-jan-2025
if ((getMajorCode().equalsIgnoreCase("ILC")||getMajorCode().equalsIgnoreCase("IDC")) && (getMinorCode().equalsIgnoreCase("ISI")
|| getMinorCode().equalsIgnoreCase("NAMI") || getMinorCode().equalsIgnoreCase("POC")|| getMinorCode().equalsIgnoreCase("CAC"))) {
try {
getTenordays();
System.out.println("Inside Tendor Days Method");
} catch (Exception e) {
e.printStackTrace();
}
try {
getLimitError(validationDetails);
System.out.println("Inside limit utilisation error Days Method");
} catch (Exception e) {
e.printStackTrace();
}

}
if ((getMajorCode().equalsIgnoreCase("ILC")) && (getMinorCode().equalsIgnoreCase("POC"))) {
// getWarningBOE(validationDetails);
try {
System.out.println("Inside boe grid");
getFcctDetails(validationDetails);
getErrorPurposeCode(validationDetails);
getRtgsNeftAmt();
} catch (Exception e) {
e.printStackTrace();
}
}

// Merchant Trade 19/12/22
if ((getMajorCode().equalsIgnoreCase("ILC") || getMajorCode().equalsIgnoreCase("ELC")
|| getMajorCode().equalsIgnoreCase("IDC") || getMajorCode().equalsIgnoreCase("OCC")
|| getMajorCode().equalsIgnoreCase("ODC"))
&& (getMinorCode().equalsIgnoreCase("POC") || getMinorCode().equalsIgnoreCase("DOP")
      || getMinorCode().equalsIgnoreCase("POD") || getMinorCode().equalsIgnoreCase("CLP")
      || getMinorCode().equalsIgnoreCase("CRC") || getMinorCode().equalsIgnoreCase("ISI")
      || getMinorCode().equalsIgnoreCase("NCAM"))) {
try {
freezeMttNumber();
mttFreeze();
//          unsetMTTSeqNo();
setMTTRefNumber();
// getMttSeqNo();
validateClosedMttNumber(validationDetails);
validateMttRefNumber(validationDetails);
unsetMTTFields();
newMttNumber();
invalidMTTNumber(validationDetails);
} catch (Exception e) {
e.printStackTrace();
}
try {
mttcommenceDate(validationDetails);
mttforeignExchange(validationDetails);
} catch (Exception e) {
e.printStackTrace();
}
try {
getForwardReference();

// getipbranchCode();
System.out.println("forward contract reference:");
} catch (Exception e) {
e.printStackTrace();
// System.out.println("outside BUYERS data:"+e);

}
try {
getPostingBranch(validationDetails);
} catch (Exception e) {
e.printStackTrace();
}

}
if ((getMajorCode().equalsIgnoreCase("ILC")) && (getMinorCode().equalsIgnoreCase("ISI")
|| getMinorCode().equalsIgnoreCase("POC") || getMinorCode().equalsIgnoreCase("CRC"))) {
try {
System.out.println("inside ifsc code:");
getIFSC();
getCountryRisk();
} catch (Exception e) {
e.printStackTrace();
}
}
if ((getMajorCode().equalsIgnoreCase("ELC")) && (getMinorCode().equalsIgnoreCase("ADE")
|| getMinorCode().equalsIgnoreCase("POD") || getMinorCode().equalsIgnoreCase("DOP"))) {
try {
System.out.println("inside CONTRY RISK code:");
//                getIFSC();
getCountryRisk();
} catch (Exception e) {
e.printStackTrace();
}
}
try {
currencyCalc();
} catch (Exception e) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
Loggers.general().info(LOG, "Exception in currency value population---->" + e.getMessage());

}
}
if ((getMajorCode().equalsIgnoreCase("IDC") || getMajorCode().equalsIgnoreCase("ELC")
|| getMajorCode().equalsIgnoreCase("ILC"))
&& (getMinorCode().equalsIgnoreCase("CLP") || getMinorCode().equalsIgnoreCase("POD")
      || getMinorCode().equalsIgnoreCase("POC"))) {

try {
getNostroValidation(validationDetails);
} catch (Exception e) {
e.printStackTrace();
}
}
if ((getMajorCode().equalsIgnoreCase("ILC") || getMajorCode().equalsIgnoreCase("IDC"))
&& (getMinorCode().equalsIgnoreCase("CLP") || getMinorCode().equalsIgnoreCase("POC")
      || getMinorCode().equalsIgnoreCase("CRC"))) {
try {
//    getPane().getExtEventBOECAPUpdate().setEnabled(false);
//    getPane().getExtEventBOECAPNew().setEnabled(false);
//    getPane().getExtEventBOECAPDelete().setEnabled(false);
//    getPane().getExtEventBOECAPUp().setEnabled(false);
//    getPane().getExtEventBOECAPDown().setEnabled(false);
getaccountCode();
} catch (Exception e) {
e.printStackTrace();
// System.out.println("outside BUYERS data:"+e);

}
try {
System.out.println("inside orm  details validation:");
getOrmDetailsValidation(validationDetails);
} catch (Exception e) {
e.printStackTrace();
}
}

if ((getMajorCode().equalsIgnoreCase("ILC") || getMajorCode().equalsIgnoreCase("IDC"))
&& (getMinorCode().equalsIgnoreCase("CLP") || getMinorCode().equalsIgnoreCase("POC")
      || getMinorCode().equalsIgnoreCase("ISI"))) {
try {
getBOEWarning(validationDetails);
} catch (Exception e) {
e.printStackTrace();
}
}
if ((getMajorCode().equalsIgnoreCase("ELC")) && (getMinorCode().equalsIgnoreCase("DOP"))) {
try {
String merchantTrading = "";
String formType = "";
List<ExtEventShippingTable> shipcount = (List<ExtEventShippingTable>) getWrapper()
      .getExtEventShippingTable();
List<ExtEventShippingdetailslc> shipdetails = (List<ExtEventShippingdetailslc>) getWrapper()
      .getExtEventShippingdetailslc();
List<ExtEventInvoicedetailsLC> invoiceDetails = (List<ExtEventInvoicedetailsLC>) getWrapper()
      .getExtEventInvoicedetailsLC();
String subproCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
System.out.println("shipping table size" + shipcount.size() + " " + shipdetails.size() + " "
      + invoiceDetails.size());
/*
* String query =
* "SELECT TRIM(EXTS.CBILLNUM),TRIM(EXTS.CBILLDA),TRIM(EXTS.CFORMN) FROM MASTER MAS, BASEEVENT BEV, EXTEVENT EXT, EXTEVENTSHC EXTS"
* + " WHERE MAS.KEY97 = BEV.MASTER_KEY" + " AND BEV.KEY97 = EXT.EVENT" +
* " AND EXTS.FK_EVENT = EXT.KEY29" + " AND MAS.MASTER_REF = '"+masterref+"'" +
* " AND TRIM(BEV.REFNO_PFIX) ||LPAD(BEV.REFNO_SERL,3,0)='"+eventREF+"'";
* System.out.println(query); ps1 = con.prepareStatement(query); rs1 =
* ps1.executeQuery();
*/
merchantTrading = getPane().getMACHTD();
formType = getPane().getFORTYP();
if ((subproCode.equalsIgnoreCase("01E") || subproCode.equalsIgnoreCase("02E")
      || subproCode.equalsIgnoreCase("04E") || subproCode.equalsIgnoreCase("05E")
      || subproCode.equalsIgnoreCase("06E") || subproCode.equalsIgnoreCase("07E")
      || subproCode.equalsIgnoreCase("08E") || subproCode.equalsIgnoreCase("09E")
      || subproCode.equalsIgnoreCase("41F") || subproCode.equalsIgnoreCase("42F")
      || subproCode.equalsIgnoreCase("44F") || subproCode.equalsIgnoreCase("51F")
      || subproCode.equalsIgnoreCase("52F") || subproCode.equalsIgnoreCase("54F")
      || subproCode.equalsIgnoreCase("61F") || subproCode.equalsIgnoreCase("62F"))
      && (!merchantTrading.equalsIgnoreCase("YES")) && (!formType.equalsIgnoreCase("EXEMPTED"))) {
if (shipcount.size() == 0) {
      validationDetails.addError(ErrorType.Other, "Shipping details should be filled");
} else {
      System.out.println("Shipping grid  has data");
}
if (shipdetails.size() == 0 || invoiceDetails.size() == 0) {
      validationDetails.addError(ErrorType.Other, "Shipping and invoice details must be filled");
}
}
} catch (Exception e) {
e.printStackTrace();
}
try {
System.out.println("Utilized Shipping Bill Validation");
getShiipingWarning(validationDetails);
} catch (Exception e) {
e.printStackTrace();
}
try {
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
Calendar c = Calendar.getInstance();
String rvdDate = getDriverWrapper().getEventFieldAsText("PRD", "d", "");
String tenorPeriod = getDriverWrapper().getEventFieldAsText("cAEP", "s", "");
System.out.println("getNotionalDueDate: " + rvdDate);
if (tenorPeriod != null && !tenorPeriod.trim().isEmpty()) {
int t = Integer.parseInt(tenorPeriod);
System.out.println("tenorPeriod" + t);
if (t > 0) {
      c.setTime(sdf.parse(rvdDate));
      c.add(Calendar.DATE, t);
      System.out.println("inside notional IF: " + sdf.format(c.getTime()));
      getPane().setSIGVALDT(sdf.format(c.getTime()));
}
}

} catch (Exception e) {
e.printStackTrace();
}

}
}

//EBG Field

try {
String stateCode = getPane().getSTATECOD();

if (!stateCode.isEmpty()) {
double stampDuty = getStateWiseStampDuty(stateCode);
// getWrapper().setEBGSTAMPDUTYCurrency("INR");

// getWrapper().setEBGSTAMPDUTY(getDriverWrapper().convertFromToDBFormat(stampDuty,
// "INR", "T") + " " + "INR");

getPane().setEBGSTAMPDUTY(stampDuty + " " + "INR");

System.out.println("The state code is ::: " + stateCode + " Stamp Amount is ::: " + stampDuty);
}
} catch (Exception e) {

e.getStackTrace();
}

//DMS field
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

System.out.println("getDmsreferncelink custom validation class " + getDmsreferncelink);
Loggers.general().info(LOG, "getDmsreferncelink " + getDmsreferncelink);

} catch (Exception e) {

System.out.println("getDmsreferncelink exception custom validation class " + e.getStackTrace());
e.getStackTrace();
}
System.out.println("DMS FIELD in service ONvalidation class");

//end DMS link

//DMS repo field
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

System.out.println("getDmsReporeferncelink custom validation class " + getDmsreferncelink);
Loggers.general().info(LOG, "getDmsReporeferncelink " + getDmsreferncelink);

} catch (Exception e) {

System.out.println("getDmsReporeferncelink exception custom validation class " + e.getStackTrace());
e.getStackTrace();
}
System.out.println("DMS repo FIELD in service ONvalidation class");

//end DMS repo link

}

public static String randomCorrelationId() {
// Loggers.general().info(LOG,"randomCorrelationId generate");
return UUID.randomUUID().toString();
}

/*
* private String setValueTOString(double d1) { DecimalFormat df = new
* DecimalFormat("#.##"); BigDecimal dValue = new
* BigDecimal(df.format(d1)).setScale(2, RoundingMode.HALF_UP); return
* String.valueOf(dValue); }
*/

public String NullChecker(String conv) {
if (conv.isEmpty() || conv.equalsIgnoreCase("") || conv == null) {
conv = "0.0";
}
return conv;
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

public String setxxforparameters(String Value) {
try {
if (Value.equalsIgnoreCase("")) {
Value = "x";
}

} catch (Exception e) {
// Loggers.general().info(LOG,"Exception in setxxforparameters " +
// e.getMessage());
}
return Value;
}

}