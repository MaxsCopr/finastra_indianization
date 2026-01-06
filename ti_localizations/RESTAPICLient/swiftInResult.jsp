<%@page import="com.swift.in.swiftInProcessPull"%>
<%@page import="  java.util.HashMap"%>
<%@page import=" java.util.Map"%>
 
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>UBI-SWIFT IN</title>
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
<div>
<h4>SWIFT MESSAGE</h4>
<textarea name="swiftINMessage" id="swiftINMessage" rows="15"
			cols="80" spellcheck="false" readonly> <%=session.getAttribute("swiftINMessage")%>
</textarea>
<h4>TI SWIFT REQUEST</h4>
<textarea name="TIRequest" id="TIRequest" rows="15" cols="80"
			spellcheck="false" readonly> <%=session.getAttribute("TIRequest")%>
</textarea>
<h4>TI SWIFT RESPONSE</h4>
<textarea name="TIResponse" id="TIResponse" rows="15" cols="80"
			spellcheck="false" readonly> <%=session.getAttribute("TIResponse")%>
</textarea>
</div>
</body>
</html>