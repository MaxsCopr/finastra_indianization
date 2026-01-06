<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="scf" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<jsp:include page="common/header.jsp" />
<link rel="stylesheet" href="css/jquery.dataTables.min.css"></link>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>		
<script type="text/javascript" src="js/bulkUpload_Checker.js"></script>		
 
 
<div class="row">
<div class="col-md-2">
<div class="side_nav" style="width: 200px;">
<ul class="nav nav-pills nav-stacked">
<!-- <li style="text-align: center;"><a href="https://encore:9469/tiplus2-global">Close</a></li> -->
<!-- <li style="text-align: center;"><a href="https://tradeuatapp106:8003/devglobal">Close</a></li>   -->
<!-- <li style="text-align: center;"><a href="https://tradeapp130:8003/idfc-global">Close</a></li> -->
<!-- <li style="text-align: center;"><a href="https://tradeuatapp107:8003/uatglobal">Close</a></li> -->
<li style="text-align: center;"><a href="close">Close</a></li>
<%-- <li style="text-align: center;"><a href="<scf:property value="%{#session.closeURL}" />">Close</a></li> --%>
<li style="text-align: center;"><a href="checker" >Reset</a></li>
<!-- <li style="text-align: center;"><a href="https://tipluspreprod.idfcbank.com/preprod-global">Close</a></li> -->
<!-- <li style="text-align: center;"><a href="https://tiplusprod.idfcbank.com/liveglobal">Close</a></li> -->
</ul>
</div>
</div>
 
	<div class="col-md-10 content_box">
 
		<h5 class="row fontcol spac">Export Collection Bulk Upload -
			Checker</h5>
 
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
<scf:textfield name="checkervo.batchId" id="batchId"
										cssClass="form-control text_box" />
</div>
</div>		
<div class="form-group">
<label class="col-md-4 Control-label"
									style="font-weight: normal;"></label>
<div class="col-md-6 input-group input-group-md">
<scf:submit name="validate"
									   onclick="getDataFromView1()" cssClass="button" value="Search" />
</div>
</div>					
 
						</div>
<div class="col-md-6">
 
							<div class="form-group">
<label class="col-md-4 Control-label"
									style="font-weight: normal;">Received Date</label>
<div class="col-md-6 input-group input-group-md">
<scf:textfield name="checkervo.odcUpDate" id="odcUpDate"
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
												ondblClick="selectlist(this.id)">
<td class="left-spacing-val"><scf:property	value="batchId" /></td>
<td class="left-spacing-val"><scf:property value="odcUpDate" /></td>
<td class="left-spacing-val"><scf:property value="remarks" /></td>
<scf:hidden name="checkervo.invoiceDetailsList[%{#list.index}].batchId"></scf:hidden>
<scf:hidden name="checkervo.invoiceDetailsList[%{#list.index}].odcUpDate"></scf:hidden>
<scf:hidden name="checkervo.invoiceDetailsList[%{#list.index}].remarks"></scf:hidden>
<scf:hidden name="checkervo.invoiceDetailsList[%{#list.index}].batchId" id="batchId" 
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
			<scf:hidden name="checkervo.batchId"  id="batchId_val" />
			<input type="hidden" name="statusFlag" id="statusFlag" />
			<div class="page_collapsible" id="body-section1">
			<span></span>
			<h5 style="font-weight: bold; font-size: 13px;">&nbsp;Export Collection Details</h5>
			</div>
			<div class="col-md-12">
			<div class="col-md-12">
			<div class="row page_content">								
			<a onclick="exexcel()" style="margin-left: 92%;" id="excl"
														title="Export to Excel"><img src="images/excel-icon.jpg"
														alt="Export to Excel" width="30" height="30"></a>
			
			<table style="text-align: left;" id="invoiceList">
			<tbody>
			<tr>
			<th class="grid_heading"><label>Their ReferenceNo</label>
			<th class="grid_heading"><label>Behalf of Branch</label>
			<th class="grid_heading"><label>Drawer</label>
			<th class="grid_heading"><label>DraweeCustomerID</label>
			<th class="grid_heading"><label>DraweeCustomerName</label>
			<th class="grid_heading"><label>CollectionAmount</label>
			<th class="grid_heading"><label>CollectionCurrency</label>
			<th class="grid_heading"><label>Shipping Bill No</label>
			<th class="grid_heading"><label>Shipping Bill Date</label>
			<th class="grid_heading"><label>Port Code</label>
			<th class="grid_heading"><label>Form Type</label>
			<th class="grid_heading"><label>Shipping Bill Amount</label>
			<th class="grid_heading"><label>Shipping Bill Currency</label>
			<th class="grid_heading"><label>Short Collection Amount</label>
			<th class="grid_heading"><label>Short Collection Currency</label>
			<th class="grid_heading"><label>Remittance Number</label>
			<th class="grid_heading"><label>Remittance Amount</label>
			<th class="grid_heading"><label>Remittance Currency</label>
			<th class="grid_heading"><label>Shipment From Country</label>
			<th class="grid_heading"><label>Shipment To Country</label>
			<th class="grid_heading"><label>Harmonised code</label>
			<th class="grid_heading"><label>Incoterms</label>
			<th class="grid_heading"><label>RBI Purpose Code</label>
			<th class="grid_heading"><label>GoodsDescription</label>
			<th class="grid_heading"><label>GoodsCode</label>
			<th class="grid_heading"><label>Port of Destination</label>
			<th class="grid_heading"><label>Invoice Number</label>
			<th class="grid_heading"><label>Invoice Date</label>
			<th class="grid_heading"><label>Port of	Loading</label>
			<th class="grid_heading"><label>Transport Document Number</label>
			<th class="grid_heading"><label>Transport Document Date</label>
			 
															</tr>
			<scf:if test="%{invoiceAjaxDetails.size()>0}">
			<scf:iterator value="invoiceAjaxDetails" status="statusval">
			<tr class="eventrefresh1" <scf:property value="setColor"/>
																	  id='invoiceAjaxDetails_<scf:property value="%{#status.count}"/>' >
			<td class="left-spacing-val"> <scf:property value="theirref" /></td>														
			<td class="left-spacing-val"> <scf:property value="behalfofbranch" /></td>														
			<td class="left-spacing-val"> <scf:property value="drawer" /></td>														
			<td class="left-spacing-val"> <scf:property value="draweecustomerid" /></td>														
			<td class="left-spacing-val"> <scf:property value="draweecustomername" /></td>														
			<td class="left-spacing-val"> <scf:property value="collectionamount" /></td>														
			<td class="left-spacing-val"> <scf:property value="collectioncurrency" /></td>														
			<td class="left-spacing-val"> <scf:property value="shpbill_no" /></td>														
			<td class="left-spacing-val"> <scf:property value="bill_date" /></td>														
			<td class="left-spacing-val"> <scf:property value="port_code" /></td>														
			<td class="left-spacing-val"> <scf:property value="form_no" /></td>														
			<td class="left-spacing-val"> <scf:property value="shp_amt" /></td>														
			<td class="left-spacing-val"> <scf:property value="shp_currency" /></td>														
			<td class="left-spacing-val"> <scf:property value="shortcollection_amt" /></td>														
			<td class="left-spacing-val"> <scf:property value="short_coll_currency" /></td>														
			<td class="left-spacing-val"> <scf:property value="remittance_num" /></td>														
			<td class="left-spacing-val"> <scf:property value="utilization_amount" /></td>														
			<td class="left-spacing-val"> <scf:property value="utilization_currency" /></td>														
			<td class="left-spacing-val"> <scf:property value="shipmentfromcountry" /></td>														
			<td class="left-spacing-val"> <scf:property value="shipmentto" /></td>														
			<td class="left-spacing-val"> <scf:property value="hscode" /></td>														
			<td class="left-spacing-val"> <scf:property value="incoterms" /></td>														
			<td class="left-spacing-val"> <scf:property value="rbi_purposecode" /></td>														
			<td class="left-spacing-val"> <scf:property value="gooddescription" /></td>														
			<td class="left-spacing-val"> <scf:property value="goodscode" /></td>														
			<td class="left-spacing-val"> <scf:property value="portofdestination" /></td>														
			<td class="left-spacing-val"> <scf:property value="invoiceno" /></td>														
			<td class="left-spacing-val"> <scf:property value="invoicedate" /></td>														
			<td class="left-spacing-val"> <scf:property value="iportofloading" /></td>														
			<td class="left-spacing-val"> <scf:property value="transportdocno" /></td>														
			<td class="left-spacing-val"> <scf:property value="transportdocdate" /></td>	
			</tr>
			</scf:iterator>
			</scf:if>
			</tbody>
			</table>
			</div> 
			<scf:form>
			<table id="checker_list_table"  class="table table-striped table-bordered">
			<thead>
			<tr> <th ><input name="select_all" value= "1" id="select_all" type="checkbox"></th> 
			<th><scf:text name="Document Release" /></th>
			<th><scf:text name="Product Type" /></th>
			<th><scf:text name="Recevied From Ref" /></th>
			<th><scf:text name="Recevied On" /></th>	
			</tr>
			</thead>
			<tbody>
			<tr>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			</tr>
			</tbody>
			</table>
			</scf:form>
			 
								</div><br/>
			</div>
			<div class="col-md-12" style="width: 100%;" align="center">								
			
			<!-- <input type="button" value="Upload With Warnings" id="upload_war"  class="button" onclick="validate()"  /> -->
			&nbsp;&nbsp;
			<input type="button" value="Upload All Successful" id="upload_al" class="button" onclick="UploadAll()" /> 
			&nbsp;&nbsp;
			<input type="button" value="Reject All" id="reject_al" class="button" onclick="RejectAll()" />
			
			 
										</div>
			</div>
			</div>
			</div>
			</scf:form>
			</div><br/><br/><br/>
			</div>
			</div>
			<jsp:include page="common/footer.jsp" />
			<script type="text/javascript">
			$('.datepicker').datepicker({
				format : 'dd-mm-yyyy',
				autoclose : true
			});
			</script>