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
<div class="side_nav"  style="width: 215px; ">
<ul class="nav nav-pills nav-stacked">				
<boe:if test="%{#session.loginType == 'Yes'}">
<li style="text-align: center;"><a href="#" onclick="windowclose()">Close</a></li>
</boe:if>		
<boe:else>
<li style="text-align: center;"><a href="closeWindow">Close</a></li>
</boe:else>			
</ul>
</div>
<div class="side_nav">
</div>
</div>
<div class="col-md-10 content_box">                                                                               
<br></br>			
<div class="col-md-12">
<div class="page_collapsible" id="body-section1"><span></span>
<h5 style="font-weight: bold; font-size:14px;">BOE Process</h5>
</div>
<table border="1" width="1050px">
<tr>
<td style="padding:4px 5px;"><a id='golink10' href="makerProcess" style="text-decoration:none;"><font color="black"  style="font-weight: normal; font-size:12px;"> BOE Maker Process</font></a></td>
</tr>
<tr>
<td style="padding:4px 5px;"><a id='golink11' href="checkerProcess" style="text-decoration:none;"><font color="black"  style="font-weight: normal; font-size:12px;"> BOE Checker Process</font></a></td>
</tr>
<tr>
<td style="padding:4px 5px;"><a id='golink12' href="manualBOESearch" style="text-decoration:none;"><font color="black"  style="font-weight: normal; font-size:12px;"> Manual BOE Status</font></a></td>
</tr>			
<tr>
<td style="padding:4px 5px;"><a id='golink12' href="manualBOEBulkUpload" style="text-decoration:none;"><font color="black"  style="font-weight: normal; font-size:12px;"> Manual BOE Bulk Upload</font></a></td>
</tr>
<tr>
<td style="padding:4px 5px;"><a id='golink12' href="OBBBulkUpload" style="text-decoration:none;"><font color="black"  style="font-weight: normal; font-size:12px;">OBB Bulk Upload</font></a></td>
</tr>
</table>
</div>
</div>
</div>
</body>
</html>