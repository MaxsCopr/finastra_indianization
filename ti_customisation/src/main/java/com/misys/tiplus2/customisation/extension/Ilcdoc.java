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

public class Ilcdoc extends ConnectionMaster {
      //private static final Logger logger = Logger.getLogger(ConnectionMaster.class.getName());
      private static final Logger LOG = LoggerFactory.getLogger(Ilcdoc.class);
      Connection con = null;

      // @Override
      public boolean onPostInitialise() {
            String strPropName = "MigrationDone";
            String dailyval = "";
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");
            //// Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
            EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
            if (PROPCode != null) {

                  dailyval = PROPCode.getPropval();
                  //// Loggers.general().info(LOG,"ADDDailyTxnLimit -------->" +
                  //// PROPCode.getPropval());
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
                  // BOE
                  try {
                        String val = "BOE";
                        String getBoeLink = Link(val);

                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlBOEILCSETTclayHyperlink();
                        dmsh.setUrl(getBoeLink);
                        //// Loggers.general().info(LOG,"ILC outclaim ");
                        ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlBOEINWDOCCOLPAYclayHyperlink();
                        dmsh1.setUrl(getBoeLink);
                        //// Loggers.general().info(LOG,"IDC PAYMENT ");
                        ExtendedHyperlinkControlWrapper dmsh2 = getPane().getCtlBOEOUTDOCCOLEXPclayHyperlink();
                        dmsh2.setUrl(getBoeLink);
                        //// Loggers.general().info(LOG," ODC Expire ");
                        ExtendedHyperlinkControlWrapper dmsh3 = getPane().getCtlBOEELCEXPIREclayHyperlink();
                        dmsh3.setUrl(getBoeLink);
                        //// Loggers.general().info(LOG," ELC Expire ");
                        ExtendedHyperlinkControlWrapper dmsh4 = getPane().getCtlBOEIMPLCCLAIMRECclayHyperlink();
                        dmsh4.setUrl(getBoeLink);
                        //// Loggers.general().info(LOG," ILC claim received ");
                  } catch (Exception e) {
                        // Loggers.general().info(LOG," ");
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
                        // Loggers.general().info(LOG,"error");
                  }

                  // PRESHIPMENT}
                  try {

                        // String Preshipment = getHyperPreshipment();
                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlPreshipmentFINEXPLCADJclayHyperlink();// FELC
                                                                                                                                                                        // CREATE
                        // dmsh.setUrl(Preshipment);
                        ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlPreshipmentFINEXPLCREPclayHyperlink();// FELC
                                                                                                                                                                        // CREATE
                        // dmsh1.setUrl(Preshipment);

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,e.getMessage());
                        //// Loggers.general().info(LOG,"Shipment Catch Ending");
                  }

                  // TST
                  try {
                        String val = "DMSTS";
                        String TSTHyperlink = Link(val);
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
                        ExtendedHyperlinkControlWrapper dmsh6 = getPane().getCtlTSTINWDOCCOLPAYclayHyperlink();
                        dmsh6.setUrl(TSTHyperlink);
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

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }
                  try {
                        currencyCalc();
                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception in currency value population---->" + e.getMessage());

                        }
                  }
                  String step_csm = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                  // cross currency conversion
                  if (!step_csm.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {

                        if (getMajorCode().equalsIgnoreCase("ODC") && getMinorCode().equalsIgnoreCase("CLP")) {
                              EventPane pane = (EventPane) getPane();
                              // pane.getBtnFETCHINVDETELCSE.setEnabled(false);
                              pane.getBtnFetchshipdetEXPCOLSETTclay().setEnabled(false);
                              pane.getBtnFetchinvdetEXPCOLSETTclay().setEnabled(false);
                        }

                        String prd_typ = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();
                        if ((!prd_typ.equalsIgnoreCase("OCF"))
                                    || prd_typ.equalsIgnoreCase("OCI") && getMinorCode().equalsIgnoreCase("CLP")) {
                              EventPane pane = (EventPane) getPane();
                              pane.getBtnFetchshipdetEXPCOLSETTclay().setEnabled(false);
                              pane.getBtnFetchinvdetEXPCOLSETTclay().setEnabled(false);

                              pane.getExtEventShippingCollectionsNew().setEnabled(false);
                              pane.getExtEventShippingCollectionsDelete().setEnabled(false);
                              pane.getExtEventShippingdetailsNew().setEnabled(false);
                              pane.getExtEventShippingdetailsDelete().setEnabled(false);
                              pane.getExtEventShippingdetailsUpdate().setEnabled(false);
                              pane.getExtEventInvoiceDetailsNew().setEnabled(false);
                              pane.getExtEventInvoiceDetailsUpdate().setEnabled(false);
                              pane.getExtEventInvoiceDetailsDelete().setEnabled(false);

                        }

                        // sub product BOE checker
                        String prodcode = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
                        //// Loggers.general().info(LOG,"the sub product code" + prodcode);
                        if (prodcode.trim().equalsIgnoreCase("Inland LC") || prodcode.trim().equalsIgnoreCase("Service LC")
                                    || prodcode.trim().equalsIgnoreCase("Intermediatory LC")) {
                              getWrapper().setBOEWAI(true);
                        }

                  }
            }

            return true;
      }

      public void onValidate(ValidationDetails validationDetails) {
            String strPropName = "MigrationDone";
            String dailyval = "";
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");
            //// Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
            EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
            if (PROPCode != null) {

                  dailyval = PROPCode.getPropval();
                  //// Loggers.general().info(LOG,"ADDDailyTxnLimit -------->" +
                  //// PROPCode.getPropval());
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

                  try {
                        currencyCalc();
                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception in currency value population---->" + e.getMessage());

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
                  // seifsc+ "'";
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
                  // }
                  // else
                  // {
                  // //Loggers.general().info(LOG,"valid Sender IFSC code");
                  // }
                  //
                  //
                  // } catch (Exception e1) {
                  // //Loggers.general().info(LOG,"Exception Sender IFSC Code" +
                  // e1.getMessage());
                  // }
                  // finally {
                  // try {
                  // if (con != null) {
                  // con.close();
                  // if (ps1 != null)
                  // ps1.close();
                  // if (rs1 != null)
                  // rs1.close();
                  // }
                  // } catch (SQLException e) {
                  // //Loggers.general().info(LOG,"Connection Failed! Check output console");
                  // e.printStackTrace();
                  // }
                  // }
                  // }
                  // else
                  // {
                  // //Loggers.general().info(LOG,"Sender IFSC Code"+seifsc);
                  //
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
                  // validationDetails.addError(ErrorType.Other, "Invalid Receiver
                  // IFSC code(" + recifsc + ") [CM]");
                  // }
                  // else
                  // {
                  // //Loggers.general().info(LOG,"valid Receiver IFSC code");
                  // }
                  //
                  //
                  // } catch (Exception e1) {
                  // //Loggers.general().info(LOG,"Exception Receiver IFSC Code" +
                  // e1.getMessage());
                  // }
                  // finally {
                  // try {
                  // if (con != null) {
                  // con.close();
                  // if (ps1 != null)
                  // ps1.close();
                  // if (rs1 != null)
                  // rs1.close();
                  // }
                  // } catch (SQLException e) {
                  // //Loggers.general().info(LOG,"Connection Failed! Check output console");
                  // e.printStackTrace();
                  // }
                  // }
                  // }
                  // else
                  // {
                  // //Loggers.general().info(LOG,"Receiver IFSC Code"+ recifsc);
                  //
                  // }

            }
      }
}