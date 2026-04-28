$(document).ready(function() {
 
	$("#buttonClose").click(function() {
		var screen = document.getElementById('screenType').value;
 
		if (screen == 'MakerScreen') {
			$("#myForm").attr("action", "makerProcess");
			$("#myForm").submit();
		} else if (screen == 'CheckerScreen') {
			$("#myForm").attr("action", "checkerProcess");
			$("#myForm").submit();
		} else if (screen == 'EnquiryScreen') {
			$("#myForm").attr("action", "enquiryProcess");
			$("#myForm").submit();
		} else if (screen == 'MakerBookingScreen') {
			$("#myForm").attr("action", "bookingProcess");
			$("#myForm").submit();
		}else if (screen == 'MakerCancelScreen') { 
			$("#myForm").attr("action", "cancelProcess");
			$("#myForm").submit();
		}
 
	});
 
});
 
function selectCustomerID(event) {
	$(".customerList").removeClass("highlighted");
	$(event).addClass("highlighted");
	var value = $(event).find("td").eq(0).text().trim();
	var value1 = $(event).find("td").eq(1).text().trim();
 
	var screen = document.getElementById('screenType').value;
 
	$("#customerID").val(value);
	console.log("customer ID" + value);
	$("#customerName").val(value1);
	console.log("customer Name" + value1);
 
	if (screen == 'MakerScreen') {
		$("#myForm").attr("action", "makerProcess");
		$("#myForm").submit();
	} else if (screen == 'CheckerScreen') {
		$("#myForm").attr("action", "checkerProcess");
		$("#myForm").submit();
	} else if (screen == 'EnquiryScreen') {
		$("#myForm").attr("action", "enquiryProcess");
		$("#myForm").submit();
	} else if (screen == 'MakerBookingScreen') {
		$("#myForm").attr("action", "bookingProcess");
		$("#myForm").submit();
	}else if (screen == 'MakerCancelScreen') { 
		$("#myForm").attr("action", "cancelProcess");
		$("#myForm").submit();
	}
 
}
 
function selectAcctNumber(event) {
	$(".accountList").removeClass("highlighted");
	$(event).addClass("highlighted");
	var value = $(event).find("td").eq(0).text().trim();
	var value1 = $(event).find("td").eq(3).text().trim();
 
	var screen = document.getElementById('screenType').value;
 
	$("#acctNumber").val(value);
	console.log("acct Number" + value);
	$("#customerID").val(value1);
	console.log("customer ID" + value1);
	if (screen == 'MakerScreen') {
		$("#myForm").attr("action", "makerProcess");
		$("#myForm").submit();
	} else if (screen == 'CheckerScreen') {
		$("#myForm").attr("action", "checkerProcess");
		$("#myForm").submit();
	} else if (screen == 'EnquiryScreen') {
		$("#myForm").attr("action", "enquiryProcess");
		$("#myForm").submit();
	} else if (screen == 'MakerBookingScreen') {
		$("#myForm").attr("action", "bookingProcess");
		$("#myForm").submit();
	}else if (screen == 'MakerCancelScreen') { 
		$("#myForm").attr("action", "cancelProcess");
		$("#myForm").submit();
	}
}
 
function selectBranch(event) {
	$(".branchList").removeClass("highlighted");
	$(event).addClass("highlighted");
	var value = $(event).find("td").eq(0).text().trim();
	var value1 = $(event).find("td").eq(1).text().trim();
 
	var screen = document.getElementById('screenType').value;
 
	$("#branchCode").val(value);
	console.log("branch" + value);
	$("#branchFullName").val(value1);
	console.log("branch Full Name" + value1);
	if (screen == 'MakerScreen') {
		$("#myForm").attr("action", "makerProcess");
		$("#myForm").submit();
	} else if (screen == 'CheckerScreen') {
		$("#myForm").attr("action", "checkerProcess");
		$("#myForm").submit();
	} else if (screen == 'EnquiryScreen') {
		$("#myForm").attr("action", "enquiryProcess");
		$("#myForm").submit();
	} else if (screen == 'MakerBookingScreen') {
		$("#myForm").attr("action", "bookingProcess");
		$("#myForm").submit();
	}else if (screen == 'MakerCancelScreen') { 
		$("#myForm").attr("action", "cancelProcess");
		$("#myForm").submit();
	}
}
 
function selectCurrency(event) {
	$(".currencyList").removeClass("highlighted");
	$(event).addClass("highlighted");
	var value = $(event).find("td").eq(0).text().trim();
	var value1 = $(event).find("td").eq(1).text().trim();
 
	var screen = document.getElementById('screenType').value;
 
	$("#dealCurrency").val(value);
	console.log("currency" + value);
	$("#currencyFullName").val(value1);
	console.log("currency Full Name" + value1);
	if (screen == 'MakerScreen') {
		$("#myForm").attr("action", "makerProcess");
		$("#myForm").submit();
	} else if (screen == 'CheckerScreen') {
		$("#myForm").attr("action", "checkerProcess");
		$("#myForm").submit();
	} else if (screen == 'EnquiryScreen') {
		$("#myForm").attr("action", "enquiryProcess");
		$("#myForm").submit();
	} else if (screen == 'MakerBookingScreen') {
		$("#myForm").attr("action", "bookingProcess");
		$("#myForm").submit();
	}else if (screen == 'MakerCancelScreen') { 
		$("#myForm").attr("action", "cancelProcess");
		$("#myForm").submit();
	}
}
 
