package com.misys.tiplus2.customisation.extension;

//import org.apache.log4j.Logger;

import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.misys.tiplus2.foundations.lang.logging.Loggers;
public class PostingCustom extends PostingExtension {
      //private static final Logger logger = Logger.getLogger(ConnectionMaster.class.getName());
      private static final Logger LOG = LoggerFactory.getLogger(PostingCustom.class);
      public void addExtraSettlementFields() {
            //
      }

      @Override
      public void addExtraFields() {

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

            //// Loggers.general().info(LOG,"MigrationDone value in posting -------->" +
            //// dailyval);

            if (dailyval.equalsIgnoreCase("NO")) {

                  String eventStatus = getDriverWrapper().getEventFieldAsText("EVSS", "s", "", "E");
                  String stepID = getDriverWrapper().getEventFieldAsText("CSID", "s", "", "E");
                  if (!stepID.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {
                        try {

                              String extevente = getDriverWrapper().getEventFieldAsText("cAHW", "l", "", "E");
                              // String
                              // valdate=getDriverWrapper().getPostingFieldAsText(argCode,
                              // partCode)
                              // //Loggers.general().info(LOG,"force debit check box value----->"
                              // +
                              // extevente);
                              if (extevente.equalsIgnoreCase("Y")) {

                                    getWrapper().setFORCDBT("Y");

                              } else {

                                    getWrapper().setFORCDBT(extevente);
                              }

                        }

                        catch (Exception e) {
                              Loggers.general().info(LOG,"posting custom Exception for force debit---->" + e.getMessage());
                        }

                        // Force Limit

                        try {

                              String limit = getDriverWrapper().getEventFieldAsText("cBOY", "l", "", "E");

                              if (limit.equalsIgnoreCase("Y")) {

                                    getWrapper().setFORPOST("Y");
                                    //// Loggers.general().info(LOG,"Force Limit flag is after get
                                    //// the
                                    //// value " + getWrapper().getFORPOST());
                              } else {

                                    getWrapper().setFORPOST(limit);
                              }

                        }

                        catch (Exception e) {
                              Loggers.general().info(LOG,"posting custom Exception for force Limit---->" + e.getMessage());
                        }

                        String strLog = "Log";
                        String dailyval_Log = "";
                        AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                                    .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
                        EXTGENCUSTPROP CodeLog = queryLog.getUnique();
                        if (CodeLog != null) {

                              dailyval_Log = CodeLog.getPropval();
                        } else {
                              // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

                        }

                        String eventCode = getDriverWrapper().getEventFieldAsText("EVCD", "s", "", "E");
                        String prodCode = getDriverWrapper().getEventFieldAsText("PCO", "s", "", "E");

                        // Netting value code - 22/01/17

                        String strTCD = "";
                        String strPSID = "";
                        String stract_Type = "";
                        String strNetting = "";
                        String strRTGS = "";
                        String strPDS="";
                        int index;
                        int index1;
                        try {

                              // //Loggers.general().info(LOG,"Initially netting value----->");

                              strTCD = getDriverWrapper().getPostingFieldAsText("TCD", "").trim();
                              stract_Type = getDriverWrapper().getPostingFieldAsText("ACT", "").trim();
                              strPSID = getDriverWrapper().getPostingFieldAsText("PSID", "").trim();
                              strPDS = getDriverWrapper().getPostingFieldAsText("PDSC", "").trim().toUpperCase();

                              // String nett =
                              // getDriverWrapper().getEventFieldAsText("cBME",
                              // "p", "", "E");
                              // Loggers.general().info(LOG,"Netting Account type----->" +
                              // stract_Type);
                              getWrapper().setNETTING("");
                              //TIPLUS-174 start
                              index1 = strPDS.indexOf("OTHER BANK CHARGES");
                              if(index1 != - 1)
                              {

                              getWrapper().setNETTING("");
                              }
                              else{
                                    ////TIPLUS-174 start
                              if (stract_Type.trim().equalsIgnoreCase("") || stract_Type.startsWith("CA")
                                          || stract_Type.startsWith("CC") || stract_Type.startsWith("OD")) {
                                    //strNetting = strTCD + "-" + strPSID;//for current account no need to populate
                                    
                                     index = strPDS.indexOf("INTEREST");
                                    
                                     if(index != - 1) {
                                     strNetting = strTCD + "-" + "I-" + strPSID; // Posting description for interest
                                     } else {
                                     strNetting = "";
                                     }

                                    getWrapper().setNETTING(strNetting); // Unnetting
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Netting in account type----->" + stract_Type + " And===>" + strNetting);
                                    }
                              }

                              if ((strTCD.equalsIgnoreCase("227") || strTCD.equalsIgnoreCase("727"))
                                          && (!stract_Type.trim().equalsIgnoreCase("CN"))) {

                                    String strCCI = getDriverWrapper().getPostingFieldAsText("CCI", "").trim();
                                    Loggers.general().info(LOG,"Transaction code 2" + strCCI);
                                    if ((!strCCI.equalsIgnoreCase("")) && strCCI.length() > 0 && !strPSID.equalsIgnoreCase("")) {

                                          strNetting = strTCD + "-" + strPSID + "-" + strCCI;

                                          getWrapper().setNETTING(strNetting);
                                    }

                              }

                              else if ((strTCD.equalsIgnoreCase("228") || strTCD.equalsIgnoreCase("728"))
                                          && (!stract_Type.trim().equalsIgnoreCase("CN"))) {
                                     Loggers.general().info(LOG,"else netting value----->" +
                                     strTCD);
                                    strNetting = strTCD + "-" + strPSID;

                                    getWrapper().setNETTING(strNetting);
                              } else {
                                    // getWrapper().setNETTING("");
                                    Loggers.general().info(LOG,"228 or 728");
                              }

                              if (stract_Type.trim().equalsIgnoreCase("CN")) {
                                    strNetting = ""; // Netting
                                    getWrapper().setNETTING(strNetting);
                              }

                              if (strPSID.startsWith("OTHB")) {
                                    // Loggers.general().info(LOG,"Other bank charges====>" +
                                    // strCCI);
                                    strNetting = "";
                                    getWrapper().setNETTING(strNetting);
                              }

                              Loggers.general().info(LOG,"event code"+eventCode);
                              Loggers.general().info(LOG,"Product Code"+prodCode);
                              /*if (((prodCode.equalsIgnoreCase("ODC") && eventCode.equalsIgnoreCase("CLP"))
                                          || eventCode.equalsIgnoreCase("FEC") || eventCode.equalsIgnoreCase("CSA1"))
                                          || (eventCode.equalsIgnoreCase("POD") || eventCode.equalsIgnoreCase("CSA4"))) {*/
                              if ((prodCode.equalsIgnoreCase("ODC") && eventCode.equalsIgnoreCase("CLP"))
                                          || eventCode.equalsIgnoreCase("FEC") || eventCode.equalsIgnoreCase("CSA1")
                                          || eventCode.equalsIgnoreCase("POD") || eventCode.equalsIgnoreCase("CSA4")) {

                                    String strPDSC = getDriverWrapper().getPostingFieldAsText("TCD", "").trim();
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Preshipment Repayment transaction code====>" + strPDSC);
                                    }
                                    if (strPDSC.equalsIgnoreCase("943") || strPDSC.equalsIgnoreCase("944")) {
                                          strNetting = "";
                                          getWrapper().setNETTING(strNetting);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Preshipment Repayment if loop====>" + strPDSC);
                                                Loggers.general().info(LOG,"Event code and product code for netting if loop" + getWrapper().getNETTING());
                                          }
                                    } else {
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Preshipment Repayment else loop====>" + strPDSC);

                                          }
                                    }
                              } else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Event code and product code for netting else"
                                                      + getWrapper().getNETTING() + eventCode);
                                    }
                              }

                              if (stract_Type.trim().equalsIgnoreCase("RTGS")) {
                                    strRTGS = getDriverWrapper().getPostingFieldAsText("SYP", "").trim();
                                    strPSID = getDriverWrapper().getPostingFieldAsText("PSID", "").trim();
                                    if (strRTGS.equalsIgnoreCase("SP665")) {
                                          strNetting = strTCD + "-" + strPSID + "-" + strRTGS;
                                          getWrapper().setNETTING(strNetting);
                                    }

                              }
                              }
                        }

                        catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Exception in posting NETTING====>" + e.getMessage());
                              }
                        }

                        // LOB populated

                        try {

                              getWrapper().setLOBCOD("");
                              String lob = "";
                              
                              String templob="";
                              
                              if (prodCode.equalsIgnoreCase("FSA") && (!eventCode.equalsIgnoreCase("CSA"))) {
                                    lob = getDriverWrapper().getEventFieldAsText("cBOD", "s", "", "E").trim();
                                    Loggers.general().info(LOG,"Lob cBOD==>"+lob);
                                    templob=getDriverWrapper().getEventFieldAsText("cBOD", "s", "", "").trim();
                                    Loggers.general().info(LOG,"tempLob cBOD==>"+templob);
                                    
                              } else {

                                    lob = getDriverWrapper().getEventFieldAsText("cAAH", "s", "", "E").trim();
                                    Loggers.general().info(LOG,"Lob cAAH==>"+lob);
                                    templob=getDriverWrapper().getEventFieldAsText("cAAH", "s", "", "").trim();
                                    Loggers.general().info(LOG,"tempLob cAAH==>"+templob);
                              }

                              String act = getDriverWrapper().getPostingFieldAsText("ACT", "").trim();
                              // String act_type = act.substring(0, 1);
                              Loggers.general().info(LOG,"ACT==>"+act);
                              Loggers.general().info(LOG,"Lob length"+lob.length());
                              Loggers.general().info(LOG,"templob length"+templob.length());

                              if (lob.length() > 0 && !act.equalsIgnoreCase("YS") && !act.equalsIgnoreCase("RTGS")
                                          && !act.equalsIgnoreCase("CN")) {
                                    getWrapper().setLOBCOD(lob);
                                    Loggers.general().info(LOG,"lobcode"+getWrapper().getLOBCOD());

                              } else {
                                    getWrapper().setLOBCOD("");
                                    Loggers.general().info(LOG,"lobcode in else==>"+getWrapper().getLOBCOD());
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    }
                              }

                              // act.startsWith("OD") || act.startsWith("CA") ||
                              // act.startsWith("SB") for CASA transactions // MISYS-5130
                              // ,MISYS-5169 , MISYS-5168

                              // act.startsWith("YX") for deal reconciliation transactions
                              // //
                              // MISYS-5281

                              if (act.equalsIgnoreCase("YS") || act.equalsIgnoreCase("RTGS") || act.equalsIgnoreCase("CN")
                                          || act.startsWith("OD") || act.startsWith("CA") || act.startsWith("SB")
                                          || act.startsWith("YX") || act.startsWith("C5A") || act.startsWith("CV")
                                          || act.startsWith("DJ") || act.startsWith("DK") || act.startsWith("YH")
                                          || act.startsWith("R3") || act.startsWith("CCOD") || act.startsWith("DDA")
                                          || act.startsWith("SUB") || act.startsWith("TDA") || act.startsWith("TFX")
                                          || act.startsWith("AC")) {

                                    getWrapper().setLOBCOD("");
                                    Loggers.general().info(LOG,"Lob code in account types"+getWrapper().getLOBCOD());

                              }
                              Loggers.general().info(LOG,"Lob code at  the last"+getWrapper().getLOBCOD());
                        }

                        catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Exception LOB is after get the value---->" + e.getMessage());
                              }
                        }
                        
                        //Cust id and Treasury refrence number done by vishal gondhwania
                  /*    try {

                              String treasury1 = getDriverWrapper().getEventFieldAsText("cBQA", "s", "", "E");
                              String treasury2 = getDriverWrapper().getEventFieldAsText("cBQB", "s", "", "E");
                              String treasury3 = getDriverWrapper().getEventFieldAsText("cBQC", "s", "", "E");
                              String treasury4 = getDriverWrapper().getEventFieldAsText("cBQD", "s", "", "E");
                              String treasury5 = getDriverWrapper().getEventFieldAsText("cBQE", "s", "", "E");
                              String treasamt1 = getDriverWrapper().getEventFieldAsText("cBQL", "v", "m", "E");
                              String treasamt2 = getDriverWrapper().getEventFieldAsText("cBQN", "v", "m", "E");
                              String treasamt3 = getDriverWrapper().getEventFieldAsText("cBQO", "v", "m", "E");
                              String treasamt4 = getDriverWrapper().getEventFieldAsText("cBQP", "v", "m", "E");
                              String treasamt5 = getDriverWrapper().getEventFieldAsText("cBQQ", "v", "m", "E");
                              String treasccy1 = getDriverWrapper().getEventFieldAsText("cBQL", "v", "c", "E");
                              String treasccy2 = getDriverWrapper().getEventFieldAsText("cBQN", "v", "c", "E");
                              String treasccy3 = getDriverWrapper().getEventFieldAsText("cBQO", "v", "c", "E");
                              String treasccy4 = getDriverWrapper().getEventFieldAsText("cBQP", "v", "c", "E");
                              String treasccy5 = getDriverWrapper().getEventFieldAsText("cBQQ", "v", "c", "E");
                              String forward1 = getDriverWrapper().getEventFieldAsText("cBQF", "s", "", "E");
                              String forward2 = getDriverWrapper().getEventFieldAsText("cBQG", "s", "", "E");
                              String forward3 = getDriverWrapper().getEventFieldAsText("cBQH", "s", "", "E");
                              String forward4 = getDriverWrapper().getEventFieldAsText("cBQI", "s", "", "E");
                              String forward5 = getDriverWrapper().getEventFieldAsText("cBQJ", "s", "", "E");
                              String forwardamt1 = getDriverWrapper().getEventFieldAsText("cBQM", "v", "m", "E");
                              String forwardamt2 = getDriverWrapper().getEventFieldAsText("cBQR", "v", "m", "E");
                              String forwardamt3 = getDriverWrapper().getEventFieldAsText("cBQS", "v", "m", "E");
                              String forwardamt4 = getDriverWrapper().getEventFieldAsText("cBQT", "v", "m", "E");
                              String forwardamt5 = getDriverWrapper().getEventFieldAsText("cBQU", "v", "m", "E");
                              String forwardccy1 = getDriverWrapper().getEventFieldAsText("cBQM", "v", "c", "E");
                              String forwardccy2 = getDriverWrapper().getEventFieldAsText("cBQR", "v", "c", "E");
                              String forwardccy3 = getDriverWrapper().getEventFieldAsText("cBQS", "v", "c", "E");
                              String forwardccy4 = getDriverWrapper().getEventFieldAsText("cBQT", "v", "c", "E");
                              String forwardccy5 = getDriverWrapper().getEventFieldAsText("cBQU", "v", "c", "E");
                              String fincustid = getDriverWrapper().getEventFieldAsText("B+FT", "p", "cu", "E");
                              System.out.println("forward reference number "+forward1+" ,"+forward2);
                              
                              
                              
                              Loggers.general().info(LOG,"Treasury refrence number "+treasury1);
                              Loggers.general().info(LOG,"Treasury refrence number "+fincustid);
                              
                              
                        getWrapper().setFINCSTID(fincustid);
                        getWrapper().setTREASRF1(treasury1);
                        getWrapper().setTREASRF2(treasury2);
                        getWrapper().setTREASRF3(treasury3);
                        getWrapper().setTREASRF4(treasury4);
                        getWrapper().setTREASRF5(treasury5);
                        getWrapper().setTRERFAM1(treasamt1);
                        getWrapper().setTRSRFAM2(treasamt2);
                        getWrapper().setTRSRFAM3(treasamt3);
                        getWrapper().setTRSRFAM4(treasamt4);
                        getWrapper().setTRSRFAM5(treasamt5);
                        getWrapper().setTRESCCY1(treasccy1);
                        getWrapper().setTRESCCY2(treasccy2);
                        getWrapper().setTRESCCY3(treasccy3);
                        getWrapper().setTRESCCY4(treasccy4);
                        getWrapper().setTRESCCY5(treasccy5);
                        getWrapper().setFWDCNTRF1(forward1);
                        getWrapper().setFWDCTRF2(forward2);
                        getWrapper().setFWDCTRF3(forward3);
                        getWrapper().setFWDCTRF4(forward4);
                        getWrapper().setFWDCTRF5(forward5);
                        getWrapper().setFWDCTAM1(forwardamt1);
                        getWrapper().setFWDCTAM2(forwardamt2);
                        getWrapper().setFWDCTAM3(forwardamt3);
                        getWrapper().setFWDCTAM4(forwardamt4);
                        getWrapper().setFWDCTAM5(forwardamt5);
                        getWrapper().setFWDCCY1(forwardccy1);
                        getWrapper().setFWDCCY2(forwardccy2);
                        getWrapper().setFWDCCY3(forwardccy3);
                        getWrapper().setFWDCCY4(forwardccy4);
                        getWrapper().setFWDCCY5(forwardccy5);
                        

                  }
                        catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Exception LOB is after get the value---->" + e.getMessage());
                              }
                        }*/

                  ///////// Netting logic using SQL query - This was used as
                  ///////// workaround when ForceDebit event field value not working
                  //
                  // String fxrateQuery = "SELECT ext.forcdebt from extevent ext join
                  ///////// baseevent bev on bev.key97 = ext.event join master mas on
                  ///////// mas.key97=bev.master_key where mas.master_ref='"
                  // + Masterref + "' AND BEV.REFNO_PFIX = '" + evnt + "' AND
                  ///////// BEV.REFNO_SERL='" + evvcount + "'";
                  // //Loggers.general().info(LOG,"settlement details Query: \n" +
                  ///////// fxrateQuery);
                  // Connection connect = ConnectionMaster.getConnection();
                  //
                  // PreparedStatement pstmt = connect.prepareStatement(fxrateQuery);
                  // ResultSet result = pstmt.executeQuery();
                  // // result.getMetaData().getColumnCount();
                  // while (result.next()) {
                  // //Loggers.general().info(LOG,"posting custom if block---->" +
                  ///////// result.getString(1));
                  // getWrapper().setFORCDBT(result.getString(1));
                  //
                  // }
                  // //Loggers.general().info(LOG,"posting custom out of
                  ///////// loop------------->>>>>>>>>>>" +
                  ///////// getWrapper().getFORCDBT());
                  //

                  // return;
            }
            }

      }
}