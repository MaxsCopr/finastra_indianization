package com.misys.tiplus2.customisation.pane;
 
 
import java.sql.Connection;

import java.sql.PreparedStatement;

import java.sql.ResultSet;
 
import com.misys.tiplus2.customisation.extension.EventExtension;

import com.misys.tiplus2.customisation.extension.OdcFEC;

import com.misys.tiplus2.apps.ti.kernel.extpm.pane.ExtEventExtensionDriverPWrapper;

import com.misys.tiplus2.customisation.extension.ConnectionMaster;

import com.misys.tiplus2.enigma.lang.datatype.EnigmaArray;

import com.misys.tiplus2.enigma.customisation.validation.ValidationTexts;

import com.misys.tiplus2.foundations.lang.logging.Loggers;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import com.misys.tiplus2.foundations.lang.logging.Loggers;
 
public class ServiceTAXCALC extends EventPane{

	Connection con=null;

	PreparedStatement dmsp=null;

	PreparedStatement dmsp1=null;

	ResultSet dmsr=null;

	ResultSet dmsr1=null;

	String swachhCharge="";

	String swachhCharge1="";

	private static final Logger LOG = LoggerFactory.getLogger(ServiceTAXCALC.class);

}