function selectTreasuryRefNumber(event) {
	$(".treasuryList").removeClass("highlighted");
	$(event).addClass("highlighted");
	var value = $(event).find("td").eq(0).text().trim();
	var value1 = $(event).find("td").eq(1).text().trim();
	var value2 = $(event).find("td").eq(2).text().trim();
	var value3 = $(event).find("td").eq(3).text().trim();
	var value4 = $(event).find("td").eq(4).text().trim();
 
	$("#treasuryRefNo").val(value);
	$("#customerID").val(value1);
	$("#bookingDate").val(value2);
	$("#dealValidFromDate").val(value3);
	$("#dealValidToDate").val(value4);

	var screen = document.getElementById('screenType').value;
 
	if (value != '' && value1 != '' && screen == 'MakerBookingScreen') {
		$("#myForm").attr("action", "fetchBookingTreasuryDetails");
		$("#myForm").submit();
	} else if(value != '' && value1 != '' && screen == 'MakerCancelScreen'){
		$("#myForm").attr("action", "fetchCancelTreasuryDetails");
		$("#myForm").submit();
	}else if(screen == 'MakerBookingScreen'){
		$("#myForm").attr("action", "bookingProcess");
		$("#myForm").submit();
	}else if(screen == 'MakerCancelScreen'){
		$("#myForm").attr("action", "cancelProcess");
		$("#myForm").submit();
	}
 
}
 
function selectFwdContractRefNumber(event) {
	$(".fwdContractDetailsList").removeClass("highlighted");
	$(event).addClass("highlighted");
	var value = $(event).find("td").eq(0).text().trim();
	/*var value1 = $(event).find("td").eq(1).text().trim();
	var id = $(event).find("input[type ='hidden']").val().trim();
	var enquiryValue = id + ':' + value;*/
 
	$('#idAndFwdContractNo').val(value);
 
	var fwdContractNo = $('#idAndFwdContractNo').val();
	if (value != '' && fwdContractNo !='') {
		$("#myForm").attr("action", "fetchFWCReferenceDetails");
		$("#myForm").submit();
	} else {
		$("#myForm").attr("action", "cancelForwardContract");
		$("#myForm").submit();
	}
 
}
 
function selectLimitID(event) {
	$(".limitList").removeClass("highlighted");
	$(event).addClass("highlighted");
	var limitID = $(event).find("td").eq(0).text().trim();
	var limitAmt = $(event).find("td").eq(2).text().trim();
	var liabilityAmt = $(event).find("td").eq(3).text().trim();
	var currency = $(event).find("td").eq(4).text().trim();
	var availLimit=limitAmt-liabilityAmt+' '+currency;

 
	$("#limitID").val(limitID);
	console.log("limit ID" + limitID);
	$("#availableLimit").val(availLimit);
	console.log("avail Limit" + availLimit);
	var screen = document.getElementById('screenType').value;
 
	if(screen == 'MakerBookingScreen'){
		$("#myForm").attr("action", "bookingProcess");
		$("#myForm").submit();
	}else if(screen == 'MakerCancelScreen'){
		$("#myForm").attr("action", "cancelProcess");
		$("#myForm").submit();
	}
 
}
 
function fetchCustomerStaticData() {
	$("#myForm").attr("action", "fetchCustomerStaticData");
	$("#myForm").submit();
}
 
function fetchAccountStaticData() {
	$("#myForm").attr("action", "fetchAccountStaticData");
	$("#myForm").submit();
}
 
function fetchBranchStaticData() {
	$("#myForm").attr("action", "fetchBranchStaticData");
	$("#myForm").submit();
}
 
function fetchCurrencyStaticData() {
	$("#myForm").attr("action", "fetchCurrencyStaticData");
	$("#myForm").submit();
}
 
function fetchTreasuryDetails() {
	$("#myForm").attr("action", "fetchTreasuryDetails");
	$("#myForm").submit();
}
 
function fetchFwdContractDetailsToCancel() {
	$("#myForm").attr("action", "fetchFwdContractDetailsToCancel");
	$("#myForm").submit();
}
 
function fetchLimitDetails() {
	$("#myForm").attr("action", "fetchLimitDetails");
	$("#myForm").submit();
}
 
function doLoad() {
	$('body').modal("hide");
	$('body').css('display', 'block');
	$('body').removeClass('removePageLoad');
	$('body').addClass('removePageLoad');
}
function onChangeLoad() {
	$('body').modal({
		show : 'false'
	});
	$('body').removeClass('removePageLoad');
	$('body').addClass('addPageLoad');
}