package com.misys.tiplus2.customisation.extension;

import java.math.BigDecimal;
//com.misys.tiplus2.customisation.extension.Service
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

//import org.apache.log4j.Logger;

import com.bs.theme.transport.client.ThemeTransportClient;
import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;
import com.misys.tiplus2.customisation.entity.ExtEventInstrumentTable;
import com.misys.tiplus2.customisation.entity.ExtEventLienMarking;
import com.misys.tiplus2.customisation.pane.EventPane;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import com.misys.tiplus2.enigma.customisation.control.ExtendedHyperlinkControlWrapper;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.ErrorType;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.WarningType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.misys.tiplus2.foundations.lang.logging.Loggers;
public class OdcPoc extends ConnectionMaster {
      //private static final Logger logger = Logger.getLogger(ConnectionMaster.class.getName());
      private static final Logger LOG = LoggerFactory.getLogger(OdcPoc.class);
      // //Loggers.general().info(LOG,"eventRefNumber - "+eventRefNumber);
      Connection con = null;

      public boolean onPostInitialise() {
            // GETTING LOB
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
                  // FDenquiry link

                  try {

                        String FDenquiryLink = FDenquiry();
                        ExtendedHyperlinkControlWrapper fdlink = getPane().getCtlFDENQUIRYSHPGUACREclayHyperlink();
                        fdlink.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink1 = getPane().getCtlFDENQUIRYSHPGUADJUSTlayHyperlink();
                        fdlink1.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink2 = getPane().getCtlFDENQUIRYSHPGUAMDlayHyperlink();
                        fdlink2.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink3 = getPane().getCtlFDENQUIRYSHPGUALODCLAIMclayHyperlink();
                        fdlink3.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink4 = getPane().getCtlFDENQUIRYSHPGUASETTCLAclayHyperlink();
                        fdlink4.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink5 = getPane().getCtlFDENQUIRYSHPGURELayHyperlink();
                        fdlink5.setUrl(FDenquiryLink);

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"FDenquiry link"+e.getMessage());
                  }

                  // TST
                  try {
                        String val = "DMSTS";
                        String TSTHyperlink = Link(val);
                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlTSTSHPGUACREclayHyperlink();
                        dmsh.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlTSTSHPGUADJUSTlayHyperlink();
                        dmsh1.setUrl(TSTHyperlink);

                        ExtendedHyperlinkControlWrapper dmsh2 = getPane().getCtlTSTSHPGUALODCLAIMclayHyperlink();
                        dmsh2.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh3 = getPane().getCtlTSTSHPGUAMDlayHyperlink();
                        dmsh3.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh4 = getPane().getCtlTSTSHPGURELayHyperlink();
                        dmsh4.setUrl(TSTHyperlink);

                        ExtendedHyperlinkControlWrapper dmsh5 = getPane().getCtlTSTSHPGUASETTCLAclayHyperlink();
                        dmsh5.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh6 = getPane().getCtlTSTSHPGURETURNclayHyperlink();
                        dmsh6.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh7 = getPane().getCtlTSTSHPGUACORLayHyperlink();
                        dmsh7.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh8 = getPane().getCtlTSTSHPGUMANULlayHyperlink();
                        dmsh8.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh9 = getPane().getCtlTSTSHPGUMAINLayHyperlink();
                        dmsh9.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh10 = getPane().getCtlTSTSHPGUAPAYPERIOlayHyperlink();
                        dmsh10.setUrl(TSTHyperlink);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }

                  // WORKFLOW CHECKLIST

                  try {
                        String Hyperreferel12 = getWorkflow().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack1 = getPane().getCtlCSMCHECKLISTSHPGUACREclayHyperlink();
                        csmreftrack1.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrack12 = getPane().getCtlCSMCHECKLISTSHPGUADJUSTlayHyperlink();
                        csmreftrack12.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrack13 = getPane().getCtlCSMCHECKLISTSHPGUAMDlayHyperlink();
                        csmreftrack13.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrack14 = getPane()
                                    .getCtlCSMCHECKLISTSHPGUALODCLAIMclayHyperlink();
                        csmreftrack14.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrack15 = getPane()
                                    .getCtlCSMCHECKLISTSHPGUASETTCLAclayHyperlink();
                        csmreftrack15.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrack16 = getPane().getCtlCSMCHECKLISTSHPGURELayHyperlink();
                        csmreftrack16.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrack17 = getPane().getCtlCSMCHECKLISTSHPGURETURNclayHyperlink();
                        csmreftrack17.setUrl(Hyperreferel12);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST--------->" +
                        // ees.getMessage());
                  }
                  
                  //For CR-143 Limit Nodes
                  try{
                        String HyperLimitNode = getLimitNode().trim();
                        ExtendedHyperlinkControlWrapper limitnode1 = getPane().getCtlUnavailablelimitnodesSHPGUACREclayHyperlink();
                        limitnode1.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode2 = getPane().getCtlUnavailablelimitnodesSHPGUADJUSTlayHyperlink();
                        limitnode2.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode3 = getPane().getCtlUnavailablelimitnodesSHPGUAMDlayHyperlink();
                        limitnode3.setUrl(HyperLimitNode);
                  }catch(Exception e){
                        System.out.println("For Limit Node"+e.getMessage());
                        
                  }

