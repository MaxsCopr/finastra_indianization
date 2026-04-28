<%@page import="com.swift.in.swiftInProcessPull"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
 
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
<h4>SWIFT IN Message from MQueue Processing ...</h4>
<%
		HashMap<String, String> result=new HashMap<>();
			 swiftInProcessPull aswiftInProcess = new swiftInProcessPull();
			 result=aswiftInProcess.fetchSwiftFromQueue();
		 HttpSession aHttpSession = request.getSession();
		 aHttpSession.setAttribute("swiftINMessage", result.get("swiftINMessage"));
		 aHttpSession.setAttribute("TIRequest", result.get("TIRequest"));
		 aHttpSession.setAttribute("TIResponse", result.get("TIResponse"));
 
			response.sendRedirect("swiftInResult.jsp");
	%>
 
</body>
</html>