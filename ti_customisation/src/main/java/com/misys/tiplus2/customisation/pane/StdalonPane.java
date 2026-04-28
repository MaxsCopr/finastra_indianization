package com.misys.tiplus2.customisation.pane;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

//import org.apache.log4j.Logger;

import com.bs.theme.transport.client.ThemeTransportClient;
import com.misys.tiplus2.apps.ti.kernel.extpm.entity.ExtEvent;
import com.misys.tiplus2.apps.ti.kernel.extpm.entity.Key_ExtEvent;
import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;
import com.misys.tiplus2.customisation.entity.ExtEventAdvanceTableEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventLienMark;
import com.misys.tiplus2.customisation.entity.ExtEventLienMarkEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventLienMarking;
import com.misys.tiplus2.customisation.entity.ExtEventLienMarkingEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventLoanDetailsEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventServiceTax;
import com.misys.tiplus2.customisation.entity.ExtEventServiceTaxEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventForwardContract;
import com.misys.tiplus2.customisation.entity.ExtEventForwardContractEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventForwardCharge;
import com.misys.tiplus2.customisation.entity.ExtEventForwardChargeEntityWrapper;
import com.misys.tiplus2.customisation.extension.ConnectionMaster;
import com.misys.tiplus2.customisation.extension.OdcFEC;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import com.misys.tiplus2.enigma.customisation.validation.ValidationTexts;
import com.misys.tiplus2.enigma.lang.control.EnigmaControl;
import com.misys.tiplus2.enigma.lang.datatype.EnigmaArray;
import com.misys.tiplus2.foundations.lang.logging.Loggers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.misys.tiplus2.foundations.lang.logging.Loggers;
public class StdalonPane extends EventPane {
      //private static final Logger logger = Logger.getLogger(ConnectionMaster.class.getName());
      private static final Logger LOG = LoggerFactory.getLogger(StdalonPane.class);
      Connection con, con1 = null;
      PreparedStatement peco, ps1, ps, dmsp, ttRatePS, psd ,pst= null;
      ResultSet dmsr1, rs1, rs, dmsr, ttRateRS, rst = null;
      String swachhCharge = "";
      String serviceTax = "";

      public static String randomCorrelationId() {
            // Loggers.general().info(LOG,"randomCorrelationId generate");
            return UUID.randomUUID().toString();
      }

      // public void onSERVICEISSUESHIPGROUPLayButton() {
      //
      // if (SERVICFECTH()) {
      // } else {
      // // Loggers.general().info(LOG,"Else systemOutput");
      // }
      //
      // }

      // public void onSERVICESHPGURETURNclayButton() {
      // if (SERVICFECTH()) {
      // } else {
      // // Loggers.general().info(LOG,"Else systemOutput");
      // }
      //
      // }

      // public void onSERVICESHPGUAMDlayButton() {
      //
      // if (SERVICFECTH()) {
      // // Loggers.general().info(LOG,"onSERVICEELCADVclayButton calling");
      // } else {
      // // Loggers.general().info(LOG,"Else systemOutput");
      // }
      //
      // }

      // public void onSERVICESHPGUACREclayButton() {
      //
      // if (SERVICFECTH()) {
      // // Loggers.general().info(LOG,"onSERVICEELCADVclayButton calling");
      // } else {
      // // Loggers.general().info(LOG,"Else systemOutput");
      // }
      //
      // }

      // public void onSERVICESHPGUALODCLAIMclayButton() {
      //
      // if (SERVICFECTH()) {
      // // Loggers.general().info(LOG,"onSERVICEELCADVclayButton calling");
      // } else {
      // // Loggers.general().info(LOG,"Else systemOutput");
      // }
      //
      // }

      // public void onSERVICESHPGUADJUSTlayButton() {
      //
      // // Loggers.general().info(LOG,"SERVICE Button");
      // if (SERVICFECTH()) {
      // // Loggers.general().info(LOG,"onSERVICEELCADVclayButton calling");
      // } else {
      // // Loggers.general().info(LOG,"Else systemOutput");
      // }
      //
      // }

      // public void onSERVICESHPGUASETTCLAclayButton() {
      // }

      // public void onSERVICESHPGURELayButton() {
      //
      // if (SERVICFECTH()) {
      // // Loggers.general().info(LOG,"onSERVICEELCADVclayButton calling");
      // } else {
      // // Loggers.general().info(LOG,"Else systemOutput");
      // }
      //
      // }

      // public boolean SERVICFECTH() {
      // boolean value = false;
      // getExtEventServiceTaxNew().setEnabled(false);
      // getExtEventServiceTaxDelete().setEnabled(false);
      // getExtEventServiceTaxUpdate().setEnabled(false);
      // getExtEventServiceTaxUp().setEnabled(false);
      // getExtEventServiceTaxDown().setEnabled(false);
      // getExtEventServiceTaxView().setEnabled(false);
      // String masterRefNumber = getDriverWrapper().getEventFieldAsText("MST",
      // "r", "");
      // // //Loggers.general().info(LOG,"Master Reference" + masterRefNumber);
      //
      // String eventPrefix = getDriverWrapper().getEventFieldAsText("EPF", "s",
      // "");
      // String eventPrefixSerialNo =
      // getDriverWrapper().getEventFieldAsText("ESQ", "i", "");
      // Integer eventPrefixNo = Integer.parseInt(eventPrefixSerialNo);
      // EnigmaArray<ExtEventServiceTaxEntityWrapper> liste =
      // getExtEventServiceTaxData();
      // int count = 0;
      // Iterator<ExtEventServiceTaxEntityWrapper> iterator = liste.iterator();
      // // Loggers.general().info(LOG,"getServiceTAX fetching--->" + liste.getSize());
      // // for (int i = 0; i < seivce.size(); i++) {
      // // ExtEventServiceTax serviceTax = seivce.get(i);
      // // }
      //
      // if (liste.getSize() < 1) {
      // try {
      // // Loggers.general().info(LOG,"calling service tax query");
      // String query = "SELECT
      // TRIM(DESCR),TRIM(CHARGE_AMT),TRIM(SERVICE_TAX),TRIM(EDU_CESS),TRIM(KRISHI_CESS)
      // FROM ETTV_SERVICETAX_SWACH_CALC WHERE REFNO_PFIX='"
      // + eventPrefix + "' AND REFNO_SERL=" + eventPrefixNo + " AND MASTER_REF='"
      // + masterRefNumber
      // + "'";
      //
      // // Loggers.general().info(LOG,"Service tax query - " + query);
      // con = ConnectionMaster.getConnection();
      // PreparedStatement ps1 = con.prepareStatement(query);
      // ResultSet rs1 = ps1.executeQuery();
      //
      // while (rs1.next()) {
      // Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
      // ExtEventServiceTax serviceTax = new ExtEventServiceTax();
      // serviceTax.setColumn("DESCR", rs1.getString(1));
      // serviceTax.setColumn("CHRGAMT", rs1.getString(2) + " " + "INR");
      // serviceTax.setColumn("SERVTAX", rs1.getString(3) + " " + "INR");
      // serviceTax.setColumn("EDUCES", rs1.getString(4) + " " + "INR");
      // serviceTax.setColumn("KRISH", rs1.getString(5) + " " + "INR");
      // serviceTax.setNewKey();
      // serviceTax.setFk(fkey);
      // serviceTax.setSequence(count);
      //
      // ExtEventServiceTaxEntityWrapper projectdetchk = new
      // ExtEventServiceTaxEntityWrapper(serviceTax,
      // getDriverWrapper());
      // addNewExtEventServiceTax(projectdetchk);
      //
      // count++;
      //
      // getExtEventServiceTaxNew().setEnabled(false);
      // getExtEventServiceTaxDelete().setEnabled(false);
      // getExtEventServiceTaxUpdate().setEnabled(false);
      // getExtEventServiceTaxUp().setEnabled(false);
      // getExtEventServiceTaxDown().setEnabled(false);
      // getExtEventServiceTaxView().setEnabled(false);
      // }
      //
      // } catch (Exception e) {
      // // Loggers.general().info(LOG,"Exeception of service tax- " +
      // // e.getMessage());
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
      //
      // } else {
      // // Loggers.general().info(LOG,"Service tax grid value greater then 1 ");
      // }
      // return value;
      // }

      public void oncalcmarginSHPGUADJUSTlayButton() {
            MarginCAL();
      }

      public void oncalcmarginSHPGUAMDlayButton() {
            MarginCAL();
      }

      public void oncalcmarginSHPGURELayButton() {
            MarginCAL();
      }

      public void onFETCHLIENSHPGURELayButton() {
            lienMark();
      }

      public void onREVERSELIENSHPGURELayButton() {
            lienReverse();
      }

      public void onFETCHLIENSHPGUADJUSTlayButton() {
            lienMark();
      }

      public void onREVERSELIENSHPGUADJUSTlayButton() {
            lienReverse();
      }

      public void onREVERSELIENSHPGUAMDlayButton() {
            lienReverse();
      }

      public void onFETCHLIENSHPGUAMDlayButton() {
            lienMark();
      }

      public void onREVERSELIENISSUESHIPGROUPLayButton()