                  // REFEREL TRACKING
                  try {

                        String Hyperreferel3 = getReferealtracking().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack = getPane().getCtlCSMREFRALSHPGUACREclayHyperlink();
                        csmreftrack.setUrl(Hyperreferel3);
                        ExtendedHyperlinkControlWrapper csmreftrack1 = getPane().getCtlCSMREFRALSHPGUADJUSTlayHyperlink();
                        csmreftrack1.setUrl(Hyperreferel3);
                        ExtendedHyperlinkControlWrapper csmreftrack2 = getPane().getCtlCSMREFRALSHPGUAMDlayHyperlink();
                        csmreftrack2.setUrl(Hyperreferel3);
                        ExtendedHyperlinkControlWrapper csmreftrack3 = getPane().getCtlCSMREFRALSHPGUALODCLAIMclayHyperlink();
                        csmreftrack3.setUrl(Hyperreferel3);
                        ExtendedHyperlinkControlWrapper csmreftrack4 = getPane().getCtlCSMREFRALSHPGUASETTCLAclayHyperlink();
                        csmreftrack4.setUrl(Hyperreferel3);
                        ExtendedHyperlinkControlWrapper csmreftrack5 = getPane().getCtlCSMREFRALSHPGURELayHyperlink();
                        csmreftrack5.setUrl(Hyperreferel3);
                        ExtendedHyperlinkControlWrapper csmreftrack6 = getPane().getCtlCSMREFRALSHPGURETURNclayHyperlink();
                        csmreftrack6.setUrl(Hyperreferel3);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING--------->" +
                        // ees.getMessage());
                  }

                  // Workflow CBS
                  try {
                        String Hyperreferel12 = getWorkflowCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack1 = getPane().getCtlCPCCHECKSHPGUACREclayHyperlink();
                        cpcreftrack1.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrack12 = getPane().getCtlCPCCHECKSHPGUADJUSTlayHyperlink();
                        cpcreftrack12.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrack13 = getPane().getCtlCPCCHECKSHPGUAMDlayHyperlink();
                        cpcreftrack13.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrack14 = getPane().getCtlCPCCHECKSHPGUALODCLAIMclayHyperlink();
                        cpcreftrack14.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrack15 = getPane().getCtlCPCCHECKSHPGUASETTCLAclayHyperlink();
                        cpcreftrack15.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrack16 = getPane().getCtlCPCCHECKSHPGURELayHyperlink();
                        cpcreftrack16.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrack17 = getPane().getCtlCPCCHECKSHPGURETURNclayHyperlink();
                        cpcreftrack17.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST WorkflowCBS--------->"
                        // + ees.getMessage());
                  }

                  // Refereal CBS
                  try {
                        String Hyperreferel12 = getReferealtrackingCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack = getPane().getCtlCPCREFERELSHPGUACREclayHyperlink();
                        cpcreftrack.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrack1 = getPane().getCtlCPCREFERELSHPGUADJUSTlayHyperlink();
                        cpcreftrack1.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrack2 = getPane().getCtlCPCREFERELSHPGUAMDlayHyperlink();
                        cpcreftrack2.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrack3 = getPane().getCtlCPCREFERELSHPGUALODCLAIMclayHyperlink();
                        cpcreftrack3.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrack4 = getPane().getCtlCPCREFERELSHPGUASETTCLAclayHyperlink();
                        cpcreftrack4.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrack5 = getPane().getCtlCPCREFERELSHPGURELayHyperlink();
                        cpcreftrack5.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrack6 = getPane().getCtlCPCREFERELSHPGURETURNclayHyperlink();
                        cpcreftrack6.setUrl(Hyperreferel12);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING REFERE CBS--------->" +
                        // ees.getMessage());
                  }
                  String step_csm = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                  if (!step_csm.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {

                        if (getMajorCode().equalsIgnoreCase("SHG") && getMinorCode().equalsIgnoreCase("SCR")) {
                              try {
                                    getLOBCREATE();
                              } catch (Exception ee) {
                                    Loggers.general().info(LOG,"Exception getLOBCREATE =====>" + ee.getMessage());
                              }
                        }

                        try {
                              currencyCalc();
                        } catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Exception in currency value population---->" + e.getMessage());

                              }
                        }

