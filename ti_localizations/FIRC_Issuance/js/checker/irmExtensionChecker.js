/**
* 
*/
$(document).ready(function() {
		$("#search").click(function(){
			 $("#form1").attr("action","extensionSearch");
			 $("#form1").submit();
		}); 		 
		$("#reset").click(function(){
			 $("#form1").attr("action","irmExtensionChecker");
			 $("#form1").submit();
		}); 
		$("#approve").click(function() {
			$("#check").val("approve");
			var remark = $.trim($('#remarks').val());
			if(remark.length == 0){
				alert("Remarks must be filled out");
				return false;
			}else{
				$('#form1').attr('action','updateExtensionChecker');
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
				$('#form1').attr('action','updateExtensionChecker');
				$('#form1').submit();
			}
		});		
});