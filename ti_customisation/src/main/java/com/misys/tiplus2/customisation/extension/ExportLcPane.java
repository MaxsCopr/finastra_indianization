package com.misys.tiplus2.customisation.extension;

import java.sql.SQLException;

/*import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;*/

import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import com.misys.tiplus2.enigma.customisation.control.ExtendedHyperlinkControlWrapper;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.ErrorType;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.WarningType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.misys.tiplus2.foundations.lang.logging.Loggers;
public class ExportLcPane extends ConnectionMaster {
      //private static final Logger logger = Logger.getLogger(ConnectionMaster.class.getName());
      private static final Logger LOG = LoggerFactory.getLogger(ExportLcPane.class);
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
                  String step_csm = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                  if (!step_csm.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {
                        getLob();
                        // crosscurrency();
                  }
                  

                  // TST
                  try {
                        String val = "DMSTS";
                        String TSTHyperlink = Link(val);
                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlTSTEXPLCTRACOLDADJLAYHyperlink();
                        dmsh.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlTSTEXPLCTRACOLDAMDLAYHyperlink();
                        dmsh1.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh2 = getPane().getCtlTSTEXPLCTRACEXPLAYHyperlink();
                        dmsh2.setUrl(TSTHyperlink);

                        ExtendedHyperlinkControlWrapper dmsh3 = getPane().getCtlTSTEXPLCTRACERLAYHyperlink();
                        dmsh3.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh4 = getPane().getCtlTSTEXPLCTRACERADJLAYHyperlink();
                        dmsh4.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh5 = getPane().getCtlTSTEXPLCTRACERAMDLAYHyperlink();
                        dmsh5.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh6 = getPane().getCtlTSTELCPAYPRIODIClayHyperlink();
                        dmsh6.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh7 = getPane().getCtlTSTELCManullayHyperlink();
                        dmsh7.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh8 = getPane().getCtlTSTELCBENCANlayHyperlink();
                        dmsh8.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh9 = getPane().getCtlTSTELCBENAMDlayHyperlink();
                        dmsh9.setUrl(TSTHyperlink);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }
                  
                  
                  
                  //sfms
                  try {
Loggers.general().info(LOG,"inside transfer sfms on post");
                        String getHyperSFMS = getIFSCSEARCH();
                        ExtendedHyperlinkControlWrapper ifscilctra = getPane().getCtlSFMSEXPLCTRACERLAYHyperlink();
                        ifscilctra.setUrl(getHyperSFMS);
                  }
                  catch(Exception e)
                  {
                        Loggers.general().info(LOG,"exception in sfms"+e.getMessage());
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

                  // if (step_csm.equalsIgnoreCase("CBS Authoriser") ||
                  // step_csm.equalsIgnoreCase("Authorise")) {
                  // ////Loggers.general().info(LOG,"Step id for service tax step---->" +
                  // step_csm + " and --->" + step_Input);
                  // if (getMinorCode().equalsIgnoreCase("AJT")) {
                  // getPane().onSERVICEEXPLCTRACOLDADJLAYButton();
                  // } else if (getMinorCode().equalsIgnoreCase("ATX")) {
                  // getPane().onSERVICEEXPLCTRACOLDAMDLAYButton();
                  // } else if (getMinorCode().equalsIgnoreCase("ITX")) {
                  // getPane().onSERVICEEXPLCTRACERLAYButton();
                  // } else if (getMinorCode().equalsIgnoreCase("NTRJ")) {
                  // getPane().onSERVICEEXPLCTRACERADJLAYButton();
                  // } else if (getMinorCode().equalsIgnoreCase("NTRM")) {
                  // getPane().onSERVICEEXPLCTRACERAMDLAYButton();
                  // } else {
                  // //Loggers.general().info(LOG,"service tax step else---->");
                  // }
                  //
                  // } else {
                  // Loggers.general().info(LOG,"Service tax called other the button action");
                  // }
                  // String refNumber = getDriverWrapper().getEventFieldAsText("EMST",
                  // "r", "").trim();
                  // //Loggers.general().info(LOG,"refNumber ----->" + refNumber);
                  // String evnt = getDriverWrapper().getEventFieldAsText("EPF", "s",
                  // "");
                  // //Loggers.general().info(LOG,"event refNumber ----->" + evnt);
                  // String evvcount = getDriverWrapper().getEventFieldAsText("ESQ",
                  // "i", "");
                  // //Loggers.general().info(LOG,"Event count for posting----->" + evvcount);
                  // try {
                  // String seriveceTAX = "";
                  // String SeriveSSS = "";
                  // String Krishi = "";
                  // String sql = "SELECT TRIM(SERVICE_TAX),
                  // TRIM(EDU_CESS),TRIM(KRISHI_CESS) from ETTV_SERVICETAX_SWACH_CALC
                  // where MASTER_REF='"
                  // + refNumber + "' AND REFNO_SERL=" + evvcount + " and
                  // REFNO_PFIX='" + evnt + "'";
                  // //Loggers.general().info(LOG,"Service TAX query" + sql);
                  // con = ConnectionMaster.getConnection();
                  // PreparedStatement ps = con.prepareStatement(sql);
                  // ResultSet rs = ps.executeQuery();
                  // while (rs.next()) {
                  // seriveceTAX = rs.getString(1);
                  // // //Loggers.general().info(LOG,"tax service");
                  // SeriveSSS = rs.getString(2);
                  // Krishi = rs.getString(3);
                  //
                  // getPane().setSERTAX_Name(seriveceTAX);
                  // getPane().setSERTAXCY_Name("INR");
                  // getPane().setSURTAX_Name(SeriveSSS);
                  // getPane().setSURTAXCY_Name("INR");
                  //
                  // getPane().setKRISHI(Krishi);
                  // getPane().setKRICUR("INR");
                  // }
                  // } catch (Exception ee) {
                  // //Loggers.general().info(LOG,"Service TAX Catch--->" + ee.getMessage());
                  // // //Loggers.general().info(LOG,"Service TAX Catch");
                  // }
                  try {
                        currencyCalc();
                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception in currency value population---->" + e.getMessage());

                        }
                  }
                  // GETTING LOB
                  try {
                        // //Loggers.general().info(LOG,"get value for LOB");
                        getLob();
                  } catch (Exception ee) {
                        // Loggers.general().info(LOG,ee.getMessage());
                        // //Loggers.general().info(LOG,"LOB Catch");
                  } finally {
                        // //Loggers.general().info(LOG,"finally LOB ");
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
                  try {
                        Loggers.general().info(LOG,"inside transfer sfms on validate");
                                                String getHyperSFMS = getIFSCSEARCH();
                                                ExtendedHyperlinkControlWrapper ifscilctra = getPane().getCtlSFMSEXPLCTRACERLAYHyperlink();
                                                ifscilctra.setUrl(getHyperSFMS);
                                          }
                                          catch(Exception e)
                                          {
                                                Loggers.general().info(LOG,"exception in sfms"+e.getMessage());
                                          }

                  // FCY Tax calculation
                  
                  try {

                        getFCCTCALCULATION();

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Exception update" +
                        // e.getMessage());
                  }
                  // Notes Populated in Summary
                  String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim(); //

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
            }
      }
}