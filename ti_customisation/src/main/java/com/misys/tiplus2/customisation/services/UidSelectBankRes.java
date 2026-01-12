package com.misys.tiplus2.customisation.services;
 
public class UidSelectBankRes {
 
	private UidSelectBankResData data;

	private String msgrrn;

	private String msgtime;

	private String msgid;

	private String channelName;

	private String status;

	public UidSelectBankResData getData() {

		return data;

	}

	public void setData(UidSelectBankResData data) {

		this.data = data;

	}

	public String getMsgrrn() {

		return msgrrn;

	}

	public void setMsgrrn(String msgrrn) {

		this.msgrrn = msgrrn;

	}

	public String getMsgtime() {

		return msgtime;

	}

	public void setMsgtime(String msgtime) {

		this.msgtime = msgtime;

	}

	public String getMsgid() {

		return msgid;

	}

	public void setMsgid(String msgid) {

		this.msgid = msgid;

	}

	public String getChannelName() {

		return channelName;

	}

	public void setChannelName(String channelName) {

		this.channelName = channelName;

	}

	public String getStatus() {

		return status;

	}

	public void setStatus(String status) {

		this.status = status;

	}


}