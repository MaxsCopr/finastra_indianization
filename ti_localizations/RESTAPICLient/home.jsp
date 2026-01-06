<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>UBI-REST API</title>
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
 
	<form action="RestProcess" method="get">
<div>
<h4>SERVICE</h4>
<textarea name="service" id="apiService" rows="2" cols="50">
</textarea>
<h4>API URL</h4>
<textarea name="URL" id="apiURL" rows="2" cols="50">
</textarea>
<h4>API KEY</h4>
<textarea name="key" id="apiKey" rows="2" cols="50">
</textarea>
<h4>Request</h4>
<textarea name="requestXML" id="requestXML" rows="15" cols="80"
				spellcheck="false" placeholder="TI Gateway request XML">
</textarea>
</div>
<input type="submit" value="Submit">
<%-- <textarea name="responseXML" id="requestXML" rows="15" cols="80"
			spellcheck="false" value="<%= request.getAttribute("responseXML") %>">
</textarea> --%>
</form>
</body>
</html>