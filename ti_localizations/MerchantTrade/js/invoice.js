$(document).ready(function() {
           var check = $("#ex_range").val();
           if(check == 0 || check > 1){
        	   $('#ex_range').attr('readonly', false);
           }
});
 
$(function(){
	$('.datepicker').datepicker({format: 'dd-mm-yyyy'} );
});
$(document).ready(function() {
	$("#submit").click(function(){
		$("#myForm").attr("action","changeInvoiceDetails?mode=store");
		$("#myForm").submit();
	});
	$("#validate").click(function(){
		$("#myForm").attr("action","changeInvoiceDetails?mode=validate");
		 $("#myForm").submit();
	});
	$("#cancel").click(function(){
		$("#myForm").attr("action","changeInvoiceDetails?mode=cancel");
		 $("#myForm").submit();
	});
});
 
 
$(document).ready(function() {
	 $('#ex_range').change(function (){
		 var exchange_val =  $("#ex_range").val();		 
		 var fob_amt =  $("#temp_fob").val();
		 var freight_amt = $("#temp_freight").val();
		 var ins_amt = $("#temp_ins").val();
		 var comm_amt = $("#temp_comm").val();		
		 var ded_amt =  $("#temp_ded").val();
		 var dis_amt = $("#temp_dis").val();
		 var pak_amt = $("#temp_pak").val();		 
		 var trans_amt = $("#temp_transAmt").val();
		 var billAmt = $("#billAmt").val();				 
		 var convert_Value = Number(exchange_val) * Number(trans_amt);
		 var realized_fobamt = Number(convert_Value) / Number(billAmt) * Number(fob_amt);
		 var realized_friamt = Number(convert_Value) / Number(billAmt) * Number(freight_amt);
		 var realized_insamt = Number(convert_Value) / Number(billAmt) * Number(ins_amt);
		 var realized_commamt = Number(convert_Value) / Number(billAmt) * Number(comm_amt);
		 var realized_dedamt = Number(convert_Value) / Number(billAmt) * Number(ded_amt);
		 var realized_disamt = Number(convert_Value) / Number(billAmt) * Number(dis_amt);
		 var realized_pakamt = Number(convert_Value) / Number(billAmt) * Number(pak_amt);

		 $("#real_FobAmt").val(realized_fobamt);		
		 $("#real_FreightAmt").val(realized_friamt);
		 $("#real_InsAmt").val(realized_insamt);
		 $("#real_CommAmt").val(realized_commamt);
		 $("#real_DedAmt").val(realized_dedamt);
		 $("#real_disAmt").val(realized_disamt);
		 $("#real_PakAmt").val(realized_pakamt);
	 });
});