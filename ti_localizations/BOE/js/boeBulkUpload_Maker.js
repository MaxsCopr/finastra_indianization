function rejectAll() {
	$('#myForm').attr('action', 'boeRejectData');
	$('#myForm').submit();
}
 
function uploadAll(){
	$('#myForm').attr('action', 'boeUploadData');
	$('#myForm').submit();
}
 
function exexcel(){
	$("#myForm").attr("action", "makerExcel");
	$("#myForm").submit();
}
 
function bulkerrorlistexcel(){
	$("#myForm").attr("action", "errorlistExcel");
	$("#myForm").submit();
}