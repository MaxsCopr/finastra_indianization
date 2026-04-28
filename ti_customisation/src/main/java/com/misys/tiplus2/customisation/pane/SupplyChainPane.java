package com.misys.tiplus2.customisation.pane;

//com.misys.tiplus2.customisation.pane.SupplyChainPane
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

//import org.apache.log4j.Logger;

import com.misys.tiplus2.apps.ti.kernel.extpm.entity.ExtEvent;
import com.misys.tiplus2.apps.ti.kernel.extpm.entity.Key_ExtEvent;
import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;
import com.misys.tiplus2.customisation.entity.ExtEventServiceTax;
import com.misys.tiplus2.customisation.entity.ExtEventServiceTaxEntityWrapper;
import com.misys.tiplus2.customisation.extension.ConnectionMaster;
import com.misys.tiplus2.customisation.extension.OdcFEC;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.WarningType;
import com.misys.tiplus2.enigma.lang.datatype.EnigmaArray;
import com.misys.tiplus2.enigma.customisation.validation.ValidationTexts;
import com.misys.tiplus2.foundations.lang.logging.Loggers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.misys.tiplus2.foundations.lang.logging.Loggers;
public class SupplyChainPane extends EventPane {
      //private static final Logger logger = Logger.getLogger(ConnectionMaster.class.getName());
      private static final Logger LOG = LoggerFactory.getLogger(SupplyChainPane.class);
      Connection con = null;
      PreparedStatement peco, ps1 = null;
      ResultSet dmsr1, rs1 = null;
      // @Override

