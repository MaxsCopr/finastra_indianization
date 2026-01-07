package com.misys.tiplus2.customisation.extension;

//com.misys.tiplus2.customisation.extension.Service
import java.sql.Connection;
import java.sql.SQLException;

//import org.apache.log4j.Logger;

import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;
import com.misys.tiplus2.customisation.pane.EventPane;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import com.misys.tiplus2.enigma.customisation.control.ExtendedHyperlinkControlWrapper;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.ErrorType;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.WarningType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.misys.tiplus2.foundations.lang.logging.Loggers;
public class IdcClp extends ConnectionMaster {
      //private static final Logger logger = Logger.getLogger(ConnectionMaster.class.getName());
      private static final Logger LOG = LoggerFactory.getLogger(IdcClp.class);
      // //Loggers.general().info(LOG,"eventRefNumber - "+eventRefNumber);
      Connection con = null;

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
                  if (!eventStatus.equalsIgnoreCase("Completed")) {
                        try {
                              // crosscurrency();
                              EventPane pane = (EventPane) getPane();
                              pane.getExtEventInvoiceDataNew().setEnabled(false);
                              pane.getExtEventInvoiceDataDelete().setEnabled(false);
                              pane.getExtEventInvoiceDataUpdate().setEnabled(false);
                              getLob();
                        } catch (Exception ee) {
                              // Loggers.general().info(LOG,ee.getMessage());
                              // //Loggers.general().info(LOG,"LOB Catch");
                        } finally {
                              // //Loggers.general().info(LOG,"finally LOB ");
                        }
                  }

                  // SFMS
                  try {
                        String getHyperSFMS = getIFSCSEARCH();
                        ExtendedHyperlinkControlWrapper sfmsExpAdv = getPane().getCtlSFMSINWCOLACCEPTclayHyperlink();
                        sfmsExpAdv.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExp = getPane().getCtlSFMSINWCOLCANLAYHyperlink();
                        sfmsExp.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExp1 = getPane().getCtlSFMSINWDOCOCRlayHyperlink();
                        sfmsExp1.setUrl(getHyperSFMS);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }

                  
                  
                  // WORKFLOW CHECKLIST

                  try {
                        String Hyperreferel12 = getWorkflow().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack1 = getPane().getCtlCSMCHECKLISTINWCOLACCEPTclayHyperlink();
                        csmreftrack1.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST--------->" +
                        // ees.getMessage());
                  }

                  // REFEREL TRACKING
                  try {

                        String Hyperreferel3 = getReferealtracking().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack = getPane().getCtlCSMREFRALINWCOLACCEPTclayHyperlink();
                        csmreftrack.setUrl(Hyperreferel3);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING--------->" +
                        // ees.getMessage());
                  }

                  // Workflow CBS
                  try {
                        String Hyperreferel12 = getWorkflowCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack1 = getPane().getCtlCPCCHECKINWCOLACCEPTclayHyperlink();
                        cpcreftrack1.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST WorkflowCBS--------->"
                        // + ees.getMessage());
                  }