      {

            String releaseacc = getLIENBAL().trim();
            String behalfBranch = getDriverWrapper().getEventFieldAsText("BOB", "s", "");
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
            // Loggers.general().info(LOG,"Lien reversal text box=====>" + releaseacc);
            if (releaseacc.equalsIgnoreCase("") || releaseacc == null) {

                  try {
                        // Loggers.general().info(LOG,"Reverse service method calling started
                        // IGT Adjust in if loop");
                        String headerReq = "<?xml version=$1.0$ encoding=$UTF-8$?><ServiceRequest xmlns=$urn:control.services.tiplus2.misys.com$ xmlns:ns2=$urn:messages.service.ti.apps.tiplus2.misys.com$ xmlns:ns3=$urn:common.service.ti.apps.tiplus2.misys.com$ xmlns:ns4=$urn:custom.service.ti.apps.tiplus2.misys.com$><RequestHeader><Service>Customization</Service><Operation>FDLienRemoval</Operation><Credentials><Name>SUPERVISOR</Name></Credentials><ReplyFormat>FULL</ReplyFormat><TargetSystem>KOTAKEXT</TargetSystem><SourceSystem>ZONE1</SourceSystem><NoRepair>Y</NoRepair><NoOverride>Y</NoOverride><CorrelationId>Correlation_Id</CorrelationId><TransactionControl>NONE</TransactionControl></RequestHeader><fdlienremoval xmlns:xsi=$http://www.w3.org/2001/XMLSchema-instance$>";
                        String endXml = "</fdlienremoval></ServiceRequest>";
                        String gridXml = "";
                        String lienStatus = "";
                        String lienbox = "";
                        EnigmaArray<ExtEventLienMarkEntityWrapper> list = getExtEventLienMarkData();
                        for (ExtEventLienMarkEntityWrapper fdwrapper : list) {
                              // //Loggers.general().info(LOG,"11112212121");
                              String marginAmt = fdwrapper.getMARGAM();
                              String marginAcc = fdwrapper.getDEPOSNO().trim();
                              String luCur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                              String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                              String eventRefNo = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                              String lien_id = fdwrapper.getLIENIDN();
                              lienStatus = fdwrapper.getLINESTS().trim();
                              lienbox = lienbox + lienStatus;
                              // Loggers.general().info(LOG,"Lien Status " + lienbox);
                              if ((!lienbox.equalsIgnoreCase("") || lienbox != null)
                                          && lienStatus.equalsIgnoreCase("MARK SUCCEEDED")) {

                                    String fdDefaultXml = "<FDRow><MasterReference>Maseter_ref</MasterReference><EventReference>Event_ref</EventReference><BehalfOfBranch>behalf_Branch</BehalfOfBranch><AccountNumber>Account_Number</AccountNumber><LienId>lienRev_id</LienId><Amount>Amount_Ccy</Amount><Currency>Lc_Ccy</Currency><Remarks>masRefNo</Remarks></FDRow>";
                                    // String endXml = "</fdlien></ServiceRequest>";
                                    fdDefaultXml = fdDefaultXml.replace("Maseter_ref", masRefNo);
                                    fdDefaultXml = fdDefaultXml.replace("Event_ref", eventRefNo);
                                    fdDefaultXml = fdDefaultXml.replace("behalf_Branch", behalfBranch);
                                    fdDefaultXml = fdDefaultXml.replace("Account_Number", marginAcc);
                                    fdDefaultXml = fdDefaultXml.replace("lienRev_id", lien_id);
                                    fdDefaultXml = fdDefaultXml.replace("Amount_Ccy", marginAmt);
                                    fdDefaultXml = fdDefaultXml.replace("Lc_Ccy", luCur);
                                    fdDefaultXml = fdDefaultXml.replace("masRefNo", masRefNo);
                                    gridXml = gridXml + fdDefaultXml;
                              } else {
                                    // Loggers.general().info(LOG,"Lien status is blank");
                              }

                        }
                        // //Loggers.general().info(LOG,"gridXml - "+gridXml);
                        // //Loggers.general().info(LOG,"gridXml final ----> "+gridXml);
                        // Final formation of request
                        char j = '"';
                        String val = Character.toString(j);
                        // Loggers.general().info(LOG,"char value - " + j);
                        headerReq = headerReq.replace("$", val);
                        // Replace correlation id
                        String correlationId = randomCorrelationId();
                        // Loggers.general().info(LOG,"correlationId " + correlationId);
                        headerReq = headerReq.replace("Correlation_Id", correlationId);
                        // //Loggers.general().info(LOG,"headerReq replaced with quotes - " +
                        // headerReq);

                        String finalXml = headerReq + gridXml + endXml;
                        // Loggers.general().info(LOG,"Themebridge finalXml -----> " +
                        // finalXml);

                        // sending to themebridge
                        ThemeTransportClient anThemeTransportClient = new ThemeTransportClient();
                        String resquestXML = anThemeTransportClient.invoke("Customization", "FDLienAdd", finalXml);
                        // Loggers.general().info(LOG,"Themebridge resquestXML" + resquestXML);

                        // String line = "12312~500~SUCCEEDED~Lien test
                        // reversal,222450~600~SUCCEEDED~Lien
                        // reversal,33333~700~FAILED~Lien
                        // failed";
                        String line = resquestXML;
                        // String reverse = "Reversal";
                        String[] sp_line = line.split(",");
                        for (int k = 0; k < sp_line.length; k++) {
                              String[] res_sp_line = sp_line[k].split("~");
                              EnigmaArray<ExtEventLienMarkEntityWrapper> leanDataList = getExtEventLienMarkData();
                              for (ExtEventLienMarkEntityWrapper str : leanDataList) {
                                    String currentDepositNum = res_sp_line[0];
                                    String depositNum = str.getDEPOSNO().trim();
                                    String lienst = str.getLINESTS().trim();
                                    // Loggers.general().info(LOG,"currentDepositNum - " +
                                    // currentDepositNum);
                                    // Loggers.general().info(LOG,"depositNum - " + depositNum);
                                    if (depositNum.equalsIgnoreCase(currentDepositNum)) {
                                          if (lienst.equalsIgnoreCase("MARK SUCCEEDED")
                                                      && (!lienbox.equalsIgnoreCase("") || lienbox != null)) {
                                                // Loggers.general().info(LOG,"update the lien status
                                                // blank");
                                                str.setLINESTS(res_sp_line[2]);
                                                // Loggers.general().info(LOG,"Lien id " +
                                                // res_sp_line[1]);
                                                str.setLIENIDN(res_sp_line[1]);
                                                str.setLIENREMK(res_sp_line[3]);
                                                // getBtnReverselienIMPLCCLAIMRECclay().setEnabled(false);
                                          } else if (lienst.equalsIgnoreCase("REVERSAL SUCCEEDED")) {

                                          } else {
                                                // Loggers.general().info(LOG,"update the lien status
                                                // FAILED");
                                                // str.setLIENID(res_sp_line[1]);
                                                // str.setLINEST(res_sp_line[2]);
                                                // str.setLIENREM(res_sp_line[3]);
                                          }
                                    } else {
                                          // Loggers.general().info(LOG,"depositNum is not equal
                                          // currentDepositNum in reversal");
                                    }

                              }
                        }
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"ThemeTransportClient Exceptions! " +
                        // e.getMessage());
                  }

            } else {
                  // Loggers.general().info(LOG,"Reverse service method calling started IGT
                  // Adjust in else====>");

                  try {
                        // Loggers.general().info(LOG,"Reverse service method calling started
                        // IGT Adjust");
                        String headerReq = "<?xml version=$1.0$ encoding=$UTF-8$?><ServiceRequest xmlns=$urn:control.services.tiplus2.misys.com$ xmlns:ns2=$urn:messages.service.ti.apps.tiplus2.misys.com$ xmlns:ns3=$urn:common.service.ti.apps.tiplus2.misys.com$ xmlns:ns4=$urn:custom.service.ti.apps.tiplus2.misys.com$><RequestHeader><Service>Customization</Service><Operation>FDLienRemoval</Operation><Credentials><Name>SUPERVISOR</Name></Credentials><ReplyFormat>FULL</ReplyFormat><TargetSystem>KOTAKEXT</TargetSystem><SourceSystem>ZONE1</SourceSystem><NoRepair>Y</NoRepair><NoOverride>Y</NoOverride><CorrelationId>Correlation_Id</CorrelationId><TransactionControl>NONE</TransactionControl></RequestHeader><fdlienremoval xmlns:xsi=$http://www.w3.org/2001/XMLSchema-instance$>";
                        String endXml = "</fdlienremoval></ServiceRequest>";
                        String gridXml = "";
                        String lienStatus = "";
                        String lienbox = "";
                        EnigmaArray<ExtEventLienMarkEntityWrapper> list = getExtEventLienMarkData();
                        for (ExtEventLienMarkEntityWrapper fdwrapper : list) {
                              // //Loggers.general().info(LOG,"11112212121");
                              String marginAmt = fdwrapper.getMARGAM();
                              String DepositNum = fdwrapper.getDEPOSNO().trim();
                              String marginAcc = getLIENBAL().trim();
                              String luCur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                              String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                              String eventRefNo = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                              String lien_id = fdwrapper.getLIENIDN();
                              lienStatus = fdwrapper.getLINESTS();
                              lienbox = lienbox + lienStatus;
                              // Loggers.general().info(LOG,"Lien Status " + lienbox);
                              if ((!lienbox.equalsIgnoreCase("") || lienbox != null)
                                          && (!marginAcc.equalsIgnoreCase("") || marginAcc != null)
                                          && marginAcc.equalsIgnoreCase(DepositNum)
                                          && lienStatus.equalsIgnoreCase("MARK SUCCEEDED")) {
                                    String fdDefaultXml = "<FDRow><MasterReference>Maseter_ref</MasterReference><EventReference>Event_ref</EventReference><BehalfOfBranch>behalf_Branch</BehalfOfBranch><AccountNumber>Account_Number</AccountNumber><LienId>lienRev_id</LienId><Amount>Amount_Ccy</Amount><Currency>Lc_Ccy</Currency><Remarks>masRefNo</Remarks></FDRow>";
                                    // String endXml = "</fdlien></ServiceRequest>";
                                    fdDefaultXml = fdDefaultXml.replace("Maseter_ref", masRefNo);
                                    fdDefaultXml = fdDefaultXml.replace("Event_ref", eventRefNo);
                                    fdDefaultXml = fdDefaultXml.replace("behalf_Branch", behalfBranch);
                                    fdDefaultXml = fdDefaultXml.replace("Account_Number", marginAcc);
                                    fdDefaultXml = fdDefaultXml.replace("lienRev_id", lien_id);
                                    fdDefaultXml = fdDefaultXml.replace("Amount_Ccy", marginAmt);
                                    fdDefaultXml = fdDefaultXml.replace("Lc_Ccy", luCur);
                                    fdDefaultXml = fdDefaultXml.replace("masRefNo", masRefNo);
                                    gridXml = gridXml + fdDefaultXml;
                              } else {
                                    // Loggers.general().info(LOG,"Lien status and lien deposit no
                                    // is blank" + DepositNum + "and====>" + marginAcc);
                              }
                        }
                        // //Loggers.general().info(LOG,"gridXml - "+gridXml);
                        // //Loggers.general().info(LOG,"gridXml final ----> "+gridXml);
                        // Final formation of request
                        char j = '"';
                        String val = Character.toString(j);
                        // Loggers.general().info(LOG,"char value - " + j);
                        headerReq = headerReq.replace("$", val);
                        // Replace correlation id
                        String correlationId = randomCorrelationId();
                        // Loggers.general().info(LOG,"correlationId " + correlationId);
                        headerReq = headerReq.replace("Correlation_Id", correlationId);
                        // //Loggers.general().info(LOG,"headerReq replaced with quotes - " +
                        // headerReq);

                        String finalXml = headerReq + gridXml + endXml;
                        // Loggers.general().info(LOG,"Themebridge finalXml -----> " +
                        // finalXml);

                        // sending to themebridge
                        ThemeTransportClient anThemeTransportClient = new ThemeTransportClient();
                        String resquestXML = anThemeTransportClient.invoke("Customization", "FDLienAdd", finalXml);
                        // Loggers.general().info(LOG,"Themebridge resquestXML" + resquestXML);

                        // String line = "12312~500~SUCCEEDED~Lien test
                        // reversal,222450~600~SUCCEEDED~Lien
                        // reversal,33333~700~FAILED~Lien
                        // failed";
                        String line = resquestXML;
                        // String reverse = "Reversal";
                        String[] sp_line = line.split(",");
                        for (int k = 0; k < sp_line.length; k++) {
                              String[] res_sp_line = sp_line[k].split("~");

                              EnigmaArray<ExtEventLienMarkEntityWrapper> leanDataList = getExtEventLienMarkData();
                              for (ExtEventLienMarkEntityWrapper str : leanDataList) {
                                    // String currentDepositNum = res_sp_line[0].trim();
                                    String currentDepositNum = str.getDEPOSNO().trim();
                                    String depositNum = getLIENBAL().trim();
                                    String lienst = str.getLINESTS().trim();
                                    // Loggers.general().info(LOG,"currentDepositNum - " +
                                    // currentDepositNum);
                                    // Loggers.general().info(LOG,"depositNum - " + depositNum);
                                    if (depositNum.equalsIgnoreCase(currentDepositNum)) {
                                          if (lienst.equalsIgnoreCase("MARK SUCCEEDED")
                                                      && (!lienbox.equalsIgnoreCase("") || lienbox != null)) {
                                                // Loggers.general().info(LOG,"update the lien status
                                                // blank");
                                                str.setLINESTS(res_sp_line[2]);
                                                // Loggers.general().info(LOG,"Lien id " +
                                                // res_sp_line[1]);
                                                str.setLIENIDN(res_sp_line[1]);
                                                str.setLIENREMK(res_sp_line[3]);
                                                // getBtnReverselienIMPLCCLAIMRECclay().setEnabled(false);
                                          } else if (lienst.equalsIgnoreCase("REVERSAL SUCCEEDED")) {
                                                // Loggers.general().info(LOG,"REVERSAL update=====>");

                                          } else {
                                                // Loggers.general().info(LOG,"update the lien status
                                                // FAILED");
                                                // str.setLIENID(res_sp_line[1]);
                                                // str.setLINEST(res_sp_line[2]);
                                                // str.setLIENREM(res_sp_line[3]);
                                          }
                                    } else {
                                          // Loggers.general().info(LOG,"depositNum is not equal
                                          // currentDepositNum in reversal");
                                    }

                              }

                        }
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"ThemeTransportClient Exceptions! " +
                        // e.getMessage());
                  }

            }

      }

