package com.misys.tiplus2.customisation.services;
 
import java.sql.Connection;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.Timestamp;

import java.util.Date;

import java.util.LinkedHashMap;
 
import org.apache.commons.httpclient.HttpClient;

import org.apache.commons.httpclient.methods.PostMethod;

import org.apache.commons.httpclient.methods.StringRequestEntity;
 
import com.infrasoft.kiya.security.EncryptionDecryptionImpl;

import com.misys.tiplus2.customisation.extension.ConnectionMaster;
 
 
public class ServiceUtility {

 
	static EncryptionDecryptionImpl obj = new EncryptionDecryptionImpl();

	public static LinkedHashMap<String, String> TBProperties = new LinkedHashMap<String, String>();

	String reqdata;

	String msgid;

	public ServiceUtility(String reqData,String msgId)

	{

		this.msgid=msgId;

		this.reqdata=reqData;

	}

	public static String getBankFinResponse(String bankEncReq, String url) {
 
		String encResponse = null;

		StringBuffer buffer = new StringBuffer();

		PostMethod post = new PostMethod(url);

		System.out.println("Entering getBankFinResponse");

		try {
 
			StringRequestEntity requestEntity = new StringRequestEntity(bankEncReq, "application/json", "utf-8");

			post.setRequestEntity(requestEntity);

			HttpClient httpclient = new HttpClient();
 
			int result = httpclient.executeMethod(post);
 
			if (result != 200) {

				throw new Exception("Server returned code " + result);

			}

			encResponse = post.getResponseBodyAsString();

			System.out.println("Encrypted Response From Bank-->\n" + encResponse);
 
			System.out.println("Exiting getBankFinResponse");

		} catch (Exception e) {

			System.out.println("Exception in getBankFinResponse:- " + e);

			e.printStackTrace();

		} finally {

			post.releaseConnection();

		}

		return encResponse.trim();
 
	}

	public static void getProperties() {
 
		Connection con = null;

		PreparedStatement pst = null;

		ResultSet rs = null;

		System.out.println(" Entering getProperties ");

		try {

			con = ConnectionMaster.getThemeBridgeConnection();

			String query = "SELECT * FROM Bridgeproperties ";

			pst = con.prepareStatement(query);

			rs = pst.executeQuery();

			while (rs.next()) {

				TBProperties.put(rs.getString("key").trim(), rs.getString("value").trim());

			}

			System.out.println(" Size of Bridgeproperties From DB ---->" + TBProperties.size());

			System.out.println(" Entering getProperties ");

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			ConnectionMaster.surrenderDB(con, pst, rs);

		}
 
	}	


	public static String generateEncryptBankRequest(String bankRequestJson, String key) {

		String encMes = null;

		System.out.println(" Entering generateEncryptBankRequest ");

		try {
 
			encMes = obj.encryptMessage(bankRequestJson, key);

			System.out.println(" Exiting generateEncryptBankRequest ");

		} catch (Exception e) {

			System.out.println(" Error in  generateEncryptBankRequest --->" + e.getMessage());

			e.printStackTrace();

		}

		return encMes;
 
		// TODO Auto-generated method stub
 
	}
 
	public static String generateDecryptBankResponse(String bankEncRes, String key) {
 
		String decMes = null;

		System.out.println(" Entering generateDecryptBankResponse ");

		try {
 
			decMes = obj.decryptMessage(bankEncRes, key);

			System.out.println(" Exiting generateDecryptBankResponse ");

		} catch (Exception e) {

			System.out.println(" Error in  generateDecryptBankResponse --->" + e.getMessage());

			e.printStackTrace();

		}

		return decMes;
 
	}	

	public static Timestamp getSqlLocalDateTime() {

		Date date = new Date();

		long t = date.getTime();

		Timestamp sqlTimestamp = new Timestamp(t);

		return sqlTimestamp;

	}
 
}