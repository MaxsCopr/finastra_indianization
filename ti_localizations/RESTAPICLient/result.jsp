<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>UBI-REST API</title>
<script type="text/javascript">
</script>
</head>
<body>
 
	<form action="RestProcess" method="get">
<div class="col-sm-6">
<h4>Bank Request</h4>
<textarea name="requestXML" id="requestXML" rows="15" cols="80"
				spellcheck="false" readonly> <%=session.getAttribute("requestXML")%>
</textarea>
<!-- <input type="submit" value="Submit"> -->
 
		</div>
<div class="col-sm-6">
<h4> Bank Response</h4>
<textarea name="responseXML" id="requestXML" rows="15" cols="80"
				spellcheck="false" readonly><%=session.getAttribute("responseXML")%>
</textarea>
</div>
</form>
</body>
</html>