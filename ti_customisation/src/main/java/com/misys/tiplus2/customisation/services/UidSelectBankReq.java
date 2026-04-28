package com.misys.tiplus2.customisation.services;
 
public class UidSelectBankReq {

	private String requestType;

	private String msgid;

	private UidSelectBankReqData data;

	public String getRequestType() {

		return requestType;

	}

	public void setRequestType(String requestType) {

		this.requestType = requestType;

	}

	public String getMsgid() {

		return msgid;

	}

	public void setMsgid(String msgid) {

		this.msgid = msgid;

	}

	public UidSelectBankReqData getData() {

		return data;

	}

	public void setData(UidSelectBankReqData data) {

		this.data = data;

	}

}