$(document).ready(function() {
 
	$("#boeSearch").click(function() {	
		$('#myForm').attr('action','fetchCheckerManualBOE');
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
 
function confirm(value) {
	$("#statusID").val(value);
	var remark = $.trim($('#remarks').val());
	if(remark.length == 0){
		alert("Remarks must be filled out");
		return false;
	}else{
		$('#myForm').attr('action','updateManualBOE');
		$('#myForm').submit();
	}		
}
 
 
function boeDetails(event){
	 $(".boeList").removeClass("highlighted");
	 $(event).addClass("highlighted");
	 var value1 = $(event).find("td").eq(0).text().trim();
	 var value2 = $(event).find("td").eq(1).text().trim();
	 var value3 = $(event).find("td").eq(2).text().trim();
	 var boeValue = value1 + ':' + value2 + ':' + value3 ;
	 $.ajaxSetup({
			 async: false
	 });	 
     $('#boeData').val(boeValue);
     if(boeValue != ''){
    	 $('#myForm').attr('action', 'manualBoeDataView');
    	 $('#myForm').submit();
     }
}