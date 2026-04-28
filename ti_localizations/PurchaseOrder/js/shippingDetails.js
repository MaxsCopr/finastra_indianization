$(document).ready(function() {
           var check = $("#ex_range").val();
           if(check == 0 || check > 1 ){
        	   $('#ex_range').attr('readonly', false);
           }
});
 
$(document).ready(function() {
	 $('#ex_range').change(function (){
		 var exchange_val =  $("#ex_range").val();
		 var trans_amt = $("#transAmt").val();
		 var ship_amt = $("#shipBillAmt").val();
		 var billAmt = $("#billAmt").val();		
		 var convert_Value = Number(exchange_val) * Number(trans_amt);
		 var realized_amt = Number(convert_Value) / Number(billAmt) * Number(ship_amt);
		 $("#realAmt").val(realized_amt);		 
	 });
});

$(function(){
		$('.datepicker').datepicker({format: 'dd-mm-yyyy'} );
	});
 
 
	 
$(document).ready(function() {
		$("#submit").click(function(){
			$("#myForm").attr("action","updateShippingDetails?mode=store");
			$("#myForm").submit();
		});
		$("#validate").click(function(){
			$("#myForm").attr("action","updateShippingDetails?mode=validate");
			 $("#myForm").submit();
		});
		$("#cancel").click(function(){
			$("#myForm").attr("action","updateShippingDetails?mode=cancel");
			$("#myForm").submit();
		});
	});