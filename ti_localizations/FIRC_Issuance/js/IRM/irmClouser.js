/**
* 
*/
 
function isNumber(evt) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 45 || charCode > 57)) {
        return false;
    }
    return true;
}
 
$(document).ready(function () {		
	var sysDate =  $("#sysProcDate").val().split("-");
	var sysProcDate = new Date(sysDate[2], sysDate[1] - 1, sysDate[0]);

	$("#adjDate").datepicker({
		changeMonth: true,
	    changeYear: true,
		maxDate : sysProcDate,
		dateFormat : 'dd-mm-y'
	});	
	$("#docDate").datepicker({
		changeMonth: true,
	    changeYear: true,
		dateFormat : 'dd-mm-y'
	});	
});
 
 
$(document).ready(function() {
		$("#fetch").click(function(){
			 $("#myForm").attr("action","fetchIRMClosureData");
			 $("#myForm").submit();
		});
		$("#validate").click(function(){
			 $("#myForm").attr("action","validateIRMClosureData");
			 $("#myForm").submit();
		});
		$("#submit").click(function(){			
			$("#myForm").attr("action","storeIRMClosureData");
			$("#myForm").submit();
		});
		$("#reset").click(function() {
			window.location.href = "irmClosure";
		});

});
 
 
function validateSpecialChar(e) {
    e = e || event;
    return /[-a-z0-9]/i.test(String.fromCharCode(e.charCode || e.keyCode)) || !e.charCode && e.keyCode  < 48;
}