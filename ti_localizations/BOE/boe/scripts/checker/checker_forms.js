$(document).ready(function() {
 
	$("#boeSearch").click(function() {	
		$('#myForm').attr('action','checker_multi_paymentReferences');
		$('#myForm').submit();
	});
	$("#ok").click(function() {
		$("#check").val("approve");
		var remark = $.trim($('#remarks').val());
		if(remark.length == 0){
			alert("Remarks must be filled out");
			return false;
		}
		$('#myForm').attr('action','updateStatusAction');
		$('#myForm').submit();
 
	});
	$("#reject").click(function() {
		$("#check").val("reject");
		var remark = $.trim($('#remarks').val());
		if(remark.length == 0){
			alert("Remarks must be filled out");
			return false;
		}		
		$('#myForm').attr('action','updateStatusAction');
		$('#myForm').submit();
 
	});
 
});
 
$(document).ready(function() {		
	$("#boeDateFrom").datepicker({
		 changeMonth: true,
	     changeYear: true,
		 dateFormat : 'dd/mm/yy'
	});	
	$("#boeDateTo").datepicker({
		 changeMonth: true,
	     changeYear: true,
		 dateFormat : 'dd/mm/yy'
	});	
});
 
 
function boeDetails(event){
	 $(".boeList").removeClass("highlighted");
	 $(event).addClass("highlighted");
	 var value1 = $(event).find("td").eq(0).text().trim();
	 var value2 = $(event).find("td").eq(1).text().trim();
	 var value3 = $(event).find("td").eq(2).text().trim();
	 var value4 = $(event).find("td").eq(3).text().trim();
	 var value5 = $(event).find("td").eq(4).text().trim();
	 var boeValue = value1 + ':' + value2 + ':' + value3 + ':' + value4 + ':' + value5;
	 $.ajaxSetup({
			 async: false
	 });	 
     $('#boeData').val(boeValue);
     if(boeValue != ''){
    	 $('#myForm').attr('action', 'boeDataView');
    	 $('#myForm').submit();
     }
}