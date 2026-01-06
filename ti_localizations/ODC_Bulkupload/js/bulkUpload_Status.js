/**
* 
*/
function doLoad() {
	   $('body').modal("hide");
	   $('body').css('display','block');
	   $('body').removeClass('removePageLoad');
	   $('body').addClass('removePageLoad');
}
 
$(document).ready(function() {
		$('.datepicker .datepicker-inline').css('display', 'none');
		$('input[type="submit"],input[type="button"]').click(function() {
			$('body').modal({
				show : 'false'
			});
			$('body').removeClass('removePageLoad');
			$('body').addClass('addPageLoad');
 
		});
});
 
 
if (window.addEventListener) {
	$('body').modal({
		show : 'false'
	});
	$('body').addClass('addPageLoad');
	window.addEventListener("load", doLoad, false);
}
 
 
function exexcel() {
	$("#myForm").attr("action", "statusDownloadExcel");
	$("#myForm").submit();
}
 
$(".Y").addClass('highlighted');
$("#datReceived").attr('readonly', true);	
$("#stpprod").attr('readonly', true);
 
function UploadAll() {
	$("#myForm").attr("action", "checkeruploadall");
	$("#statusFlag").val("uploadall");
	$("#myForm").submit();
}
 
function validate() {
	alert("validate");
	$("#myForm").attr("action", "checkerval");
	$("#statusFlag").val("validat");
	$("#batchId_val").val();
	$("#myForm").submit();
}
 
function RejectAll() {
	$("#myForm").attr("action", "checkerrejectall");
	$("#statusFlag").val("rejectall");
	$("#myForm").submit();
}
 
function getDataFromView1() {
	var odcUpDate = $("#odcUpDate").val();
	alert(odcUpDate);
	if (dcUpDate == '') {
		$('#formId').attr('action', 'viewerSearch');
		$('#formId').submit();
	} else {
		$('#formId').attr('action', '');
		$('#formId').submit();
	}
}
 
 
function UploadWarnings() {
	$("#myForm").attr("action", "checkeruploadwitwarnings");
	$("#statusFlag").val("uploadwar");
	$("#myForm").submit();
}
 
 
function selectlist(event) { 
	alert(batchId);
var value = event;
$("#batchId").val(value);
$(".highlight").removeClass('highlight');
$(".highlight1").removeClass('highlighted');
alert("next");
	$("#"+event).addClass('highlight'); 
        $("#invoiceDetailsList").find("tr:gt(0)").remove();      
    	$('body').modal({
    		show : 'false'
    	});
    	$('body').removeClass('removePageLoad');
 
    	$('body').addClass('addPageLoad');
    	$("#programmeLister").attr("action", "viewerFetch");
    	$("#programmeLister").submit();
}