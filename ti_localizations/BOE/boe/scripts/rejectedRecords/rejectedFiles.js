/**
* 
*/
 
 
function deleteRec()
{
	 	$('#myForm').attr('action','delRejRecords');
		$('#myForm').submit();
}
$(document).ready(function() {	
 
	$("#boeRejSearch").click(function() {
		$('#myForm').attr('action', 'viewRejectedRecords');
		$('#myForm').submit();
	});
 
});
 
 
function boeDetails(event) {
	$(".boeList").removeClass("highlighted");
	$(event).addClass("highlighted");
	var value1 = $(event).find("td").eq(0).text().trim();
	var value2 = $(event).find("td").eq(1).text().trim();
	var value3 = $(event).find("td").eq(2).text().trim();
	var value4 = $(event).find("td").eq(3).text().trim();
	var value5 = $(event).find("td").eq(4).text().trim();
	var value6 = $(event).find("td").eq(8).text().trim();
	var boeValue = value1 + ':' + value2 + ':' + value3 + ':' + value4 + ':' + value5;
 
	$.ajaxSetup({
		async : false
	});
	$('#boeData').val(boeValue);
	if(value6 == 'M'){
		$('#myForm').attr('action', 'boeRejDataMultipleView');
		$('#myForm').submit();
	}else{
		$('#myForm').attr('action', 'boeRejDataSingleView');
		$('#myForm').submit();
	}	
 
}
 
 
function confirm(value) {
	$("#check").val(value);
	var remark = $.trim($('#chkList').length);
	if(remark.length == 0){
		alert("Please Select Records to Delete");
		return false;
	}else{
		$('#myForm').attr('action','delRejRecords');
		$('#myForm').submit();
	}		
}
 
/*function deleteBoeDetails(event) {
	$(".boeList").removeClass("highlighted");
	$(event).addClass("highlighted");
	var value1 = $(event).find("td").eq(0).text().trim();
	var value2 = $(event).find("td").eq(1).text().trim();
	var value3 = $(event).find("td").eq(2).text().trim();
	var value4 = $(event).find("td").eq(3).text().trim();
	var value5 = $(event).find("td").eq(4).text().trim();
	var boeValue = value1 + ':' + value2 + ':' + value3 + ':' + value4 + ':' + value5;
 
	$.ajaxSetup({
		async : false
	});
	$('#boeData').val(boeValue);

	$('#myForm').attr('action', 'deleteBoeDetails');
	$('#myForm').submit();
 
}*/