                  // Refereal CBS
                  try {
                        String Hyperreferel12 = getReferealtrackingCBS().trim();

                        ExtendedHyperlinkControlWrapper cpcreftrack = getPane().getCtlCPCREFERELINWCOLACCEPTclayHyperlink();
                        cpcreftrack.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING REFERE CBS--------->" +
                        // ees.getMessage());
                  }
                  //FORWARD
                  try {

                        String Forward = getForward();
                        ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlFORWARDFREECORRlayHyperlink();
                        dmsh1.setUrl(Forward);
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,e.getMessage());
                  }
                  // TST
                  try {
                        String val = "DMSTS";
                        String TSTHyperlink = Link(val);
                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlTSTINWCOLACCEPTclayHyperlink();
                        dmsh.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlTSTINWDOCOCRlayHyperlink();
                        dmsh1.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh2 = getPane().getCtlTSTINWCOLCANLAYHyperlink();
                        dmsh2.setUrl(TSTHyperlink);

                        ExtendedHyperlinkControlWrapper dmsh3 = getPane().getCtlTSTINWCOLMANULlayHyperlink();
                        dmsh3.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh4 = getPane().getCtlTSTINWCOLEXPlayHyperlink();
                        dmsh4.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh5 = getPane().getCtlTSTINWCOLBOOKlayHyperlink();
                        dmsh5.setUrl(TSTHyperlink);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
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
                  String step_Input = getDriverWrapper().getEventFieldAsText("CSTY", "s", "");
                  // Loggers.general().info(LOG,"step_Input check ----->" + step_Input);
                  String step_csm = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                  // Loggers.general().info(LOG,"step id check for CSM ----->" + step_csm);

                  // // KYC DATE VALIDATION
                  // try{
                  //
                  // //Loggers.general().info(LOG,"KYC Date Validation");
                  // String t= getDriverWrapper().getEventFieldAsText("PRI", "p",
                  // "cBEY");
                  // //Loggers.general().info(LOG,"value os first date " +t);
                  // String i = getDriverWrapper().getEventFieldAsText("ISS", "d",
                  // "");
                  // //Loggers.general().info(LOG,"value of issue date "+i);
                  // SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                  // Date date1 = sdf.parse(t.trim());
                  // Date date2 = sdf.parse(i.trim());
                  // if(date1.compareTo(date2)<0 && (step_Input.equalsIgnoreCase("i"))
                  // && (step_csm.equalsIgnoreCase("CBS Maker"))){
                  // validationDetails.addWarning(ValidationDetails.WarningType.Other,
                  // "KYC Expired for the Customer [CM]" );
                  // }
                  // }catch(Exception e){
                  // //Loggers.general().info(LOG,"Exception " + e.getMessage());
                  // }

                  // cross currency conversion

                  try {
                        currencyCalc();
                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception in currency value population---->" + e.getMessage());

                        }
                  }
                  // GETTING LOB

                  try {
                        EventPane pane = (EventPane) getPane();
                        pane.getExtEventInvoiceDataNew().setEnabled(false);
                        pane.getExtEventInvoiceDataDelete().setEnabled(false);
                        pane.getExtEventInvoiceDataUpdate().setEnabled(false);
                        getLob();
                  } catch (Exception ee) {
                        // Loggers.general().info(LOG,ee.getMessage());
                        // //Loggers.general().info(LOG,"LOB Catch");
                  } finally {
                        // //Loggers.general().info(LOG,"finally LOB ");
                  }

                  // SFMS
                  try {
                        String getHyperSFMS = getIFSCSEARCH();
                        ExtendedHyperlinkControlWrapper sfmsExpAdv = getPane().getCtlSFMSINWCOLACCEPTclayHyperlink();
                        sfmsExpAdv.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExp = getPane().getCtlSFMSINWCOLCANLAYHyperlink();
                        sfmsExp.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExp1 = getPane().getCtlSFMSINWDOCOCRlayHyperlink();
                        sfmsExp1.setUrl(getHyperSFMS);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }
                  //kalpana Ghorpade add new trigger point for IDC cac :30/01/25
                  if (getMajorCode().equalsIgnoreCase("IDC") && getMinorCode().equalsIgnoreCase("CAC")) {
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

                              // TODO: handle exception
                        }

                  
                  // WORKFLOW CHECKLIST

                  try {
                        String Hyperreferel12 = getWorkflow().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack1 = getPane().getCtlCSMCHECKLISTINWCOLACCEPTclayHyperlink();
                        csmreftrack1.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST--------->" +
                        // ees.getMessage());
                  }

                  // REFEREL TRACKING
                  try {

                        String Hyperreferel3 = getReferealtracking().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack = getPane().getCtlCSMREFRALINWCOLACCEPTclayHyperlink();
                        csmreftrack.setUrl(Hyperreferel3);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING--------->" +
                        // ees.getMessage());
                  }

                  // Workflow CBS
                  try {
                        String Hyperreferel12 = getWorkflowCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack1 = getPane().getCtlCPCCHECKINWCOLACCEPTclayHyperlink();
                        cpcreftrack1.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST WorkflowCBS--------->"
                        // + ees.getMessage());
                  }

                  // Refereal CBS
                  try {
                        String Hyperreferel12 = getReferealtrackingCBS().trim();

                        ExtendedHyperlinkControlWrapper cpcreftrack = getPane().getCtlCPCREFERELINWCOLACCEPTclayHyperlink();
                        cpcreftrack.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING REFERE CBS--------->" +
                        // ees.getMessage());
                  }
                  //FORWARD
                  try {

                        String Forward = getForward();
                        ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlFORWARDFREECORRlayHyperlink();
                        dmsh1.setUrl(Forward);
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,e.getMessage());
                  }

                  // CR 215 starts here

                  if ((getMinorCode().equalsIgnoreCase("MBC"))
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


                  // TST
                  try {
                        String val = "DMSTS";
                        String TSTHyperlink = Link(val);
                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlTSTINWCOLACCEPTclayHyperlink();
                        dmsh.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlTSTINWDOCOCRlayHyperlink();
                        dmsh1.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh2 = getPane().getCtlTSTINWCOLCANLAYHyperlink();
                        dmsh2.setUrl(TSTHyperlink);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }

                  String masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();

                  String productcode = getDriverWrapper().getEventFieldAsText("PCO", "s", "").trim(); // ILC
                  String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim(); // ISS001
                  String subproductCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();// ILF
                  String evnt = getDriverWrapper().getEventFieldAsText("EPF", "s", "");
                  String evvcount = getDriverWrapper().getEventFieldAsText("ESQ", "i", "");

                  // workflow error configuration
                  try {
                        String query_wrk = "";
                        if (step_csm.equalsIgnoreCase("CSM") || step_csm.equalsIgnoreCase("AdhocCSM")) {
                              query_wrk = "select count(*) from ETT_WF_CHKLST_TRACKING where MASTER_REF='" + masterref
                                          + "' and EVENTREF='" + eventCode + "' and PROD_CODE='" + productcode
                                          + "' and SUB_PRODUCT_CODE='" + subproductCode + "' and INIT_AT='CSM'";
                        } else if (step_csm.equalsIgnoreCase("CBS Maker")) {
                              query_wrk = "select count(*) from ETT_WF_CHKLST_TRACKING where MASTER_REF='" + masterref
                                          + "' and EVENTREF='" + eventCode + "' and PROD_CODE='" + productcode
                                          + "' and SUB_PRODUCT_CODE='" + subproductCode + "' and INIT_AT='CBS Maker'";
                        }

                        // Loggers.general().info(LOG,"ETT_WF_CHKLST_TRACKING----> " +
                        // query_wrk);
                        int count3 = 0;
                        con = getConnection();
                        ps1 = con.prepareStatement(query_wrk);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                              count3 = rs1.getInt(1);
                              // //Loggers.general().info(LOG,"ETT_WF_CHKLST_TRACKING in while
                              // loop----> " + count3);

                        }
                        if (count3 < 1
                                    && step_Input.equalsIgnoreCase("i") && (step_csm.equalsIgnoreCase("CBS Maker")
                                                || step_csm.equalsIgnoreCase("CSM") || step_csm.equalsIgnoreCase("AdhocCSM"))
                                    && (getMinorCode().equalsIgnoreCase("CAC"))) {

                              validationDetails.addError(ErrorType.Other, "Workflow checklist is mandatory  [CM]");                       }

                        else {
                              // Loggers.general().info(LOG,"ETT_WF_CHKLST_TRACKING in else
                              // loop----> " + count3);
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
                  String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim(); //

                  try {
                        con = getConnection();
                        String query = "SELECT mas.MASTER_REF, note.* FROM master mas, TIDATAITEM tid, NOTE WHERE mas.KEY97 = tid.MASTER_KEY AND tid.KEY97 = NOTE.KEY97 and NOTE.TYPE not in (3,129,1089) AND note_event IS NOT NULL AND NOTE.ACTIVE = 'Y' AND mas.MASTER_REF = '"
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

            }
      }

}