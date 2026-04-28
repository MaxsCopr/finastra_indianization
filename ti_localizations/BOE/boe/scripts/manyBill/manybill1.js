function getDischargePort(){
	$("#formId").attr("action", "getPortOfDischargeSB");
	$("#formId").submit();
}
 
function getShipmentPort() {
	$("#formId").attr("action", "getPortOfShipmentSB");
	$("#formId").submit();
}
 
 
function pressCloseBtn(){
	var okIdFlag = $('#okIdFlg').val();
	if(okIdFlag == "reject"){
		$('#formId').attr('action', 'viewRejectedRecords');
		$('#toRejectedPage').val("Y");
		$('#formId').submit();
	}else{
		$('#formId').attr('action', 'paymentDataFromView');
		$('#formId').submit();
	}    
}