function rejectAll() {
	$('#myForm').attr('action', 'OBBrejectData');
	$('#myForm').submit();
}
 
function uploadAll(){
 
	$('#myForm').attr('action', 'OBBuploadData');
	$('#myForm').submit();
}
 
function OBBexexcel(){
	$("#myForm").attr("action", "OBBListExcel");
	$("#myForm").submit();
}
 