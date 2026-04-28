$(document).ready(function() {
		$("#search").click(function(){
			 $("#myForm").attr("action","fetchBOEProcess");
			 $("#myForm").submit();
		});
		$("#reset").click(function() {
			window.location.href = "viewProcess";
		});
 
		$("#custList").click(function(){
			$("#myForm").attr("action","viewCustList");
			$("#myForm").submit();
		});
		$("#custSearchList").click(function(){
			 $("#myForm").attr("action","viewCustList");
			 $("#myForm").submit();
		});
		$("#closeButton").click(function(){
			 $("#myForm").attr("action","fetchBOEProcess");
			 $("#myForm").submit();
		});
});
 
function select(event){
	 $(".customerlist").removeClass("highlighted");
	 $(event).addClass("highlighted");	
	 var value1 = $(event).find("td").eq(2).text().trim();
	 $('#custCIFNo').val(value1);
	 $("#myForm").attr("action","fetchBOEProcess");
	 $("#myForm").submit(); 			 
}