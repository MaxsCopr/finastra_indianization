package com.misys.tiplus2.customisation.extension;
 
 
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
 
import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;

import com.misys.tiplus2.enigma.customisation.AdhocQuery;

import com.misys.tiplus2.enigma.customisation.control.ExtendedHyperlinkControlWrapper;

import com.misys.tiplus2.foundations.lang.logging.Loggers;
 
 
public class Master extends MasterExtension {

	private static final Logger LOG = LoggerFactory.getLogger(Master.class);

       public String getmasRefNo() {

              String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");

              Loggers.general () .info(LOG,"Getting Master Reference"+masRefNo);

              return masRefNo;

       }


       public String getDMSLink(String val) {

              Loggers.general () .info(LOG,"DMS link method entered");

              String strLog = "Log";

              String dailyval_Log = "";

              @SuppressWarnings("unchecked")

              AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()

                           .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");

              EXTGENCUSTPROP CodeLog = queryLog.getUnique();

              if (CodeLog != null) {
 
                     dailyval_Log = CodeLog.getPropval();

                     Loggers.general () .info(LOG,"DMS link method if block");

              } else {

                 //Loggers.general().info(LOG,"Executing Else block-------->");

                 Loggers.general () .info(LOG,"DMS link method else block");

              }


              String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");

              String link = "";

              if (dailyval_Log.equalsIgnoreCase("YES")) {

                     //Loggers.general().info(LOG,"DMS link called in connection master");

              }
 
              String strPropName = "DMS";

              String displayVal = "";

              AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()

                           .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");

              // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");

              EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();

              if (PROPCode != null) {
 
                     displayVal = PROPCode.getPropval();

                     //Loggers.general().info(LOG,"DMS URL getting -------->" +displayVal);

                     Loggers.general () .info(LOG,"DMS URL getting -------->" +displayVal);

              } else {

                     //Loggers.general().info(LOG,"DMS URL is empty-------->");

                     Loggers.general () .info(LOG,"DMS URL is empty-------->");
 
              }

        //Loggers.general().info(LOG,"Inside the link method=================>");

        Loggers.general () .info(LOG,"Inside the link method=================>");

        link = displayVal + masRefNo.toString().trim();

              if (dailyval_Log.equalsIgnoreCase("YES")) {

                     //Loggers.general().info(LOG,"DMS link returns this link====> " + link);

                     Loggers.general () .info(LOG,"DMS link returns this link====> " + link);

              }

              return link;        

       }



       public boolean onPostInitialise() {

              //ConnectionMaster conmas = new ConnectionMaster();

              Loggers.general () .info(LOG,"Entered onPostInitialise");

       try {

              Loggers.general () .info(LOG,"Entered try");

              String val = "DMS";

              Loggers.general () .info(LOG,"Entering getDMSLink");

              String DMSHyperlink = getDMSLink(val);

              Loggers.general () .info(LOG,"Exiting getDMSLink");

              ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlDMSINbankREMITTANCEcMasterHyperlink();

              dmsh.setUrl(DMSHyperlink); 

              ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlDMSOUTREMITTANCECMasterHyperlink();

              dmsh1.setUrl(DMSHyperlink); 

              ExtendedHyperlinkControlWrapper dmsh2 = getPane().getCtlDMSINREMITTAANCEcMasterHyperlink();

              dmsh2.setUrl(DMSHyperlink); 

              ExtendedHyperlinkControlWrapper dmsh3 = getPane().getCtlDMSOUREMITTANCEcMasterHyperlink();

              dmsh3.setUrl(DMSHyperlink); 

              ExtendedHyperlinkControlWrapper dmsh4 = getPane().getCtlDMSINGRNTEEcMasterHyperlink();

              dmsh4.setUrl(DMSHyperlink); 

              ExtendedHyperlinkControlWrapper dmsh5 = getPane().getCtlDMSELCcMasterHyperlink();

              dmsh5.setUrl(DMSHyperlink); 

              ExtendedHyperlinkControlWrapper dmsh6 = getPane().getCtlDMSESBLayHyperlink();

              dmsh6.setUrl(DMSHyperlink); 

              ExtendedHyperlinkControlWrapper dmsh7 = getPane().getCtlDMSFINEXPLCMASTERclayHyperlink();

              dmsh7.setUrl(DMSHyperlink); 

              ExtendedHyperlinkControlWrapper dmsh8 = getPane().getCtlDMSFINIMPORTCOLLMclayHyperlink();

              dmsh8.setUrl(DMSHyperlink); 

              ExtendedHyperlinkControlWrapper dmsh9 = getPane().getCtlDMSIMPORTSTBYLCMHyperlink();

              dmsh9.setUrl(DMSHyperlink); 

              ExtendedHyperlinkControlWrapper dmsh10 = getPane().getCtlDMSFINIMPLCMclayHyperlink();

              dmsh10.setUrl(DMSHyperlink); 

              ExtendedHyperlinkControlWrapper dmsh11 = getPane().getCtlDMSFINEXPCOLLMASclayHyperlink();

              dmsh11.setUrl(DMSHyperlink); 

              ExtendedHyperlinkControlWrapper dmsh12 = getPane().getCtlDMSFSAcMasterHyperlink();

              dmsh12.setUrl(DMSHyperlink); 

              ExtendedHyperlinkControlWrapper dmsh13 = getPane().getCtlDMSINCOLLECTIONcMasterlayHyperlink();

              dmsh13.setUrl(DMSHyperlink); 

              ExtendedHyperlinkControlWrapper dmsh14 = getPane().getCtlDMSOUTGRNTEEcMasterHyperlink();

              dmsh14.setUrl(DMSHyperlink); 

              ExtendedHyperlinkControlWrapper dmsh15 = getPane().getCtlDMSILCcMasterHyperlink();

              dmsh15.setUrl(DMSHyperlink); 

              ExtendedHyperlinkControlWrapper dmsh16 = getPane().getCtlDMSOUTCOLLECTIONMASTERclayHyperlink();

              dmsh16.setUrl(DMSHyperlink); 

              ExtendedHyperlinkControlWrapper dmsh17 = getPane().getCtlDMSSHIPRNTEEMHyperlink();

              dmsh17.setUrl(DMSHyperlink); 

              ExtendedHyperlinkControlWrapper dmsh18 = getPane().getCtlDMSFELCOHyperlink();

              dmsh18.setUrl(DMSHyperlink); 

             /* ExtendedHyperlinkControlWrapper dmsh19 = getPane().getCtlDMSFELNOHyperlink();

              dmsh19.setUrl(DMSHyperlink); */

              } catch (Exception ees) {

                ees.printStackTrace();

              }

       Loggers.general () .info(LOG,"Exiting onPostInitialise");

       return false;

       }

}