<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="firc" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
 
<!-- <meta http-equiv="Cache-control" content="no-cache" />
<meta http-equiv="Expires" content="-1" /> -->
 
<title>Home Page</title>
<jsp:include page="/view/common/header.jsp" />
</head>
 
<body class="body_bg" onload="display_ct()">  
<jsp:include page="includes/TITLE.jsp" />   
<div class="row">
<div class="col-md-2">
<div class="side_nav"  style="width: 215px; ">
<ul class="nav nav-pills nav-stacked">
<firc:if test="%{#session.count == 1}">				
<li style="text-align: center;"><a href="closeWindow">Close</a></li>
</firc:if>
<firc:else>
<li style="text-align: center;"><a href="home">Close</a></li>
</firc:else>
</ul>
</div>
<div class="side_nav">
</div>
</div>
<div class="col-md-10 content_box">
<h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FIRC Process</h5>
<div id="userIdMessage" style="color: orange"></div>
<div class="row cont_colaps">
<div class="col-md-12">
<div id="GR_List" class="col-md-12" style="display:block;">
<div class="btn btn-link" id="GR_Process"><a href="single">FIRC Single Printing</a></div><br/>
<div class="btn btn-link" id="GR_Process"><a href="bulk">FIRC Bulk Printing</a></div><br/>
</div>
</div>	
</div></div>
</div>
</body>
</html>