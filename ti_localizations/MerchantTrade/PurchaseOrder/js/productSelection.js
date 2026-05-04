function select(event){
	 $(".customerlist").removeClass("highlighted");
	 $(event).addClass("highlighted");
	 var id = $(event).find("td").eq(0).text().trim();
	 var desc = $(event).find("td").eq(1).text().trim();
	 var key97 = $(event).find("td").eq(2).text().trim();
	 onChangeLoad();
	 $("#productID").val(id);
	 $("#productKey97ID").val(key97);
	 $("#productDescID").val(desc);
	 $("#productTypeID").val(id+"-"+desc);
	 $("#myForm").attr("action","gotoHome");
	 $("#myForm").submit(); 
}
 
 
function fetchProductList(){
	$("#myForm").attr("action","fetchProductList");
	 $("#myForm").submit();	
}
function returnHome(){
	$("#myForm").attr("action","gotoHome");
	 $("#myForm").submit();	
}
 
function doLoad() {
	   $('body').modal("hide");
	   $('body').css('display','block');
	   $('body').removeClass('removePageLoad');
	   $('body').addClass('removePageLoad');
}
function onChangeLoad(){
	$('body').modal({
		   show: 'false'
		 });
	$('body').removeClass('removePageLoad');
	$('body').addClass('addPageLoad');
}