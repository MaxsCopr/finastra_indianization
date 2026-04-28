<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="scf" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<jsp:include page="common/header.jsp" />
<script type="text/javascript" src="js/bulkUpload_Maker.js"></script>
<div class="row">
<div class="col-md-2">
<div class="side_nav" style="width: 200px;">
<ul class="nav nav-pills nav-stacked">
<li style="text-align: center;"><a href="close">Close</a></li>
<li style="text-align: center;"><a href="maker">Reset</a></li>
</ul>
</div>
</div>
 
 
	<div class="col-md-10 content_box">
 
		<h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Export
			Collection Bulk Upload</h5>
 
		<div id="userIdMessage" style="color: orange"></div>
<div class="row cont_colaps">
<div class="col-md-12">
<div class="page_collapsible " id="body-section1">
<span></span>
<h5 style="font-weight: bold; font-size: 13px;">&nbsp;Excel Input
</h5>
</div>
 
				<scf:form method="post" 	enctype="multipart/form-data" id="bulkUpload" name="bulk_import">
 
					<div class="row page_content">
<%-- <div class="col-md-6">
<div align="center" id="div1"></div>
<div class="form-group">
<label class="col-md-4 Control-label"
									style="font-weight: normal;">Input Branch <span
									style="color: #579EDC; cursor: pointer;"
									onclick="buyerBranchSelection()">?</span></label>
<div class="col-md-5 input-group input-group-md">
<scf:textfield id="branchcod" name="makervo.branchCode"
										cssClass="form-control text_box" readonly="true" />
</div>
</div>
<div class="form-group" >
<label class="col-md-4 Control-label" style="font-weight: normal;">Remarks</label>									
<div class="col-md-5 input-group input-group-md">								
<scf:textarea name="makervo.remarks" id="remarks"
											cssClass="form-control textarea" maxlength="200" /><br /> <br />
</div>
</div> 		
</div> --%>
 
						<div class="col-md-6">
<div class="form-group">
<label class="col-md-4 Control-label"
									style="font-weight: normal;">Upload File</label>
<div class="col-md-5 input-group input-group-md">
<input type="file" name="inputFile" style="height: 25px;"
										accept="application/vnd.ms-excel,text/plain,csv,.xlsx" />
</div>
</div>
<br />
 
							<div class="form-group">
<label class="col-md-4 Control-label"
									style="font-weight: normal;"></label>
<div class="col-md-5 input-group input-group-md">
<scf:submit action="loadInvoice" name="load" style="height: 100%;" cssClass="button" value="Load" /> &nbsp;&nbsp;
<scf:submit onclick="excelValidation()" name="validate" style="height: 100%;" cssClass="button" value="Validate" />
</div>
<%-- <div class="col-md-6 input-group input-group-md">
<scf:submit onclick="excelValidation()" name="validate"
										style="height: 25px;" cssClass="button" value="Validate" />
</div> --%>
</div>
 
							<%-- <div class="form-group">
<label class="col-md-4 Control-label"
									style="font-weight: normal;"></label>
<div class="col-md-5 input-group input-group-md">
<scf:submit onclick="excelValidation()" name="validate"
										style="height: 25px;" cssClass="button" value="Validate" />
</div>
</div> --%>
<scf:hidden id="batchID" name="makervo.batchId"
									cssClass="form-control text_box"  />
</div>
</div>
</scf:form>
<br />
<div class="col-md-12">
<div class="row page_content">
<div class="page_collapsible" id="body-section1">
<span></span>
<h5 style="font-weight: bold; font-size: 13px;">&nbsp;Export
								Collection Bulk Upload</h5>
</div>
</div>
</div>
<scf:form method="post" id="myForm">
<div class="col-md-6">
<div class="form-group">
<label class="col-md-4 Control-label"
								style="font-weight: normal;">Batch Id </label>
<div class="col-md-5 input-group input-group-md">
<scf:textfield id="batchID" name="makervo.batchId"
									cssClass="form-control text_box" />
</div>
</div>
</div>
<%-- <div class="col-md-6">
<div class="form-group">
<label class="col-md-4 Control-label"
								style="font-weight: normal;">REMP ERROR </label>
<div class="col-md-5 input-group input-group-md" >
<scf:textfield id="statusID" name="makervo.tempStatus"
									cssClass="form-control text_box"  />
</div>
</div>
</div>	 --%>
<div class="col-md-12">
<div class="col-md-12">
<div class="row page_content">
<div class="form-group">
<a onclick="exexcel()" style="margin-left: 92%;" id="excl"
										title="Export to Excel"><img src="images/excel-icon.jpg"
										alt="Export to Excel" width="30" height="30"></a>
<div style="height: 20px;"></div>
<div class="table">
<table id="invoiceList">
<tbody>
<tr>
<th class="grid_heading"><label>Their ReferenceNo</label></th>
<th class="grid_heading"><label>Collecting Bank</label></th>
<th class="grid_heading"><label>Behalf of Branch</label></th>
<th class="grid_heading"><label>Drawer</label></th>
<th class="grid_heading"><label>DraweeCustomerID</label></th>
<th class="grid_heading"><label>Drawee Name </label></th>
<th class="grid_heading"><label>Drawee Country</label></th>
<th class="grid_heading"><label>Charge Debit Account Number</label></th>