      /*
       * public void ondisplayvalREVFACCREclayButton() { //Loggers.general().info(LOG,
       * "ondisplayvalSupplychainsellButton start"); String strPrdCode =
       * "",strPrdSubType = "",strAnchor = "",strVendor = "",strVendorCu="";
       *
       * strPrdCode=getDriverWrapper().getEventFieldAsText("PCO", "s", "");
       * //Loggers.general().info(LOG,"ondisplayvalSupplychainsellButton started");
       * //Loggers.general().info(LOG,"strPrdCode - "+strPrdCode); strPrdSubType =
       * getDriverWrapper().getEventFieldAsText("PTP", "s", "");
       * //Loggers.general().info(LOG,"strPrdSubType - "+strPrdSubType); if
       * (strPrdCode.equals("IRF")) { if (strPrdSubType.equals("AIP")) {
       *
       * strAnchor = getDriverWrapper().getEventFieldAsText("PRI", "p", "");
       * strVendor = getDriverWrapper().getEventFieldAsText("CDT", "p", "");
       * strVendorCu = getDriverWrapper().getEventFieldAsText("CDT", "p", "cu");
       * setBUYERN_Name(strAnchor); setSELLERN_Name(strVendor);
       * setADVANC_Name(strAnchor); setCREDITTO_Name(strVendor);
       * setLABELN_Name(strAnchor);
       *
       * benei(strVendorCu); }
       *
       * else if (strPrdSubType.equals("AIR")) {
       *
       * strAnchor = getDriverWrapper().getEventFieldAsText("CDT", "p", "");
       * strVendor = getDriverWrapper().getEventFieldAsText("PRI", "p", "");
       * strVendorCu = getDriverWrapper().getEventFieldAsText("PRI", "p", "cu");
       * setBUYERN_Name(strAnchor);//Buyer Name
       * setSELLERN_Name(strVendor);//Seller Name
       * setADVANC_Name(strVendor);//Advance to setCREDITTO_Name(strVendor);//
       * Credit to setLABELN_Name(strVendor);//interest charge party
       *
       *
       * benei(strVendorCu); }
       *
       * else if (strPrdSubType.equals("VIR")) {
       *
       * strAnchor = getDriverWrapper().getEventFieldAsText("PRI", "p", "");
       * strVendor = getDriverWrapper().getEventFieldAsText("CDT", "p", "");
       * strVendorCu = getDriverWrapper().getEventFieldAsText("CDT", "p", "cu");
       * setBUYERN_Name(strAnchor); setSELLERN_Name(strVendor);
       * setADVANC_Name(strVendor); setCREDITTO_Name(strVendor);
       * setLABELN_Name(strVendor);
       *
       * benei(strVendorCu); }
       *
       * else if (strPrdSubType.equals("DIP")) { strAnchor =
       * getDriverWrapper().getEventFieldAsText("CDT", "p", ""); strVendor =
       * getDriverWrapper().getEventFieldAsText("PRI", "p", ""); strVendorCu =
       * getDriverWrapper().getEventFieldAsText("PRI", "p", "cu");
       * setBUYERN_Name(strAnchor); setSELLERN_Name(strVendor);
       * setADVANC_Name(strAnchor); setCREDITTO_Name(strVendor);
       * setLABELN_Name(strAnchor);
       *
       * benei(strVendorCu); }
       *
       * else if (strPrdSubType.equals("ABP")) {
       *
       * strAnchor = getDriverWrapper().getEventFieldAsText("PRI", "p", "");
       * strVendor = getDriverWrapper().getEventFieldAsText("CDT", "p", "");
       * strVendorCu = getDriverWrapper().getEventFieldAsText("CDT", "p", "cu");
       * setBUYERN_Name(strAnchor); setSELLERN_Name(strVendor);
       * setADVANC_Name(strAnchor); setCREDITTO_Name(strVendor);
       * setLABELN_Name(strAnchor);
       *
       * benei(strVendorCu); }
       *
       * else if (strPrdSubType.equals("ABR")) {
       *
       * strAnchor = getDriverWrapper().getEventFieldAsText("CDT", "p", "");
       * strVendor = getDriverWrapper().getEventFieldAsText("PRI", "p", "");
       * strVendorCu = getDriverWrapper().getEventFieldAsText("PRI", "p", "cu");
       * setBUYERN_Name(strAnchor); setSELLERN_Name(strVendor);
       * setADVANC_Name(strVendor); setCREDITTO_Name(strVendor);
       * setLABELN_Name(strVendor);
       *
       * benei(strVendorCu); }
       *
       * else if (strPrdSubType.equals("VBR")) { strAnchor =
       * getDriverWrapper().getEventFieldAsText("PRI", "p", ""); strVendor =
       * getDriverWrapper().getEventFieldAsText("CDT", "p", ""); strVendorCu =
       * getDriverWrapper().getEventFieldAsText("CDT", "p", "cu");
       * setBUYERN_Name(strAnchor); setSELLERN_Name(strVendor);
       * setADVANC_Name(strVendor); setCREDITTO_Name(strVendor);
       * setLABELN_Name(strVendor);
       *
       * benei(strVendorCu); }
       *
       * else if (strPrdSubType.equals("DBP")) {
       *
       * strAnchor = getDriverWrapper().getEventFieldAsText("CDT", "p", "");
       * strVendor = getDriverWrapper().getEventFieldAsText("PRI", "p", "");
       * strVendorCu = getDriverWrapper().getEventFieldAsText("PRI", "p", "cu");
       * setBUYERN_Name(strAnchor); setSELLERN_Name(strVendor);
       * setADVANC_Name(strAnchor); setCREDITTO_Name(strVendor);
       * setLABELN_Name(strAnchor);
       *
       * benei(strVendorCu); }
       *
       * } //Loggers.general().info(LOG,"ondisplayvalSupplychainsellButton end");
       *
       * }
       */

//    public void onSERVICEINVDISREPclayButton() {
//
//          // Loggers.general().info(LOG,"SERVICE Button");
//          if (SERVICFECTH()) {
//                // Loggers.general().info(LOG,"onSERVICEELCADVclayButton calling");
//          } else {
//                // Loggers.general().info(LOG,"Else systemOutput");
//          }
//
//    }

//    public void onSERVICEINVDISCREclayButton() {
//
//          // Loggers.general().info(LOG,"SERVICE Button");
//          if (SERVICFECTH()) {
//                // Loggers.general().info(LOG,"onSERVICEELCADVclayButton calling");
//          } else {
//                // Loggers.general().info(LOG,"Else systemOutput");
//          }
//
//    }

//    public void onSERVICEINOVICECREATElayButton() {
//
//          // Loggers.general().info(LOG,"SERVICE Button");
//          if (SERVICFECTH()) {
//                // Loggers.general().info(LOG,"onSERVICEELCADVclayButton calling");
//          } else {
//                // Loggers.general().info(LOG,"Else systemOutput");
//          }
//
//    }

//    public void onSERVICEREVFACCREclayButton() {
//
//          // Loggers.general().info(LOG,"SERVICE Button");
//          if (SERVICFECTH()) {
//                // Loggers.general().info(LOG,"onSERVICEELCADVclayButton calling");
//          } else {
//                // Loggers.general().info(LOG,"Else systemOutput");
//          }
//
//    }

//    public void onSERVICEIRFCREATElayButton() {
//
//          // Loggers.general().info(LOG,"SERVICE Button");
//          if (SERVICFECTH()) {
//                // Loggers.general().info(LOG,"onSERVICEELCADVclayButton calling");
//          } else {
//                // Loggers.general().info(LOG,"Else systemOutput");
//          }
//
//    }

//    public void onSERVICEREVFACREPclayButton() {
//
//          // Loggers.general().info(LOG,"SERVICE Button");
//          if (SERVICFECTH()) {
//                // Loggers.general().info(LOG,"onSERVICEELCADVclayButton calling");
//          } else {
//                // Loggers.general().info(LOG,"Else systemOutput");
//          }
//
//    }

