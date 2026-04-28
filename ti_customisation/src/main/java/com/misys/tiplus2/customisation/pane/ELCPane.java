package com.misys.tiplus2.customisation.pane;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;

//import org.apache.log4j.Logger;

import com.misys.tiplus2.apps.ti.kernel.extpm.entity.ExtEvent;
import com.misys.tiplus2.apps.ti.kernel.extpm.entity.Key_ExtEvent;
import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;
import com.misys.tiplus2.customisation.entity.ExtEventAdvanceTableEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventAccountRealisation;
import com.misys.tiplus2.customisation.entity.ExtEventAccountRealisationEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventFircDetails;
import com.misys.tiplus2.customisation.entity.ExtEventFircDetailsEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventInvoiceDetailsEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventInvoiceTableEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventInvoicedetailsLC;
import com.misys.tiplus2.customisation.entity.ExtEventInvoicedetailsLCEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventLoanDetails;
import com.misys.tiplus2.customisation.entity.ExtEventLoanDetailsEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventServiceTax;
import com.misys.tiplus2.customisation.entity.ExtEventServiceTaxEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventShippingTableEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventShippingdetailsEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventShippingdetailslc;
import com.misys.tiplus2.customisation.entity.ExtEventShippingdetailslcEntityWrapper;
import com.misys.tiplus2.customisation.extension.ConnectionMaster;
import com.misys.tiplus2.customisation.extension.OdcFEC;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import com.misys.tiplus2.enigma.customisation.pane.ExtensionViewPaneMode;
import com.misys.tiplus2.enigma.customisation.pane.ExtensionViewWebPane;
import com.misys.tiplus2.enigma.customisation.validation.ValidationTexts;
import com.misys.tiplus2.enigma.lang.EnigmaException;
import com.misys.tiplus2.enigma.lang.EnigmaExceptionCode;
import com.misys.tiplus2.enigma.lang.control.EnigmaControl;
import com.misys.tiplus2.enigma.lang.datatype.EnigmaArray;
import com.misys.tiplus2.enigma.lang.util.PaneManager;
import com.misys.tiplus2.foundations.lang.logging.Loggers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.misys.tiplus2.foundations.lang.logging.Loggers;
public class ELCPane extends EventPane {
      //private static final Logger logger = Logger.getLogger(ConnectionMaster.class.getName());
      private static final Logger LOG = LoggerFactory.getLogger(ELCPane.class);
      Connection con, con1 = null;
      PreparedStatement dmsp, ps, ps1, ps2, prepare1, prepare2, dmsp1, psd, pst,ps3,ps4,account = null;
      ResultSet dmsr, rs, rs1, rs2,rs3,rs4, result, result2 = null;
      ResultSet dmsr1, rst,rst2,rst1 = null;
      String swachhCharge = "";
      String serviceTax = "";
      String MTref103 = "";

      ConnectionMaster cm = new ConnectionMaster();

      // public void onSERVICEEXPSTDLCDPclayButton() {
      // // ////Loggers.general().info(LOG,"FIRCButton");
      // if (SERVICFECTH()) {
      // // ////Loggers.general().info(LOG,"onSERVICEELCADVclayButton calling");
      // } else {
      // //// Loggers.general().info(LOG,"Else systemOutput");
      // }
      // }

      public void onFETCHIFSCELCADJclayButton() {
            //// Loggers.general().info(LOG,"Fetch IFSC Adjust called");
            FETCHIFSC();
            // FETCHIFSCIncoming();
      }

      // public void onSERVICEELCSETTclayButton() {
      // String strLog = "Log";
      // String dailyval_Log = "";
      // @SuppressWarnings("unchecked")
      // AdhocQuery<? extends
      // com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog =
      // getDriverWrapper()
      // .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
      // EXTGENCUSTPROP CodeLog = queryLog.getUnique();
      // if (CodeLog != null) {
      //
      // dailyval_Log = CodeLog.getPropval();
      // } else {
      // // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");
      //
      // }
      // EnigmaArray<ExtEventServiceTaxEntityWrapper> faclist =
      // getExtEventServiceTaxData();
      // Loggers.general().info(LOG,"Step for faclist before delete=====>" +
      // faclist.getSize());
      // Iterator<ExtEventServiceTaxEntityWrapper> iterator = faclist.iterator();
      // ExtEventServiceTaxEntityWrapper sdwrapper =
      // (ExtEventServiceTaxEntityWrapper) iterator.next();
      //
      // try {
      //
      // Loggers.general().info(LOG,"Step for faclist description before delete=====>" +
      // sdwrapper.getDESCR());
      // getExtEventServiceTaxData().clear();
      // getExtEventServiceTaxData().remove(null);
      // Loggers.general().info(LOG,"Step for faclist after clear data=====>" +
      // faclist.getSize());
      // Loggers.general().info(LOG,"Step for faclist description after clear
      // data=====>=====>" + sdwrapper.getDESCR());
      // for (ExtEventServiceTaxEntityWrapper ExtEventServiceTaxEntityWrapper :
      // faclist) {
      // removeExtEventServiceTax(ExtEventServiceTaxEntityWrapper);
      //
      // }
      //
      // EnigmaControl sa = getExtEventServiceTaxDelete();
      //
      // sa.isAutoSubmit();
      // Loggers.general().info(LOG,"AutoSubmit() ------>" + sa.isAutoSubmit());
      // Loggers.general().info(LOG,"Step for faclist final result=====>" +
      // faclist.getSize());
      // Loggers.general().info(LOG,"Step for faclist description final result=====>" +
      // sdwrapper.getDESCR());
      //
      // } catch (NullPointerException e) {
      // // TODO: handle exception
      // } catch (Exception e) {
      // // TODO: handle exception
      // }
      //
      // }

      // public void onSERVICEELCDPclayButton() {
      //
      // EnigmaArray<ExtEventServiceTaxEntityWrapper> faclist =
      // getExtEventServiceTaxData();
      // Loggers.general().info(LOG,"Step for faclist before delete=====>" +
      // faclist.getSize());
      // Iterator<ExtEventServiceTaxEntityWrapper> iterator = faclist.iterator();
      // ExtEventServiceTaxEntityWrapper sdwrapper =
      // (ExtEventServiceTaxEntityWrapper) iterator.next();
      //
      // try {
      //
      // Loggers.general().info(LOG,"Step for faclist description before delete=====>" +
      // sdwrapper.getDESCR());
      // getExtEventServiceTaxData().clear();
      // getExtEventServiceTaxData().remove(null);
      // Loggers.general().info(LOG,"Step for faclist after clear data=====>" +
      // faclist.getSize());
      // Loggers.general().info(LOG,"Step for faclist description after clear
      // data=====>=====>" + sdwrapper.getDESCR());
      // for (ExtEventServiceTaxEntityWrapper ExtEventServiceTaxEntityWrapper :
      // faclist) {
      // removeExtEventServiceTax(ExtEventServiceTaxEntityWrapper);
      //
      // }
      //
      // EnigmaControl sa = getExtEventServiceTaxDelete();
      //
      // sa.isAutoSubmit();
      // Loggers.general().info(LOG,"AutoSubmit() ------>" + sa.isAutoSubmit());
      // Loggers.general().info(LOG,"Step for faclist final result=====>" +
      // faclist.getSize());
      // Loggers.general().info(LOG,"Step for faclist description final result=====>" +
      // sdwrapper.getDESCR());
      //
      // } catch (NullPointerException e) {
      // // TODO: handle exception
      // } catch (Exception e) {
      // // TODO: handle exception
      // }
      //
      // }

      // public boolean SERVICFECTH() {
      // boolean value = false;
      // // getExtEventServiceTaxNew().setEnabled(false);
      // // getExtEventServiceTaxDelete().setEnabled(false);
      // // getExtEventServiceTaxUpdate().setEnabled(false);
      // // getExtEventServiceTaxUp().setEnabled(false);
      // // getExtEventServiceTaxDown().setEnabled(false);
      // // getExtEventServiceTaxView().setEnabled(false);
      // // String masterRefNumber =
      // // getDriverWrapper().getEventFieldAsText("MST", "r", "");
      // // // ////Loggers.general().info(LOG,"Master Reference" + masterRefNumber);
      // //
      // // String eventPrefix = getDriverWrapper().getEventFieldAsText("EPF",
      // // "s", "");
      // // String eventPrefixSerialNo =
      // // getDriverWrapper().getEventFieldAsText("ESQ", "i", "");
      // // Integer eventPrefixNo = Integer.parseInt(eventPrefixSerialNo);
      // // EnigmaArray<ExtEventServiceTaxEntityWrapper> liste =
      // // getExtEventServiceTaxData();
      // // int count = 0;
      // // Iterator<ExtEventServiceTaxEntityWrapper> iterator =
      // // liste.iterator();
      // // // for (int i = 0; i < seivce.size(); i++) {
      // // // ExtEventServiceTax serviceTax = seivce.get(i);
      // // // }
      // //
      // // if (liste.getSize() < 1) {
      // // try {
      // // //// Loggers.general().info(LOG,"calling service tax query");
      // // String query = "SELECT
      // //
      // TRIM(DESCR),TRIM(CHARGE_AMT),TRIM(SERVICE_TAX),TRIM(EDU_CESS),TRIM(KRISHI_CESS)
      // // FROM ETTV_SERVICETAX_SWACH_CALC WHERE REFNO_PFIX='"
      // // + eventPrefix + "' AND REFNO_SERL=" + eventPrefixNo + " AND
      // // MASTER_REF='" + masterRefNumber
      // // + "'";
      // //
      // // //// Loggers.general().info(LOG,"Service tax query - " + query);
      // // con = ConnectionMaster.getConnection();
      // // PreparedStatement ps1 = con.prepareStatement(query);
      // // ResultSet rs1 = ps1.executeQuery();
      // //
      // // while (rs1.next()) {
      // // Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
      // // ExtEventServiceTax serviceTax = new ExtEventServiceTax();
      // // serviceTax.setColumn("DESCR", rs1.getString(1));
      // //
      // // serviceTax.setColumn("CHRGAMT", rs1.getString(2) + " " + "INR");
      // //
      // // serviceTax.setColumn("SERVTAX", rs1.getString(3) + " " + "INR");
      // //
      // // serviceTax.setColumn("EDUCES", rs1.getString(4) + " " + "INR");
      // //
      // // serviceTax.setColumn("KRISH", rs1.getString(5) + " " + "INR");
      // //
      // // serviceTax.setNewKey();
      // // serviceTax.setFk(fkey);
      // // serviceTax.setSequence(count);
      // //
      // // ExtEventServiceTaxEntityWrapper projectdetchk = new
      // // ExtEventServiceTaxEntityWrapper(serviceTax,
      // // getDriverWrapper());
      // // addNewExtEventServiceTax(projectdetchk);
      // //
      // // count++;
      // //
      // // getExtEventServiceTaxNew().setEnabled(false);
      // // getExtEventServiceTaxDelete().setEnabled(false);
      // // getExtEventServiceTaxUpdate().setEnabled(false);
      // // getExtEventServiceTaxUp().setEnabled(false);
      // // getExtEventServiceTaxDown().setEnabled(false);
      // // getExtEventServiceTaxView().setEnabled(false);
      // // }
      // //
      // // //// Loggers.general().info(LOG,"ServiceTax out of loop");
      // //
      // // } catch (Exception e) {
      // // //// Loggers.general().info(LOG,"Exeception of service tax- " +
      // // //// e.getMessage());
      // // } finally {
      // // try {
      // // if (con != null) {
      // // con.close();
      // // if (ps1 != null)
      // // ps1.close();
      // // if (rs1 != null)
      // // rs1.close();
      // // }
      // // } catch (SQLException e) {
      // // //// Loggers.general().info(LOG,"Connection Failed! Check output
      // // //// console");
      // // e.printStackTrace();
      // // }
      // // }
      // //
      // // } else {
      // // //// Loggers.general().info(LOG,"Service tax grid value greater then 1 ");
      // // }
      // return value;
      // }

      // public void onFETCHLOANELCDPclayButton() {
      //
      // String masterRefNumber = getDriverWrapper().getEventFieldAsText("MST",
      // "r", "");
      // String eventPrefix = getDriverWrapper().getEventFieldAsText("EPF", "s",
      // "");
      // String evvcount = getDriverWrapper().getEventFieldAsText("ESQ", "i", "");
      // String productType = getDriverWrapper().getEventFieldAsText("FPP:XFPTP",
      // "s", ""); // FCA
      // String crystal = getDriverWrapper().getEventFieldAsText("cBOJ", "l", "");
      // String billAmt = getDriverWrapper().getEventFieldAsText("cBOH", "v",
      // "m");
      // Loggers.general().info(LOG,"Discounted Bill Amount----> " + billAmt);
      //
      // if (productType.equalsIgnoreCase("FCA") && crystal.equalsIgnoreCase("N")
      // && getMinorCode().equalsIgnoreCase("DOP")) {
      // try {
      // // //Loggers.general().info(LOG,"With converstion Expot----> ");
      // String query_with = "select TRIM(WITH_CONV),TRIM(WITH_CONV_CCY) from
      // ETT_VSELL_FCYTAX_WITHCONV where MASTER_REF = '"
      // + masterRefNumber + "' AND REFNO_PFIX ='" + eventPrefix + "' AND
      // REFNO_SERL='" + evvcount + "'";
      //
      // con = ConnectionMaster.getConnection();
      // ps1 = con.prepareStatement(query_with);
      // rs1 = ps1.executeQuery();
      // Loggers.general().info(LOG,"Discounted Bill Amount query " + query_with);
      // if (rs1.next()) {
      //
      // String conamt = rs1.getString(1).trim();
      // String conccy = rs1.getString(2).trim();
      // Loggers.general().info(LOG,"With converstion amount and currency Expot----> " +
      // rs1.getString(1));
      // setBILRELAM(conamt + " INR");
      //
      // }
      // // else
      // // {
      // // setBILRELAM("");
      // // }
      // } catch (Exception e) {
      // // Loggers.general().info(LOG,"With converstion Expot----> " +
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
      // } else if ((productType.equalsIgnoreCase("INA")) &&
      // crystal.equalsIgnoreCase("N")
      // && getMinorCode().equalsIgnoreCase("DOP")) {
      // String dealAmt = getDriverWrapper().getEventFieldAsText("FPP:XB+DA", "v",
      // "m");
      // Loggers.general().info(LOG,"Finance deal amount =====>" + dealAmt);
      // setBILRELAM(dealAmt + " INR");
      //
      // }
      //
      // // else if (crystal.equalsIgnoreCase("Y")) {
      // // String cryloanAmt = getDriverWrapper().getEventFieldAsText("cBOL",
      // // "v", "m");
      // // Loggers.general().info(LOG,"Crystallization Amount----> " + cryloanAmt);
      // // String billAmount = getDriverWrapper().getEventFieldAsText("cBOH",
      // // "v", "m");
      // // Loggers.general().info(LOG,"Discounted Bill Amount =====>" + billAmount);
      // // BigDecimal dealAmt_Big = new BigDecimal(cryloanAmt);
      // // BigDecimal billAmount_Big = new BigDecimal(billAmount);
      // // BigDecimal totalAmt = dealAmt_Big.subtract(billAmount_Big);
      // // Loggers.general().info(LOG,"Total deal amount =====>" + totalAmt);
      // // String finalvalue = String.format("%.2f", totalAmt);
      // // Loggers.general().info(LOG,"Final deal amount =====>" + finalvalue);
      // // setPRFT(finalvalue + " INR");
      // // } else if (!productType.equalsIgnoreCase("FCA") ||
      // // !productType.equalsIgnoreCase("INA")) {
      // // setBILRELAM("");
      // // setPRFT("");
      // // setCYRLOAN(false);
      // // }
      //
      // }

      // public void onFETCHLOANELCSETTclayButton() {
      //
      // String masterRefNumber = getDriverWrapper().getEventFieldAsText("MST",
      // "r", "");
      // String eventPrefix = getDriverWrapper().getEventFieldAsText("EPF", "s",
      // "");
      // String evvcount = getDriverWrapper().getEventFieldAsText("ESQ", "i", "");
      // String productType = getDriverWrapper().getEventFieldAsText("FPP:XFPTP",
      // "s", ""); // FCA
      // Loggers.general().info(LOG,"productType----> " + productType);
      // String crystal = getDriverWrapper().getEventFieldAsText("cBOJ", "l", "");
      // String billAmt = getDriverWrapper().getEventFieldAsText("cBOH", "v",
      // "m");
      // Loggers.general().info(LOG,"Discounted Bill Amount----> " + billAmt);
      //
      // if (productType.equalsIgnoreCase("FCA") && crystal.equalsIgnoreCase("N")
      // && getMinorCode().equalsIgnoreCase("POD")) {
      // try {
      // Loggers.general().info(LOG,"Discounted Bill Amount query call if loop");
      // String query_with = "select TRIM(WITH_CONV),TRIM(WITH_CONV_CCY) from
      // ETT_VSELL_FCYTAX_WITHCONV where MASTER_REF = '"
      // + masterRefNumber + "' AND REFNO_PFIX ='" + eventPrefix + "' AND
      // REFNO_SERL='" + evvcount + "'";
      //
      // con = ConnectionMaster.getConnection();
      // ps1 = con.prepareStatement(query_with);
      // rs1 = ps1.executeQuery();
      // Loggers.general().info(LOG,"Discounted Bill Amount query " + query_with);
      // if (rs1.next()) {
      //
      // String conamt = rs1.getString(1).trim();
      // String conccy = rs1.getString(2).trim();
      // Loggers.general().info(LOG,"With converstion amount and currency Expot----> " +
      // rs1.getString(1));
      // setBILRELAM(conamt + " INR");
      //
      // }
      // // else
      // // {
      // // setBILRELAM("");
      // // }
      // } catch (Exception e) {
      // // Loggers.general().info(LOG,"With converstion Expot----> " +
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
      // } else if ((productType.equalsIgnoreCase("INA")) &&
      // crystal.equalsIgnoreCase("N")
      // && getMinorCode().equalsIgnoreCase("POD")) {
      // Loggers.general().info(LOG,"productType INA----> " + productType);
      // String dealAmt = getDriverWrapper().getEventFieldAsText("FPP:XB+DA", "v",
      // "m");
      // Loggers.general().info(LOG,"Finance deal amount =====>" + dealAmt);
      // setBILRELAM(dealAmt + " INR");
      //
      // }
      //
      // // else if (crystal.equalsIgnoreCase("Y")) {
      // // Loggers.general().info(LOG,"productType INA----> " + productType);
      // // String cryloanAmt = getDriverWrapper().getEventFieldAsText("cBOL",
      // // "v", "m");
      // // Loggers.general().info(LOG,"Crystallization Amount----> " + cryloanAmt);
      // //
      // // String billAmount = getDriverWrapper().getEventFieldAsText("cBOH",
      // // "v", "m");
      // // Loggers.general().info(LOG,"Discounted Bill Amount =====>" + billAmount);
      // // BigDecimal dealAmt_Big = new BigDecimal(cryloanAmt);
      // // BigDecimal billAmount_Big = new BigDecimal(billAmount);
      // // BigDecimal totalAmt = dealAmt_Big.subtract(billAmount_Big);
      // // Loggers.general().info(LOG,"Total deal amount =====>" + totalAmt);
      // // String finalvalue = String.format("%.2f", totalAmt);
      // // Loggers.general().info(LOG,"Final deal amount =====>" + finalvalue);
      // // setPRFT(finalvalue + " INR");
      // // } else if (!productType.equalsIgnoreCase("FCA") ||
      // // !productType.equalsIgnoreCase("INA")) {
      // // Loggers.general().info(LOG,"productType clear----> " + productType);
      // // setBILRELAM("");
      // // setPRFT("");
      // // setCYRLOAN(false);
      // // } else {
      // // Loggers.general().info(LOG,"Else part----> " + productType + "billAmt===>" +
      // // billAmt);
      // // }
      //
      // }

      public void onFETCHIFSCELCSETTclayButton() {
            // FETCHIFSCIncoming();
            if (FETCHIFSC()) {
                  //// Loggers.general().info(LOG,"Sender ifsc calling");
            } else {
                  //// Loggers.general().info(LOG,"Else systemOutput");
            }
      }

      public void onFETCHIFSCELCADVclayButton() {
            // FETCHIFSCIncoming();
            if (FETCHIFSC()) {
                  //// Loggers.general().info(LOG,"Sender ifsc calling Advise");
            }
      }

      public void onFETCHIFSCELCAMDclayButton() {
            // FETCHIFSCIncoming();
            //// Loggers.general().info(LOG,"Fetch IFSC Amend called");
            FETCHIFSC();
      }