<th class="grid_heading"><label>CollectionAmount</label></th>
<th class="grid_heading"><label>CollectionCurrency</label></th>
<th class="grid_heading"><label>Shipping Bill No</label></th>
<th class="grid_heading"><label>Shipping Bill Date</label></th>
<th class="grid_heading"><label>Port Code</label></th>
<th class="grid_heading"><label>Form Type</label></th>
<th class="grid_heading"><label>Shipping Bill Amount</label></th>
<th class="grid_heading"><label>Shipping Bill Currency</label></th>
<th class="grid_heading"><label>Short Collection Amount</label></th>
<th class="grid_heading"><label>Remittance Number</label></th>
<th class="grid_heading"><label>Remittance Amount</label></th>
<th class="grid_heading"><label>Shipment From Country</label></th>
<th class="grid_heading"><label>Shipment To Country</label></th>
<th class="grid_heading"><label>Harmonised code</label></th>
<th class="grid_heading"><label>Incoterms</label></th>
<th class="grid_heading"><label>GoodsDescription</label></th>
<th class="grid_heading"><label>GoodsCode</label></th>
<th class="grid_heading"><label>Port of Destination</label></th>
<th class="grid_heading"><label>Invoice Number</label></th>
<th class="grid_heading"><label>Invoice Date</label></th>
<th class="grid_heading"><label>Port of	Loading</label></th>
<th class="grid_heading"><label>Transport Document Number</label></th>
<th class="grid_heading"><label>Transport Document Date</label></th>
<th class="grid_heading"><label>Error Details</label></th>
<th class="grid_heading"><label> Status</label></th>

 
 
 
 
												</tr>
<%-- <scf:hidden id="batchID" name="makervo.batchId" /> --%>
<scf:hidden name="statusFlag" id="statusFlag" />
<scf:iterator value="invoiceList" status="list">
<tr class="eventrefresh"
														id='invoicelist_<scf:property value="%{#status.count}"/>'
														onClick="selectlist(this)"
														ondblclick="onselectlist(this);">
<td class="left-spacing-val"> <scf:property value="theirref" /></td>
<td class="left-spacing-val"> <scf:property value="collecting_bank" /></td>														
<td class="left-spacing-val"> <scf:property value="behalfofbranch" /></td>														
<td class="left-spacing-val"> <scf:property value="drawer" /></td>														
<td class="left-spacing-val"> <scf:property value="draweecustomerid" /></td>
<td class="left-spacing-val"> <scf:property value="drawee_address" /></td>
<td class="left-spacing-val"> <scf:property value="drawee_name" /></td>
<td class="left-spacing-val"> <scf:property value="chargeAccount" /></td>
<td class="left-spacing-val"> <scf:property value="collectionamount" /></td>														
<td class="left-spacing-val"> <scf:property value="collectioncurrency" /></td>														
<td class="left-spacing-val"> <scf:property value="shpbill_no" /></td>														
<td class="left-spacing-val"> <scf:property value="bill_date" /></td>														
<td class="left-spacing-val"> <scf:property value="port_code" /></td>														
<td class="left-spacing-val"> <scf:property value="form_type" /></td>														
<td class="left-spacing-val"> <scf:property value="shp_amt" /></td>														
<td class="left-spacing-val"> <scf:property value="shp_currency" /></td>														
<td class="left-spacing-val"> <scf:property value="shortcollectionamount" /></td>												
<td class="left-spacing-val"> <scf:property value="remittance_num" /></td>													
<td class="left-spacing-val"> <scf:property value="utilization_amount" /></td>												
<td class="left-spacing-val"> <scf:property value="shipmentfromcountry" /></td>														
<td class="left-spacing-val"> <scf:property value="shipmenttocountry" /></td>														
<td class="left-spacing-val"> <scf:property value="hscode" /></td>														
<td class="left-spacing-val"> <scf:property value="incoterms" /></td>														
<td class="left-spacing-val"> <scf:property value="gooddescription" /></td>														
<td class="left-spacing-val"> <scf:property value="goodscode" /></td>														
<td class="left-spacing-val"> <scf:property value="portofdestination" /></td>														
<td class="left-spacing-val"> <scf:property value="invoiceno" /></td>														
<td class="left-spacing-val"> <scf:property value="invoicedate" /></td>														
<td class="left-spacing-val"> <scf:property value="portofloading" /></td>														
<td class="left-spacing-val"> <scf:property value="transportdocno" /></td>														
<td class="left-spacing-val"> <scf:property value="transportdocdate" /></td>
<td class="left-spacing-val"><scf:property value="errordtls" /></td>
<%-- <td class="left-spacing-val"> <scf:property value="errordescription" /></td> --%>
<td class="left-spacing-val"> <scf:property value="status" /></td>
</tr>
</scf:iterator>
</tbody>
</table>
</div>
</div>
</div>
</div>
</div>
<div class="col-md-12" style="width: 100%" align="center">
<br />
<scf:if test="%{makervo.state=='Success'}">
 
							<input type="button" value="Upload to TI" class="button"
								onclick="uploadAll()" />
&nbsp;&nbsp;
</scf:if>
 
						<!-- <!--  <input type="button" value="Upload All" class="button" onclick="uploadAll()" /> -->
<!-- &nbsp;&nbsp; <input type="button" value="Reject All"	class="button" onclick="rejectAll()" /> --> 
</div>
<scf:token/>
</scf:form>
</div>
</div>
</div>
</div>
 
<script type="text/javascript">
	var batid = $('#batchID').val();
	if(batid != ""){
		$("#excl").show();
	} else {
		$("#excl").hide();
	} 
</script>
<jsp:include page="common/footer.jsp" />