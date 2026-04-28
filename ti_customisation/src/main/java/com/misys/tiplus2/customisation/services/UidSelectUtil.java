package com.misys.tiplus2.customisation.services;
 
import java.util.HashMap;

import java.util.Map;
 
import com.google.gson.Gson;

import com.google.gson.GsonBuilder;
 
public class UidSelectUtil {
 
	static String bankRequestJson = "";

	static String bankEncRequest = "";

	static String EncrequestJson = "";

	static UidSelectBankReq aRequestHeader = new UidSelectBankReq();

	static UidSelectBankReqData aRequestData = new UidSelectBankReqData();

	static UidSelectBankRes bankResponse = new UidSelectBankRes();

	static Gson aGson = new GsonBuilder().disableHtmlEscaping().create();
 
	public static Map<String, String> getUidDetailsFromAPI(String tranDate, String tranSubType, String tranReference,

			String uniqueID) {

		String plainBankRequest = "";

		String tempEncRequest = "";

		String encBankRequest = "";

		String encBankResponse = "";

		String plainBankResponse = "";

		ServiceUtility.getProperties();

		String UidSelectURL = ServiceUtility.TBProperties.get("UID_SELECT_URL");

		String UidSelectKey = ServiceUtility.TBProperties.get("UID_SELECT_KEY");

		Map<String, String> responseTokens = null;

		try {
 
			System.out.println("UidSelectURL & UidSelectKey  --> " + UidSelectURL + " & " + UidSelectKey);
 
			plainBankRequest = generateUidSelectBankRequest(tranDate, tranSubType, tranReference, uniqueID);

			System.out.println("UidSelect Bank Request in Json Format -->" + plainBankRequest);
 
			// storing enc msg in general format not json

			tempEncRequest = ServiceUtility.generateEncryptBankRequest(plainBankRequest, UidSelectKey);
 
			// storing enc msg in json format along with msgid

			encBankRequest = generateEncryptedUidSelectJson(tempEncRequest);
 
			System.out.println("UidSelect Bank Enc Request -->" + encBankRequest);
 
			encBankResponse = ServiceUtility.getBankFinResponse(encBankRequest, UidSelectURL);
 
			// decrypting the msg to json format

			plainBankResponse = ServiceUtility.generateDecryptBankResponse(encBankResponse, UidSelectKey);

			System.out.println("UidSelect Bank Json Response -->" + plainBankResponse);
 
			responseTokens = getResponseTokenDetails(plainBankResponse);
 
		} catch (Exception e) {

			e.printStackTrace();

		}

		return responseTokens;

	}
 
	public static String generateUidSelectBankRequest(String tranDate, String tranSubType, String tranReference,

			String uniqueID) {

		String bankRequest = null;
 
		try {

			String option = "3";

			String sequence = ServiceUtility.getSqlLocalDateTime().toString();

			sequence = sequence.replaceAll("[- :.]", "");
 
			System.out.println("uniqueID: " + uniqueID);

			System.out.println("tranReference: " + tranReference);
 
			aRequestData.setOption(option);

			aRequestData.setField_61_2_date(tranDate);

			aRequestData.setField_61_6_tran_subtype(tranSubType);

			aRequestData.setField_61_7_ref_num(tranReference);

			aRequestData.setUnqId(uniqueID);
 
			aRequestHeader.setRequestType("0");

			aRequestHeader.setMsgid(sequence);

			aRequestHeader.setData(aRequestData);

			bankRequest = aGson.toJson(aRequestHeader).trim();

			System.out.println("bankRequest of UidSelect: " + bankRequest);
 
		} catch (Exception e) {

			e.printStackTrace();

		}
 
		return bankRequest;

	}
 
	public static String generateEncryptedUidSelectJson(String bankEncRequest) {

		String reqJson;
 
		ServiceUtility encryptedReq = new ServiceUtility(bankEncRequest, aRequestHeader.getMsgid());

		reqJson = aGson.toJson(encryptedReq).trim();

		return reqJson;

	}
 
