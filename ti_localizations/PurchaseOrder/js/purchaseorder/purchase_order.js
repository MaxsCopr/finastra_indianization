function margin_cal(event)
{
	var po_amt=document.getElementById(elementId).value;
	console.log('po_amt----'+po_amt)
	var margin_value=document.getElementById(elementId).value;
	console.log('margin_value--'+margin_value)
	var eligible_amt=parseDouble(po_amt)*parseDouble(margin_value);
	console.log('eligible_amt----'+eligible_amt)
	$('#margin').val(eligible_amt)
}
 
 
 
$(document).ready(function() {
	$("#exportexpiryDate").datepicker({
		dateFormat : 'dd/mm/yy'
	});
	$("#lastShipmentDate").datepicker({
		dateFormat : 'dd/mm/yy'
	});
	$("#datepicker2").datepicker({
		dateFormat : 'dd/mm/yy'
	});
 
	$('input[type="submit"],a').click(function() {
		$('body').modal({
			show : 'false'
		});
		$('body').removeClass('removePageLoad');
		$('body').addClass('addPageLoad');
 
	});
 
});
function sync() {
	var a = document.getElementById('insurancededuction');
	var twogiga = document.getElementById('insurancededuction').value;
	var fourgiga = document.getElementById('freightdeduction').value;
 
	var sum = parseInt(twogiga) + parseInt(fourgiga);
	var sum1 = parseInt(sum);
 
	if (parseInt(sum) >= 100) {
		alert("Addition of InsuranceDeduction and FreightDeduction should not be more or equal to 100");
		document.getElementById('insurancededuction').value = "";
		document.getElementById('freightdeduction').value = "";
	}
 
	else if (a.value >= 100) {
 
		alert("Percentage Doesnot Exceed or Equal to 100");
		document.getElementById('insurancededuction').value = "";
	} else {
		$("#myForm").attr("action", "calculate");
		$("#myForm").submit();
	}
}
function validate() {
	var b = document.getElementById('freightdeduction');
 
	if (b.value >= 100) {
		alert("Percentage Doesnot Exceed  or Equal to 100");
		document.getElementById('freightdeduction').value = "";
	}
	sync();
}
if (window.addEventListener) {
	$('body').modal({
		show : 'false'
	});
	$('body').addClass('addPageLoad');
	window.addEventListener("load", doLoad, false);
}
 
function reset() {
	$("#myForm").attr("action", "reset");
	$("#myForm").submit();
}
 
function getValue() {
	var v = document.getElementById('povalue');
 
	$("#eligibleamount").val(v.value);
	document.getElementById("insurancededuction").value = 0;
	document.getElementById("freightdeduction").value = 0;
	parseAlphaNumeric();
	fetchMargin();
 
}
 
function parseAlphaNumeric() {
	var vAlphaNum = document.getElementById("povalue").value; //"125sdf5jk67lk98";
 
	var vAlpha = vAlphaNum.split(/[a-zA-Z]+/);
	var vNum = vAlphaNum.split(/[.0-9]+/);
	$('#povalue').val(vAlpha.join("") + " " + vNum.join(""));
 
}
 
function fetchBenName() {
 
	$("#myForm").attr("action", "fetchBenName");
	$("#myForm").submit();
}
 
function fetchMargin() {
 
	$("#myForm").attr("action", "calculate");
	$("#myForm").submit();
}
 
$(document).ready(function() {
	document.getElementById("table").addEventListener("scroll", function() {
		var translate = "translate(0," + this.scrollTop + "px)";
		this.querySelector("thead").style.transform = translate;
	});
});
 
function copyPurchaseDetails() {
	if (confirm('Do you want to Copy this Data?')) {
		$("#myForm").attr("action", "copyPurchaseOrderDetails");
		$("#myForm").submit();
	} else {
	}
}