/**
* 
*/
 
function amtCalculation1() {

	var exRate1 = isValueCheck(document.getElementById("crossExRate1").value);	
	var insAmt 	= isValueCheck(document.getElementById("crossInsAmt").value);
	var eqInsAmt = parseFloat(exRate1) * parseFloat(insAmt);
	$('#crossEqInsAmt').val(eqInsAmt.toFixed(2));
	var fobCu			 = isValueCheck(document.getElementById("crossFobAmt").value);
	var equivalentInsAmt = isValueCheck(document.getElementById("crossEqInsAmt").value);
	var equivalentFreAmt = isValueCheck(document.getElementById("crossEqFrAmt").value);
	var latestInvoiceTotal = parseFloat(fobCu)+parseFloat(equivalentInsAmt)+parseFloat(equivalentFreAmt);
	$('#crossTotInvAmt').val(latestInvoiceTotal.toFixed(2));

}
 
function amtCalculation2() {
	var exRate2 = isValueCheck(document.getElementById("crossExRate2").value);	
	var frAmt 	= isValueCheck(document.getElementById("crossFrAmt").value);
	var eqFrAmt = parseFloat(exRate2) * parseFloat(frAmt);
	$('#crossEqFrAmt').val(eqFrAmt.toFixed(2));
	var fobCu			 = isValueCheck(document.getElementById("crossFobAmt").value);
	var equivalentInsAmt = isValueCheck(document.getElementById("crossEqInsAmt").value);
	var equivalentFreAmt = isValueCheck(document.getElementById("crossEqFrAmt").value);
	var latestInvoiceTotal = parseFloat(fobCu)+parseFloat(equivalentInsAmt)+parseFloat(equivalentFreAmt);
	$('#crossTotInvAmt').val(latestInvoiceTotal.toFixed(2));
}
 
 
function crossCurrencyConversion() {		
	$("#formId").attr("action", "updateCrossCurrencyManyBill");
	$("#formId").submit();
}
 
function closeCrossCurrencyButton(){	
	$("#formId").attr("action", "backCrossCurrencyManyBill");
	$("#formId").submit();	
}
 
 
function isValueCheck(value) {
	if (trim(value) == "" || value == null){
			return "0";
	}else{
			return value;
	}
}
 
 
function isNumber(evt) {
	evt = (evt) ? evt : window.event;
	var charCode = (evt.which) ? evt.which : evt.keyCode;
	if (charCode > 31 && (charCode < 45 || charCode > 57)) {
		return false;
	}
	return true;
}