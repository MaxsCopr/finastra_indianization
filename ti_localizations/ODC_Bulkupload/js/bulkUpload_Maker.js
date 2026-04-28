/**
* 
*/
function doLoad() {
	   $('body').modal("hide");
	   $('body').css('display','block');
	   $('body').removeClass('removePageLoad');
	   $('body').addClass('removePageLoad');
}
 
$(document).ready(function(){
	$('.datepicker .datepicker-inline').css('display','none');
	$('input[type="submit"],input[type="button"]').click(function(){
		$('body').modal({
			   show: 'false'
			 });
		$('body').removeClass('removePageLoad');
		$('body').addClass('addPageLoad');
	});
});
 
if ( window.addEventListener ) { 
	  $('body').modal({
		   show: 'false'
		 });
	 $('body').addClass('addPageLoad');
	 window.addEventListener( "load", doLoad, false );
}
 
function selectlist1(event) {
	$(".eventrefresh").removeClass("highlighted");
	$(event).addClass('highlighted');
}
 
function buyerBranchSelection() {
	$("#bulkUpload").attr("action", "buyerBranchSelection");
	$("#bulkUpload").submit();
}
 
function buyerbranchrefreshList() {
	$("#buyerCust").attr("action", "buyerBranchSelection");
	$("#buyerCust").submit();
}
 
function excelValidation() {
	$("#bulkUpload").attr("action","listofinvoice");
	$("#bulkUpload").submit();
}
 
function buyerBranchList(event) {	
	var id = $('.highlighted').attr('id');
	if (id == undefined) {
		$('#selectrow').css('display', 'block');
	}
	var test = id.split('_');
	var val = test[1];
	var test1 = $("#name_" + val).attr('id');
	var value_key1 = $("#" + test1).val();
	$('#fname').val(value_key1);
	$("#buyerCust").attr("action", "buyerBranch");
	$("#buyerCust").submit();
}
 
function branchListCancel(){
	$("#buyerCust").attr("action", "buyerCustListCancel");
	$("#buyerCust").submit();
}
 
	function exexcel(){
		$("#myForm").attr("action", "downloadExcel");
		$("#myForm").submit();
	}
	function exexcel1(){
		alert("coming");
		$("#myForm").attr("action", "downloadExcel1");
		$("#myForm").submit();
	}
	function uploadAll(){
		console.log("Inside the expected method");
		$("#myForm").attr("action","makeruploadall");
		$("#statusFlag").val("uploadall");
		$('body').modal();
		$("#myForm").submit();
	}
 
	function rejectAll(){
		$("#myForm").attr("action","makerrejectall");
		$("#statusFlag").val("rejectall");
		$("#myForm").submit();
	}	
 