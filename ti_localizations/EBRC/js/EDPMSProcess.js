$(document).ready(function() {
    $("#div1").delay(8000).hide(100, function() {
    });
});
 
function eventType() {	
	var id = document.getElementById("billRefNo").value;
	$.ajaxSetup({
		 async: false
		 });
	 $.getJSON("getEventReferenceJson"+"?billRefNo="+id,function(details) {
		 updateComboBox(details.eventList, 'idEventRefNo');    		
	      });	
}
 
function getTIPlusDetails(){
	var id = document.getElementById("billRefNo").value;
	var eventId = document.getElementById("idEventRefNo").value;
	var comId = id + "to" +eventId ;
	 $("#tiPlusList").find("tr:gt(0)").remove();
	 $.ajaxSetup({
		 async: false
		 });
	 $.getJSON("getTIPlusJson"+"?combineId="+comId,function(data) {  
						 var listvalue="";	
						 var list = "";
					   	 var buyerName = "";
						 var buyerCountry = "";
			 	           $.each(data.tiPlusList, function(key,etype) {
							  buyerName = etype.buyerName;
			 	        	  buyerCountry = etype.buyerCountry;
			 	        	  var shipDetails = "'"+ etype.shippingBillNo + "to" + etype.shippingBillDate + "to" + etype.portCode + "'";
			 	        	  listvalue  = '<tr><td style="padding:4px 5px;"><a href="#" style="text-align: center;" onclick="shippingDetails('+shipDetails+')">SELECT</a></td><td style="padding:4px 5px;">'  + etype.shippingBillNo + '</td> '+'<td style="padding:4px 5px;">'  + etype.shippingBillDate + '</td>'+
			 	        	                   '<td style="padding:4px 5px;">'  + etype.portCode + '</td> '+'<td style="padding:4px 5px;">'  + etype.formNo + '</td>'+
											    '<td style="padding:4px 5px;">'  + etype.shippingBillAmt + '</td> '+'<td style="padding:4px 5px;">'  + etype.shippingBillCurr + '</td>'+
			 	        	                '</tr>';
			 	        	  list=list+listvalue;			
			 	            });		
			 	         $('#tiPlusList tbody').append(list);	
			 	        if(list != ""){
				        	  $('#noOfShippingBills').val(( $('#tiPlusList tbody tr').size()-1));
				         }else{
				        	 $('#noOfShippingBills').val(0);
				         }
			 	         $('#billCurr').val(data.detailsVO.billCurr);
			 	   		 $('#billAmt').val(data.detailsVO.billAmt);
			 	   		 $('#custCIFNo').val(data.detailsVO.custCIFNo);
			 	   	 	 $('#ieCode').val(data.detailsVO.ieCode);
			 	   		 $('#custName').val(data.detailsVO.custName);
			 	   		 $('#adCode').val(data.detailsVO.adCodec);
			 	    	 $('#buyerName').val(buyerName);
			 	   	 	 $('#buyerCountry').val(buyerCountry);    
	 }); 
}
 
function shippingDetails(shipDetails){
	 $.getJSON("getShippingDetailsJson"+"?shipDetail="+shipDetails,function(data) {		 
		 var listvalue="";	
		 var list = "";
		 	$("#invoiceList").find("tr:gt(0)").remove();
		 		$.ajaxSetup({
				 async: false
				 });
	           $.each(data.invoiceDetailsList, function(key,etype) {	        	   
	        	listvalue  = '<tr><td style="padding:4px 5px;">'  + etype.invoiceSerialNo + '</td> '+'<td style="padding:4px 5px;">'  + etype.invoiceNo + '</td>'+
	        	                   '<td style="padding:4px 5px;">'  + etype.invoiceDate + '</td> '+'<td style="padding:4px 5px;">'  + etype.invoiceTotal + '</td> '+'<td style="padding:4px 5px;">'  + etype.fobCurr + '</td>'+
	        	                   '<td style="padding:4px 5px;" align="right">'  + etype.fobAmt + '</td> '+'<td style="padding:4px 5px;">'  + etype.freightCurr + '</td>'+
	        	                   '<td style="padding:4px 5px;" align="right">'  + etype.freightAmt + '</td> '+'<td style="padding:4px 5px;">'  + etype.insuranceCurr + '</td>'+
	        	                   '<td style="padding:4px 5px;" align="right">'  + etype.insuranceAmt + '</td> '+'<td style="padding:4px 5px;">'  + etype.commissionCurr + '</td>'+
	        	                   '<td style="padding:4px 5px;" align="right">'  + etype.commissionAmt + '</td> '+'<td style="padding:4px 5px;">'  + etype.discountCurr + '</td>'+
	        	                   '<td style="padding:4px 5px;" align="right">'  + etype.discountAmt + '</td> '+'<td style="padding:4px 5px;">'  + etype.deductionCurr + '</td>'+
	        	                   '<td style="padding:4px 5px;" align="right">'  + etype.deductionAmt + '</td> '+'<td style="padding:4px 5px;">'  + etype.packagingCurr + '</td>'+
	        	                   '<td style="padding:4px 5px;" align="right">'  + etype.packagingAmt + '</td> '+
	        	                '</tr>';
	        	  list=list+listvalue;	
	            });		
	         $('#invoiceList tbody').append(list);
	         if(list != ""){
	        	  $('#noInvoices').val(( $('#invoiceList tbody tr').size()-1));
	         }else{
	        	 $('#noInvoices').val(0);
	         }
	         $('#storeType').val(data.detailsVO.storeType);
		 	 $('#shippingBillNo').val(data.detailsVO.shippingBillNo);
	   		 $('#portCode').val(data.detailsVO.portCode);
	   		 $('#exportAgency').val(data.detailsVO.exportAgency);
	   	 	 $('#ie_Code').val(data.detailsVO.ieCode);
	   		 $('#countryOfDest').val(data.detailsVO.countryOfDest);
	   		 $('#leoDate').val(data.detailsVO.leoDate);
	    	 $('#recInd').val(data.detailsVO.recInd);
	    	 $('#shippingBillDate').val(data.detailsVO.shippingBillDate);
	   	 	 $('#formNo').val(data.detailsVO.formNo);  
	   	   	 $('#custSerialNo').val(data.detailsVO.custCIFNo);
	   		 $('#exportType').val(data.detailsVO.exportType);
	    	 $('#ad_Code').val(data.detailsVO.adCode);
	    	/*  $('#dateOfNeg').val(data.detailsVO.custName);
	    	 $('#adBillNo').val(data.detailsVO.custName);
	   	 	 $('#directDispInd').val(data.detailsVO.buyerCountry);  */
	 });
 
}
 