                        try {
                              getPane().getExtEventLienMarkingUpdate().setEnabled(true);
                              getPane().getExtEventLienMarkingDelete().setEnabled(false);

                        } catch (Exception ee) {
                              // Loggers.general().info(LOG,ee.getMessage());
                              // //Loggers.general().info(LOG,"LOB Catch");
                        } finally {
                              // //Loggers.general().info(LOG,"finally LOB ");
                        }

                  }

            }
            return false;
      }

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
                  String step_csm = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                  // Loggers.general().info(LOG,"step id check for CSM ----->" + step_csm);

                  // FCY Tax calculation
                  try {
                        getPane().getExtEventLienMarkingDelete().setEnabled(false);

                        getFCCTCALCULATION();

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Exception update" +
                        // e.getMessage());
                  }

                  if (getMajorCode().equalsIgnoreCase("SHG") && getMinorCode().equalsIgnoreCase("SCR")) {
                        try {
                              getLOBCREATE();
                        } catch (Exception ee) {
                              Loggers.general().info(LOG,"Exception getLOBCREATE =====>" + ee.getMessage());
                        }
                  }

                  String step_Input = getDriverWrapper().getEventFieldAsText("CSTY", "s", "");
                  // Loggers.general().info(LOG,"step_Input check ----->" + step_Input);

                  try {
                        currencyCalc();
                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception in currency value population---->" + e.getMessage());

                        }
                  }
                  // FCY Tax calculation
                  try {

                        getFCCTCALCULATION();

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Exception update" +
                        // e.getMessage());
                  }

                  // FDenquiry link

                  try {

                        String FDenquiryLink = FDenquiry();
                        ExtendedHyperlinkControlWrapper fdlink = getPane().getCtlFDENQUIRYSHPGUACREclayHyperlink();
                        fdlink.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink1 = getPane().getCtlFDENQUIRYSHPGUADJUSTlayHyperlink();
                        fdlink1.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink2 = getPane().getCtlFDENQUIRYSHPGUAMDlayHyperlink();
                        fdlink2.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink3 = getPane().getCtlFDENQUIRYSHPGUALODCLAIMclayHyperlink();
                        fdlink3.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink4 = getPane().getCtlFDENQUIRYSHPGUASETTCLAclayHyperlink();
                        fdlink4.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink5 = getPane().getCtlFDENQUIRYSHPGURELayHyperlink();
                        fdlink5.setUrl(FDenquiryLink);

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"FDenquiry link"+e.getMessage());
                  }

                  // WORKFLOW CHECKLIST

                  try {
                        String Hyperreferel12 = getWorkflow().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack1 = getPane().getCtlCSMCHECKLISTSHPGUACREclayHyperlink();
                        csmreftrack1.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrack12 = getPane().getCtlCSMCHECKLISTSHPGUADJUSTlayHyperlink();
                        csmreftrack12.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrack13 = getPane().getCtlCSMCHECKLISTSHPGUAMDlayHyperlink();
                        csmreftrack13.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrack14 = getPane()
                                    .getCtlCSMCHECKLISTSHPGUALODCLAIMclayHyperlink();
                        csmreftrack14.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrack15 = getPane()
                                    .getCtlCSMCHECKLISTSHPGUASETTCLAclayHyperlink();
                        csmreftrack15.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrack16 = getPane().getCtlCSMCHECKLISTSHPGURELayHyperlink();
                        csmreftrack16.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrack17 = getPane().getCtlCSMCHECKLISTSHPGURETURNclayHyperlink();
                        csmreftrack17.setUrl(Hyperreferel12);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST--------->" +
                        // ees.getMessage());
                  }
                  
                  //For CR-143 Limit Nodes
                  try{
                        String HyperLimitNode = getLimitNode().trim();
                        ExtendedHyperlinkControlWrapper limitnode1 = getPane().getCtlUnavailablelimitnodesSHPGUACREclayHyperlink();
                        limitnode1.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode2 = getPane().getCtlUnavailablelimitnodesSHPGUADJUSTlayHyperlink();
                        limitnode2.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode3 = getPane().getCtlUnavailablelimitnodesSHPGUAMDlayHyperlink();
                        limitnode3.setUrl(HyperLimitNode);
                  }catch(Exception e){
                        System.out.println("For Limit Node"+e.getMessage());
                        
                  }
                  // CR 215 starts here

                  if ((getMinorCode().equalsIgnoreCase("SMB"))
                              && (step_csm.equalsIgnoreCase("CBS Maker 1") || step_csm
                                          .equalsIgnoreCase("CBS Authoriser"))) {

                        int getAccountFromTI = 0;

                        try {
                              getAccountFromTI = getAccountFromTI();

                              System.out.println("Result getAccountFromTI-->"
                                          + getAccountFromTI);

                              if (getAccountFromTI > 0) {
                                    validationDetails
                                                .addError(ErrorType.Other,
                                                            "Manual book keeping entries not allowed for these GL Accounts [CM]");
                              }

                        } catch (Exception e) {
                              System.out.println("Exception in getAcoount-->"
                                          + e.getMessage());
                              e.printStackTrace();
                        }

                  }
            
