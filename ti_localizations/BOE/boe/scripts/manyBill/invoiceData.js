$(document).ready(function() {
 
	$(function() {
 
		var buttonType = $("#buttonType").val();
 
		if (buttonType == 'View') {
 
			$("#formId :input").attr('readonly', true);
 
			$("#okButton").hide();
 
		} else if (buttonType == 'Delete') {
 
			$("#formId :input").attr('readonly', true);
 
		} else if (buttonType == 'Update') {
 
			$("#invSno").attr('readonly', true);
 
			$("#invNo").attr('readonly', true);
 
			$("#billRefNo").attr('readonly', true);
 
			$("#portcode").attr('readonly', true);
 
			$("#billEntryDateId").attr('readonly', true);
 
		}
 
	});
 
});
 
function isNumber(evt) {
 
	evt = (evt) ? evt : window.event;
	var charCode = (evt.which) ? evt.which : evt.keyCode;
	if (charCode > 31 && (charCode < 45 || charCode > 57)) {
		return false;
	}
	return true;
}
 
function alphanumeric(val) {
	val = (val) ? val : window.event;
	var charCode = (val.which) ? val.which : val.keyCode;
	if ((charCode > 47 && charCode < 58) || (charCode > 64 && charCode < 91)
			|| (charCode > 96 && charCode < 123)) {
		return true;
	}
	return false;
}
 
function closeButton() {
	$("#formId").attr("action", "backBOEData");
	$("#formId").submit();
}
 
function insertManualInvoiceData() {
	$("#formId").attr("action", "insertManualInvoiceData");
	$("#formId").submit();
}
 
function manualCloseButton() {
	$("#formId").attr("action", "manualCloseButton");
	$("#formId").submit();
}
 
function updateInvoiceDate() {
	$("#formId").attr("action", "updateInvoiceDate");
	$("#formId").submit();
}