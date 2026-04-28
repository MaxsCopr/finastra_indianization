package com.misys.tiplus2.customisation.extension;
 
//com.misys.tiplus2.customisation.extension.Invoice

import java.sql.Connection;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.text.SimpleDateFormat;

import java.util.Calendar;

import java.util.Date;
 
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

public class Invoice  extends ConnectionMaster{

	//private static final Logger logger = Logger.getLogger(ConnectionMaster.class.getName());

	private static final Logger LOG = LoggerFactory.getLogger(Invoice.class);

	Connection con=null;

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

			//Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");
 
		}

		if (dailyval.equalsIgnoreCase("NO")) {

			//Link going to add

		}

		return false;
 
	}

	public void onValidate(ValidationDetails validationDetails)

	{

		String strPropName = "MigrationDone";

		String dailyval ="";

		AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP>

		queryRCODE = getDriverWrapper().createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'" );

		////Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");

		EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();

		if (PROPCode != null) {

		 dailyval = PROPCode.getPropval();

			////Loggers.general().info(LOG,"ADDDailyTxnLimit -------->" + PROPCode.getPropval());

		}

		else

		{

			//Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

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

			//Loggers.general().info(LOG,"step_Input check ----->" + step_Input);

			String step_csm = getDriverWrapper().getEventFieldAsText("CSID", "s", "");

			//Loggers.general().info(LOG,"step id check for CSM ----->" + step_csm);


		//GETTING LOB

		try{

			////Loggers.general().info(LOG,"get value for LOB");

			getLob();

		}

		catch(Exception ee)

		{

			//Loggers.general().info(LOG,ee.getMessage() );

		   // //Loggers.general().info(LOG,"LOB Catch");

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

		finally{

			////Loggers.general().info(LOG,"finally LOB ");

		}

 
		

//		////Loggers.general().info(LOG,"Validate Call invoice");

//		try{

//			String programme = getDriverWrapper().getEventFieldAsText("PROGC","s","");

//			////Loggers.general().info(LOG,"Programe Called" + programme);//Programme

//			String programna = getDriverWrapper().getEventFieldAsText("PROGN","s","");

//			////Loggers.general().info(LOG,"Programe Called" + programna);//Program Name

//			String sellc = getDriverWrapper().getEventFieldAsText("INVSC","s","");

//			////Loggers.general().info(LOG,"Programe Called" + sellc);//INVOICE SELLER

//			String sellc1 = getDriverWrapper().getEventFieldAsText("INVSN","s","");

//			////Loggers.general().info(LOG,"Programe Called" + sellc1);//INVOICE SELLER NAME

//			con = ConnectionMaster.getConnection();

//			////Loggers.general().info(LOG,"Connection - "+con);

//			String Inv = getDriverWrapper().getEventFieldAsText("ISS","d","");  // Start date

//			String quer = "SELECT trim(Staleprd) FROM extselldet WHERE progcha='"+programna+"' AND sellcod='"+sellc+"'";

//			//Loggers.general().info(LOG,"query - "+ quer);

//			int sper = 0;

//			PreparedStatement ps1 = con.prepareStatement(quer);

//			ResultSet rs1 = ps1.executeQuery();

//			while(rs1.next()) {

//				////Loggers.general().info(LOG,"While");

//				sper = rs1.getInt(1);

//			}

//			////Loggers.general().info(LOG,"sper"+ sper);

//		

//			

//			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

//			////Loggers.general().info(LOG,Inv);

//			Date date = formatter.parse(Inv);

//			int ant =sper;// Integer.parseInt(sper); 

//			////Loggers.general().info(LOG,ant);

//			Calendar cal = Calendar.getInstance();

//			 cal.setTime(date);

//			 cal.add(Calendar.DATE, ant);

//			//cal.add(Calendar.DAY_OF_MONTH,ant);

//			////Loggers.general().info(LOG,cal.getTime());

//			Date date2=cal.getTime();

//			String stepcd=getDriverWrapper().getEventFieldAsText("ECOI", "s", "").trim();

//			String dt = getDriverWrapper().getEventFieldAsText("TDY","d","");  // ti system date

//			////Loggers.general().info(LOG,dt + " TI dt Date");

//			Date date1 = formatter.parse(dt);

//			////Loggers.general().info(LOG,"date2  :"+date2+"date1  :"+date1);

//				if((step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))){	

//			if(date2.before(date1)){

//				validationDetails.addWarning(WarningType.Other,"Invoice is stale and ineligible for finance  [CM]");

//			}

//			else{

//				//Loggers.general().info(LOG,"The date invoice date is correct");

//			}

//		}	}

//		catch (Exception e) {

//			//Loggers.general().info(LOG,"Exception is "+e.getMessage());

//			////Loggers.general().info(LOG,"Exception is error calender");

//			

//		}

//		finally {

//			try {

//				if (con != null) {

//					con.close();

//					if (ps1 != null)

//						ps1.close();

//					if (rs1 != null)

//						rs1.close();

//				}

//			} catch (SQLException e) {

//				//Loggers.general().info(LOG,"Connection Failed! Check output console");

//				e.printStackTrace();

//			}

//		}

	}

}

}