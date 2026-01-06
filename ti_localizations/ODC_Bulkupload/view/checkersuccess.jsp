<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="scf" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
 
<jsp:include page="common/header.jsp" />
<div class="row">
<div class="col-md-2">
<div class="side_nav" style="width: 200px;">
<ul class="nav nav-pills nav-stacked">
<li style="text-align: center;"><a href="checker">Close</a></li>
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
 
			<div id="userIdMessage" style="color: orange"></div>
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
</div>
</div>
<jsp:include page="common/footer.jsp" />