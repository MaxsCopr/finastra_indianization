package com.misys.tiplus2.customisation.extension;
 
import java.sql.Connection;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import com.misys.tiplus2.enigma.customisation.control.ExtendedHyperlinkControlWrapper;

import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails;

import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.ErrorType;
 
public class Customer extends CustomerExtension {

	public boolean onPostInitialise() {

	try {

		System.out.println("Inside onPostInitialise");

		String gfpfValues = "";

		String customerBlkLink = "";
 
		String customerMnemonic = getDriverWrapper().getCustomerFieldAsText("Mnemonic").trim();

		System.out.println("customerMnemonic in onPostInitialise====>" + customerMnemonic);
 
		customerBlkLink = getCustomerBlkLink(customerMnemonic);

		System.out.println("customerBlkLink---->" + customerBlkLink);
 
		

		ExtendedHyperlinkControlWrapper csmreftrack1 = getPane().getCtlBlockunblockCUSTcLayHyperlink();

		csmreftrack1.setUrl(customerBlkLink);
 
		gfpfValues = getGfpfValues(customerMnemonic);

		System.out.println("gfpfValues onPostInitialise---->" + gfpfValues);
 
		if(!gfpfValues.equalsIgnoreCase(null) || !gfpfValues.isEmpty()){


		if (gfpfValues.equalsIgnoreCase("Y")) {

			getWrapper().setBLKUNBLK(true);

		} else {

			getWrapper().setBLKUNBLK(false);

		}

		System.out.println("Block Unblock Value onPostInitialise of Customer class--->" + getWrapper().getBLKUNBLK());

		}

	} catch (Exception e) {

		System.out.println("Exception in onPostInitialise " + e.getMessage());

		e.printStackTrace();
 
	}

     return false;

	}
 
	public void onValidate(ValidationDetails validationDetails) {
 
		String customerMnemonic = getDriverWrapper().getCustomerFieldAsText("Mnemonic").trim();

		System.out.println("customerMnemonic====>" + customerMnemonic);

		String gfpfValues = "";

		String customerBlkLink = "";

		String blockValue="";
 
		try {
 
			ConnectionMaster aConnectionMaster = new ConnectionMaster();
 
			customerBlkLink = getCustomerBlkLink(customerMnemonic);

			System.out.println("customerBlkLink---->" + customerBlkLink);
 
			ExtendedHyperlinkControlWrapper csmreftrack1 = getPane().getCtlBlockunblockCUSTcLayHyperlink();

			csmreftrack1.setUrl(customerBlkLink);
 
			gfpfValues = getGfpfValues(customerMnemonic);

			System.out.println("gfpfValues---->" + gfpfValues);
 
			if(gfpfValues!=null && !gfpfValues.isEmpty() && !gfpfValues.trim().equals("")){
 
			String blockValues = getWrapper().getBLKUNBLK().toString();
 
			System.out.println("blockValue--->" + blockValue);

			if(blockValues.equalsIgnoreCase("true")){

				blockValue="Y";

			}

			if(blockValues.equalsIgnoreCase("false")){

				blockValue="N";

			}

			if (!blockValue.equalsIgnoreCase(gfpfValues)) {

				System.out.println("Inside when mismatch blockValue--->" + blockValue + "gfpfValues--->" + gfpfValues);
 
				validationDetails.addError(ErrorType.Other,

						"Block/UnBlock Values are not Updated immediately in TI.Click Cancel in Customer Details screen and proceed till Zone for the changes to be reflected");
 
			} else {

				System.out.println("Else blockValue--->" + blockValue + "gfpfValues--->" + gfpfValues);
 
			}

			}

		} catch (Exception e) {

			System.out.println("Exception in onValidate " + e.getMessage());

			e.printStackTrace();
 
		}

	}

	public String getCustomerBlkLink(String customerMnemonic) {

		String displayVal = "";

		Connection con = null;

		PreparedStatement pst = null;

		ResultSet rs = null;

		String query = "";

		String returns="";

		try {
 
			con = ConnectionMaster.getConnection();

			if(con != null){

			query = "SELECT VALUE1 FROM ETT_PARAMETER_TBL WHERE PARAMETER_ID='CUSTOBLK'";			

			pst = con.prepareStatement(query);			

			rs = pst.executeQuery();

			while (rs.next()) {

				 displayVal=rs.getString(1);

			}

		returns = displayVal + "?customerMnemonic=" +customerMnemonic;

		System.out.println("return value---> "+returns);

			}

		}catch(Exception e){

				System.out.println("Exception in getting Link--->"+e.getMessage());

			}finally {

				ConnectionMaster.surrenderDB(con, pst, rs);

			}
 
		return returns;
 
	}


public String getGfpfValues(String customerMnemonic){

 
		Connection con = null;

		PreparedStatement ps1 = null;

		ResultSet rs1 = null;

		String gfpf=null;

		try {
 
			con = ConnectionMaster.getConnection();

			String query = "SELECT GFCUB FROM GFPF WHERE GFCUS1='"+customerMnemonic+"'";

			ps1 = con.prepareStatement(query);

			rs1 = ps1.executeQuery();

			if (rs1.next()) {

				gfpf=rs1.getString(1).trim();

			}

	}catch(Exception e){

		e.printStackTrace();

		System.out.println("Exception in getExtCustValues"+e.getMessage());

		}finally{

			ConnectionMaster.surrenderDB(con,ps1,rs1);

		}

		return gfpf;

	}

}