      public void onfetchlienISSUESHIPGROUPLayButton() {

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

            String Margamt = "0";
            String gridamtcur = "";
            String result = "";
            String systemDate = getDriverWrapper().getEventFieldAsText("TDY", "d", "");
            String AcnoRem = "Check Space or Spl charactor should not be there";
            String behalfBranch = getDriverWrapper().getEventFieldAsText("BOB", "s", "").trim();
            getExtEventLienMarkingDelete().setEnabled(false);
            Margamt = getMARAMT().toString();
            String facility = getFACLTYID();
            int count_R = 0;
            int count_M = 0;
            if (Margamt.equalsIgnoreCase("") || Margamt == null) {
                  Margamt = "0";

            }
            BigDecimal margindec = new BigDecimal("0");
            BigDecimal margindecadd = new BigDecimal("0");
            BigDecimal marginbig = new BigDecimal("0");
            BigDecimal one = new BigDecimal(1);
            BigDecimal thousand = new BigDecimal(1000);
            BigDecimal hundred = new BigDecimal(100);
            String add_lie = Margamt.replaceAll("[^0-9]", "");
            // Loggers.general().info(LOG," MarginAmt in number----> " + add_lie);
            // Margamtdob = Double.valueOf(add_lie);
            BigDecimal MargamtNum = new BigDecimal(add_lie);
            BigDecimal Margamtdob = MargamtNum.divide(hundred);

            // Loggers.general().info(LOG,"Grid BigDecimal amount--->" + Margamtdob);

            getExtEventLienMarkDelete().setEnabled(false);

            if (dailyval_Log.equalsIgnoreCase("YES")) {
                  Loggers.general().info(LOG,"Grid BigDecimal amount--->" + Margamtdob);

            }

            EnigmaArray<ExtEventLienMarkEntityWrapper> liste2 = getExtEventLienMarkData();

            if (liste2.getSize() > 0) {
                  Iterator<ExtEventLienMarkEntityWrapper> iterator1 = liste2.iterator();

                  while (iterator1.hasNext()) {
                        ExtEventLienMarkEntityWrapper ExtEventLienMark = (ExtEventLienMarkEntityWrapper) iterator1.next();

                        String marginAmt = ExtEventLienMark.getMARGAMAmount().toString();
                        // Loggers.general().info(LOG,"Amount marginAmt String-----> " +
                        // marginAmt);

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Amount marginAmt String-----> " + marginAmt);

                        }
                        marginbig = new BigDecimal(marginAmt);

                        gridamtcur = ExtEventLienMark.getMARGAMCurrency().toString();

                        if (gridamtcur.equalsIgnoreCase("BHD") || gridamtcur.equalsIgnoreCase("KWD")
                                    || gridamtcur.equalsIgnoreCase("OMR")) {
                              margindec = marginbig.divide(thousand);
                        } else if (gridamtcur.equalsIgnoreCase("JPY") || gridamtcur.equalsIgnoreCase("KRW")) {

                              margindec = marginbig.divide(one);
                        } else {
                              margindec = marginbig.divide(hundred);
                        }
                        // Loggers.general().info(LOG,"Amount marginAmt bigdecimal in
                        // while-----> " + margindec);

                        margindecadd = margindecadd.add(margindec);

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Margin add amount-----> " + margindecadd);

                        }

                        String deptno = ExtEventLienMark.getDEPOSNO().trim();
                        String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
                        String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim();

                        // }
                  }

            }
            // Loggers.general().info(LOG,"count_Reversal====>" + count_R +
            // "count_Mark=====>" + count_M);

            // if (count_R >= count_M) {
            // Loggers.general().info(LOG,"If loop ==== > Account no is added new");
            if (facility.length() > 0) {
                  if (Margamtdob.compareTo(margindecadd) >= 0) {

                        try {

                              String headerReq = "<?xml version=$1.0$ encoding=$UTF-8$?><ServiceRequest xmlns=$urn:control.services.tiplus2.misys.com$ xmlns:ns2=$urn:messages.service.ti.apps.tiplus2.misys.com$ xmlns:ns3=$urn:common.service.ti.apps.tiplus2.misys.com$ xmlns:ns4=$urn:custom.service.ti.apps.tiplus2.misys.com$><RequestHeader><Service>Customization</Service><Operation>FDLienAdd</Operation><Credentials><Name>SUPERVISOR</Name></Credentials><ReplyFormat>FULL</ReplyFormat><TargetSystem>KOTAKEXT</TargetSystem><SourceSystem>ZONE1</SourceSystem><NoRepair>Y</NoRepair><NoOverride>Y</NoOverride><CorrelationId>Correlation_Id</CorrelationId><TransactionControl>NONE</TransactionControl></RequestHeader><fdlienadd xmlns:xsi=$http://www.w3.org/2001/XMLSchema-instance$>";
                              String endXml = "</fdlienadd></ServiceRequest>";
                              String gridXml = "";
                              String startDate = "";
                              String endDate = "";
                              EnigmaArray<ExtEventLienMarkEntityWrapper> list = getExtEventLienMarkData();
                              for (ExtEventLienMarkEntityWrapper fdwrapper : list) {
                                    String marginAmt = fdwrapper.getMARGAM();
                                    String marginAcc = fdwrapper.getDEPOSNO().trim();
                                    String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                                    String eventRefNo = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
                                    String issueDate = getDriverWrapper().getEventFieldAsText("ISS", "d", "");
                                    String mas_evnt = masRefNo;
                                    String expiryDate = getDriverWrapper().getEventFieldAsText("EXP", "d", "");
                                    DateFormat userDateFormat = new SimpleDateFormat("dd/mm/yy");
                                    DateFormat dateFormatNeeded = new SimpleDateFormat("yyyy-mm-dd");

                                    Date date;
                                    Date date1;

                                    try {
                                          date = (Date) userDateFormat.parse(issueDate);
                                          startDate = dateFormatNeeded.format(date);
                                          date1 = (Date) userDateFormat.parse(expiryDate);
                                          endDate = dateFormatNeeded.format(date1);

                                          SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yy");

                                          Date sysdate = (Date) formatter1.parse(systemDate);
                                          // Loggers.general().info(LOG,"systemDate Date lien----- " +
                                          // systemDate);
                                          result = formatter1.format(sysdate);
                                          // Loggers.general().info(LOG,"result Date ----- " +
                                          // result);

                                    } catch (ParseException e) {
                                          // Loggers.general().info(LOG,"Date Exception ");
                                    }
                                    String luCur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                                    String mas_evntRef = masRefNo;
                                    String lienbox = fdwrapper.getLINESTS();
                                    if (lienbox.equalsIgnoreCase("") || lienbox == null) {
                                          // Loggers.general().info(LOG,"Lien Status is blank");
                                          String fdDefaultXml = "<FDRow><MasterReference>Maseter_ref</MasterReference><EventReference>Event_ref</EventReference><BehalfOfBranch>behalf_Branch</BehalfOfBranch><AccountNumber>Account_Number</AccountNumber><Amount>Amount_Ccy</Amount><Currency>LC_Cur</Currency><ReasonCode>TRD</ReasonCode><Remarks>mas_evnt</Remarks><LienStartDate>Issue_Date</LienStartDate><LienEndDate>Expiry_Date</LienEndDate></FDRow>";
                                          fdDefaultXml = fdDefaultXml.replace("Maseter_ref", masRefNo);
                                          fdDefaultXml = fdDefaultXml.replace("Event_ref", eventRefNo);
                                          fdDefaultXml = fdDefaultXml.replace("behalf_Branch", behalfBranch);
                                          fdDefaultXml = fdDefaultXml.replace("Account_Number", marginAcc);
                                          fdDefaultXml = fdDefaultXml.replace("Amount_Ccy", marginAmt);
                                          fdDefaultXml = fdDefaultXml.replace("LC_Cur", luCur);
                                          fdDefaultXml = fdDefaultXml.replace("mas_evnt", mas_evntRef);
                                          fdDefaultXml = fdDefaultXml.replace("Issue_Date", startDate);
                                          fdDefaultXml = fdDefaultXml.replace("Expiry_Date", endDate);
                                          gridXml = gridXml + fdDefaultXml;
                                    } else {
                                          // Loggers.general().info(LOG,"Lien Status is not
                                          // blank");
                                    }

                              }

                              char j = '"';
                              String val = Character.toString(j);
                              headerReq = headerReq.replace("$", val);
                              String correlationId = randomCorrelationId();
                              // Loggers.general().info(LOG,"correlationId " + correlationId);
                              headerReq = headerReq.replace("Correlation_Id", correlationId);
                              String finalXml = headerReq + gridXml + endXml;
                              // Loggers.general().info(LOG,"Themebridge finalXml " +
                              // finalXml);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Themebridge finalXml " + finalXml);

                              }
                              ThemeTransportClient anThemeTransportClient = new ThemeTransportClient();
                              String responseXML = anThemeTransportClient.invoke("Customization", "FDLienAdd", finalXml);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Final Themebridge responseXML==========>" + responseXML);

                              }

                              EnigmaArray<ExtEventLienMarkEntityWrapper> leanDataList = getExtEventLienMarkData();
                              String line = responseXML;
                              String[] sp_line = line.split(",");
                              for (int k = 0; k < sp_line.length; k++) {
                                    String[] res_sp_line = sp_line[k].split("~");
                                    Iterator<ExtEventLienMarkEntityWrapper> iterator1 = leanDataList.iterator();

                                    for (ExtEventLienMarkEntityWrapper str : leanDataList) {
                                          String depositNum = str.getDEPOSNO().trim();

                                          String currentDepositNum = res_sp_line[0].trim();
                                          // //Loggers.general().info(LOG,"res_sp_line[0]==>"+res_sp_line[0]);
                                          // String lienStr = str.getMARGAMT();
                                          // double lienAmt = Double.valueOf(lienStr);
                                          String lienbox = str.getLINESTS();
                                          // //Loggers.general().info(LOG,"currentDepositNum -
                                          // "+currentDepositNum);
                                          // //Loggers.general().info(LOG,"depositNum -
                                          // "+depositNum);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Lien grid depositNum" + depositNum);
                                                Loggers.general().info(LOG,"Lien response currentDepositNum" + currentDepositNum);

                                          }
                                          if (depositNum.matches("[0-9]+") && depositNum.length() > 1) {
                                                if (depositNum.equalsIgnoreCase(currentDepositNum)) {
                                                      if (res_sp_line[2].equalsIgnoreCase("MARK SUCCEEDED")
                                                                  && (lienbox.equalsIgnoreCase("") || lienbox == null)) {
                                                            // Loggers.general().info(LOG,"lien
                                                            // SUCCEEDED
                                                            // status" + res_sp_line[2]);
                                                            str.setLINESTS(res_sp_line[2]);
                                                            str.setLIENIDN(res_sp_line[1]);
                                                            str.setLIENREMK(res_sp_line[3]);
                                                      //    str.setLIENDATE(result);
                                                            getExtEventLienMarkUpdate().setEnabled(false);
                                                            getExtEventLienMarkDelete().setEnabled(false);
                                                      } else if ((lienbox.equalsIgnoreCase("") || lienbox == null)) {
                                                            // Loggers.general().info(LOG,"lien FAILED
                                                            // status" + res_sp_line[2]);
                                                            str.setLINESTS(res_sp_line[2]);
                                                            str.setLIENIDN(res_sp_line[1]);
                                                            str.setLIENREMK(res_sp_line[3]);
                                                      //    str.setLIENDATE(result);
                                                            getExtEventLienMarkUpdate().setEnabled(true);
                                                            getExtEventLienMarkDelete().setEnabled(false);
                                                      }

                                                      else {

                                                            // Loggers.general().info(LOG,"lien marked
                                                            // depositNum is not equal to
                                                            // currentDepositNum");
                                                      }
                                                } else {
                                                      // Loggers.general().info(LOG,"lien marked
                                                      // status is
                                                      // not blank");
                                                }
                                          } else {
                                                str.setLIENREMK(AcnoRem);
                                                // Loggers.general().info(LOG,"Special charactor");
                                          }
                                    }

                              }

                              // }
                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"ThemeTransportClient Exceptions!
                              // " +
                              // e.getMessage());
                        }

                  }

                  else {
                        // Loggers.general().info(LOG,"Lien mark amount is greater than the
                        // margin amount");

                  }
            } else {
                  // Loggers.general().info(LOG,"Facility id is blank");

                  try {

                        String headerReq = "<?xml version=$1.0$ encoding=$UTF-8$?><ServiceRequest xmlns=$urn:control.services.tiplus2.misys.com$ xmlns:ns2=$urn:messages.service.ti.apps.tiplus2.misys.com$ xmlns:ns3=$urn:common.service.ti.apps.tiplus2.misys.com$ xmlns:ns4=$urn:custom.service.ti.apps.tiplus2.misys.com$><RequestHeader><Service>Customization</Service><Operation>FDLienAdd</Operation><Credentials><Name>SUPERVISOR</Name></Credentials><ReplyFormat>FULL</ReplyFormat><TargetSystem>KOTAKEXT</TargetSystem><SourceSystem>ZONE1</SourceSystem><NoRepair>Y</NoRepair><NoOverride>Y</NoOverride><CorrelationId>Correlation_Id</CorrelationId><TransactionControl>NONE</TransactionControl></RequestHeader><fdlienadd xmlns:xsi=$http://www.w3.org/2001/XMLSchema-instance$>";
                        String endXml = "</fdlienadd></ServiceRequest>";
                        String gridXml = "";
                        String startDate = "";
                        String endDate = "";
                        EnigmaArray<ExtEventLienMarkEntityWrapper> list = getExtEventLienMarkData();
                        for (ExtEventLienMarkEntityWrapper fdwrapper : list) {
                              String marginAmt = fdwrapper.getMARGAM();
                              String marginAcc = fdwrapper.getDEPOSNO().trim();
                              String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                              String eventRefNo = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
                              String issueDate = getDriverWrapper().getEventFieldAsText("ISS", "d", "");
                              String mas_evnt = masRefNo;
                              String expiryDate = getDriverWrapper().getEventFieldAsText("EXP", "d", "");
                              DateFormat userDateFormat = new SimpleDateFormat("dd/mm/yy");
                              DateFormat dateFormatNeeded = new SimpleDateFormat("yyyy-mm-dd");
                              Date date;
                              Date date1;
                              try {
                                    date = (Date) userDateFormat.parse(issueDate);
                                    startDate = dateFormatNeeded.format(date);
                                    date1 = (Date) userDateFormat.parse(expiryDate);
                                    endDate = dateFormatNeeded.format(date1);

                                    SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yy");

                                    Date sysdate = (Date) formatter1.parse(systemDate);
                                    // Loggers.general().info(LOG,"systemDate Date lien----- " +
                                    // systemDate);
                                    result = formatter1.format(sysdate);
                                    // Loggers.general().info(LOG,"result Date ----- " + result);

                              } catch (ParseException e) {
                                    // Loggers.general().info(LOG,"Date Exception ");
                              }
                              String luCur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                              String mas_evntRef = masRefNo;
                              String lienbox = fdwrapper.getLINESTS();
                              if (lienbox.equalsIgnoreCase("") || lienbox == null) {
                                    // Loggers.general().info(LOG,"Lien Status is blank");
                                    String fdDefaultXml = "<FDRow><MasterReference>Maseter_ref</MasterReference><EventReference>Event_ref</EventReference><BehalfOfBranch>behalf_Branch</BehalfOfBranch><AccountNumber>Account_Number</AccountNumber><Amount>Amount_Ccy</Amount><Currency>LC_Cur</Currency><ReasonCode>TRD</ReasonCode><Remarks>mas_evnt</Remarks><LienStartDate>Issue_Date</LienStartDate><LienEndDate>Expiry_Date</LienEndDate></FDRow>";
                                    fdDefaultXml = fdDefaultXml.replace("Maseter_ref", masRefNo);
                                    fdDefaultXml = fdDefaultXml.replace("Event_ref", eventRefNo);
                                    fdDefaultXml = fdDefaultXml.replace("behalf_Branch", behalfBranch);
                                    fdDefaultXml = fdDefaultXml.replace("Account_Number", marginAcc);
                                    fdDefaultXml = fdDefaultXml.replace("Amount_Ccy", marginAmt);
                                    fdDefaultXml = fdDefaultXml.replace("LC_Cur", luCur);
                                    fdDefaultXml = fdDefaultXml.replace("mas_evnt", mas_evntRef);
                                    fdDefaultXml = fdDefaultXml.replace("Issue_Date", startDate);
                                    fdDefaultXml = fdDefaultXml.replace("Expiry_Date", endDate);
                                    gridXml = gridXml + fdDefaultXml;
                              } else {
                                    // Loggers.general().info(LOG,"Lien Status is not blank");
                              }

                        }

                        char j = '"';
                        String val = Character.toString(j);
                        headerReq = headerReq.replace("$", val);
                        String correlationId = randomCorrelationId();
                        // Loggers.general().info(LOG,"correlationId " + correlationId);
                        headerReq = headerReq.replace("Correlation_Id", correlationId);
                        String finalXml = headerReq + gridXml + endXml;
                        // Loggers.general().info(LOG,"Themebridge finalXml " + finalXml);

                        ThemeTransportClient anThemeTransportClient = new ThemeTransportClient();
                        String responseXML = anThemeTransportClient.invoke("Customization", "FDLienAdd", finalXml);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Final Themebridge responseXML==========>" + responseXML);

                        }

                        EnigmaArray<ExtEventLienMarkEntityWrapper> leanDataList = getExtEventLienMarkData();
                        String line = responseXML;
                        String[] sp_line = line.split(",");
                        for (int k = 0; k < sp_line.length; k++) {
                              String[] res_sp_line = sp_line[k].split("~");
                              Iterator<ExtEventLienMarkEntityWrapper> iterator1 = leanDataList.iterator();

                              for (ExtEventLienMarkEntityWrapper str : leanDataList) {
                                    String depositNum = str.getDEPOSNO().trim();

                                    String currentDepositNum = res_sp_line[0];
                                    // //Loggers.general().info(LOG,"res_sp_line[0]==>"+res_sp_line[0]);

                                    String lienbox = str.getLINESTS();
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Lien grid depositNum" + depositNum);
                                          Loggers.general().info(LOG,"Lien response currentDepositNum" + currentDepositNum);

                                    }
                                    if (depositNum.matches("[0-9]+") && depositNum.length() > 1) {
                                          if (depositNum.equalsIgnoreCase(currentDepositNum)) {
                                                if (res_sp_line[2].equalsIgnoreCase("MARK SUCCEEDED")
                                                            && (lienbox.equalsIgnoreCase("") || lienbox == null)) {
                                                      // Loggers.general().info(LOG,"lien SUCCEEDED
                                                      // status" + res_sp_line[2]);
                                                      str.setLINESTS(res_sp_line[2]);
                                                      str.setLIENIDN(res_sp_line[1]);
                                                      str.setLIENREMK(res_sp_line[3]);
                                                //    str.setLIENDATE(result);
                                                      getExtEventLienMarkingUpdate().setEnabled(false);
                                                      getExtEventLienMarkingDelete().setEnabled(false);
                                                } else if ((lienbox.equalsIgnoreCase("") || lienbox == null)) {
                                                      // Loggers.general().info(LOG,"lien FAILED
                                                      // status" +
                                                      // res_sp_line[2]);
                                                      str.setLINESTS(res_sp_line[2]);
                                                      str.setLIENIDN(res_sp_line[1]);
                                                      str.setLIENREMK(res_sp_line[3]);
                                                      //str.setLIENDATE(result);
                                                      getExtEventLienMarkingUpdate().setEnabled(true);
                                                      getExtEventLienMarkingDelete().setEnabled(false);
                                                }

                                                else {

                                                      // Loggers.general().info(LOG,"lien marked
                                                      // depositNum is not equal to
                                                      // currentDepositNum");
                                                }
                                          } else {
                                                // Loggers.general().info(LOG,"lien marked status is
                                                // not
                                                // blank");
                                          }
                                    } else {
                                          str.setLIENREMK(AcnoRem);
                                          // Loggers.general().info(LOG,"Special charactor");
                                    }
                              }

                        }

                        // }
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"ThemeTransportClient Exceptions! " +
                        // e.getMessage());
                  }

            }

      }

      public void onREVERSELIENSHPGUACREclayButton() {
            lienReverse();
      }

      public boolean lienMark() {
            boolean value = false;

            String Margamt = "0";
            String gridamtcur = "";
            String result = "";
            String systemDate = getDriverWrapper().getEventFieldAsText("TDY", "d", "");
            String AcnoRem = "Check Space or Spl charactor should not be there";
            String behalfBranch = getDriverWrapper().getEventFieldAsText("BOB", "s", "").trim();
            getExtEventLienMarkingDelete().setEnabled(false);
            Margamt = getMARAMT().toString();
            String facility = getFACLTYID();
            int count_R = 0;
            int count_M = 0;
            if (Margamt.equalsIgnoreCase("") || Margamt == null) {
                  Margamt = "0";

            }
            BigDecimal margindec = new BigDecimal("0");
            BigDecimal margindecadd = new BigDecimal("0");
            BigDecimal marginbig = new BigDecimal("0");
            BigDecimal one = new BigDecimal(1);
            BigDecimal thousand = new BigDecimal(1000);
            BigDecimal hundred = new BigDecimal(100);
            String add_lie = Margamt.replaceAll("[^0-9]", "");
            // Loggers.general().info(LOG," MarginAmt in number----> " + add_lie);
            // Margamtdob = Double.valueOf(add_lie);
            BigDecimal MargamtNum = new BigDecimal(add_lie);
            BigDecimal Margamtdob = MargamtNum.divide(hundred);

            // Loggers.general().info(LOG,"Grid BigDecimal amount--->" + Margamtdob);

            getExtEventLienMarkingDelete().setEnabled(false);
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

            if (dailyval_Log.equalsIgnoreCase("YES")) {
                  Loggers.general().info(LOG,"Grid BigDecimal amount--->" + Margamtdob);

            }

            EnigmaArray<ExtEventLienMarkingEntityWrapper> liste2 = getExtEventLienMarkingData();

            if (liste2.getSize() > 0) {
                  Iterator<ExtEventLienMarkingEntityWrapper> iterator1 = liste2.iterator();

                  while (iterator1.hasNext()) {
                        ExtEventLienMarkingEntityWrapper ExtEventLienMark = (ExtEventLienMarkingEntityWrapper) iterator1.next();

                        String marginAmt = ExtEventLienMark.getMARGAMTAmount().toString();
                        // Loggers.general().info(LOG,"Amount marginAmt String-----> " +
                        // marginAmt);

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Amount marginAmt String-----> " + marginAmt);

                        }
                        marginbig = new BigDecimal(marginAmt);

                        gridamtcur = ExtEventLienMark.getMARGAMTCurrency().toString();
                        // Loggers.general().info(LOG,"Amount marginAmt-----> " + marginAmt);

                        // ConnectionMaster connectionMaster = new ConnectionMaster();
                        // double divideByDecimal =
                        // connectionMaster.getDecimalforCurrency(gridamtcur);
                        // gridadded = valamtdob / divideByDecimal;
                        // Loggers.general().info(LOG,"Grid divided amount--->" + gridadded);

                        // gridamount = gridamount + gridadded;
                        //
                        // gridamount += gridadded;
                        // Loggers.general().info(LOG,"Amount marginAmt double in while-----> "
                        // + gridamount);
                        if (gridamtcur.equalsIgnoreCase("BHD") || gridamtcur.equalsIgnoreCase("KWD")
                                    || gridamtcur.equalsIgnoreCase("OMR")) {
                              margindec = marginbig.divide(thousand);
                        } else if (gridamtcur.equalsIgnoreCase("JPY") || gridamtcur.equalsIgnoreCase("KRW")) {

                              margindec = marginbig.divide(one);
                        } else {
                              margindec = marginbig.divide(hundred);
                        }
                        // Loggers.general().info(LOG,"Amount marginAmt bigdecimal in
                        // while-----> " + margindec);

                        margindecadd = margindecadd.add(margindec);

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Margin add amount-----> " + margindecadd);

                        }

                        String deptno = ExtEventLienMark.getDEPOSTNO().trim();
                        String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
                        String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim();

                        // try {
                        // if (!deptno.equalsIgnoreCase("")) {
                        // String qr = "SELECT COUNT(*),LIEN,ACCOUNTID,MASTERREFERENCE
                        // FROM THEMEBRIDGE.LIENDETAILS WHERE ACCOUNTID ='"
                        // + deptno + "' AND MASTERREFERENCE ='" + masRefNo + "' AND
                        // EVENTREFERENCE ='" + eventCode
                        // + "' GROUP BY LIEN,ACCOUNTID,MASTERREFERENCE";
                        // Loggers.general().info(LOG,"Query for LIENDETAILS====>" + qr);
                        // con = ConnectionMaster.getThemeBridgeConnection();
                        // ps = con.prepareStatement(qr);
                        // // //Loggers.general().info(LOG,qr);
                        // rs = ps.executeQuery();
                        // while (rs.next()) {
                        // if (rs.getString(2).equalsIgnoreCase("R")) {
                        // count_R++;
                        // Loggers.general().info(LOG,"LIENDETAILS count_Reversal====>" +
                        // count_R);
                        // } else if (rs.getString(2).equalsIgnoreCase("M")) {
                        // count_M++;
                        // Loggers.general().info(LOG,"LIENDETAILS count_Mark====>" + count_M);
                        // }
                        //
                        // }
                        // }
                        //
                        // } catch (Exception e) {
                        // //Loggers.general().info(LOG,"exception caught " +e.getMessage()
                        // );
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
                  }

            }
            // Loggers.general().info(LOG,"count_Reversal====>" + count_R +
            // "count_Mark=====>" + count_M);

            // if (count_R >= count_M) {
            // Loggers.general().info(LOG,"If loop ==== > Account no is added new");
            if (facility.length() > 0) {
                  if (Margamtdob.compareTo(margindecadd) >= 0) {

                        try {

                              String headerReq = "<?xml version=$1.0$ encoding=$UTF-8$?><ServiceRequest xmlns=$urn:control.services.tiplus2.misys.com$ xmlns:ns2=$urn:messages.service.ti.apps.tiplus2.misys.com$ xmlns:ns3=$urn:common.service.ti.apps.tiplus2.misys.com$ xmlns:ns4=$urn:custom.service.ti.apps.tiplus2.misys.com$><RequestHeader><Service>Customization</Service><Operation>FDLienAdd</Operation><Credentials><Name>SUPERVISOR</Name></Credentials><ReplyFormat>FULL</ReplyFormat><TargetSystem>KOTAKEXT</TargetSystem><SourceSystem>ZONE1</SourceSystem><NoRepair>Y</NoRepair><NoOverride>Y</NoOverride><CorrelationId>Correlation_Id</CorrelationId><TransactionControl>NONE</TransactionControl></RequestHeader><fdlienadd xmlns:xsi=$http://www.w3.org/2001/XMLSchema-instance$>";
                              String endXml = "</fdlienadd></ServiceRequest>";
                              String gridXml = "";
                              String startDate = "";
                              String endDate = "";
                              EnigmaArray<ExtEventLienMarkingEntityWrapper> list = getExtEventLienMarkingData();
                              for (ExtEventLienMarkingEntityWrapper fdwrapper : list) {
                                    String marginAmt = fdwrapper.getMARGAMT();
                                    String marginAcc = fdwrapper.getDEPOSTNO().trim();
                                    String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                                    String eventRefNo = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
                                    String issueDate = getDriverWrapper().getEventFieldAsText("ISS", "d", "");
                                    String mas_evnt = masRefNo;
                                    String expiryDate = getDriverWrapper().getEventFieldAsText("EXP", "d", "");
                                    DateFormat userDateFormat = new SimpleDateFormat("dd/mm/yy");
                                    DateFormat dateFormatNeeded = new SimpleDateFormat("yyyy-mm-dd");

                                    Date date;
                                    Date date1;

                                    try {
                                          date = (Date) userDateFormat.parse(issueDate);
                                          startDate = dateFormatNeeded.format(date);
                                          date1 = (Date) userDateFormat.parse(expiryDate);
                                          endDate = dateFormatNeeded.format(date1);

                                          SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yy");

                                          Date sysdate = (Date) formatter1.parse(systemDate);
                                          // Loggers.general().info(LOG,"systemDate Date lien----- " +
                                          // systemDate);
                                          result = formatter1.format(sysdate);
                                          // Loggers.general().info(LOG,"result Date ----- " +
                                          // result);

                                    } catch (ParseException e) {
                                          // Loggers.general().info(LOG,"Date Exception ");
                                    }
                                    String luCur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                                    String mas_evntRef = masRefNo;
                                    String lienbox = fdwrapper.getLINEST();
                                    if (lienbox.equalsIgnoreCase("") || lienbox == null) {
                                          // Loggers.general().info(LOG,"Lien Status is blank");
                                          String fdDefaultXml = "<FDRow><MasterReference>Maseter_ref</MasterReference><EventReference>Event_ref</EventReference><BehalfOfBranch>behalf_Branch</BehalfOfBranch><AccountNumber>Account_Number</AccountNumber><Amount>Amount_Ccy</Amount><Currency>LC_Cur</Currency><ReasonCode>TRD</ReasonCode><Remarks>mas_evnt</Remarks><LienStartDate>Issue_Date</LienStartDate><LienEndDate>Expiry_Date</LienEndDate></FDRow>";
                                          fdDefaultXml = fdDefaultXml.replace("Maseter_ref", masRefNo);
                                          fdDefaultXml = fdDefaultXml.replace("Event_ref", eventRefNo);
                                          fdDefaultXml = fdDefaultXml.replace("behalf_Branch", behalfBranch);
                                          fdDefaultXml = fdDefaultXml.replace("Account_Number", marginAcc);
                                          fdDefaultXml = fdDefaultXml.replace("Amount_Ccy", marginAmt);
                                          fdDefaultXml = fdDefaultXml.replace("LC_Cur", luCur);
                                          fdDefaultXml = fdDefaultXml.replace("mas_evnt", mas_evntRef);
                                          fdDefaultXml = fdDefaultXml.replace("Issue_Date", startDate);
                                          fdDefaultXml = fdDefaultXml.replace("Expiry_Date", endDate);
                                          gridXml = gridXml + fdDefaultXml;
                                    } else {
                                          // Loggers.general().info(LOG,"Lien Status is not
                                          // blank");
                                    }

                              }

                              char j = '"';
                              String val = Character.toString(j);
                              headerReq = headerReq.replace("$", val);
                              String correlationId = randomCorrelationId();
                              // Loggers.general().info(LOG,"correlationId " + correlationId);
                              headerReq = headerReq.replace("Correlation_Id", correlationId);
                              String finalXml = headerReq + gridXml + endXml;
                              // Loggers.general().info(LOG,"Themebridge finalXml " +
                              // finalXml);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Themebridge finalXml " + finalXml);

                              }
                              ThemeTransportClient anThemeTransportClient = new ThemeTransportClient();
                              String responseXML = anThemeTransportClient.invoke("Customization", "FDLienAdd", finalXml);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Final Themebridge responseXML==========>" + responseXML);

                              }

                              EnigmaArray<ExtEventLienMarkingEntityWrapper> leanDataList = getExtEventLienMarkingData();
                              String line = responseXML;
                              String[] sp_line = line.split(",");
                              for (int k = 0; k < sp_line.length; k++) {
                                    String[] res_sp_line = sp_line[k].split("~");
                                    Iterator<ExtEventLienMarkingEntityWrapper> iterator1 = leanDataList.iterator();

                                    for (ExtEventLienMarkingEntityWrapper str : leanDataList) {
                                          String depositNum = str.getDEPOSTNO().trim();

                                          String currentDepositNum = res_sp_line[0].trim();
                                          // //Loggers.general().info(LOG,"res_sp_line[0]==>"+res_sp_line[0]);
                                          // String lienStr = str.getMARGAMT();
                                          // double lienAmt = Double.valueOf(lienStr);
                                          String lienbox = str.getLINEST();
                                          // //Loggers.general().info(LOG,"currentDepositNum -
                                          // "+currentDepositNum);
                                          // //Loggers.general().info(LOG,"depositNum -
                                          // "+depositNum);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Lien grid depositNum" + depositNum);
                                                Loggers.general().info(LOG,"Lien response currentDepositNum" + currentDepositNum);

                                          }
                                          if (depositNum.matches("[0-9]+") && depositNum.length() > 1) {
                                                if (depositNum.equalsIgnoreCase(currentDepositNum)) {
                                                      if (res_sp_line[2].equalsIgnoreCase("MARK SUCCEEDED")
                                                                  && (lienbox.equalsIgnoreCase("") || lienbox == null)) {
                                                            // Loggers.general().info(LOG,"lien
                                                            // SUCCEEDED
                                                            // status" + res_sp_line[2]);
                                                            str.setLINEST(res_sp_line[2]);
                                                            str.setLIENID(res_sp_line[1]);
                                                            str.setLIENREM(res_sp_line[3]);
                                                      //    str.setLIENDAT(result);
                                                            getExtEventLienMarkingUpdate().setEnabled(false);
                                                            getExtEventLienMarkingDelete().setEnabled(false);
                                                      } else if ((lienbox.equalsIgnoreCase("") || lienbox == null)) {
                                                            // Loggers.general().info(LOG,"lien FAILED
                                                            // status" + res_sp_line[2]);
                                                            str.setLINEST(res_sp_line[2]);
                                                            str.setLIENID(res_sp_line[1]);
                                                            str.setLIENREM(res_sp_line[3]);
                                                      //    str.setLIENDAT(result);
                                                            getExtEventLienMarkingUpdate().setEnabled(true);
                                                            getExtEventLienMarkingDelete().setEnabled(false);
                                                      }

                                                      else {

                                                            // Loggers.general().info(LOG,"lien marked
                                                            // depositNum is not equal to
                                                            // currentDepositNum");
                                                      }
                                                } else {
                                                      // Loggers.general().info(LOG,"lien marked
                                                      // status is
                                                      // not blank");
                                                }
                                          } else {
                                                str.setLIENREM(AcnoRem);
                                                // Loggers.general().info(LOG,"Special charactor");
                                          }
                                    }

                              }

                              // }
                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"ThemeTransportClient Exceptions!
                              // " +
                              // e.getMessage());
                        }

                  }

                  else {
                        // Loggers.general().info(LOG,"Lien mark amount is greater than the
                        // margin amount");

                  }
            } else {
                  // Loggers.general().info(LOG,"Facility id is blank");

                  try {

                        String headerReq = "<?xml version=$1.0$ encoding=$UTF-8$?><ServiceRequest xmlns=$urn:control.services.tiplus2.misys.com$ xmlns:ns2=$urn:messages.service.ti.apps.tiplus2.misys.com$ xmlns:ns3=$urn:common.service.ti.apps.tiplus2.misys.com$ xmlns:ns4=$urn:custom.service.ti.apps.tiplus2.misys.com$><RequestHeader><Service>Customization</Service><Operation>FDLienAdd</Operation><Credentials><Name>SUPERVISOR</Name></Credentials><ReplyFormat>FULL</ReplyFormat><TargetSystem>KOTAKEXT</TargetSystem><SourceSystem>ZONE1</SourceSystem><NoRepair>Y</NoRepair><NoOverride>Y</NoOverride><CorrelationId>Correlation_Id</CorrelationId><TransactionControl>NONE</TransactionControl></RequestHeader><fdlienadd xmlns:xsi=$http://www.w3.org/2001/XMLSchema-instance$>";
                        String endXml = "</fdlienadd></ServiceRequest>";
                        String gridXml = "";
                        String startDate = "";
                        String endDate = "";
                        EnigmaArray<ExtEventLienMarkingEntityWrapper> list = getExtEventLienMarkingData();
                        for (ExtEventLienMarkingEntityWrapper fdwrapper : list) {
                              String marginAmt = fdwrapper.getMARGAMT();
                              String marginAcc = fdwrapper.getDEPOSTNO().trim();
                              String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                              String eventRefNo = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
                              String issueDate = getDriverWrapper().getEventFieldAsText("ISS", "d", "");
                              String mas_evnt = masRefNo;
                              String expiryDate = getDriverWrapper().getEventFieldAsText("EXP", "d", "");
                              DateFormat userDateFormat = new SimpleDateFormat("dd/mm/yy");
                              DateFormat dateFormatNeeded = new SimpleDateFormat("yyyy-mm-dd");
                              Date date;
                              Date date1;
                              try {
                                    date = (Date) userDateFormat.parse(issueDate);
                                    startDate = dateFormatNeeded.format(date);
                                    date1 = (Date) userDateFormat.parse(expiryDate);
                                    endDate = dateFormatNeeded.format(date1);

                                    SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yy");

                                    Date sysdate = (Date) formatter1.parse(systemDate);
                                    // Loggers.general().info(LOG,"systemDate Date lien----- " +
                                    // systemDate);
                                    result = formatter1.format(sysdate);
                                    // Loggers.general().info(LOG,"result Date ----- " + result);

                              } catch (ParseException e) {
                                    // Loggers.general().info(LOG,"Date Exception ");
                              }
                              String luCur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                              String mas_evntRef = masRefNo;
                              String lienbox = fdwrapper.getLINEST();
                              if (lienbox.equalsIgnoreCase("") || lienbox == null) {
                                    // Loggers.general().info(LOG,"Lien Status is blank");
                                    String fdDefaultXml = "<FDRow><MasterReference>Maseter_ref</MasterReference><EventReference>Event_ref</EventReference><BehalfOfBranch>behalf_Branch</BehalfOfBranch><AccountNumber>Account_Number</AccountNumber><Amount>Amount_Ccy</Amount><Currency>LC_Cur</Currency><ReasonCode>TRD</ReasonCode><Remarks>mas_evnt</Remarks><LienStartDate>Issue_Date</LienStartDate><LienEndDate>Expiry_Date</LienEndDate></FDRow>";
                                    fdDefaultXml = fdDefaultXml.replace("Maseter_ref", masRefNo);
                                    fdDefaultXml = fdDefaultXml.replace("Event_ref", eventRefNo);
                                    fdDefaultXml = fdDefaultXml.replace("behalf_Branch", behalfBranch);
                                    fdDefaultXml = fdDefaultXml.replace("Account_Number", marginAcc);
                                    fdDefaultXml = fdDefaultXml.replace("Amount_Ccy", marginAmt);
                                    fdDefaultXml = fdDefaultXml.replace("LC_Cur", luCur);
                                    fdDefaultXml = fdDefaultXml.replace("mas_evnt", mas_evntRef);
                                    fdDefaultXml = fdDefaultXml.replace("Issue_Date", startDate);
                                    fdDefaultXml = fdDefaultXml.replace("Expiry_Date", endDate);
                                    gridXml = gridXml + fdDefaultXml;
                              } else {
                                    // Loggers.general().info(LOG,"Lien Status is not blank");
                              }

                        }

                        char j = '"';
                        String val = Character.toString(j);
                        headerReq = headerReq.replace("$", val);
                        String correlationId = randomCorrelationId();
                        // Loggers.general().info(LOG,"correlationId " + correlationId);
                        headerReq = headerReq.replace("Correlation_Id", correlationId);
                        String finalXml = headerReq + gridXml + endXml;
                        // Loggers.general().info(LOG,"Themebridge finalXml " + finalXml);

                        ThemeTransportClient anThemeTransportClient = new ThemeTransportClient();
                        String responseXML = anThemeTransportClient.invoke("Customization", "FDLienAdd", finalXml);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Final Themebridge responseXML==========>" + responseXML);

                        }

                        EnigmaArray<ExtEventLienMarkingEntityWrapper> leanDataList = getExtEventLienMarkingData();
                        String line = responseXML;
                        String[] sp_line = line.split(",");
                        for (int k = 0; k < sp_line.length; k++) {
                              String[] res_sp_line = sp_line[k].split("~");
                              Iterator<ExtEventLienMarkingEntityWrapper> iterator1 = leanDataList.iterator();

                              for (ExtEventLienMarkingEntityWrapper str : leanDataList) {
                                    String depositNum = str.getDEPOSTNO().trim();

                                    String currentDepositNum = res_sp_line[0];
                                    // //Loggers.general().info(LOG,"res_sp_line[0]==>"+res_sp_line[0]);

                                    String lienbox = str.getLINEST();
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Lien grid depositNum" + depositNum);
                                          Loggers.general().info(LOG,"Lien response currentDepositNum" + currentDepositNum);

                                    }
                                    if (depositNum.matches("[0-9]+") && depositNum.length() > 1) {
                                          if (depositNum.equalsIgnoreCase(currentDepositNum)) {
                                                if (res_sp_line[2].equalsIgnoreCase("MARK SUCCEEDED")
                                                            && (lienbox.equalsIgnoreCase("") || lienbox == null)) {
                                                      // Loggers.general().info(LOG,"lien SUCCEEDED
                                                      // status" + res_sp_line[2]);
                                                      str.setLINEST(res_sp_line[2]);
                                                      str.setLIENID(res_sp_line[1]);
                                                      str.setLIENREM(res_sp_line[3]);
                                                //    str.setLIENDAT(result);
                                                      getExtEventLienMarkingUpdate().setEnabled(false);
                                                      getExtEventLienMarkingDelete().setEnabled(false);
                                                } else if ((lienbox.equalsIgnoreCase("") || lienbox == null)) {
                                                      // Loggers.general().info(LOG,"lien FAILED
                                                      // status" +
                                                      // res_sp_line[2]);
                                                      str.setLINEST(res_sp_line[2]);
                                                      str.setLIENID(res_sp_line[1]);
                                                      str.setLIENREM(res_sp_line[3]);
                                                //    str.setLIENDAT(result);
                                                      getExtEventLienMarkingUpdate().setEnabled(true);
                                                      getExtEventLienMarkingDelete().setEnabled(false);
                                                }

                                                else {

                                                      // Loggers.general().info(LOG,"lien marked
                                                      // depositNum is not equal to
                                                      // currentDepositNum");
                                                }
                                          } else {
                                                // Loggers.general().info(LOG,"lien marked status is
                                                // not
                                                // blank");
                                          }
                                    } else {
                                          str.setLIENREM(AcnoRem);
                                          // Loggers.general().info(LOG,"Special charactor");
                                    }
                              }

                        }

                        // }
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"ThemeTransportClient Exceptions! " +
                        // e.getMessage());
                  }

            }

            return value;
      }

      public boolean lienReverse() {

            boolean value = false;

            String releaseacc = getLIENBAL().trim();
            String behalfBranch = getDriverWrapper().getEventFieldAsText("BOB", "s", "");
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
            // Loggers.general().info(LOG,"Lien reversal text box=====>" + releaseacc);
            if (releaseacc.equalsIgnoreCase("") || releaseacc == null) {

                  try {
                        // Loggers.general().info(LOG,"Reverse service method calling started
                        // IGT Adjust in if loop");
                        String headerReq = "<?xml version=$1.0$ encoding=$UTF-8$?><ServiceRequest xmlns=$urn:control.services.tiplus2.misys.com$ xmlns:ns2=$urn:messages.service.ti.apps.tiplus2.misys.com$ xmlns:ns3=$urn:common.service.ti.apps.tiplus2.misys.com$ xmlns:ns4=$urn:custom.service.ti.apps.tiplus2.misys.com$><RequestHeader><Service>Customization</Service><Operation>FDLienRemoval</Operation><Credentials><Name>SUPERVISOR</Name></Credentials><ReplyFormat>FULL</ReplyFormat><TargetSystem>KOTAKEXT</TargetSystem><SourceSystem>ZONE1</SourceSystem><NoRepair>Y</NoRepair><NoOverride>Y</NoOverride><CorrelationId>Correlation_Id</CorrelationId><TransactionControl>NONE</TransactionControl></RequestHeader><fdlienremoval xmlns:xsi=$http://www.w3.org/2001/XMLSchema-instance$>";
                        String endXml = "</fdlienremoval></ServiceRequest>";
                        String gridXml = "";
                        String lienStatus = "";
                        String lienbox = "";
                        EnigmaArray<ExtEventLienMarkingEntityWrapper> list = getExtEventLienMarkingData();
                        for (ExtEventLienMarkingEntityWrapper fdwrapper : list) {
                              // //Loggers.general().info(LOG,"11112212121");
                              String marginAmt = fdwrapper.getMARGAMT();
                              String marginAcc = fdwrapper.getDEPOSTNO().trim();
                              String luCur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                              String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                              String eventRefNo = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                              String lien_id = fdwrapper.getLIENID();
                              lienStatus = fdwrapper.getLINEST().trim();
                              lienbox = lienbox + lienStatus;
                              // Loggers.general().info(LOG,"Lien Status " + lienbox);
                              if ((!lienbox.equalsIgnoreCase("") || lienbox != null)
                                          && lienStatus.equalsIgnoreCase("MARK SUCCEEDED")) {

                                    String fdDefaultXml = "<FDRow><MasterReference>Maseter_ref</MasterReference><EventReference>Event_ref</EventReference><BehalfOfBranch>behalf_Branch</BehalfOfBranch><AccountNumber>Account_Number</AccountNumber><LienId>lienRev_id</LienId><Amount>Amount_Ccy</Amount><Currency>Lc_Ccy</Currency><Remarks>masRefNo</Remarks></FDRow>";
                                    // String endXml = "</fdlien></ServiceRequest>";
                                    fdDefaultXml = fdDefaultXml.replace("Maseter_ref", masRefNo);
                                    fdDefaultXml = fdDefaultXml.replace("Event_ref", eventRefNo);
                                    fdDefaultXml = fdDefaultXml.replace("behalf_Branch", behalfBranch);
                                    fdDefaultXml = fdDefaultXml.replace("Account_Number", marginAcc);
                                    fdDefaultXml = fdDefaultXml.replace("lienRev_id", lien_id);
                                    fdDefaultXml = fdDefaultXml.replace("Amount_Ccy", marginAmt);
                                    fdDefaultXml = fdDefaultXml.replace("Lc_Ccy", luCur);
                                    fdDefaultXml = fdDefaultXml.replace("masRefNo", masRefNo);
                                    gridXml = gridXml + fdDefaultXml;
                              } else {
                                    // Loggers.general().info(LOG,"Lien status is blank");
                              }

                        }
                        // //Loggers.general().info(LOG,"gridXml - "+gridXml);
                        // //Loggers.general().info(LOG,"gridXml final ----> "+gridXml);
                        // Final formation of request
                        char j = '"';
                        String val = Character.toString(j);
                        // Loggers.general().info(LOG,"char value - " + j);
                        headerReq = headerReq.replace("$", val);
                        // Replace correlation id
                        String correlationId = randomCorrelationId();
                        // Loggers.general().info(LOG,"correlationId " + correlationId);
                        headerReq = headerReq.replace("Correlation_Id", correlationId);
                        // //Loggers.general().info(LOG,"headerReq replaced with quotes - " +
                        // headerReq);

                        String finalXml = headerReq + gridXml + endXml;
                        // Loggers.general().info(LOG,"Themebridge finalXml -----> " +
                        // finalXml);

                        // sending to themebridge
                        ThemeTransportClient anThemeTransportClient = new ThemeTransportClient();
                        String resquestXML = anThemeTransportClient.invoke("Customization", "FDLienAdd", finalXml);
                        // Loggers.general().info(LOG,"Themebridge resquestXML" + resquestXML);

                        // String line = "12312~500~SUCCEEDED~Lien test
                        // reversal,222450~600~SUCCEEDED~Lien
                        // reversal,33333~700~FAILED~Lien
                        // failed";
                        String line = resquestXML;
                        // String reverse = "Reversal";
                        String[] sp_line = line.split(",");
                        for (int k = 0; k < sp_line.length; k++) {
                              String[] res_sp_line = sp_line[k].split("~");
                              EnigmaArray<ExtEventLienMarkingEntityWrapper> leanDataList = getExtEventLienMarkingData();
                              for (ExtEventLienMarkingEntityWrapper str : leanDataList) {
                                    String currentDepositNum = res_sp_line[0];
                                    String depositNum = str.getDEPOSTNO().trim();
                                    String lienst = str.getLINEST().trim();
                                    // Loggers.general().info(LOG,"currentDepositNum - " +
                                    // currentDepositNum);
                                    // Loggers.general().info(LOG,"depositNum - " + depositNum);
                                    if (depositNum.equalsIgnoreCase(currentDepositNum)) {
                                          if (lienst.equalsIgnoreCase("MARK SUCCEEDED")
                                                      && (!lienbox.equalsIgnoreCase("") || lienbox != null)) {
                                                // Loggers.general().info(LOG,"update the lien status
                                                // blank");
                                                str.setLINEST(res_sp_line[2]);
                                                // Loggers.general().info(LOG,"Lien id " +
                                                // res_sp_line[1]);
                                                str.setLIENID(res_sp_line[1]);
                                                str.setLIENREM(res_sp_line[3]);
                                                // getBtnReverselienIMPLCCLAIMRECclay().setEnabled(false);
                                          } else if (lienst.equalsIgnoreCase("REVERSAL SUCCEEDED")) {

                                          } else {
                                                // Loggers.general().info(LOG,"update the lien status
                                                // FAILED");
                                                // str.setLIENID(res_sp_line[1]);
                                                // str.setLINEST(res_sp_line[2]);
                                                // str.setLIENREM(res_sp_line[3]);
                                          }
                                    } else {
                                          // Loggers.general().info(LOG,"depositNum is not equal
                                          // currentDepositNum in reversal");
                                    }

                              }
                        }
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"ThemeTransportClient Exceptions! " +
                        // e.getMessage());
                  }

            } else {
                  // Loggers.general().info(LOG,"Reverse service method calling started IGT
                  // Adjust in else====>");

                  try {
                        // Loggers.general().info(LOG,"Reverse service method calling started
                        // IGT Adjust");
                        String headerReq = "<?xml version=$1.0$ encoding=$UTF-8$?><ServiceRequest xmlns=$urn:control.services.tiplus2.misys.com$ xmlns:ns2=$urn:messages.service.ti.apps.tiplus2.misys.com$ xmlns:ns3=$urn:common.service.ti.apps.tiplus2.misys.com$ xmlns:ns4=$urn:custom.service.ti.apps.tiplus2.misys.com$><RequestHeader><Service>Customization</Service><Operation>FDLienRemoval</Operation><Credentials><Name>SUPERVISOR</Name></Credentials><ReplyFormat>FULL</ReplyFormat><TargetSystem>KOTAKEXT</TargetSystem><SourceSystem>ZONE1</SourceSystem><NoRepair>Y</NoRepair><NoOverride>Y</NoOverride><CorrelationId>Correlation_Id</CorrelationId><TransactionControl>NONE</TransactionControl></RequestHeader><fdlienremoval xmlns:xsi=$http://www.w3.org/2001/XMLSchema-instance$>";
                        String endXml = "</fdlienremoval></ServiceRequest>";
                        String gridXml = "";
                        String lienStatus = "";
                        String lienbox = "";
                        EnigmaArray<ExtEventLienMarkingEntityWrapper> list = getExtEventLienMarkingData();
                        for (ExtEventLienMarkingEntityWrapper fdwrapper : list) {
                              // //Loggers.general().info(LOG,"11112212121");
                              String marginAmt = fdwrapper.getMARGAMT();
                              String DepositNum = fdwrapper.getDEPOSTNO().trim();
                              String marginAcc = getLIENBAL().trim();
                              String luCur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                              String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                              String eventRefNo = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                              String lien_id = fdwrapper.getLIENID();
                              lienStatus = fdwrapper.getLINEST();
                              lienbox = lienbox + lienStatus;
                              // Loggers.general().info(LOG,"Lien Status " + lienbox);
                              if ((!lienbox.equalsIgnoreCase("") || lienbox != null)
                                          && (!marginAcc.equalsIgnoreCase("") || marginAcc != null)
                                          && marginAcc.equalsIgnoreCase(DepositNum)
                                          && lienStatus.equalsIgnoreCase("MARK SUCCEEDED")) {
                                    String fdDefaultXml = "<FDRow><MasterReference>Maseter_ref</MasterReference><EventReference>Event_ref</EventReference><BehalfOfBranch>behalf_Branch</BehalfOfBranch><AccountNumber>Account_Number</AccountNumber><LienId>lienRev_id</LienId><Amount>Amount_Ccy</Amount><Currency>Lc_Ccy</Currency><Remarks>masRefNo</Remarks></FDRow>";
                                    // String endXml = "</fdlien></ServiceRequest>";
                                    fdDefaultXml = fdDefaultXml.replace("Maseter_ref", masRefNo);
                                    fdDefaultXml = fdDefaultXml.replace("Event_ref", eventRefNo);
                                    fdDefaultXml = fdDefaultXml.replace("behalf_Branch", behalfBranch);
                                    fdDefaultXml = fdDefaultXml.replace("Account_Number", marginAcc);
                                    fdDefaultXml = fdDefaultXml.replace("lienRev_id", lien_id);
                                    fdDefaultXml = fdDefaultXml.replace("Amount_Ccy", marginAmt);
                                    fdDefaultXml = fdDefaultXml.replace("Lc_Ccy", luCur);
                                    fdDefaultXml = fdDefaultXml.replace("masRefNo", masRefNo);
                                    gridXml = gridXml + fdDefaultXml;
                              } else {
                                    // Loggers.general().info(LOG,"Lien status and lien deposit no
                                    // is blank" + DepositNum + "and====>" + marginAcc);
                              }
                        }
                        // //Loggers.general().info(LOG,"gridXml - "+gridXml);
                        // //Loggers.general().info(LOG,"gridXml final ----> "+gridXml);
                        // Final formation of request
                        char j = '"';
                        String val = Character.toString(j);
                        // Loggers.general().info(LOG,"char value - " + j);
                        headerReq = headerReq.replace("$", val);
                        // Replace correlation id
                        String correlationId = randomCorrelationId();
                        // Loggers.general().info(LOG,"correlationId " + correlationId);
                        headerReq = headerReq.replace("Correlation_Id", correlationId);
                        // //Loggers.general().info(LOG,"headerReq replaced with quotes - " +
                        // headerReq);

                        String finalXml = headerReq + gridXml + endXml;
                        // Loggers.general().info(LOG,"Themebridge finalXml -----> " +
                        // finalXml);

                        // sending to themebridge
                        ThemeTransportClient anThemeTransportClient = new ThemeTransportClient();
                        String resquestXML = anThemeTransportClient.invoke("Customization", "FDLienAdd", finalXml);
                        // Loggers.general().info(LOG,"Themebridge resquestXML" + resquestXML);

                        // String line = "12312~500~SUCCEEDED~Lien test
                        // reversal,222450~600~SUCCEEDED~Lien
                        // reversal,33333~700~FAILED~Lien
                        // failed";
                        String line = resquestXML;
                        // String reverse = "Reversal";
                        String[] sp_line = line.split(",");
                        for (int k = 0; k < sp_line.length; k++) {
                              String[] res_sp_line = sp_line[k].split("~");

                              EnigmaArray<ExtEventLienMarkingEntityWrapper> leanDataList = getExtEventLienMarkingData();
                              for (ExtEventLienMarkingEntityWrapper str : leanDataList) {
                                    // String currentDepositNum = res_sp_line[0].trim();
                                    String currentDepositNum = str.getDEPOSTNO().trim();
                                    String depositNum = getLIENBAL().trim();
                                    String lienst = str.getLINEST().trim();
                                    // Loggers.general().info(LOG,"currentDepositNum - " +
                                    // currentDepositNum);
                                    // Loggers.general().info(LOG,"depositNum - " + depositNum);
                                    if (depositNum.equalsIgnoreCase(currentDepositNum)) {
                                          if (lienst.equalsIgnoreCase("MARK SUCCEEDED")
                                                      && (!lienbox.equalsIgnoreCase("") || lienbox != null)) {
                                                // Loggers.general().info(LOG,"update the lien status
                                                // blank");
                                                str.setLINEST(res_sp_line[2]);
                                                // Loggers.general().info(LOG,"Lien id " +
                                                // res_sp_line[1]);
                                                str.setLIENID(res_sp_line[1]);
                                                str.setLIENREM(res_sp_line[3]);
                                                // getBtnReverselienIMPLCCLAIMRECclay().setEnabled(false);
                                          } else if (lienst.equalsIgnoreCase("REVERSAL SUCCEEDED")) {
                                                // Loggers.general().info(LOG,"REVERSAL update=====>");

                                          } else {
                                                // Loggers.general().info(LOG,"update the lien status
                                                // FAILED");
                                                // str.setLIENID(res_sp_line[1]);
                                                // str.setLINEST(res_sp_line[2]);
                                                // str.setLIENREM(res_sp_line[3]);
                                          }
                                    } else {
                                          // Loggers.general().info(LOG,"depositNum is not equal
                                          // currentDepositNum in reversal");
                                    }

                              }

                        }
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"ThemeTransportClient Exceptions! " +
                        // e.getMessage());
                  }

            }

            return value;
      }

      public boolean MarginCAL() {
            boolean value = false;
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

                  double notionalRate = 0.0;
                  double permaradub = 0.0;
                  String facid = getFACLTYID().trim();
                  String permar = getPMARGIN();
                  String permaradd = getPADDMARG();
                  if (!facid.equalsIgnoreCase("")) {
                        if (permaradd == null || permaradd.isEmpty()) {
                              permaradd = "0";
                        }
                        permaradub = Double.valueOf(permaradd);
                        // String magn = "";
                        int magne = 0;
                        String ttRateString = "";
                        // Loggers.general().info(LOG,"Percentage margin ----" + permar);
                        String mastamt = getDriverWrapper().getEventFieldAsText("ORA", "v", "m");
                        // Loggers.general().info(LOG,"Master and tolerant amount--->" +
                        // mastamt);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              Loggers.general().info(LOG,"Master and tolerant amount--->" + mastamt);
                        }
                        if (mastamt.isEmpty() || mastamt == null) {
                              // Loggers.general().info(LOG,"Master and tolerant amount is
                              // empty--->" + mastamt);
                              mastamt = "0";
                        }
                        float amunt1 = Float.valueOf(mastamt);
                        // Loggers.general().info(LOG,"amount" + amunt1);
                        long amount01 = (long) amunt1;
                        // Loggers.general().info(LOG,"current Value ----" + amount01);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              Loggers.general().info(LOG,"current Value ----" + amount01);
                        }
                        String curr = getDriverWrapper().getEventFieldAsText("AMT", "v", "c").trim();
                        String customerNo = getDriverWrapper().getEventFieldAsText("PRM", "p", "no");

                        con = ConnectionMaster.getConnection();
                        if (permar.equalsIgnoreCase("") || permar == null) {
                              String queryfac = "select margin from customermargin where facility='" + facid + "' and CIF ='"
                                          + customerNo + "'"; // Loggers.general().info(LOG,"queryfac
                                                                        // " + queryfac);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG,"queryfac " + queryfac);
                              }
                              dmsp = con.prepareStatement(queryfac);
                              // Loggers.general().info(LOG,"dmsp-----> ");
                              dmsr = dmsp.executeQuery();
                              // Loggers.general().info(LOG,"executeQuery-----> ");
                              Boolean facilityFound = false;
                              while (dmsr.next()) {
                                    // Loggers.general().info(LOG,"while margin-----> ");
                                    facilityFound = true;
                                    // Loggers.general().info(LOG,"magn--->");
                                    String magn = dmsr.getString(1).trim();
                                    // Loggers.general().info(LOG,"magn--->" + magn);
                                    magne = Integer.parseInt(magn);
                                    // Loggers.general().info(LOG,"Margin " + magn);
                                    // Loggers.general().info(LOG,"Adding amount---- " + (magne +
                                    // permaradub));

                                    if (curr.equalsIgnoreCase("INR")) {
                                          notionalRate = 1;
                                    } else {
                                          String ttRateQuery = "select trim(SPOTRATE) from spotrate where currency='" + curr + "'";
                                          // Loggers.general().info(LOG,"ttRateQuery - " +
                                          // ttRateQuery);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                Loggers.general().info(LOG,"ttRateQuery - " + ttRateQuery);
                                          }
                                          ttRatePS = con.prepareStatement(ttRateQuery);
                                          ttRateRS = ttRatePS.executeQuery();
                                          while (ttRateRS.next()) {
                                                String ttRate_String = ttRateRS.getString(1).trim();
                                                notionalRate = Double.parseDouble(ttRate_String);
                                          }
                                    }
                                    // Loggers.general().info(LOG,"notionalRate - " + notionalRate);
                                    long k = (long) (((amount01 * (magne + permaradub))) * notionalRate);
                                    String ks = String.valueOf(k);
                                    // Loggers.general().info(LOG,"Margin amount in final for set if
                                    // stmt " + ks + "" + "INR");
                                    setPMARGIN(magn);
                                    // setNOTRATE(String.valueOf(notionalRate));
                                    setMARAMT(ks + " " + "INR");
                                    //// setPRMARAMT(ks + " " + "INR");
                              }
                              if (facilityFound == false) {
                                    setPMARGIN("");
                                    // setNOTRATE("");
                                    setMARAMT("");
                                    // setPRMARAMT("");
                              }
                        } else {
                              permar = getPMARGIN();
                              // Loggers.general().info(LOG,"permar value in string else--->" +
                              // permar);
                              float permarflo = Float.valueOf(permar);
                              long magndou = (long) permarflo;
                              // Loggers.general().info(LOG,"magndou value in long else--->" +
                              // magndou);
                              // Loggers.general().info(LOG,"float value in else--->" + magndou);
                              if (curr.equalsIgnoreCase("INR")) {
                                    notionalRate = 1;
                              } else {
                                    // Loggers.general().info(LOG,"margin lc in else");
                                    String ttRateQuery = "select trim(SPOTRATE) from spotrate where currency='" + curr + "'";
                                    // Loggers.general().info(LOG,"ttRateQuery else - " +
                                    // ttRateQuery);

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG,"ttRateQuery else - " + ttRateQuery);
                                    }
                                    ttRatePS = con.prepareStatement(ttRateQuery);
                                    ttRateRS = ttRatePS.executeQuery();
                                    while (ttRateRS.next()) {
                                          String ttRate_String = ttRateRS.getString(1).trim();
                                          notionalRate = Double.parseDouble(ttRate_String);
                                    }
                              }
                              // Loggers.general().info(LOG,"Adding amount in else---- " +
                              // (magndou + permaradub));
                              long k = (long) (((amount01 * (magndou + permaradub))) * notionalRate);
                              // Loggers.general().info(LOG,"Expected margin amount in else---> -
                              // " + k);
                              String ks = String.valueOf(k);
                              // Loggers.general().info(LOG,"Margin amount in final for set else
                              // stmt " + ks + "" + "INR");

                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG,"Margin amount in final for set else stmt " + ks + "" + "INR");
                              }
                              setPMARGIN(permar);
                              // setNOTRATE(String.valueOf(notionalRate));
                              setMARAMT(ks + " " + "INR");
                              //// setPRMARAMT(ks + " " + "INR");
                        }
                  } else {

                        // Loggers.general().info(LOG,"Facility id is blank");
                        setPMARGIN("");
                        // setNOTRATE("");
                        setMARAMT("");
                        setPADDMARG("");
                  }
            } catch (Exception e) {
                  // Loggers.general().info(LOG,"exception caught on margin button validation
                  // " + e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {

                        Loggers.general().info(LOG,"exception caught on margin button validation " + e.getMessage());
                  }
            } finally {
                  try {
                        if (ttRateRS != null)
                              ttRateRS.close();
                        if (ttRatePS != null)
                              ttRatePS.close();
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

            return value;

      }

      public boolean deleteLienGridDetails() {
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
            String step_Id = getDriverWrapper().getEventFieldAsText("CSID", "s", "").trim();
            try {

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Issue shipping lien Table grid is clear");
                  }
                  EnigmaArray<ExtEventLienMarkEntityWrapper> loanDetails = getExtEventLienMarkData();

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Issue shipping lien Grid details size" + loanDetails.getSize());
                  }
                  for (ExtEventLienMarkEntityWrapper detailsEntityWrapper : loanDetails) {
                        removeExtEventLienMark(detailsEntityWrapper);
                  }

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Issue shipping after delete " + loanDetails.getSize());

                  }
                  return true;
            } catch (Exception e) {
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Exception Issue shipping in Grid details delete" + e.getMessage());
                  }
                  return false;
            }
      }

      public void onDELETEISSUESHIPGROUPLayButton() {

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
            String step_input = getDriverWrapper().getEventFieldAsText("CSID", "s", "").trim();
            if (getMajorCode().equalsIgnoreCase("ILC") && getMinorCode().equalsIgnoreCase("LSC")
                        && (step_input.equalsIgnoreCase("CSM") || step_input.equalsIgnoreCase("CBS Maker"))) {

                  try {
                        String refNumber = getDriverWrapper().getEventFieldAsText("MST", "r", "");

                        String query = "select step.status from baseevent ev, master m, stephist step, eventstep evstep where ev.master_key = m.key97 and step.event_key = ev.key97 and step.eventstep = evstep.key97 and m.master_ref = '"
                                    + refNumber + "' order by step.timestart desc";

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Step ID query in ELC pane===> " + query);
                        }
                        con = ConnectionMaster.getConnection();
                        ps = con.prepareStatement(query);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                              String stepID = rs.getString(1).trim();
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Custom value step id===> " + stepID);
                              }
                              if (stepID.length() > 0 && !stepID.equalsIgnoreCase("P")) {
                                    deleteLienGridDetails();

                              }

                              getBtnDELETEISSUESHIPGROUPLay().setEnabled(false);
                        }
                  } catch (Exception e) {

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception Custom value cleared===> " + e.getMessage());
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

      }