//CR 215 ends here
                  // REFEREL TRACKING
                  try {

                        String Hyperreferel3 = getReferealtracking().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack = getPane().getCtlCSMREFRALSHPGUACREclayHyperlink();
                        csmreftrack.setUrl(Hyperreferel3);
                        ExtendedHyperlinkControlWrapper csmreftrack1 = getPane().getCtlCSMREFRALSHPGUADJUSTlayHyperlink();
                        csmreftrack1.setUrl(Hyperreferel3);
                        ExtendedHyperlinkControlWrapper csmreftrack2 = getPane().getCtlCSMREFRALSHPGUAMDlayHyperlink();
                        csmreftrack2.setUrl(Hyperreferel3);
                        ExtendedHyperlinkControlWrapper csmreftrack3 = getPane().getCtlCSMREFRALSHPGUALODCLAIMclayHyperlink();
                        csmreftrack3.setUrl(Hyperreferel3);
                        ExtendedHyperlinkControlWrapper csmreftrack4 = getPane().getCtlCSMREFRALSHPGUASETTCLAclayHyperlink();
                        csmreftrack4.setUrl(Hyperreferel3);
                        ExtendedHyperlinkControlWrapper csmreftrack5 = getPane().getCtlCSMREFRALSHPGURELayHyperlink();
                        csmreftrack5.setUrl(Hyperreferel3);
                        ExtendedHyperlinkControlWrapper csmreftrack6 = getPane().getCtlCSMREFRALSHPGURETURNclayHyperlink();
                        csmreftrack6.setUrl(Hyperreferel3);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING--------->" +
                        // ees.getMessage());
                  }

                  // Workflow CBS
                  try {
                        String Hyperreferel12 = getWorkflowCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack1 = getPane().getCtlCPCCHECKSHPGUACREclayHyperlink();
                        cpcreftrack1.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrack12 = getPane().getCtlCPCCHECKSHPGUADJUSTlayHyperlink();
                        cpcreftrack12.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrack13 = getPane().getCtlCPCCHECKSHPGUAMDlayHyperlink();
                        cpcreftrack13.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrack14 = getPane().getCtlCPCCHECKSHPGUALODCLAIMclayHyperlink();
                        cpcreftrack14.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrack15 = getPane().getCtlCPCCHECKSHPGUASETTCLAclayHyperlink();
                        cpcreftrack15.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrack16 = getPane().getCtlCPCCHECKSHPGURELayHyperlink();
                        cpcreftrack16.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrack17 = getPane().getCtlCPCCHECKSHPGURETURNclayHyperlink();
                        cpcreftrack17.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST WorkflowCBS--------->"
                        // + ees.getMessage());
                  }

                  // Refereal CBS
                  try {
                        String Hyperreferel12 = getReferealtrackingCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack = getPane().getCtlCPCREFERELSHPGUACREclayHyperlink();
                        cpcreftrack.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrack1 = getPane().getCtlCPCREFERELSHPGUADJUSTlayHyperlink();
                        cpcreftrack1.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrack2 = getPane().getCtlCPCREFERELSHPGUAMDlayHyperlink();
                        cpcreftrack2.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrack3 = getPane().getCtlCPCREFERELSHPGUALODCLAIMclayHyperlink();
                        cpcreftrack3.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrack4 = getPane().getCtlCPCREFERELSHPGUASETTCLAclayHyperlink();
                        cpcreftrack4.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrack5 = getPane().getCtlCPCREFERELSHPGURELayHyperlink();
                        cpcreftrack5.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrack6 = getPane().getCtlCPCREFERELSHPGURETURNclayHyperlink();
                        cpcreftrack6.setUrl(Hyperreferel12);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING REFERE CBS--------->" +
                        // ees.getMessage());
                  }

                  // Margin validation
                  // String stepcd = getDriverWrapper().getEventFieldAsText("ECOI",
                  // "s", "").trim();
                  double marginB = 0.0;
                  try {
                        // //Loggers.general().info(LOG,"Margin validation part Entered");
                        // boolean isMarginAvailableForCIF =
                        // checkAvailblityOfMarginForCIF(con);

                        String facilityId = getDriverWrapper().getEventFieldAsText("cBEC", "s", "");
                        // //Loggers.general().info(LOG,"facilityId - " + facilityId);
                        // //Loggers.general().info(LOG,"facilityId ordinary length - " +
                        // facilityId.length() + " & facility trim length is "+
                        // facilityId.trim().length() + " & isMarginAvailableForCIF - "
                        // +
                        // isMarginAvailableForCIF);
                        // if ((facilityId == null || facilityId.length() == 1) &&
                        // isMarginAvailableForCIF
                        // && (step_Input.equalsIgnoreCase("i")) &&
                        // (step_csm.equalsIgnoreCase("CBS Maker"))) {
                        // // //Loggers.general().info(LOG,"facility id is not available");
                        // //
                        // validationDetails.addWarning(ValidationDetails.WarningType.Other,
                        // // "Facility ID to be entered to calculate Margin amount
                        // // :[CM]");
                        // } else {
                        // boolean isFacilityIdForCIF = checkFacilityIdForCIF(con,
                        // facilityId);
                        // // //Loggers.general().info(LOG,"isFacilityIdForCIF - " +
                        // // isFacilityIdForCIF);
                        // if (!isFacilityIdForCIF && (step_Input.equalsIgnoreCase("i"))
                        // && (step_csm.equalsIgnoreCase("CBS Maker")))
                        //
                        // {
                        // //
                        // validationDetails.addWarning(ValidationDetails.WarningType.Other,
                        // // "Facility Id entered is not related to the primary
                        // // customer :[CM]");
                        // }
                        // }

                        String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim(); //

                        if (getMajorCode().equalsIgnoreCase("SHG")) {
                              String facid = getWrapper().getFACLTYID().trim();

                              if (facid == null || facid.equalsIgnoreCase("")) {
                                    getPane().setPMARGIN("");
                                    getPane().setMARAMT("");
                              }

                        }

                        // //Loggers.general().info(LOG,"Going to calculate the Expected margin
                        // amount");
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
                              // //Loggers.general().info(LOG," & marginAmt - " + marginAmt);
                              // double clearB = 0.0;

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

                              if (marginAmt != null)
                                    marginB = Double.valueOf(marginAmt.doubleValue() / 100);

                              totalMargin += marginB;
                              if (totalMargin > marDob && (step_Input.equalsIgnoreCase("i"))
                                          && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                    // //Loggers.general().info(LOG,"totalMargin -----> " +
                                    // totalMargin);
                                    validationDetails.addWarning(WarningType.Other,
                                                "Grid Margin amount should not greater then the Margin Amount  [CM]");
                              } else {
                                    // Loggers.general().info(LOG,"totalMargin is less then the
                                    // margin amount ");
                              }
                              
                              //todo for jira 7045
                              
                              
                              Loggers.general().info(LOG,"Lien status check===> "+liensta);
                        
                              if(liensta.equalsIgnoreCase("MARK FAILED"))
                              {
                                    Loggers.general().info(LOG,"inside lien mark failed error");
                                    validationDetails.addError(ErrorType.Other,"Lien Marking Failed[CM]");
                              }
                              else
                              {
                                    Loggers.general().info(LOG,"else lien mark failed error");
                              }
                              
                        
                        
                        //end of 7045


                              if (liensta.equalsIgnoreCase("MARK SUCCEEDED")) {
                                    arr.add(marginB);
                                    // //Loggers.general().info(LOG,"lien status array list
                                    // arr---->"
                                    // + arr);

                              } else {
                                    // Loggers.general().info(LOG,"lien status in else loop---->" +
                                    // liensta);
                              }
                        }
                        String evnt = getDriverWrapper().getEventFieldAsText("EPF", "s", "");
                        String evvcount = getDriverWrapper().getEventFieldAsText("ESQ", "i", "");
                        if (step_csm.equalsIgnoreCase("CBS Maker 1") && ExtEventLienMark.size() > 0) {
                              try {

                                    String query = "SELECT count(*),lmg.DEPOSTNO,lmg.LINEST FROM MASTER mas, BASEEVENT bas, EXTEVENTLMG lmg WHERE mas.KEY97 =bas.MASTER_KEY AND bas.EXTFIELD =lmg.FK_EVENT AND lmg.DEPOSTNO='"
                                                + dopNo + "' AND lmg.LINEST ='MARK SUCCEEDED' AND mas.MASTER_REF ='" + MasterReference
                                                + "' AND bas.REFNO_PFIX ='" + evnt + "' AND bas.REFNO_SERL=" + evvcount
                                                + " group by lmg.DEPOSTNO,lmg.LINEST";
                                    // Loggers.general().info(LOG,"lien status check---->" + query);
                                    con = getConnection();
                                    ps1 = con.prepareStatement(query);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          counterVal = rs1.getInt(1);
                                          // Loggers.general().info(LOG,"lien counterVal in
                                          // while---->" + counterVal);

                                    }
                              } catch (Exception e) {
                                    Loggers.general().info(LOG,"Lien account===> " + e.getMessage());
                              } finally {
                                    try {
                                          if (rs1 != null)
                                                rs1.close();
                                          if (ps1 != null)
                                                ps1.close();
                                          if (con != null)
                                                con.close();
                                    } catch (SQLException e) {
                                          Loggers.general().info(LOG,"Connection Failed! Check output console");
                                          e.printStackTrace();
                                    }
                              }

                              if (ExtEventLienMark.size() > 0 && counterVal > 1 && (step_csm.equalsIgnoreCase("CBS Maker 1"))) {

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Multiple liens cannot be marked for same account number---->" + dopNo);
                                    }

                                    validationDetails.addError(ErrorType.Other,
                                                "Multiple liens cannot be marked for same account number(" + dopNo
                                                            + "). Reverse lien for account number and mark new lien [CM]");
                              }

                              else if (ExtEventLienMark.size() > 0) {
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
                                                      Loggers.general().info(LOG,"Lien Status in else" + lienbox);
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
                                          Loggers.general().info(LOG,"Themebridge finalXml for enquiry" + finalXml);

                                          ThemeTransportClient anThemeTransportClient = new ThemeTransportClient();
                                          String responseXML = anThemeTransportClient.invoke("Customization", "FDLienAdd", finalXml);
                                          Loggers.general().info(LOG,"Final Themebridge responseXML for enquiry==========>" + responseXML);
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
                                                      Loggers.general().info(LOG,"Lien mark Amount" + lienAmt);
                                                      String currentDepositNum = res_sp_line[0].trim();
                                                      Loggers.general().info(LOG,"Lien response currentDepositNum" + currentDepositNum);
                                                      String responseValue = res_sp_line[3].trim();
                                                      BigDecimal responseAmount = new BigDecimal(responseValue);
                                                      String responseAmt = diff.format(responseAmount);

                                                      Loggers.general().info(LOG,"Lien response amount" + responseAmt);
                                                      String markVal = res_sp_line[4].trim();
                                                      String markValue = "MARK " + markVal;
                                                      Loggers.general().info(LOG,"Lien mark response Value" + markValue);
                                                      String lienbox = str.getLINEST().trim();
                                                      Loggers.general().info(LOG,"Lien mark grig Value" + lienbox);
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

                                                                  Loggers.general().info(LOG,"lien and response amount is same else" + lienAmt
                                                                              + "responseAmt" + responseAmt);
                                                            }
                                                      } else {
                                                            Loggers.general().info(LOG,"lien depositNum else" + depositNum + "currentDepositNum===>"
                                                                        + currentDepositNum);
                                                      }

                                                }

                                          }

                                          // }
                                    } catch (Exception e) {
                                          Loggers.general().info(LOG,"ThemeTransportClient Exception response " + e.getMessage());
                                    }
                              } else {

                              }

                        }
                        // validation mark succeed
                        double dob1 = 0;
                        for (Double dob : arr) {

                              dob1 = dob1 + dob;
                              // //Loggers.general().info(LOG,"marksucceed=======>" + dob);
                        }

                        Loggers.general().info(LOG,"marksucceed=======>" + dob1);
                        Loggers.general().info(LOG,"Array size=="+arr.size());
                        Loggers.general().info(LOG,"Step Input===>"+step_Input);
                        Loggers.general().info(LOG,"Step csm===>"+step_csm);

                        if (arr.size() > 0 && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))
                                    && getMinorCode().equalsIgnoreCase("SGR")) {
                              Loggers.general().info(LOG,"Marginamt_log for -----> " + dob1);
                              validationDetails.addWarning(WarningType.Other, "Total Lien Mark amount is (" + dob1 + " INR), Please Release the Lien [CM]");
                        } else {
                               Loggers.general().info(LOG,"Lien mark amount in else" + dob1);
                        }

                        if (marDob > 0 && ExtEventLienMark.size() == 0 && (step_Input.equalsIgnoreCase("i"))
                                    && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1"))
                                    && (getMinorCode().equalsIgnoreCase("SCR") || getMinorCode().equalsIgnoreCase("SGD")
                                                || getMinorCode().equalsIgnoreCase("SGM") || getMinorCode().equalsIgnoreCase("SGR"))) {
                              // //Loggers.general().info(LOG,"ExtEventLienMark ILC--> " +
                              // ExtEventLienMark + " & marginAmt ILC ---> " + marDob);
                              validationDetails.addWarning(WarningType.Other,
                                          "Lien amount is calculated and no Lien Amount is entered in FD lien grid  [CM]");
                        } else {
                              // Loggers.general().info(LOG," FD lien grid is entered----->");

                        }

                        // //Loggers.general().info(LOG,"totalMargin - "+totalMargin);
                        String expectedMarginStr = getDriverWrapper().getEventFieldAsText("cAAR", "v", "m");
                        double expectedMarginAmt = 0.0;
                        if (expectedMarginStr.length() != 0)
                              expectedMarginAmt = Double.parseDouble(expectedMarginStr);
                        // //Loggers.general().info(LOG,"expectedMarginAmt - " +
                        // expectedMarginAmt);
                        if ((ExtEventLienMark.size() > 0) && (totalMargin != expectedMarginAmt)
                                    && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                              validationDetails.addWarning(WarningType.Other,
                                          "Sum of margin amount should be equal to Required Lien Amount [CM]");
                        }

                        // if (isMarginAvailableForCIF && (expectedMarginAmt < 0.0) &&
                        // (step_Input.equalsIgnoreCase("i"))
                        // && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                        //
                        // // validationDetails.addWarning(WarningType.Other, "Expected
                        // // margin amount field is empty, please input facility Id to
                        // // calculate the same [CM]");
                        // }

                        con.close();
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Exception occured in margin validation -
                        // " + e.getMessage());
                  }

                  // String seifsc = getWrapper().getSENIFSC().trim();
                  // //Loggers.general().info(LOG,"IFSC Code"+Ifsc);

                  // if (seifsc.length() > 0 && (step_Input.equalsIgnoreCase("i")) &&
                  // (step_csm.equalsIgnoreCase("CBS Maker"))) {
                  // try {
                  //
                  // String query = "select count(*) from EXTIFSCC where IFSC='" +
                  // seifsc + "'";
                  // // //Loggers.general().info(LOG,"query " + query);
                  // int count3 = 0;
                  // con = getConnection();
                  // ps1 = con.prepareStatement(query);
                  // rs1 = ps1.executeQuery();
                  // while (rs1.next()) {
                  // // //Loggers.general().info(LOG,"Entered while");
                  // count3 = rs1.getInt(1);
                  // }
                  //
                  // if (count3 == 0 && (step_Input.equalsIgnoreCase("i")) &&
                  // (step_csm.equalsIgnoreCase("CBS Maker"))) {
                  //
                  // validationDetails.addError(ErrorType.Other, "Invalid Sender IFSC
                  // code(" + seifsc + ") [CM]");
                  // } else {
                  // // Loggers.general().info(LOG,"valid Sender IFSC code");
                  // }
                  //
                  // } catch (Exception e1) {
                  // // Loggers.general().info(LOG,"Exception Sender IFSC Code" +
                  // // e1.getMessage());
                  // } finally {
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
                  // }
                  // } else {
                  // Loggers.general().info(LOG,"Sender IFSC Code" + seifsc);

                  // }

                  // String recifsc = getWrapper().getRECIFSC().trim();
                  // //Loggers.general().info(LOG,"IFSC Code"+Ifsc);

                  // if (recifsc.length() > 0 && (step_Input.equalsIgnoreCase("i")) &&
                  // (step_csm.equalsIgnoreCase("CBS Maker"))) {
                  // try {
                  //
                  // String query = "select count(*) from EXTIFSCC where IFSC='" +
                  // recifsc + "'";
                  // // //Loggers.general().info(LOG,"query " + query);
                  // int count3 = 0;
                  // con = getConnection();
                  // ps1 = con.prepareStatement(query);
                  // rs1 = ps1.executeQuery();
                  // while (rs1.next()) {
                  // // //Loggers.general().info(LOG,"Entered while");
                  // count3 = rs1.getInt(1);
                  // }
                  //
                  // if (count3 == 0 && (step_Input.equalsIgnoreCase("i")) &&
                  // (step_csm.equalsIgnoreCase("CBS Maker"))) {
                  //
                  // validationDetails.addError(ErrorType.Other,
                  // "Invalid Receiver IFSC code(" + recifsc + ") [CM]");
                  // } else {
                  // // Loggers.general().info(LOG,"valid Receiver IFSC code");
                  // }
                  //
                  // } catch (Exception e1) {
                  // // Loggers.general().info(LOG,"Exception Receiver IFSC Code" +
                  // // e1.getMessage());
                  // } finally {
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
                  // }
                  // } else {
                  // Loggers.general().info(LOG,"Receiver IFSC Code" + recifsc);

                  // }
                  String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim(); //
                  String productcode = getDriverWrapper().getEventFieldAsText("PCO", "s", "").trim(); // ILC
                  String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim(); // ISS001
                  String subproductCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();// ILF
                  // workflow error configuration
                  try {
                        int count = 0;

                        if (step_csm.equalsIgnoreCase("CSM") || step_csm.equalsIgnoreCase("AdhocCSM")) {
                              String query_wrk = "select count(*) from ETT_WF_CHKLST_TRACKING where MASTER_REF='"
                                          + MasterReference + "' and EVENTREF='" + eventCode + "' and PROD_CODE='" + productcode
                                          + "' and SUB_PRODUCT_CODE='" + subproductCode + "' and INIT_AT='CSM'";
                              con = getConnection();
                              ps1 = con.prepareStatement(query_wrk);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    count = rs1.getInt(1);
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
                                    count = rs1.getInt(1);
                                    // //Loggers.general().info(LOG,"ETT_WF_CHKLST_TRACKING in while
                                    // loop----> " + count3);

                              }
                        }
                        if (count < 1 && step_Input.equalsIgnoreCase("i")
                                    && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CSM")
                                                || step_csm.equalsIgnoreCase("AdhocCSM"))
                                    && (getMinorCode().equalsIgnoreCase("SCR") || getMinorCode().equalsIgnoreCase("SGD")
                                                || getMinorCode().equalsIgnoreCase("SGL") || getMinorCode().equalsIgnoreCase("SGM")
                                                || getMinorCode().equalsIgnoreCase("SGR") || getMinorCode().equalsIgnoreCase("SGS")
                                                || getMinorCode().equalsIgnoreCase("SGT"))
                                    && getMajorCode().equalsIgnoreCase("SHG")) {

                              validationDetails.addError(ErrorType.Other, "Workflow checklist is mandatory  [CM]");                       }

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

                  // Notes Populated in Summary

                  try {
                        con = getConnection();
                        String query = "select * from master mas,NOTE, TIDATAITEM tid WHERE mas.KEY97 = tid.MASTER_KEY AND tid.KEY97 = NOTE.KEY97 AND (NOTE.TYPE NOT IN (3,129,1089) or NOTE.TYPE is null) AND note_event IS NOT NULL AND NOTE.ACTIVE = 'Y' AND mas.MASTER_REF = '"
                                    + MasterReference + "' ";
                        // Loggers.general().info(LOG,"Notes Populated in Summary query====> " +
                        // query);

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

                  try {
                        int noOfRecord = 0;
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

                                    Loggers.general().info(LOG,"Duplicate Record Query is " + duplicateMaster);
                              }
                              con = getConnection();
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

                                          Loggers.general().info(LOG,"Duplicate Record in else " + noOfRecord);
                                    }
                              }
                        }
                        // connection.close();
                  } catch (Exception e) {

                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              Loggers.general().info(LOG,"Exception in Duplicate master " + e.getMessage());
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
                  ///error configured for duplicate events
                  try {
                   int cnt=0;
                   Loggers.general().info(LOG,"duplicate reference");
                              cnt= getduplicate();
                               Loggers.general().info(LOG,"count"+cnt);
                              if(cnt>1)
                              {
                                     Loggers.general().info(LOG,"duplicate reference");
                                    validationDetails.addError(ErrorType.Other,
                                                "The event reference no already exsist kindly abort and recreate the event [CM]");
                              }
                        } catch (Exception ee) {
                              ee.printStackTrace();
                              Loggers.general().info(LOG,"duplicate reference" + ee.getMessage());

                        }
                  
                  try {
                        String fxRate = getPane().getPOSTRATE();
                        if (fxRate.isEmpty() || fxRate.trim().equalsIgnoreCase("") || fxRate == null) {
                              getPostingFxrate();
                        }
                  }catch (Exception e) {

                        LOG.info("Posing FX Rate: " + e.getMessage());

                  }
                  
                  // Instrument Table Validations
                  
                  if (getMajorCode().equalsIgnoreCase("OCC") && getMinorCode().equalsIgnoreCase("CRE")) {
                        try {
                        BigDecimal instrumentamt = new BigDecimal(0);
//                          BigDecimal hundred = new BigDecimal(100);
                            BigDecimal totalamt = new BigDecimal(0);
                            String  instrumentamtccy="";
                            String colamt = getDriverWrapper().getEventFieldAsText("AMT", "v", "m").trim();
                            BigDecimal colamtct=new BigDecimal(colamt);
                            List<ExtEventInstrumentTable> liste = (List<ExtEventInstrumentTable>) getWrapper().getExtEventInstrumentTable();
                               for (int k = 0; k < liste.size(); k++) {
                                  ExtEventInstrumentTable val1 = liste.get(k);
                                  instrumentamt=val1.getINSTAMT();
                                instrumentamtccy=val1.getINSTCURR();
//                                instrumentamt=instrumentamt.divide(hundred);
                                    System.out.println("utilizeamt amount " +instrumentamt);
                                    totalamt = totalamt.add(instrumentamt);
                  }
                               if (totalamt != null  && instrumentamtccy != null && !instrumentamtccy.trim().equals("")) {
                  getPane().setINSTOTAM(totalamt.toString()+" "+instrumentamtccy);
                  }
                  if(totalamt.compareTo(colamtct)==1)
                        {
                               System.out.println("Inside if of instrument table");
                              validationDetails.addWarning(WarningType.Other,
                                          "Total instrument amount should not be greater than collection amount ");
                        }
               }
                  catch(Exception e) {
                        System.out.println("Instrument table validation " + e.getMessage());
                        e.printStackTrace();
                        
                  }
                  
                }
                  
                  
                  // try {
                  // String evvcount = getDriverWrapper().getEventFieldAsText("ESQ",
                  // "i", "");
                  // int evtno = Integer.parseInt(evvcount);
                  // List<ExtEventSERVICETAXLC> servicetax =
                  // (List<ExtEventSERVICETAXLC>) getWrapper()
                  // .getExtEventSERVICETAXLC();
                  // if (servicetax.size() > 0 && (step_Input.equalsIgnoreCase("i"))
                  // && (step_csm.equalsIgnoreCase("CBS Maker")) && evtno != 1) {
                  // if ((getMajorCode().equalsIgnoreCase("SHG") &&
                  // getMinorCode().equalsIgnoreCase("SGS"))) {
                  // validationDetails.addWarning(WarningType.Other,
                  // "If Service tax values are change in this transaction Kindly
                  // delete the service tax record in CBS Maker step and the changed
                  // lastest value will populate in Authorized step [CM]");
                  // }
                  // } else {
                  // // Loggers.general().info(LOG,"Service tax lc value" +
                  // // servicetax.size());
                  // }
                  // } catch (Exception e) {
                  // // Loggers.general().info(LOG,"Exception Service tax lc value");
                  // }

            }
      }

      public static String randomCorrelationId() {
            // Loggers.general().info(LOG,"randomCorrelationId generate");
            return UUID.randomUUID().toString();
      }

      // public boolean checkFacilityIdForCIF(Connection con, String facilityId) {
      // boolean isFacilityIdForCIF = false;
      // PreparedStatement ps = null;
      // ResultSet rs = null;
      //
      // try {
      // String priCustStr = getDriverWrapper().getEventFieldAsText("PRI", "p",
      // "no");
      // if (priCustStr != null) {
      // String facilityAvailabilityForCIFQuery = "SELECT COUNT(*) FROM
      // CUSTOMERMARGIN WHERE TRIM(CIF)='"
      // + priCustStr + "' and trim(facility)='" + facilityId + "'";
      // // //Loggers.general().info(LOG,"facilityAvailabilityForCIFQuery - " +
      // // facilityAvailabilityForCIFQuery);
      // ps = con.prepareStatement(facilityAvailabilityForCIFQuery);
      // // //Loggers.general().info(LOG,"prepared statement for margin
      // // availability - " + ps);
      // rs = ps.executeQuery();
      // int countOfFacility = 0;
      // while (rs.next()) {
      // countOfFacility = Integer.parseInt(rs.getString(1));
      // // //Loggers.general().info(LOG,"countOfFacility - " +
      // // countOfFacility);
      // }
      // if (countOfFacility > 0)
      // isFacilityIdForCIF = true;
      // }
      // } catch (Exception e) {
      // // //Loggers.general().info(LOG,"Exception occured in checkFacilityIdForCIF -
      // // " + e.getMessage());
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
      // //Loggers.general().info(LOG,"Connection Failed! Check output console");
      // e.printStackTrace();
      // }
      // }
      // return isFacilityIdForCIF;
      // }

      // public boolean checkAvailblityOfMarginForCIF(Connection con) {
      // boolean isMarginAvailable = false;
      // PreparedStatement ps = null;
      // ResultSet rs = null;
      // try {
      // String priCustStr = getDriverWrapper().getEventFieldAsText("PRI", "p",
      // "no");
      // if (priCustStr != null) {
      // String marginAvailabilityQuery = "SELECT COUNT(*) FROM CUSTOMERMARGIN
      // WHERE TRIM(CIF)='" + priCustStr
      // + "'";
      // // //Loggers.general().info(LOG,"marginAvailabilityQuery - " +
      // // marginAvailabilityQuery);
      // ps = con.prepareStatement(marginAvailabilityQuery);
      // // //Loggers.general().info(LOG,"prepared statement for margin
      // // availability - " + ps);
      // rs = ps.executeQuery();
      // int countOfMargin = 0;
      // while (rs.next()) {
      // countOfMargin = Integer.parseInt(rs.getString(1));
      // // //Loggers.general().info(LOG,"countOfMargin - " + countOfMargin);
      // }
      // if (countOfMargin > 0)
      // isMarginAvailable = true;
      // }
      // } catch (Exception e) {
      // // //Loggers.general().info(LOG,"Exception occured in
      // // checkAvailblityOfMarginForCIF - " + e.getMessage());
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
      // //Loggers.general().info(LOG,"Connection Failed! Check output console");
      // e.printStackTrace();
      // }
      // }
      //
      // return isMarginAvailable;
      // }
}