package com.misys.tiplus2.customisation.extension;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

//import org.apache.log4j.Logger;

import com.bs.theme.transport.client.ThemeTransportClient;
import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;
import com.misys.tiplus2.customisation.entity.ExtEventCounterGurantee;
import com.misys.tiplus2.customisation.entity.ExtEventLienMark;
import com.misys.tiplus2.customisation.entity.ExtEventLienMarking;
import com.misys.tiplus2.customisation.entity.ExtEventLoanDetails;
import com.misys.tiplus2.customisation.entity.ExtEventLoanDetailsEntityWrapper;
import com.misys.tiplus2.customisation.pane.EventPane;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import com.misys.tiplus2.enigma.customisation.control.ExtendedHyperlinkControlWrapper;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.ErrorType;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.WarningType;
import com.misys.tiplus2.enigma.lang.datatype.EnigmaArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.misys.tiplus2.foundations.lang.logging.Loggers;
//com.misys.tiplus2.customisation.extension.Outstanding
public class Outstanding extends ConnectionMaster {
      //private static final Logger logger = Logger.getLogger(ConnectionMaster.class.getName());
      private static final Logger LOG = LoggerFactory.getLogger(Outstanding.class);
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
                  try {

                        String FDenquiryLink = FDenquiry();
                        ExtendedHyperlinkControlWrapper fdlink = getPane().getCtlFDENQUIRYIMPSTDLCISSclayHyperlink();
                        fdlink.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink1 = getPane().getCtlFDENQUIRYIMPSTDLCADJclayHyperlink();
                        fdlink1.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink2 = getPane().getCtlFDENQUIRYIMPSTDLCAMDclayHyperlink();
                        fdlink2.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink3 = getPane().getCtlFDENQUIRYIMPSTDLCCLRECclayHyperlink();
                        fdlink3.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink4 = getPane().getCtlFDENQUIRYIMPSTALCOUTCLAclayHyperlink();
                        fdlink4.setUrl(FDenquiryLink);

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"FDenquiry link" + e.getMessage());
                  }

                  // WORKFLOW CHECKLIST

                  try {
                        String Hyperreferel12 = getWorkflow().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack1 = getPane().getCtlCSMCHECKLISTIMPSTDLCISSclayHyperlink();
                        csmreftrack1.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrack2 = getPane().getCtlCSMCHECKLISTIMPSTDLCCLRECclayHyperlink();
                        csmreftrack2.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrack3 = getPane().getCtlCSMCHECKLISTIMPSTDLCAMDclayHyperlink();
                        csmreftrack3.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrack4 = getPane().getCtlCSMCHECKLISTIMPSTDLCADJclayHyperlink();
                        csmreftrack4.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrack5 = getPane().getCtlCSMCHECKLISTISSUESHIPGROUPLayHyperlink();
                        csmreftrack5.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrack6 = getPane()
                                    .getCtlCSMCHECKLISTIMPSTALCOUTCLAclayHyperlink();
                        csmreftrack6.setUrl(Hyperreferel12);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST--------->" +
                        // ees.getMessage());
                  }

                  //For CR-143 Limit Nodes
                  try{
                        String HyperLimitNode = getLimitNode().trim();
                        ExtendedHyperlinkControlWrapper limitnode1 = getPane().getCtlUnavailablelimitnodesIMPSTDLCISSclayHyperlink();
                        limitnode1.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode2 = getPane().getCtlUnavailablelimitnodesIMPSTDLCAMDclayHyperlink();
                        limitnode2.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode3 = getPane().getCtlUnavailablelimitnodesIMPSTDLCADJclayHyperlink();
                        limitnode3.setUrl(HyperLimitNode);
                  }catch(Exception e){
                        System.out.println("For Limit Node"+e.getMessage());
                        
                  }
                  
                  // REFEREL TRACKING
                  try {

                        String Hyperreferel3 = getReferealtracking().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack = getPane().getCtlCSMREFRALIMPSTDLCISSclayHyperlink();
                        csmreftrack.setUrl(Hyperreferel3);

                        ExtendedHyperlinkControlWrapper csmreftrack2 = getPane().getCtlCSMREFRALIMPSTDLCCLRECclayHyperlink();
                        csmreftrack2.setUrl(Hyperreferel3);

                        ExtendedHyperlinkControlWrapper csmreftrack3 = getPane().getCtlCSMREFRALIMPSTDLCAMDclayHyperlink();
                        csmreftrack3.setUrl(Hyperreferel3);

                        ExtendedHyperlinkControlWrapper csmreftrack4 = getPane().getCtlCSMREFRALIMPSTDLCADJclayHyperlink();
                        csmreftrack4.setUrl(Hyperreferel3);
                        ExtendedHyperlinkControlWrapper csmreftrack5 = getPane().getCtlCSMREFRALISSUESHIPGROUPLayHyperlink();
                        csmreftrack5.setUrl(Hyperreferel3);

                        ExtendedHyperlinkControlWrapper csmreftrack6 = getPane().getCtlCSMREFRALIMPSTALCOUTCLAclayHyperlink();
                        csmreftrack6.setUrl(Hyperreferel3);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING--------->" +
                        // ees.getMessage());
                  }

                  // Workflow CBS
                  try {
                        String Hyperreferel12 = getWorkflowCBS().trim();

                        ExtendedHyperlinkControlWrapper cpcreftrack1 = getPane().getCtlCPCCHECKIMPSTDLCISSclayHyperlink();
                        cpcreftrack1.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack2 = getPane().getCtlCPCCHECKIMPSTDLCCLRECclayHyperlink();
                        cpcreftrack2.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack3 = getPane().getCtlCPCCHECKIMPSTDLCAMDclayHyperlink();
                        cpcreftrack3.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack4 = getPane().getCtlCPCCHECKIMPSTDLCADJclayHyperlink();
                        cpcreftrack4.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack5 = getPane().getCtlCPCCHECKISSUESHIPGROUPLayHyperlink();
                        cpcreftrack5.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack6 = getPane().getCtlCPCCHECKIMPSTALCOUTCLAclayHyperlink();
                        cpcreftrack6.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST WorkflowCBS--------->"
                        // + ees.getMessage());
                  }

                  // Refereal CBS
                  try {
                        String Hyperreferel12 = getReferealtrackingCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack = getPane().getCtlCPCREFERELIMPSTDLCISSclayHyperlink();
                        cpcreftrack.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack2 = getPane().getCtlCPCREFERELIMPSTDLCCLRECclayHyperlink();
                        cpcreftrack2.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack3 = getPane().getCtlCPCREFERELIMPSTDLCAMDclayHyperlink();
                        cpcreftrack3.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack4 = getPane().getCtlCPCREFERELIMPSTDLCADJclayHyperlink();
                        cpcreftrack4.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack5 = getPane().getCtlCPCREFERELISSUESHIPGROUPLayHyperlink();
                        cpcreftrack5.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrack6 = getPane().getCtlCPCREFERELIMPSTALCOUTCLAclayHyperlink();
                        cpcreftrack6.setUrl(Hyperreferel12);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING REFERE CBS--------->" +
                        // ees.getMessage());
                  }

                  // TST
                  try {
                        String val = "DMSTS";
                        String TSTHyperlink = Link(val);
                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlTSTIMPSTDLCISSclayHyperlink();
                        dmsh.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlTSTIMPSTDLCADJclayHyperlink();
                        dmsh1.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh3 = getPane().getCtlTSTIMPSTDLCAMDclayHyperlink();
                        dmsh3.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh4 = getPane().getCtlTSTIMPSTDLCCLRECclayHyperlink();
                        dmsh4.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh5 = getPane().getCtlTSTIMPSTDLCREPclayHyperlink();
                        dmsh5.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh6 = getPane().getCtlTSTEXPSTDLCOUTPREclayHyperlink();
                        dmsh6.setUrl(TSTHyperlink);

                        ExtendedHyperlinkControlWrapper dmsh7 = getPane().getCtlTSTEXPSTDLCCORESLayHyperlink();
                        dmsh7.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh8 = getPane().getCtlTSTEXPSTDLCDPclayHyperlink();
                        dmsh8.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh9 = getPane().getCtlTSTEXPORTSTANDFINANCEclayHyperlink();
                        dmsh9.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh10 = getPane().getCtlTSTEXPSTDLCAMDclayHyperlink();
                        dmsh10.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh11 = getPane().getCtlTSTEXPSTDLCADJclayHyperlink();
                        dmsh11.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh12 = getPane().getCtlTSTEXPSTDLCADVclayHyperlink();
                        dmsh12.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh13 = getPane().getCtlTSTISSUESHIPGROUPLayHyperlink();
                        dmsh13.setUrl(TSTHyperlink);

                        ExtendedHyperlinkControlWrapper dmsh14 = getPane().getCtlTSTIMPSTDLCCanLayHyperlink();
                        dmsh14.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh15 = getPane().getCtlTSTIMPSTDLCManualLayHyperlink();
                        dmsh15.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh16 = getPane().getCtlTSTIMPSTDLCBENAMDLayHyperlink();
                        dmsh16.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh17 = getPane().getCtlTSTIMPSTDLCBENCANLayHyperlink();
                        dmsh17.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh18 = getPane().getCtlTSTIMPSTDLCCORRESlayHyperlink();
                        dmsh18.setUrl(TSTHyperlink);

                        ExtendedHyperlinkControlWrapper dmsh19 = getPane().getCtlTSTEXPSTDLCcanlayHyperlink();
                        dmsh19.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh20 = getPane().getCtlTSTEXPSTDLCMANULlayHyperlink();
                        dmsh20.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh21 = getPane().getCtlTSTEXPSTDLCBENCANlayHyperlink();
                        dmsh21.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh22 = getPane().getCtlTSTEXPSTDLCMAINlayHyperlink();
                        dmsh22.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh23 = getPane().getCtlTSTEXPSTDLCBENAMDlayHyperlink();
                        dmsh23.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh24 = getPane().getCtlTSTEXPSTDLCPAYlayHyperlink();
                        dmsh24.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh25 = getPane().getCtlTSTIMPSTDLCMAINlayHyperlink();
                        dmsh25.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh26=getPane().getCtlTSTIMPSTALCOUTCLAclayHyperlink();
                        dmsh26.setUrl(TSTHyperlink);
            //          ExtendedHyperlinkControlWrapper dmsh27=getPane().getCtlTSTISBEXPLAYHyperlink();
            //          dmsh27.setUrl(TSTHyperlink);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }

                  // // SFMS
                  try {

                        // String val = "SFMS";
                        String getHyperSFMS = getIFSCSEARCH();
                        ExtendedHyperlinkControlWrapper sfmsExp = getPane().getCtlSFMSISSUESHIPGROUPLayHyperlink();
                        sfmsExp.setUrl(getHyperSFMS);
                        // ISB
                        ExtendedHyperlinkControlWrapper sfmsExpAdv = getPane().getCtlSFMSIMPSTDLCADJclayHyperlink();
                        sfmsExpAdv.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv1 = getPane().getCtlSFMSIMPSTDLCAMDclayHyperlink();
                        sfmsExpAdv1.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv2 = getPane().getCtlSFMSIMPSTDLCCLRECclayHyperlink();
                        sfmsExpAdv2.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv3 = getPane().getCtlSFMSIMPSTDLCISSclayHyperlink();
                        sfmsExpAdv3.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv4 = getPane().getCtlSFMSIMPSTDLCREPclayHyperlink();
                        sfmsExpAdv4.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv5 = getPane().getCtlSFMSIMPSTDLCCORRESlayHyperlink();
                        sfmsExpAdv5.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv6 = getPane().getCtlSFMSIMPSTALCOUTCLAclayHyperlink();
                        sfmsExpAdv6.setUrl(getHyperSFMS);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }
                  String stepID = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                  if (!stepID.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {

                        // if (getMajorCode().equalsIgnoreCase("ILC") &&
                        // getMinorCode().equalsIgnoreCase("LSC")
                        // && (stepID.equalsIgnoreCase("CSM") ||
                        // stepID.equalsIgnoreCase("CBS Maker"))) {
                        // }

                        try {

                              getPane().getExtEventLienMarkingUpdate().setEnabled(true);
                              getPane().getExtEventLienMarkingDelete().setEnabled(false);
                              getPane().getExtEventLienMarkUpdate().setEnabled(true);
                              getPane().getExtEventLienMarkDelete().setEnabled(false);

                        } catch (Exception ee) {
                              // Loggers.general().info(LOG,ee.getMessage());
                              // //Loggers.general().info(LOG,"LOB Catch");
                        } finally {
                              // //Loggers.general().info(LOG,"finally LOB ");
                        }

                        if (getMajorCode().equalsIgnoreCase("ISB")) {

                              getPeriodicAdv();

                              getChargeGurISB();

                              getPerdChgEndDate();
                              getclaimExpiryDateISB();
                        } else {
                              // Loggers.general().info(LOG," Else systemOutput");
                        }

                        if ((!getMajorCode().equalsIgnoreCase("ILC") && !getMajorCode().equalsIgnoreCase("IGT")
                                    && !getMajorCode().equalsIgnoreCase("ISB"))) {
                              // value for LOB
                              try {
                                    getLob();

                              } catch (Exception ee) {
                                    Loggers.general().info(LOG,"Exception LOB Catch" + ee.getMessage());
                              }
                        }

                        try {
                              getLOBISSUE();

                        } catch (Exception ee) {
                              Loggers.general().info(LOG,ee.getMessage());
                        }

                        // Loggers.general().info(LOG,"prodCode for chacklist------------>" +
                        // stepID);

                        try {
                              currencyCalc();
                        } catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Exception in currency value population---->" + e.getMessage());

                              }
                        }

                  }
                  
                  //======================================================================
                  String perichargval=getPane().getPERCHRAD().trim();
                Loggers.general().info(LOG,"Periodic charge in upfront "+getPane().getPERCHRAD());
            
            
            
            if((getMajorCode().equalsIgnoreCase("ISB")) && (getMinorCode().equalsIgnoreCase("IIS")))
            {
                  try{
                        
                        String d = getDriverWrapper().getEventFieldAsText("ISS", "d", "").trim();                             
                        String d1 = getDriverWrapper().getEventFieldAsText("cBKY", "d", "").trim();
                        
                        Loggers.general().info(LOG,"Issue date issue"+d);
                        Loggers.general().info(LOG,"Charge basis end date issue "+d1);
                        
                        getPane().setCHRPERAM(difference(d, d1).substring(0, difference(d, d1).indexOf(".")));
                        Loggers.general().info(LOG,"charge - issue date days issue"+getPane().getCHRPERAM());
                        
                  }
                  catch(Exception e)
                  {
                        Loggers.general().info(LOG,"Exception in calculating days issue "+e.getMessage());
                  }
            }
            if((getMajorCode().equalsIgnoreCase("ISB")) && (getMinorCode().equalsIgnoreCase("NJIS")))
            {
                  try{
                        
                        String d = getDriverWrapper().getEventFieldAsText("AMD", "d", "").trim();                             
                        String d1 = getDriverWrapper().getEventFieldAsText("cBKY", "d", "").trim();
                        
                        Loggers.general().info(LOG,"Issue date adjust"+d);
                        Loggers.general().info(LOG,"Charge basis end date adjust "+d1);
                        
                        getPane().setCHRPERAM(difference(d, d1).substring(0, difference(d, d1).indexOf(".")));
                        Loggers.general().info(LOG,"charge - issue date days adjust"+getPane().getCHRPERAM());
                        
                  }
                  catch(Exception e)
                  {
                        Loggers.general().info(LOG,"Exception in calculating days adjust "+e.getMessage());
                  }
            }
            
            
            if(perichargval.equalsIgnoreCase("Yes")){
            
            if((getMajorCode().equalsIgnoreCase("ISB")) && (getMinorCode().equalsIgnoreCase("NAIS")))
            {
                  
                  String imlAmount= getDriverWrapper().getEventFieldAsText("IML", "v", "m").trim();
                  String n = getDriverWrapper().getEventFieldAsText("cBKY", "d", "").trim();                            
                  String m = getDriverWrapper().getEventFieldAsText("cBKZ", "d", "").trim();
                  Loggers.general().info(LOG,"iml amount amend"+imlAmount);
                  Loggers.general().info(LOG,"charge end end mirror "+m);
                  Loggers.general().info(LOG,"charge end end  "+n);
            
                  
                  try{
                  SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                  
                  Date date1 = sdf1.parse(n);
                Date date2 = sdf1.parse(m);
                  
                  Loggers.general().info(LOG,"date1.compareTo(date2) "+date1.compareTo(date2));
                  if(date1.compareTo(date2) > 0){
                  try{
                        
                        String d = getDriverWrapper().getEventFieldAsText("occBKY", "d", "");                           
                        String d1 = getDriverWrapper().getEventFieldAsText("nccBKY", "d", "");
                        
                        Loggers.general().info(LOG,"Issue date amend"+d);
                        Loggers.general().info(LOG,"Charge basis end date amend "+d1);
                        
                        getPane().setCHRTENIN(difference(d, d1).substring(0, difference(d, d1).indexOf(".")));
                        Loggers.general().info(LOG,"charge - issue date days amend"+getPane().getCHRTENIN());
                        
                  }
                  catch(Exception e)
                  {
                        Loggers.general().info(LOG,"Exception in calculating days amend "+e.getMessage());
                  }
                  }
                  
                  if(imlAmount!=null && !imlAmount.equalsIgnoreCase("")){
                  
            try{
                        
                        String d = getDriverWrapper().getEventFieldAsText("AMD", "d", "");                              
                        String d1 = getDriverWrapper().getEventFieldAsText("cBKZ", "d", "");
                        
                        Loggers.general().info(LOG,"Issue date amend"+d);
                        Loggers.general().info(LOG,"Charge basis end date amend "+d1);
                        
                  
                        
                  }
                  catch(Exception e)
                  {
                        Loggers.general().info(LOG,"Exception in calculating days amend "+e.getMessage());
                  }
                  }
                  
            }catch(Exception e)
                  {
                  Loggers.general().info(LOG,"Exception in date format"+e.getMessage());
                  }
            
            
            }
            }
            }
            return true;
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

                  try {
                        // String val = "SFMS";
                        String getHyperSFMS = getIFSCSEARCH();
                        ExtendedHyperlinkControlWrapper sfmsExp = getPane().getCtlSFMSISSUESHIPGROUPLayHyperlink();
                        sfmsExp.setUrl(getHyperSFMS);
                        // ISB
                        ExtendedHyperlinkControlWrapper sfmsExpAdv = getPane().getCtlSFMSIMPSTDLCADJclayHyperlink();
                        sfmsExpAdv.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv1 = getPane().getCtlSFMSIMPSTDLCAMDclayHyperlink();
                        sfmsExpAdv1.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv2 = getPane().getCtlSFMSIMPSTDLCCLRECclayHyperlink();
                        sfmsExpAdv2.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv3 = getPane().getCtlSFMSIMPSTDLCISSclayHyperlink();
                        sfmsExpAdv3.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv4 = getPane().getCtlSFMSIMPSTDLCREPclayHyperlink();
                        sfmsExpAdv4.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv5 = getPane().getCtlSFMSIMPSTDLCCORRESlayHyperlink();
                        sfmsExpAdv5.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv6 = getPane().getCtlSFMSIMPSTALCOUTCLAclayHyperlink();
                        sfmsExpAdv6.setUrl(getHyperSFMS);

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"FDenquiry link" + e.getMessage());
                  }

                  try {

                        String FDenquiryLink = FDenquiry();
                        ExtendedHyperlinkControlWrapper fdlink = getPane().getCtlFDENQUIRYIMPSTDLCISSclayHyperlink();
                        fdlink.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink1 = getPane().getCtlFDENQUIRYIMPSTDLCADJclayHyperlink();
                        fdlink1.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink2 = getPane().getCtlFDENQUIRYIMPSTDLCAMDclayHyperlink();
                        fdlink2.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink3 = getPane().getCtlFDENQUIRYIMPSTDLCCLRECclayHyperlink();
                        fdlink3.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink4 = getPane().getCtlFDENQUIRYIMPSTALCOUTCLAclayHyperlink();
                        fdlink4.setUrl(FDenquiryLink);

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"FDenquiry link" + e.getMessage());
                  }

                  // WORKFLOW CHECKLIST

                  try {
                        String Hyperreferel12 = getWorkflow().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack1 = getPane().getCtlCSMCHECKLISTIMPSTDLCISSclayHyperlink();
                        csmreftrack1.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrack2 = getPane().getCtlCSMCHECKLISTIMPSTDLCCLRECclayHyperlink();
                        csmreftrack2.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrack3 = getPane().getCtlCSMCHECKLISTIMPSTDLCAMDclayHyperlink();
                        csmreftrack3.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrack4 = getPane().getCtlCSMCHECKLISTIMPSTDLCADJclayHyperlink();
                        csmreftrack4.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrack5 = getPane().getCtlCSMCHECKLISTISSUESHIPGROUPLayHyperlink();
                        csmreftrack5.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrack6 = getPane()
                                    .getCtlCSMCHECKLISTIMPSTALCOUTCLAclayHyperlink();
                        csmreftrack6.setUrl(Hyperreferel12);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST--------->" +
                        // ees.getMessage());
                  }
                  

                  //For CR-143 Limit Nodes
                  try{
                        String HyperLimitNode = getLimitNode().trim();
                        ExtendedHyperlinkControlWrapper limitnode1 = getPane().getCtlUnavailablelimitnodesIMPSTDLCISSclayHyperlink();
                        limitnode1.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode2 = getPane().getCtlUnavailablelimitnodesIMPSTDLCAMDclayHyperlink();
                        limitnode2.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode3 = getPane().getCtlUnavailablelimitnodesIMPSTDLCADJclayHyperlink();
                        limitnode3.setUrl(HyperLimitNode);
                  }catch(Exception e){
                        System.out.println("For Limit Node"+e.getMessage());
                        
                  }

                  // REFEREL TRACKING
                  try {

                        String Hyperreferel3 = getReferealtracking().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack = getPane().getCtlCSMREFRALIMPSTDLCISSclayHyperlink();
                        csmreftrack.setUrl(Hyperreferel3);

                        ExtendedHyperlinkControlWrapper csmreftrack2 = getPane().getCtlCSMREFRALIMPSTDLCCLRECclayHyperlink();
                        csmreftrack2.setUrl(Hyperreferel3);

                        ExtendedHyperlinkControlWrapper csmreftrack3 = getPane().getCtlCSMREFRALIMPSTDLCAMDclayHyperlink();
                        csmreftrack3.setUrl(Hyperreferel3);

                        ExtendedHyperlinkControlWrapper csmreftrack4 = getPane().getCtlCSMREFRALIMPSTDLCADJclayHyperlink();
                        csmreftrack4.setUrl(Hyperreferel3);
                        ExtendedHyperlinkControlWrapper csmreftrack5 = getPane().getCtlCSMREFRALISSUESHIPGROUPLayHyperlink();
                        csmreftrack5.setUrl(Hyperreferel3);

                        ExtendedHyperlinkControlWrapper csmreftrack6 = getPane().getCtlCSMREFRALIMPSTALCOUTCLAclayHyperlink();
                        csmreftrack6.setUrl(Hyperreferel3);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING--------->" +
                        // ees.getMessage());
                  }

                  // Workflow CBS
                  try {
                        String Hyperreferel12 = getWorkflowCBS().trim();

                        ExtendedHyperlinkControlWrapper cpcreftrack1 = getPane().getCtlCPCCHECKIMPSTDLCISSclayHyperlink();
                        cpcreftrack1.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack2 = getPane().getCtlCPCCHECKIMPSTDLCCLRECclayHyperlink();
                        cpcreftrack2.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack3 = getPane().getCtlCPCCHECKIMPSTDLCAMDclayHyperlink();
                        cpcreftrack3.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack4 = getPane().getCtlCPCCHECKIMPSTDLCADJclayHyperlink();
                        cpcreftrack4.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack5 = getPane().getCtlCPCCHECKISSUESHIPGROUPLayHyperlink();
                        cpcreftrack5.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack6 = getPane().getCtlCPCCHECKIMPSTALCOUTCLAclayHyperlink();
                        cpcreftrack6.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST WorkflowCBS--------->"
                        // + ees.getMessage());
                  }

                  // Refereal CBS
                  try {
                        String Hyperreferel12 = getReferealtrackingCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack = getPane().getCtlCPCREFERELIMPSTDLCISSclayHyperlink();
                        cpcreftrack.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack2 = getPane().getCtlCPCREFERELIMPSTDLCCLRECclayHyperlink();
                        cpcreftrack2.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack3 = getPane().getCtlCPCREFERELIMPSTDLCAMDclayHyperlink();
                        cpcreftrack3.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack4 = getPane().getCtlCPCREFERELIMPSTDLCADJclayHyperlink();
                        cpcreftrack4.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack5 = getPane().getCtlCPCREFERELISSUESHIPGROUPLayHyperlink();
                        cpcreftrack5.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrack6 = getPane().getCtlCPCREFERELIMPSTALCOUTCLAclayHyperlink();
                        cpcreftrack6.setUrl(Hyperreferel12);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING REFERE CBS--------->" +
                        // ees.getMessage());
                  }

                  //  on Sep 20 Counter Gurantee
                  String step_Input = getDriverWrapper().getEventFieldAsText("CSTY", "s", "");
                  if (getMajorCode().equalsIgnoreCase("ISB")) {
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
                              Loggers.general().info(LOG,"exception " + e.getMessage());
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
                               Loggers.general().info(LOG,"Exception caught on branch code validation......" + e.getMessage());
                        }

                  }

                  // Todo on Sep 17 for IFSC
                  // String step_Input =
                  // getDriverWrapper().getEventFieldAsText("CSTY", "s", "");

                  String seifsc = getWrapper().getSENIFSC().trim();
                  // //Loggers.general().info(LOG,"IFSC Code"+Ifsc);

                  if (seifsc.length() > 0 && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))
                              && getMajorCode().equalsIgnoreCase("ISB")) {
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

                  if (recifsc.length() > 0 && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))
                              && getMajorCode().equalsIgnoreCase("ISB")) {
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

                  // String step_Input =
                  // getDriverWrapper().getEventFieldAsText("CSTY", "s", "");
                  // Loggers.general().info(LOG,"step_Input check ----->" + step_Input);

                  // // KYC DATE VALIDATION
                  // try {
                  //
                  // //Loggers.general().info(LOG,"KYC Date Validation");
                  // String t = getDriverWrapper().getEventFieldAsText("PRI", "p",
                  // "cBEY");
                  // //Loggers.general().info(LOG,"value os first date " + t);
                  // String i = getDriverWrapper().getEventFieldAsText("ISS", "d",
                  // "");
                  // //Loggers.general().info(LOG,"value of issue date " + i);
                  // SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                  // Date date1 = sdf.parse(t.trim());
                  // Date date2 = sdf.parse(i.trim());
                  // if (date1.compareTo(date2) < 0
                  // && getDriverWrapper().getEventFieldAsText("CSID", "s",
                  // "").equalsIgnoreCase("input")) {
                  // validationDetails.addWarning(ValidationDetails.WarningType.Other,
                  // "KYC Expired for the Customer [CM]");
                  // }
                  // } catch (Exception e) {
                  // //Loggers.general().info(LOG,"Exception " + e.getMessage());
                  // }
//                try {
//                      currencyCalc();
//                } catch (Exception e) {
//                      if (dailyval_Log.equalsIgnoreCase("YES")) {
//                            Loggers.general().info(LOG,"Exception in currency value population---->" + e.getMessage());
//
//                      }
            //    }
                  // FCY Tax calculation
                  try {
                        getPane().getExtEventLienMarkingDelete().setEnabled(false);
                        getPane().getExtEventLienMarkDelete().setEnabled(false);
                        getFCCTCALCULATION();
                        getFcctDetails(validationDetails);
                        System.out.println("outside fcct details");
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Exception update" +
                        // e.getMessage());
                  }
                  try {

                        getutrNoGenerated();
                  } catch (Exception ee) {
                        Loggers.general().info(LOG,"UTR Number getutrNoGenerated" + ee.getMessage());

                  }

                  if ((!getMajorCode().equalsIgnoreCase("ILC") && !getMajorCode().equalsIgnoreCase("IGT")
                              && !getMajorCode().equalsIgnoreCase("ISB"))) {
                        // value for LOB
                        try {
                              getLob();

                        } catch (Exception ee) {
                              Loggers.general().info(LOG,"Exception LOB Catch" + ee.getMessage());
                        }
                  }

                  try {
                        getLOBISSUE();

                  } catch (Exception ee) {
                        Loggers.general().info(LOG,ee.getMessage());
                  }

                  if (getMajorCode().equalsIgnoreCase("ISB")) {

                        getPeriodicAdv();

                        String facid = getWrapper().getFACLTYID().trim();

                        if (facid == null || facid.equalsIgnoreCase("")) {
                              getPane().setPMARGIN("");
                              getPane().setMARAMT("");
                        }

                  }

                  if (getMajorCode().equalsIgnoreCase("ISB")) {

                        getChargeGurISB();

                        getPerdChgEndDate();
                        getclaimExpiryDateISB();

                  } else {
                        // Loggers.general().info(LOG," Else systemOutput");
                  }

                  // LOG.info("Outstanding Call Outstanding");
                  String usd = "", inr = "", conv = "";
                  // inr=getDriverWrapper().getCurrencyAtFXRate("","","!PS","");
                  inr = getDriverWrapper().getEventFieldAsText("FXD", "*", "");
                  usd = getDriverWrapper().getEventFieldAsText("", "*", "");
                  conv = getDriverWrapper().getEventFieldAsText("FXS", "*", "");
                  // //Loggers.general().info(LOG,inr + "INR result");
                  // //Loggers.general().info(LOG,usd + "USD currency");
                  try {
                        // Loggers.general().info(LOG,conv + "conv");
                        // inr = getDriverWrapper().convertAmount(currency2,"INR","");
                        // //Loggers.general().info(LOG,inr);
                        // usd =
                        // getDriverWrapper().getCurrencyAtFXRate("USD",inr,"GENL","");
                        // fxRatecode=getpane();
                        // Loggers.general().info(LOG,usd);
                        // getWrapper().setRAMOT_Name(inr);
                        // getWrapper().setCRAMT_Name(conv);
                        // getWrapper().setREUSD_Name(usd);

                  } catch (Exception e) {
                        e.printStackTrace();
                  }

                  // Category code populate based on input branch
                  try {
                        String BranchCode = getDriverWrapper().getEventFieldAsText("BIN", "b", "c");
                        con = ConnectionMaster.getConnection();
                        if (!(BranchCode.length() == 0)) {
                              String sql6 = "select telex from capf where cabrnm='" + BranchCode + "'";
                              // Loggers.general().info(LOG,"BranchCode Query - " + sql6);
                              ps1 = con.prepareStatement(sql6);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    String inmt = rs1.getString(1);
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

                  // Charge Account Validation
                  String prodtype = getDriverWrapper().getEventFieldAsText("PCO", "s", "");// pROD
                                                                                                                                    // CODE
                                                                                                                                    // ILC

                  String masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
                  String cust = "";

                  if (prodtype.trim().equalsIgnoreCase("ESB")) {
                        // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                        cust = getDriverWrapper().getEventFieldAsText("BEN", "p", "no").trim();// party
                                                                                                                                    // id
                  }

                  if (prodtype.trim().equalsIgnoreCase("ISB")) {
                        // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                        cust = getDriverWrapper().getEventFieldAsText("APP", "p", "no").trim();// party
                                                                                                                                    // id
                  }
                  // String cust = getDriverWrapper().getEventFieldAsText("PRM",
                  // "p", "no").trim();
                  // //Loggers.general().info(LOG,"Primary customer taking ----> " + cust);
                  String eventREF = getDriverWrapper().getEventFieldAsText("MEVR", "r", "").trim();
                  String acctno = getDriverWrapper().getEventFieldAsText("CHA", "a", "").trim();
                  // Loggers.general().info(LOG,"charge account in event field======>" +
                  // acctno);
                  String chargecol = getDriverWrapper().getEventFieldAsText("BOTC", "l", "");
                  // Loggers.general().info(LOG,"charge account collected " + chargecol);
                  String custval = "";
                  try {
                        if (step_csm.equalsIgnoreCase("CBS Maker 1")) {

                              con = getConnection();

                              String dmstlmt = "select TRIM(CUS_MNM) from ETT_CUS_ACCT_SETTLE where MASTER_REF ='" + masterref
                                          + "' and EVENT_REF = '" + eventREF + "'";
                              dmsp = con.prepareStatement(dmstlmt);

                              dmsr = dmsp.executeQuery();
                              while (dmsr.next()) {

                                    custval = dmsr.getString(1);

                                    if (custval.length() > 0 && chargecol.equalsIgnoreCase("Y")
                                                && (!chargecol.equalsIgnoreCase("N"))) {

                                          // Loggers.general().info(LOG,"custoemr number in query" +
                                          // custval);
                                          // Loggers.general().info(LOG,"custoemr number in
                                          // transaction" + cust);
                                          validationDetails.addWarning(WarningType.Other,
                                                      "Account selected in Settlement for charges does not belong to the Applicant  [CM]");
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

                                                validationDetails.addWarning(ValidationDetails.WarningType.Other,
                                                            "Account selected for charges does not belong to the Applicant  [CM]");

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

                  // try {
                  //
                  // String dms = "select * from ETTV_BOE_PENDING_VIEW where CIF='" +
                  // cust + "'";
                  // if (dailyval_Log.equalsIgnoreCase("YES")) {
                  // Loggers.general().info(LOG,"ETTV_BOE_PENDING_VIEW QUERY ----> " + dms);
                  // }
                  // con = getConnection();
                  // ps1 = con.prepareStatement(dms);
                  //
                  // rs1 = ps1.executeQuery();
                  // while (rs1.next()) {
                  //
                  // if ((step_Input.equalsIgnoreCase("i")) &&
                  // (step_csm.equalsIgnoreCase("CSM")
                  // || step_csm.equalsIgnoreCase("CBS Maker") ||
                  // step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                  //
                  // validationDetails.addWarning(WarningType.Other, "Bill of entry
                  // pending for this client [CM]");
                  //
                  // } else {
                  // if (dailyval_Log.equalsIgnoreCase("YES")) {
                  // Loggers.general().info(LOG,"Bill of entry pending for this client else
                  // ----> ");
                  // }
                  // }
                  //
                  // }
                  //
                  // } catch (Exception e) {
                  // // Loggers.general().info(LOG,"charge account collected----->" +
                  // // e.getMessage());
                  // } finally {
                  // try {
                  // if (rs1 != null)
                  // rs1.close();
                  // if (ps1 != null)
                  // ps1.close();
                  // if (con != null)
                  // con.close();
                  // } catch (SQLException e) {
                  // // Loggers.general().info(LOG,"Connection Failed! Check output
                  // // console");
                  // e.printStackTrace();
                  // }
                  // }

                  // NPA customer

                  try {

                        String productVal = "N";

                        if (prodtype.trim().equalsIgnoreCase("ESB")) {
                              // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                              productVal = getDriverWrapper().getEventFieldAsText("BEN", "p", "cAJB").trim();// party
                              // id
                        }

                        if (prodtype.trim().equalsIgnoreCase("ISB")) {
                              // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                              productVal = getDriverWrapper().getEventFieldAsText("APP", "p", "cAJB").trim();// party
                              // id
                        }
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"NPA customer-----> " + productVal);
                        }
                        if (productVal.equalsIgnoreCase("Y") && (step_Input.equalsIgnoreCase("i"))
                                    && (step_csm.equalsIgnoreCase("AdhocCSM") || step_csm.equalsIgnoreCase("CSM")
                                                || step_csm.equalsIgnoreCase("CBS Maker")
                                                || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                              validationDetails.addWarning(WarningType.Other, "Customer is a NPA customer [CM]");
                        } else {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Not a NPA customer-----> " + productVal);
                              }

                        }

                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception NPA customer-----> " + e.getMessage());
                        }
                  }

                  // Preshipment value populated

                  // List<ExtEventLoanDetails> preship = (List<ExtEventLoanDetails>)
                  // getWrapper().getExtEventLoanDetails();
                  // String currency = "";
                  // String amt = "";
                  // if (preship.size() < 5) {
                  // getPane().setTOTUSD(0 + " USD");
                  // getPane().setTOTINR(0 + " INR");
                  // getPane().setTOTEUR(0 + " EUR");
                  // getPane().setTOTJPY(0 + " JPY");
                  // getPane().setTOTGBP(0 + " GBP");
                  // }

                  // for (int l = 0; l < preship.size(); l++) {
                  // // for(ExtEventLoanDetails preshipment : preship){
                  // ExtEventLoanDetails preshipment = preship.get(l);
                  //
                  // // //Loggers.general().info(LOG,"fIRST VALUE ==>" +
                  // // preshipment.toString());
                  //
                  // currency = preshipment.getREAMOUNTCurrency();
                  // // //Loggers.general().info(LOG,"PreShipment value in currency" +
                  // // currency);
                  // amt = preshipment.getREAMOUNT().toString();
                  // // //Loggers.general().info(LOG,"PreShipment value in amount" + amt);
                  // if (currency.equalsIgnoreCase("USD")) {
                  // // //Loggers.general().info(LOG,"PreShipment value in if loop amount
                  // // and currency USD" + amt + currency);
                  // getPane().setTOTUSD(amt + " USD");
                  // } else {
                  // // getPane().setTOTUSD(0 + " USD");
                  // //Loggers.general().info(LOG,"USD---------->");
                  // }
                  // if (currency.equalsIgnoreCase("INR")) {
                  // // //Loggers.general().info(LOG,"PreShipment value in if loop amount
                  // // and currency INR" + amt + currency);
                  // getPane().setTOTINR(amt + " INR");
                  // } else {
                  // // getPane().setTOTINR(0 + " INR");
                  // //Loggers.general().info(LOG,"INR---------->");
                  // }
                  // if (currency.equalsIgnoreCase("JPY")) {
                  // // //Loggers.general().info(LOG,"PreShipment value in if loop amount
                  // // and currency JPY" + amt + currency);
                  // getPane().setTOTJPY(amt + " JPY");
                  // } else {
                  //
                  // // getPane().setTOTJPY(0 + " JPY");
                  // //Loggers.general().info(LOG,"JPY---------->");
                  // }
                  // if (currency.equalsIgnoreCase("EUR")) {
                  // // //Loggers.general().info(LOG,"PreShipment value in if loop amount
                  // // and currency EUR" + amt + currency);
                  // getPane().setTOTEUR(amt + " EUR");
                  // } else {
                  // // getPane().setTOTEUR(0 + " EUR");
                  // //Loggers.general().info(LOG,"EUR---------->");
                  // }
                  // if (currency.equalsIgnoreCase("GBP")) {
                  // // //Loggers.general().info(LOG,"PreShipment value in if loop amount
                  // // and currency GBP" + amt + currency);
                  // getPane().setTOTGBP(amt + " GBP");
                  // } else {
                  // // getPane().setTOTGBP(0 + " GBP");
                  // //Loggers.general().info(LOG,"GBP---------->");
                  // }
                  //
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
                        // Loggers.general().info(LOG,"Preshipment value checking====> 1" +
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
                        // Loggers.general().info(LOG,"Preshipment value checking====> 2" +
                        // inrCheck);
                        if (!inrCheck) {
                              getPane().setTOTINR("0.0 INR");
                        }
                        // Loggers.general().info(LOG,"Preshipment value checking====> 3" +
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

                  // LIEN VALIDATION
                  String productp = getDriverWrapper().getEventFieldAsText("PCO", "s", "").trim();
                  // String stepcd = getDriverWrapper().getEventFieldAsText("ECOI",
                  // "s", "");

                  // String step_Input =
                  // getDriverWrapper().getEventFieldAsText("CSTY", "s", "");
                  // Loggers.general().info(LOG,"step_Input check ----->" + step_Input);
                  // String step_csm = getDriverWrapper().getEventFieldAsText("CSID",
                  // "s", "");
                  // Loggers.general().info(LOG,"step id check for CSM ----->" + step_csm);
                  String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim(); //
                  double marginB = 0.0;
                  if (productp.equalsIgnoreCase("ISB") && (step_Input.equalsIgnoreCase("i"))) {
                        try {

                              Connection con = ConnectionMaster.getConnection();
                              boolean isMarginAvailableForCIF = checkAvailblityOfMarginForCIF(con);
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
                                    // //Loggers.general().info(LOG," & marginAmt - " + marginAmt);
                                    String liensta = eventLien.getLINEST();
                                    dopNo = eventLien.getDEPOSTNO().trim();
                                    // Loggers.general().info(LOG,"Lien status for mark----> " +
                                    // liensta);
                                    double clearB = 0.0;

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

                                    // //Loggers.general().info(LOG," & marginB - " + marginB);
                                    // if (marginB > clearB &&
                                    // stepcd.equalsIgnoreCase("Input")) {
                                    // String marginErrorStr = "Margin amount entered should
                                    // not be greater than the clear balance for the deposit
                                    // number [CM] "
                                    // + eventLien.getDEPOSTNO();
                                    // validationDetails.addWarning(ValidationDetails.WarningType.Other,
                                    // marginErrorStr);
                                    // }

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
                                          // Loggers.general().info(LOG,"lien status array list
                                          // arr---->" + arr);

                                    } else {
                                          // Loggers.general().info(LOG,"lien status in else
                                          // loop---->" + liensta);
                                    }
                              }

                              String evnt = getDriverWrapper().getEventFieldAsText("EPF", "s", "");
                              String evvcount = getDriverWrapper().getEventFieldAsText("ESQ", "i", "");
                              if (step_csm.equalsIgnoreCase("CBS Maker 1") && ExtEventLienMark.size() > 0) {
                                    try {

                                          String query = "SELECT count(*),lmg.DEPOSTNO,lmg.LINEST FROM MASTER mas, BASEEVENT bas, EXTEVENTLMG lmg WHERE mas.KEY97 =bas.MASTER_KEY AND bas.EXTFIELD =lmg.FK_EVENT AND lmg.DEPOSTNO='"
                                                      + dopNo + "' AND lmg.LINEST ='MARK SUCCEEDED' AND mas.MASTER_REF ='"
                                                      + MasterReference + "' AND bas.REFNO_PFIX ='" + evnt + "' AND bas.REFNO_SERL="
                                                      + evvcount + " group by lmg.DEPOSTNO,lmg.LINEST";
                                          Loggers.general().info(LOG,"lien status check---->" + query);
                                          con = getConnection();
                                          ps1 = con.prepareStatement(query);
                                          rs1 = ps1.executeQuery();
                                          while (rs1.next()) {
                                                counterVal = rs1.getInt(1);
                                                Loggers.general().info(LOG,"lien counterVal in while---->" + counterVal);

                                          }
                                    } catch (Exception e) {
                                          Loggers.general().info(LOG,"lien account===>" + e.getMessage());
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

                                    if (ExtEventLienMark.size() > 0 && counterVal > 1
                                                && (step_csm.equalsIgnoreCase("CBS Maker 1"))) {

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
                                                String responseXML = anThemeTransportClient.invoke("Customization", "FDLienAdd",
                                                            finalXml);
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
                                                            // Loggers.general().info(LOG,"Lien mark Amount"
                                                            // + lienAmt);
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
                                                            // double lienAmt =
                                                            // Double.valueOf(lienStr);

                                                            if (depositNum.equalsIgnoreCase(currentDepositNum)
                                                                        && markValue.equalsIgnoreCase("MARK SUCCEEDED")
                                                                        && lienbox.equalsIgnoreCase("MARK SUCCEEDED")) {
                                                                  if (!lienAmt.equalsIgnoreCase(responseAmt)) {
                                                                        validationDetails.addError(ErrorType.Other,
                                                                                    "For Account no (" + depositNum
                                                                                                + "), lien is marked for amount (" + responseAmt
                                                                                                + ") in finacle. Kindly update the lien amount");
                                                                  }

                                                                  else {

                                                                        Loggers.general().info(LOG,"lien and response amount is same else" + lienAmt
                                                                                    + "responseAmt" + responseAmt);
                                                                  }
                                                            } else {
                                                                  Loggers.general().info(LOG,"lien depositNum else" + depositNum
                                                                              + "currentDepositNum===>" + currentDepositNum);
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
                                    Loggers.general().info(LOG,"marksucceed=======>" + dob);
                              }

                              Loggers.general().info(LOG,"marksucceed=======>" + dob1);
                              Loggers.general().info(LOG,"arr.size() =======>" +arr.size() );
                              
                              String finalpayment=getDriverWrapper().getEventFieldAsText("FNP","l", "").trim();                     
                              Loggers.general().info(LOG,"Final payment "+finalpayment);
                              
                              String availbleamt=getDriverWrapper().getEventFieldAsText("AMPR", "v", "m");
                              Loggers.general().info(LOG,"Available amount "+availbleamt);
                              
                              
                              String maximumamt=getDriverWrapper().getEventFieldAsText("MAL", "v", "m");
                              Loggers.general().info(LOG,"Presented amount "+maximumamt);

                              if (arr.size() > 0 && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))
                                          &&( getMinorCode().equalsIgnoreCase("OIS"))&&((finalpayment.equalsIgnoreCase("Y"))||(maximumamt.equalsIgnoreCase("0.00")))) {
                                    Loggers.general().info(LOG,"Marginamt_log for -----> " +
                                     dob1);
                                     validationDetails.addWarning(WarningType.Other,
                                     "Total Lien Mark amount is (" + dob1 + " INR), Please Release the Lien [CM]");
                              } else {
                                     Loggers.general().info(LOG,"Lien mark amount in else" +
                                     dob1);
                              }

                              if (marDob > 0 && ExtEventLienMark.size() == 0 && (step_Input.equalsIgnoreCase("i"))
                                          && (step_csm.equalsIgnoreCase("CBS Maker"))
                                          && (getMinorCode().equalsIgnoreCase("IIS") || getMinorCode().equalsIgnoreCase("NAIS")
                                                      || getMinorCode().equalsIgnoreCase("NJIS"))) {
                                    // //Loggers.general().info(LOG,"ExtEventLienMark ILC--> " +
                                    // ExtEventLienMark + " & marginAmt ILC ---> " +
                                    // marDob);
                                    validationDetails.addWarning(WarningType.Other,
                                                "Lien amount is calculated and no Lien Amount is entered in FD lien grid  [CM]");
                              } else {
                                    // Loggers.general().info(LOG," FD lien grid is entered----->");

                              }

                              // //Loggers.general().info(LOG,"totalMargin - "+totalMargin);
                              String expectedMarginStr = getDriverWrapper().getEventFieldAsText("cAAR", "v", "m").trim();
                              double expectedMarginAmt = 0.0;
                              Loggers.general().info(LOG,"expectedMarginStr - " +
                                          expectedMarginStr);
                              if (expectedMarginStr.length() != 0){
                                    expectedMarginAmt = Double.parseDouble(expectedMarginStr);
                              Loggers.general().info(LOG,"expectedMarginAmt - " +
                               expectedMarginAmt);
                              }
                              Loggers.general().info(LOG,"expectedMarginAmt --------- " +
                                           expectedMarginAmt);
                              Loggers.general().info(LOG,"totalMargin--------- " +
                                          totalMargin);
                              Loggers.general().info(LOG,"ExtEventLienMark.size() --------- " +
                                          ExtEventLienMark.size());
                              if ((ExtEventLienMark.size() > 0) && (totalMargin != expectedMarginAmt)
                                          && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                    validationDetails.addWarning(WarningType.Other,
                                                "Sum of margin amount should be equal to Required Lien Amount  [CM]");
                              }

                              if (isMarginAvailableForCIF && (expectedMarginAmt < 0.0) && (step_Input.equalsIgnoreCase("i"))
                                          && (step_csm.equalsIgnoreCase("CBS Maker"))) {

                                    // validationDetails.addWarning(WarningType.Other,
                                    // "Expected
                                    // margin amount field is empty, please input facility
                                    // Id to
                                    // calculate the same [CM]");
                              }
                        }

                        catch (Exception ee) {
                              // Loggers.general().info(LOG,ee.getMessage());

                        }
                  }

                  if (productp.equalsIgnoreCase("LSC") && (step_Input.equalsIgnoreCase("i")))

                  {

                        try {

                              Connection con = ConnectionMaster.getConnection();
                              boolean isMarginAvailableForCIF = checkAvailblityOfMarginForCIF(con);
                              String margin = getWrapper().getMARAMT();
                              if (margin == null) {
                                    margin = "0";
                              }
                              // //Loggers.general().info(LOG,"Current margin amount " +
                              // margin);
                              double marDob = Double.valueOf(margin);
                              // calculateMargin();
                              List<ExtEventLienMark> ExtEventLienMark = (List<ExtEventLienMark>) getWrapper()
                                          .getExtEventLienMark();
                              // //Loggers.general().info(LOG,"ExtEventLienMark ILC------> " +
                              // ExtEventLienMark.size());
                              double totalMargin = 0.0;
                              String dopNo = "";
                              int counterVal = 0;
                              ArrayList<Double> arr = new ArrayList<Double>();
                              for (int l = 0; l < ExtEventLienMark.size(); l++) {
                                    ExtEventLienMark eventLien = (ExtEventLienMark) ExtEventLienMark.get(l);
                                    // BigDecimal clearBal = eventLien.getCLEARBLC();
                                    BigDecimal marginAmt = eventLien.getMARGAM();
                                    // //Loggers.general().info(LOG," & marginAmt - " +
                                    // marginAmt);
                                    String liensta = eventLien.getLINESTS();
                                    dopNo = eventLien.getDEPOSNO().trim();
                                    // Loggers.general().info(LOG,"Lien status for mark----> " +
                                    // liensta);
                                    double clearB = 0.0;

                                    if (ExtEventLienMark.size() > 0 && (liensta.equalsIgnoreCase("") || liensta == null)
                                                && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                          validationDetails.addError(ErrorType.Other,
                                                      "Lien amount is available please Mark the lien [CM]");
                                    }

                                    double marginDob = eventLien.getMARGAM().doubleValue();
                                    if ((ExtEventLienMark.size() > 0) && (marginDob == 0.0 || marginDob == 0 || marginDob < 1)
                                                && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                          validationDetails.addError(ErrorType.Other, "Lien amount should be greater than Zero [CM]");
                                          EventPane pane = (EventPane) getPane();
                                          pane.getExtEventLienMarkUpdate().setEnabled(true);
                                    }

                                    if (marginAmt != null)
                                          marginB = Double.valueOf(marginAmt.doubleValue() / 100);

                                    // //Loggers.general().info(LOG," & marginB - " + marginB);
                                    // if (marginB > clearB &&
                                    // stepcd.equalsIgnoreCase("Input")) {
                                    // String marginErrorStr = "Margin amount entered
                                    // should
                                    // not be greater than the clear balance for the
                                    // deposit
                                    // number [CM] "
                                    // + eventLien.getDEPOSTNO();
                                    // validationDetails.addWarning(ValidationDetails.WarningType.Other,
                                    // marginErrorStr);
                                    // }

                                    totalMargin += marginB;
                                    if (totalMargin > marDob && (step_Input.equalsIgnoreCase("i"))
                                                && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                          // //Loggers.general().info(LOG,"totalMargin -----> " +
                                          // totalMargin);
                                          validationDetails.addWarning(WarningType.Other,
                                                      "Grid Margin amount should not greater then the Margin Amount  [CM]");
                                    } else {
                                          // Loggers.general().info(LOG,"totalMargin is less then
                                          // the
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
                                          // Loggers.general().info(LOG,"lien status array list
                                          // arr---->" + arr);

                                    } else {
                                          // Loggers.general().info(LOG,"lien status in else
                                          // loop---->" + liensta);
                                    }
                              }

                              String evnt = getDriverWrapper().getEventFieldAsText("EPF", "s", "");
                              String evvcount = getDriverWrapper().getEventFieldAsText("ESQ", "i", "");
                              if (step_csm.equalsIgnoreCase("CBS Maker 1") && ExtEventLienMark.size() > 0) {
                                    try {

                                          String query = "SELECT count(*),lmg.DEPOSNO,lmg.LINESTS FROM MASTER mas, BASEEVENT bas, EXTEVENTLMK lmg WHERE mas.KEY97 =bas.MASTER_KEY AND bas.EXTFIELD =lmg.FK_EVENT AND lmg.DEPOSNO='"
                                                      + dopNo + "' AND lmg.LINESTS ='MARK SUCCEEDED' AND mas.MASTER_REF ='"
                                                      + MasterReference + "' AND bas.REFNO_PFIX ='" + evnt + "' AND bas.REFNO_SERL="
                                                      + evvcount + " group by lmg.DEPOSNO,lmg.LINESTS";
                                          Loggers.general().info(LOG,"lien status check---->" + query);
                                          con = getConnection();
                                          ps1 = con.prepareStatement(query);
                                          rs1 = ps1.executeQuery();
                                          while (rs1.next()) {
                                                counterVal = rs1.getInt(1);
                                                Loggers.general().info(LOG,"lien counterVal in while---->" + counterVal);

                                          }
                                    } catch (Exception e) {
                                          Loggers.general().info(LOG,"lien account===>" + e.getMessage());
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

                                    if (ExtEventLienMark.size() > 0 && counterVal > 1
                                                && (step_csm.equalsIgnoreCase("CBS Maker 1"))) {

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
                                                List<ExtEventLienMark> ExtEventLien = (List<ExtEventLienMark>) getWrapper()
                                                            .getExtEventLienMark();
                                                for (ExtEventLienMark fdwrapper : ExtEventLien) {
                                                      BigDecimal hundred = new BigDecimal(100);
                                                      BigDecimal margin_Amt = fdwrapper.getMARGAM();
                                                      BigDecimal margin_Amount = margin_Amt.divide(hundred);
                                                      DecimalFormat diff = new DecimalFormat("0.00");
                                                      diff.setMaximumFractionDigits(2);
                                                      String marginAmount = diff.format(margin_Amount);
                                                      String marginAcc = fdwrapper.getDEPOSNO().trim();
                                                      String LienID = fdwrapper.getLIENIDN().trim();
                                                      String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                                                      String eventRefNo = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
                                                      String issueDate = getDriverWrapper().getEventFieldAsText("ISS", "d", "");
                                                      String mas_evnt = masRefNo;

                                                      String behalfBranch = getDriverWrapper().getEventFieldAsText("BOB", "s", "");
                                                      String mas_evntRef = masRefNo;
                                                      String lienbox = fdwrapper.getLINESTS();
                                                      if (!lienbox.equalsIgnoreCase("") && lienbox != null
                                                                  && lienbox.equalsIgnoreCase("MARK SUCCEEDED")) {
                                                            // Loggers.general().info(LOG,"Lien Status
                                                            // is
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
                                                String responseXML = anThemeTransportClient.invoke("Customization", "FDLienAdd",
                                                            finalXml);
                                                Loggers.general().info(LOG,"Final Themebridge responseXML for enquiry==========>" + responseXML);
                                                // EnigmaArray<ExtEventLienMarkingEntityWrapper>
                                                // leanDataList =
                                                // getExtEventLienMarkingData();
                                                String line = responseXML;
                                                String[] sp_line = line.split(",");
                                                for (int k = 0; k < sp_line.length; k++) {
                                                      String[] res_sp_line = sp_line[k].split("~");
                                                      Iterator<ExtEventLienMark> iterator1 = ExtEventLien.iterator();

                                                      for (ExtEventLienMark str : ExtEventLien) {
                                                            String depositNum = str.getDEPOSNO().trim();

                                                            BigDecimal hundred = new BigDecimal(100);
                                                            BigDecimal margin_Amt = str.getMARGAM();
                                                            BigDecimal margin_Amount = margin_Amt.divide(hundred);
                                                            DecimalFormat diff = new DecimalFormat("0.00");
                                                            diff.setMaximumFractionDigits(2);
                                                            String lienAmt = diff.format(margin_Amount);
                                                            // Loggers.general().info(LOG,"Lien mark
                                                            // Amount"
                                                            // + lienAmt);
                                                            String currentDepositNum = res_sp_line[0].trim();
                                                            Loggers.general().info(LOG,"Lien response currentDepositNum" + currentDepositNum);
                                                            String responseValue = res_sp_line[3].trim();
                                                            BigDecimal responseAmount = new BigDecimal(responseValue);
                                                            String responseAmt = diff.format(responseAmount);

                                                            Loggers.general().info(LOG,"Lien response amount" + responseAmt);
                                                            String markVal = res_sp_line[4].trim();
                                                            String markValue = "MARK " + markVal;
                                                            Loggers.general().info(LOG,"Lien mark response Value" + markValue);
                                                            String lienbox = str.getLINESTS().trim();
                                                            Loggers.general().info(LOG,"Lien mark grig Value" + lienbox);
                                                            // String lienStr =
                                                            // str.getMARGAMT();
                                                            // double lienAmt =
                                                            // Double.valueOf(lienStr);

                                                            if (depositNum.equalsIgnoreCase(currentDepositNum)
                                                                        && markValue.equalsIgnoreCase("MARK SUCCEEDED")
                                                                        && lienbox.equalsIgnoreCase("MARK SUCCEEDED")) {
                                                                  if (!lienAmt.equalsIgnoreCase(responseAmt)) {
                                                                        validationDetails.addError(ErrorType.Other,
                                                                                    "For Account no (" + depositNum
                                                                                                + "), lien is marked for amount (" + responseAmt
                                                                                                + ") in finacle. Kindly update the lien amount");
                                                                  }

                                                                  else {

                                                                        Loggers.general().info(LOG,"lien and response amount is same else" + lienAmt
                                                                                    + "responseAmt" + responseAmt);
                                                                  }
                                                            } else {
                                                                  Loggers.general().info(LOG,"lien depositNum else" + depositNum
                                                                              + "currentDepositNum===>" + currentDepositNum);
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
                                    // //Loggers.general().info(LOG,"marksucceed=======>" +
                                    // dob);
                              }

                              // //Loggers.general().info(LOG,"marksucceed=======>" + dob1);

                              if (arr.size() > 0 && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))
                                          && getMinorCode().equalsIgnoreCase("OIS")) {
                                    // //Loggers.general().info(LOG,"Marginamt_log for -----> "
                                    // +
                                    // dob1);
                                    // validationDetails.addWarning(WarningType.Other,
                                    // "Total Lien Mark amount is (" + dob1 + " INR),
                                    // Please
                                    // Release the Lien [CM]");
                              } else {
                                    // Loggers.general().info(LOG,"Lien mark amount in else" +
                                    // dob1);
                              }

                              if (marDob > 0 && ExtEventLienMark.size() == 0 && (step_Input.equalsIgnoreCase("i"))
                                          && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                    // //Loggers.general().info(LOG,"ExtEventLienMark ILC--> " +
                                    // ExtEventLienMark + " & marginAmt ILC ---> " +
                                    // marDob);
                                    validationDetails.addWarning(WarningType.Other,
                                                "Lien amount is calculated and no Lien Amount is entered in FD lien grid  [CM]");
                              } else {
                                    // Loggers.general().info(LOG," FD lien grid is
                                    // entered----->");

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
                                                "Sum of margin amount should be equal to Required Lien Amount  [CM]");
                              }

                              if (isMarginAvailableForCIF && (expectedMarginAmt < 0.0) && (step_Input.equalsIgnoreCase("i"))
                                          && (step_csm.equalsIgnoreCase("CBS Maker"))) {

                                    // validationDetails.addWarning(WarningType.Other,
                                    // "Expected
                                    // margin amount field is empty, please input
                                    // facility
                                    // Id to
                                    // calculate the same [CM]");
                              }
                        }

                        catch (Exception ee) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG,"Exception in Lien details in LSC" + ee.getMessage());
                              }

                        }

                  }

                  String lsc = getDriverWrapper().getEventFieldAsText("S:PTP", "s", "").trim(); // SHG
                                                                                                                                          // or
                                                                                                                                          // DOI
                  String subMaster = getDriverWrapper().getEventFieldAsText("S:MST", "r", "").trim(); //
                  String subEvent = getDriverWrapper().getEventFieldAsText("S:EVR", "r", "").trim(); // CRE001

                  // need to add || getMinorCode().equalsIgnoreCase("LSC")

                  String productcode = getDriverWrapper().getEventFieldAsText("PCO", "s", "").trim(); // ILC
                  String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim(); // ISS001
                  String subproductCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();// ILF
                  String evnt = getDriverWrapper().getEventFieldAsText("EPF", "s", "");
                  String evvcount = getDriverWrapper().getEventFieldAsText("ESQ", "i", "");

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
                              Loggers.general().info(LOG,"Query for  checklist if"+query_wrk);
                              while (rs1.next()) {
                                    count = rs1.getInt(1);
                                    Loggers.general().info(LOG,"ETT_WF_CHKLST_TRACKING in whileloop if----> " + count);

                              }
                        } else if (step_csm.equalsIgnoreCase("CBS Maker")) {
                              String query_wrk = "select count(*) from ETT_WF_CHKLST_TRACKING where MASTER_REF='"
                                          + MasterReference + "' and EVENTREF='" + eventCode + "' and PROD_CODE='" + productcode
                                          + "' and SUB_PRODUCT_CODE='" + subproductCode + "' and INIT_AT='CBS Maker'";
                              con = getConnection();
                              ps1 = con.prepareStatement(query_wrk);
                              rs1 = ps1.executeQuery();
                              Loggers.general().info(LOG,"Query for  checklist else"+query_wrk);
                              while (rs1.next()) {
                                    count = rs1.getInt(1);
                                    // //Loggers.general().info(LOG,"ETT_WF_CHKLST_TRACKING in while
                                    // loop----> " + count3);
                                    Loggers.general().info(LOG,"ETT_WF_CHKLST_TRACKING in whileloop else----> " + count);

                              }
                        }
                        Loggers.general().info(LOG,"Count==="+count);
                        Loggers.general().info(LOG,"Stepcsm======="+step_csm);
                        if (count < 1 && step_Input.equalsIgnoreCase("i")
                                    && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CSM")
                                                || step_csm.equalsIgnoreCase("AdhocCSM"))
                                    && (getMinorCode().equalsIgnoreCase("IIS") || getMinorCode().equalsIgnoreCase("PCOC")
                                                || getMinorCode().equalsIgnoreCase("NAIS") || getMinorCode().equalsIgnoreCase("NJIS")
                                                || getMinorCode().equalsIgnoreCase("OIS"))) {

                              validationDetails.addError(ErrorType.Other, "Workflow checklist is mandatory  [CM]");                       }

                        else {
                              // Loggers.general().info(LOG,"ETT_WF_CHKLST_TRACKING in else
                              // loop----> " + count3);
                        }

                        // Ilc to shg
                        //
                        // if (subMaster.length() > 0 && !subMaster.equalsIgnoreCase("")
                        // && getMinorCode().equalsIgnoreCase("LSC")) {
                        // if (step_csm.equalsIgnoreCase("CSM")) {
                        // query_wrk = "select count(*) from ETT_WF_CHKLST_TRACKING
                        // where MASTER_REF='" + subMaster
                        // + "' and EVENTREF='" + subEvent + "' and SUB_PRODUCT_CODE='"
                        // + lsc
                        // + "' and INIT_AT='CSM'";
                        // } else if (step_csm.equalsIgnoreCase("CBS Maker")) {
                        // query_wrk = "select count(*) from ETT_WF_CHKLST_TRACKING
                        // where MASTER_REF='" + subMaster
                        // + "' and EVENTREF='" + subEvent + "' and SUB_PRODUCT_CODE='"
                        // + lsc
                        // + "' and INIT_AT='CBS Maker'";
                        // }
                        //
                        // // Loggers.general().info(LOG,"ETT_WF_CHKLST_TRACKING----> " +
                        // // query_wrk);
                        // int count4 = 0;
                        // con = getConnection();
                        // ps1 = con.prepareStatement(query_wrk);
                        // rs1 = ps1.executeQuery();
                        // while (rs1.next()) {
                        // count4 = rs1.getInt(1);
                        // // //Loggers.general().info(LOG,"ETT_WF_CHKLST_TRACKING in while
                        // // loop----> " + count3);
                        //
                        // }
                        // if (count4 < 1 && step_Input.equalsIgnoreCase("i")
                        // && (step_csm.equalsIgnoreCase("CBS Maker") ||
                        // step_csm.equalsIgnoreCase("CSM"))
                        // && (lsc.equalsIgnoreCase("SHG") ||
                        // lsc.equalsIgnoreCase("DOI"))) {
                        //
                        // validationDetails.addError(ErrorType.Other, "Workflow
                        // checklist is mandatory [CM]");
                        // }
                        //
                        // else {
                        // // Loggers.general().info(LOG,"ETT_WF_CHKLST_TRACKING in else
                        // // loop----> " + count3);
                        // }
                        //
                        // } else {
                        // // Loggers.general().info(LOG,"ETT_WF_CHKLST_TRACKING in else for
                        // // subMaster----> ");
                        // }

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

                  // try {
                  // int evtno = Integer.parseInt(evvcount);
                  // List<ExtEventSERVICETAXLC> servicetax =
                  // (List<ExtEventSERVICETAXLC>) getWrapper()
                  // .getExtEventSERVICETAXLC();
                  // if (servicetax.size() > 0 && (step_Input.equalsIgnoreCase("i"))
                  // && (step_csm.equalsIgnoreCase("CBS Maker")) && evtno != 1) {
                  // if ((getMajorCode().equalsIgnoreCase("ISB") &&
                  // getMinorCode().equalsIgnoreCase("OIS"))
                  // || (getMajorCode().equalsIgnoreCase("ESB") &&
                  // getMinorCode().equalsIgnoreCase("OES"))) {
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
                  // Loggers.general().info(LOG,"Exception Service tax lc value");
                  // }
                  // CR 215 starts here

                  if ((getMinorCode().equalsIgnoreCase("BIS") || getMinorCode()
                              .equalsIgnoreCase("BES"))
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

                  // Over due bill exists for this customer
                  if (step_csm.equalsIgnoreCase("CBS Maker")) {
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
                  //swiftsfms
      /*          if(getMajorCode().equalsIgnoreCase("ISB") && (getMinorCode().equalsIgnoreCase("IIS")||getMinorCode().equalsIgnoreCase("NJIS")))
                  {
                        int v;
                        String advDirect=getDriverWrapper().getEventFieldAsText("ABD", "l", "").trim();
                        if(advDirect.equalsIgnoreCase("N") && !advDirect.equalsIgnoreCase("Y"))
                        {
                  try{
                        String Branch=getDriverWrapper().getEventFieldAsText("NPR", "p", "ss").trim();
                        Loggers.general().info(LOG,"Branch code ---->"+Branch);
                        v=getSWIFTSFMS(Branch);
                        if (v==1)
                        {
                              validationDetails.addError(ErrorType.Other, "Please select SFMS in Swift/Sfms[CM]");
                        }
                        if(v==2)
                        {
                              validationDetails.addError(ErrorType.Other, "Please select SWIFT in Swift/Sfms [CM]");
                        }
                  }
                  catch(Exception e)
                  {
                        Loggers.general().info(LOG,"Swiftsfms--->" + e.getMessage());

                  }
                        }
                  }*/
                  //======================================================================
                  String perichargval=getPane().getPERCHRAD().trim();
                Loggers.general().info(LOG,"Periodic charge in upfront "+getPane().getPERCHRAD());
            
            
            
            if((getMajorCode().equalsIgnoreCase("ISB")) && (getMinorCode().equalsIgnoreCase("IIS")))
            {
                  try{
                        
                        String d = getDriverWrapper().getEventFieldAsText("ISS", "d", "").trim();                             
                        String d1 = getDriverWrapper().getEventFieldAsText("cBKY", "d", "").trim();
                        
                        Loggers.general().info(LOG,"Issue date issue"+d);
                        Loggers.general().info(LOG,"Charge basis end date issue "+d1);
                        
                        getPane().setCHRPERAM(difference(d, d1).substring(0, difference(d, d1).indexOf(".")));
                        Loggers.general().info(LOG,"charge - issue date days issue"+getPane().getCHRPERAM());
                        
                  }
                  catch(Exception e)
                  {
                        Loggers.general().info(LOG,"Exception in calculating days issue "+e.getMessage());
                  }
            }
            if((getMajorCode().equalsIgnoreCase("ISB")) && (getMinorCode().equalsIgnoreCase("NJIS")))
            {
                  try{
                        
                        String d = getDriverWrapper().getEventFieldAsText("AMD", "d", "").trim();                             
                        String d1 = getDriverWrapper().getEventFieldAsText("cBKY", "d", "").trim();
                        
                        Loggers.general().info(LOG,"Issue date adjust"+d);
                        Loggers.general().info(LOG,"Charge basis end date adjust "+d1);
                        
                        getPane().setCHRPERAM(difference(d, d1).substring(0, difference(d, d1).indexOf(".")));
                        Loggers.general().info(LOG,"charge - issue date days adjust"+getPane().getCHRPERAM());
                        
                  }
                  catch(Exception e)
                  {
                        Loggers.general().info(LOG,"Exception in calculating days adjust "+e.getMessage());
                  }
            }
            
            
            if(perichargval.equalsIgnoreCase("Yes")){
            
            if((getMajorCode().equalsIgnoreCase("ISB")) && (getMinorCode().equalsIgnoreCase("NAIS")))
            {
                  
                  String imlAmount= getDriverWrapper().getEventFieldAsText("IML", "v", "m").trim();
                  String n = getDriverWrapper().getEventFieldAsText("cBKY", "d", "").trim();                            
                  String m = getDriverWrapper().getEventFieldAsText("cBKZ", "d", "").trim();
                  Loggers.general().info(LOG,"iml amount amend"+imlAmount);
                  Loggers.general().info(LOG,"charge end end mirror "+m);
                  Loggers.general().info(LOG,"charge end end  "+n);
            
                  
                  try{
                  SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                  
                  Date date1 = sdf1.parse(n);
                Date date2 = sdf1.parse(m);
                  
                  Loggers.general().info(LOG,"date1.compareTo(date2) "+date1.compareTo(date2));
                  if(date1.compareTo(date2) > 0){
                  try{
                        
                        String d = getDriverWrapper().getEventFieldAsText("occBKY", "d", "");                           
                        String d1 = getDriverWrapper().getEventFieldAsText("nccBKY", "d", "");
                        
                        Loggers.general().info(LOG,"Issue date amend"+d);
                        Loggers.general().info(LOG,"Charge basis end date amend "+d1);
                        
                        getPane().setCHRTENIN(difference(d, d1).substring(0, difference(d, d1).indexOf(".")));
                        Loggers.general().info(LOG,"charge - issue date days amend"+getPane().getCHRTENIN());
                        
                  }
                  catch(Exception e)
                  {
                        Loggers.general().info(LOG,"Exception in calculating days amend "+e.getMessage());
                  }
                  }
                  
                  if(imlAmount!=null && !imlAmount.equalsIgnoreCase("")){
                  
            try{
                        
                        String d = getDriverWrapper().getEventFieldAsText("AMD", "d", "");                              
                        String d1 = getDriverWrapper().getEventFieldAsText("cBKZ", "d", "");
                        
                        Loggers.general().info(LOG,"Issue date amend"+d);
                        Loggers.general().info(LOG,"Charge basis end date amend "+d1);
                        
                        
                        
                  }
                  catch(Exception e)
                  {
                        Loggers.general().info(LOG,"Exception in calculating days amend "+e.getMessage());
                  }
                  }
                  
            }catch(Exception e)
                  {
                  Loggers.general().info(LOG,"Exception in date format"+e.getMessage());
                  }
            
            
            }
            }

            if((getMajorCode().equalsIgnoreCase("ISB")) && ((getMinorCode().equalsIgnoreCase("IIS")||getMinorCode().equalsIgnoreCase("NJIS")||getMinorCode().equalsIgnoreCase("NAIS"))))
                  {
                        String val="";
                        String sts="";
                        String percharup=getPane().getPERCHRAD().trim();
                        
                        try {
                              
                              con = getConnection();
                              String query = "select dis_msg,status from KMB_M_CHG_VALID_MSG where trim(Master_ref)= '" + masterref + "' and trim(event_ref)='" +eventCode+"'";

                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                              
                                     val=rs1.getString(1);
                                     Loggers.general().info(LOG,"Value  "+val);
                                    
                                     sts=rs1.getString(2);
                                     Loggers.general().info(LOG,"Status  "+sts);
                              
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
                        
                     if((percharup.equalsIgnoreCase("Yes")&&sts.equalsIgnoreCase("N") &&step_csm.equalsIgnoreCase("CBS Authoriser")))
                               {
                         validationDetails.addError(ErrorType.Other, val+"[CM]");
                               }
      
            
            
                  
            
            }

                  
            try{
                  String paymentType = getWrapper().getPROREMT();
                  String utrNum = getWrapper().getUTRNO().trim();
                  String rtgsFlag = getDriverWrapper().getEventFieldAsText("cBPJ", "l", "");
                  if (paymentType.equalsIgnoreCase("RTG") && utrNum.length() != 22
                              && (step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm.equalsIgnoreCase("Authorise")||step_csm.equalsIgnoreCase("CBS Maker1")||step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Reject"))
                              && rtgsFlag.equalsIgnoreCase("Y") && !rtgsFlag.equalsIgnoreCase("N"))
                  {
                        validationDetails.addError(ErrorType.Other, "Please review UTR number for RTGS[CM]");
                  }else if (paymentType.equalsIgnoreCase("NEF") && utrNum.length() != 16
                              && (step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm.equalsIgnoreCase("Authorise")||step_csm.equalsIgnoreCase("CBS Maker1")||step_csm.equalsIgnoreCase("CBS Maker")||step_csm.equalsIgnoreCase("CBS Reject"))
                              && rtgsFlag.equalsIgnoreCase("Y") && !rtgsFlag.equalsIgnoreCase("N"))
                  {
                        validationDetails.addError(ErrorType.Other, "Please review UTR number for NEFT[CM]");
                  }
                  else
                  {
                        Loggers.general().info(LOG,"Lenth is correct");
                  }
                        
                  }
                  catch(Exception e)
                  {
                        e.printStackTrace();
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
                  currencyCalc();
            } catch (Exception e) {
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Exception in currency value population---->" + e.getMessage());

                  }
                  
                  //====================================================================
            }
      }
      }
      public static String randomCorrelationId() {
            // Loggers.general().info(LOG,"randomCorrelationId generate");
            return UUID.randomUUID().toString();
      }

      private boolean checkAvailblityOfMarginForCIF(Connection con) {
            // TODO Auto-generated method stub
            return false;
      }
      public static String difference(String a, String b) throws ParseException {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
            Date date1 = format.parse(a);
            Date date2 = format.parse(b);
            long diff = date2.getTime() - date1.getTime();
            double z = Math.abs(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            // //Loggers.general().info(LOG,"values " + Math.abs(TimeUnit.DAYS.convert(diff,
            // TimeUnit.MILLISECONDS)));
            Loggers.general().info(LOG,"Days  "+z);
            return Double.toString(z);

      }

      // private boolean isChargeAccountDiff(Connection con) {
      // boolean isChargeAccountDiff = false;
      // PreparedStatement ps = null;
      // ResultSet rs = null;
      //
      // // Loggers.general().info(LOG,"isChargeAccountDiff method Entered");
      // try {
      // String masterRefNumber = getDriverWrapper().getEventFieldAsText("MST",
      // "r", "");
      // String account = getDriverWrapper().getEventFieldAsText("PRI", "q",
      // "RCA").trim();
      // String ar[] = account.split("-");
      //
      // // //Loggers.general().info(LOG,"Master Reference" + masterRefNumber);
      // String priCustStr = getDriverWrapper().getEventFieldAsText("PRI", "p",
      // "no");
      // if (priCustStr != null) {
      // /*
      // * String chargeAccountCheckQuery =
      // * "select trim(p.bo_acc_no) from master m, baseevent b, relitem r,
      // posting p where m.key97 = b.master_key and b.key97 = r.event_key and
      // r.key97 = p.key97 and p.acc_type in ('CA', 'RB') and m.master_ref='"
      // * + masterRefNumber +
      // * "' and trim(p.bo_acc_no) not in (select trim(bo_acctno) from account
      // where trim(cus_mnm)='"
      // * + priCustStr + "')"; //Loggers.general().info(LOG,
      // * "chargeAccountCheckQuery - " + chargeAccountCheckQuery); ps =
      // * con.prepareStatement(chargeAccountCheckQuery); System.out
      // * .println("prepared statement for chargeAccountCheck - " +
      // * ps); rs = ps.executeQuery(); if (rs.next()) {
      // */
      // if (priCustStr != ar[2].trim()) {
      // isChargeAccountDiff = true;
      // }
      //
      // }
      // } catch (Exception e) {
      // // Loggers.general().info(LOG,"Exception occured in isChargeAccountDiff - "
      // // + e.getMessage());
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
      // return isChargeAccountDiff;
      // }

}