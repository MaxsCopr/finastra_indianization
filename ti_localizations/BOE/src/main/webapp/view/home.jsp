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
 
function windowClose()
{
top.close();
}
 
</script>	
</head>
 
<body class="body_bg">
<jsp:include page="includes/TITLE.jsp" />  
<div class="row">
<div class="col-md-2">
<div class="side_nav"  style="width: 215px; ">
<ul class="nav nav-pills nav-stacked">
<boe:if test="%{#session.xEvtRefNum.length() > 0}">
<li style="text-align: center;"><a href="#" onclick="windowClose()">Close</a></li>
</boe:if>										
<boe:else>
<li style="text-align: center;"><a href="closeBOEWindow">Close</a></li>
</boe:else>

</ul>
</div>
<div class="side_nav">
</div>
</div>
<div class="col-md-10 content_box">
<h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;BOE Process</h5>
<div id="userIdMessage" style="color: orange"></div>
<div class="row cont_colaps">
<div class="col-md-12">
<div id="GR_Head"><div class="btn btn-link" onclick="GRCollapsePane()">BOE Maker Process</div><br/>
<div id="GR_List" class="col-md-12" style="display:block;">		
<%-- <boe:textfield ></boe:textfield>	
			 --%>
<div class="btn btn-link" id="GR_Process">
<a href="BillEnteryForm" id="sMstRefNums">Bill vs Multiple BOEs</a></div><br/>
<div class="btn btn-link" id="GR_Process"><a href="billNoToManyPaymentReference">BOE vs Multiple Bills</a></div><br/>
<%-- <boe:if test="%{#session.loginType != 'Yes'}"> --%>
 
						<div class="btn btn-link" id="GR_Process"><a href="manualBOE">Manual Bill Of Entry</a></div><br/>
<%-- <boe:if test="%{#session.loginType.length() ==0}"> --%>
<div class="btn btn-link" id="GR_Process"><a href="viewRejectedTransactions">Rejected Records</a></div><br/>								
<div class="btn btn-link" id="GR_Process"><a href="BOEBlk">BOE Bulk Upload</a></div><br/>
<%-- </boe:if> --%>
</div>
</div>	
</div></div>
</div>	
</div>
</body>
</html>