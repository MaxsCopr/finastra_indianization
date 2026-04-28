package com.misys.tiplus2.customisation.extension;
 
import java.sql.CallableStatement;

import java.sql.Connection;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import com.misys.tiplus2.foundations.lang.logging.Loggers;

public class CustomJar {

	int res2 = 0;

	Connection con = null;

	CallableStatement cstmt = null;

	private static final Logger LOG = LoggerFactory.getLogger(CustomJar.class);

	{

		try {

			Loggers.general().info(LOG,"Custom jar called");

			// con = ConnectionMaster.getConnection();

			// String query2 = "{call ETT_CLAIM_EXPIRY_DIARY_UPDATE}";

			// //Loggers.general().info(LOG,"Query 2 is " + query2);

			// cstmt = con.prepareCall(query2);

			// res2 = cstmt.executeUpdate();

		} catch (Exception ee) {

			Loggers.general().info(LOG,ee.getMessage());
 
		} finally {

			Loggers.general().info(LOG,"Custom jar called successfully");

		}

	}

}