function select(event) {
	$(".customerlist").removeClass("highlighted");
	$(event).addClass("highlighted");
	var value = $(event).find("td").eq(0).text().trim();
	var value1 = $(event).find("td").eq(1).text().trim();
 
	$("#cifCodeID").val(value);
	console.log("Cif"+value);
	$("#customerName").val(value1);
	console.log("Cif Name"+value1);
	$("#myForm").attr("action", "gotoHome");
	$("#myForm").submit();
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
 
 
function select1(event) {
	$(".customerlist").removeClass("highlighted");
	$(event).addClass("highlighted");
	var value = $(event).find("td").eq(0).text().trim();
	var value1 = $(event).find("td").eq(1).text().trim();
 
	$("#cifCodeID").val(value);
	$("#customerName").val(value1);
	$("#myForm").attr("action", "gotoHome");
	$("#myForm").submit();
}
 
function selectGoodsCode(event) {
	$(".customerlist").removeClass("highlighted");
	$(event).addClass("highlighted");
	var value = $(event).find("td").eq(0).text().trim();

 
	$("#goodsCode").val(value);
	$("#myForm").attr("action", "setGoodsCodeValue");
	$("#myForm").submit();
}
function selectInwardDetails(event){
	$(".customerlist").removeClass("highlighted");
	$(event).addClass("highlighted");
	var value1 = $(event).find("td").eq(0).text().trim();
	var value2 = $(event).find("td").eq(0).text().trim();

 
	$("#master").val(value1);
	$("#custNo").val(value2);
	$("#myForm").attr("action", "setMasterDetails");
	$("#myForm").submit();
}
function fetchCustomer() {
	$("#myForm").attr("action", "fetchCustomer");
	$("#myForm").submit();
}
 
function fetchIncoTerms() {
	$("#myForm").attr("action", "fetchIncoTerms");
	$("#myForm").submit();
}
 
function fetchGoodsCode() {
	$("#myForm").attr("action", "searchGoodsCode");
	$("#myForm").submit();
}
function fetchInwardDetails() {
	$("#myForm").attr("action", "fetchInwardDetails");
	$("#myForm").submit();
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