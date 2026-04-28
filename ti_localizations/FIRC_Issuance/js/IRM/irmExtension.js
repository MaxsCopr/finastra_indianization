/**
* 
*/
 
$(document).ready(function () {	
	var sysDate =  $("#sysProcDate").val().split("-");
	var sysProcDate = new Date(sysDate[2], sysDate[1] - 1, sysDate[0]);
	$("#extensionDate").datepicker({
		changeMonth: true,
	    changeYear : true,
		dateFormat : 'dd/mm/y'
	});	
	$("#letterDate").datepicker({
		changeMonth: true,
	    changeYear : true,
		maxDate    : sysProcDate,
		dateFormat : 'dd/mm/y'
	});	
});
 
 
$(document).ready(function() {
		$("#fetch").click(function(){
			 $("#myForm").attr("action","fetchIRMData");
			 $("#myForm").submit();
		});
		$("#validate").click(function(){
			 $("#myForm").attr("action","validateIRMExData");
			 $("#myForm").submit();
		});
		$("#submit").click(function(){			
			$("#myForm").attr("action","storeIRMExData");
			$("#myForm").submit();
		});
		$("#reset").click(function() {
			window.location.href = "irmExtension";
		});

});
 
function validateSpecialChar(e) {
    e = e || event;
    return /[-a-z0-9]/i.test(String.fromCharCode(e.charCode || e.keyCode)) || !e.charCode && e.keyCode  < 48;
}