// button to get forward details added by Vishal G
      public void onFORWARDCNTFREECORRlayButton() {
        String customer = getDriverWrapper().getEventFieldAsText("PRI", "p", "no").trim();
            String inputBranch = getDriverWrapper().getEventFieldAsText("BIN", "b", "c").trim();
            String today = getDriverWrapper().getEventFieldAsText("TDY", "d", "").trim();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String output="";
            Calendar c = Calendar.getInstance();
            try {
            c.setTime(sdf.parse(today));
            c.add(Calendar.DATE, -10);
             output = sdf.format(c.getTime());
            }catch (Exception e) {
                  e.printStackTrace();
            }
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
                  EnigmaArray<ExtEventForwardContractEntityWrapper> liste = getExtEventForwardContractData();
                  Iterator<ExtEventForwardContractEntityWrapper> iterator = liste.iterator();         
                  String query="";
                  for (int i = 0; i < liste.getSize().intValue(); i++) {
                        while (iterator.hasNext()) {

                              ExtEventForwardContractEntityWrapper val1 = (ExtEventForwardContractEntityWrapper) iterator
                                          .next();

                              String forwardno = val1.getFWCCONTRACTNO().trim();
                  //          String formNumber=val1.getCFORMN();
                              
                              // String iecode = val1.getCIECOD();
                              System.out.println("forwardno bill no :" + forwardno+" "+inputBranch);
                              
                              
                              query = "SELECT CATEGORY, FWC_CONTRACT_NO, TO_CHAR(to_date(BOOKING_DATE,'DD-MM-YY'),'YY-MM-DD'), FWC_AMOUNT, TO_CCY_AMT, TO_CHAR(to_date(DEAL_VALID_FROM,'DD-MM-YY'),'YY-MM-DD'), TO_CHAR(to_date(DEAL_VALID_TO,'DD-MM-YY'),'YY-MM-DD'), "
                                          + "TREASURY_REF_NO, TREASURY_RATE, WASH_RATE, PL_AMOUNT FROM CUSTOM_FWC_DETAILS WHERE FWC_CONTRACT_NO = '"
                                          + forwardno + "' " + " and status='APPROVED' AND CIF_ID='" + customer + "' "
                                          + " AND BRANCH='" + inputBranch + "'" + " and TO_DATE(BOOKING_DATE,'DD-MM-YY') between TO_DATE('" + output + "','DD-MM-YY') AND TO_DATE('" + today + "','DD-MM-YY')";
;
                              
                              
                              // + "AND shp.iecode ='"+iecode+"' " ;
                              System.out.println("SQL QUERY: " + query);
                              con = ConnectionMaster.getConnection();
                              pst = con.prepareStatement(query);
                              rst = pst.executeQuery();
                              
                              while (rst.next()) {
                                    System.out.println("inside while of forward details"+rst.getString(3)+" "+rst.getString(7));
                                    val1.setCATEGORY(rst.getString(1));
                                    val1.setFWCCONTRACTNO(rst.getString(2));
                                    val1.setBOOKINGDATE(rst.getString(3));
                                    val1.setFWCAMOUNT(rst.getString(4));
                                    val1.setTOCCYAMT(rst.getString(5));
                                    val1.setDEALVALIDFROM(rst.getString(6));
                                    val1.setDEALVALIDTO(rst.getString(7));
                                    val1.setTREASURYREFNO(rst.getString(8));
                                    val1.setTREASURY_RATE(rst.getString(9));
                                    val1.setWASH_RATE(rst.getString(10));
                        //          val1.setPLAMT(rst.getString(11));
                              }

                        }
                  }
            } catch (SQLException e) {
                  e.printStackTrace();
            } finally {

                  try {
                        if (rst != null)
                              rst.close();
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
            
      
      }
      
      // button to get forward amount details added by Vishal G
      public void onFORWARDCHGFREECORRlayButton() {
        String customer = getDriverWrapper().getEventFieldAsText("PRI", "p", "no").trim();
            String inputBranch = getDriverWrapper().getEventFieldAsText("BIN", "b", "c").trim();
            String today = getDriverWrapper().getEventFieldAsText("TDY", "d", "").trim();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String output="";
            Calendar c = Calendar.getInstance();
            try {
            c.setTime(sdf.parse(today));
            c.add(Calendar.DATE, -10);
             output = sdf.format(c.getTime());
            }catch (Exception e) {
                  e.printStackTrace();
            }
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
                  EnigmaArray<ExtEventForwardChargeEntityWrapper> liste = getExtEventForwardChargeData();
                  Iterator<ExtEventForwardChargeEntityWrapper> iterator = liste.iterator();           
                  String query="";
                  for (int i = 0; i < liste.getSize().intValue(); i++) {
                        while (iterator.hasNext()) {

                              ExtEventForwardChargeEntityWrapper val1 = (ExtEventForwardChargeEntityWrapper) iterator
                                          .next();

                              String forwardno = val1.getFWCCONTRACTN().trim();
                  //          String formNumber=val1.getCFORMN();
                              
                              // String iecode = val1.getCIECOD();
                              System.out.println("forwardno bill no :" + forwardno+" "+inputBranch);
                              
                              
                              query = "SELECT CATEGORY, FWC_CONTRACT_NO, CHARGE_AMOUNT, GST_AMOUNT  FROM CUSTOM_FWC_DETAILS WHERE FWC_CONTRACT_NO = '"+forwardno+"' "+
                                          " and status='APPROVED' AND CIF_ID='"+customer+"' "+
                                          " AND BRANCH='"+inputBranch+"'"+
                                          " and TO_DATE(BOOKING_DATE,'DD-MM-YY') between TO_DATE('" + output + "','DD-MM-YY') AND TO_DATE('" + today + "','DD-MM-YY')";
;
                              
                              
                              // + "AND shp.iecode ='"+iecode+"' " ;
                              System.out.println("SQL QUERY of charge: " + query);
                              con = ConnectionMaster.getConnection();
                              pst = con.prepareStatement(query);
                              rst = pst.executeQuery();
                              
                              while (rst.next()) {
                                    System.out.println("inside while of forward charge");
                                    val1.setCATEGOR(rst.getString(1));
                                    val1.setFWCCONTRACTN(rst.getString(2));
                                    val1.setCHARGE_AMOUNT(rst.getString(3));
                                    val1.setGST_AMOUNT(rst.getString(4));
                              }

                        }
                  }
            } catch (SQLException e) {
                  e.printStackTrace();
            } finally {

                  try {
                        if (rst != null)
                              rst.close();
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
            
      
      }
      public void oncalcmarginSHPGUALODCLAIMclayButton() {
            MarginCAL();

      }

      public void oncalcmarginISSUESHIPGROUPLayButton() {

            MarginCAL();

      }

      public void oncalcmarginSHPGUACREclayButton() {
            MarginCAL();

      }

      public void onFetchlienSHPGUACREclayButton() {
            lienMark();
      }

      public void oncalcmarginSHPGUASETTCLAclayButton() {
            MarginCAL();

      }

}