      public boolean FETCHIFSC() {
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

                  String behalfHalfBranch = getDriverWrapper().getEventFieldAsText("BOB", "s", "");
                  String amountCurrency = getDriverWrapper().getEventFieldAsText("ORA", "v", "c");
                  String subProductType = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
                  String subProd = getDriverWrapper().getEventFieldAsText("PUL1", "s", "").trim();
                  String query = "";
                  String senderIfsc = "";
                  if (subProductType.equalsIgnoreCase("ELD") && amountCurrency.equalsIgnoreCase("INR")) {
                        query = "select trim(IFSC) from extbramas where BCODE='" + behalfHalfBranch + "' ";
                        con = ConnectionMaster.getConnection();
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                              senderIfsc = rs1.getString(1);
                        }
                        //// Loggers.general().info(LOG,"Sender IFSC code----->" + senderIfsc);
                        setSENIFSC(senderIfsc);
                  }
            } catch (Exception e) {
                  //// Loggers.general().info(LOG,"Exeception of recifsc " + e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Exeception of recifsc " + e.getMessage());

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
            return value;
      }

      public void onMERCHANTELCADJclayButton() {
            // String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r",
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
                  con = ConnectionMaster.getConnection();
                  String mercht = getDriverWrapper().getEventFieldAsText("cARQ", "l", "").toString();
                  String relrefno = getREMERREF().trim();
                  String adremno = getINWREMNO();

                  int dmT = 0;

                  try {

                        String dms = "SELECT DISTINCT DECODE(trim(mas.MASTER_REF) ,NULL ,0,1)AS REMERREF FROM master mas, BASEEVENT bev, EXTEVENT exte, PRODTYPE prod WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = exte.EVENT AND mas.PRODTYPE = prod.KEY97 AND mas.MASTER_REF ='"
                                    + relrefno + "'";
                        //// Loggers.general().info(LOG,"Master ref no valid for Export lc" +
                        //// dms);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Master ref no valid for Export lc" + dms);

                        }

                        ps = con.prepareStatement(dms);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                              //// Loggers.general().info(LOG,"while--->");
                              dmT = rs.getInt(1);
                              //// Loggers.general().info(LOG,"AFTER GET THE VALUE " + dmT);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"AFTER GET THE VALUE " + dmT);

                              }
                              if (dmT > 0 && mercht.equalsIgnoreCase("Y")) {
                                    try {
                                          // ////Loggers.general().info(LOG,"enter into try");

                                          String query_dms = "SELECT TO_CHAR(ext.MERDUET,'DD/MM/YY') AS MERDUET FROM master mas, baseevent bev, extevent ext WHERE mas.key97 = bev.master_key AND bev.key97 = ext.event AND bev.refno_pfix IN ('AMD','ADJ','ISS','CLM','CRE') AND mas.master_ref ='"
                                                      + relrefno + "'";
                                          //// Loggers.general().info(LOG,"values fetching Export lc"
                                          //// +
                                          //// query_dms);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"values fetching Export lc" + query_dms);

                                          }
                                          ps = con.prepareStatement(query_dms);
                                          rs = ps.executeQuery();
                                          while (rs.next()) {
                                                String merdate = rs.getString(1);
                        //                      setMERDUET(merdate);
                                                //// Loggers.general().info(LOG,"AFTER GET THE VALUE " +
                                                //// dmT);
                                          }

                                    } catch (Exception e) {
                                          //// Loggers.general().info(LOG,e.getMessage());
                                    }

                              } else {
                                    //// Loggers.general().info(LOG,"Merchant trade is not tickec or
                                    //// master not valide lc or collection--->" + dmT +
                                    //// mercht);
                              }

                        }

                  } catch (Exception e) {
                        //// Loggers.general().info(LOG,"Merchanting details in lc--->" +
                        //// e.getMessage());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Merchanting details in lc--->" + e.getMessage());

                        }
                  }

                  try {
                        // inward renittance
                        String query_adv = "SELECT DECODE(trim(mas.MASTER_REF) ,NULL ,0,1)AS InwardNo FROM master mas, BASEEVENT bev, EXTEVENT exte, master mas1, PRODTYPE prod WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = exte.EVENT AND mas1.PRODTYPE = prod.KEY97 AND prod.CODE ='XAR' AND mas1.MASTER_REF ='"
                                    + adremno + "'";
                        //// Loggers.general().info(LOG,"Advance rem no valid for Export lc" +
                        //// query_adv);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Advance rem no valid for Export lc" + query_adv);

                        }
                        Connection con = ConnectionMaster.getConnection();
                        ps = con.prepareStatement(query_adv);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                              dmT = rs.getInt(1);
                              //// Loggers.general().info(LOG,"query_adv AFTER GET THE VALUE " +
                              //// dmT);

                              if (dmT > 0 && mercht.equalsIgnoreCase("Y")) {
                                    try {

                                          String query_dms = "SELECT TO_CHAR(cpm.RCV_DATE,'DD/MM/YY') AS REV_DATE FROM master mas, CPAYMASTER cpm, PARTYDTLS prt, TIDATAITEM tid WHERE mas.key97 = tid.MASTER_KEY AND tid.KEY97 = prt.KEY97 AND mas.KEY97 = cpm.KEY97 AND prt.ROLE = 'BEN' AND mas.master_ref ='"
                                                      + adremno + "'";
                                          //// Loggers.general().info(LOG,"Advance rem no values
                                          //// fetching
                                          //// Export lc" + query_dms);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Advance rem no values fetching Export lc" + query_dms);

                                          }
                                          ps = con.prepareStatement(query_dms);
                                          rs = ps.executeQuery();
                                          while (rs.next()) {
                                                String recdate = rs.getString(1);
                                                setADRECDT(recdate);
                                                //// Loggers.general().info(LOG,"Outward received date
                                                //// fetching " + recdate);

                                                //// Loggers.general().info(LOG,"systemDate date --->" +
                                                //// recdate);

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Outward received date fetching " + recdate);
                                                      Loggers.general().info(LOG,"systemDate date --->" + recdate);

                                                }
                                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                                                Calendar cal = Calendar.getInstance();
                                                int gra = 270;

                                                try {
                                                      cal.setTime(sdf.parse(recdate));
                                                      // ////Loggers.general().info(LOG,"expdate in
                                                      // issue------->
                                                      // ");
                                                      cal.add(Calendar.DATE, gra);
                                                      String output = sdf.format(cal.getTime());
                                                      //// Loggers.general().info(LOG,"output----->" +
                                                      //// output);
                  //                                  setMERDUET(output);

                                                } catch (Exception e) {
                                                      //// Loggers.general().info(LOG,"Sight value date
                                                      //// --->"
                                                      //// + e.getMessage());
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            Loggers.general().info(LOG,"Sight value date --->" + e.getMessage());
                                                      }

                                                }

                                          }

                                    } catch (Exception e) {
                                          //// Loggers.general().info(LOG,e.getMessage());
                                    }

                              } else {
                                    //// Loggers.general().info(LOG,"Merchant trade is not ticked or
                                    //// master not valide outward --->" + dmT + mercht);
                              }

                        }

                  } catch (Exception e) {
                        //// Loggers.general().info(LOG,"Merchanting details outward--->" +
                        //// e.getMessage());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Merchanting details outward--->" + e.getMessage());
                        }
                  }

            } catch (Exception e) {
                  //// Loggers.general().info(LOG,"Merchanting details outward--->" +
                  //// e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Exception Merchanting details--->" + e.getMessage());
                  }
            }

            finally {
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

      public void onNOSTROELCSETTclayButton() {

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
//              String nostref_MT103102 = getNOSTMT().trim();
//              String nostref_MT940950 = getNOSTRM().trim();
//              String masReference = getDriverWrapper().getEventFieldAsText("MST", "r", "");
//              String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
//              String Credit_AmountMT103202 = "";
//              String Credit_curMT103202 = "";
//              String ValuedateMT103202 = "";
//              String Credit_AmountMT940950 = "";
//              String Credit_curMT940950 = "";
//              String CreditAccountMT940950 = "";
//              String Nostro_key = "";
//              String msg940950_1 = "";
//              String msg940950_2 = "";
//              String msg940950_concat = "";
//              String swift_type = "";
//              con = ConnectionMaster.getConnection();
//              if (nostref_MT103102.length() > 0) {
//                try {
      //
//                    String query = "SELECT MT103_MT202_AMOUNT AS CreditMT103202, MT103_MT202_CURRENCY_CODE AS CreditCurMT103202, TO_CHAR(MT103_MT202_VALUE_DATE,'DD/MM/YY') AS ValuedateMT103202, MT940_MT950_AMOUNT AS CreditAmountMT940950, MT940_MT950_CURRENCY_CODE AS CreditCurMT940950, MT940_MT950_ACCOUNT_NO AS CreditAccountMT940950, MT940_MT950_IND_TEXT, MT940_MT950_ACC_OWNER_INFO,Trim(SWIFT_TYPE),MT940_MT950_REFERENCE_NUMBER ,NOSTRO_KEY FROM ETTV_NOSTRO_MT103_MT202_TBL WHERE MT103_MT202_REFERENCE_NUMBER='"
//                          + nostref_MT103102 + "'";
//                    if (dailyval_Log.equalsIgnoreCase("YES")) {
//                      Loggers.general().info(LOG, "Nostro ETTV_NOSTRO_MT103_MT202_TBL value query " + query);
//                    }
      //
//                    ps = con.prepareStatement(query);
//                    rs = ps.executeQuery();
//                    if (rs.next()) {
      //
//                      Credit_AmountMT103202 = rs.getString(1);
//                      Credit_curMT103202 = rs.getString(2);
      //
//                      // setNOSTAMT(Credit_AmountMT103202 + " " + Credit_curMT103202);
//                      setNOSTOUT(Credit_AmountMT103202 + " " + Credit_curMT103202);
//                      ValuedateMT103202 = rs.getString(3);
//                      setNOSTDAT(ValuedateMT103202);
//                      Credit_AmountMT940950 = rs.getString(4);
//                      Credit_curMT940950 = rs.getString(5);
      //
//                      // setPOOLAMT(Credit_AmountMT940950 + " " + Credit_curMT940950);
//                      CreditAccountMT940950 = rs.getString(6);
      //
//                      setNOSTACC(CreditAccountMT940950);
      //
//                      Nostro_key = rs.getString(11);
//                      Loggers.general().info(LOG, "Nostro Key-=====================-->" + Nostro_key);
//                      setNOSTROKE(Nostro_key);
      //
//                      // ========todo on mar 9
//                      BigDecimal totalVal1 = new BigDecimal(Credit_AmountMT103202);
      //
//                      ConnectionMaster connectionMaster1 = new ConnectionMaster();
//                      double divideByDecimal1 = connectionMaster1.getDecimalforCurrency(Credit_curMT103202);
//                      BigDecimal divideByBig1 = new BigDecimal(divideByDecimal1);
//                      BigDecimal totalValue1 = totalVal1.divide(divideByBig1);
      //
//                      // BigDecimal totalValue1 = new BigDecimal(Credit_AmountMT103202);
      //
//                      BigDecimal totalVal2 = new BigDecimal(Credit_AmountMT940950);
      //
//                      // ConnectionMaster connectionMaster1 = new ConnectionMaster();
//                      double divideByDecimal2 = connectionMaster1.getDecimalforCurrency(Credit_curMT940950);
//                      BigDecimal divideByBig2 = new BigDecimal(divideByDecimal2);
//                      BigDecimal totalValue2 = totalVal2.divide(divideByBig2);
      //
//                      // =======================
      //
//                      // =====================
      //
//                      if (dailyval_Log.equalsIgnoreCase("YES")) {
//                          Loggers.general().info(LOG,
//                                "Total Nostro Credit amount-=====================-->" + totalValue1);
//                          Loggers.general().info(LOG, "poollll amount-=====================-->" + totalValue2);
      //
//                      }
//                      String finalVal1 = String.format("%.2f", totalValue1);
//                      String finalVal2 = String.format("%.2f", totalValue2);
//                      if (dailyval_Log.equalsIgnoreCase("YES")) {
//                          Loggers.general().info(LOG, "Final Nostro Credit amount===========--->" + finalVal1);
//                          Loggers.general().info(LOG, "Final poolll amount===========--->" + finalVal2);
      //
//                      }
//                      setNOSTAMT(finalVal1 + " " + Credit_curMT103202);
      //
//                      setPOOLAMT(finalVal2 + " " + Credit_curMT940950);
      //
//                      // ================================
//                      /*
//                       * msg940950_1 = rs.getString(7); msg940950_2 = rs.getString(8);
//                       *
//                       * msg940950_concat = msg940950_1 + " \n " + msg940950_2;
//                       * setMTMESG(msg940950_concat);
//                       */
//                      swift_type = rs.getString(9);
//                      String nostref_MT940 = rs.getString(10);
//                      setNOSTRM(nostref_MT940);
      //
//                      // String dms = "SELECT NVL(SUM(NOSUTLAMT),0) AS
//                      // OUTSAMOUNT FROM (SELECT SUM(ext.NOSAMT) AS NOSUTLAMT,
//                      // mas.MASTER_REF FROM master mas, BASEEVENT bas,
//                      // extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND
//                      // bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND
//                      // (mas.MASTER_REF ='"
//                      // + masReference + "' AND trim(bas.REFNO_PFIX
//                      // ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
//                      // + "') AND ext.NOSTMT ='" + nostref_MT103102
//                      // + "' GROUP BY mas.MASTER_REF UNION ALL SELECT
//                      // SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM
//                      // master mas, BASEEVENT bas, extevent ext WHERE
//                      // mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT
//                      // AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
//                      // + masReference + "' AND trim(bas.REFNO_PFIX
//                      // ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
//                      // + "') AND ext.NOSTMT ='" + nostref_MT103102
//                      // + "' GROUP BY mas.MASTER_REF ) NOSRTOAMOUNT";
      //
//                      String dms = "SELECT NVL(SUM(NOSUTLAMT),0) AS OUTSAMOUNT FROM (SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF ='"
//                            + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
//                            + "') AND ext.NOSTMT ='" + nostref_MT103102
//                            + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
//                            + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
//                            + "') AND ext.NOSTMT ='" + nostref_MT103102
//                            + "' GROUP BY mas.MASTER_REF  UNION ALL SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
//                            + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) ='" + eventCode
//                            + "') AND ext.NOSTMT ='" + nostref_MT103102
//                            + "' GROUP BY mas.MASTER_REF  ) NOSRTOAMOUNT";
      //
//                      if (dailyval_Log.equalsIgnoreCase("YES")) {
//                          Loggers.general().info(LOG, "Nostro outsanding amount--->" + dms);
      //
//                      }
//                      ps = con.prepareStatement(dms);
//                      rs = ps.executeQuery();
//                      while (rs.next()) {
//                          String nostOut = rs.getString(1).trim();
//                          BigDecimal nostOutBig = new BigDecimal(nostOut);
//                          if (dailyval_Log.equalsIgnoreCase("YES")) {
//                            Loggers.general().info(LOG, "Result of Nostro outsanding amount--->" + nostOutBig);
      //
//                          }
//                          BigDecimal CreditBig = new BigDecimal(Credit_AmountMT940950);
//                          if (dailyval_Log.equalsIgnoreCase("YES")) {
//                            Loggers.general().info(LOG, "Nostro Credit amount--->" + CreditBig);
      //
//                          }
//                          BigDecimal totalVal = CreditBig.subtract(nostOutBig);
      //
//                          ConnectionMaster connectionMaster = new ConnectionMaster();
//                          double divideByDecimal = connectionMaster.getDecimalforCurrency(Credit_curMT940950);
//                          BigDecimal divideByBig = new BigDecimal(divideByDecimal);
//                          BigDecimal totalValue = totalVal.divide(divideByBig);
      //
//                          if (dailyval_Log.equalsIgnoreCase("YES")) {
//                            Loggers.general().info(LOG, "Total Nostro Credit amount--->" + totalValue);
      //
//                          }
//                          String finalVal = String.format("%.2f", totalValue);
//                          if (dailyval_Log.equalsIgnoreCase("YES")) {
//                            Loggers.general().info(LOG, "Final Nostro Credit amount--->" + finalVal);
      //
//                          }
//                          if (totalValue.compareTo(BigDecimal.ZERO) >= 0) {
//                            setNOSTOUT(finalVal + " " + Credit_curMT940950);
//                          } else {
//                            finalVal = "0";
//                            setNOSTOUT(finalVal + " " + Credit_curMT940950);
      //
//                          }
      //
//                      }
//                      String query_940 = "SELECT TRIM( 'MessageDetails:'||SUBSTR(MESSAGE_DATA,1, INSTR(MESSAGE_DATA,':61:')-1 ) )AS MSGDATA,TRIM('TAG 61:'||IND_TEXT) AS IND_TEXT,TRIM('TAG 86:'||ACC_OWNER_INFO) AS ACC_OWNER_INFO FROM ETT_NOSTRO_UTILITY_MT940 where IND_NOS_CUS_REFNO='"
//                            + nostref_MT940 + "' or IND_NOS_BANK_REFNO='" + nostref_MT940 + "'";
//                      //// Loggers.general().info(LOG,"Nostro swift_type is
//                      //// query_103
//                      //// " + query_103);
//                      ps3 = con.prepareStatement(query_940);
//                      rs3 = ps3.executeQuery();
//                      while (rs3.next()) {
//                          String msg1 = rs3.getString(1);
//                          String msg2 = rs3.getString(2);
//                          String msg3 = rs3.getString(3);
//                          String fullmsg = msg1 + " \n " + msg2 + " \n " + msg3;
//                          setMTMESG(fullmsg);
//                      }
//                      String query_950 = "SELECT TRIM( 'MessageDetails:'||SUBSTR(MESSAGE_DATA,1, INSTR(MESSAGE_DATA,':61:')-1 ) )AS MSGDATA,TRIM('TAG 61:'||IND_TEXT) AS IND_TEXT FROM ETT_NOSTRO_UTILITY_MT950 where IND_NOS_CUS_REFNO='"
//                            + nostref_MT940 + "' or IND_NOS_BANK_REFNO='" + nostref_MT940 + "'";
//                      //// Loggers.general().info(LOG,"Nostro swift_type is
//                      //// query_103
//                      //// " + query_103);
//                      ps4 = con.prepareStatement(query_950);
//                      rs4 = ps4.executeQuery();
//                      while (rs4.next()) {
//                          String msg1 = rs4.getString(1);
//                          String msg2 = rs4.getString(2);
      //
//                          String fullmsg = msg1 + " \n " + msg2;
//                          setMTMESG(fullmsg);
//                      }
      //
//                      if (swift_type.equalsIgnoreCase("103")) {
//                          String query_103 = "SELECT MESSAGE_DATA FROM ETT_NOSTRO_UTILITY_MT103 WHERE REFERENCE_NUMBER ='"
//                                + nostref_MT103102 + "'";
//                          //// Loggers.general().info(LOG,"Nostro swift_type is
//                          //// query_103
//                          //// " + query_103);
//                          ps = con.prepareStatement(query_103);
//                          rs = ps.executeQuery();
//                          while (rs.next()) {
//                            String swift = rs.getString(1);
//                            setINWMSG(swift);
//                          }
//                      } else if (swift_type.equalsIgnoreCase("202")) {
//                          String query_202 = "SELECT MESSAGE_DATA FROM ETT_NOSTRO_UTILITY_MT202 WHERE REFERENCE_NUMBER ='"
//                                + nostref_MT103102 + "'";
//                          //// Loggers.general().info(LOG,"Nostro swift_type is
//                          //// query_202
//                          //// " + query_202);
      //
//                          ps = con.prepareStatement(query_202);
//                          rs = ps.executeQuery();
//                          while (rs.next()) {
//                            String swift = rs.getString(1);
//                            setINWMSG(swift);
//                          }
//                      } else {
//                          if (dailyval_Log.equalsIgnoreCase("YES")) {
//                            Loggers.general().info(LOG, "Nostro swift_type is empty " + swift_type);
//                          }
//                          setINWMSG("");
      //
//                      }
      //
//                    } else {
      //
//                      setPOOLAMT("");
//                      setNOSTACC("");
//                      setMTMESG("");
      //
//                      /*
//                       * String queryVal =
//                       * "select M.AMOUNT,M.CURRENCY_CODE, trim(TO_CHAR(M.VALUE_DATE,'DD/MM/YY')),M.MESSAGE_DATA from ETT_NOSTRO_UTILITY_MT103 M where trim(M.REFERENCE_NUMBER)='"
//                       * + nostref_MT103102 + "'";
//                       */
//                      String queryVal = "select M.AMOUNT,M.CURRENCY_CODE, trim(TO_CHAR(M.VALUE_DATE,'DD/MM/YY')),M.MESSAGE_DATA,M.MT103_SL_NO from ETT_NOSTRO_UTILITY_MT103 M where QUEUE_TYPE='CLSQ' AND RESPQ_STATUS='A' AND trim(M.REFERENCE_NUMBER)='"
//                            + nostref_MT103102 + "'";
//                      if (dailyval_Log.equalsIgnoreCase("YES")) {
//                          Loggers.general().info(LOG,
//                                "Nostro ETT_NOSTRO_UTILITY_MT103 value query else===> " + queryVal);
//                      }
//                      ps = con.prepareStatement(queryVal);
//                      rs = ps.executeQuery();
      //
//                      // while (rs.next()) { changed to add mt202
//                      if (rs.next()) {
//                          Credit_AmountMT103202 = rs.getString(1);
//                          BigDecimal Credit_Amount = new BigDecimal(Credit_AmountMT103202);
      //
//                          Credit_curMT103202 = rs.getString(2);
//                          String Credit_AmountMT103 = String.format("%.2f", Credit_Amount);
//                          if (dailyval_Log.equalsIgnoreCase("YES")) {
//                            System.out.println(
//                                  "Nostro ETT_NOSTRO_UTILITY_MT103 value==1==> " + Credit_AmountMT103202);
//                            Loggers.general().info(LOG,
//                                  "Nostro ETT_NOSTRO_UTILITY_MT103 value==2==> " + Credit_AmountMT103);
//                          }
//                          setNOSTAMT(Credit_AmountMT103 + " " + Credit_curMT103202);
//                          setNOSTOUT(Credit_AmountMT103 + " " + Credit_curMT103202);
//                          ValuedateMT103202 = rs.getString(3);
//                          setNOSTDAT(ValuedateMT103202);
      //
//                          msg940950_concat = rs.getString(4);
//                          setINWMSG(msg940950_concat);
//                          Nostro_key = rs.getString(5);
//                          Loggers.general().info(LOG, "Nostro Key-=====================-->" + Nostro_key);
//                          setNOSTROKE(Nostro_key);
      //
//                          // String dms = "SELECT NVL(SUM(NOSUTLAMT),0) AS
//                          // OUTSAMOUNT FROM (SELECT SUM(ext.NOSAMT) AS
//                          // NOSUTLAMT, mas.MASTER_REF FROM master mas,
//                          // BASEEVENT bas, extevent ext WHERE mas.KEY97
//                          // =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND
//                          // bas.STATUS IN ('i','c') AND (mas.MASTER_REF ='"
//                          // + masReference + "' AND trim(bas.REFNO_PFIX
//                          // ||LPAD(bas.REFNO_SERL,3,0)) !='"
//                          // + eventCode + "') AND ext.NOSTMT ='" +
//                          // nostref_MT103102
//                          // + "' GROUP BY mas.MASTER_REF UNION ALL SELECT
//                          // SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM
//                          // master mas, BASEEVENT bas, extevent ext WHERE
//                          // mas.KEY97 =bas.MASTER_KEY AND bas.KEY97
//                          // =ext.EVENT AND bas.STATUS IN ('i','c') AND
//                          // (mas.MASTER_REF !='"
//                          // + masReference + "' AND trim(bas.REFNO_PFIX
//                          // ||LPAD(bas.REFNO_SERL,3,0)) !='"
//                          // + eventCode + "') AND ext.NOSTMT ='" +
//                          // nostref_MT103102
//                          // + "' GROUP BY mas.MASTER_REF ) NOSRTOAMOUNT";
      //
//                          String dms = "SELECT NVL(SUM(NOSUTLAMT),0) AS OUTSAMOUNT FROM (SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF ='"
//                                + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
//                                + eventCode + "') AND ext.NOSTMT ='" + nostref_MT103102
//                                + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c')  AND (mas.MASTER_REF !='"
//                                + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
//                                + eventCode + "') AND ext.NOSTMT ='" + nostref_MT103102
//                                + "' GROUP BY mas.MASTER_REF  UNION ALL SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c')  AND (mas.MASTER_REF !='"
//                                + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) ='"
//                                + eventCode + "') AND ext.NOSTMT ='" + nostref_MT103102
//                                + "' GROUP BY mas.MASTER_REF  ) NOSRTOAMOUNT";
      //
//                          if (dailyval_Log.equalsIgnoreCase("YES")) {
//                            Loggers.general().info(LOG, "Nostro outsanding amount else--->" + dms);
      //
//                          }
//                          ps = con.prepareStatement(dms);
//                          rs = ps.executeQuery();
//                          while (rs.next()) {
//                            String nostOut = rs.getString(1).trim();
//                            BigDecimal nostOutBig = new BigDecimal(nostOut);
//                            if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                Loggers.general().info(LOG, "Result of Nostro outsanding amount--->" + nostOutBig);
      //
//                            }
//                            BigDecimal CreditBig = new BigDecimal(Credit_AmountMT103202);
//                            if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                Loggers.general().info(LOG, "Nostro Credit amount for MT103--->" + CreditBig);
      //
//                            }
//                            ConnectionMaster connectionMaster = new ConnectionMaster();
//                            double divideByDecimal = connectionMaster.getDecimalforCurrency(Credit_curMT103202);
//                            BigDecimal divideByBig = new BigDecimal(divideByDecimal);
//                            BigDecimal creditAmount = CreditBig.multiply(divideByBig);
//                            if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                Loggers.general().info(LOG,
//                                      "Nostro Credit amount for MT103 after multifly===>" + CreditBig);
      //
//                            }
//                            BigDecimal totalVal = creditAmount.subtract(nostOutBig);
      //
//                            BigDecimal totalValue = totalVal.divide(divideByBig);
      //
//                            if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                Loggers.general().info(LOG, "Total Nostro Credit amount--->" + totalValue);
      //
//                            }
//                            String finalVal = String.format("%.2f", totalValue);
//                            if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                Loggers.general().info(LOG, "Final Nostro Credit amount--->" + finalVal);
      //
//                            }
//                            if (totalValue.compareTo(BigDecimal.ZERO) >= 0) {
//                                setNOSTOUT(finalVal + " " + Credit_curMT103202);
//                            } else {
//                                if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                  Loggers.general().info(LOG,
//                                        "Final Nostro Credit amount ZERO in else--->" + finalVal);
      //
//                                }
//                                finalVal = "0";
//                                setNOSTOUT(finalVal + " " + Credit_curMT103202);
      //
//                            }
      //
//                          }
      //
//                      }
//                      // MT202 START
//                      else {
//                          setPOOLAMT("");
//                          setNOSTACC("");
//                          setMTMESG("");
      //
//                          /*
//                           * String queryVal2 =
//                           * "select M.AMOUNT,M.CURRENCY_CODE, trim(TO_CHAR(M.VALUE_DATE,'DD/MM/YY')),M.MESSAGE_DATA from ETT_NOSTRO_UTILITY_MT202 M where trim(M.REFERENCE_NUMBER)='"
//                           * + nostref_MT103102 + "'";
//                           */
//                          String queryVal2 = "select M.AMOUNT,M.CURRENCY_CODE, trim(TO_CHAR(M.VALUE_DATE,'DD/MM/YY')),M.MESSAGE_DATA from ETT_NOSTRO_UTILITY_MT202 M where QUEUE_TYPE='CLSQ' AND RESPQ_STATUS='A' AND trim(M.REFERENCE_NUMBER)='"
//                                + nostref_MT103102 + "'";
//                          if (dailyval_Log.equalsIgnoreCase("YES")) {
//                            Loggers.general().info(LOG,
//                                  "Nostro ETT_NOSTRO_UTILITY_MT202 value query else===> " + queryVal2);
//                          }
//                          ps = con.prepareStatement(queryVal2);
//                          rs = ps.executeQuery();
      //
//                          // while (rs.next()) {
//                          if (rs.next()) {
//                            Credit_AmountMT103202 = rs.getString(1);
//                            BigDecimal Credit_Amount = new BigDecimal(Credit_AmountMT103202);
      //
//                            Credit_curMT103202 = rs.getString(2);
//                            String Credit_AmountMT103 = String.format("%.2f", Credit_Amount);
//                            if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                System.out.println(
//                                      "Nostro ETT_NOSTRO_UTILITY_MT202 value==1==> " + Credit_AmountMT103202);
//                                System.out.println(
//                                      "Nostro ETT_NOSTRO_UTILITY_MT202 value==2==> " + Credit_AmountMT103);
//                            }
//                            setNOSTAMT(Credit_AmountMT103 + " " + Credit_curMT103202);
//                            setNOSTOUT(Credit_AmountMT103 + " " + Credit_curMT103202);
//                            ValuedateMT103202 = rs.getString(3);
//                            setNOSTDAT(ValuedateMT103202);
      //
//                            msg940950_concat = rs.getString(4);
//                            setINWMSG(msg940950_concat);
      //
//                            // String dms = "SELECT NVL(SUM(NOSUTLAMT),0) AS
//                            // OUTSAMOUNT FROM (SELECT SUM(ext.NOSAMT) AS
//                            // NOSUTLAMT, mas.MASTER_REF FROM master mas,
//                            // BASEEVENT bas, extevent ext WHERE mas.KEY97
//                            // =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND
//                            // bas.STATUS IN ('i','c') AND (mas.MASTER_REF
//                            // ='"
//                            // + masReference + "' AND trim(bas.REFNO_PFIX
//                            // ||LPAD(bas.REFNO_SERL,3,0)) !='"
//                            // + eventCode + "') AND ext.NOSTMT ='" +
//                            // nostref_MT103102
//                            // + "' GROUP BY mas.MASTER_REF UNION ALL SELECT
//                            // SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF
//                            // FROM
//                            // master mas, BASEEVENT bas, extevent ext WHERE
//                            // mas.KEY97 =bas.MASTER_KEY AND bas.KEY97
//                            // =ext.EVENT AND bas.STATUS IN ('i','c') AND
//                            // (mas.MASTER_REF !='"
//                            // + masReference + "' AND trim(bas.REFNO_PFIX
//                            // ||LPAD(bas.REFNO_SERL,3,0)) !='"
//                            // + eventCode + "') AND ext.NOSTMT ='" +
//                            // nostref_MT103102
//                            // + "' GROUP BY mas.MASTER_REF ) NOSRTOAMOUNT";
      //
//                            String dms = "SELECT NVL(SUM(NOSUTLAMT),0) AS OUTSAMOUNT FROM (SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF ='"
//                                  + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
//                                  + eventCode + "') AND ext.NOSTMT ='" + nostref_MT103102
//                                  + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
//                                  + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
//                                  + eventCode + "') AND ext.NOSTMT ='" + nostref_MT103102
//                                  + "' GROUP BY mas.MASTER_REF  UNION ALL SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
//                                  + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) ='"
//                                  + eventCode + "') AND ext.NOSTMT ='" + nostref_MT103102
//                                  + "' GROUP BY mas.MASTER_REF  ) NOSRTOAMOUNT";
      //
//                            if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                Loggers.general().info(LOG, "Nostro outsanding amount else--->" + dms);
      //
//                            }
//                            ps = con.prepareStatement(dms);
//                            rs = ps.executeQuery();
//                            while (rs.next()) {
//                                String nostOut = rs.getString(1).trim();
//                                BigDecimal nostOutBig = new BigDecimal(nostOut);
//                                if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                  Loggers.general().info(LOG,
//                                        "Result of Nostro outsanding amount--->" + nostOutBig);
      //
//                                }
//                                BigDecimal CreditBig = new BigDecimal(Credit_AmountMT103202);
//                                if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                  Loggers.general().info(LOG, "Nostro Credit amount for MT202--->" + CreditBig);
      //
//                                }
//                                ConnectionMaster connectionMaster = new ConnectionMaster();
//                                double divideByDecimal = connectionMaster.getDecimalforCurrency(Credit_curMT103202);
//                                BigDecimal divideByBig = new BigDecimal(divideByDecimal);
//                                BigDecimal creditAmount = CreditBig.multiply(divideByBig);
//                                if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                  System.out.println(
//                                        "Nostro Credit amount for MT202 after multifly===>" + CreditBig);
      //
//                                }
//                                BigDecimal totalVal = creditAmount.subtract(nostOutBig);
      //
//                                BigDecimal totalValue = totalVal.divide(divideByBig);
      //
//                                if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                  Loggers.general().info(LOG, "Total Nostro Credit amount--->" + totalValue);
      //
//                                }
//                                String finalVal = String.format("%.2f", totalValue);
//                                if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                  Loggers.general().info(LOG, "Final Nostro Credit amount--->" + finalVal);
      //
//                                }
//                                if (totalValue.compareTo(BigDecimal.ZERO) >= 0) {
//                                  setNOSTOUT(finalVal + " " + Credit_curMT103202);
//                                } else {
//                                  if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                      System.out
//                                            .println("Final Nostro Credit amount ZERO in else--->" + finalVal);
      //
//                                  }
//                                  finalVal = "0";
//                                  setNOSTOUT(finalVal + " " + Credit_curMT103202);
      //
//                                }
      //
//                            }
      //
//                          }
//                      }
//                      // MT202 END
      //
//                    }
      //
//                } catch (Exception ee) {
//                    Loggers.general().info(LOG, "Exception Inward reference 103" + ee.getMessage());
//                }
//              } else if (nostref_MT940950.length() > 0) {
      //
//                //// Loggers.general().info(LOG,"the Nostro MT103/202 reference number
//                //// is
//                //// empty");
      //
//                try {
      //
//                    String query = "SELECT MT103_MT202_AMOUNT AS CreditMT103202, MT103_MT202_CURRENCY_CODE AS CreditCurMT103202, TO_CHAR(MT103_MT202_VALUE_DATE,'DD/MM/YY') AS ValuedateMT103202, MT940_MT950_AMOUNT AS CreditAmountMT940950, MT940_MT950_CURRENCY_CODE AS CreditCurMT940950, MT940_MT950_ACCOUNT_NO AS CreditAccountMT940950, MT940_MT950_IND_TEXT, MT940_MT950_ACC_OWNER_INFO,Trim(SWIFT_TYPE),MT103_MT202_REFERENCE_NUMBER,NOSTRO_KEY FROM ETTV_NOSTRO_MT103_MT202_TBL WHERE MT940_MT950_REFERENCE_NUMBER='"
//                          + nostref_MT940950 + "'";
//                    if (dailyval_Log.equalsIgnoreCase("YES")) {
//                      Loggers.general().info(LOG, "Nostro MT940950 value query " + query);
//                    }
//                    ps = con.prepareStatement(query);
//                    rs = ps.executeQuery();
//                    while (rs.next()) {
      //
//                      Credit_AmountMT103202 = rs.getString(1);
//                      Credit_curMT103202 = rs.getString(2);
//                      //// Loggers.general().info(LOG,"Nostro MT103202 credit amount
//                      //// and
//                      //// currency" + Credit_AmountMT103202 + " " +
//                      //// Credit_curMT103202);
//                      // setNOSTAMT(Credit_AmountMT103202 + " " + Credit_curMT103202);
      //
//                      ValuedateMT103202 = rs.getString(3);
//                      setNOSTDAT(ValuedateMT103202);
//                      Credit_AmountMT940950 = rs.getString(4);
//                      Credit_curMT940950 = rs.getString(5);
//                      //// Loggers.general().info(LOG,"Nostro MT940950 credit amount
//                      //// and
//                      //// currency" + Credit_AmountMT940950 + " " +
//                      //// Credit_curMT940950);
//                      // setPOOLAMT(Credit_AmountMT940950 + " " + Credit_curMT940950);
//                      setNOSTOUT(Credit_AmountMT940950 + " " + Credit_curMT940950);
//                      CreditAccountMT940950 = rs.getString(6);
      //
//                      //// Loggers.general().info(LOG,"setNOSTACC------------>" +
//                      //// getNOSTACC());
//                      setNOSTACC(CreditAccountMT940950);
//                      /*
//                       * msg940950_1 = rs.getString(7); msg940950_2 = rs.getString(8);
//                       *
//                       * msg940950_concat = msg940950_1 + " \n " + msg940950_2;
//                       * setMTMESG(msg940950_concat);
//                       */
//                      swift_type = rs.getString(9);
//                      String nostref_MT103 = rs.getString(10);
//                      setNOSTMT(nostref_MT103);
//                      Nostro_key = rs.getString(11);
//                      Loggers.general().info(LOG, "Nostro Key-=====================-->" + Nostro_key);
//                      setNOSTROKE(Nostro_key);
      ////=====================================================
//                      BigDecimal totalVal1 = new BigDecimal(Credit_AmountMT103202);
      //
//                      ConnectionMaster connectionMaster1 = new ConnectionMaster();
//                      double divideByDecimal1 = connectionMaster1.getDecimalforCurrency(Credit_curMT103202);
//                      BigDecimal divideByBig1 = new BigDecimal(divideByDecimal1);
//                      BigDecimal totalValue1 = totalVal1.divide(divideByBig1);
      //
//                      // BigDecimal totalValue1 = new BigDecimal(Credit_AmountMT103202);
      //
//                      BigDecimal totalVal2 = new BigDecimal(Credit_AmountMT940950);
      //
//                      // ConnectionMaster connectionMaster1 = new ConnectionMaster();
//                      double divideByDecimal2 = connectionMaster1.getDecimalforCurrency(Credit_curMT940950);
//                      BigDecimal divideByBig2 = new BigDecimal(divideByDecimal2);
//                      BigDecimal totalValue2 = totalVal2.divide(divideByBig2);
      //
//                      // =======================
      //
//                      // =====================
      //
//                      if (dailyval_Log.equalsIgnoreCase("YES")) {
//                          Loggers.general().info(LOG,
//                                "Total Nostro Credit amount-940=====================-->" + totalValue1);
//                          Loggers.general().info(LOG, "poollll amount-====940=================-->" + totalValue2);
      //
//                      }
//                      String finalVal1 = String.format("%.2f", totalValue1);
//                      String finalVal2 = String.format("%.2f", totalValue2);
//                      if (dailyval_Log.equalsIgnoreCase("YES")) {
//                          Loggers.general().info(LOG, "Final Nostro Credit amount==940=========--->" + finalVal1);
//                          Loggers.general().info(LOG, "Final poolll amount===940========--->" + finalVal2);
      //
//                      }
//                      setNOSTAMT(finalVal1 + " " + Credit_curMT103202);
      //
//                      setPOOLAMT(finalVal2 + " " + Credit_curMT940950);
      //
//                      // ===============================
//                      // String dms = "SELECT NVL(SUM(NOSUTLAMT),0) AS
//                      // OUTSAMOUNT FROM (SELECT SUM(ext.NOSAMT) AS NOSUTLAMT,
//                      // mas.MASTER_REF FROM master mas, BASEEVENT bas,
//                      // extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND
//                      // bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND
//                      // (mas.MASTER_REF ='"
//                      // + masReference + "' AND trim(bas.REFNO_PFIX
//                      // ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
//                      // + "') AND ext.NOSTRM ='" + nostref_MT940950
//                      // + "' GROUP BY mas.MASTER_REF UNION ALL SELECT
//                      // SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM
//                      // master mas, BASEEVENT bas, extevent ext WHERE
//                      // mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT
//                      // AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
//                      // + masReference + "' AND trim(bas.REFNO_PFIX
//                      // ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
//                      // + "') AND ext.NOSTRM ='" + nostref_MT940950
//                      // + "' GROUP BY mas.MASTER_REF ) NOSRTOAMOUNT";
      //
//                      String dms = "SELECT NVL(SUM(NOSUTLAMT),0) AS OUTSAMOUNT FROM (SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF ='"
//                            + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
//                            + "') AND ext.NOSTRM ='" + nostref_MT940950
//                            + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
//                            + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
//                            + "') AND ext.NOSTRM ='" + nostref_MT940950
//                            + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
//                            + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) ='" + eventCode
//                            + "') AND ext.NOSTRM ='" + nostref_MT940950
//                            + "' GROUP BY mas.MASTER_REF ) NOSRTOAMOUNT";
      //
//                      if (dailyval_Log.equalsIgnoreCase("YES")) {
//                          Loggers.general().info(LOG, "Nostro outsanding amount--->" + dms);
      //
//                      }
//                      ps1 = con.prepareStatement(dms);
//                      rs = ps1.executeQuery();
//                      while (rs.next()) {
//                          String nostOut = rs.getString(1).trim();
//                          BigDecimal nostOutBig = new BigDecimal(nostOut);
//                          if (dailyval_Log.equalsIgnoreCase("YES")) {
//                            Loggers.general().info(LOG, "Result of Nostro outsanding amount--->" + nostOutBig);
      //
//                          }
//                          BigDecimal CreditBig = new BigDecimal(Credit_AmountMT940950);
//                          if (dailyval_Log.equalsIgnoreCase("YES")) {
//                            Loggers.general().info(LOG, "Nostro Credit amount--->" + CreditBig);
      //
//                          }
//                          BigDecimal totalVal = CreditBig.subtract(nostOutBig);
      //
//                          ConnectionMaster connectionMaster = new ConnectionMaster();
//                          double divideByDecimal = connectionMaster.getDecimalforCurrency(Credit_curMT940950);
//                          BigDecimal divideByBig = new BigDecimal(divideByDecimal);
//                          BigDecimal totalValue = totalVal.divide(divideByBig);
      //
//                          if (dailyval_Log.equalsIgnoreCase("YES")) {
//                            Loggers.general().info(LOG, "Total Nostro Credit amount--->" + totalValue);
      //
//                          }
//                          String finalVal = String.format("%.2f", totalValue);
//                          if (dailyval_Log.equalsIgnoreCase("YES")) {
//                            Loggers.general().info(LOG, "Final Nostro Credit amount--->" + finalVal);
      //
//                          }
//                          if (totalValue.compareTo(BigDecimal.ZERO) >= 0) {
//                            setNOSTOUT(finalVal + " " + Credit_curMT940950);
//                          } else {
//                            finalVal = "0";
//                            setNOSTOUT(finalVal + " " + Credit_curMT940950);
      //
//                          }
      //
//                      }
      //
//                      String query_940 = "SELECT TRIM( 'MessageDetails:'||SUBSTR(MESSAGE_DATA,1, INSTR(MESSAGE_DATA,':61:')-1 ) )AS MSGDATA,TRIM('TAG 61:'||IND_TEXT) AS IND_TEXT,TRIM('TAG 86:'||ACC_OWNER_INFO) AS ACC_OWNER_INFO FROM ETT_NOSTRO_UTILITY_MT940 where IND_NOS_CUS_REFNO='"
//                            + nostref_MT940950 + "' or IND_NOS_BANK_REFNO='" + nostref_MT940950 + "'";
//                      //// Loggers.general().info(LOG,"Nostro swift_type is
//                      //// query_103
//                      //// " + query_103);
//                      ps3 = con.prepareStatement(query_940);
//                      rs3 = ps3.executeQuery();
//                      while (rs3.next()) {
//                          String msg1 = rs3.getString(1);
//                          String msg2 = rs3.getString(2);
//                          String msg3 = rs3.getString(3);
//                          String fullmsg = msg1 + " \n " + msg2 + " \n " + msg3;
//                          setMTMESG(fullmsg);
//                      }
//                      String query_950 = "SELECT TRIM( 'MessageDetails:'||SUBSTR(MESSAGE_DATA,1, INSTR(MESSAGE_DATA,':61:')-1 ) )AS MSGDATA,TRIM('TAG 61:'||IND_TEXT) AS IND_TEXT FROM ETT_NOSTRO_UTILITY_MT950 where IND_NOS_CUS_REFNO='"
//                            + nostref_MT940950 + "' or IND_NOS_BANK_REFNO='" + nostref_MT940950 + "'";
//                      //// Loggers.general().info(LOG,"Nostro swift_type is
//                      //// query_103
//                      //// " + query_103);
//                      ps4 = con.prepareStatement(query_950);
//                      rs4 = ps4.executeQuery();
//                      while (rs4.next()) {
//                          String msg1 = rs4.getString(1);
//                          String msg2 = rs4.getString(2);
      //
//                          String fullmsg = msg1 + " \n " + msg2;
//                          setMTMESG(fullmsg);
//                      }
      //
//                      if (swift_type.equalsIgnoreCase("103")) {
//                          String query_103 = "SELECT MESSAGE_DATA FROM ETT_NOSTRO_UTILITY_MT103 WHERE REFERENCE_NUMBER ='"
//                                + nostref_MT103 + "'";
//                          //// Loggers.general().info(LOG,"Nostro swift_type is
//                          //// query_103
//                          //// " + query_103);
//                          ps = con.prepareStatement(query_103);
//                          rs = ps.executeQuery();
//                          while (rs.next()) {
//                            String swift = rs.getString(1);
//                            setINWMSG(swift);
//                          }
//                      } else if (swift_type.equalsIgnoreCase("202")) {
//                          String query_202 = "SELECT MESSAGE_DATA FROM ETT_NOSTRO_UTILITY_MT202 WHERE REFERENCE_NUMBER ='"
//                                + nostref_MT103 + "'";
//                          //// Loggers.general().info(LOG,"Nostro swift_type is
//                          //// query_202
//                          //// " + query_202);
      //
//                          ps = con.prepareStatement(query_202);
//                          rs = ps.executeQuery();
//                          while (rs.next()) {
//                            String swift = rs.getString(1);
//                            setINWMSG(swift);
//                          }
//                      } else {
//                          //// Loggers.general().info(LOG,"Nostro swift_type is empty
//                          //// " +
//                          //// swift_type);
      //
//                          setINWMSG("");
      //
//                      }
      //
//                    }
      //
//                } catch (Exception ee) {
//                    Loggers.general().info(LOG, "Exception Inward reference 940" + ee.getMessage());
//                }
      //
//              } else {
//                setNOSTAMT("");
//                setNOSTDAT("");
//                setPOOLAMT("");
//                setNOSTACC("");
//                setMTMESG("");
//                setINWMSG("");
//                setNOSTOUT("");
      //
//              }
      //
//              try {
      //
//                String query = "";
//                if (dailyval_Log.equalsIgnoreCase("YES")) {
//                    Loggers.general().info(LOG, "Nostro ref no nostref_MT103102" + nostref_MT103102);
//                }
//                // String nostref_MT940950 = getWrapper().getNOSTRM().trim();
      //
//                if (nostref_MT103102.length() > 0) {
      //
//                    query = "SELECT count(*) as COUNT FROM ETTV_NOSTRO_MT103_MT202_TBL WHERE MT103_MT202_REFERENCE_NUMBER='"
//                          + nostref_MT103102 + "'";
//                    if (dailyval_Log.equalsIgnoreCase("YES")) {
//                      Loggers.general().info(LOG, "Nostro ref no ETTV_NOSTRO_MT103_MT202_TBL query" + query);
//                    }
//                    con = ConnectionMaster.getConnection();
//                    ps = con.prepareStatement(query);
//                    rs = ps.executeQuery();
//                    int val = 0;
//                    if (rs.next()) {
//                      val = rs.getInt(1);
      //
//                    }
      //
//                    query = "select count(*) from ETT_NOSTRO_UTILITY_MT103 M where M.QUEUE_TYPE='CLSQ' and M.REFERENCE_NUMBER='"
//                          + nostref_MT103102 + "'";
//                    if (dailyval_Log.equalsIgnoreCase("YES")) {
//                      Loggers.general().info(LOG, "Nostro ref no ETT_NOSTRO_UTILITY_MT103 query" + query);
//                    }
//                    con = ConnectionMaster.getConnection();
//                    ps = con.prepareStatement(query);
//                    rs = ps.executeQuery();
//                    int value103 = 0;
//                    if (rs.next()) {
//                      value103 = rs.getInt(1);
      //
//                    }
//                    if (dailyval_Log.equalsIgnoreCase("YES")) {
//                      Loggers.general().info(LOG,
//                            "Nostro ref no nostref_MT103102 count" + val + "value103" + value103);
//                    }
//                    // MT202 start
//                    query = "select count(*) from ETT_NOSTRO_UTILITY_MT202 M where M.QUEUE_TYPE='CLSQ' and M.REFERENCE_NUMBER='"
//                          + nostref_MT103102 + "'";
//                    if (dailyval_Log.equalsIgnoreCase("YES")) {
//                      Loggers.general().info(LOG, "Nostro ref no ETT_NOSTRO_UTILITY_MT202 query" + query);
//                    }
//                    con = ConnectionMaster.getConnection();
//                    ps = con.prepareStatement(query);
//                    rs = ps.executeQuery();
//                    int value202 = 0;
//                    if (rs.next()) {
//                      value202 = rs.getInt(1);
      //
//                    }
      //
//                    if (dailyval_Log.equalsIgnoreCase("YES")) {
//                      Loggers.general().info(LOG,
//                            "Nostro ref no nostref_MT103102 count" + val + "value202" + value202);
//                    }
//                    // MT202 end
//                    if ((nostref_MT103102.length() > 0)
//                          && ((val == 0 && value103 == 0) && (val == 0 && value202 == 0))) {
//                      Loggers.general().info(LOG, "MT202/103 EMPTY");
//                      setNOSTAMT("");
//                      setNOSTDAT("");
//                      setPOOLAMT("");
//                      setNOSTACC("");
//                      setMTMESG("");
//                      setINWMSG("");
//                      setNOSTOUT("");
//                    } else {
//                      if (dailyval_Log.equalsIgnoreCase("YES")) {
//                          Loggers.general().info(LOG, "MT103 Nostro Reference else" + val);
//                      }
//                    }
      //
//                } else if (nostref_MT940950.length() > 0) {
//                    query = "SELECT count(*) FROM ETTV_NOSTRO_MT103_MT202_TBL WHERE MT940_MT950_REFERENCE_NUMBER='"
//                          + nostref_MT940950 + "'";
//                    if (dailyval_Log.equalsIgnoreCase("YES")) {
//                      Loggers.general().info(LOG, "Nostro ref no nostref_MT940 query" + query);
//                    }
//                    ps = con.prepareStatement(query);
//                    rs = ps.executeQuery();
//                    int val = 0;
//                    while (rs.next()) {
//                      val = rs.getInt(1);
//                    }
//                    if (dailyval_Log.equalsIgnoreCase("YES")) {
//                      Loggers.general().info(LOG, "Nostro ref no nostref_MT940 count" + val);
//                    }
//                    if ((nostref_MT940950.length() > 0) && val == 0) {
//                      Loggers.general().info(LOG, "MT940 EMPTY");
//                      setNOSTAMT("");
//                      setNOSTDAT("");
//                      setPOOLAMT("");
//                      setNOSTACC("");
//                      setMTMESG("");
//                      setINWMSG("");
//                      setNOSTOUT("");
      //
//                    } else {
//                      if (dailyval_Log.equalsIgnoreCase("YES")) {
//                          Loggers.general().info(LOG, "MT940 Nostro Reference else" + val);
//                      }
//                    }
//                }
      //
//              } catch (Exception e1) {
//                if (dailyval_Log.equalsIgnoreCase("YES")) {
//                    Loggers.general().info(LOG, "Exception Nostro ref no validation" + e1.getMessage());
//                }
//              }
      //
                  String forwardUid = getFDTRSUID().trim();
                  String nostro_Reference_no1="";
                  String nostro_Reference_no2="";
                  String nostro_value_date="";
                  con = ConnectionMaster.getConnection();
                  String Credit_Amount = "";
                  String Credit_currency = "";
                  String CreditAccount = "";
                  String available_Amount="";
                  
                  String query = "SELECT AMOUNT AS Credit_Amount, CCY AS Credit_currency, TO_CHAR(VALUE_DATE,'DD/MM/YY') AS nostro_value_date,  NOSTRO AS CreditAccount,INST_REF_NUM,OWNER_REF_NUM FROM UID_CREDIT_DAILY_TBL WHERE UNIQUE_ID='"
                              + forwardUid + "'";
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        System.out.println("Nostro ETTV_NOSTRO_MT103_MT202_TBL value query " + query);
                  }

                  ps = con.prepareStatement(query);
                  rs = ps.executeQuery();
                  if (rs.next()) {
            //          setNOSTOUT(Credit_AmountMT103202 + " " + Credit_curMT103202);
                        nostro_value_date = rs.getString(3);
                        
                        Credit_Amount = rs.getString(1);
                        Credit_currency = rs.getString(2);
                        nostro_Reference_no1=rs.getString(6);
                        nostro_Reference_no2=rs.getString(5);
                        CreditAccount=rs.getString(4);
                        System.out.println("Nostro values  details===>" +Credit_Amount+" "+nostro_Reference_no2+" " +CreditAccount);
                         setNOSTMT(nostro_Reference_no1);
                         setNOSTRM(nostro_Reference_no2);
                         setNOSTDAT(nostro_value_date);
                         setPOOLAMT(Credit_Amount+" "+Credit_currency);
                         setNOSTACC(CreditAccount);
                  }
//                String amountquery = "SELECT UID_AVAILABLE_AMOUNT||' '||ccy as availableAmount from REP_UID_AVAILABLE_AMOUNT where UNIQUE_ID='"+ forwardUid + "'";
//                ps1 = con.prepareStatement(amountquery);
//                rs1 = ps1.executeQuery();
//                if (rs1.next()) {
//                      available_Amount=rs1.getString(1);
//                      System.out.println("aVAILABLE AMOUNT "+available_Amount);
//                      setNOSTAMT(available_Amount);
//                }
            } catch (Exception e) {
                  e.printStackTrace();
            }

            finally {
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

      public void ontotalcalcELCDPclayButton() {
            // Loggers.general().debug(LOG, "on{}Button:{}", "totalcalcELCDOCL",
            // ValidationTexts.METHOD_NOT_IMPLEMENTED);
            try {
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
                  //// Loggers.general().info(LOG,"enter into INVOICE CALCULTAE");
                  double totINVcamt = 0.00;
                  double testdouble = 0.00;
                  String billCurr = String.valueOf(getDriverWrapper().getEventFieldAsText("AMPR", "v", "c"));
                  EnigmaArray<ExtEventInvoiceTableEntityWrapper> liste = getExtEventInvoiceTableData();
                  if (liste.getSize() > 0) {
                        Iterator<ExtEventInvoiceTableEntityWrapper> iterator1 = liste.iterator();
                        while (iterator1.hasNext()) {
                              ExtEventInvoiceTableEntityWrapper ExtEventInvoiceTableEntityWrapper1 = (ExtEventInvoiceTableEntityWrapper) iterator1
                                          .next();
                              String testData = ExtEventInvoiceTableEntityWrapper1.getINVOIAMAmount();
                              //// Loggers.general().info(LOG,"INVOICE Amount ------->" +
                              //// testData);
                              testdouble = Double.parseDouble(testData);
                              totINVcamt = totINVcamt + testdouble;
                              // setTRAMT(String.valueOf(totINVcamt/100));
                              // totINVcamt=totINVcamt+testdouble;
                              StringBuilder str = new StringBuilder(String.format("%.2f", (totINVcamt / 100)));
                              //// Loggers.general().info(LOG,"value of amount + currency" +
                              //// str.append(billCurr).toString());
                              setTRAMT(str.toString());
                              // setTRAMT(str.append(billCurr).toString());
                              // ////Loggers.general().info(LOG,"totIfircamt in loop " +
                              // totINVcamt);
                        }
                  }

            } catch (Exception e) {
                  //// Loggers.general().info(LOG,"Exeception" + e.getMessage());
            }

      }

      public void onFetchShipdetELCDPclayButton() {
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
                  EnigmaArray<ExtEventShippingTableEntityWrapper> liste = getExtEventShippingTableData();
                  EnigmaArray<ExtEventShippingdetailslcEntityWrapper> listef = getExtEventShippingdetailslcData();
                  Iterator<ExtEventShippingTableEntityWrapper> iterator = liste.iterator();
                  Iterator<ExtEventShippingdetailslcEntityWrapper> iterator1 = listef.iterator();
                  Loggers.general().info(LOG,"Iterator before==>");
                  for(ExtEventShippingdetailslcEntityWrapper extship:listef)
                  {
                        Loggers.general().info(LOG,"Entered into for all");
                        removeExtEventShippingdetailslc(extship);
                  }
                  

                  String iecode = getDriverWrapper().getEventFieldAsText("BEN", "p", "cBBF").trim();
                  String query = "";
                  int count = 0;
                  int j=0;
                  Loggers.general().info(LOG,"size==>"+liste.getSize().intValue());

                  Connection connection = null;
                  ResultSet rst = null;
                  PreparedStatement ps = null;
                  for (int i = 0; i < liste.getSize().intValue(); i++) {
                        if (connection == null) {
                              connection = ConnectionMaster.getConnection();
                        }
                        //Iterator<ExtEventShippingTableEntityWrapper> iterator = liste.iterator();
                        //Iterator<ExtEventShippingdetailslcEntityWrapper> iterator1 = listef.iterator();
                        Loggers.general().info(LOG,"entered in for");

                        while (iterator.hasNext()) {
                              
                              ExtEventShippingTableEntityWrapper sdwrapper = (ExtEventShippingTableEntityWrapper) iterator.next();
                              ExtEventShippingdetailslcEntityWrapper sdwrapper1 = (ExtEventShippingdetailslcEntityWrapper) iterator1
                                          .next();
                              //// Loggers.general().info(LOG,"bill number" +
                              //// sdwrapper.getBILLNUM());
                               String billnum = "";
                                  String formNO = "";
                                  String portcode = "";
                                  String shipamt = "";

                                  String add_lie = "";

                                  try {
                                    billnum = sdwrapper.getBILLNUM().trim();

                                    System.out.println("billnum num and other details" + billnum);
                                  } catch (Exception e) {
                                    System.out.println("Exception billnum num and other details" + e.getMessage());

                                  }

                                  try {
                                    formNO = sdwrapper.getFORMNUM().trim();
                                    System.out.println("Form num and other details" + formNO);
                                  } catch (Exception e) {
                                    System.out.println("Exception Form num and other details" + e.getMessage());

                                  }

                                  portcode = sdwrapper.getPORTCODDD();
                                  shipamt = sdwrapper.getSHPAMT();
                                  if (!shipamt.equalsIgnoreCase("")) {
                                    add_lie = shipamt.replaceAll("[^0-9]", "");
                                  }
                                  String shidate = sdwrapper.getBILLDAT();
                                  System.out.println("bill num and other details" + billnum + " " + portcode + " " + shipamt);
                                  System.out.println("Shipping bill num" + billnum);
                                  System.out.println("Shipping form num" + formNO);


                              sdwrapper.setIECOD(iecode);
                              if (add_lie.length() > 0) {
                                    String shipcur = sdwrapper.getSHPAMTCurrency();
                                    BigDecimal priceDecimal = new BigDecimal(add_lie);

                                    String ship_final = String.valueOf(priceDecimal);

                                    sdwrapper.setEQUBILL(ship_final + "" + shipcur);
                                    //// Loggers.general().info(LOG,"Shipping bill amount for set
                                    //// outstanding amount" + ship_final);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          System.out.println("Shipping bill amount for set outstanding amount" + ship_final);
                                    }
                                    sdwrapper.setLOUTSAMT(ship_final + "" + shipcur);
                                    sdwrapper.setSHCOLAM(0 + "" + shipcur);

                              } else {
                                    //// Loggers.general().info(LOG,"Shipping bill amount is
                                    //// empty");
                              }

                        //     formNO = sdwrapper.getFORMNUM().trim();
                              //// Loggers.general().info(LOG,"Shipping form no" + formNO);
                              System.out.println("Shipping bill num" + billnum);
                              System.out.println("Shipping form num" + formNO);
                              System.out.println("Shipping bill num LENGTH" + billnum.length());
                              System.out.println("Shipping form num LENGTH" + formNO.length());

                    j=j+1;
                              int count2 = 0;
                               if (billnum.length() > 0 && formNO.length() > 0) {
                                    /*String query2 = "SELECT DISTINCT CASE WHEN LCFORMNO IN (SELECT elm.LCFORMNO FROM EXTEVENTSLC elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.MASTER_KEY = mas.KEY97 AND elm.LCFORMNO ='"
                                                + formNO + "' and TO_CHAR(elm.lcbildat) =to_char(to_date('" + shidate+ "','DD-MM-YY')) "
                                                + " AND elm.LCPRTCDE='" + portcode
                                                + "') THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSLC elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 and elm.LCFORMNO='"
                                                + formNO + "'  and TO_CHAR(elm.lcbildat) =to_char(to_date('" + shidate+ "','DD-MM-YY')) "
                                                + " AND elm.LCPRTCDE='" + portcode + "'";*/
                                    
                                     System.out.println("billnum and formNO entered");

                                     String query2 ="SELECT CASE WHEN COUNT(*)<>0 THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSLC elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 "
                                         + " AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 AND trim(elm.lcBILLNO)    ='"+ billnum +"'"
                                         + " and trim(elm.LCFORMNO)='"+ formNO +"'  and TO_CHAR(elm.lcbildat) =TO_CHAR(to_date('"+ shidate +"','DD-MM-YY')) AND trim(elm.lcprtcde) ='"+ portcode +"'";
                                    
                                    
                                     System.out.println("Query2 formNO EXTEVENTELC---------->" + query2);
                                    // ////Loggers.general().info(LOG,"Query2 -------->" + query2);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Query2 -------->" + query2);
                                    }
                                    //con = ConnectionMaster.getConnection();
                                    try {
                                        if (ps != null)
                                          ps.close();
                                        if (rst != null)
                                          rst.close();
                                        rst = null;

                                    } catch (Exception e) {
                                        System.out.println("Exception Query2 formNO EXTEVENTSPD---------->" + e.getMessage());
                                    }
                                    ps= connection.prepareStatement(query2);
                                    rst = ps.executeQuery();
                                    while (rst.next()) {
                                          count2 = rst.getInt(1);
                                          System.out.println("Count value for shipping formNO---------->" + count2);
                                          // ////Loggers.general().info(LOG,"Count value for Invoice
                                          // bill---------->" + count2);
                                    }
                                    // ODC bill no checking
                                    int count_ODC = 0;
                              /*    String query_ODC = "SELECT DISTINCT CASE WHEN SDBILLNO IN (SELECT elm.SDBILLNO FROM EXTEVENTSPD elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.MASTER_KEY = mas.KEY97 AND elm.SDBILLNO ='"
                                                + billnum
                                                + "') THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSPD elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 and elm.SDBILLNO ='"
                                                + billnum + "'";*/
                                    
                                    
                                     String query_ODC ="SELECT CASE WHEN COUNT(*)<>0 THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSPD elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 "
                                 + " AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 AND trim(elm.SDBILLNO) ='"+ billnum +"'"
                                  + " and trim(elm.sdformno)='"+ formNO +"'  AND TO_CHAR(elm.sdbildat) =TO_CHAR(to_date('"+ shidate +"','DD-MM-YY')) AND trim(elm.sdprtcde) ='"+ portcode +"'";
                        
                                    // ////Loggers.general().info(LOG,"Query2 billnum---------->" +
                                    // query_ODC);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          System.out.println("Query2 billnum---------->" + query_ODC);
                                    }
                                    try {
                                        if (ps != null)
                                          ps.close();
                                        if (rst != null)
                                          rst.close();
                                        rst = null;

                                    } catch (Exception e) {
                                        System.out.println("Exception Query2 form no EXTEVENTSLC-------->" + e.getMessage());
                                    }
                                    ps = connection.prepareStatement(query_ODC);
                                    rst = ps.executeQuery();
                                    while (rst.next()) {
                                          count_ODC = rst.getInt(1);
                                          System.out.println("count_ODC value for shipping bill---------->" + count_ODC);
                                    }

                                    if (count_ODC < 1 && count2 < 1) {
                                    
                          Loggers.general().info(LOG,"Start of export 1-----");
                                          String queryexport1 = "select DISTINCT shipbillno,TO_CHAR(TO_DATE(shipbilldate,'ddmmyyyy'),'yyyy-mm-dd') AS SDATE,portcode,TO_CHAR(leodate,'yyyy-mm-dd') AS LDATE,custno,exportagency,exporttype,countrydest,iecode,adcode,formno,RECIND from ett_edpms_shp where shipbillno='"
                                                      + billnum + "' AND trim(IECODE)='" + iecode
                                                      + "' AND SHIPBILLDATE = to_char(to_date('" + shidate
                                                      + "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE='" + portcode + "' AND FORMNO='"+formNO+"'";
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                System.out.println("Shipping bill number result SEZ export 1-------->" + query);

                                          }
                                           try {
                                                      if (ps != null)
                                                          ps.close();
                                                      if (rst != null)
                                                          rst.close();
                                                      rst = null;

                                                    } catch (Exception e) {
                                                      System.out.println(" Exception Shipping bill queryexport1-------->" + e.getMessage());
                                                    }
                                          ps = connection.prepareStatement(queryexport1);
                                          rst = ps.executeQuery();
                                          while (rst.next()) {
                                                System.out.println("Entered rs");
                                                Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                                                ExtEventShippingdetailslc shippingdetails = new ExtEventShippingdetailslc();
                                                if (billnum.length() > 0) {
                                                      shippingdetails.setColumn("LCBILLNO", rst.getString(1));
                                                } else {
                                                      // ////Loggers.general().info(LOG,"LCBILLNO number
                                                      // else");
                                                      shippingdetails.setColumn("LCBILLNO", "");
                                                }
                                                shippingdetails.setColumn("LCBILDAT", rst.getString(2));
                                                Loggers.general().info(LOG,"lcbildate---" + shippingdetails.getColumn("LCBILDAT"));
                                                Loggers.general().info(LOG,"date" + rst.getString(2));
                                                shippingdetails.setColumn("LCPRTCDE", rst.getString(3));
                                                // ////Loggers.general().info(LOG,"PORTCODE" +
                                                // rst.getString(3));
                                                shippingdetails.setColumn("LCLEODAT", rst.getString(4));
                                                shippingdetails.setColumn("LCCUSTNO", rst.getString(5));
                                                shippingdetails.setColumn("LCEXAGNC", rst.getString(6));
                                                shippingdetails.setColumn("LCEXPTYP", rst.getString(7));
                                                shippingdetails.setColumn("LCDESCON", rst.getString(8));
                                                shippingdetails.setColumn("LCIECOD", rst.getString(9));
                                                shippingdetails.setColumn("LCADCOD", rst.getString(10));
                                                shippingdetails.setColumn("LCFORMNO", rst.getString(11));
                                                shippingdetails.setColumn("RECIN", rst.getString(12));
                                                shippingdetails.setSERIAL(String.valueOf(j));
                                                getBtnFetchInvdetELCDPclay().setEnabled(true);
                                                // ////Loggers.general().info(LOG,"getBtnFetchInvdetELCDPclay
                                                // is enabled in if loop");
                                                System.out.println("After button in while");
                                                shippingdetails.setNewKey();
                                                shippingdetails.setFk(fkey);
                                                shippingdetails.setSequence(count);

                                                ExtEventShippingdetailslcEntityWrapper shippingdwrapper1 = new ExtEventShippingdetailslcEntityWrapper(
                                                            shippingdetails, getDriverWrapper());
                                                addNewExtEventShippingdetailslc(shippingdwrapper1);

                                                count++;

                                          }
                                          
                          Loggers.general().info(LOG,"Start of export 2");
                                          String queryexport2 = "select DISTINCT shipbillno,TO_CHAR(TO_DATE(shipbilldate,'ddmmyyyy'),'yyyy-mm-dd') AS SDATE,portcode,TO_CHAR(leodate,'yyyy-mm-dd') AS LDATE,custno,exportagency,exporttype,countrydest,iecode,adcode,formno,RECIND from ETT_EDPMS_SHP_SOFTEX where shipbillno='"
                                                      + billnum + "' AND trim(IECODE)='" + iecode
                                                      + "' AND SHIPBILLDATE = to_char(to_date('" + shidate
                                                      + "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE='" + portcode + "' AND FORMNO='"+formNO+"'";
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                System.out.println("Shipping bill number result SEZ export 2-------->" + queryexport2);

                                          }
                                            try {
                                                      if (ps != null)
                                                          ps.close();
                                                      if (rst != null)
                                                          rst.close();
                                                      rst = null;

                                                    } catch (Exception e) {
                                                      System.out.println("Exception From number result-------->" + e.getMessage());
                                                    }
                                          ps = connection.prepareStatement(queryexport2);
                                          rst = ps.executeQuery();
                                          while (rst.next()) {
                                                Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                                                ExtEventShippingdetailslc shippingdetails = new ExtEventShippingdetailslc();
                                                
                                                      shippingdetails.setColumn("LCBILLNO", rst.getString(1));
                                    
                                                shippingdetails.setColumn("LCBILDAT", rst.getString(2));
                                                shippingdetails.setColumn("LCPRTCDE", rst.getString(3));
                                                System.out.println("PORTCODE" + rst.getString(3));
                                                shippingdetails.setColumn("LCLEODAT", rst.getString(4));
                                                shippingdetails.setColumn("LCCUSTNO", rst.getString(5));
                                                shippingdetails.setColumn("LCEXAGNC", rst.getString(6));
                                                shippingdetails.setColumn("LCEXPTYP", rst.getString(7));
                                                shippingdetails.setColumn("LCDESCON", rst.getString(8));
                                                shippingdetails.setColumn("LCIECOD", rst.getString(9));
                                                shippingdetails.setColumn("LCADCOD", rst.getString(10));
                                                shippingdetails.setColumn("LCFORMNO", rst.getString(11));
                                                shippingdetails.setColumn("RECIN", rst.getString(12));
                                                shippingdetails.setSERIAL(String.valueOf(j));
                                                getBtnFetchInvdetELCDPclay().setEnabled(true);
                                                System.out.println("getBtnFetchInvdetELCDPclay is enabled in if loop");
                                                shippingdetails.setNewKey();
                                                shippingdetails.setFk(fkey);
                                                shippingdetails.setSequence(count);

                                                ExtEventShippingdetailslcEntityWrapper shippingdwrapper1 = new ExtEventShippingdetailslcEntityWrapper(
                                                            shippingdetails, getDriverWrapper());
                                                addNewExtEventShippingdetailslc(shippingdwrapper1);

                                                count++;

                                          }


                                    } else {
                                          Loggers.general().info(LOG,"count_ODC value for shipping bill---------->" + count2 + " " + count_ODC);
                                    }

                                    if (listef.getSize().intValue() == 0) {
                                          getBtnFetchInvdetELCDPclay().setEnabled(false);
                                    } else if (listef.getSize().intValue() > 0) {

                                          getBtnFetchInvdetELCDPclay().setEnabled(true);
                                    }
                              }
                              
                              else if (billnum.length() > 0 && formNO.length()<=0) {
                              /*    String query2 = "SELECT DISTINCT CASE WHEN SDBILLNO IN (SELECT elm.SDBILLNO FROM EXTEVENTSPD elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.MASTER_KEY = mas.KEY97 AND elm.SDBILLNO ='"
                                                + billnum + "' and TO_CHAR(elm.sdbildat) =to_char(to_date('" + shidate+ "','DD-MM-YY')) "
                                                + " and elm.sdprtcde ='" + portcode
                                                + "' ) THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSPD elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 AND elm.SDBILLNO ='"
                                                + billnum + "' and TO_CHAR(elm.sdbildat) =to_char(to_date('" + shidate+ "','DD-MM-YY')) "
                                                + " and elm.sdprtcde ='" + portcode + "'";*/
                                    System.out.println("billnum entered---------->" + billnum);
                                     String query2 ="SELECT CASE WHEN COUNT(*)<>0 THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSPD elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 "
                                 + " AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 AND trim(elm.SDBILLNO) ='"+ billnum +"'"
                                  + " AND TO_CHAR(elm.sdbildat) =TO_CHAR(to_date('"+ shidate +"','DD-MM-YY')) AND trim(elm.sdprtcde) ='"+ portcode +"'";
                                     System.out.println("Query2 billnum EXTEVENTspd---------->" + query2);
                                    // ////Loggers.general().info(LOG,"Query2 -------->" + query2);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Query2 -------->" + query2);
                                    }
                                    //con = ConnectionMaster.getConnection();
                                    try {
                                        if (ps != null)
                                          ps.close();
                                        if (rst != null)
                                          rst.close();
                                        rst = null;

                                    } catch (Exception e) {
                                        System.out.println("Exception Query2 billnum EXTEVENTELC---------->" + e.getMessage());
                                    }
                                    ps = connection.prepareStatement(query2);
                                    rst = ps.executeQuery();
                                    while (rst.next()) {
                                          count2 = rst.getInt(1);
                                          System.out.println("Query2 billnum count2---------->" + count2);
                                          // ////Loggers.general().info(LOG,"Count value for Invoice
                                          // bill---------->" + count2);
                                    }
                                    // ODC bill no checking
                                    int count_ODC = 0;
                              /*    String query_ODC = "SELECT DISTINCT CASE WHEN SDBILLNO IN (SELECT elm.SDBILLNO FROM EXTEVENTSPD elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.MASTER_KEY = mas.KEY97 AND elm.SDBILLNO ='"
                                                + billnum
                                                + "') THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSPD elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 and elm.SDBILLNO ='"
                                                + billnum + "'";*/
                                     String query_ODC ="SELECT CASE WHEN COUNT(*)<>0 THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSLC elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 "
                                 + " AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 AND trim(elm.lcBILLNO) ='"+ billnum +"'"
                                 + " AND TO_CHAR(elm.lcbildat) =TO_CHAR(to_date('"+shidate+"','DD-MM-YY')) AND trim(elm.lcprtcde) ='"+ portcode +"'";
                        
                                    
                                    /*

*/                       System.out.println("Query2 billnum---------->" +query_ODC);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Query2 billnum---------->" + query_ODC);
                                    }
                                    try {
                                        if (ps != null)
                                          ps.close();
                                        if (rst != null)
                                          rst.close();
                                        rst = null;

                                    } catch (Exception e) {
                                        System.out.println("Exception Query_elc EXTEVENTSLC-------->" + e.getMessage());
                                    }

                                    ps = connection.prepareStatement(query_ODC);
                                    rst = ps.executeQuery();
                                    while (rst.next()) {
                                          count_ODC = rst.getInt(1);
                                          System.out.println("count_ODC value for shipping bill EXTEVENTSLC---------->" + count_ODC);
                                    }

                                    if (count_ODC < 1 && count2 < 1) {

                                          query = "select DISTINCT shipbillno,TO_CHAR(TO_DATE(shipbilldate,'ddmmyyyy'),'yyyy-mm-dd') AS SDATE,portcode,TO_CHAR(leodate,'yyyy-mm-dd') AS LDATE,custno,exportagency,exporttype,countrydest,iecode,adcode,formno,RECIND from ett_edpms_shp where shipbillno='"
                                                      + billnum + "' AND trim(IECODE)='" + iecode
                                                      + "' AND SHIPBILLDATE = to_char(to_date('" + shidate
                                                      + "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE='" + portcode + "'";
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Shipping bill number result-------->" + query);

                                          }
                                          try {
                                              if (ps != null)
                                                ps.close();
                                              if (rst != null)
                                                rst.close();
                                              rst = null;

                                          } catch (Exception e) {
                                              System.out.println("Exception Shipping bill result-------->" + e.getMessage());
                                          }
                                          ps = connection.prepareStatement(query);
                                          rst = ps.executeQuery();
                                          while (rst.next()) {
                                                Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                                                ExtEventShippingdetailslc shippingdetails = new ExtEventShippingdetailslc();
                                                if (billnum.length() > 0) {
                                                      shippingdetails.setColumn("LCBILLNO", rst.getString(1));
                                                } else {
                                                      // ////Loggers.general().info(LOG,"LCBILLNO number
                                                      // else");
                                                      shippingdetails.setColumn("LCBILLNO", "");
                                                }
                                                System.out.println("inside shipment fetch button" + query);
                                                shippingdetails.setColumn("LCBILDAT", rst.getString(2));
                                                shippingdetails.setColumn("LCPRTCDE", rst.getString(3));
                                                // ////Loggers.general().info(LOG,"PORTCODE" +
                                                // rst.getString(3));
                                                shippingdetails.setColumn("LCLEODAT", rst.getString(4));
                                                shippingdetails.setColumn("LCCUSTNO", rst.getString(5));
                                                shippingdetails.setColumn("LCEXAGNC", rst.getString(6));
                                                shippingdetails.setColumn("LCEXPTYP", rst.getString(7));
                                                shippingdetails.setColumn("LCDESCON", rst.getString(8));
                                                shippingdetails.setColumn("LCIECOD", rst.getString(9));
                                                shippingdetails.setColumn("LCADCOD", rst.getString(10));
                                                shippingdetails.setColumn("LCFORMNO", rst.getString(11));
                                                shippingdetails.setColumn("RECIN", rst.getString(12));
                                                shippingdetails.setSERIAL(String.valueOf(j));
                                                getBtnFetchInvdetELCDPclay().setEnabled(true);
                                                // ////Loggers.general().info(LOG,"getBtnFetchInvdetELCDPclay
                                                // is enabled in if loop");
                                                shippingdetails.setNewKey();
                                                shippingdetails.setFk(fkey);
                                                shippingdetails.setSequence(count);

                                                ExtEventShippingdetailslcEntityWrapper shippingdwrapper1 = new ExtEventShippingdetailslcEntityWrapper(
                                                            shippingdetails, getDriverWrapper());
                                                addNewExtEventShippingdetailslc(shippingdwrapper1);

                                                count++;

                                          }

                                    } else {
                                          System.out.println("count_ODC value for shipping bill---------->" + count2 + " " + count_ODC);
                                    }

                                    if (listef.getSize().intValue() == 0) {
                                          getBtnFetchInvdetELCDPclay().setEnabled(false);
                                    } else if (listef.getSize().intValue() > 0) {

                                          getBtnFetchInvdetELCDPclay().setEnabled(true);
                                    }
                              } else if (formNO.length() > 0&&billnum.length()<=0) {
                                    System.out.println("entered formNO=====>");
                              /*    String query2 = "SELECT DISTINCT CASE WHEN LCFORMNO IN (SELECT elm.LCFORMNO FROM EXTEVENTSLC elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.MASTER_KEY = mas.KEY97 AND elm.LCFORMNO ='"
                                                + formNO + "' and TO_CHAR(elm.lcbildat) =to_char(to_date('" + shidate+ "','DD-MM-YY')) "
                                                + " AND elm.LCPRTCDE='" + portcode
                                                + "') THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSLC elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 and elm.LCFORMNO='"
                                                + formNO + "'  and TO_CHAR(elm.lcbildat) =to_char(to_date('" + shidate+ "','DD-MM-YY')) "
                                                + " AND elm.LCPRTCDE='" + portcode + "'";*/
                                    
                                     String query2 ="SELECT CASE WHEN COUNT(*)<>0 THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSLC elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 "
                                 + " AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 AND trim(elm.lcformno)  ='"+ formNO +"'"
                                 + " AND TO_CHAR(elm.lcbildat) =TO_CHAR(to_date('"+ shidate +"','DD-MM-YY')) AND trim(elm.lcprtcde) ='"+ portcode +"'";
                        
                                    
                                     System.out.println("Query2 form no-------->" + query2);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Query2 form no-------->" + query2);
                                    }
                                    try {
                                        if (ps != null)
                                          ps.close();
                                        if (rst != null)
                                          rst.close();
                                        rst = null;

                                    } catch (Exception e) {
                                        System.out.println("Query2 formNO EXTEVENTSPD exception" + e.getMessage());
                                        e.getMessage();
                                    }
                                    //con = ConnectionMaster.getConnection();
                                    ps = connection.prepareStatement(query2);
                                    rst = ps.executeQuery();
                                    while (rst.next()) {
                                          count2 = rst.getInt(1);
                                          System.out.println("Count value for Invoice form no---------->" + count2);
                                    }

                                    // ODC FORM NO CHECKING
                                    int count_ODC = 0;
                                    /*String query_ODC = "SELECT DISTINCT CASE WHEN SDFORMNO IN (SELECT elm.SDFORMNO FROM EXTEVENTSPD elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.MASTER_KEY = mas.KEY97 AND elm.SDFORMNO ='"
                                                + formNO + "' and TO_CHAR(elm.sdbildat) =to_char(to_date('" + shidate+ "','DD-MM-YY')) "
                                                + " and elm.sdprtcde ='" + portcode
                                                + "' ) THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSPD elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29 AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 AND elm.SDFORMNO ='"
                                                + formNO + "' and TO_CHAR(elm.sdbildat) =to_char(to_date('" + shidate+ "','DD-MM-YY')) "
                                                + " and elm.sdprtcde ='" + portcode + "'";*/
                                    
                                     String query_ODC ="SELECT CASE WHEN COUNT(*)<>0 THEN 1 ELSE 0 END AS COUNT FROM EXTEVENTSPD elm, EXTEVENT exte, BASEEVENT bev, master mas WHERE elm.FK_EVENT = exte.KEY29  "
                                 + " AND exte.EVENT = bev.key97 AND bev.STATUS != 'a' AND bev.MASTER_KEY = mas.KEY97 AND trim(elm.SDFORMNO) ='"+ formNO +"'"
                                  + " AND TO_CHAR(elm.sdbildat) =TO_CHAR(to_date('"+ shidate +"','DD-MM-YY')) AND trim(elm.sdprtcde) ='"+ portcode +"'";
                                    
                                     System.out.println("Query2 formNO---------->" + query_ODC);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Query2 formNO---------->" + query_ODC);
                                    }
                                    try{
                                          if(ps !=null)
                                                ps.close();
                                          if(rst !=null)
                                                rst.close();
                                    }catch(Exception e){
                                          System.out.println("close shp---->"+e.getMessage());
                                    }
                                    ps = connection.prepareStatement(query_ODC);
                                    rst = ps.executeQuery();
                                    while (rst.next()) {
                                          count_ODC = rst.getInt(1);
                                          System.out.println("count_ODC value for shipping formNO---------->" + count_ODC);
                                    }

                                    if (count_ODC < 1 && count2 < 1) {
                                          query = "SELECT DISTINCT fileno, TO_CHAR(TO_DATE(shipbilldate,'ddmmyyyy'),'yyyy-mm-dd') AS sofdate, portcode, TO_CHAR(leodate,'yyyy-mm-dd') AS LEODATE, custno, exportagency, exporttype, countrydest, iecode, adcode, formno,RECIND FROM ett_edpms_shp_softex WHERE formno ='"
                                                      + formNO + "' AND trim(IECODE)='" + iecode
                                                      + "' AND SHIPBILLDATE = to_char(to_date('" + shidate
                                                      + "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE='" + portcode + "'";
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Form number result-------->" + query);

                                          }
                                          try {
                                                if (ps != null)
                                                    ps.close();
                                                if (rst != null)
                                                    rst.close();
                                                rst = null;

                                              } catch (Exception e) {
                                                System.out.println("Exception fetch button last query------>" + e.getMessage());
                                              }
                                          ps = connection.prepareStatement(query);
                                          rst = ps.executeQuery();
                                          while (rst.next()) {
                                                Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                                                ExtEventShippingdetailslc shippingdetails = new ExtEventShippingdetailslc();
                                                if (billnum.length() > 0) {
                                                      shippingdetails.setColumn("LCBILLNO", rst.getString(1));
                                                } else {
                                                      //// Loggers.general().info(LOG,"LCBILLNO number
                                                      //// else");
                                                      shippingdetails.setColumn("LCBILLNO", "");
                                                }
                                                shippingdetails.setColumn("LCBILDAT", rst.getString(2));
                                                shippingdetails.setColumn("LCPRTCDE", rst.getString(3));
                                                System.out.println("PORTCODE" +rst.getString(3));
                                                shippingdetails.setColumn("LCLEODAT", rst.getString(4));
                                                shippingdetails.setColumn("LCCUSTNO", rst.getString(5));
                                                shippingdetails.setColumn("LCEXAGNC", rst.getString(6));
                                                shippingdetails.setColumn("LCEXPTYP", rst.getString(7));
                                                shippingdetails.setColumn("LCDESCON", rst.getString(8));
                                                shippingdetails.setColumn("LCIECOD", rst.getString(9));
                                                shippingdetails.setColumn("LCADCOD", rst.getString(10));
                                                shippingdetails.setColumn("LCFORMNO", rst.getString(11));
                                                shippingdetails.setColumn("RECIN", rst.getString(12));
                                                shippingdetails.setSERIAL(String.valueOf(j));
                                                getBtnFetchInvdetELCDPclay().setEnabled(true);
                                                // ////Loggers.general().info(LOG,"getBtnFetchInvdetELCDPclay
                                                // is enabled in if loop");
                                                shippingdetails.setNewKey();
                                                shippingdetails.setFk(fkey);
                                                shippingdetails.setSequence(count);

                                                ExtEventShippingdetailslcEntityWrapper shippingdwrapper1 = new ExtEventShippingdetailslcEntityWrapper(
                                                            shippingdetails, getDriverWrapper());
                                                addNewExtEventShippingdetailslc(shippingdwrapper1);

                                                count++;

                                          }

                                    } else {
                                          //// Loggers.general().info(LOG,"count_ODC value for form
                                          //// no---------->" + count2 + " " + count_ODC);
                                    }
                                    if (listef.getSize().intValue() == 0) {
                                          getBtnFetchInvdetELCDPclay().setEnabled(false);
                                    } else if (listef.getSize().intValue() > 0) {

                                          getBtnFetchInvdetELCDPclay().setEnabled(true);
                                    }
                              }
                              else
                              {
                                    Loggers.general().info(LOG,"Not in any if elcpane===");
                              }
                                    
                        }
                        try {
                              if (connection != null)
                                    connection.close();
                        } catch (Exception e) {
                              e.getMessage();
                        }

                  }
                  if (liste.getSize().intValue() > 0) {
                        getBtnFetchShipdetELCDPclay().setEnabled(false);
                  } else {
                        getBtnFetchShipdetELCDPclay().setEnabled(true);
                  }

            } catch (Exception e) {
                  //// Loggers.general().info(LOG,"Exception for shipping population " +
                  //// e.getMessage());
                  System.out.println("Exception for shipping population " + e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        System.out.println("Exception for shipping population " + e.getMessage());
                  }

            }

            finally {
                  try {
                        if (rst != null)
                            rst.close();
                        if (pst != null)
                            pst.close();
                        if (rs != null)
                            rs.close();
                        if (ps != null)
                            ps.close();
                        if (con != null)
                            con.close();
                      } catch (SQLException e) {
                        System.out.println("Connection Failed! Check output console");
                        e.printStackTrace();
                  }
            }
      }

      public void onFetchInvdetELCDPclayButton() {
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

                  EnigmaArray<ExtEventShippingTableEntityWrapper> liste = getExtEventShippingTableData();
                  EnigmaArray<ExtEventShippingdetailslcEntityWrapper> listes = getExtEventShippingdetailslcData();
                  EnigmaArray<ExtEventInvoicedetailsLCEntityWrapper> listef = getExtEventInvoicedetailsLCData();
                  Iterator<ExtEventShippingTableEntityWrapper> iterator = liste.iterator();
                  Iterator<ExtEventInvoicedetailsLCEntityWrapper> iterator1 = listef.iterator();
                  Iterator<ExtEventShippingdetailslcEntityWrapper> iterator2 = listes.iterator();
                  for(ExtEventInvoicedetailsLCEntityWrapper extship:listef)
                  {
                        Loggers.general().info(LOG,"Entered into for all");
                        removeExtEventInvoicedetailsLC(extship);
                  }
                  int count = 0;
                  int j=0;
                  String query = "";
                  String formNO = "";
                  String billnum = "";
                   String portcode = "";
                      String shipamt = "";
              String add_lie = "";
                  Connection connection = null;
                  ResultSet rst = null;
                  PreparedStatement ps = null;

                  for (int i = 0; i < listes.getSize().intValue(); i++) {
                        if (connection == null) {
                              connection = ConnectionMaster.getConnection();
                        }

                        while (iterator.hasNext()) {
                              
                  
                              ExtEventShippingTableEntityWrapper sdwrapper = (ExtEventShippingTableEntityWrapper) iterator.next();
                              ExtEventInvoicedetailsLCEntityWrapper sdwrapper1 = (ExtEventInvoicedetailsLCEntityWrapper) iterator1
                                          .next();
                              ExtEventShippingdetailslcEntityWrapper sdwrapper2 = (ExtEventShippingdetailslcEntityWrapper) iterator2
                                          .next();
                              // ////Loggers.general().info(LOG,"bill number" +
                              // sdwrapper.getBILLNUM());
                              
                                

                                  try {
                                    billnum = sdwrapper.getBILLNUM().trim();

                                    System.out.println("billnum num and other details" + billnum);
                                  } catch (Exception e) {
                                    System.out.println("Exception billnum num and other details" + e.getMessage());

                                  }

                                  try {
                                    formNO = sdwrapper.getFORMNUM().trim();
                                    System.out.println("Form num and other details" + formNO);
                                  } catch (Exception e) {
                                    System.out.println("Exception Form num and other details" + e.getMessage());

                                  }

                                  portcode = sdwrapper.getPORTCODDD();
                                  shipamt = sdwrapper.getSHPAMT();
                                  if (!shipamt.equalsIgnoreCase("")) {
                                    add_lie = shipamt.replaceAll("[^0-9]", "");
                                  }
                        //        String shidate = sdwrapper.getBILLDAT();
                                  System.out.println("bill num and other details" + billnum + " " + portcode + " " + shipamt);
                                  System.out.println("Shipping bill num" + billnum);
                                  System.out.println("Shipping form num" + formNO);
                                  j=j+1;
                              String billdate = null;
                               if (billnum.length() > 0 && formNO.length()>0) {
                                          Loggers.general().info(LOG,"Inside sez invoice---");
            
                                                      
                                                      Loggers.general().info(LOG,"inside sez invoice export 1blaaa----");
                                                      String formNO_b = sdwrapper2.getLCFORMNO().trim();
                                                       String billnum_b = sdwrapper2.getLCBILLNO().trim();
                                                       String portcode_b = sdwrapper2.getLCPORTCODE();
                                                       String shidate = sdwrapper2.getLCBILDAT();
                                                       System.out.println("portcode"+portcode_b);
                                                       System.out.println("billnum_b"+billnum_b);
                                                      
                                                      String queryexport1 = "SELECT invserialno, invno, TO_CHAR(TO_DATE(invdate,'dd-mm-yyyy'),'yyyy-mm-dd') AS INDATE, fobamt, fobcurrcode, NVL(frieghtamt,0), NVL(TRIM(frieghtcurrcode),fobcurrcode), NVL(insamt,0), NVL(TRIM(inscurrcode),fobcurrcode), NVL(commamt,0), NVL(TRIM(commcurrcode), fobcurrcode), NVL(disamt,0), NVL(TRIM(discurrcode), fobcurrcode), NVL(dedamt,0), NVL(TRIM(dedcurrcode), fobcurrcode), NVL(pkgamt,0), NVL(TRIM(pkgcurrcode), fobcurrcode), shipbillno, TO_CHAR(TO_DATE(shipbilldate,'ddmmyyyy'),'DD/MM/YY'), portcode, shipbillno,formno FROM ett_edpms_shp_inv WHERE shipbillno='"
                                                                  + billnum_b + "' AND SHIPBILLDATE = to_char(to_date('" + shidate
                                                                  + "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE='" + portcode_b + "' and formno='" + formNO_b +"'";
                                                      //// Loggers.general().info(LOG,"Invoice bill fecting==>" +
                                                      //// query);
                                                      System.out.println("Query value Invoice billnum and form no" + queryexport1);
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            Loggers.general().info(LOG,"Invoice bill fecting sez export 1==>" + queryexport1);
                                                      }
                                                      try {
                                                          if (ps != null)
                                                            ps.close();
                                                          if (rst != null)
                                                            rst.close();
                                                          rst = null;
                                                      } catch (Exception e) {
                                                          System.out.println("Exception Query value Invoice form no" + e.getMessage());
                                                      }
                                                      ps = connection.prepareStatement(queryexport1);
                                                      rst = ps.executeQuery();
                                                      while (rst.next()) {
                                                            Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                                                            ExtEventInvoicedetailsLC iinvoicedetails = new ExtEventInvoicedetailsLC();
                                                            iinvoicedetails.setColumn("INVSRNO", rst.getString(1));
                                                            iinvoicedetails.setColumn("INVNO", rst.getString(2));
                                                            iinvoicedetails.setColumn("INVDATE", rst.getString(3));
                                                            System.out.println("invdate---" + iinvoicedetails.getColumn("INVDATE"));
                                                          System.out.println("date" + rst.getString(3));
                                                            iinvoicedetails.setColumn("IFOBAMT",
                                                                        getDriverWrapper().convertFromToDBFormat(rst.getString(4), rst.getString(5),
                                                                                    "T") + rst.getString(5));
                                                            iinvoicedetails.setColumn("CCY", rst.getString(5));
                                                            iinvoicedetails.setColumn("INVFRAMT",
                                                                        getDriverWrapper().convertFromToDBFormat(rst.getString(6), rst.getString(7),
                                                                                    "T") + rst.getString(7));
                                                            iinvoicedetails.setColumn("CCY_1", rst.getString(7));
                                                            iinvoicedetails.setColumn("INSUAMT",
                                                                        getDriverWrapper().convertFromToDBFormat(rst.getString(8), rst.getString(9),
                                                                                    "T") + rst.getString(9));
                                                            iinvoicedetails.setColumn("CCY_2", rst.getString(9));
                                                            iinvoicedetails.setColumn("ICOMMAMT",
                                                                        getDriverWrapper().convertFromToDBFormat(rst.getString(10), rst.getString(11),
                                                                                    "T") + rst.getString(11));
                                                            iinvoicedetails.setColumn("CCY_3", rst.getString(11));
                                                            iinvoicedetails.setColumn("IDISCAMT",
                                                                        getDriverWrapper().convertFromToDBFormat(rst.getString(12), rst.getString(13),
                                                                                    "T") + rst.getString(13));
                                                            iinvoicedetails.setColumn("CCY_4", rst.getString(13));
                                                            iinvoicedetails.setColumn("IDEDUAMT",
                                                                        getDriverWrapper().convertFromToDBFormat(rst.getString(14), rst.getString(15),
                                                                                    "T") + rst.getString(15));
                                                            //// Loggers.general().info(LOG,"PORTCODE" +
                                                            //// rst.getString(3));
                                                            iinvoicedetails.setColumn("CCY_5", rst.getString(15));
                                                            iinvoicedetails.setColumn("IPKGAMT",
                                                                        getDriverWrapper().convertFromToDBFormat(rst.getString(16), rst.getString(17),
                                                                                    "T") + rst.getString(17));
                                                            iinvoicedetails.setColumn("CCY_6", rst.getString(17));
                                                            iinvoicedetails.setColumn("INVPRTCD", rst.getString(20));
                                                            if (billnum.length() > 0) {
                                                                  // ////Loggers.general().info(LOG,"ISHPBILL number
                                                                  // else");
                                                                  iinvoicedetails.setColumn("ISHPBILL", rst.getString(21));
                                                            } else {
                                                                  iinvoicedetails.setColumn("ISHPBILL", "");
                                                            }
                                                            iinvoicedetails.setColumn("IFORNO", rst.getString(22));
                                                            iinvoicedetails.setSERINO(String.valueOf(j));
                                                            iinvoicedetails.setNewKey();
                                                            iinvoicedetails.setFk(fkey);
                                                            iinvoicedetails.setSequence(count);

                                                            ExtEventInvoicedetailsLCEntityWrapper invoicewrapper = new ExtEventInvoicedetailsLCEntityWrapper(
                                                                        iinvoicedetails, getDriverWrapper());
                                                            addNewExtEventInvoicedetailsLC(invoicewrapper);

                                                            count++;

                                                      }
                                                      Loggers.general().info(LOG,"inside sez invoice expoert 2----");
                                                      String queryexport2 = "SELECT invserialno, invno, TO_CHAR(TO_DATE(invdate),'yyyy-mm-dd') AS invdate, fobamt, fobcurrcode, NVL(frieghtamt,0), NVL(TRIM(frieghtcurrcode),fobcurrcode), NVL(insamt,0), NVL(TRIM(inscurrcode),fobcurrcode), NVL(commamt,0), NVL(TRIM(commcurrcode), fobcurrcode), NVL(disamt,0), NVL(TRIM(discurrcode), fobcurrcode), NVL(dedamt,0), NVL(TRIM(dedcurrcode), fobcurrcode), NVL(pkgamt,0), NVL(TRIM(pkgcurrcode), fobcurrcode), fileno, TO_CHAR(TO_DATE(shipbilldate,'ddmmyyyy'),'DD/MM/YY') AS SHIPBILLDATE, portcode, shipbillno, formno FROM ett_edpms_shp_inv_softex WHERE formno='"
                                                                  + formNO_b + "' AND SHIPBILLDATE = to_char(to_date('" + shidate
                                                                  + "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE='" + portcode_b + "' and shipbillno='" + billnum_b + "'";
                                                      //// Loggers.general().info(LOG,"Invoice bill fecting==>" +
                                                      //// query);
                                                      System.out.println("Invoice bill fecting sez export 2==>" + queryexport2);
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            Loggers.general().info(LOG,"Invoice bill fecting==>" +
                                                                         query);
                                                      }
                                                      try {
                                                          if (ps != null)
                                                            ps.close();
                                                          if (rst != null)
                                                            rst.close();
                                                          rst = null;
                                                      } catch (Exception e) {
                                                          System.out.println("Exception Query value Invoice form no" + e.getMessage());
                                                      }
                                                      ps = connection.prepareStatement(queryexport2);
                                                      rst = ps.executeQuery();
                                                      while (rst.next()) {
                                                            Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                                                            ExtEventInvoicedetailsLC iinvoicedetails = new ExtEventInvoicedetailsLC();
                                                            iinvoicedetails.setColumn("INVSRNO", rst.getString(1));
                                                            iinvoicedetails.setColumn("INVNO", rst.getString(2));
                                                            iinvoicedetails.setColumn("INVDATE", rst.getString(3));
                                                            System.out.println("invdate---" + iinvoicedetails.getColumn("INVDATE"));
                                                          System.out.println("date" + rst.getString(3));
                                                            iinvoicedetails.setColumn("IFOBAMT",
                                                                        getDriverWrapper().convertFromToDBFormat(rst.getString(4), rst.getString(5),
                                                                                    "T") + rst.getString(5));
                                                            iinvoicedetails.setColumn("CCY", rst.getString(5));
                                                            iinvoicedetails.setColumn("INVFRAMT",
                                                                        getDriverWrapper().convertFromToDBFormat(rst.getString(6), rst.getString(7),
                                                                                    "T") + rst.getString(7));
                                                            iinvoicedetails.setColumn("CCY_1", rst.getString(7));
                                                            iinvoicedetails.setColumn("INSUAMT",
                                                                        getDriverWrapper().convertFromToDBFormat(rst.getString(8), rst.getString(9),
                                                                                    "T") + rst.getString(9));
                                                            iinvoicedetails.setColumn("CCY_2", rst.getString(9));
                                                            iinvoicedetails.setColumn("ICOMMAMT",
                                                                        getDriverWrapper().convertFromToDBFormat(rst.getString(10), rst.getString(11),
                                                                                    "T") + rst.getString(11));
                                                            iinvoicedetails.setColumn("CCY_3", rst.getString(11));
                                                            iinvoicedetails.setColumn("IDISCAMT",
                                                                        getDriverWrapper().convertFromToDBFormat(rst.getString(12), rst.getString(13),
                                                                                    "T") + rst.getString(13));
                                                            iinvoicedetails.setColumn("CCY_4", rst.getString(13));
                                                            iinvoicedetails.setColumn("IDEDUAMT",
                                                                        getDriverWrapper().convertFromToDBFormat(rst.getString(14), rst.getString(15),
                                                                                    "T") + rst.getString(15));
                                                            //// Loggers.general().info(LOG,"PORTCODE" +
                                                            //// rst.getString(3));
                                                            iinvoicedetails.setColumn("CCY_5", rst.getString(15));
                                                            iinvoicedetails.setColumn("IPKGAMT",
                                                                        getDriverWrapper().convertFromToDBFormat(rst.getString(16), rst.getString(17),
                                                                                    "T") + rst.getString(17));
                                                            iinvoicedetails.setColumn("CCY_6", rst.getString(17));
                                                            iinvoicedetails.setColumn("INVPRTCD", rst.getString(20));
                                                            if (billnum.length() > 0) {
                                                                  // ////Loggers.general().info(LOG,"ISHPBILL number
                                                                  // else");
                                                                  iinvoicedetails.setColumn("ISHPBILL", rst.getString(21));
                                                            } else {
                                                                  iinvoicedetails.setColumn("ISHPBILL", "");
                                                            }
                                                            iinvoicedetails.setColumn("IFORNO", rst.getString(22));
                                                            iinvoicedetails.setSERINO(String.valueOf(j));
                                                            iinvoicedetails.setNewKey();
                                                            iinvoicedetails.setFk(fkey);
                                                            iinvoicedetails.setSequence(count);

                                                            ExtEventInvoicedetailsLCEntityWrapper invoicewrapper = new ExtEventInvoicedetailsLCEntityWrapper(
                                                                        iinvoicedetails, getDriverWrapper());
                                                            addNewExtEventInvoicedetailsLC(invoicewrapper);

                                                            count++;

                                                      }



                                          
                                          }
                              else if (billnum.length() > 0&&formNO.length()<=0) {
                                    
                                     String billnum_b = sdwrapper2.getLCBILLNO().trim();
                                     String portcode_b = sdwrapper2.getLCPORTCODE();
                                     String shidate = sdwrapper2.getLCBILDAT();
                                     System.out.println("portcode"+portcode_b);
                                     System.out.println("billnum_b"+billnum_b);
                                          // query = "select DISTINCT
                                          // invserialno,invno,TO_CHAR(TO_DATE(invdate),'DD/MM/YY')
                                          // AS
                                          // invdate,fobamt,fobcurrcode,frieghtamt,frieghtcurrcode,insamt,inscurrcode,commamt,commcurrcode,disamt,discurrcode,dedamt,dedcurrcode,pkgamt,pkgcurrcode,shipbillno,TO_CHAR(TO_DATE(shipbilldate,'ddmmyyyy'),'DD/MM/YY'),portcode,shipbillno,fileno
                                          // from ett_edpms_shp_inv where shipbillno='"
                                          query = "SELECT invserialno, invno, TO_CHAR(TO_DATE(invdate,'dd-mm-yyyy'),'yyyy-mm-dd') AS INDATE, fobamt, fobcurrcode, NVL(frieghtamt,0), NVL(TRIM(frieghtcurrcode),fobcurrcode), NVL(insamt,0), NVL(TRIM(inscurrcode),fobcurrcode), NVL(commamt,0), NVL(TRIM(commcurrcode), fobcurrcode), NVL(disamt,0), NVL(TRIM(discurrcode), fobcurrcode), NVL(dedamt,0), NVL(TRIM(dedcurrcode), fobcurrcode), NVL(pkgamt,0), NVL(TRIM(pkgcurrcode), fobcurrcode), shipbillno, TO_CHAR(TO_DATE(shipbilldate,'ddmmyyyy'),'DD/MM/YY'), portcode, shipbillno, fileno FROM ett_edpms_shp_inv WHERE shipbillno='"
                                                      + billnum_b + "' AND SHIPBILLDATE = to_char(to_date('" + shidate
                                                      + "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE='" + portcode_b + "'";
                                          //// Loggers.general().info(LOG,"Invoice bill fecting==>" +
                                          //// query);
                                          System.out.println("Invoice bill fecting==>" + query);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Invoice bill fecting==>" + query);
                                          }
                                          try {
                                              if (ps != null)
                                                ps.close();
                                              if (rst2 != null)
                                                rst2.close();
                                              rst2 = null;
                                          } catch (Exception e) {
                                              System.out.println("Exception Query value Invoice form no" + e.getMessage());
                                          }
                                          ResultSet rst2 = null;
                                          ps = connection.prepareStatement(query);
                                          rst2 = ps.executeQuery();
                                          while (rst2.next()) {
                                                Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                                                ExtEventInvoicedetailsLC iinvoicedetails = new ExtEventInvoicedetailsLC();
                                                iinvoicedetails.setColumn("INVSRNO", rst2.getString(1));
                                                iinvoicedetails.setColumn("INVNO", rst2.getString(2));
                                                iinvoicedetails.setColumn("INVDATE", rst2.getString(3));
                                                System.out.println("invdate---" + iinvoicedetails.getColumn("INVDATE"));
                                              System.out.println("invoice number " + rst2.getString(2));
                                                iinvoicedetails.setColumn("IFOBAMT",
                                                            getDriverWrapper().convertFromToDBFormat(rst2.getString(4), rst2.getString(5),
                                                                        "T") + rst2.getString(5));
                                                iinvoicedetails.setColumn("CCY", rst2.getString(5));
                                                iinvoicedetails.setColumn("INVFRAMT",
                                                            getDriverWrapper().convertFromToDBFormat(rst2.getString(6), rst2.getString(7),
                                                                        "T") + rst2.getString(7));
                                                iinvoicedetails.setColumn("CCY_1", rst2.getString(7));
                                                iinvoicedetails.setColumn("INSUAMT",
                                                            getDriverWrapper().convertFromToDBFormat(rst2.getString(8), rst2.getString(9),
                                                                        "T") + rst2.getString(9));
                                                iinvoicedetails.setColumn("CCY_2", rst2.getString(9));
                                                iinvoicedetails.setColumn("ICOMMAMT",
                                                            getDriverWrapper().convertFromToDBFormat(rst2.getString(10), rst2.getString(11),
                                                                        "T") + rst2.getString(11));
                                                iinvoicedetails.setColumn("CCY_3", rst2.getString(11));
                                                iinvoicedetails.setColumn("IDISCAMT",
                                                            getDriverWrapper().convertFromToDBFormat(rst2.getString(12), rst2.getString(13),
                                                                        "T") + rst2.getString(13));
                                                iinvoicedetails.setColumn("CCY_4", rst2.getString(13));
                                                iinvoicedetails.setColumn("IDEDUAMT",
                                                            getDriverWrapper().convertFromToDBFormat(rst2.getString(14), rst2.getString(15),
                                                                        "T") + rst2.getString(15));
                                                //// Loggers.general().info(LOG,"PORTCODE" +
                                                //// rst.getString(3));
                                                iinvoicedetails.setColumn("CCY_5", rst2.getString(15));
                                                iinvoicedetails.setColumn("IPKGAMT",
                                                            getDriverWrapper().convertFromToDBFormat(rst2.getString(16), rst2.getString(17),
                                                                        "T") + rst2.getString(17));
                                                iinvoicedetails.setColumn("CCY_6", rst2.getString(17));
                                                iinvoicedetails.setColumn("INVPRTCD", rst2.getString(20));
                                                if (billnum.length() > 0) {
                                                      // ////Loggers.general().info(LOG,"ISHPBILL number
                                                      // else");
                                                      iinvoicedetails.setColumn("ISHPBILL", rst2.getString(21));
                                                } else {
                                                      iinvoicedetails.setColumn("ISHPBILL", "");
                                                }
                                                iinvoicedetails.setSERINO(String.valueOf(j));
                                                System.out.println(" ISHPBILL "+rst2.getString(21));
                                                iinvoicedetails.setNewKey();
                                                iinvoicedetails.setFk(fkey);
                                                iinvoicedetails.setSequence(count);

                                                ExtEventInvoicedetailsLCEntityWrapper invoicewrapper = new ExtEventInvoicedetailsLCEntityWrapper(
                                                            iinvoicedetails, getDriverWrapper());
                                                addNewExtEventInvoicedetailsLC(invoicewrapper);

                                                count++;

                                          }

                              /*    } else {
                                          //// Loggers.general().info(LOG,"Count value Invoice bill
                                          //// else<===count_ODC===>" + count_ODC + "<>" +
                                          //// count2);
                                    }*/
                              } else if (formNO.length() > 0 && billnum.length()<=0) {


                                     String portcode_f = sdwrapper2.getLCPORTCODE();
                                          String formNO_f = sdwrapper2.getLCFORMNO().trim();
                                          String shidate = sdwrapper2.getLCBILDAT();
                                          System.out.println("portcode"+portcode_f);
                                          System.out.println("formNO_f"+formNO_f);
                                          query = "SELECT invserialno, invno, TO_CHAR(TO_DATE(invdate),'yyyy-mm-dd') AS invdate, fobamt, fobcurrcode, NVL(frieghtamt,0), NVL(TRIM(frieghtcurrcode),fobcurrcode), NVL(insamt,0), NVL(TRIM(inscurrcode),fobcurrcode), NVL(commamt,0), NVL(TRIM(commcurrcode), fobcurrcode), NVL(disamt,0), NVL(TRIM(discurrcode), fobcurrcode), NVL(dedamt,0), NVL(TRIM(dedcurrcode), fobcurrcode), NVL(pkgamt,0), NVL(TRIM(pkgcurrcode), fobcurrcode), fileno, TO_CHAR(TO_DATE(shipbilldate,'ddmmyyyy'),'DD/MM/YY') AS SHIPBILLDATE, portcode, shipbillno, formno FROM ett_edpms_shp_inv_softex WHERE formno='"
                                                      + formNO_f + "' AND SHIPBILLDATE = to_char(to_date('" + shidate
                                                      + "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE='" + portcode_f + "'";
                                          //// Loggers.general().info(LOG,"Softex in Invoice form no
                                          //// for fetching" + query);
                                          System.out.println("Query value Invoice form no" + query);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Softex in Invoice form no for fetching" + query);
                                          }
                                          try {
                                              if (ps != null)
                                                ps.close();
                                              if (rst != null)
                                                rst.close();
                                              rst = null;
                                          } catch (Exception e) {
                                              System.out.println(" Exception Invoice form no------>" + e.getMessage());
                                          }
                                          ps = connection.prepareStatement(query);
                                          rst = ps.executeQuery();
                                          while (rst.next()) {
                                                Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                                                ExtEventInvoicedetailsLC iinvoicedetails = new ExtEventInvoicedetailsLC();
                                                iinvoicedetails.setColumn("INVSRNO", rst.getString(1));
                                                iinvoicedetails.setColumn("INVNO", rst.getString(2));
                                                iinvoicedetails.setColumn("INVDATE", rst.getString(3));
                                                 System.out.println("invdate---" + iinvoicedetails.getColumn("INVDATE"));
                                                    System.out.println("date" + rst.getString(3));

                                                //// Loggers.general().info(LOG,"INVDATE--->" +
                                                //// rst.getString(3));
                                                iinvoicedetails.setColumn("IFOBAMT",
                                                            getDriverWrapper().convertFromToDBFormat(rst.getString(4), rst.getString(5),
                                                                        "T") + rst.getString(5));
                                                iinvoicedetails.setColumn("CCY", rst.getString(5));
                                                iinvoicedetails.setColumn("INVFRAMT",
                                                            getDriverWrapper().convertFromToDBFormat(rst.getString(6), rst.getString(7),
                                                                        "T") + rst.getString(7));
                                                iinvoicedetails.setColumn("CCY_1", rst.getString(7));
                                                iinvoicedetails.setColumn("INSUAMT",
                                                            getDriverWrapper().convertFromToDBFormat(rst.getString(8), rst.getString(9),
                                                                        "T") + rst.getString(9));
                                                iinvoicedetails.setColumn("CCY_2", rst.getString(9));
                                                iinvoicedetails.setColumn("ICOMMAMT",
                                                            getDriverWrapper().convertFromToDBFormat(rst.getString(10), rst.getString(11),
                                                                        "T") + rst.getString(11));
                                                iinvoicedetails.setColumn("CCY_3", rst.getString(11));
                                                iinvoicedetails.setColumn("IDISCAMT",
                                                            getDriverWrapper().convertFromToDBFormat(rst.getString(12), rst.getString(13),
                                                                        "T") + rst.getString(13));
                                                iinvoicedetails.setColumn("CCY_4", rst.getString(13));
                                                iinvoicedetails.setColumn("IDEDUAMT",
                                                            getDriverWrapper().convertFromToDBFormat(rst.getString(14), rst.getString(15),
                                                                        "T") + rst.getString(15));
                                                iinvoicedetails.setColumn("CCY_5", rst.getString(15));
                                                iinvoicedetails.setColumn("IPKGAMT",
                                                            getDriverWrapper().convertFromToDBFormat(rst.getString(16), rst.getString(17),
                                                                        "T") + rst.getString(17));
                                                iinvoicedetails.setColumn("CCY_6", rst.getString(17));
                                                iinvoicedetails.setColumn("INVPRTCD", rst.getString(20));

                                                iinvoicedetails.setColumn("ISHPBILL", "");

                                                iinvoicedetails.setColumn("IFORNO", rst.getString(22));
                                                iinvoicedetails.setSERINO(String.valueOf(j));
                                                iinvoicedetails.setNewKey();
                                                iinvoicedetails.setFk(fkey);
                                                iinvoicedetails.setSequence(count);

                                                ExtEventInvoicedetailsLCEntityWrapper invoicewrapper = new ExtEventInvoicedetailsLCEntityWrapper(
                                                            iinvoicedetails, getDriverWrapper());
                                                addNewExtEventInvoicedetailsLC(invoicewrapper);

                                                count++;

                                          }

                        
                              }
                              else
                              {
                                    System.out.println("Not in any else=====>");
                              }
                              
                        }
                        try {
                              if (connection != null)
                                    connection.close();
                        } catch (Exception e) {
                              e.getMessage();
                        }
                  
                  }
                  if (liste.getSize().intValue() > 0)
                        getBtnFetchInvdetELCDPclay().setEnabled(false);

            } catch (Exception e) {
                  //// Loggers.general().info(LOG,"ELC invoice value exception " +
                  //// e.getMessage()); e.printStackTrace();

                System.out.println("Exception invoicedetails" + e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"ELC invoice value exception " + e.getMessage());
                  }
            } finally {
                   try {
                              if (rst != null)
                                  rst.close();
                              if (pst != null)
                                  pst.close();
                              if (rs != null)
                                  rs.close();
                              if (ps != null)
                                  ps.close();
                              if (rst2 != null)
                                  rst2.close();
                              if (con != null)
                                  con.close();
                            }catch (SQLException e) {
                        //// Loggers.general().info(LOG,"Connection Failed! Check output
                        //// console");
                        e.printStackTrace();
                  }
            }

      }

      public void onFetchShippingDetailsELCDPclayButton() {
            String customer = getDriverWrapper().getEventFieldAsText("BEN", "p", "cu");
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
                  EnigmaArray<ExtEventShippingTableEntityWrapper> liste = getExtEventShippingTableData();
                  Iterator<ExtEventShippingTableEntityWrapper> iterator = liste.iterator();
                  int count = 0;
                  int j = 0;
                  String iecode = "";
                  String query = "";
                  con = ConnectionMaster.getConnection();
                  for (int i = 0; i < liste.getSize().intValue(); i++) {
                        while (iterator.hasNext()) {

                              ExtEventShippingTableEntityWrapper val1 = (ExtEventShippingTableEntityWrapper) iterator
                                          .next();
                              String shippingBillno = val1.getBILLNUM();
                              String formNumber = val1.getFORMNUM();
                              String notional =val1.getNOTIONAL();
                              BigDecimal sbAmtTotal = new BigDecimal(0);
                              BigDecimal fobamtbd = new BigDecimal(0);
                              BigDecimal freightbdbd = new BigDecimal(0);
                              BigDecimal insubd = new BigDecimal(0);
                              BigDecimal notionalbd = new BigDecimal(notional);
                              String freightcurr="";
                              String insucurr="";
                              String fobamt="";
                              String freight="";
                              String insurance="";
                              // String iecode = val1.getCIECOD();
                              System.out.println("SHIPPING bill no :" + shippingBillno + " " + formNumber+" "+notional);
                              String query1 = "SELECT IECODE  FROM EXTCUST where CUST = '" + customer + "'";
                              
                              ps1 = con.prepareStatement(query1);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    iecode = rs1.getString(1);
                                    val1.setIECOD(iecode);
                              }
                              if (shippingBillno != null && !shippingBillno.equalsIgnoreCase("")) {
                                    query = "SELECT shp.shipbillno,TO_CHAR(TO_DATE(shp.shipbilldate,'ddmmyyyy'),'yy-mm-dd') AS shipbilldate,shp.portcode, shp.formno,shp.iecode,"
                                                + " TO_CHAR(sum(inv.fobamt) , 'fm99999999999990.00'),inv.FOBCURRCODE,TO_CHAR(sum(inv.FRIEGHTAMT) , 'fm99999999999990.00'), "
                                                + " TO_CHAR(sum(inv.INSAMT) , 'fm99999999999990.00'),  "
                                                + "INV.FRIEGHTCURRCODE,INV.INSCURRCODE "
                                                + "FROM ett_edpms_shp shp,ett_edpms_shp_inv inv WHERE "
                                                + "inv.shipbillno = shp.shipbillno " + "and shp.PORTCODE=inv.PORTCODE "
                                                + "AND shp.shipbillno ='" + shippingBillno + "' group by  INV.FRIEGHTCURRCODE,INV.INSCURRCODE,inv.FOBCURRCODE, "
                                                 +"shp.shipbillno,shp.shipbilldate,shp.portcode, shp.formno,shp.iecode";
                              }
                              if (formNumber != null && !formNumber.equalsIgnoreCase("")) {
                                    query = "SELECT ESP.shipbillno,TO_CHAR(TO_DATE(ESP.shipbilldate,'ddmmyyyy'),'yy-mm-dd') AS shipbilldate,ESP.portcode, ESP.formno,ESP.iecode, "
                                                + " TO_CHAR(sum(ein.fobamt ), 'fm99999999999990.00'),ein.FOBCURRCODE,TO_CHAR(sum(ein.FRIEGHTAMT) , 'fm99999999999990.00'), "
                                                + " TO_CHAR(sum(ein.INSAMT) , 'fm99999999999990.00'),  "
                                                + " ein.FRIEGHTCURRCODE,ein.INSCURRCODE "
                                                + "FROM ETT_EDPMS_FILES EEF,ETT_EDPMS_SHP_SOFTEX ESP,ETT_EDPMS_SHP_INV_SOFTEX EIN "
                                                + "WHERE EEF.FILENO      = ESP.FILENO " + "AND ESP.FORMNO        = EIN.FORMNO "
                                                + "AND ESP.SHIPBILLDATE  = EIN.SHIPBILLDATE " + "AND ESP.PORTCODE      = EIN.PORTCODE "
                                                + "AND TRIM(ESP.FORMNO)||TRIM(ESP.SHIPBILLDATE)||TRIM(ESP.PORTCODE) "
                                                + "NOT IN (SELECT TRIM(EDT.FORMNO)||TRIM(EDT.SHIPBILLDATE)||TRIM(EDT.PORTCODE) "
                                                + "FROM ETT_GR_SHP_TBL EDT WHERE TRIM(EDT.SHIPBILLNO) IS NULL) AND ESP.FORMNO ='"
                                                + formNumber + "' group by  ein.FRIEGHTCURRCODE,ein.INSCURRCODE,ein.FOBCURRCODE, "
                                                +" ESP.shipbillno,esp.shipbilldate,ESP.portcode, ESP.formno,ESP.iecode";
                              }
                              // + "AND shp.iecode ='"+iecode+"' " ;
                              System.out.println("SQL QUERY: " + query);
                        //    con = ConnectionMaster.getConnection();
                              pst = con.prepareStatement(query);
                              rst = pst.executeQuery();
                              j = j + 1;
                              while (rst.next()) {
                                    // Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                                    // ExtEventShippingCollections shippingdetails = new
                                    // ExtEventShippingCollections();
                                    if (iecode.equalsIgnoreCase(rst.getString(5))) {
                                          val1.setBILLDAT(rst.getString(2));
                                          val1.setPORTCODDD(rst.getString(3));
                                          // val1.setCFORMN(rst.getString(4));
                                          val1.setREPAYAM("0.00" + " " + rst.getString(7));
                                          val1.setSHCOLAM("0.00" + " " + rst.getString(7));
                                          val1.setSERIALNU(String.valueOf(j));
                                          fobamt=rst.getString(6);
                                          freight=rst.getString(8);
                                          insurance=rst.getString(9);
                                          
                                          fobamtbd = new BigDecimal(fobamt);
                                           freightbdbd = new BigDecimal(freight);
                                           insubd = new BigDecimal(insurance);
                                          sbAmtTotal=fobamtbd;
                                          freightcurr=rst.getString(10);
                                          insucurr=rst.getString(11);
                                          System.out.println("inside while SHIPPING bill no :" + fobamt + " " + freight+" "+insurance+" "+fobamtbd+" "+insubd);
                                          if (  freight != null &&!freight.equalsIgnoreCase("")&& freightcurr != null && !freightcurr.equalsIgnoreCase("") ) {
                                                if(freightcurr.equalsIgnoreCase("INR")) {
                                                      System.out.println("inside of freight currency :" + freightcurr + " " + freight+" "+insurance+" "+fobamtbd+" "+freightbdbd);
                                                      freightbdbd=freightbdbd.divide(notionalbd,4, RoundingMode.HALF_UP);
                                                }
                                                else {
                                                      freightbdbd=freightbdbd;
                                                }
                                                
                                                sbAmtTotal = sbAmtTotal.add(freightbdbd);
                                          }
                                          if ( insurance != null &&!insurance.equalsIgnoreCase("") &&  insucurr != null && !insucurr.equalsIgnoreCase("") ) {
                                                if(insucurr.equalsIgnoreCase("INR")) {
                                                      insubd=insubd.divide(notionalbd,4, RoundingMode.HALF_UP);
                                                                  
                                                }
                                                else {
                                                      insubd=insubd;
                                                }
                                                sbAmtTotal = (sbAmtTotal.add(insubd).setScale(2,BigDecimal.ROUND_HALF_EVEN));
                                          }
//                                        if (  rst.getString(13) != null && !rst.getString(13).equalsIgnoreCase("")) {
//                                              sbAmtTotal = sbAmtTotal.add(new BigDecimal(rst.getString(13)));
//                                        }
//                                        if ( rst.getString(10) != null  &&  !rst.getString(10).equalsIgnoreCase("") ) {
//                                              sbAmtTotal = sbAmtTotal.subtract(new BigDecimal(rst.getString(10)));
//                                        }
//                                        if ( rst.getString(11) != null  &&  !rst.getString(11).equalsIgnoreCase("") ) {
//                                              sbAmtTotal = sbAmtTotal.subtract(new BigDecimal(rst.getString(11)));
//                                        }
//                                        if ( rst.getString(12) != null  &&  !rst.getString(12).equalsIgnoreCase("") ) {
//                                              sbAmtTotal = sbAmtTotal.subtract(new BigDecimal(rst.getString(12)));
//                                        }
                        //                sbAmtTotal = sbAmtTotal.multiply(new BigDecimal(100));
                                          val1.setSHPAMT(sbAmtTotal + " "+ rst.getString(7));

                                          // val1.setCIECOD(rst.getString(5));
                                          System.out.println("SHIPPING GRID DETAILS " + String.valueOf(j) + " " + sbAmtTotal + " ");
                                          // shippingdetails.setNewKey();
                                          // shippingdetails.setFk(fkey);
                                          // shippingdetails.setSequence(count);

                                          // ExtEventShippingCollectionsEntityWrapper invoicewrapper = new
                                          // ExtEventShippingCollectionsEntityWrapper(
                                          // shippingdetails, getDriverWrapper());
                                          // addNewExtEventShippingCollections(invoicewrapper);

                                          // count++;
                                          // if (liste.getSize().intValue() > 0)

                                          // getBtnFetchShippingDetailsEXPBILLclay().setEnabled(false);
                                    }
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
      public void oncalcELCSETTclayButton() {

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

            String lc = "";
            String nostroC = "";
            String nostroamt = "";
            double nosrat = 0.0;
            double lcrat = 0.0;
            double nosamdb = 0.0;
            String convrate1 = "";
            String finlnos = "";
            String val1 = "";
            //convrate1 = getCONVRAT();
            Double convrate = 0.0;//Double.valueOf(String.valueOf(nullpointer(getCONVRAT())));
            //// Loggers.general().info(LOG,"conversion rate" + convrate);
            String lccur = getDriverWrapper().getEventFieldAsText("AMPR", "v", "c");
            //// Loggers.general().info(LOG,"inward remittance currency" + lccur);

            String val = getNOSAMT();
            //// Loggers.general().info(LOG,"val length--------->" + val.length());
            if (dailyval_Log.equalsIgnoreCase("YES")) {
                  Loggers.general().info(LOG,"val length--------->" + val.length());
            }
            if (val.length() > 0) {
                  String val2 = val.substring(0, val.length() - 3).trim();
                  //// Loggers.general().info(LOG,"nostro amount" + val + "val
                  //// length--------->" + val.length());
                  finlnos = val2.replaceAll("^0-9", "");
                  //// Loggers.general().info(LOG,"final nostro value" + finlnos);
                  val1 = val.substring(val.length() - 3, val.length());
                  //// Loggers.general().info(LOG,"val1 length" + val1);
            }
            try {
                  con = ConnectionMaster.getConnection();
                  String query = "select trim(SPOTRATE) from spotrate where currency='" + lccur + "'";
                  //// Loggers.general().info(LOG,"Query value is query " + query);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Query value is query " + query);
                  }

                  ps = con.prepareStatement(query);

                  rs = ps.executeQuery();

                  while (rs.next()) {

                        lc = rs.getString(1);
                        lcrat = Double.valueOf(lc);
                        //// Loggers.general().info(LOG,"lc currency is " + lcrat);
                  }

                  String query1 = "select trim(SPOTRATE) from spotrate where currency='" + val1 + "'";
                  //// Loggers.general().info(LOG,"Query value is query1 " + query1);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Query value is query1 " + query1);
                  }
                  ps1 = con.prepareStatement(query1);
                  rs1 = ps1.executeQuery();

                  while (rs1.next()) {
                        nostroC = rs1.getString(1);
                        nosrat = Double.valueOf(nostroC);
                        //// Loggers.general().info(LOG,"nostro currency valuee is " + nostroC);
                  }

                  if (nosrat >= lcrat) {
                        //// Loggers.general().info(LOG,"nostro rate in loop1" + nosrat);
                        //// Loggers.general().info(LOG,"lc rate in loop1" + lcrat);
                        /*
                         * String val=getNOSAMT(); String
                         * val2=val.substring(0,val.length()-3).trim();
                         * ////Loggers.general().info(LOG,"nostro amount" +val );
                         */

                        nosamdb = Double.valueOf(nullpointer(finlnos));

                        //// Loggers.general().info(LOG,"NOSTRO AMT rate in loop 1" + nosamdb);
                        Double total = convrate * nosamdb;
                        //// Loggers.general().info(LOG,"total value in loop 1 " + total);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"total value in loop 1 " + total);
                        }

                        /*
                         * String val1= val.substring(val.length()-3,val.length());
                         * ////Loggers.general().info(LOG,"val1 length" + val1);
                         */
                        String total1 = String.valueOf(total);
                        setEQBLAMT(total1 + "" + lccur);
                        //// Loggers.general().info(LOG,"setEQBLAMT------>" + getEQBLAMT());

                  } else if (nosrat <= lcrat) {
                        //// Loggers.general().info(LOG,"nostro rate in loop2 " + nosrat);
                        //// Loggers.general().info(LOG,"lc rate in loop2" + lcrat);

                        nosamdb = Double.valueOf(nullpointer(finlnos));
                        //// Loggers.general().info(LOG,"NOSTRO AMT rate in loop2 " + nosamdb);
                        Double total = nosamdb / convrate;
                        //// Loggers.general().info(LOG,"total value in loop2 " + total);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"total value in loop2 " + total);
                        }

                        String total1 = String.valueOf(total);
                        setEQBLAMT(total1 + "" + lccur);
                        //// Loggers.general().info(LOG,"setEQBLAMT------>" + getEQBLAMT());
                  } else if ((finlnos.equalsIgnoreCase("")) && (convrate1.equalsIgnoreCase(""))) {

                        setEQBLAMT("");

                  } else {
                        //// Loggers.general().info(LOG,"finally of conversion rate
                        //// calculation");
                  }

            } catch (Exception e) {
                  //// Loggers.general().info(LOG,"exception in inward layout" +
                  //// e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"exception in inward layout" + e.getMessage());
                  }
            } finally {
                  try {

                        if (rs1 != null)
                              rs1.close();
                        if (ps1 != null)
                              ps1.close();
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

      public String nullpointer(String msg) {
            if (msg.trim().equalsIgnoreCase("") || msg == null) {
                  msg = "0.0";
            }
            return msg;
      }

      public void ononfetchELCDPclayButton() {
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

                  String inwnum = "";
                  double totalAmt = 0;
                  String balanceValCurrency = ""; // String
                  String balance = "0.0";
                  String creditcur = "";
                  String bank_ADCODE = "";
                  long creditAmount = 0;
                  long balanceAmt = 0;
                  String cif_no = "";
                  String masReference = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "");

                  try {
                        con = ConnectionMaster.getConnection();
                        EnigmaArray<ExtEventAdvanceTableEntityWrapper> liste = getExtEventAdvanceTableData();
                        for (int i = 0; i < liste.getSize().intValue(); i++) {
                              Iterator<ExtEventAdvanceTableEntityWrapper> iterator1 = liste.iterator();
                              while (iterator1.hasNext()) {
                                    ExtEventAdvanceTableEntityWrapper fdwarapper1 = (ExtEventAdvanceTableEntityWrapper) iterator1
                                                .next();
                                    inwnum = fdwarapper1.getINWARD().trim();
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Inward Remittance Number " + inwnum);
                                    }

                                    if (!inwnum.equalsIgnoreCase("")) {
                                          String inwardDetails = "SELECT TRIM(ORDCUS_CST), trim(TO_CHAR(value_date,'yyyy-mm-dd')), trim(REMITTER_COUNTRY), CREDIT_AMOUNT, REC_CCY, trim(CIF_NO),TRIM(BANK_ADCODE) FROM ETTV_INWARD_REMITTANCE_AMOUNT WHERE MAS_REF='"
                                                      + inwnum + "'";
                                          Loggers.general().info(LOG,"query for getting all fields in inward Remittance grid " + inwardDetails);
                                          pst = con.prepareStatement(inwardDetails);
                                          rs1 = pst.executeQuery();
                                          
                                          fdwarapper1.setINWARD(inwnum);//added as the user inputed value is encountered with space

                                          
                                          if (rs1.next()) {
                                                fdwarapper1.setNAMREM(rs1.getString(1));
                                                fdwarapper1.setDATREM(rs1.getString(2));
                                                
                                                Loggers.general().info(LOG,"date==>"+fdwarapper1.getDATREM());
                                                fdwarapper1.setCOUNREM(rs1.getString(3));
                                                creditAmount = rs1.getLong(4);
                                                creditcur = rs1.getString(5);
                                                cif_no = rs1.getString(6);

                                                fdwarapper1.setCUSCIFNO(cif_no);
                                                bank_ADCODE = rs1.getString(7);
                                                fdwarapper1.setADVRECB(bank_ADCODE);

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Credit AMount " + creditAmount);
                                                }
                                          } else {
                                                //// Loggers.general().info(LOG,"entered else since
                                                //// result set value returned nothing");
                                          }

                                          // String BalAmtQuery = "SELECT
                                          // ext.ccy_1,NVL(SUM(ext.amtutil),0) AS TOTAL_AMT"
                                          // + " FROM exteventadv ext, BASEEVENT bev, MASTER
                                          // mas WHERE ext.FK_EVENT = bev.EXTFIELD "
                                          // + " AND mas.KEY97 = bev.MASTER_KEY AND bev.STATUS
                                          // IN ('i','c') AND ext.inward ='"
                                          // + inwnum + "' AND mas.MASTER_REF !='" +
                                          // masReference + "' GROUP BY ext.ccy_1 ";

                                          String BalAmtQuery = "SELECT SUM(NVL(INWARDAMT,0)) AS INWARDAMOUNT FROM (SELECT SUM(NVL(ext.amtutil,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventadv ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c')  AND (mas.MASTER_REF ='"
                                                      + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
                                                      + eventCode + "') AND ext.INWARD ='" + inwnum
                                                      + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(NVL(ext.amtutil,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventadv ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
                                                      + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
                                                      + eventCode + "') AND ext.INWARD ='" + inwnum
                                                      + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(NVL(ext.amtutil,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventadv ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
                                                      + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) ='"
                                                      + eventCode + "') AND ext.INWARD ='" + inwnum
                                                      + "' GROUP BY mas.MASTER_REF ) INWARDAMOUNT";

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Query for getting Inward Utilized Amount===>" + BalAmtQuery);
                                          }
                                          pst = con.prepareStatement(BalAmtQuery);
                                          rs1 = pst.executeQuery();
                                          if (rs1.next()) {

                                                totalAmt = rs1.getDouble(1);

                                                balanceValCurrency = creditcur;

                                                double irmAmt = 0;
                                                String closureQuery = "SELECT SUM(IR.AMOUNT)*POWER(10,C8.C8CED) AS IRMAMT FROM ETT_IRM_CLOSURE_TBL IR,C8PF C8 "
                                                            + " WHERE IR.MASTER_REFNO = '" + inwnum + "' AND "
                                                            + " IR.CURRENCY = C8.C8CCY AND IR.STATUS IN ('TP','A') GROUP BY C8.C8CED ";

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"IRM Clourse Query ---" + closureQuery);
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
                                                      Loggers.general().info(LOG,"Balance Credit Amount-->" + balanceAmt);
                                                }
                                                fdwarapper1.setCUSCIFNO(cif_no);
                                                if (balanceAmt > 0) {

                                                      balance = String.valueOf(balanceAmt);
                                                      fdwarapper1.setBALANCE(balance + " " + balanceValCurrency);

                                                } else {
                                                      fdwarapper1.setBALANCE(0 + " " + balanceValCurrency);
                                                }
                                          } else {

                                                double irmAmt = 0;
                                                String closureQuery = "SELECT SUM(IR.AMOUNT)*POWER(10,C8.C8CED) AS IRMAMT FROM ETT_IRM_CLOSURE_TBL IR,C8PF C8 "
                                                            + " WHERE IR.MASTER_REFNO = '" + inwnum + "' AND "
                                                            + " IR.CURRENCY = C8.C8CCY AND IR.STATUS IN ('TP','A') GROUP BY C8.C8CED ";

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"IRM Clourse Query --->" + closureQuery);
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
                                                      Loggers.general().info(LOG,"Balance Credit Amount-->" + balan_cret);
                                                }

                                                if (balan_cret > 0) {
                                                      String balan_Str = String.valueOf(balan_cret);
                                                      fdwarapper1.setBALANCE(balan_Str + " " + creditcur);

                                                } else {
                                                      fdwarapper1.setBALANCE(0 + " " + creditcur);
                                                }

                                                fdwarapper1.setCUSCIFNO(cif_no);

                                          }
                                    } else {
                                          //// Loggers.general().info(LOG,"entered else since there is
                                          //// no Inward remittance no ");

                                    }

                              }
                        }

                  } catch (Exception e) {

                        //// Loggers.general().info(LOG,"Inward remittance excepton" +
                        //// e.getMessage());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception Inward remittance" + e.getMessage());
                        }

                  }

            } catch (Exception e) {

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Exception in Inward remittance ELCDOCLAYOUTButton" + e.getMessage());
                  }
            }

            finally {
                  try {
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
      }

      public void ononfetchELCSETTclayButton() {
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

                  String inwnum = "";
                  double totalAmt = 0;
                  String balanceValCurrency = ""; // String
                  String balance = "0.0";
                  String creditcur = "";
                  String bank_ADCODE = "";
                  long creditAmount = 0;
                  long balanceAmt = 0;
                  String cif_no = "";
                  String masReference = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                  String fircNumber = "";
                String tempcountry = "";
                String amtutil = "0.0";
                long utilamt = 0;
                String rbiCode="";
                  try {
                        con = ConnectionMaster.getConnection();
                        EnigmaArray<ExtEventAdvanceTableEntityWrapper> liste = getExtEventAdvanceTableData();
                        for (int i = 0; i < liste.getSize().intValue(); i++) {
                              Iterator<ExtEventAdvanceTableEntityWrapper> iterator1 = liste.iterator();
                              while (iterator1.hasNext()) {
                                    ExtEventAdvanceTableEntityWrapper fdwarapper1 = (ExtEventAdvanceTableEntityWrapper) iterator1
                                                .next();
                                    inwnum = fdwarapper1.getINWARD().trim();
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Inward Remittance Number " + inwnum);
                                    }
                                    if (!inwnum.equalsIgnoreCase("")) {
                                        String rbiquery = "SELECT PURCODE FROM ETTV_INWARD_REMITTANCE_AMOUNT WHERE MAS_REF='"
                                              + inwnum + "'";
                                        pst = con.prepareStatement(rbiquery);
                                        rs1 = pst.executeQuery();
                                        if (rs1.next()) {
                                           System.out.println("RBI Purpose code " + rs1.getString(1));
                                          rbiCode=rs1.getString(1);
                                        }
                                    }
                                    if(rbiCode.equalsIgnoreCase("P0103")) {
                                    if (!inwnum.equalsIgnoreCase("")) {
                                          String inwardDetails = "SELECT TRIM(ORDCUS_CST), trim(TO_CHAR(value_date,'DD/MM/YY')), trim(REMITTER_COUNTRY), CREDIT_AMOUNT, REC_CCY, trim(CIF_NO),TRIM(BANK_ADCODE) FROM ETTV_INWARD_REMITTANCE_AMOUNT WHERE MAS_REF='"
                                                      + inwnum + "'";
                                          Loggers.general().info(LOG,"query for getting all fields in inward Remittance grid " + inwardDetails);
                                          pst = con.prepareStatement(inwardDetails);
                                          rs1 = pst.executeQuery();
                                          fdwarapper1.setINWARD(inwnum);//added as the user inputed value is encountered with space

                                          if (rs1.next()) {
                                                fdwarapper1.setNAMREM(rs1.getString(1));
                                                fdwarapper1.setDATREM(rs1.getString(2));
                                                fdwarapper1.setCOUNREM(rs1.getString(3));
                                                creditAmount = rs1.getLong(4);
                                                creditcur = rs1.getString(5);
                                                cif_no = rs1.getString(6);

                                                fdwarapper1.setCUSCIFNO(cif_no);
                                                bank_ADCODE = rs1.getString(7);
                                                fdwarapper1.setADVRECB(bank_ADCODE);

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Credit AMount " + creditAmount);
                                                }
                                          } else {
                                                //// Loggers.general().info(LOG,"entered else since
                                                //// result set value returned nothing");
                                          }

                                          // String BalAmtQuery = "SELECT
                                          // ext.ccy_1,NVL(SUM(ext.amtutil),0) AS TOTAL_AMT"
                                          // + " FROM exteventadv ext, BASEEVENT bev, MASTER
                                          // mas WHERE ext.FK_EVENT = bev.EXTFIELD "
                                          // + " AND mas.KEY97 = bev.MASTER_KEY AND bev.STATUS
                                          // IN ('i','c') AND ext.inward ='"
                                          // + inwnum + "' AND mas.MASTER_REF !='" +
                                          // masReference + "' GROUP BY ext.ccy_1 ";

                                          String BalAmtQuery = "SELECT SUM(NVL(INWARDAMT,0)) AS INWARDAMOUNT FROM (SELECT SUM(NVL(ext.amtutil,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventadv ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c')  AND (mas.MASTER_REF ='"
                                                      + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
                                                      + eventCode + "') AND ext.INWARD ='" + inwnum
                                                      + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(NVL(ext.amtutil,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventadv ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
                                                      + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
                                                      + eventCode + "') AND ext.INWARD ='" + inwnum
                                                      + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(NVL(ext.amtutil,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventadv ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
                                                      + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) ='"
                                                      + eventCode + "') AND ext.INWARD ='" + inwnum
                                                      + "' GROUP BY mas.MASTER_REF ) INWARDAMOUNT";

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Query for getting Inward Utilized Amount===>" + BalAmtQuery);
                                          }
                                          pst = con.prepareStatement(BalAmtQuery);
                                          rs1 = pst.executeQuery();
                                          if (rs1.next()) {

                                                totalAmt = rs1.getDouble(1);

                                                balanceValCurrency = creditcur;

                                                double irmAmt = 0;
                                                String closureQuery = "SELECT SUM(IR.AMOUNT)*POWER(10,C8.C8CED) AS IRMAMT FROM ETT_IRM_CLOSURE_TBL IR,C8PF C8 "
                                                            + " WHERE IR.MASTER_REFNO = '" + inwnum + "' AND "
                                                            + " IR.CURRENCY = C8.C8CCY AND IR.STATUS IN ('TP','A') GROUP BY C8.C8CED ";

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"IRM Clourse Query ---" + closureQuery);
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
                                                      Loggers.general().info(LOG,"Balance Credit Amount-->" + balanceAmt);
                                                }
                                                fdwarapper1.setCUSCIFNO(cif_no);
                                                if (balanceAmt > 0) {

                                                      balance = String.valueOf(balanceAmt);
                                                      fdwarapper1.setBALANCE(balance + " " + balanceValCurrency);

                                                } else {
                                                      fdwarapper1.setBALANCE(0 + " " + balanceValCurrency);
                                                }
                                          } else {

                                                double irmAmt = 0;
                                                String closureQuery = "SELECT SUM(IR.AMOUNT)*POWER(10,C8.C8CED) AS IRMAMT FROM ETT_IRM_CLOSURE_TBL IR,C8PF C8 "
                                                            + " WHERE IR.MASTER_REFNO = '" + inwnum + "' AND "
                                                            + " IR.CURRENCY = C8.C8CCY AND IR.STATUS IN ('TP','A') GROUP BY C8.C8CED ";

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"IRM Clourse Query --->" + closureQuery);
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
                                                      Loggers.general().info(LOG,"Balance Credit Amount-->" + balan_cret);
                                                }

                                                if (balan_cret > 0) {
                                                      String balan_Str = String.valueOf(balan_cret);
                                                      fdwarapper1.setBALANCE(balan_Str + " " + creditcur);

                                                } else {
                                                      fdwarapper1.setBALANCE(0 + " " + creditcur);
                                                }

                                    //          fdwarapper1.setCUSCIFNO(cif_no);

                                          }
                                    } else {
                                          //// Loggers.general().info(LOG,"entered else since there is
                                          //// no Inward remittance no ");

                                    }
                              }
                              }
                        }

                  } catch (Exception e) {

                        //// Loggers.general().info(LOG,"Inward remittance excepton" +
                        //// e.getMessage());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception Inward remittance" + e.getMessage());
                        }

                  }

            } catch (Exception e) {

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Exception in Inward remittance ELCPAYLAYOUTButton" + e.getMessage());
                  }
            }

            finally {
                  try {
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
      }

      public void ononfetchEXPSTDLCOUTPREclayButton() {
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

                  String inwnum = "";
                  double totalAmt = 0;
                  String balanceValCurrency = ""; // String
                  String balance = "0.0";
                  String creditcur = "";
                  String bank_ADCODE = "";
                  long creditAmount = 0;
                  long balanceAmt = 0;
                  String cif_no = "";
                  String master = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
                  try {
                        con = ConnectionMaster.getConnection();
                        EnigmaArray<ExtEventAdvanceTableEntityWrapper> liste = getExtEventAdvanceTableData();
                        for (int i = 0; i < liste.getSize().intValue(); i++) {
                              Iterator<ExtEventAdvanceTableEntityWrapper> iterator1 = liste.iterator();
                              while (iterator1.hasNext()) {
                                    ExtEventAdvanceTableEntityWrapper fdwarapper1 = (ExtEventAdvanceTableEntityWrapper) iterator1
                                                .next();
                                    inwnum = fdwarapper1.getINWARD().trim();
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Inward Remittance Number " + inwnum);
                                    }

                                    if (!inwnum.equalsIgnoreCase("")) {
                                          String inwardDetails = "SELECT TRIM(ORDCUS_CST), trim(TO_CHAR(value_date,'DD/MM/YY')), trim(REMITTER_COUNTRY), CREDIT_AMOUNT, CREDIT_CURRENCY, trim(CIF_NO),TRIM(BANK_ADCODE) FROM ETTV_INWARD_REMITTANCE_AMOUNT WHERE MAS_REF='"
                                                      + inwnum + "'";
                                          Loggers.general().info(LOG,"query for getting all fields in inward Remittance grid " + inwardDetails);
                                          pst = con.prepareStatement(inwardDetails);
                                          rs1 = pst.executeQuery();
                                          fdwarapper1.setINWARD(inwnum);//added as the user inputed value is encountered with space

                                          if (rs1.next()) {
                                                fdwarapper1.setNAMREM(rs1.getString(1));
                                                fdwarapper1.setDATREM(rs1.getString(2));
                                                fdwarapper1.setCOUNREM(rs1.getString(3));
                                                creditAmount = rs1.getLong(4);
                                                creditcur = rs1.getString(5);
                                                cif_no = rs1.getString(6);

                                                fdwarapper1.setCUSCIFNO(cif_no);
                                                bank_ADCODE = rs1.getString(7);
                                                fdwarapper1.setADVRECB(bank_ADCODE);

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Credit AMount " + creditAmount);
                                                }
                                          } else {
                                                //// Loggers.general().info(LOG,"entered else since
                                                //// result set value returned nothing");
                                          }

                                          String BalAmtQuery = "SELECT ext.ccy_1,NVL(SUM(ext.amtutil),0) AS TOTAL_AMT"
                                                      + " FROM exteventadv ext, BASEEVENT bev, MASTER mas WHERE ext.FK_EVENT = bev.EXTFIELD "
                                                      + " AND mas.KEY97 = bev.MASTER_KEY AND bev.STATUS IN ('i','c') AND mas.MASTER_REF !='"
                                                      + master + "' AND  ext.inward ='" + inwnum + "' GROUP BY ext.ccy_1 ";
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"Query for getting Total Utilized Amt " + BalAmtQuery);
                                          }
                                          pst = con.prepareStatement(BalAmtQuery);
                                          rs1 = pst.executeQuery();
                                          if (rs1.next()) {

                                                totalAmt = rs1.getDouble(2);
                                                balanceValCurrency = rs1.getString(1);
                                                if (null == balanceValCurrency || balanceValCurrency.equalsIgnoreCase("")) {
                                                      balanceValCurrency = creditcur;
                                                }

                                                double irmAmt = 0;
                                                String closureQuery = "SELECT SUM(IR.AMOUNT)*POWER(10,C8.C8CED) AS IRMAMT FROM ETT_IRM_CLOSURE_TBL IR,C8PF C8 "
                                                            + " WHERE IR.MASTER_REFNO = '" + inwnum + "' AND "
                                                            + " IR.CURRENCY = C8.C8CCY AND IR.STATUS IN ('TP','A') GROUP BY C8.C8CED ";

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"IRM Clourse Query ---" + closureQuery);
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
                                                      Loggers.general().info(LOG,"Balance Credit Amount-->" + balanceAmt);
                                                }
                                                fdwarapper1.setCUSCIFNO(cif_no);
                                                if (balanceAmt > 0) {

                                                      balance = String.valueOf(balanceAmt);
                                                      fdwarapper1.setBALANCE(balance + " " + balanceValCurrency);

                                                } else {
                                                      fdwarapper1.setBALANCE(0 + " " + balanceValCurrency);
                                                }
                                          } else {

                                                double irmAmt = 0;
                                                String closureQuery = "SELECT SUM(IR.AMOUNT)*POWER(10,C8.C8CED) AS IRMAMT FROM ETT_IRM_CLOSURE_TBL IR,C8PF C8 "
                                                            + " WHERE IR.MASTER_REFNO = '" + inwnum + "' AND "
                                                            + " IR.CURRENCY = C8.C8CCY AND IR.STATUS IN ('TP','A') GROUP BY C8.C8CED ";

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"IRM Clourse Query --->" + closureQuery);
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
                                                      Loggers.general().info(LOG,"Balance Credit Amount-->" + balan_cret);
                                                }

                                                if (balan_cret > 0) {
                                                      String balan_Str = String.valueOf(balan_cret);
                                                      fdwarapper1.setBALANCE(balan_Str + " " + creditcur);

                                                } else {
                                                      fdwarapper1.setBALANCE(0 + " " + creditcur);
                                                }

                                                fdwarapper1.setCUSCIFNO(cif_no);

                                          }
                                    } else {
                                          //// Loggers.general().info(LOG,"entered else since there is
                                          //// no Inward remittance no ");

                                    }

                              }
                        }

                  } catch (Exception e) {

                        //// Loggers.general().info(LOG,"Inward remittance excepton" +
                        //// e.getMessage());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Inward remittance excepton" + e.getMessage());
                        }

                  }

            } catch (Exception e) {

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Exception in ononfetchELCDOCLAYOUTButton" + e.getMessage());
                  }
            }

            finally {
                  try {
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
      }

      public void onRTGSELCDPclayButton() {
            //// Loggers.general().info(LOG,"IFSCFECTH");
            if (IFSCFECTH()) {
                  //// Loggers.general().info(LOG," IFSCFECTH BUTTON");
            } else {
                  //// Loggers.general().info(LOG,"IFSCFECTH Else systemOutput");
            }
      }

      public void onBENIFSCELCSETTclayButton() {
            //// Loggers.general().info(LOG,"IFSCFECTH");
            if (IFSCFECTH()) {
                  //// Loggers.general().info(LOG," IFSCFECTH BUTTON");
            } else {
                  //// Loggers.general().info(LOG,"IFSCFECTH Else systemOutput");
            }
      }

      public boolean IFSCFECTH() {
            boolean value = false;
            //// Loggers.general().info(LOG,"Exp lc button for POD");
            String Ifsc = getIFSCCO_Name().trim();
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
            if (!Ifsc.trim().equalsIgnoreCase("") || Ifsc != null) {
                  try {
                        con = ConnectionMaster.getConnection();
                        String query = "select count(*) from EXTIFSCC where IFSC='" + Ifsc + "'";
                        //// Loggers.general().info(LOG,"query for IFSC button" + query);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"query for IFSC button" + query);
                        }
                        int count = 0;
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                              //// Loggers.general().info(LOG,"Entered while");
                              count = rs1.getInt(1);
                              //// Loggers.general().info(LOG,"value of count in while " + count);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"value of count in while " + count);
                              }
                        }

                        if (count == 0) {

                              //// Loggers.general().info(LOG,"IFSC Code is Not valid ");
                              String ban = getBENBAK();
                              String bran = getBENBRN();
                              String cit = getBENCITY();
                              ban = "";
                              bran = "";
                              bran = "";
                              setBENBAK(ban);
                              setBENBRN(bran);
                              setBENCITY(bran);

                        } else {
                              try {

                                    String spq = "Select trim(BANK),trim(BRANCH),trim(CITY) from EXTIFSCC where IFSC ='" + Ifsc
                                                + "'";
                                    //// Loggers.general().info(LOG,spq);
                                    ps1 = con.prepareStatement(spq);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          setBENBAK((rs1.getString(1).trim()));
                                          setBENBRN(rs1.getString(2).trim());
                                          setBENCITY((rs1.getString(3).trim()));

                                    }

                              } catch (Exception e) {

                                    //// Loggers.general().info(LOG,"event catch");
                              }

                        }
                  } catch (Exception e) {

                        //// Loggers.general().info(LOG,"event catch");
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
            } else {
                  //// Loggers.general().info(LOG,"IFSC code is blak");
            }
            return value;
      }

      // -----------***--------------

      protected void loadExtEventFircDetailsViewPane(ExtensionViewPaneMode mode, ExtEventFircDetailsEntityWrapper item) {
            EnigmaArray<ExtEventFircDetailsEntityWrapper> listes = getExtEventFircDetailsData();

            //// Loggers.general().info(LOG,"loasdksiskdikdi");

            PaneManager.preExecute();
            try {
                  ExtensionViewWebPane pane = createExtEventFircDetailsViewPane(getLayoutName());
                  // pane.getco
                  pane.initialise(this, "ExtEventFircDetails", mode, item, getDriverWrapper());
                  PaneManager.execute(pane);
                  //// Loggers.general().info(LOG,"PaneManager");
            } catch (EnigmaException ex) {
                  PaneManager.undoPreExecute();
                  throw ex;
            } catch (Exception ex) {
                  PaneManager.undoPreExecute();
                  throw new EnigmaException(new EnigmaExceptionCode("CUST", 100), "Cannot load view pane", ex);
            }

      }

      public void onPRESHIPFINELCDPclayButton() {

            String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim(); //
            String evnt = getDriverWrapper().getEventFieldAsText("EPF", "s", "");
            String evvcount = getDriverWrapper().getEventFieldAsText("ESQ", "i", "");

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
                  con = ConnectionMaster.getConnection();

                  try {

                        String dms = "SELECT exte.MARAMT, exte.CCY_1 FROM master mas, BASEEVENT bev, BASEEVENT bev1, EXTEVENT exte WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = bev1.ATTACHD_EV AND bev1.KEY97 = exte.EVENT and exte.MARAMT is not null and trim(exte.CCY_1) is not null AND mas.MASTER_REF = '"
                                    + MasterReference + "' AND bev.REFNO_PFIX = '" + evnt + "' AND bev.REFNO_SERL =" + evvcount
                                    + "";

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Finance DPR margin query---->" + dms);
                        }
                        psd = con.prepareStatement(dms);
                        rst = psd.executeQuery();
                        if (rst.next()) {
                              String margin = rst.getString(1);
                              String ccy = rst.getString(2);
                              if (margin != null && margin.length() > 0 && ccy.length() > 1) {
                                    setMARAMT(margin + "" + ccy);
                              } else {
                                    setMARAMT("");
                              }

                        }

                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception Margin finance amount" + e.getMessage());
                        }
                  }

                  try {
                        getExtEventLoanDetailsNew().setEnabled(false);
                        getExtEventLoanDetailsDelete().setEnabled(false);
                        getExtEventLoanDetailsUpdate().setEnabled(false);
                        getExtEventLoanDetailsUp().setEnabled(false);
                        getExtEventLoanDetailsDown().setEnabled(false);
                        EnigmaArray<ExtEventLoanDetailsEntityWrapper> liste = getExtEventLoanDetailsData();
                        int count = 0;
                        // Iterator<ExtEventLoanDetailsEntityWrapper> iterator =
                        // liste.iterator();

                        if (liste.getSize() < 1) {
                              String query = "SELECT trim(exte.DEALREF),exte.REAMOUNT,exte.CCY,TO_CHAR(exte.VALDATE,'yyyy-mm-dd') FROM master mas, BASEEVENT bev, BASEEVENT bev1, EXTEVENTLRT exte WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = bev1.ATTACHD_EV AND bev1.EXTFIELD = exte.FK_EVENT AND mas.MASTER_REF = '"
                                          + MasterReference + "' AND bev.REFNO_PFIX = '" + evnt + "' AND bev.REFNO_SERL =" + evvcount
                                          + "";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"DPR Loan Details" + query);
                              }

                              psd = con.prepareStatement(query);
                              rst = psd.executeQuery();
                              //// Loggers.general().info(LOG,"executeQuery statement ");
                              while (rst.next()) {
                                    //// Loggers.general().info(LOG,"Enter into while");
                                    Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                                    ExtEventLoanDetails loanvalues = new ExtEventLoanDetails();
                                    loanvalues.setColumn("DEALREF", rst.getString(1));
                                    loanvalues.setColumn("REAMOUNT", rst.getString(2) + "" + rst.getString(3));
                                    // loanvalues.setColumn("CCY", rst.getString(3));
                                    loanvalues.setColumn("VALDATE", rst.getString(4));
                                    Loggers.general().info(LOG,"datevalue===>"+rst.getString(4));
                                    loanvalues.setNewKey();
                                    loanvalues.setFk(fkey);
                                    loanvalues.setSequence(count);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Loan details" + rst.getString(1));
                                          Loggers.general().info(LOG,"Repayment amount" + rst.getString(2));
                                          Loggers.general().info(LOG,"Value date" + rst.getString(3));
                                    }
                                    getExtEventLoanDetailsNew().setEnabled(false);
                                    getExtEventLoanDetailsDelete().setEnabled(false);
                                    getExtEventLoanDetailsUpdate().setEnabled(false);
                                    getExtEventLoanDetailsUp().setEnabled(false);
                                    getExtEventLoanDetailsDown().setEnabled(false);
                                    getBtnPRESHIPFINELCDPclay().setEnabled(false);

                                    ExtEventLoanDetailsEntityWrapper projectdetchk = new ExtEventLoanDetailsEntityWrapper(
                                                loanvalues, getDriverWrapper());
                                    addNewExtEventLoanDetails(projectdetchk);

                                    count++;
                              }
                        }
                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception LoanDetails population" + e.getMessage());
                        }

                  }
            } catch (Exception e) {
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Exception LoanDetails" + e.getMessage());
                  }

            }

            finally {

                  try {
                        if (rst != null)
                              rst.close();
                        if (psd != null)
                              psd.close();
                        if (con != null)
                              con.close();
                  } catch (SQLException e) {
                        //// Loggers.general().info(LOG,"Connection Failed! Check output
                        //// console");
                        e.printStackTrace();
                  }

            }

      }

      public void onPRESHIPFINELCSETTclayButton() {

            String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim(); //
            String evnt = getDriverWrapper().getEventFieldAsText("EPF", "s", "");
            String evvcount = getDriverWrapper().getEventFieldAsText("ESQ", "i", "");

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
                  con = ConnectionMaster.getConnection();
                  try {

                        String dms = "SELECT exte.MARAMT, exte.CCY_1 FROM master mas, BASEEVENT bev, BASEEVENT bev1, EXTEVENT exte WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = bev1.ATTACHD_EV AND bev1.KEY97 = exte.EVENT and exte.MARAMT is not null and trim(exte.CCY_1) is not null AND mas.MASTER_REF = '"
                                    + MasterReference + "' AND bev.REFNO_PFIX = '" + evnt + "' AND bev.REFNO_SERL =" + evvcount
                                    + "";

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Finance  margin query---->" + dms);
                        }
                        psd = con.prepareStatement(dms);
                        rst = psd.executeQuery();
                        if (rst.next()) {
                              String margin = rst.getString(1);
                              String ccy = rst.getString(2);
                              if (margin != null && margin.length() > 0 && ccy.length() > 1) {
                                    setMARAMT(margin + "" + ccy);
                              } else {
                                    setMARAMT("");
                              }

                        }

                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception Margin finance amount" + e.getMessage());
                        }
                  }

                  try {
                        getExtEventLoanDetailsNew().setEnabled(false);
                        getExtEventLoanDetailsDelete().setEnabled(false);
                        getExtEventLoanDetailsUpdate().setEnabled(false);
                        getExtEventLoanDetailsUp().setEnabled(false);
                        getExtEventLoanDetailsDown().setEnabled(false);
                        EnigmaArray<ExtEventLoanDetailsEntityWrapper> liste = getExtEventLoanDetailsData();
                        int count = 0;
                        // Iterator<ExtEventLoanDetailsEntityWrapper> iterator =
                        // liste.iterator();

                        if (liste.getSize() < 1) {
                              String query = "SELECT trim(exte.DEALREF),exte.REAMOUNT,exte.CCY,TO_CHAR(exte.VALDATE,'yyyy-mm-dd') FROM master mas, BASEEVENT bev, BASEEVENT bev1, EXTEVENTLRT exte WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = bev1.ATTACHD_EV AND bev1.EXTFIELD = exte.FK_EVENT AND mas.MASTER_REF = '"
                                          + MasterReference + "' AND bev.REFNO_PFIX = '" + evnt + "' AND bev.REFNO_SERL =" + evvcount
                                          + "";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"DPR Loan Details" + query);
                              }

                              psd = con.prepareStatement(query);
                              rst = psd.executeQuery();
                              //// Loggers.general().info(LOG,"executeQuery statement ");
                              while (rst.next()) {
                                    //// Loggers.general().info(LOG,"Enter into while");
                                    Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                                    ExtEventLoanDetails loanvalues = new ExtEventLoanDetails();
                                    loanvalues.setColumn("DEALREF", rst.getString(1));
                                    loanvalues.setColumn("REAMOUNT", rst.getString(2) + "" + rst.getString(3));
                                    // loanvalues.setColumn("CCY", rst.getString(3));
                                    loanvalues.setColumn("VALDATE", rst.getString(4));
                                    loanvalues.setNewKey();
                                    loanvalues.setFk(fkey);
                                    loanvalues.setSequence(count);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,"Loan details" + rst.getString(1));
                                          Loggers.general().info(LOG,"Repayment amount" + rst.getString(2));
                                          Loggers.general().info(LOG,"Value date" + rst.getString(3));
                                    }
                                    getExtEventLoanDetailsNew().setEnabled(false);
                                    getExtEventLoanDetailsDelete().setEnabled(false);
                                    getExtEventLoanDetailsUpdate().setEnabled(false);
                                    getExtEventLoanDetailsUp().setEnabled(false);
                                    getExtEventLoanDetailsDown().setEnabled(false);
                                    getBtnPRESHIPFINELCSETTclay().setEnabled(false);

                                    ExtEventLoanDetailsEntityWrapper projectdetchk = new ExtEventLoanDetailsEntityWrapper(
                                                loanvalues, getDriverWrapper());
                                    addNewExtEventLoanDetails(projectdetchk);

                                    count++;
                              }
                        }
                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Exception LoanDetails population" + e.getMessage());
                        }

                  }

            } catch (Exception e) {
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Exception LoanDetails" + e.getMessage());
                  }

            }

            finally {

                  try {
                        if (rst != null)
                              rst.close();
                        if (psd != null)
                              psd.close();
                        if (con != null)
                              con.close();
                  } catch (SQLException e) {
                        //// Loggers.general().info(LOG,"Connection Failed! Check output
                        //// console");
                        e.printStackTrace();
                  }

            }

      }

      public boolean deleteGridDetails() {
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
                        Loggers.general().info(LOG,"Advance Table grid is clear");
                  }
                  EnigmaArray<ExtEventAdvanceTableEntityWrapper> loanDetails = getExtEventAdvanceTableData();

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Grid details size" + loanDetails.getSize());
                  }
                  for (ExtEventAdvanceTableEntityWrapper detailsEntityWrapper : loanDetails) {
                        removeExtEventAdvanceTable(detailsEntityWrapper);
                  }

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Grid details after delete " + loanDetails.getSize());

                  }
                  return true;
            } catch (Exception e) {
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Exception in Grid details delete" + e.getMessage());
                  }
                  return false;
            }
      }

      public boolean deleteLoanDetails() {
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
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"New PreShipment method is Called");
                  }
                  EnigmaArray<ExtEventLoanDetailsEntityWrapper> loanDetails = getExtEventLoanDetailsData();
                  for (ExtEventLoanDetailsEntityWrapper detailsEntityWrapper : loanDetails) {
                        removeExtEventLoanDetails(detailsEntityWrapper);
                  }

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"PreShipment details size  after delete" + loanDetails.getSize());

                  }
                  return true;
            } catch (Exception e) {
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Exception in PreShip Fetch Test" + e.getMessage());
                  }
                  return false;
            }
      }

      public void onDELETEELCSETTclayButton() {
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
            if (getMajorCode().equalsIgnoreCase("ELC") && getMinorCode().equalsIgnoreCase("POD")
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
                                    deleteGridDetails();
                                    deleteLoanDetails();
                              }

                              getBtnDELETEELCSETTclay().setEnabled(false);
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

      public void onFetchpreshipELCSETTclayButton() {
            String strLog = "Log";
            String dailyval_Log = "";
            //String Subproduct="";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            // Loggers.general().info(LOG,"Enetered onFetchpreShipEXPCOLFECclayButton");
            Connection con = null;
            PreparedStatement prepareStmt = null;

            ResultSet result_loan_emplty = null;
            String masref = "";
            String eventRef = "";
            // int count = 0;
            // int k = 0;
            String[] currencies = { "USD", "JPY", "INR", "GBP", "EUR" };
            int temp_count = 0;
            String startCurr = "";
            double sumAmount = 0;
            ArrayList<String> sumAmountInAllCurrencies;
            int sequence = 0;
            int count = 0;
            int cc = 0;
            String loanType = "";// This will fetch the Loan Type from the grid
            ArrayList<String> loans = new ArrayList<String>();
            try {

                  deleteLoanDetails();

                  sumAmountInAllCurrencies = new ArrayList<String>();
                  masref = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  eventRef = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                  /*Subproduct = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
                  Loggers.general().info(LOG,"Preshipment subproduct"+Subproduct);*/
                  EnigmaArray<ExtEventLoanDetailsEntityWrapper> liste = getExtEventLoanDetailsData();
                  temp_count = liste.getSize().intValue();
                  // Loggers.general().info(LOG,"temp value " + temp_count);
                  if (liste.getSize() == 0) {
                        con = ConnectionMaster.getConnection();
                        String loan_query = "select trim(loan_ref) AS LOAN,repayamt AS AMOUNT ,trim(CURR) AS CURR,to_char(VALUE_DATE,'dd/mm/yyyy') AS VDATE,TRIM(PCFC) AS TYPE from ett_preshipment_apiserver where masref='"
                                    + masref + "' and eventref='" + eventRef + "' ORDER BY CURR";
                        // Loggers.general().info(LOG,"loan query is " + loan_query);
                        PreparedStatement ps = con.prepareStatement(loan_query);
                        ResultSet ress = ps.executeQuery();
                        while (ress.next()) {

                              loans.add(ress.getString("LOAN"));
                              loans.add(ress.getString("AMOUNT").replaceAll("[^\\d]", "").trim() + " "
                                          + ress.getString("CURR").trim());
                              loans.add(ress.getString("VDATE"));
                              loanType = ress.getString("TYPE");
                              // Loggers.general().info(LOG,"eneteerd while");

                        }
                        // Loggers.general().info(LOG,"Loan Type Value is " + loanType);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Loan Type Value is " + loanType);
                        }
                        for (int i = 0; i < loans.size() / 3; i++) {
                              SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                              Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                              ExtEventLoanDetails loanvalues = new ExtEventLoanDetails();
                              loanvalues.setColumn("DEALREF", loans.get(cc));
                              loanvalues.setColumn("REAMOUNT", loans.get(cc + 1));
                              Date date1 = format.parse(loans.get(cc + 2));
                              loanvalues.setVALDATE(date1);

                              loanvalues.setNewKey();
                              loanvalues.setFk(fkey);
                              loanvalues.setSequence(sequence);
                              ExtEventLoanDetailsEntityWrapper loanDetailsWrapper = new ExtEventLoanDetailsEntityWrapper(
                                          loanvalues, getDriverWrapper());

                              addNewExtEventLoanDetails(loanDetailsWrapper);
                              sequence++;
                              cc = cc + 3;
                              /*setPRESHIP(Subproduct);
                              Loggers.general().info(LOG,"Preshipment subproduct"+ getPRESHIP());*/
                              // Loggers.general().info(LOG,"va " + cc);
                        }
                        //// setLOANTYPE(loanType);
                        try {
                              // Loggers.general().info(LOG,"PreShipment new Changes");
                              sumAmountInAllCurrencies = new ArrayList<String>();

                              for (int a = 0; a < currencies.length; a++) {
                                    try {
                                          String query = "SELECT NVL(SUM(REPAYAMT),'0') FROM ETT_PRESHIPMENT_APISERVER WHERE CURR='"
                                                      + currencies[a] + "' AND masref='" + masref + "' and eventref='" + eventRef
                                                      + "' ORDER BY CURR DESC";
                                          // Loggers.general().info(LOG,"PreShipment Sum query " +
                                          // query);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"PreShipment Sum query " + query);
                                          }
                                          PreparedStatement prep = con.prepareStatement(query);
                                          ResultSet result = prep.executeQuery();
                                          if (result.next()) {
                                                double amount = 0;
                                                double dob_amt = result.getDouble(1);
                                                // Loggers.general().info(LOG,"Entered if and the Value
                                                // of Amount in double" + dob_amt);
                                                String amt = result.getString(1);
                                                BigDecimal amt_dec = new BigDecimal(amt);
                                                // Loggers.general().info(LOG,"Entered if and the Value
                                                // of Amount in String " + amt + " Bigdecimal" +
                                                // amt_dec);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Entered if and the Value of Amount in String " + amt
                                                                  + " Bigdecimal" + amt_dec);
                                                }
                                                if (currencies[a].equalsIgnoreCase("EUR")) {
                                                      amount = result.getDouble(1) / 100;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in if loop" + amount);

                                                } else {
                                                      amount = result.getDouble(1) / 100;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in else loop" + amount);
                                                }
                                                // Loggers.general().info(LOG,"Entered if and the Value
                                                // of Amount in double" + amount);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,"Entered if and the Value of Amount in double" + amount);
                                                }
                                                // -----------DECIMAL
                                                // ISSUE-----------------------------------------------------------

                                                if (currencies[a].equalsIgnoreCase("USD")) {
                                                       Loggers.general().info(LOG,"PRESHIPMENT Value of Amount in before" + amount);
                                                      amount = result.getDouble(1) / 100;
                                                       Loggers.general().info(LOG,"PRESHIPMENT Value of Amount in afte" + amount);
                                                } else {

                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                if (currencies[a].equalsIgnoreCase("INR")) {
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in before" + amount);
                                                      amount = result.getDouble(1) / 100;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in afte" + amount);
                                                } else {
                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                if (currencies[a].equalsIgnoreCase("JPY")) {
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in before" + amount);
                                                      amount = result.getDouble(1) / 1;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in afte" + amount);
                                                } else {
                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                if (currencies[a].equalsIgnoreCase("GBP")) {
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in before" + amount);
                                                      amount = result.getDouble(1) / 100;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in afte" + amount);
                                                } else {
                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                // -------------------------------------------------------------------------------------
//18-aug-19 decimal to bigdecimal for exponential in amount
                                                BigDecimal b_amt=new BigDecimal(amount);
                                                Currency cur=Currency.getInstance(currencies[a]);
                                                int precision=cur.getDefaultFractionDigits();
                                                RoundingMode DEFAULT_ROUNDING=RoundingMode.HALF_EVEN;
                                                BigDecimal roundoffvalue=null;
                                                roundoffvalue=b_amt.setScale(precision,DEFAULT_ROUNDING);
                                                sumAmountInAllCurrencies.add(roundoffvalue + " " + currencies[a]);
                                          } else {
                                                // Loggers.general().info(LOG,"ENtered else since the
                                                // Resultset is empty");
                                          }
                                          result.close();
                                          prep.close();

                                    } catch (SQLException sqlexception) {
                                          sqlexception.printStackTrace();
                                    }

                              }
                              // Loggers.general().info(LOG," sumAmountInAllCurrencies Size " +
                              // sumAmountInAllCurrencies.size());
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG," sumAmountInAllCurrencies Size " + sumAmountInAllCurrencies.size());
                              }
                              for (int b = 0; b < sumAmountInAllCurrencies.size(); b++) {
                                    // Loggers.general().info(LOG,"Inside For Loop " + b);
                                    if (b == 0) {
                                           Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTUSD(sumAmountInAllCurrencies.get(b));
                                          Loggers.general().info(LOG,"USD"+getTOTUSD());
                                    }
                                    if (b == 1) {
                                          setTOTJPY(sumAmountInAllCurrencies.get(b));
                                          // Loggers.general().info(LOG,"Value of b " + b);
                                    }
                                    if (b == 2) {
                                          // Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTINR(sumAmountInAllCurrencies.get(b));
                                    }
                                    if (b == 3) {
                                          // Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTGBP(sumAmountInAllCurrencies.get(b));
                                    }
                                    if (b == 4) {
                                          // Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTEUR(sumAmountInAllCurrencies.get(b));
                                    }
                              }

                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Exception in try inside try" +
                              // e.getMessage());
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Exception in try inside try" + e.getMessage());
                              }
                        }

                  }

            } catch (Exception e) {
                  // Loggers.general().info(LOG,"Exception int preshipment fetch " +
                  // e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Exception int preshipment fetch" + e.getMessage());
                  }
            } finally {
                  ConnectionMaster.surrenderDB1(result_loan_emplty, prepareStmt, con);
            }

      }

      public void onFetchpreShipEXPCOLFECclayButton() {
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
            //// Loggers.general().info(LOG,"Enetered onFetchpreShipEXPCOLFECclayButton");
            Connection con = null;
            PreparedStatement prepareStmt = null;

            ResultSet result_loan_emplty = null;
            String masref = "";
            String eventRef = "";
            // int count = 0;
            // int k = 0;
            String[] currencies = { "USD", "JPY", "INR", "GBP", "EUR" };
            int temp_count = 0;
            String startCurr = "";
            double sumAmount = 0;
            ArrayList<String> sumAmountInAllCurrencies;
            int sequence = 0;
            int count = 0;
            int cc = 0;
            String loanType = "";// This will fetch the Loan Type from the grid
            ArrayList<String> loans = new ArrayList<String>();
            try {

                  deleteLoanDetails();
                  sumAmountInAllCurrencies = new ArrayList<String>();
                  masref = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  eventRef = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                  EnigmaArray<ExtEventLoanDetailsEntityWrapper> liste = getExtEventLoanDetailsData();
                  temp_count = liste.getSize().intValue();
                  //// Loggers.general().info(LOG,"temp value " + temp_count);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"temp value " + temp_count);
                  }
                  if (liste.getSize() == 0) {
                        con = ConnectionMaster.getConnection();
                        String loan_query = "select trim(loan_ref) AS LOAN,repayamt AS AMOUNT ,trim(CURR) AS CURR,to_char(VALUE_DATE,'dd/mm/yyyy') AS VDATE,TRIM(PCFC) AS TYPE from ett_preshipment_apiserver where masref='"
                                    + masref + "' and eventref='" + eventRef + "' ORDER BY CURR";
                        //// Loggers.general().info(LOG,"loan query is " + loan_query);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"loan query is " + loan_query);
                        }
                        PreparedStatement ps = con.prepareStatement(loan_query);
                        ResultSet ress = ps.executeQuery();
                        while (ress.next()) {

                              loans.add(ress.getString("LOAN"));
                              loans.add(ress.getString("AMOUNT").replaceAll("[^\\d]", "").trim() + " "
                                          + ress.getString("CURR").trim());
                              loans.add(ress.getString("VDATE"));
                              loanType = ress.getString("TYPE");
                              //// Loggers.general().info(LOG,"eneteerd while");

                        }
                        //// Loggers.general().info(LOG,"Loan Type Value is " + loanType);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,"Loan Type Value is " + loanType);
                        }
                        for (int i = 0; i < loans.size() / 3; i++) {
                              SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                              Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                              ExtEventLoanDetails loanvalues = new ExtEventLoanDetails();
                              loanvalues.setColumn("DEALREF", loans.get(cc));
                              loanvalues.setColumn("REAMOUNT", loans.get(cc + 1));
                              Date date1 = format.parse(loans.get(cc + 2));
                              loanvalues.setVALDATE(date1);

                              loanvalues.setNewKey();
                              loanvalues.setFk(fkey);
                              loanvalues.setSequence(sequence);
                              ExtEventLoanDetailsEntityWrapper loanDetailsWrapper = new ExtEventLoanDetailsEntityWrapper(
                                          loanvalues, getDriverWrapper());

                              addNewExtEventLoanDetails(loanDetailsWrapper);
                              sequence++;
                              cc = cc + 3;
                              //// Loggers.general().info(LOG,"va " + cc);
                        }
                        // setLOANTYPE(loanType);
                        try {
                              //// Loggers.general().info(LOG,"PreShipment new Changes");
                              sumAmountInAllCurrencies = new ArrayList<String>();

                              for (int a = 0; a < currencies.length; a++) {
                                    try {
                                          String query = "SELECT NVL(SUM(REPAYAMT),'0') FROM ETT_PRESHIPMENT_APISERVER WHERE CURR='"
                                                      + currencies[a] + "' AND masref='" + masref + "' and eventref='" + eventRef.trim()
                                                      + "' ORDER BY CURR DESC";
                                          //// Loggers.general().info(LOG,"PreShipment Sum query " +
                                          //// query);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,"PreShipment Sum query " + query);
                                          }
                                          PreparedStatement prep = con.prepareStatement(query);
                                          ResultSet result = prep.executeQuery();
                                          if (result.next()) {
                                                double amount = 0;
                                                //// Loggers.general().info(LOG,"Entered if and the
                                                //// Value of Amount is " +
                                                //// result.getString(1));

                                                if (currencies[a].equalsIgnoreCase("EUR")) {
                                                      amount = result.getDouble(1) / 100;
                                                      //// Loggers.general().info(LOG,"PRESHIPMENT Value
                                                      //// of Amount in if loop" + amount);

                                                } else {
                                                      amount = result.getDouble(1) / 100;
                                                      //// Loggers.general().info(LOG,"PRESHIPMENT Value
                                                      //// of Amount in else loop" + amount);
                                                }
                                                //// Loggers.general().info(LOG,"Entered if and the
                                                //// Value of Amount in double" + amount);
                                                // -----------DECIMAL
                                                // ISSUE-----------------------------------------------------------

                                                if (currencies[a].equalsIgnoreCase("USD")) {
                                                       Loggers.general().info(LOG,"PRESHIPMENT Value of Amount in before" + amount);
                                                      amount = result.getDouble(1) / 100;
                                                       Loggers.general().info(LOG,"PRESHIPMENT Value of Amount in afte" + amount);
                                                } else {

                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                if (currencies[a].equalsIgnoreCase("INR")) {
                                                      //// Loggers.general().info(LOG,"PRESHIPMENT Value
                                                      //// of Amount in before" + amount);
                                                      amount = result.getDouble(1) / 100;
                                                      //// Loggers.general().info(LOG,"PRESHIPMENT Value
                                                      //// of Amount in afte" + amount);
                                                } else {
                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                if (currencies[a].equalsIgnoreCase("JPY")) {
                                                      //// Loggers.general().info(LOG,"PRESHIPMENT Value
                                                      //// of Amount in before" + amount);
                                                      amount = result.getDouble(1) / 1;
                                                      //// Loggers.general().info(LOG,"PRESHIPMENT Value
                                                      //// of Amount in afte" + amount);
                                                } else {
                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                if (currencies[a].equalsIgnoreCase("GBP")) {
                                                      //// Loggers.general().info(LOG,"PRESHIPMENT Value
                                                      //// of Amount in before" + amount);
                                                      amount = result.getDouble(1) / 100;
                                                      //// Loggers.general().info(LOG,"PRESHIPMENT Value
                                                      //// of Amount in afte" + amount);
                                                } else {
                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                // -------------------------------------------------------------------------------------
                                                //18-aug-19 decimal to bigdecimal for exponential in amount
                                                BigDecimal b_amt=new BigDecimal(amount);
                                                Currency cur=Currency.getInstance(currencies[a]);
                                                int precision=cur.getDefaultFractionDigits();
                                                RoundingMode DEFAULT_ROUNDING=RoundingMode.HALF_EVEN;
                                                BigDecimal roundoffvalue=null;
                                                roundoffvalue=b_amt.setScale(precision,DEFAULT_ROUNDING);
                                                sumAmountInAllCurrencies.add(roundoffvalue + " " + currencies[a]);
                                                //sumAmountInAllCurrencies.add(amount + " " + currencies[a]);
                                          } else {
                                                //// Loggers.general().info(LOG,"ENtered else since the
                                                //// Resultset is empty");
                                          }
                                          result.close();
                                          prep.close();

                                    } catch (SQLException sqlexception) {
                                          sqlexception.printStackTrace();
                                    }

                              }
                              //// Loggers.general().info(LOG," sumAmountInAllCurrencies Size " +
                              //// sumAmountInAllCurrencies.size());
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG," sumAmountInAllCurrencies Size " + sumAmountInAllCurrencies.size());
                              }
                              for (int b = 0; b < sumAmountInAllCurrencies.size(); b++) {
                                    //// Loggers.general().info(LOG,"Inside For Loop " + b);
                                    if (b == 0) {
                                           Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTUSD(sumAmountInAllCurrencies.get(b));
                                          Loggers.general().info(LOG,"get usd"+getTOTUSD());
                                    }
                                    if (b == 1) {
                                          setTOTJPY(sumAmountInAllCurrencies.get(b));
                                          //// Loggers.general().info(LOG,"Value of b " + b);
                                    }
                                    if (b == 2) {
                                          //// Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTINR(sumAmountInAllCurrencies.get(b));
                                    }
                                    if (b == 3) {
                                          //// Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTGBP(sumAmountInAllCurrencies.get(b));
                                    }
                                    if (b == 4) {
                                          //// Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTEUR(sumAmountInAllCurrencies.get(b));
                                    }
                              }

                        } catch (Exception e) {
                              //// Loggers.general().info(LOG,"Exception in try inside try" +
                              //// e.getMessage());
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,"Exception in try inside try" + e.getMessage());
                              }
                        }

                  }

            } catch (Exception e) {

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG,"Exception int preshipment fetch" + e.getMessage());
                  }
            } finally {
                  ConnectionMaster.surrenderDB1(result_loan_emplty, prepareStmt, con);
            }

      }

      private String setValueTOString(double d1, String exchangeccy) {
            DecimalFormat df = new DecimalFormat("#.##");
            BigDecimal dValue = new BigDecimal(df.format(d1));
            return String.valueOf(dValue) + exchangeccy;
      }
      public void onFETCHACCOUNTELCDPclayButton() {
      account();
    }
    public void onFETCHACCOUNTELCSETTclayButton() {
      account();
    }
    //   Fetch button to get pca account details in export product added by Vishal G
    public void account() {
      try {
                  con = ConnectionMaster.getConnection();
                  String masReference = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                  String key29="";
                  int count = 1;
//                String query="Select bev.extfield from master mas,baseevent bev "+
//                       "where mas.key97=bev.master_key "+
//                       "and mas.MASTER_REF='"+masReference.trim()+"'"+
//                      " and bev.REFNO_PFIX||LPAD(bev.refno_serl,3,0)='"+eventCode+"'"+
//                      " and bev.status not in ('a','c')";
//          System.out.println("query for account code button:"+query+" "+masReference+" "+eventCode);
//          dmsp = con.prepareStatement(query);
//          // //Loggers.general().info(LOG,"DMS Query " + dms);
//          dmsr = dmsp.executeQuery();   
//          while (dmsr.next()) {
//                key29 = dmsr.getString(1);
//                // dmsurl = dmsstr + "&dirAmount=" + payamt;
//                
//                }
                  String accountQuery = "Select xkey,seqn,fk_event,raccount,raccttyp,ramount,rpercent,response,drcr from exteventaccount "+
                      " where fk_event=("+"Select bev.extfield from master mas,baseevent bev "+
                         "where mas.key97=bev.master_key "+
                         "and mas.MASTER_REF='"+masReference+"'"+
                        " and bev.REFNO_PFIX||LPAD(bev.refno_serl,3,0)='"+eventCode+"'"+
                        " and bev.status not in ('a','c')"+")";
           account = con.prepareStatement(accountQuery);
           rst1 = account.executeQuery();
           System.out.println("account query fetch" + accountQuery);
           while (rst1.next()) {
           Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
           ExtEventAccountRealisation account = new ExtEventAccountRealisation();

            account.setRACCOUNT(rst1.getString(4));
            account.setRACCTTYP(rst1.getString(5));
            account.setRAMOUNT(rst1.getString(6));
            account.setRPERCENT(rst1.getString(7));
            System.out.println("inside while loop of account" + rst1.getString(3)+" ramount "+rst1.getString(5));
            account.setRESPONSE(rst1.getString(8));
            account.setDRCR(rst1.getString(9));
            account.setNewKey();
            account.setFk(fkey);
            account.setSequence(count);
            System.out.println("last of while foreign "+fkey);
            ExtEventAccountRealisationEntityWrapper accountwrapper = new ExtEventAccountRealisationEntityWrapper(
              account, getDriverWrapper());
            addNewExtEventAccountRealisation(accountwrapper);
            count++;
            System.out.println("last of while  "+count);
             }
             }catch (Exception e) {
             e.printStackTrace();
             System.out.println("Exception update" + e.getMessage());
            }
            finally {
              ConnectionMaster.surrenderDB(con, account, rst1);
              ConnectionMaster.surrenderDB(con, dmsp, dmsr);
           }
         }
   
}