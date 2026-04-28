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
	$("#myForm").attr("action", "checkerDownloadExcel");
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
	if (odcUpDate == '') {
		alert('Please select the Received Date');
		return false;
	}else{
		$('#programmeLister').attr('action', 'search');
		$('#programmeLister').submit();
	}
}
 
 
function UploadWarnings() {
	$("#myForm").attr("action", "checkeruploadwitwarnings");
	$("#statusFlag").val("uploadwar");
	$("#myForm").submit();
}
 
 
function selectlist(event) { 
var value = event;
$("#batchId").val(value);
$(".highlight").removeClass('highlight');
$(".highlight1").removeClass('highlighted');
		$("#"+event).addClass('highlight'); 


	/*	 $('#checker_list_table').dataTable({			 
	            "bDestroy": true
	        }).fnDestroy();
		 */
	    // Array holding selected row IDs
	    var rows_selected = [];
	    $("#checker_list_table").dataTable( {
	        "bProcessing": false,
	        "bServerSide": false,
	        "searching": false,
	        "sort": "position",
	        "sAjaxSource": "fetch",

	        "aoColumns": [
	             { "mData": "" },
	            { "mData": "documentRelease" },
	            { "mData": "productType" },
	            { "mData": "receviedFromRef" },
	            { "mData": "receviedOn" },

	        ],
	        'columnDefs': [{
	            'targets': 0,
	            'searchable': false,
	            'orderable': false,
	            'className': 'dt-body-center',
	            'render': function (data, type, full, meta){
	                return '<input type="checkbox" name="id[]" value="' + $('<div/>').text(data).html() + '">';
	            }
	        }],

	    	'order': [[1, 'asc']],
	    	'rowCallback': function(row, data, dataIndex){
	         // Get row ID

	         var rowId = data[0];


	         // If row ID is in the list of selected row IDs
	         if($.inArray(rowId, rows_selected) !== -1){
	            $(row).find('input[type="checkbox"]').prop('checked', true);
	            $(row).addClass('selected');

 
	           

	         }
	      }

	    } );
	    $('#select_all').click(function(event) { //on click
    		if (this.checked) { // check select status
    			alert("selected");
    			$(':checkbox').each(function() { //loop through each checkbox
    				this.checked = true; //select all checkboxes with class "checkbox1"              
    			});
    		} else {
    			$(':checkbox').each(function() { //loop through each checkbox
    				this.checked = false; //deselect all checkboxes with class "checkbox1"                      
    			});
    		}
    	}); 

 
}
 