      // public boolean SERVICFECTH() {
      // boolean value = false;
      // //Loggers.general().info(LOG,"onSERVICEELCADVclayButton Button is Called");
      // getExtEventServiceTaxNew().setEnabled(false);
      // getExtEventServiceTaxDelete().setEnabled(false);
      // getExtEventServiceTaxUpdate().setEnabled(false);
      // getExtEventServiceTaxUp().setEnabled(false);
      // getExtEventServiceTaxDown().setEnabled(false);
      // getExtEventServiceTaxView().setEnabled(false);
      // //Loggers.general().info(LOG,"getServiceTAX fetching--->");
      //
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
      //
      // String masterRefNumber = getDriverWrapper().getEventFieldAsText("MST",
      // "r", "");
      // // //Loggers.general().info(LOG,"Master Reference" + masterRefNumber);
      // if (dailyval_Log.equalsIgnoreCase("YES")) {
      //
      // Loggers.general().info(LOG,"Master Reference" + masterRefNumber);
      // }
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
      // //Loggers.general().info(LOG,"getServiceTAX fetching--->" + liste.getSize());
      // // for (int i = 0; i < seivce.size(); i++) {
      // // ExtEventServiceTax serviceTax = seivce.get(i);
      // // }
      //
      // if (liste.getSize() < 1) {
      // try {
      // //Loggers.general().info(LOG,"calling service tax query");
      // String query = "SELECT
      // TRIM(DESCR),TRIM(CHARGE_AMT),TRIM(SERVICE_TAX),TRIM(EDU_CESS),TRIM(KRISHI_CESS)
      // FROM ETTV_SERVICETAX_SWACH_CALC WHERE REFNO_PFIX='"
      // + eventPrefix + "' AND REFNO_SERL=" + eventPrefixNo + " AND MASTER_REF='"
      // + masterRefNumber + "'";
      //
      // //Loggers.general().info(LOG,"Service tax query - " + query);
      // if (dailyval_Log.equalsIgnoreCase("YES")) {
      //
      // Loggers.general().info(LOG,"Service tax query - " + query);
      // }
      // con = ConnectionMaster.getConnection();
      // PreparedStatement ps1 = con.prepareStatement(query);
      // ResultSet rs1 = ps1.executeQuery();
      //
      // while (rs1.next()) {
      // //Loggers.general().info(LOG,"Enter into while");
      // Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
      // ExtEventServiceTax serviceTax = new ExtEventServiceTax();
      // //Loggers.general().info(LOG,"liste size -----> " + fkey);
      // serviceTax.setColumn("DESCR", rs1.getString(1));
      // serviceTax.setColumn("CHRGAMT", rs1.getString(2) + " " + "INR");
      // serviceTax.setColumn("SERVTAX", rs1.getString(3) + " " + "INR");
      // //Loggers.general().info(LOG,"Service tax value --->" + rs1.getString(1));
      // serviceTax.setColumn("EDUCES", rs1.getString(4) + " " + "INR");
      // serviceTax.setColumn("KRISH", rs1.getString(5) + " " + "INR");
      // serviceTax.setNewKey();
      // serviceTax.setFk(fkey);
      // serviceTax.setSequence(count);
      // //Loggers.general().info(LOG,"Service tax values");
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
      // //Loggers.general().info(LOG,"ServiceTax out of loop");
      //
      // } catch (Exception e) {
      // //Loggers.general().info(LOG,"Exeception of service tax- " + e.getMessage());
      // if (dailyval_Log.equalsIgnoreCase("YES")) {
      //
      // Loggers.general().info(LOG,"Exeception of service tax- " + e.getMessage());
      // }
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
      // //Loggers.general().info(LOG,"Connection Failed! Check output console");
      // e.printStackTrace();
      // }
      // }
      //
      // } else {
      // //Loggers.general().info(LOG,"Service tax grid value greater then 1 ");
      // }
      // return value;
      // }

