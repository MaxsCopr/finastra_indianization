$(document).ready(function() {
	 $("#Search").click(function(){
		 $("#myForm").attr("action","fircOurBankApproval");
		 $("#myForm").submit();
	}); 

	$("#approve").click(function() {
		$("#check").val("approve");
		var remark = $.trim($('#remarks').val());
		if(remark.length == 0){
			alert("Remarks must be filled out");
			return false;
		}else{
			$('#myForm').attr('action','checkerUpdateAction');
			$('#myForm').submit();
		}	
 
	});
	$("#reject").click(function() {
		$("#check").val("reject");
		var remark = $.trim($('#remarks').val());
		if(remark.length == 0){
			alert("Remarks must be filled out");
			return false;
		}else{
			$('#myForm').attr('action','checkerUpdateAction');
			$('#myForm').submit();
		}		
 
	});
});