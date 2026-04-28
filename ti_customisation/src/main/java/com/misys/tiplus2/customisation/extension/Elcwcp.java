package com.misys.tiplus2.customisation.extension;

import java.sql.SQLException;

//import org.apache.log4j.Logger;

import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import com.misys.tiplus2.enigma.customisation.control.ExtendedHyperlinkControlWrapper;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.ErrorType;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.WarningType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.misys.tiplus2.foundations.lang.logging.Loggers;
//com.misys.tiplus2.customisation.extension.Elcwcp;
public class Elcwcp extends ConnectionMaster

{
      //private static final Logger logger = Logger.getLogger(ConnectionMaster.class.getName());
      private static final Logger LOG = LoggerFactory.getLogger(Elcwcp.class);
      @Override
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

                  // GETTING LOB
                  if (!eventStatus.equalsIgnoreCase("Completed")) {
                        getLob();
                  }
                  // ----------------------//
                  // SFMS
                  try {
                        String getHyperSFMS = getIFSCSEARCH();
                        ExtendedHyperlinkControlWrapper sfmsExpAdv1 = getPane().getCtlSFMSEGTCORRESlayHyperlink();
                        sfmsExpAdv1.setUrl(getHyperSFMS);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }

                  // TST
                  try {
                        String val = "DMSTS";
                        String TSTHyperlink = Link(val);
                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlTSTEGTCORRESlayHyperlink();
                        dmsh.setUrl(TSTHyperlink);

                        ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlTSTEGTMANULayHyperlink();
                        dmsh1.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh2 = getPane().getCtlTSTEGTMaitainChargelayHyperlink();
                        dmsh2.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh3 = getPane().getCtlTSTEGTMaitainLiablayHyperlink();
                        dmsh3.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh4 = getPane().getCtlTSTEGTBOOKlayHyperlink();
                        dmsh4.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh5 = getPane().getCtlTSTEGTExpirelayHyperlink();
                        dmsh5.setUrl(TSTHyperlink);

                        ExtendedHyperlinkControlWrapper dmsh6 = getPane().getCtlTSTILCPAYLAYHyperlink();
                        dmsh6.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh7 = getPane().getCtlTSTIMPSTDLCPAYlayHyperlink();
                        dmsh7.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh8 = getPane().getCtlTSTIMPGUAPAYLAYHyperlink();
                        dmsh8.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh9 = getPane().getCtlTSTEGTPAYPRIODICLayHyperlink();
                        dmsh9.setUrl(TSTHyperlink);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }

                  // String thre = getDriverWrapper().getEventFieldAsText("THR", "r",
                  // "");
                  // System.out.print("Their Master reference" + thre);
                  //
                  // if (thre.length() == 0) {
                  // getDriverWrapper().logError(thre);
                  // } else {
                  // System.out.print("Else initlise");
                  //
                  // }
            }
            return false;

      }

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
                  String step_Input = getDriverWrapper().getEventFieldAsText("CSTY", "s", "");
                  // Loggers.general().info(LOG,"step_Input check ----->" + step_Input);
                  String step_csm = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                  // //Loggers.general().info(LOG,"step id check for CSM ----->" + step_csm);
                  //

                  // GETTING lob

                  try {
                        // //Loggers.general().info(LOG,"get value for LOB");
                        getLob();
                  } catch (Exception ee) {
                        // Loggers.general().info(LOG,ee.getMessage());
                        // //Loggers.general().info(LOG,"LOB Catch");
                  } finally {
                        // //Loggers.general().info(LOG,"finally LOB ");
                  }

                  // USD EQUIVALENT
                  if (currencyCalc()) {
                        // //Loggers.general().info(LOG," Currency systemOutput");
                  } else {
                        // Loggers.general().info(LOG," Else systemOutput");
                  }
                  // SFMS
                  try {
                        String getHyperSFMS = getIFSCSEARCH();
                        ExtendedHyperlinkControlWrapper sfmsExpAdv1 = getPane().getCtlSFMSEGTCORRESlayHyperlink();
                        sfmsExpAdv1.setUrl(getHyperSFMS);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
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
                  String seifsc = getWrapper().getSENIFSC().trim();
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
                  // }
                  //
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
                  // // Loggers.general().info(LOG,"Connection Failed! Check output
                  // // console");
                  // e.printStackTrace();
                  // }
                  // }
                  //
                  // } else {
                  // Loggers.general().info(LOG,"Sender IFSC Code" + seifsc);

                  // }

                  String recifsc = getWrapper().getRECIFSC().trim();
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
                  // }
                  //
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
                  // // Loggers.general().info(LOG,"Connection Failed! Check output
                  // // console");
                  // e.printStackTrace();
                  // }
                  // }
                  //
                  // } else {
                  // Loggers.general().info(LOG,"Receiver IFSC Code" + recifsc);

                  // }
                  // CR220 Changes Starts here
                                    String MasterReference = getDriverWrapper().getEventFieldAsText(
                                                "MST", "r", "").trim();
                                    String evnt = getDriverWrapper()
                                                .getEventFieldAsText("EPF", "s", "");
                                    String evvcount = getDriverWrapper().getEventFieldAsText("ESQ",
                                                "i", "");
                  String subProductype = getDriverWrapper().getEventFieldAsText("PTP",
                              "s", "");
                  if (getMajorCode().equalsIgnoreCase("IGT")&&(subProductype.equalsIgnoreCase("BCR")||subProductype.equalsIgnoreCase("IGP")||subProductype.equalsIgnoreCase("IGF"))) {
                        System.out.println("CR-220 Validation starts here");
                        try {
                              con = getConnection();
                              String query = "select count(*) from master mas, BASEEVENT bev, warnmsg wm where mas.key97=BEV.MASTER_KEY and bev.key97=wm.associ30 and WM.PARTCODE = 'RtTol' and MAS.MASTER_REF = '"
                                          + MasterReference
                                          + "' and BEV.REFNO_PFIX= '"
                                          + evnt
                                          + "' and BEV.REFNO_SERL = '"
                                          + evvcount
                                          + "'";
                              int count = 0;
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    count = rs1.getInt(1);
                              }

                              if (count > 0
                                          && (step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm
                                                      .equalsIgnoreCase("CBS Maker 1"))) {
                                    System.out
                                                .println("CR-220 Validation setting up Error");
                                    validationDetails
                                                .addError(ErrorType.Other,
                                                            "FX deal rate is outside specified tolerance [CM]");
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

                                    if ((getMinorCode().equalsIgnoreCase("BEG"))
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
            }
      }
}