      public void ondisplayvalSupplychainLButton() {

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
            String strPrdCode = "", strPrdSubType = "", strAnchor = "", strVendor = "", strVendorCu = "";

            strPrdCode = getDriverWrapper().getEventFieldAsText("PCO", "s", "");
            // Loggers.general().info(LOG,"ondisplayvalSupplychainsellButton started");
            // Loggers.general().info(LOG,"strPrdCode - " + strPrdCode);
            strPrdSubType = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
            // Loggers.general().info(LOG,"strPrdSubType - " + strPrdSubType);
            if (dailyval_Log.equalsIgnoreCase("YES")) {

                  Loggers.general().info(LOG,"strPrdSubType - " + strPrdSubType);
            }
            if (strPrdCode.equals("IRF")) {
                  if (strPrdSubType.equals("AIP")) {

                        strAnchor = getDriverWrapper().getEventFieldAsText("PRI", "p", "");
                        strVendor = getDriverWrapper().getEventFieldAsText("CDT", "p", "");
                        strVendorCu = getDriverWrapper().getEventFieldAsText("CDT", "p", "cu");
                        setBUYERN_Name(strAnchor);
                        setSELLERN_Name(strVendor);
                        // setADVANC_Name(strAnchor);
                        // setCREDITTO_Name(strVendor);
                        // setLABELN_Name(strAnchor);

                        benei(strVendorCu);
                  }

                  else if (strPrdSubType.equals("AIR")) {

                        strAnchor = getDriverWrapper().getEventFieldAsText("CDT", "p", "");
                        strVendor = getDriverWrapper().getEventFieldAsText("PRI", "p", "");
                        strVendorCu = getDriverWrapper().getEventFieldAsText("PRI", "p", "cu");
                        setBUYERN_Name(strAnchor);// Buyer Name
                        setSELLERN_Name(strVendor);// Seller Name
                        // setADVANC_Name(strVendor);//Advance to
                        // setCREDITTO_Name(strVendor);// Credit to
                        // setLABELN_Name(strVendor);//interest charge party

                        benei(strVendorCu);
                  }

                  else if (strPrdSubType.equals("VIR")) {

                        strAnchor = getDriverWrapper().getEventFieldAsText("PRI", "p", "");
                        strVendor = getDriverWrapper().getEventFieldAsText("CDT", "p", "");
                        strVendorCu = getDriverWrapper().getEventFieldAsText("CDT", "p", "cu");
                        setBUYERN_Name(strAnchor);
                        setSELLERN_Name(strVendor);
                        // setADVANC_Name(strVendor);
                        // setCREDITTO_Name(strVendor);
                        // setLABELN_Name(strVendor);

                        benei(strVendorCu);
                  }

                  else if (strPrdSubType.equals("DIP")) {
                        strAnchor = getDriverWrapper().getEventFieldAsText("CDT", "p", "");
                        strVendor = getDriverWrapper().getEventFieldAsText("PRI", "p", "");
                        strVendorCu = getDriverWrapper().getEventFieldAsText("PRI", "p", "cu");
                        setBUYERN_Name(strAnchor);
                        setSELLERN_Name(strVendor);
                        // setADVANC_Name(strAnchor);
                        // setCREDITTO_Name(strVendor);
                        // setLABELN_Name(strAnchor);
                        //
                        benei(strVendorCu);
                  }

                  else if (strPrdSubType.equals("ABP")) {

                        strAnchor = getDriverWrapper().getEventFieldAsText("PRI", "p", "");
                        strVendor = getDriverWrapper().getEventFieldAsText("CDT", "p", "");
                        strVendorCu = getDriverWrapper().getEventFieldAsText("CDT", "p", "cu");
                        setBUYERN_Name(strAnchor);
                        setSELLERN_Name(strVendor);
                        // setADVANC_Name(strAnchor);
                        // setCREDITTO_Name(strVendor);
                        // setLABELN_Name(strAnchor);

                        benei(strVendorCu);
                  }

                  else if (strPrdSubType.equals("ABR")) {

                        strAnchor = getDriverWrapper().getEventFieldAsText("CDT", "p", "");
                        strVendor = getDriverWrapper().getEventFieldAsText("PRI", "p", "");
                        strVendorCu = getDriverWrapper().getEventFieldAsText("PRI", "p", "cu");
                        setBUYERN_Name(strAnchor);
                        setSELLERN_Name(strVendor);
                        // setADVANC_Name(strVendor);
                        // setCREDITTO_Name(strVendor);
                        // setLABELN_Name(strVendor);

                        benei(strVendorCu);
                  }

                  else if (strPrdSubType.equals("VBR")) {
                        strAnchor = getDriverWrapper().getEventFieldAsText("PRI", "p", "");
                        strVendor = getDriverWrapper().getEventFieldAsText("CDT", "p", "");
                        strVendorCu = getDriverWrapper().getEventFieldAsText("CDT", "p", "cu");
                        setBUYERN_Name(strAnchor);
                        setSELLERN_Name(strVendor);
                        // setADVANC_Name(strVendor);
                        // setCREDITTO_Name(strVendor);
                        // setLABELN_Name(strVendor);

                        benei(strVendorCu);
                  }

                  else if (strPrdSubType.equals("DBP")) {

                        strAnchor = getDriverWrapper().getEventFieldAsText("CDT", "p", "");
                        strVendor = getDriverWrapper().getEventFieldAsText("PRI", "p", "");
                        strVendorCu = getDriverWrapper().getEventFieldAsText("PRI", "p", "cu");
                        setBUYERN_Name(strAnchor);
                        setSELLERN_Name(strVendor);
                        // setADVANC_Name(strAnchor);
                        // setCREDITTO_Name(strVendor);
                        // setLABELN_Name(strAnchor);

                        benei(strVendorCu);
                  }

            }
            // Loggers.general().info(LOG,"ondisplayvalSupplychainsellButton end**");
      }

