package com.misys.tiplus2.customisation.services;
 
public class UidSelectBankResCustomData {
 
	private String Message;

	private String successorfailure;

	private UidSelectBankResCustomDataDetails StatementTransactionDetail;

	public String getMessage() {

		return Message;

	}

	public void setMessage(String message) {

		Message = message;

	}

	public String getSuccessorfailure() {

		return successorfailure;

	}

	public void setSuccessorfailure(String successorfailure) {

		this.successorfailure = successorfailure;

	}

	public UidSelectBankResCustomDataDetails getStatementTransactionDetail() {

		return StatementTransactionDetail;

	}

	public void setStatementTransactionDetail(UidSelectBankResCustomDataDetails statementTransactionDetail) {

		StatementTransactionDetail = statementTransactionDetail;

	}

}