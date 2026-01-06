$(document).ready(function() {	

	$(function() {		
		var buttonType = $("#buttonType").val();
		if(buttonType == 'View'){
			$("#formId :input").attr('readonly', true);
			$("#okButton").hide();
		}else if(buttonType == 'Delete'){
			$("#formId :input").attr('readonly', true);
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
 
 
function insertInvoiceDatatoTable() {	
	$("#formId").attr("action", "insertInvoiceManyBillToData");
	$("#formId").submit();
}
 
function closeButton(){	
	$("#formId").attr("action", "backManyBillData");
	$("#formId").submit();	
}
 
 