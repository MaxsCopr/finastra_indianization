<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="gr" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<!-- <meta http-equiv="Cache-control" content="no-cache" />
<meta http-equiv="Expires" content="-1" /> -->
<title>Advance Payment Search</title>
<jsp:include page="/view/common/header.jsp" />
 
	<script>
 
		function windowclose() {
			top.close();
		}
		function fetchIRMDetails () {
			$('#myForm').attr('action', 'fecthIRMSearch');
			$('#myForm').submit();
		}
</script>
</head>
 
<body class="body_bg" onload="display_ct()">
<%@ include file="/view/includes/TITLE.jsp"%>
 
	<gr:form method="post" id="myForm" name="form">
<div class="row">		
<div class="col-md-2">
<div class="side_nav">
<ul class="nav nav-pills nav-stacked">						
<li style="text-align: center;"><a href="#"	onclick="windowclose()">Close</a></li>
</ul>
</div>
<br />
<div class="side_nav">
<ul class="nav nav-pills nav-stacked">						
<li style="text-align: center;"><a href="irmSearch"><gr:text name="Reset" /></a></li>
</ul>
</div>
<br/>
</div>
 
			<div class="col-md-10 content_box">
<h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Advance Payment Search</h5>				
 
				<div id="userIdMessage" style="color: orange"></div>
<div class="row cont_colaps">				
<div class="col-md-12">					
<div class="page_collapsible" id="body-section1">
<span></span>
<h5 style="font-weight: bold; font-size: 13px;">&nbsp;<gr:text name="Input Details" /></h5>
</div>	
 
						<div class="row page_content">
<div class="col-md-6">      
<div class="form-group">
<label class="col-md-4 Control-label"
										style="font-weight: normal;"><gr:text name="IRM Number" /></label>
<div class="col-md-4 input-group input-group-md">
<gr:textfield id="OrefNum" name="issuanceVO.transrefno"
											cssClass="form-control text_box" maxlength="30" />
</div>
</div>						
</div>
 
							<div class="col-md-6">			
<div class="form-group">
<label class="col-md-4 Control-label"
										style="font-weight: normal;"><gr:text name="CIF No" /></label>
<div class="col-md-4 input-group input-group-md">
<gr:textfield id="custNo" name="issuanceVO.cifNo"
											cssClass="form-control text_box" />
</div>
</div>
<div class="form-group">
<label class="col-md-4 Control-label"
										style="font-weight: normal;"></label>
<div class="col-md-4 input-group input-group-md">										
<input type="button" value="Refresh" class="button"
											style="margin-top: 5px;" onclick="fetchIRMDetails()" />
</div>
</div>
</div>
</div><br/><br/>
</div>
 
						<div class="col-md-12">
<div class="page_collapsible" id="body-section1">
<span></span>
<h5 style="font-weight: bold; font-size: 11px;">&nbsp;Advance Payment Details</h5>
</div>
<div class="col-md-12">
<div class="form-group">
<div class="table_Checker" style="float:left;">
<table border="1px" align="left" >
<tbody>
<tr>
<th style="text-align: left; width: 500px; padding: 4px 5px;"><label>IRM Number</label></th>
 
														<th style="text-align: left; width: 500px; padding: 4px 5px;"><label>CIF No</label></th>												
<th style="text-align: left; width: 500px; padding: 4px 5px;"><label>Currency</label></th>
<th style="text-align: left; width: 500px; padding: 4px 5px;"><label>Amount</label></th>		
<th style="text-align: left; width: 500px; padding: 4px 5px;"><label>O/S Amount</label></th>										
 
													</tr>
<c:if test="${empty irmList}">
<tr>
<td colspan="5">No records found</td>
</tr>
</c:if>
<c:if test="${not empty irmList}">
<gr:iterator value="irmList" id="irmList">																											
<tr class="ormList">				
<td style="text-align: left; padding: 4px 5px;">
<div class="form-group" align="left">
<gr:property value="transrefno" />
<div class="col-md-8 input-group input-group-md"></div>
</div>
</td>
 
																<td style="text-align: left; padding: 4px 5px;">
<div class="form-group">
<gr:property value="cif_no" />
<div class="col-md-8 input-group input-group-md"></div>
</div>
</td>															

<td style="text-align: left; padding: 4px 5px;">
<div class="form-group" align="left">
<gr:property value="currency" />
<div class="col-md-8 input-group input-group-md"></div>
</div>
</td>
<td style="text-align: left; padding: 4px 5px;">
<div class="form-group" align="left">
<gr:property value="amount" />
<div class="col-md-8 input-group input-group-md"></div>
</div>
</td>
<td style="text-align: left; padding: 4px 5px;">
<div class="form-group" align="left">
<gr:property value="available_amt" />
<div class="col-md-8 input-group input-group-md"></div>
</div>
</td>
</tr>
</gr:iterator>
</c:if>
</tbody>
</table><br/><br/>											 																		
</div>
</div>															 	
</div>								
</div>							 
</div><br/>						
</div>
<br />
</div>				
</gr:form>

</body>
</html>