function eventType() {	
	var id = document.getElementById("billRefNo").value;
	 $.getJSON("getEventReferenceJson"+"?billRefNo="+id,function(details) {
		 updateComboBox(details.eventList, 'idEventRefNo');    		
	      });	
}
 
 
$(function(){
	$('.datepicker').datepicker({format: 'dd/mm/yyyy'} );
});
 
 
 
function reload(){
	window.location.href = "adTransfer";
}
 
	 
$(document).ready(function() {
		 $("#validate").click(function(){
			 $("#myForm").attr("action","fetchAdTransfer?mode=validate");
			 $("#myForm").submit();
		});
		 $("#submit").click(function(){
			 $("#myForm").attr("action","fetchAdTransfer?mode=store");
			 $("#myForm").submit();
		});
		 $("#fetch").click(function(){
			 $("#myForm").attr("action","fetchAdTransfer");
			  $("#myForm").submit();
		});
});

$(document).ready(function() {
	    $("#div1").delay(8000).hide(100, function() {
	    });
	});