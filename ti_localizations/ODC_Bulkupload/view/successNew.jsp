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
<!-- <li style="text-align: center;"><a href="https://misyssitwebappmig1:8883/tiplus2-global/login">Close</a></li> -->
<!-- <li style="text-align: center;"><a href="https://tradeuatapp106:8003/devglobal">Close</a></li> -->
<!-- <li style="text-align: center;"><a href="https://tradeapp130:8003/idfc-global">Close</a></li> -->
<!-- <li style="text-align: center;"><a href="https://tradeuatapp107:8003/uatglobal">Close</a></li> -->     
<%-- <li style="text-align: center;"><a href="<scf:property value="%{#session.closeURL}" />">Close</a></li> --%>
<li style="text-align: center;"><a href="maker" >Reset</a></li>				
<!-- <li style="text-align: center;"><a href="https://tipluspreprod.idfcbank.com/preprod-global">Close</a></li> -->
<!-- <li style="text-align: center;"><a href="https://tiplusprod.idfcbank.com/liveglobal">Close</a></li> -->
</ul>
</div>
</div>
 
		<div class="col-md-10 content_box">
<h5 class="row fontcol spac">Successfully Uploaded</h5>
<!-- <h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Export Collection Bulk Upload</h5> -->			  					
 
			<div id="userIdMessage" style="color: orange"></div>
<div class="row cont_colaps">			
<div class="col-md-12">
<div class="page_collapsible " id="body-section1">
<span></span>
<h5 style="font-weight: bold; font-size: 13px;">&nbsp;Input Details</h5>
</div>
<scf:form method="post" action="bulk-import" enctype="multipart/form-data" 
						id="bulkUpload" name="bulk_import">
<div class="row page_content">
<div class="col-md-6">		
<div align="center" id="div1">				
</div>					
<div class="form-group">								
<label class="col-md-4 Control-label"
										style="font-weight: normal;">Input Branch <span
										style="color: #579EDC; cursor: pointer;" onclick="buyerBranchSelection()">?</span></label>
<div class="col-md-5 input-group input-group-md">
<scf:textfield id="branchcod" name="makervo.branchCode"
											cssClass="form-control text_box" readonly = "true"/>
</div>
</div>
<%-- <div class="form-group" >
<label class="col-md-4 Control-label" style="font-weight: normal;">Remarks</label>									
<div class="col-md-5 input-group input-group-md">								
<scf:textarea name="makervo.remarks" id="remarks"
											cssClass="form-control textarea" maxlength="200" /><br /> <br />
</div>
</div> 		 --%>				
</div>	
<div class="col-md-6">
<div class="form-group">
<label class="col-md-4 Control-label"
										style="font-weight: normal;">Upload File</label>
<div class="col-md-5 input-group input-group-md">
<input type="file" name="inputFile" 
									     style="height: 25px;" accept="application/vnd.ms-excel,text/plain,csv,.xlsx" />										
</div>
</div><br/>

<div class="form-group">	
<label class="col-md-4 Control-label"
										style="font-weight: normal;"></label>					
<div class="col-md-5 input-group input-group-md" >
<scf:submit action="listofinvoice"  name="validate"
											style="height: 25px;" cssClass="button" value="Validate"  />
</div>
</div>
</div>	
</div>			
</scf:form><br/>
<div class="col-md-12">
<div class="row page_content">
<div class="page_collapsible" id="body-section1">
<span></span>
<h5 style="font-weight: bold; font-size: 13px;">&nbsp;Export Collection Bulk Upload</h5>
</div>
</div>
</div>
<scf:form method="post" id="myForm">
<div class="col-md-6">
<div class="form-group">
<label class="col-md-4 Control-label"
								style="font-weight: normal;">Batch Id </label>
<div class="col-md-5 input-group input-group-md" >
<scf:textfield id="batchID" name="makervo.batchId"
									cssClass="form-control text_box"  />
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
<a onclick="exexcel()" style="margin-left:92%;" id="excl" title="Export to Excel"><img
									src="images/excel-icon.jpg" alt="Export to Excel" width="30" height="30"></a> 
<div style="height: 20px;"></div> 
<div class="table">
<table id="invoiceList">
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
<scf:hidden id="batchID" name="makervo.batchID" />
<scf:hidden name="statusFlag" id="statusFlag" />
<scf:iterator value="invoiceList" status="list">
<tr class="eventrefresh"
														id='invoicelist_<scf:property value="%{#status.count}"/>'
														onClick="selectlist(this)"
														ondblclick="onselectlist(this);">
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
</tbody>
</table>
</div>
</div>
</div>
</div>
</div>
<div class="col-md-12" style="width: 100%" align="center">
<br/>
<%-- 	<scf:if test="%{makervo.state=='Success'}"> --%>
<input type="button" value="Upload All" class="button" onclick="uploadAll()" />
&nbsp;&nbsp;
<%-- </scf:if> --%>
<input type="button" value="Upload Success" class="button" onclick="uploadAll()" />
<input type="button" value="Reject All" class="button" onclick="rejectAll()" /> 
</div>
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