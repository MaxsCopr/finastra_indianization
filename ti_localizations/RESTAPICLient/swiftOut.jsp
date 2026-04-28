<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>UBI-SWIFT OUT</title>
<script type="text/javascript">
	/* 
	 function onSubmit() {
	 alert('1');
	 $('#restAPI').attr('action', 'onSubmit');
	 alert('2');
	 $('#restAPI').submit();
	 } */
</script>
</head>
<body>
 
	<form action="SwiftOutProcess" method="get">
<div>
<h4>TI SWIFT MESSAGE</h4>
<textarea name="swiftOutMsg" id="swiftOut" rows="15" cols="80"
				spellcheck="false" >
</textarea>
</div>
<input type="submit" value="Submit">
<%-- <textarea name="responseXML" id="requestXML" rows="15" cols="80"
			spellcheck="false" value="<%= request.getAttribute("responseXML") %>">
</textarea> --%>
</form>
</body>
</html>