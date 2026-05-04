<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="boe" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>Home Page</title>
<jsp:include page="/view/common/header.jsp" />	
<script>	
 
		function windowclose() {	
			top.close();	
		}
</script>
</head>
 
<body class="body_bg">
<jsp:include page="includes/TITLE.jsp" />
 
	<div class="row">
<div class="col-md-2">
<div class="side_nav" style="width: 215px;">
<ul class="nav nav-pills nav-stacked">	
<boe:if test="%{#session.xEvtRefNum.length() > 0}">
<li style="text-align: center;"><a href="#" onclick="windowclose()">Close</a></li>
</boe:if>		
<boe:else>				
<li style="text-align: center;"><a href="closeBOEWindow">Close</a></li>
</boe:else>
</ul>
</div>
<div class="side_nav"></div>
</div>
<div class="col-md-10 content_box">
<h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;BOE
				Process</h5>
<div id="userIdMessage" style="color: orange"></div>
<div class="row cont_colaps">
<div class="col-md-12">
<div id="GR_Head">
<div class="btn btn-link" onclick="GRCollapsePane()">BOE Checker Process</div>
<br />
<div id="GR_List" class="col-md-12" style="display:block;">
<div class="btn btn-link" id="BOE_Process"><a href="checker_multi_paymentReferences">BOE Payment Checker</a></div><br/>
<div class="btn btn-link" id="MB_Process"><a href="checkerManualBOE">Manual BOE Checker</a></div><br/>
</div>
</div>
</div>
</div>
<boe:hidden id="loginType" name="loginType" value="%{#session.loginType}"></boe:hidden>
</div>
</div>
</body>
</html>