<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="gr" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Product List</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
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
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="js/date_search.js"></script>
<script type="text/javascript" src="js/Theme.js"></script>
<script type="text/javascript" src="js/commonTiplus.js"></script>
<script type="text/javascript" src="js/productSelection.js"></script>
<script>
$(document).ready(function(){
	$('input[type="submit"],a,input[type="button"],onchange,onblur').click(function(){
		$('body').modal({
			   show: 'false'
			 });
		$('body').removeClass('removePageLoad');
		$('body').addClass('addPageLoad');
	});
});
 
 
if ( window.addEventListener ) { 
		  $('body').modal({
			   show: 'false'
			 });
		 $('body').addClass('addPageLoad');
	window.addEventListener( "load", doLoad, false );
	}
</script>
 
<!--[if IE 7]>
<![endif]-->
</head>
 
<body class="body_bg" onload="display_ct()">
<%@ include file="/view/includes/TITLE.jsp" %>  
 
  <gr:form method="post" id="myForm" name="form">
<div class="row">
<div class="col-md-2">
<div class="side_nav"  style="width: 200px; ">
<ul class="nav nav-pills nav-stacked">
<li style="text-align: center;"><a href="#" onclick="returnHome()"><gr:text name="Close" /></a></li>
</ul>
</div>
<br />			
</div>
<div class="col-md-10 content_box">
<div style="clear: both; height: 40px;"> </div>
<%-- <h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<gr:text name="Charge List" /></h5> --%>
<div id="userIdMessage" style="color: orange"></div>
<div class="row page_content">
<div class="col-md-12">
<div class="col-md-5">
<div class="form-group">
<label class="col-md-5 Control-label"
											style="font-weight: normal;">Product Code</label>
<div class="col-md-4 input-group input-group-md">
<gr:textfield id="productCode" name="productVo.filterProductCode" 
												cssClass="form-control text_box">
</gr:textfield>
</div>
</div>
</div>
<div class="col-md-5">
<div class="form-group">
<label class="col-md-5 Control-label"
											style="font-weight: normal;">Product Description</label>
<div class="col-md-4 input-group input-group-md">
<gr:textfield id="productDesc" name="productVo.filterProductDesc" 
												cssClass="form-control text_box">
</gr:textfield>
</div>
</div>
</div>
<div class="col-md-2">
<div class="form-group">
<input type="button" value="Refresh" class="button"
											onclick="fetchProductList()" />
</div>
</div>

</div>			
</div>
<div style="clear: both; height: 20px;"> </div>

<div class="row cont_colaps">
<div class="col-md-12">						
<div class="col-md-12">
<div class="row page_content">
<div class="page_collapsible" id="body-section1">
<span></span>
<h5 style="font-weight: bold; font-size: 13px;">&nbsp;<gr:text name="Charge Details" /></h5>
</div>
<div class="form-group">
<div class="table">
<table border="1px" align="left">
<tbody>
<tr>
<th align="left" style="width:400px; padding:4px 5px;">Product Code</th>
<th align="left" style="width:600px; padding:4px 5px;">Product Description</th>
<!-- <th align="left" style="width:400px; padding:4px 5px;">Customer NO</th> -->
</tr>											    
<c:if test="${empty productList}">
<tr>
<td colspan="3">No records found</td>
</tr>
</c:if>
<c:if test="${not empty productList}">
<gr:iterator value="productList" id="productListID" >
<tr class="productList" ondblclick="select(this)">
<td style="padding:4px 5px;">
<div class="form-group" align="left">
<gr:property value="productId"/>
<div class="col-md-8 input-group input-group-md"></div>
</div>
 
												</td>
<td style="padding:4px 5px;">
<div class="form-group" align="left">
<gr:property value="productDesc"/>														
<div class="col-md-8 input-group input-group-md"></div>
</div>
</td>
<td style="padding:4px 5px; display:none">
<div class="form-group" align="left">
<gr:property value="productKey97"/>														
<div class="col-md-8 input-group input-group-md"></div>
</div>
</td>
<%-- <td style="padding:4px 5px; display:none">
<div class="form-group" align="left">
<gr:property value="accType"/>														
<div class="col-md-8 input-group input-group-md"></div>
</div>
</td> --%>																
</tr>
</gr:iterator>	
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
<gr:hidden name="chargeVO.customerCif"></gr:hidden> 
<gr:hidden id="productTypeID" name="chargeVO.productType"></gr:hidden>
<gr:hidden id="productID" name="chargeVO.productId"></gr:hidden>
<gr:hidden id="productKey97ID" name="chargeVO.productKey97"></gr:hidden>
<gr:hidden id="productDescID" name="chargeVO.productDesc"></gr:hidden> 
<gr:hidden name="chargeVO.chargeType"></gr:hidden>
<gr:hidden name="chargeVO.chargeId"></gr:hidden>
<gr:hidden name="chargeVO.chargeKey97"></gr:hidden>	
<gr:hidden name="chargeVO.chargeDesc"></gr:hidden>
 
					</div>
</div>
</div>
</gr:form>
</body>
</html>