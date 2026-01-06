<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="scf" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<jsp:include page="common/header.jsp" />
<script type="text/javascript" src="js/bulkUpload_Status.js"></script>		
<div class="row">
<div class="col-md-2">
<div class="side_nav" style="width: 200px;">
<ul class="nav nav-pills nav-stacked">
<!-- <li style="text-align: center;"><a href="https://encore:9469/tiplus2-global">Close</a></li> -->
<!--   <li style="text-align: center;"><a href="https://tradeuatapp106:8003/devglobal">Close</a></li>   -->
<!-- <li style="text-align: center;"><a href="https://tradeapp130:8003/idfc-global">Close</a></li> -->
<!-- <li style="text-align: center;"><a href="https://tradeuatapp107:8003/uatglobal">Close</a></li> -->
<li style="text-align: center;"><a href="<scf:property value="%{#session.closeURL}" />">Close</a></li>
<li style="text-align: center;"><a href="status" >Reset</a></li>
<!-- 	<li style="text-align: center;"><a href="https://tipluspreprod.idfcbank.com/preprod-global">Close</a></li> -->
<!-- <li style="text-align: center;"><a href="https://tiplusprod.idfcbank.com/liveglobal">Close</a></li> -->
</ul>
</div>
</div>
 
	<div class="col-md-10 content_box">
 
		<h5 class="row fontcol spac">Export Collection Bulk Upload -Status</h5>
 
		<div id="userIdMessage" style="color: orange"></div>
<div class="row cont_colaps">
<scf:form method="post" id="programmeLister">
<div class="col-md-12">
<div class="page_collapsible" id="body-section1">
<span></span>
<h5 style="font-weight: bold; font-size: 13px;">&nbsp;Batch Search</h5>
</div>
 
					<div class="row page_content">
<div class="col-md-6">
<div class="form-group">
<label class="col-md-4 Control-label"
									style="font-weight: normal;">Batch ID</label>
<div class="col-md-6 input-group input-group-md">
<scf:textfield name="statusvo.batchId" id="batchId"
										cssClass="form-control text_box" />
</div>
</div>	
<br>	
<div class="form-group">
<label class="col-md-4 Control-label"
									style="font-weight: normal;">Status</label>
<div class="col-md-6 input-group input-group-md">
<scf:select list = "status" name="statusvo.status" id="status"
										headerValue = "<--->" headerKey = '-1' cssClass="form-control text_box" />
</div>
</div>
<br>	
<div class="form-group">
<label class="col-md-4 Control-label"
									style="font-weight: normal;"></label>
<div class="col-md-6 input-group input-group-md">
<scf:submit action="viewerSearch" name="validate"
									   onclick="getDataFromView1()" cssClass="button" value="Search" />
</div>
</div>	


 
						</div>
<div class="col-md-6">
 
							<div class="form-group">
<label class="col-md-4 Control-label"
									style="font-weight: normal;">Received Date</label>
<div class="col-md-6 input-group input-group-md">
<scf:textfield name="statusvo.odcUpDate" id="odcUpDate"
										cssClass="datepicker form-control text_box" />
</div>
</div>
</div>						
<br>
</div>
<br>
</div>
</scf:form>
<div class="row page_content">
<div class="col-md-12">
<div class="col-md-12">
<div class="page_collapsible" id="body-section1">
<span></span>
<h5 style="font-weight: bold; font-size: 11px;">&nbsp;Batches to Approve</h5>
</div>
<div class="col-md-12">
 
							<div class="table">
 
								<table style="text-align: left;" id="fd">
<tbody>
<tr>
<th style="text-align: center; width: 200px;"><label>BatchId</label></th>
<th style="text-align: center; width: 200px;"><label>Upload Date</label></th>
<th style="text-align: center; width: 600px;"><label>Remarks</label></th>											
</tr>
<scf:iterator value="invoiceDetailsList" status="list">
<tr class="eventrefresh" id=<scf:property value="batchId"/>
												onClick="selectlist(this.id)">
<td class="left-spacing-val"><scf:property	value="batchId" /></td>
<td class="left-spacing-val"><scf:property value="odcUpDate" /></td>
<td class="left-spacing-val"><scf:property value="remarks" /></td>
<scf:hidden name="statusvo.invoiceDetailsList[%{#list.index}].batchId"></scf:hidden>
<scf:hidden name="statusvo.invoiceDetailsList[%{#list.index}].odcUpDate"></scf:hidden>
<scf:hidden name="statusvo.invoiceDetailsList[%{#list.index}].remarks"></scf:hidden>
<scf:hidden name="statusvo.invoiceDetailsList[%{#list.index}].batchId" id="batchId" 
												 	cssClass="form-control text_box" readonly="true" />
 
											</tr>
</scf:iterator>
</tbody>
</table>
 
							</div>
 
						</div>
</div>
 
				</div>
 
			</div>
			<!-- invoice details -->
<scf:form method="post" id="myForm">
<div class="row_page_content">
<div class="col-md-12">
<input type="hidden" name="batchId" id="batchId_val" />
<scf:hidden name="statusvo.batchId"  />
<input type="hidden" name="statusFlag" id="statusFlag" />
<div class="page_collapsible" id="body-section1">
<span></span>
<h5 style="font-weight: bold; font-size: 13px;">&nbsp;Export Collection Details</h5>
</div>
<div class="col-md-12">
<div class="col-md-12">
<div class="row page_content">
<scf:if test="%{invoiceAjaxDetails.size()>0}">
<a onclick="exexcel()" style="margin-left: 92%;" id="excl"
											title="Export to Excel"><img src="images/excel-icon.jpg"
											alt="Export to Excel" width="30" height="30"></a>
</scf:if>
<div class="table">
<table style="text-align: left;" id="invoiceList">
<tbody>
<tr>
<th class="grid_heading"><label>Document Release</label></th>
<th class="grid_heading_acno"><label>Product Type</label></th>
<th class="grid_alt_heading"><label>Received From Ref</label></th>
<th class="grid_alt_heading"><label>Received On</label></th>
<th class="grid_alt_heading"><label>Collection Amount</label></th>
<th class="grid_heading"><label>Finance Requested</label></th>
<th class="grid_heading"><label>Received From</label></th>
<th class="grid_alt_heading"><label>Send To</label></th>
<th class="grid_alt_heading"><label>Drawee</label></th>
<th class="grid_heading"><label>Tenor Period</label></th>
<th class="grid_heading"><label>Base Date</label></th>
<th class="grid_heading1"><label>Has Attached Doc</label></th>
<th class="grid_heading1"><label>Document Code</label></th>
<th class="grid_heading1"><label>First Mail</label></th>
<th class="grid_heading1"><label>Second Mail</label></th>
<th class="grid_heading1"><label>Total</label></th>
<th class="grid_heading1"><label>No of Invoice</label></th>
<th class="grid_heading1"><label>Invoice Serial No</label></th>
<th class="grid_heading1"><label>Invoice Date</label></th>
<th class="grid_heading1"><label>Invoice_Amt_Ccy</label></th>
<th class="grid_heading1"><label>Discounted_Amt_Ccy</label></th>
<th class="grid_heading1"><label>Deduction_Amt_Ccy</label></th>
<th class="grid_heading1"><label>Finance Ref No</label></th>
<th class="grid_heading1"><label>Product Type2</label></th>
<th class="grid_heading1"><label>Period</label></th>
<th class="grid_heading1"><label>Interest in Advance</label></th>
<th class="grid_heading1"><label>Interest in Arrears</label></th>
<th class="grid_heading1"><label>Base Rate</label></th>
<th class="grid_heading1"><label>Spread Rate</label></th>
</tr>
<scf:if test="%{invoiceAjaxDetails.size()>0}">
<scf:iterator value="invoiceAjaxDetails" status="statusval">
<tr class="eventrefresh1" <scf:property value="setColor"/>
														  id='invoiceAjaxDetails_<scf:property value="%{#status.count}"/>' >
<td class="left-spacing-val"><scf:property value="documentRelease" /></td>
<td class="left-spacing-val"><scf:property value="productType" /></td>
<td class="left-spacing-val"><scf:property value="receviedFromRef" /></td>
<td class="left-spacing-val"><scf:property value="receviedOn" /></td>
<td class="left-spacing-val"><scf:property value="collectionAmount" /></td>
<td class="left-spacing-val"><scf:property value="financeRequested" /></td>
<td class="left-spacing-val"><scf:property value="receviedFrom" /></td>
<td class="left-spacing-val" align="right" style="padding-right:3px;"><scf:property value="sendTo" /></td>
<td class="left-spacing-val" align="right" style="padding-right:3px;"><scf:property value="drawee" /></td>
<td class="left-spacing-val"><scf:property value="tenorPeriod" /></td>
<td class="left-spacing-val"><scf:property value="baseDate" />
<td class="left-spacing-val"><scf:property value="hasAttachedDoc" /></td>														
<td class="left-spacing-val"><scf:property value="documentCode" /></td>
<td class="left-spacing-val"><scf:property value="firstMail" /></td>
<td class="left-spacing-val"><scf:property value="secondMail" /></td>
<td class="left-spacing-val"><scf:property value="total" /></td>
<td class="left-spacing-val"><scf:property value="noInvoice" /></td>
<td class="left-spacing-val"><scf:property value="invoiceSerialNo" /></td>
<td class="left-spacing-val"><scf:property value="invoiceDate" /></td>
<td class="left-spacing-val"><scf:property value="invoiceAmtCcy" /></td>
<td class="left-spacing-val"><scf:property value="discountedAmtCcy" /></td>
<td class="left-spacing-val"><scf:property value="deductionAmtCcy" /></td>
<td class="left-spacing-val"><scf:property value="financeRefNo" /></td>
<td class="left-spacing-val"><scf:property value="productType2" /></td>
<td class="left-spacing-val"><scf:property value="period" /></td>
<td class="left-spacing-val"><scf:property value="interestAdvance" /></td>
<td class="left-spacing-val"><scf:property value="interestArrears" /></td>
<td class="left-spacing-val"><scf:property value="baseRate" /></td>
<td class="left-spacing-val"><scf:property value="spreadRate" /></td>	
</tr>
</scf:iterator>
</scf:if>
</tbody>
</table>
</div>
 
								</div>
</div>
<br>
<br>
 
						</div>
</div>
</div>
</scf:form>
</div>
</div>
</div>
<jsp:include page="common/footer.jsp" />
<script type="text/javascript">
$('.datepicker').datepicker({
	format : 'dd-mm-yyyy',
	autoclose : true
});
</script>