/**
* 
*/
 
$(document).ready(function() {
	$("#search").click(function(){
		$("#form1").attr("action","closureSearch");
		$("#form1").submit();
	}); 		 
	$("#reset").click(function(){
		$("#form1").attr("action","irmClosureChecker");
		$("#form1").submit();
	}); 
	$("#approve").click(function() {
		$("#check").val("approve");
		var remark = $.trim($('#remarks').val());
		if(remark.length == 0){
			alert("Remarks must be filled out");
			return false;
		}else{
			$('#form1').attr('action','updateClosureChecker');
			$('#form1').submit();
		}		
	});
	$("#reject").click(function() {
		$("#check").val("reject");
		var remark = $.trim($('#remarks').val());
		if(remark.length == 0){
			alert("Remarks must be filled out");
			return false;
		}else{
			$('#form1').attr('action','updateClosureChecker');
			$('#form1').submit();
		}		
	});		
});