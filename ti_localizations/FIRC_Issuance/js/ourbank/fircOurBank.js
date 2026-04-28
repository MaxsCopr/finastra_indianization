function getFircDetails(){
	 $("#myForm").attr("action","fetchOurBankDetails");
	 $("#myForm").submit();
}
 
function validateForm(){
	 $("#myForm").attr("action","validateOurBankDetails");
	 $("#myForm").submit();
}
 
function submitForm(){
	 $("#myForm").attr("action","insertOurBankDetails");
	 $("#myForm").submit();
}
 
function resetForm(){
	 $("#myForm").attr("action","fircOurBank");
	 $("#myForm").submit();
}