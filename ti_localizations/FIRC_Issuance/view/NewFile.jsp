<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="firc" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>Home Page</title>
 
<link rel="stylesheet" href="css/jquery-ui.css" />
<link rel="stylesheet" href="css/style.css" />
<link href="css/datepicker.css" rel="stylesheet"></link>
<link type="text/css" rel="stylesheet" href="css/bootstrap.css" /><link />
<link type="text/css" rel="stylesheet" href="css/bootstrap-dropdown.css" /><link />
<link type="text/css" rel="stylesheet" href="css/headfoot.css" /><link />
<link href="css/font-awesome.css" rel="stylesheet"></link>
<link rel="stylesheet" href="css/datepicker.css"></link>
<link rel="stylesheet" href="css/commonTiplus.css"></link>
 
 
<script src="js/jquery-1.9.1.js"></script>
<script src="js/jquery-ui.js"></script>
<script src="js/bootstrap.js" type="text/javascript"></script>
 
<script type="text/javascript" src="js/macxdnie81.js"></script>
<script type="text/javascript" src="js/maxcdnie82.js"></script>
 
<script src="js/bootstrap-datepicker.js" type="text/javascript"></script>
<script type="text/javascript" src="js/jquery.cookie.min.js"></script>
<script type="text/javascript" src="js/jquery.collapsible.min.js"></script>
<script type="text/javascript" src="js/highlight.pack.js"></script>
<script type="text/javascript" src="js/jquery.cookie.min.js"></script>
<script type="text/javascript" src="js/jquery.collapsible.min.js"></script>
<script type="text/javascript" src="js/highlight.pack.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="js/date_search.js"></script>
<script type="text/javascript" src="js/WiseConnect.js"></script>
<script type="text/javascript" src="js/commonTiplus.js"></script>
<script type="text/javascript" src="js/viewer/firc_issuanceviewer.js"></script>
 
 
<!--[if IE 7]>
<link rel="stylesheet" type="text/css" href="css/styleie7.css" />
<![endif]-->
</head>
 
<body class="body_bg" onload="display_ct()">
<%@ include file="/view/includes/TITLE.jsp" %>  
 
  <firc:form method="post" id="myForm" name="form">
<div class="row">
<div class="col-md-2">
<div class="side_nav"  style="width: 200px; ">
<ul class="nav nav-pills nav-stacked">
<li style="text-align: center;"><a href="#" id="closeButton">Close</a></li>
</ul>
</div>
<br />			
</div>
<div class="col-md-10 content_box">
 
		<h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Customer Search</h5>
<div id="userIdMessage" style="color: orange"></div>
<div class="row cont_colaps">
<div class="col-md-12">	
<div class="page_collapsible " id="body-section1">
<span></span>
<h5 style="font-weight: bold; font-size: 13px;">&nbsp;Input Details</h5>
</div>				
<div class="row page_content">
<div class="col-md-6">	
<div class="form-group">
<label class="col-md-4 Control-label"
										style="font-weight: normal; font-size: 13px;">Source
										Banking Business</label>
<div class="col-md-3 input-group input-group-md" 
										align="left" style="width: 88px;">
<firc:select list="bankname" name="otherBankVO.bankname"
											id="bankname" style="width: 219px; height: 19px" />
</div>
</div><br/>
 
								<div class="form-group">
<label class="col-md-4 Control-label"
										style="font-weight: normal; font-size: 13px;">CIF</label><i></i><i></i>
<div class="col-md-3 input-group input-group-md">
<firc:textfield name="otherBankVO.customerCIFNo"
														class="form-control text_box" 
														style="width: 138px; height: 28px" />
</div>
</div>
<div class="form-group">
<label
										class="col-md-4 Control-label"
										style="font-weight: normal; font-size: 13px;">Number</label>
<div class="col-md-3 input-group input-group-md">
<firc:textfield class="form-control text_box" name="otherBankVO.customerNo"
														style="width: 138px; height: 28px" />
</div>
</div>
 
							</div>
 
							<div class="col-md-6">			

<div class="form-group">
<label class="col-md-4 Control-label"
										style="font-weight: normal;">Full Name</label>
<div class="col-md-3 input-group input-group-md">
<firc:textfield class="form-control text_box" name="otherBankVO.customerName"
														style="width: 138px; height: 28px" />
 
									</div>
</div>
<div class="form-group">									
<div class="col-md-3 input-group input-group-md">
<firc:submit id="custSearchList" value="Refresh" class="button" />
</div>
</div>
</div>
</div>	
<div class="col-md-12">
<div class="row page_content">
<div class="page_collapsible" id="body-section1">
<span></span>
<h5 style="font-weight: bold; font-size: 13px;">&nbsp;Customer Details</h5>
</div>
<div class="form-boeoup">
<div class="table">
<table border="1px" align="left">
<tbody>
<tr>
<th align="left" style="width:200px; padding:4px 5px;">Mnemonic</th>
<th align="left" style="width:200px; padding:4px 5px;">Full name</th>
<th align="left" style="width:200px; padding:4px 5px;">Number</th>
<th align="left" style="width:200px; padding:4px 5px;">Country of residence</th>
<th align="left" style="width:200px; padding:4px 5px;">Blocked</th>											
</tr>											    
<c:if test="${empty custList}">
<tr>
<td colspan="5">No records found</td>
</tr>
</c:if>
<c:if test="${not empty custList}">
<firc:iterator value="custList" id="custList" >
<tr class="customerlist" ondblclick="select(this)">
<td style="padding:4px 5px;">
<div class="form-boeoup" align="left">
<firc:property value="custNo"/>
<div class="col-md-8 input-boeoup input-boeoup-md"></div>
</div>
 
												</td>
<td style="padding:4px 5px;">
<div class="form-boeoup" align="left">
<firc:property value="custName"/>														
<div class="col-md-8 input-boeoup input-boeoup-md"></div>
</div>
</td>
<td style="padding:4px 5px;">
<div class="form-boeoup" align="left">
<firc:property value="custCIFNo"/>														
<div class="col-md-8 input-boeoup input-boeoup-md"></div>
</div>
</td>	
<td style="padding:4px 5px;">
<div class="form-boeoup" align="left">
<firc:property value="country"/>														
<div class="col-md-8 input-boeoup input-boeoup-md"></div>
</div>
</td>
<td style="padding:4px 5px;">
<div class="form-boeoup" align="left">
<firc:property value="blocked"/>														
<div class="col-md-8 input-boeoup input-boeoup-md"></div>
</div>
</td>																	
</tr>
</firc:iterator>
</c:if>	
</tbody>
</table><br /><br />
</div>
</div>
</div>
</div>
</div>
</div>
<div>
<firc:hidden name="viewerVO.cifNo" id="custCIFNo"></firc:hidden>					
<firc:hidden name="viewerVO.RefNo" id="refNo"></firc:hidden>
<firc:hidden name="viewerVO.currency" id="currency"></firc:hidden>
<firc:hidden name="viewerVO.fircNo" id="fircNo"></firc:hidden>
</div>
</div>
</div>
</firc:form>
</body>
</html>