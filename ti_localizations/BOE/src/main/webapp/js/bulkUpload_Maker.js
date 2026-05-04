function rejectAll() {
	$('#myForm').attr('action', 'rejectData');
	$('#myForm').submit();
}
 
function uploadAll(){
	$('#myForm').attr('action', 'uploadData');
	$('#myForm').submit();
}
function exexcel(){
	$("#myForm").attr("action", "manualMakerExcel");
	$("#myForm").submit();
}
 
function manualerrorlistexcel(){
	$("#myForm").attr("action", "manualerrorlistExcel");
	$("#myForm").submit();
}