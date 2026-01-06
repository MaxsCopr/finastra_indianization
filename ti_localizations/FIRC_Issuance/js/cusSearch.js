function select(event) {
	$(".customerlist").removeClass("highlighted");
	$(event).addClass("highlighted");
	var value = $(event).find("td").eq(0).text().trim();
	var value1 = $(event).find("td").eq(1).text().trim();
 
	$("#cifCodeID").val(value);
	console.log("Cif"+value);
	$("#customerName").val(value1);
	console.log("Cif Name"+value1);
	$("#form1").attr("action", "gotoHome");
	$("#form1").submit();
}
 
function select2(event) {
	$(".customerlist").removeClass("highlighted");
	$(event).addClass("highlighted");
	var value = $(event).find("td").eq(0).text().trim();
	var value1 = $(event).find("td").eq(1).text().trim();
 
	$("#destinationCountry").val(value);
	console.log("Country"+value);
	$("#myForm").attr("action", "gotoHome");
	$("#myForm").submit();
}
 
function fetchCustomer() {
	$("#form1").attr("action", "fetchCustomer");
	$("#form1").submit();
}
 
function select1(event) {
	$(".customerlist").removeClass("highlighted");
	$(event).addClass("highlighted");
	var value = $(event).find("td").eq(0).text().trim();
	var value1 = $(event).find("td").eq(1).text().trim();
 
	$("#cifCodeID").val(value);
	$("#customerName").val(value1);
	$("#form1").attr("action", "gotoHome");
	$("#form1").submit();
}
 
function fetchMasterDetails(){
	$("#myForm").attr("action", "fetchInwardDetails");
	$("#myForm").submit();
}
function returnHome() {
	$("#myForm").attr("action", "gotoHome");
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