$("#submit").click(function(){
	  $("#myForm").submit();
});
 
function cancel(){
	 window.location.href = "grProcess";	
}
 
 
function validateForm(){
 
	var id = document.getElementById("billRefNo").value;
	var eventId = document.getElementById("idEventRefNo").value;
	var billAmt = document.getElementById("billAmt").value;
	var comId = id + "to" +eventId + "to" + billAmt ;
	var listvalue="";	 
	var list = "";		
	 $.ajaxSetup({
		async: false
		});
	 $.getJSON("validateTIPlusDetails"+"?combineId="+comId,function(data) {	
		 if(data.errorList != null){
		    $("#errorList").find("tr:gt(0)").remove();
		    $.each(data.errorList, function(key,etype) {	 
		    listvalue  = '<tr><td style="padding:4px 5px;">'+ etype.errorId +'</td>'+'<td style="padding:4px 5px;">'  + etype.errorDesc + '</td> '+'<td style="padding:4px 5px;">'  + etype.errorCode + '</td>'+
          				 '<td style="padding:4px 5px;">'  + etype.errorDetails + '</td> '+'<td style="padding:4px 5px;">' +'<a id="errorMsg" onclick="changeText()">' + etype.errorMsg + '</a>' + '</td>'+
   				 		'</tr>';
		     list=list+listvalue;				 
		   });		
	       $('#errorList tbody').append(list);	
		 }
		 if(list == ""){
			 listvalue  = '<tr><td colspan="5"> No Error Found </td></tr>';
	 		 list=list+listvalue;	
			$('#errorList tbody').append(list);
		 }
});
}
function changeText()
{
	var value = document.getElementById('errorMsg').innerHTML;
	if(value == 'N'){
		document.getElementById('errorMsg').innerHTML = 'Y';
	}else{
		document.getElementById('errorMsg').innerHTML = 'N';
	}
}
 
function submitForm(){
	var id = document.getElementById("billRefNo").value;
	var eventId = document.getElementById("idEventRefNo").value;
	var billAmt = document.getElementById("billAmt").value;
	var comId = id + "to" +eventId + "to" + billAmt ;
	var listvalue="";	 
	var list = "";
	var cval = "";
	 $.ajaxSetup({
			async: false
	 });
	 var checkVal = document.getElementById('errorMsg');
	 if(checkVal != null){
		 cval = document.getElementById('errorMsg').innerHTML;
	 }else{
		 cval = '';
	 }
	  if(cval == 'N' || cval == ''){
		 $.getJSON("validateTIPlusDetails"+"?combineId="+comId,function(data) {		 
		    if(data.errorList != null){
		      $("#errorList").find("tr:gt(0)").remove();
		       $.each(data.errorList, function(key,etype) {	 		 
		    	 listvalue  = '<tr><td style="padding:4px 5px;">'+ etype.errorId +'</td>'+'<td style="padding:4px 5px;">'  + etype.errorDesc + '</td> '+'<td style="padding:4px 5px;">'  + etype.errorCode + '</td>'+
  				 		'<td style="padding:4px 5px;">'  + etype.errorDetails + '</td> '+'<td style="padding:4px 5px;">' +'<a id="errorMsg" onclick="changeText()">' + etype.errorMsg + '</a>' + '</td>'+
  				 		'</tr>';
		     list=list+listvalue;				 
		   });		
	       $('#errorList tbody').append(list);	
		 }
		 if(list == ""){
			 $("#myForm").submit();
		 }
		 });
	  }else if(cval == 'Y'){
		 $("#myForm").submit();
	}
}