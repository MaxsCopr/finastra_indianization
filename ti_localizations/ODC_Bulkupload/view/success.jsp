<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="scf" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
 
<jsp:include page="common/header.jsp" />
<scf:form method="post" id="myForm">
<div class="row">
<div class="col-md-2">
<div class="side_nav" style="width: 200px;">
<ul class="nav nav-pills nav-stacked">
<!-- UAT3-->
<!-- <li style="text-align: center;"><a href="https://10.10.20.165:443/tiplus2-global/login">Close</a></li> --> 
<!-- UAT2-->
<!-- <li style="text-align: center;"><a href="https://10.10.20.183:443/tiplus2-global/login">Close</a></li> -->
<!-- WORKFLOW -->
<li style="text-align: center;"><a href="close">Close</a></li>
<!-- SIT -->
<!-- <li style="text-align: center;"><a href="https://10.10.20.136:443/tiplus2-global/login">Close</a></li> -->
<li style="text-align: center;"><a href="maker">Upload Again</a></li>
<!-- <li style="text-align: center;"><a href="checker">Close</a></li> -->
</ul>
</div>
<!-- <br />
<div class="side_nav">
<ul class="nav nav-pills nav-stacked">
<li><a href="#" onclick="submitForm()">OK</a></li>
<li><a href="#" onclick="validateForm()">Validate</a></li>
<li><a href="#" onclick="cancel()">Cancel</a></li>
</ul>
</div>
<br /> -->
</div>
 
		<div class="col-md-10 content_box">
 
			<h5 class="row fontcol spac">Successfully Uploaded</h5>
<a onclick="exexcel1()" style="margin-left: 92%;" id="excl" title="Export to Excel"><img
					src="images/excel-icon.jpg" alt="Export to Excel" width="30" height="30"></a>
 
			<div id="userIdMessage" style="color: orange"></div>
<div  >
<!-- <input type="button" value="Show Grid" id="upload_war" class="button" onclick="grid()"  />	 -->
<br>
<%-- <scf:if test="%{transactionList1.size()>0}"> --%>
<%-- </scf:if> --%>
</div>		
<%-- <div class="form-group">
<div class="table" >
<table border="1px" align="left" id="approvedlist">
<tbody>
<tr>
<th style="text-align: center; width: 200px;"><label>Finance Reference</label></th>
 
										<th style="text-align: center; width: 200px;"><label>Invoice Reference</label></th>
 
										 <th style="text-align: center; width: 200px;"><label>Invoice Amount</label></th>
 
										 <th style="text-align: center; width: 300px;"><label>Finance Amount</label></th>
 
										<th style="text-align: center; width: 200px;"><label>Disc %</label></th>
<th style="text-align: center; width: 200px;"><label>ROI</label></th> 
<th style="text-align: center; width: 200px;"><label>Int Amount</label></th>
<th style="text-align: center; width: 200px;"><label>Status</label></th>
<th style="text-align: center; width: 200px;"><label>Buyer Id</label></th>
<th style="text-align: center; width: 200px;"><label>Seller Id</label></th>  
</tr>
<scf:if test='transactionList1!=null && transactionList1.size>0'>
<scf:iterator value="transactionList1" id="approvedlist">
<tr class="">
<td style="width:20%">
<div class="form-group" align="center">
<scf:property value="XL_FINANCE_REFERENCE" />
</div>
</td>
<td style="width:20%">
<div class="form-group" align="center">
<scf:property value="XL_INVOICE_REF" />
</div>

 
												</td>
<td style="width:20%">
<div class="form-group" align="center">
<scf:property value="XL_INVOICE_AMOUNT" />
</div>
</td>
<td style="width:20%">
<div class="form-group" align="center">
<scf:property value="XL_FINANCE_AMOUNT" />
</div>
</td>
<td style="width:20%">
<div class="form-group" align="center">
<scf:property value="XL_DISCOUNTING_PERCENTAGE" />
</div>
</td>
<td style="width:20%">
<div class="form-group" align="center">
<scf:property value="XL_ROI" />
</div>
</td>
<td style="width:20%">
<div class="form-group" align="center">
<scf:property value="XL_INTEREST_AMOUNT" />
</div>
</td>
<td style="width:20%">
<div class="form-group" align="center">
<scf:property value="XL_STATUS" />
</div>
</td>
<td style="width:20%">
<div class="form-group" align="center">
<scf:property value="XL_BUYER_ID" />
</div>
</td>
<td style="width:20%">
<div class="form-group" align="center">
<scf:property value="XL_SELLER_ID" />
</div>
</td>
 
											</tr>
</scf:iterator>
</scf:if>
</tbody>
</table>
</div>
</div> --%>

<%-- <div class="col-md-12">
<!-- <div class="page_collapsible" id="body-section1">
<span></span>
<h5 style="font-weight: bold; font-size: 13px;">&nbsp;In</h5>
</div> -->
<div class="form-group">
<div class="table" style="min-height:500px;max-height:520px;">
<table border="1px" align="left" id="invoiceApprovedList">
<tbody>
<tr>
<th style="text-align: left; width: 200px;"><label>Master Reference</label></th>
 
										<th style="text-align: left; width: 200px;"><label>Uploaded Status</label></th>
 
										<!-- <th style="text-align: left; width: 200px;"><label>Finance Status</label></th> -->
 
										<!-- <th style="text-align: left; width: 300px;"><label>Details</label></th>
 
										<th style="text-align: left; width: 200px;"><label>Overridden</label></th> -->
</tr>
<scf:if test='invoiceApprovedList!=null && invoiceApprovedList.size>0'>
<scf:iterator value="invoiceApprovedList" id="invoiceApprovedList">
<tr class="">
<td style="width:20%">
<div class="form-group" align="left">
<scf:property value="invNumber" />
<div class="col-md-8 input-group input-group-md"></div>
</div>
 
												</td>
<td style="width:20%">
<div class="form-group" align="left">
<scf:property value="invStatus" />
<div class="col-md-8 input-group input-group-md"></div>
</div>
 
												</td>
<td style="width:20%">
<div class="form-group" align="left">
<scf:property value="finStatus" />
<div class="col-md-8 input-group input-group-md"></div>
</div>
</td>
<td style="">
<div class="form-group" align="left">
<scf:property value="errorDetails" />
<div class="col-md-8 input-group input-group-md"></div>
</div>
</td>
<td style="">
<div class="form-group" align="left">
<scf:property value="errorMsg" />
<div class="col-md-8 input-group input-group-md"></div>
</div>
</td>
 
											</tr>
</scf:iterator>
</scf:if>
</tbody>
</table>
</div>
</div>
</div> --%>
<scf:hidden id="batchID" name="makervo.batchId" />
</div>
</div>
</scf:form>
<script type="text/javascript">
	function exexcel1(){
	$("#myForm").attr("action", "downloadExcel1");
	$("#myForm").submit();
	}
</script>
<script>
	function grid(){
// executes when HTML-Document is loaded and DOM is ready
		 //alert("Coming");
    	$("#myForm").attr("action", "endgrid");
    	$("#myForm").submit();
	}
</script>
 
<jsp:include page="common/footer.jsp" />