	public static Map<String, String> getResponseTokenDetails(String plainFtrtSelectBankResponse) {
 
		Map<String, String> rateTokens = new HashMap<String, String>();

		try {
 
			bankResponse = aGson.fromJson(plainFtrtSelectBankResponse, UidSelectBankRes.class);
 
			String status = bankResponse.getData().getExecuteFinacleScript_CustomData().getSuccessorfailure();
 
			rateTokens.put("Status", status);

			System.out.println("status -->" + status);
 
			if (status.equalsIgnoreCase("S")) {
 
				UidSelectBankResCustomDataDetails ftrtSelectBankResCustomDataDetails = bankResponse.getData()

						.getExecuteFinacleScript_CustomData().getStatementTransactionDetail();
 
				rateTokens.put("AddnlInfo", ftrtSelectBankResCustomDataDetails.getFIELD_61_9_ADDNL_INFO());

				rateTokens.put("NostroMnemonic", ftrtSelectBankResCustomDataDetails.getNOSTRO_MNEMONIC());

				rateTokens.put("OriginalAmt", ftrtSelectBankResCustomDataDetails.getFIELD_61_5_ORIGINAL_AMOUNT());

				rateTokens.put("SendEmailFlg", ftrtSelectBankResCustomDataDetails.getSEND_EMAIL_FLAG());

				rateTokens.put("FboIdNum", ftrtSelectBankResCustomDataDetails.getFBO_ID_NUM_DET());

				rateTokens.put("TranSubType", ftrtSelectBankResCustomDataDetails.getFIELD_61_6_TRAN_SUBTYPE());

				rateTokens.put("MapType", ftrtSelectBankResCustomDataDetails.getMAP_TYPE());

				rateTokens.put("Ccy", ftrtSelectBankResCustomDataDetails.getFIELD_61_4_CCY());

				rateTokens.put("TranType", ftrtSelectBankResCustomDataDetails.getFIELD_61_6_TRAN_TYPE());

				rateTokens.put("RefNum", ftrtSelectBankResCustomDataDetails.getFIELD_61_7_REF_NUM());

				rateTokens.put("BankId", ftrtSelectBankResCustomDataDetails.getBANK_ID());

				rateTokens.put("Remarks1", ftrtSelectBankResCustomDataDetails.getREMARKS());

				rateTokens.put("Date", ftrtSelectBankResCustomDataDetails.getFIELD_61_2_DATE());

				rateTokens.put("IdType", ftrtSelectBankResCustomDataDetails.getID_TYPE());

				rateTokens.put("AccNumber", ftrtSelectBankResCustomDataDetails.getFIELD_25_ACC_NUMBER());

				rateTokens.put("MatchType", ftrtSelectBankResCustomDataDetails.getMATCH_TYPE());

				rateTokens.put("NostroMirAcc", ftrtSelectBankResCustomDataDetails.getNOSTRO_MIRROR_ACC());

				rateTokens.put("LchgMenu", ftrtSelectBankResCustomDataDetails.getLCHG_MENU());

				rateTokens.put("EntityCreFlg", ftrtSelectBankResCustomDataDetails.getENTITY_CRE_FLG());

				rateTokens.put("SolId", ftrtSelectBankResCustomDataDetails.getSOL_ID());

				rateTokens.put("SolIdC", ftrtSelectBankResCustomDataDetails.getSOL_ID_C());

				rateTokens.put("ValueDate", ftrtSelectBankResCustomDataDetails.getFIELD_61_1_VALUE_DATE());

				rateTokens.put("solIdB", ftrtSelectBankResCustomDataDetails.getSOL_ID_B());

				rateTokens.put("IntRefNum", ftrtSelectBankResCustomDataDetails.getFIELD_61_8_INT_REF_NUM());

				rateTokens.put("LchgUserId", ftrtSelectBankResCustomDataDetails.getLCHG_USER_ID());

				rateTokens.put("RefNumMapped", ftrtSelectBankResCustomDataDetails.getREF_NUM_MAPPED());

				rateTokens.put("MatchStatus", ftrtSelectBankResCustomDataDetails.getMATCHING_STATUS());

				rateTokens.put("Utilization", ftrtSelectBankResCustomDataDetails.getUTILIZATION());

				rateTokens.put("RetOfFunds", ftrtSelectBankResCustomDataDetails.getRET_OF_FUNDS());

				rateTokens.put("UnqId", ftrtSelectBankResCustomDataDetails.getUNQ_ID());

				rateTokens.put("CifId", ftrtSelectBankResCustomDataDetails.getCIF_ID());

				rateTokens.put("FboIdNumHdr", ftrtSelectBankResCustomDataDetails.getFBO_ID_NUM_HDR());

				rateTokens.put("AddnlInfoDet", ftrtSelectBankResCustomDataDetails.getFIELD_86_ADDNL_INFO_DET());

				rateTokens.put("RcreUserId", ftrtSelectBankResCustomDataDetails.getRCRE_USER_ID());

				rateTokens.put("EarlySolId", ftrtSelectBankResCustomDataDetails.getEARLY_SOL_ID());

				rateTokens.put("Remarks2", ftrtSelectBankResCustomDataDetails.getREMARKS_2());

				rateTokens.put("DelFlg", ftrtSelectBankResCustomDataDetails.getDEL_FLG());

				rateTokens.put("RowId", ftrtSelectBankResCustomDataDetails.getROW_ID());

				rateTokens.put("OutstandingAmt", ftrtSelectBankResCustomDataDetails.getOUTSTANDING_AMOUNT());

				rateTokens.put("RcreTime", ftrtSelectBankResCustomDataDetails.getRCRE_TIME());

				rateTokens.put("RequestDate", ftrtSelectBankResCustomDataDetails.getFIELD_61_3_DR_CR_INDICATOR());

				rateTokens.put("UidCcy", ftrtSelectBankResCustomDataDetails.getCCY_FOR_UID());

				rateTokens.put("LchgTime", ftrtSelectBankResCustomDataDetails.getLCHG_TIME());

				rateTokens.put("SingleConCharge", ftrtSelectBankResCustomDataDetails.getSINGLE_CONSOLIDATED_CHARGE());

				rateTokens.put("TranCode", ftrtSelectBankResCustomDataDetails.getTRAN_CODE());
 
				System.out.println("Stdtc API for UnqId: " + rateTokens.get("UnqId"));
 
				System.out.println(

						"OriginalAmt & Ccy: " + rateTokens.get("OriginalAmt") + " & " + rateTokens.get("UidCcy"));
 
			}
 
		} catch (Exception e) {

			e.printStackTrace();

		}

		return rateTokens;

	}
 
}