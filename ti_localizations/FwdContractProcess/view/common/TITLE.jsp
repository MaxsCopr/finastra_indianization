<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="struts_tag" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
</head>
<body>
<div style="width: 102%; height: 56px; margin: 0px:padding:none;">
<img src="images/FusionBanking.png" />
</div>
 
	<div class="row" style="background-color: #666;">
<div class="col-md-4">
<h6 style="color: #fff;">System Date: &nbsp;<struts_tag:property value="%{#session.processDate}" /></h6>
<div style="clear: both; height: 10px;"> </div>
</div>
</div>
</body>
</html>