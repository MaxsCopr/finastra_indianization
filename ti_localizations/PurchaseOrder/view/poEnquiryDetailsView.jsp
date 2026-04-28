<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="gr" uri="/struts-tags"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Home Page</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<link rel="stylesheet" href="css/jquery-ui.css" />
<link rel="stylesheet" href="css/style.css" />
<link rel="stylesheet" href="css/datepicker.css" />
<link type="text/css" rel="stylesheet" href="css/bootstrap.css" />
<link type="text/css" rel="stylesheet" href="css/bootstrap-dropdown.css" />
<link type="text/css" rel="stylesheet" href="css/headfoot.css" />
<link href="css/font-awesome.css" rel="stylesheet" />
<link rel="stylesheet" href="css/commonTiplus.css" />
<script src="js/jquery-1.9.1.js"></script>
<script src="js/jquery-ui.js"></script>
<script src="js/bootstrap.js" type="text/javascript"></script>
<script src="js/bootstrap-datepicker.js" type="text/javascript"></script>
<script type="text/javascript" src="js/macxdnie81.js"></script>
<script type="text/javascript" src="js/maxcdnie82.js"></script>
<script type="text/javascript" src="js/jquery.cookie.min.js"></script>
<script type="text/javascript" src="js/jquery.collapsible.min.js"></script>
<script type="text/javascript" src="js/highlight.pack.js"></script>

<script type="text/javascript" src="js/date_search.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/commonTiplus.js"></script>
<script type="text/javascript" src="js/chargeSchedule.js"></script>
<script>
      $(document).ready(function() {

      });
</script>
</head>

