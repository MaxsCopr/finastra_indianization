$(function(){	   
    $("#dialogBOE").dialog({                    
    	autoOpen: false
    }); 
});
 
 
function getPayRefNo() {
	$('#formId').attr('action', 'fetchPayRefNo');
	$('#formId').submit();
}
 
function fetchDataFromView() {
	var payRefNo 	 = $('#payRefId').val();
    var eventRefNo = $('#eventRefNo').val();
    if(payRefNo == ''){
    	$("#dialogBOE").html('Please Enter Payment Reference Number');
    	$("#dialogBOE").dialog('open') 
	    $("#dialogBOE").dialog({
		    autoOpen: false,
		    modal: true,
		    title: "ERROR MESSAGE",
		     buttons: {				    	
		        Close: function () {
		            $(this).dialog('close');
		        }
		    } 
	    });
	}else if(eventRefNo == ''){
    	$("#dialogBOE").html('Please Select Event Reference Number');
	    $("#dialogBOE").dialog('open') 
	    $("#dialogBOE").dialog({
		    autoOpen: false,
		    modal: true,
		    title: "ERROR MESSAGE",
		    buttons: {				    	
		        Close: function () {
		            $(this).dialog('close');
		        } 
		   }
	    });
	}else{
	$('#formId').attr('action', 'paymentDataFromView');
	$('#formId').submit();
	}
}
 
function getDataFromView() {
	var payRefNo 	 = $('#payRefId').val();
    var eventRefNo = $('#eventRefNo').val();
    if(payRefNo == ''){
    	$("#dialogBOE").html('Please Enter Payment Reference Number');
    	$("#dialogBOE").dialog('open') 
	    $("#dialogBOE").dialog({
		    autoOpen: false,
		    modal: true,
		    title: "ERROR MESSAGE",
		     buttons: {				    	
		        Close: function () {
		            $(this).dialog('close');
		        }
		    } 
	    });
	}else if(eventRefNo == ''){
    	$("#dialogBOE").html('Please Select Event Reference Number');
	    $("#dialogBOE").dialog('open') 
	    $("#dialogBOE").dialog({
		    autoOpen: false,
		    modal: true,
		    title: "ERROR MESSAGE",
		    buttons: {				    	
		        Close: function () {
		            $(this).dialog('close');
		        } 
		   }
	    });
	}else{
		$('#formId').attr('action', 'paymentReferenceToManyBillNo');
		$('#formId').submit();
	}
}