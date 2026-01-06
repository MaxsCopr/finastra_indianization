function getDischargePort(){
	$("#formId").attr("action", "getPortOfDischargeMB");
	$("#formId").submit();
}
 
function getShipmentPort() {
	$("#formId").attr("action", "getPortOfShipmentMB");
	$("#formId").submit();
}
 
function pressCloseBtn(){
	var okIdFlag = $('#okIdFlg').val();
	if(okIdFlag == "reject"){
		$('#formId').attr('action', 'viewRejectedRecords');
		$('#toRejectedPage').val("Y");
		$('#formId').submit();
	}else{
		$('#formId').attr('action', 'makerProcess');
		$('#formId').submit();
	}    
}