<body class="body_bg" onload="display_ct()">
      <%-- <%@ include file="/view/includes/TITLE.jsp"%> --%>
      <img src="images/FusionBanking.png" width="100%" />
      <gr:form method="post" id="myForm" name="icForm">
            <div class="row">
                  <div class="col-md-2">
                        <div class="side_nav" style="width: 215px;">
                              <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;"><a href="enquiryProcess">Close</a></li>
                              </ul>
                        </div>
                        <br /> <br />
                        <div class="side_nav" style="width: 215px;">
                              <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;"><a href="#"
                                          onclick="gotoPurchaseOrderScreen()">Edit Purchase Order</a></li>
                              </ul>
                        </div>
                  </div>

                  <div class="col-md-10 content_box">
                        <div class="row page_content">

                              <c:if test="${not empty msg}">
                                    <h5 style="font-weight: lighter; font-size: 13px; color: #FF0000;">
                                          <gr:property value="msg" />
                                    </h5>
                              </c:if>
                        </div>
                        <br />

                        <div id="userIdMessage" style="color: orange"></div>

                        <br />
                        <div class="row cont_colaps">
                              <div class="col-md-12">
                                    <div class="page_collapsible" id="body-section1">
                                          <span></span>
                                          <h5 style="font-weight: bold; font-size: 13px; color: #527BB8;">&nbsp;Purchase
                                                Order Details</h5>
                                    </div>
                                    <div class="row page_content">
                                          <div class="col-md-12">

                                                <div class="col-md-6">

                                                      <div class="form-group required">
                                                            <label class="col-md-6 Control-label"
                                                                  style="font-weight: normal;">Export Order/PO Number/LC Number
                                                                  &nbsp;&nbsp;</label>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="amountFrom"
                                                                        name="chargeVO.exportOrderNumber"
                                                                        cssClass="form-control text_box" readonly="true" />

                                                            </div>
                                                      </div>
                                                      <div class="form-group">
                                                            <label class="col-md-6 Control-label"
                                                                  style="font-weight: normal;">CIF ID&nbsp; </label>
                                                            <div class="col-md-4 input-group input-group-md">
                                                                  <gr:textfield id="transRefNo" name="chargeVO.cifID"
                                                                        readonly="true" cssClass="form-control text_box">
                                                                  </gr:textfield>
                                                            </div>
                                                      </div>

                                                      <div class="form-group">
                                                            <label class="col-md-6 Control-label"
                                                                  style="font-weight: normal;">Inco Terms&nbsp; </label>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="amountTo" name="chargeVO.incoTerms"
                                                                        cssClass="form-control text_box" readonly="true" />
                                                            </div>
                                                      </div>

                                                      <div class="form-group">
                                                            <label class="col-md-6 Control-label"
                                                                  style="font-weight: normal;"> PO Value</label>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="povalue" placeholder="Eg:100 USD"
                                                                        name="chargeVO.poValue" cssClass="form-control text_box"
                                                                        readonly="true" />
                                                            </div>
                                                      </div>

                                                      <div class="form-group">
                                                            <label class="col-md-6 Control-label"
                                                                  style="font-weight: normal;">Expiry Date &nbsp;&nbsp;</label>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="datepicker2"
                                                                        name="chargeVO.exportexpiryDate"
                                                                        cssClass="form-control text_box" style="text-align: left;"
                                                                        readonly="true" />

                                                            </div>
                                                      </div>

                                                      <div class="form-group">
                                                            <label class="col-md-6 Control-label"
                                                                  style="font-weight: normal;">% Freight Deduction</label>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="freightdeduction"
                                                                        name="chargeVO.freightDeduction"
                                                                        cssClass="form-control text_box" readonly="true" />
                                                            </div>
                                                      </div>

                                                      <div class="form-group">
                                                            <label class="col-md-6 Control-label"
                                                                  style="font-weight: normal;">Margin % &nbsp;&nbsp;</label>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="margin" name="chargeVO.marginPercentage"
                                                                        readonly="true" cssClass="form-control text_box" />
                                                            </div>
                                                      </div>
                                                      <div class="form-group">
                                                            <label class="col-md-6 Control-label"
                                                                  style="font-weight: normal;">Goods Code &nbsp;&nbsp;</label>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="amountTo" name="chargeVO.goodsCode"
                                                                        cssClass="form-control text_box" readonly="true" />
                                                            </div>
                                                      </div>
                                                      <div class="form-group">
                                                            <label class="col-md-6 Control-label"
                                                                  style="font-weight: normal;">Status &nbsp;&nbsp;</label>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="status" name="chargeVO.status"
                                                                        readonly="true" cssClass="form-control text_box" />
                                                            </div>
                                                      </div>
                                                      <div class="form-group">
                                                            <div class="col-md-6 Control-label">
                                                                  <label style="font-weight: normal;">Normal Transit Period &nbsp;&nbsp;</span></label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="ntPeriod" name="chargeVO.ntPeriod" value="21"
                                                                  cssClass="form-control text_box" readonly="true"/>
                                                            </div>
                                                      </div>
                                                      <div class="form-group">
                                                            <div class="col-md-6 Control-label">
                                                                  <label style="font-weight: normal;">Due Date &nbsp;&nbsp;</span></label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="dueDate" name="chargeVO.dueDate" readonly="true"
                                                                  cssClass="form-control text_box" />
                                                            </div>
                                                      </div>
                                                </div>

                                                <div class="col-md-6">
                                                      <div class="form-group">
                                                            <label class="col-md-6 Control-label"
                                                                  style="font-weight: normal;">Export Order/PO date/LC Date
                                                                  &nbsp;&nbsp;</label>
                                                            <div class="col-md-3 input-group input-group-md">

                                                                  <gr:textfield id="datepicker2"
                                                                        name="chargeVO.exporterOrderDate"
                                                                        cssClass="form-control text_box" style="text-align: left;"
                                                                        readonly="true" />
                                                            </div>
                                                      </div>

                                                      <div class="form-group">
                                                            <label class="col-md-6 Control-label"
                                                                  style="font-weight: normal;">Export Letter
                                                                  Credit No </label>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="inwardNo" name="chargeVO.inwardNo"
                                                                        readonly="true" cssClass="form-control text_box" />
                                                            </div>
                                                      </div>

                                                      <div class="form-group">
                                                            <label class="col-md-6 Control-label"
                                                                  style="font-weight: normal;"> Beneficiary Name</label>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="refNo" name="chargeVO.beneficiaryName"
                                                                        readonly="true" cssClass="form-control text_box" />
                                                            </div>
                                                      </div>



                                                      <div class="form-group">
                                                            <label class="col-md-6 Control-label"
                                                                  style="font-weight: normal;"> Importer Name</label>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="refNo" name="chargeVO.importerName"
                                                                        cssClass="form-control text_box" readonly="true" />
                                                            </div>
                                                      </div>

                                                      <div class="form-group">
                                                            <label class="col-md-6 Control-label"
                                                                  style="font-weight: normal;">Last Shipment Date
                                                                  &nbsp;&nbsp;</label>
                                                            <div class="col-md-3 input-group input-group-md">

                                                                  <gr:textfield id="datepicker2"
                                                                        name="chargeVO.lastShipmentDate"
                                                                        cssClass="form-control text_box" style="text-align: left;"
                                                                        readonly="true" />
                                                            </div>
                                                      </div>

                                                      <div class="form-group">
                                                            <label class="col-md-6 Control-label"
                                                                  style="font-weight: normal;">% Insurance Deduction</label>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="insurancededuction"
                                                                        name="chargeVO.insuranceDeduction"
                                                                        cssClass="form-control text_box" readonly="true" />
                                                            </div>
                                                      </div>

                                                      <div class="form-group">
                                                            <label class="col-md-6 Control-label"
                                                                  style="font-weight: normal;">Eligible Amount
                                                                  &nbsp;&nbsp;</label>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="eligibleamount"
                                                                        name="chargeVO.eligibleAmount" readonly="true"
                                                                        cssClass="form-control text_box" />
                                                            </div>
                                                      </div>
                                                      <div class="form-group">
                                                            <label class="col-md-6 Control-label"
                                                                  style="font-weight: normal;">Goods Description
                                                                  &nbsp;&nbsp;</label>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textarea id="goodsDescription"
                                                                        name="chargeVO.goodsDescription" cols="18" rows="4"
                                                                        readonly="true"
                                                                        style="margin: 0px; width: 200px; height: 74px;" />
                                                            </div>
                                                      </div>
                                                      <div class="form-group">
                                                            <div class="col-md-6 Control-label">
                                                                  <label style="font-weight: normal;">Shipment Mode&nbsp;&nbsp;</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="shpMode"
                                                                  name="chargeVO.shpMode" readonly="true"
                                                                  cssClass="form-control text_box" />
                                                            </div>
                                                      </div>
                                                      
                                                </div>

                                          </div>

                                          <div style="clear: both; height: 10px;"></div>
                                    </div>

                                    <!-- Changes starts here -->

                                    <%-- <table border="1px" align="left" id="chargeScheduleList">
                                                                              <thead>
                                                                                    <tr>
                                                                                          <th style="text-align: left; width: 200px;"><label>Finance Reference No</label></th>

                                                                                          <th style="text-align: left; width: 200px;"><label>Finance Amount</label></th>
                                                                                          
                                                                              </tr>
                                                                              </thead>

                                                                              <tbody>

                                                                                    <c:if test="${empty multiPaymentReferenceList}">

                                                                                          <td colspan="8">No records found</td>

                                                                                    </c:if>
                                                                                    <c:if test="${not empty multiPaymentReferenceList}">
                                                                                          <gr:iterator value="multiPaymentReferenceList"
                                                                                                id="multiPaymentReferenceList">

                                                                                                <tr class="enquiryList">                                                                                                
                                                                                                      <td style="text-align: left; width: 200px;">
                                                                                                            <div class="form-group">
                                                                                                                  <gr:property value="description" />


                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      <td>
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <gr:property value="eligibleAmount" />

                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                </tr>
                                                                                          </gr:iterator>
                                                                                    </c:if>
                                                                              </tbody>
                                                                        </table>
                                                                        <br /> <br /> --%>



                                    <!-- Changes ends here -->

                                    <div class="row cont_colaps">

                                          <div class="col-md-12">
                                                <div class="col-md-12">
                                                      <div class="row page_content">
                                                            <div id="selectRow" style="display: none">
                                                                  <h5 id="GreenTextID">Please Select the Row...</h5>
                                                            </div>
                                                            <div id="selectRow1" style="display: none">
                                                                  <h5 id="GreenTextID">Please Select All Feilds...</h5>
                                                            </div>

                                                            <div class="form-group"></div>
                                                      </div>
                                                </div>
                                          </div>
                                    </div>

                                    <div style="clear: both; height: 20px;"></div>
                                    <div class="row page_content"></div>

                                    <div style="clear: both; height: 10px;"></div>
                              </div>
                        </div>

                        <!-- New Canges Starts Here -->
                        <div class="row cont_colaps">
                              <div class="col-md-12">
                                    <div class="page_collapsible" id="body-section1">
                                          <span></span>
                                          <h5 style="font-weight: bold; font-size: 13px; color: #527BB8;">&nbsp;List
                                                Of Related Finance</h5>
                                    </div>
                                    <div class="row page_content">
                                          <div class="col-md-12">
                                                <div class="col-md-12">

                                                      <div class="col-md-12">
                                                            <div class="form-group">

                                                                  <!--  TODO changes ends here -->
                                                                  <div class="table">
                                                                        <table border="1px" align="left" id="chargeScheduleList">
                                                                              <thead>
                                                                                    <tr>
                                                                                          <th style="text-align: left; width: 200px;"><label>Finance
                                                                                                      Reference No</label></th>

                                                                                          <th style="text-align: left; width: 200px;"><label>Finance
                                                                                                      Amount</label></th>

                                                                                    </tr>
                                                                              </thead>

                                                                              <tbody>

                                                                                    <c:if test="${empty multiPaymentReferenceList}">

                                                                                          <td colspan="8">No records found</td>

                                                                                    </c:if>
                                                                                    <c:if test="${not empty multiPaymentReferenceList}">
                                                                                          <gr:iterator value="multiPaymentReferenceList"
                                                                                                id="multiPaymentReferenceList">

                                                                                                <tr class="enquiryList">
                                                                                                      <td style="text-align: left; width: 200px;">
                                                                                                            <div class="form-group">
                                                                                                                  <gr:property value="description" />


                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      <td>
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <gr:property value="eligibleAmount" />

                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                </tr>
                                                                                          </gr:iterator>
                                                                                    </c:if>
                                                                              </tbody>
                                                                        </table>
                                                                        <br /> <br />


                                                                  </div>
                                                            </div>
                                                      </div>
                                                </div>
                                          </div>
                                    </div>
                                    <%--
                                    <!-- Changes starts here -->
                              
                                                                        <table border="1px" align="left" id="chargeScheduleList">
                                                                              <thead>
                                                                                    <tr>
                                                                                          <th style="text-align: left; width: 200px;"><label>Finance Reference No</label></th>

                                                                                          <th style="text-align: left; width: 200px;"><label>Finance Amount</label></th>
                                                                                          
                                                                              </tr>
                                                                              </thead>

                                                                              <tbody>

                                                                                    <c:if test="${empty multiPaymentReferenceList}">

                                                                                          <td colspan="8">No records found</td>

                                                                                    </c:if>
                                                                                    <c:if test="${not empty multiPaymentReferenceList}">
                                                                                          <gr:iterator value="multiPaymentReferenceList"
                                                                                                id="multiPaymentReferenceList">

                                                                                                <tr class="enquiryList">                                                                                                
                                                                                                      <td style="text-align: left; width: 200px;">
                                                                                                            <div class="form-group">
                                                                                                                  <gr:property value="description" />


                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                      <td>
                                                                                                            <div class="form-group" align="left">
                                                                                                                  <gr:property value="eligibleAmount" />

                                                                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                                                                            </div>
                                                                                                      </td>
                                                                                                </tr>
                                                                                          </gr:iterator>
                                                                                    </c:if>
                                                                              </tbody>
                                                                        </table>
                                                                        <br /> <br />

                                                                  
                                    
                                    <!-- Changes ends here --> --%>

                                    <div class="row cont_colaps">

                                          <div class="col-md-12">
                                                <div class="col-md-12">
                                                      <div class="row page_content">
                                                            <div id="selectRow" style="display: none">
                                                                  <h5 id="GreenTextID">Please Select the Row...</h5>
                                                            </div>
                                                            <div id="selectRow1" style="display: none">
                                                                  <h5 id="GreenTextID">Please Select All Feilds...</h5>
                                                            </div>

                                                            <div class="form-group"></div>
                                                      </div>
                                                </div>
                                          </div>
                                    </div>

                                    <div style="clear: both; height: 20px;"></div>
                                    <div class="row page_content"></div>

                                    <div style="clear: both; height: 10px;"></div>
                              </div>
                        </div>
                        <!-- Change Ends Here -->
                        <div class="row page_content" id="fxConversionId">
                              <!-- style="display:none" -->

                        </div>
                        <div style="clear: both; height: 20px;"></div>
                  </div>
            </div>
      </gr:form>
      <div id="footer">
            <%@ include file="/view/includes/FOOTER.jsp"%>
      </div>
</body>
</html>