      public boolean benei(String cusmnea) {
            // //Loggers.general().info(LOG,"benei started");
            // Connection con=null;
            //
            // setBENNAME_Name(cusmnea);
            // String cusmneaa = getBENNAME_Name().trim();
            // //setBENEF_Name(null);
            // setBENACC_Name(null);
            // setIFSCCO_Name(null);
            //
            // //setBENSNO_Name(null);
            //
            // String rtnf = "rtnf";
            //
            // String rtnfs = getDriverWrapper().getEventFieldAsText("PUO1","s","");
            // if((rtnfs.trim().equalsIgnoreCase("RTGS"))||(rtnfs.trim().equalsIgnoreCase("RTG"))||(rtnfs.trim().equalsIgnoreCase("RTS")))
            // {
            // //Loggers.general().info(LOG,"setting RTGS");
            // rtnf = "RTGS";
            // }
            // else
            // if(rtnfs.trim().equalsIgnoreCase("NEFT")||(rtnfs.trim().equalsIgnoreCase("NFT"))||(rtnfs.trim().equalsIgnoreCase("NEF"))){
            // //Loggers.general().info(LOG,"setting NEFT");
            // rtnf ="NEFT";
            // }
            //
            // //Loggers.general().info(LOG,"getBENSNO_Name().length -
            // "+getBENSNO_Name().length());
            // if(getBENSNO_Name().length()==0){
            // try
            // {
            // con =ConnectionMaster.getConnection();
            // String spq ="Select benfi,benfyact,ifsccb,custom,sno,rtnf from
            // extsrtgs where custom ='"+cusmneaa+ "' and RTNF ='" +rtnf +"'";
            // PreparedStatement peco = con.prepareStatement(spq);
            // /*peco.setString(1, cusmneaa);
            // peco.setString(2, rtnf);*/
            //
            // ResultSet dmsr1 = peco.executeQuery();
            // ResultSet dmsr2 = peco.executeQuery();
            //
            // //Loggers.general().info(LOG,"dmsr2 - "+dmsr2.next());
            //
            // int size = 0;
            // while (dmsr1.next()) {
            // //Loggers.general().info(LOG,"Supply chainL while Started");
            // //Loggers.general().info(LOG,"BENNEF_Name"+dmsr1.getString("benfi").trim());
            // setBENEF_Name((dmsr1.getString("benfi").trim()));
            // //Loggers.general().info(LOG,"BENACC_Name"+dmsr1.getString("benfyact").trim());
            // setBENACC_Name(dmsr1.getString("benfyact").trim());
            // //Loggers.general().info(LOG,"IFSCCO_Name"+dmsr1.getString("ifsccb").trim());
            // setIFSCCO_Name((dmsr1.getString("ifsccb").trim()));
            // //Loggers.general().info(LOG,"BENNAME_Name"+dmsr1.getString("custom").trim());
            // setBENNAME_Name((dmsr1.getString("custom").trim()));
            // size = size + 1;
            // }
            // //Loggers.general().info(LOG,"size - "+size);
            // //Loggers.general().info(LOG,"Supply chainL while Ended");
            // dmsr1.close();
            // dmsr2.close();
            // peco.close();
            // con.close();
            // if(size>=2)
            // {
            // setBENSNO_Name(cusmnea);
            // setBENEF_Name(null);
            // setBENACC_Name(null);
            // setIFSCCO_Name(null);
            //
            // }
            // }
            // catch(Exception e)
            // {
            // //Loggers.general().info(LOG,"Error Catched"+e.getMessage());
            // }
            // }
            // else{
            // try{
            // //Loggers.general().info(LOG,"Else is started successfully");
            // con =ConnectionMaster.getConnection();
            // cusmneaa = getBENNAME_Name();
            // String snoo =getBENSNO_Name();
            // String spq ="Select benfi,benfyact,ifsccb,custom,sno,rtnf from
            // extsrtgs where sno ='"+snoo+"'and custom ='"+cusmneaa+ "' and RTNF
            // ='" +rtnf +"'";
            // //String spq ="Select benfi,benfyact,ifsccb,custom,sno,rtnf from
            // extsrtgs where custom ='"+cusmneaa+ "' and RTNF ='" +rtnf +"'";
            // //Loggers.general().info(LOG,"spq - "+spq);
            // PreparedStatement peco = con.prepareStatement(spq);
            // PreparedStatement peco1 = con.prepareStatement(spq);
            // /*peco.setString(1, snoo);
            // peco.setString(2, cusmneaa);
            // peco.setString(3, rtnf);*/
            // ResultSet dmsr1 = peco.executeQuery();
            // ResultSet dmsr2 = peco1.executeQuery();
            // //Loggers.general().info(LOG,"dmsr2 - "+dmsr2.next());
            // while (dmsr1.next()) {
            // //Loggers.general().info(LOG,"Supply chainL while Started");
            // //Loggers.general().info(LOG,"BENNAME_Name"+dmsr1.getString("custom").trim());
            // setBENNAME_Name((dmsr1.getString("custom").trim()));
            // //Loggers.general().info(LOG,"BENNEF_Name"+dmsr1.getString("benfi").trim());
            // setBENEF_Name((dmsr1.getString("benfi").trim()));
            // //Loggers.general().info(LOG,"BENACC_Name"+dmsr1.getString("benfyact").trim());
            // setBENACC_Name((dmsr1.getString("benfyact").trim()));
            // //Loggers.general().info(LOG,"IFSCCO_Name"+dmsr1.getString("ifsccb").trim());
            // setIFSCCO_Name((dmsr1.getString("ifsccb").trim()));
            // //Loggers.general().info(LOG,"BENSNO_Name"+dmsr1.getString("sno").trim());
            // setBENSNO_Name((dmsr1.getString("sno").trim()));
            // }
            // //Loggers.general().info(LOG,"Supply chainL while Ended");
            // dmsr1.close();
            // dmsr2.close();
            // peco.close();
            // peco1.close();
            // con.close();
            // }catch(Exception e)
            // {
            // //Loggers.general().info(LOG,"Error is Catched "+e.getMessage());
            // }
            // }
            //
            // //Loggers.general().info(LOG,"benei end");
            return true;
      }

      /*
       * public void ondisplayvalINVDISCREclayButton() { Connection con=null;
       * PreparedStatement peco=null; ResultSet dmsr1=null;
       *
       * //Loggers.general().info(LOG,"ondisplayvalSupplychainsellButton started"); //
       * setBENNAME_Name(null); String cusmneaa = getBENNAME_Name().trim();
       * setBENEF_Name(null); setBENACC_Name(null); setIFSCCO_Name(null);
       *
       * //setBENSNO_Name(null); String rtnf = "rtnf"; String rtnfs =
       * getDriverWrapper().getEventFieldAsText("PUO1","s","");
       * if((rtnfs.trim().equalsIgnoreCase("RTGS"))||(rtnfs.trim().
       * equalsIgnoreCase("RTG"))||(rtnfs.trim().equalsIgnoreCase("RTS"))) { rtnf
       * = "RTGS"; } else
       * if(rtnfs.trim().equalsIgnoreCase("NEFT")||(rtnfs.trim().equalsIgnoreCase(
       * "NFT"))||(rtnfs.trim().equalsIgnoreCase("NEF"))){ rtnf ="NEFT"; }
       *
       * if(getBENSNO_Name().length()==0){ try { con
       * =ConnectionMaster.getConnection(); String spq =
       * "Select benfi,benfyact,ifsccb,custom,sno,rtnf from extsrtgs where custom ='"
       * +cusmneaa+"'and RTNF ='" +rtnf+"'"; peco = con.prepareStatement(spq);
       * peco.setString(1,cusmneaa); peco.setString(2,rtnf); dmsr1 =
       * peco.executeQuery(); int size = 0; while (dmsr1.next()) {
       * setBENEF_Name((dmsr1.getString(1).trim()));
       * setBENACC_Name(dmsr1.getString(2).trim());
       * setIFSCCO_Name((dmsr1.getString(3).trim()));
       * setBENNAME_Name((dmsr1.getString(4).trim())); size = size + 1; }
       * //Loggers.general().info(LOG,"size - "+size); dmsr1.close(); peco.close();
       * con.close(); if(size>=2) { //Loggers.general().info(LOG,
       * "setting null value occurs" ); //setBENSNO_Name(null);
       * setBENEF_Name(null); setBENACC_Name(null); setIFSCCO_Name(null);
       *
       * } } catch(Exception e) { e.getMessage(); }} else{ try{ con
       * =ConnectionMaster.getConnection(); cusmneaa = getBENNAME_Name().trim();
       * String snoo =getBENSNO_Name().trim(); String spq =
       * "Select benfi,benfyact,ifsccb,custom,sno,rtnf from extsrtgs where sno ='"
       * +snoo+"'and custom ='"+cusmneaa+ "' and RTNF ='" +rtnf +"'"; peco =
       * con.prepareStatement(spq); peco.setString(1, snoo); peco.setString(2,
       * cusmneaa); peco.setString(3, rtnf); dmsr1 = peco.executeQuery(); while
       * (dmsr1.next()) { setBENNAME_Name((dmsr1.getString(4).trim()));
       * setBENEF_Name((dmsr1.getString(1).trim()));
       * setBENACC_Name((dmsr1.getString(2).trim()));
       * setIFSCCO_Name((dmsr1.getString(3).trim()));
       * setBENSNO_Name((dmsr1.getString(5).trim())); } } catch(Exception e) {
       * //Loggers.general().info(LOG,"Error catched"+e.getMessage()); } }
       * //Loggers.general().info(LOG,"ondisplayvalSupplychainsellButton end");
       *
       * }
       */
      //Button to fetch rtgs details added by Vishal G
      public void ondisplayvalINVDISCREclayButton() {

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
            String Ifsc = getIFSCCO_Name().trim();

            if (!Ifsc.trim().equalsIgnoreCase("") || Ifsc != null) {
                  try {
                        con = ConnectionMaster.getConnection();
                        String query = "select count(*) from EXTIFSCC where IFSC='" + Ifsc + "'";
                        // Loggers.general().info(LOG,"query for IFSC button" + query);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              Loggers.general().info(LOG,"query for IFSC button" + query);
                        }
                        int count = 0;
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                              // Loggers.general().info(LOG,"Entered while");
                              count = rs1.getInt(1);
                              // Loggers.general().info(LOG,"value of count in while " + count);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG,"value of count in while " + count);
                              }
                        }

                        if (count == 0) {

                              // Loggers.general().info(LOG,"IFSC Code is Not valid ");
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
                                    // Loggers.general().info(LOG,"IFSC Code is valid");
                                    con = ConnectionMaster.getConnection();
                                    String spq = "Select trim(BANK),trim(BRANCH),trim(CITY) from EXTIFSCC where IFSC ='" + Ifsc
                                                + "'";
                                    // Loggers.general().info(LOG,spq);
                                    ps1 = con.prepareStatement(spq);
                                    dmsr1 = ps1.executeQuery();
                                    while (dmsr1.next()) {
                                          setBENBAK((dmsr1.getString(1).trim()));
                                          setBENBRN(dmsr1.getString(2).trim());
                                          setBENCITY((dmsr1.getString(3).trim()));

                                    }

                              } catch (Exception e) {

                                    // Loggers.general().info(LOG,"event catch");
                              }

                        }
                  } catch (Exception e) {

                        // Loggers.general().info(LOG,"event catch");
                  }
                  
                  try {
                        
                        String cust = getDriverWrapper().getEventFieldAsText("INVSC", "s", "");
                        String benname= getDriverWrapper().getEventFieldAsText("INVSN", "s", "");
                        if (!cust.trim().equalsIgnoreCase("") || cust != null) {
                              con = ConnectionMaster.getConnection();
                                String query="SELECT BENCITY, BENBRN , BENBANK, IFSCCO , BENACC , BENTYP  FROM EXTCUST where CUST = '"+cust+"'";
                               System.out.println(query);
                               ps1 = con.prepareStatement(query);
                               rs1 = ps1.executeQuery();
                               while(rs1.next()) {

                                          setBENCITY(rs1.getString(1));
                                          setBENBRN(rs1.getString(2));
                                          setBENBAK(rs1.getString(3));
                                          setIFSCCO_Name(rs1.getString(4));
                                          setBENACC_Name(rs1.getString(5));
                                          setBENTYP(rs1.getString(6));
                                    //    setBENNAME_Name(rs1.getString(7));
                                          System.out.println("INSIDE GET VALUES BUTTON");
                               }
                                      setBENNAME_Name(benname);
                        }
                        
                  }catch (Exception e) {
                              e.printStackTrace();
                  }
                  
                  finally {
                        try {

                              if (dmsr1 != null)
                                    dmsr1.close();
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
                  // //Loggers.general().info(LOG,"IFSC code is blak");
            }
            

      }
      //Button to fetch rtgs details added by Vishal G
      public void ondisplayvalREVFACCREclayButton() {

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
            String Ifsc = getIFSCCO_Name();

            if (!Ifsc.trim().equalsIgnoreCase("") || Ifsc != null) {
                  try {
                        con = ConnectionMaster.getConnection();
                        String query = "select count(*) from EXTIFSCC where IFSC='" + Ifsc + "'";
                        // Loggers.general().info(LOG,"query for IFSC button" + query);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              Loggers.general().info(LOG,"query for IFSC button" + query);
                        }
                        int count = 0;
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                              // Loggers.general().info(LOG,"Entered while");
                              count = rs1.getInt(1);
                              // Loggers.general().info(LOG,"value of count in while " + count);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG,"value of count in while " + count);
                              }
                        }

                        if (count == 0) {

                              // Loggers.general().info(LOG,"IFSC Code is Not valid ");
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
                                    // Loggers.general().info(LOG,"IFSC Code is valid");
                                    con = ConnectionMaster.getConnection();
                                    String spq = "Select trim(BANK),trim(BRANCH),trim(CITY) from EXTIFSCC where IFSC ='" + Ifsc
                                                + "'";
                                    // Loggers.general().info(LOG,spq);
                                    ps1 = con.prepareStatement(spq);
                                    dmsr1 = ps1.executeQuery();
                                    while (dmsr1.next()) {
                                          setBENBAK((dmsr1.getString(1).trim()));
                                          setBENBRN(dmsr1.getString(2).trim());
                                          setBENCITY((dmsr1.getString(3).trim()));

                                    }

                              } catch (Exception e) {

                                    // Loggers.general().info(LOG,"event catch");
                              }

                        }
                  } catch (Exception e) {

                        // Loggers.general().info(LOG,"event catch");
                  }
          try {
                        
                        String cust = getDriverWrapper().getEventFieldAsText("INVSC", "s", "");
                        if (!cust.trim().equalsIgnoreCase("") || cust != null) {
                              con = ConnectionMaster.getConnection();
                                String query="SELECT BENCITY, BENBRN , BENBANK, IFSCCO , BENACC , BENTYP, BENNAME FROM EXTCUST where CUST = '"+cust+"'";
                               System.out.println(query);
                               ps1 = con.prepareStatement(query);
                               rs1 = ps1.executeQuery();
                               while(rs1.next()) {

                                          setBENCITY(rs1.getString(1));
                                          setBENBRN(rs1.getString(2));
                                          setBENBAK(rs1.getString(3));
                                          setIFSCCO_Name(rs1.getString(4));
                                          setBENACC_Name(rs1.getString(5));
                                          setBENTYP(rs1.getString(6));
                                          setBENNAME_Name(rs1.getString(7));
                                          System.out.println("INSIDE GET VALUES BUTTON of Buyer Centric");
                               }
                        }
                        
                  }catch (Exception e) {
                              e.printStackTrace();
                  }
                  finally {
                        try {

                              if (dmsr1 != null)
                                    dmsr1.close();
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
                  // //Loggers.general().info(LOG,"IFSC code is blak");
            }

      }
      //Button to fetch rtgs details added by Vishal G
      public void ondisplayvalIRFCREATElayButton() {

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
            String Ifsc = getIFSCCO_Name();

            if (!Ifsc.trim().equalsIgnoreCase("") || Ifsc != null) {
                  try {
                        con = ConnectionMaster.getConnection();
                        String query = "select count(*) from EXTIFSCC where IFSC='" + Ifsc + "'";
                        // Loggers.general().info(LOG,"query for IFSC button" + query);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              Loggers.general().info(LOG,"query for IFSC button" + query);
                        }
                        int count = 0;
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                              // Loggers.general().info(LOG,"Entered while");
                              count = rs1.getInt(1);
                              // Loggers.general().info(LOG,"value of count in while " + count);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG,"value of count in while " + count);
                              }
                        }

                        if (count == 0) {

                              // Loggers.general().info(LOG,"IFSC Code is Not valid ");
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
                                    // Loggers.general().info(LOG,"IFSC Code is valid");
                                    con = ConnectionMaster.getConnection();
                                    String spq = "Select trim(BANK),trim(BRANCH),trim(CITY) from EXTIFSCC where IFSC ='" + Ifsc
                                                + "'";
                                    // Loggers.general().info(LOG,spq);
                                    ps1 = con.prepareStatement(spq);
                                    dmsr1 = ps1.executeQuery();
                                    while (dmsr1.next()) {
                                          setBENBAK((dmsr1.getString(1).trim()));
                                          setBENBRN(dmsr1.getString(2).trim());
                                          setBENCITY((dmsr1.getString(3).trim()));

                                    }

                              } catch (Exception e) {

                                    // Loggers.general().info(LOG,"event catch");
                              }

                        }
                  } catch (Exception e) {

                        // Loggers.general().info(LOG,"event catch");
                  }
    try {
                        
                        String cust = getDriverWrapper().getEventFieldAsText("INVSC", "s", "");
                        String benname= getDriverWrapper().getEventFieldAsText("INVSN", "s", "");
                        if (!cust.trim().equalsIgnoreCase("") || cust != null) {
                              con = ConnectionMaster.getConnection();
                                String query="SELECT BENCITY, BENBRN , BENBANK, IFSCCO , BENACC , BENTYP  FROM EXTCUST where CUST = '"+cust+"'";
                               System.out.println(query);
                               ps1 = con.prepareStatement(query);
                               rs1 = ps1.executeQuery();
                               while(rs1.next()) {

                                          setBENCITY(rs1.getString(1));
                                          setBENBRN(rs1.getString(2));
                                          setBENBAK(rs1.getString(3));
                                          setIFSCCO_Name(rs1.getString(4));
                                          setBENACC_Name(rs1.getString(5));
                                        setBENTYP(rs1.getString(6));
                                    //    setBENNAME_Name(rs1.getString(7));
                                          System.out.println("INSIDE GET VALUES BUTTON of Buyer Centric");
                               }
                                       setBENNAME_Name(benname);
                        }
                        
                  }catch (Exception e) {
                              e.printStackTrace();
                  }
                  finally {
                        try {

                              if (dmsr1 != null)
                                    dmsr1.close();
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
                  // //Loggers.general().info(LOG,"IFSC code is blak");
            }

      }

}