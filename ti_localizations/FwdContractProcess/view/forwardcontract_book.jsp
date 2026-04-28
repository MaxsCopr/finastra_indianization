<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="gr" uri="/struts-tags"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Forward Contract Maker - Booking</title>
<%@include file="includes/header.jsp"%>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<link rel="stylesheet" href="css/jquery-ui.css" />
<link rel="stylesheet" href="css/style.css" />
<link href="css/datepicker.css" rel="stylesheet"></link>
<link type="text/css" rel="stylesheet" href="css/bootstrap.css" />
<link />
<link type="text/css" rel="stylesheet" href="css/bootstrap-dropdown.css" />
<link />
<link type="text/css" rel="stylesheet" href="css/headfoot.css" />
<link />
<link href="css/font-awesome.css" rel="stylesheet" />

<!-- recently added -->
<link rel="stylesheet" href="css/commonTiplus.css" />

<!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css" /> -->


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
<script type="text/javascript" src="js/home.js"></script>
<script type="text/javascript" src="js/forwardcontract.js"></script>
<!-- <script src="js/purchaseorder/purchase_order.js" type="text/javascript"></script> -->


<script src="js/jquery-ui.js"></script>

<!-- <script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/commonTiplus.js"></script>
<script type="text/javascript" src="js/forwardcontract.js"></script> -->

<script type="text/javascript">

      $(document).ready(function() {
            
            $("#bookingDate").datepicker({
                  changeMonth : true,
                  changeYear : true,
                  dateFormat : 'dd/mm/yy'
            });
            $("#dealValidFromDate").datepicker({
                  changeMonth : true,
                  changeYear : true,
                  dateFormat : 'dd/mm/yy'
            });
            $("#dealValidToDate").datepicker({
                  changeMonth : true,
                  changeYear : true,
                  dateFormat : 'dd/mm/yy'
            });
            $("#retAdRefDate").datepicker({
                  changeMonth : true,
                  changeYear : true,
                  dateFormat : 'dd/mm/yy'
            });
            $("#ftiRefDate").datepicker({
                  changeMonth : true,
                  changeYear : true,
                  dateFormat : 'dd/mm/yy'
            });
            $("#fccRefDate").datepicker({
                  changeMonth : true,
                  changeYear : true,
                  dateFormat : 'dd/mm/yy'
            });
            $("#tradeTxnRefDate").datepicker({
                  changeMonth : true,
                  changeYear : true,
                  dateFormat : 'dd/mm/yy'
            });
            $("#uidRefDate").datepicker({
                  changeMonth : true,
                  changeYear : true,
                  dateFormat : 'dd/mm/yy'
            });
            $("#datepicker2").datepicker({
                  changeMonth : true,
                  changeYear : true,
                  dateFormat : 'dd/mm/yy'
            });

            $('input[type="submit"],a').click(function() {
                  $('body').modal({
                        show : 'false'
                  });
                  $('body').removeClass('removePageLoad');
                  $('body').addClass('addPageLoad');

            });

      });
      
      function isChecked() {
            var check=document.getElementById("withoutLimit").checked;  
            if(check==true) {
                  $("#limitID").val("");
                  /* $("#withoutLimit").val("true"); */
            }/* else {
                  $("#withoutLimit").val("false");
            } */
      }
</script>

</head>

<!-- <body class="body_bg" onload="margin_cal()"> -->
<body class="body_bg">
      <%-- <%@ include file="/view/includes/TITLE.jsp"%> --%>
      <img src="images/FTI-UBI.png" width="100%" />
      <gr:form method="post" id="myForm" name="icForm">
            <div class="row">
                  <div class="col-md-2">
                        <div class="side_nav" style="width: 215px;">
                              <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;"><a href="makerProcess">Close</a></li>
                              </ul>
                        </div>
                        <br />
                        <div class="side_nav" style="width: 215px;">
                              <ul class="nav nav-pills nav-stacked">
                                     <!-- <li style="text-align: center;"><a href="#" id="insertFwcBook">Submit</a></li>  -->
                                     <li style="text-align: center;"><a href="#" onclick="insertFwcBook()">Submit</a></li>
                              </ul>
                        </div>
                        <br />
                        <div class="side_nav" style="width: 215px;">
                              <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;"><a href="#" id="reset">Reset</a></li>
                              </ul>
                        </div>
                        <br />
                        <div class="side_nav" style="width: 215px;">
                              <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;"><a href="#" id="validate">Validate</a></li>
                              </ul>
                        </div>
                        <br />
                        <div class="side_nav" style="width: 215px;">
                              <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;"><a href="#"
                                          onclick="saveBookingDetails()">Save</a></li>
                              </ul>
                        </div>
                        <br />
                        <div class="side_nav" style="width: 215px;">
                              <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;"><a href="#"
                                          onclick="generateFWCPostings()">View Postings</a></li>
                              </ul>
                        </div>
                  </div>

                  <div class="col-md-10 content_box">

                        <h5 style="font-weight: bold; font-size: 13px; color: #527BB8;">&nbsp;Forward
                              Contract Booking - Maker Process</h5>
                        <br /> <br />
                        <div class="row page_content">
                              <div class="col-md-12">
                                    <div class="page_collapsible" id="body-section1">
                                          <span></span>
                                          <h5 style="font-weight: bold; font-size: 13px; color: #527BB8;">&nbsp;Error
                                                Description</h5>
                                    </div>

                                    <div class="form-group">
                                          <br />
                                          <div class="table">
                                                <table border="1px" align="left" id="errorList">
                                                      <tbody>
                                                            <tr>
                                                                  <th style="text-align: left; width: 150px"><label>&nbsp;&nbsp;Severity</label></th>

                                                                  <th style="text-align: left; width: 150px"><label>&nbsp;&nbsp;Description</label></th>

                                                                  <th style="text-align: left; width: 150px"><label>&nbsp;&nbsp;Steps</label></th>

                                                                  <th style="text-align: left; width: 450px"><label>&nbsp;&nbsp;Details</label></th>

                                                                  <th style="text-align: left; width: 150px"><label>&nbsp;&nbsp;Overridden</label></th>
                                                            </tr>

                                                            <gr:iterator value="fwdContractVO.errorList" id="errorList">
                                                                  <tr>
                                                                        <td>
                                                                              <div class="form-group" align="left">
                                                                                    <gr:property value="errorId" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>

                                                                        </td>
                                                                        <td>
                                                                              <div class="form-group" align="left">
                                                                                    <gr:property value="errorDesc" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td>
                                                                              <div class="form-group" align="left">
                                                                                    <gr:property value="errorCode" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td>
                                                                              <div class="form-group" align="left">
                                                                                    <gr:property value="errorDetails" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td>
                                                                              <div class="form-group" align="left">
                                                                                    <gr:property value="errorMsg" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>

                                                                  </tr>
                                                            </gr:iterator>
                                                      </tbody>
                                                </table>
                                          </div>
                                    </div>
                              </div>
                              <c:if test="${not empty msg}">
                                    <h5 style="font-weight: lighter; font-size: 13px; color: #FF0000;">
                                          <gr:property value="msg" />
                                    </h5>
                              </c:if>
                        </div>
                        <br />
                        <div id="userIdMessage" style="color: orange"></div>
                        <br />


                        <!-- Posting starts -->


                        <div class="row page_content">
                              <div class="col-md-12">
                                    <div class="page_collapsible" id="body-section1">
                                          <span></span>
                                          <h5 style="font-weight: bold; font-size: 13px; color: #527BB8;">&nbsp;Postings</h5>
                                    </div>

                                    <div class="table">
                                          <table border="1px" align="left" id="postingList">
                                                <tbody>
                                                      <tr>
                                                            <!-- <th style="text-align: left; width: 200px"><label>&nbsp;&nbsp;Type</label></th> -->

                                                            <th style="text-align: left; width: 50px"><label>&nbsp;&nbsp;Dr/Cr</label></th>

                                                            <th style="text-align: left; width: 200px"><label>&nbsp;&nbsp;Account</label></th>

                                                            <th style="text-align: left; width: 200px"><label>&nbsp;&nbsp;Amount</label></th>

                                                            <th style="text-align: left; width: 150px"><label>&nbsp;&nbsp;Value
                                                                        Date</label></th>

                                                            <th style="text-align: left; width: 500px"><label>&nbsp;&nbsp;Description</label></th>
                                                      </tr>
                                                      <gr:iterator value="fwdContractVO.postingList" id="postingList">
                                                            <tr>
                                                                  <%--  <td>
                                                                              <div class="form-group" align="left">
                                                                                    <gr:property value="postingType" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>

                                                                        </td> --%>
                                                                  <td>
                                                                        <div class="form-group" align="left">
                                                                              <gr:property value="postingDrCrFlag" />
                                                                              <div class="col-md-8 input-group input-group-md"></div>
                                                                        </div>
                                                                  </td>
                                                                  <td>
                                                                        <div class="form-group" align="left">
                                                                              <gr:property value="postingAcctNumber" />
                                                                              <div class="col-md-8 input-group input-group-md"></div>
                                                                        </div>
                                                                  </td>
                                                                  <td>
                                                                        <div class="form-group" align="left">
                                                                              <gr:property value="postingAmountCcy" />
                                                                              <div class="col-md-8 input-group input-group-md"></div>
                                                                        </div>
                                                                  </td>
                                                                  <td>
                                                                        <div class="form-group" align="left">
                                                                              <gr:property value="postingValueDate" />
                                                                              <div class="col-md-8 input-group input-group-md"></div>
                                                                        </div>
                                                                  </td>
                                                                  <td>
                                                                        <div class="form-group" align="left">
                                                                              <gr:property value="postingDesc" />
                                                                              <div class="col-md-8 input-group input-group-md"></div>
                                                                        </div>
                                                                  </td>
                                                            </tr>
                                                      </gr:iterator>

                                                </tbody>
                                          </table>
                                    </div>



                              </div>
                        </div>
                        <br />
                        <br />
                        <!--  Posting ends -->

                        <div class="row cont_colaps">
                              <div class="col-md-12">
                                    <div class="page_collapsible" id="body-section1">
                                          <span></span>
                                          <h5 style="font-weight: bold; font-size: 13px; color: #527BB8;">&nbsp;Booking
                                                Details</h5>
                                    </div>

                                    <div class="col-md-12">

                                          <div class="row page_content">
                                                <div class="col-md-6">
                                                      <div class="form-group">
                                                            <label class="col-md-5 Control-label"
                                                                  style="font-weight: normal;">Sub Product</label>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:select id="subProduct" list="subProductList"
                                                                        listKey="key" listValue="value"
                                                                        headerValue="<-------------------->" headerKey=""
                                                                        name="fwdContractVO.subProduct"
                                                                        style="width: 200px; height: 25px" cssClass="chosen">
                                                                  </gr:select>
                                                            </div>
                                                      </div>
                                                </div>

                                                <div class="col-md-6">
                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;">Forward Contract
                                                                        Number &nbsp;&nbsp;</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="fwdContractNo"
                                                                        name="fwdContractVO.fwdContractNo" readonly="true"
                                                                        cssClass="form-control text_box" />
                                                            </div>
                                                      </div>
                                                </div>
                                          </div>

                                          <div class="row page_content">

                                                <div>
                                                      <p
                                                            style="font-weight: bold; color: #527BB8; text-decoration: underline; margin-left: 20px;">
                                                            Customer Details</p>
                                                </div>
                                                <div class="col-md-6">
                                                      <div class="form-group required">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;">Customer ID&nbsp;<span
                                                                        class="searchBtn"
                                                                        style="color: #579EDC; font-weight: bold; cursor: pointer;"
                                                                        onclick="customerStaticDetails()"> <img
                                                                              src="images/magnify.png" width="13%" height="13%" /></span></label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="customerID" name="fwdContractVO.customerID"
                                                                        readonly="false" cssClass="form-control text_box" />
                                                            </div>
                                                      </div>
                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;">Account
                                                                        Number&nbsp;<span class="searchBtn"
                                                                        style="color: #579EDC; font-weight: bold; cursor: pointer;"
                                                                        onclick="accountStaticDetails()"> <img
                                                                              src="images/magnify.png" width="13%" height="13%" /></span>
                                                                  </label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="acctNumber" name="fwdContractVO.acctNumber"
                                                                        readonly="false" cssClass="form-control text_box" />
                                                            </div>
                                                      </div>

                                                </div>
                                                <div class="col-md-6">
                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;"> Branch Code<!-- <span
                                                                        class="searchBtn"
                                                                        style="color: #579EDC; font-weight: bold; cursor: pointer;"
                                                                        onclick="branchStaticDetails()"> <img
                                                                              src="images/magnify.png" width="13%" height="13%" /></span> --></label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="branchCode" name="fwdContractVO.branchCode"
                                                                        readonly="true" cssClass="form-control text_box" />
                                                            </div>
                                                      </div>
                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;"> Deal Currency<!-- <span
                                                                        class="searchBtn"
                                                                        style="color: #579EDC; font-weight: bold; cursor: pointer;"
                                                                        onclick="currencyStaticDetails()"> <img
                                                                              src="images/magnify.png" width="13%" height="13%" /></span> --></label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="dealCurrency"
                                                                        name="fwdContractVO.dealCurrency" readonly="false"
                                                                        cssClass="form-control text_box" />
                                                            </div>
                                                      </div>
                                                </div>
                                          </div>

                                          <div class="row page_content">
                                                <div>
                                                      <p
                                                            style="font-weight: bold; color: #527BB8; text-decoration: underline; margin-left: 20px;">
                                                            Contract Details</p>
                                                </div>
                                                <div class="col-md-6">

                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;">Treasury
                                                                        Reference Number<span class="searchBtn"
                                                                        style="color: #579EDC; font-weight: bold; cursor: pointer;"
                                                                        onclick="treasuryRefDetails()"> <img
                                                                              src="images/magnify.png" width="13%" height="13%" /></span>
                                                                  </label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="treasuryRefNo"
                                                                        name="fwdContractVO.treasuryRefNo" readonly="false"
                                                                        cssClass="form-control text_box"
                                                                        onchange="fetchDependentTreasuryDetails()" />
                                                            </div>
                                                      </div>


                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;"> To Amount</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="toCurrencyAmt"
                                                                        name="fwdContractVO.toCurrencyAmt"
                                                                        cssClass="form-control text_box" readonly="true"/>
                                                            </div>
                                                      </div>


                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;">Deal Valid From
                                                                        &nbsp;&nbsp;</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="dealValidFromDate"
                                                                        name="fwdContractVO.dealValidFromDate"
                                                                        cssClass="form-control text_box" readonly="true"/>
                                                            </div>
                                                      </div>

                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;">Booking Date
                                                                        &nbsp;&nbsp;</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="bookingDate"
                                                                        name="fwdContractVO.bookingDate"
                                                                        cssClass="form-control text_box" readonly="true"/>
                                                            </div>
                                                      </div>


                                                </div>
                                                <div class="col-md-6">
                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;">Forward Contract
                                                                        Amount</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="fwdContractAmt"
                                                                        name="fwdContractVO.fwdContractAmt"
                                                                        cssClass="form-control text_box" readonly="true"/>
                                                            </div>
                                                      </div>


                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;"> Outstanding
                                                                        Amount</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="outstandingAmt"
                                                                        name="fwdContractVO.outstandingAmt" readonly="false"
                                                                        cssClass="form-control text_box" />
                                                            </div>
                                                      </div>

                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;"> Deal Valid
                                                                        To&nbsp;&nbsp;</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="dealValidToDate"
                                                                        name="fwdContractVO.dealValidToDate"
                                                                        cssClass="form-control text_box" readonly="true"/>
                                                            </div>
                                                      </div>

                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;"> Treasury Rate</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="treasuryRate"
                                                                        name="fwdContractVO.treasuryRate" readonly="false"
                                                                        cssClass="form-control text_box" />
                                                            </div>
                                                      </div>


                                                </div>

                                          </div>

                                          <div class="row page_content">
                                                <div>
                                                      <p
                                                            style="font-weight: bold; color: #527BB8; text-decoration: underline; margin-left: 20px;">
                                                            Limit Details</p>
                                                </div>
                                                <div class="col-md-6">

                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;">Limit ID<span
                                                                        class="searchBtn"
                                                                        style="color: #579EDC; font-weight: bold; cursor: pointer;"
                                                                        onclick="limitIDDetails()"> <img
                                                                              src="images/magnify.png" width="25%" height="25%" /></span>
                                                                  </label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="limitID" name="fwdContractVO.limitID"
                                                                        readonly="false" cssClass="form-control text_box" />
                                                            </div>
                                                      </div>

                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;"> Available
                                                                        Limit&nbsp;&nbsp;</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="availableLimit"
                                                                        name="fwdContractVO.availableLimit"
                                                                        cssClass="form-control text_box" />
                                                            </div>
                                                      </div>

                                                </div>

                                                <div class="col-md-6">

                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;"> Without
                                                                        Limit&nbsp;&nbsp;</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <!-- <input type="checkbox" id="withoutLimit" name="fwdContractVO.withoutLimit" onclick="isChecked()"></input> -->
                                                            <gr:checkbox id="withoutLimit" name="fwdContractVO.withoutLimit" onclick="isChecked()"/>
                                                            </div>
                                                      </div>

                                                </div>

                                          </div>

                                          <div class="row page_content">
                                                <div>
                                                      <p
                                                            style="font-weight: bold; color: #527BB8; text-decoration: underline; margin-left: 20px;">
                                                            Charge Details</p>
                                                </div>
                                                <div class="col-md-6">
                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;"> Charge Amount</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="chargeAmount" placeholder="Eg:1000 INR"
                                                                        name="fwdContractVO.chargeAmount" readonly="false"
                                                                        cssClass="form-control text_box" />
                                                            </div>
                                                      </div>

                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;"> GST Amount</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="gstAmount" placeholder="Eg:1000 INR"
                                                                        name="fwdContractVO.gstAmount" readonly="false"
                                                                        cssClass="form-control text_box" />
                                                            </div>
                                                      </div>
                                                </div>
                                          </div>
                                          
                                          <div class="row page_content">
                                                <div>
                                                      <p
                                                            style="font-weight: bold; color: #527BB8; text-decoration: underline; margin-left: 20px;">
                                                            Additional Details</p>
                                                </div>
                                                <div class="col-md-6">

                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;">Wash Rate
                                                                        &nbsp;&nbsp;</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="washRate" name="fwdContractVO.washRate"
                                                                        readonly="false" cssClass="form-control text_box" />
                                                            </div>
                                                      </div>


                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;"> P&L Amount</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="plAmount" name="fwdContractVO.plAmount"
                                                                        cssClass="form-control text_box" />
                                                            </div>
                                                      </div>


                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;">Instructions&nbsp;&nbsp;</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textarea id="instructions"
                                                                        name="fwdContractVO.instructions" cols="18" rows="4"
                                                                        style="margin: 0px; width: 200px; height: 74px;" />
                                                            </div>
                                                      </div>


                                                </div>
                                                <div class="col-md-6">


                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;">LEI Number
                                                                        &nbsp;&nbsp;</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="leiNumber" name="fwdContractVO.leiNumber"
                                                                        readonly="false" cssClass="form-control text_box" />
                                                            </div>
                                                      </div>


                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;"> Margin </label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="margin" name="fwdContractVO.margin"
                                                                        readonly="false" cssClass="form-control text_box" />
                                                            </div>
                                                      </div>
                                                      
                                                       <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;"> Transaction ID </label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="transid" name="fwdContractVO.transid"
                                                                        cssClass="form-control text_box" />
                                                            </div>
                                                      </div>
                                                      
                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;"> Transaction Date </label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="transdate"
                                                                        name="fwdContractVO.transdate" readonly="false"
                                                                        cssClass="datepicker form-control text_box"
                                                                        style="text-align: left;" />
                                                            </div>
                                                      </div>

                                                </div>
                                                <div style="clear: both; height: 10px;"></div>
                                          </div>

                                          <%--
                                          <gr:hidden name="fwdContractVO.subProduct"></gr:hidden>
                                          <gr:hidden name="fwdContractVO.fwdContractNo"></gr:hidden>
                                          <gr:hidden name="fwdContractVO.customerID"></gr:hidden>
                                          <gr:hidden name="fwdContractVO.acctNumber"></gr:hidden>
                                          <gr:hidden name="fwdContractVO.branchCode"></gr:hidden>
                                          <gr:hidden name="fwdContractVO.dealCurrency"></gr:hidden>
                                          <gr:hidden name="fwdContractVO.bookingDate"></gr:hidden>
                                          <gr:hidden name="fwdContractVO.toCurrency"></gr:hidden>
                                          <gr:hidden name="fwdContractVO.dealValidFromDate"></gr:hidden>
                                          <gr:hidden name="fwdContractVO.treasuryRefNo"></gr:hidden>
                                          <gr:hidden name="fwdContractVO.outstandingAmt"></gr:hidden>
                                          <gr:hidden name="fwdContractVO.fwdContractAmt"></gr:hidden>
                                          <gr:hidden name="fwdContractVO.toCurrencyAmt"></gr:hidden>
                                          <gr:hidden name="fwdContractVO.dealValidToDate"></gr:hidden>
                                          <gr:hidden name="fwdContractVO.treasuryRate"></gr:hidden>
                                          <gr:hidden name="fwdContractVO.limitID"></gr:hidden>
                                          <gr:hidden name="fwdContractVO.availableLimit"></gr:hidden>
                                          <gr:hidden name="fwdContractVO.washRate"></gr:hidden>
                                          <gr:hidden name="fwdContractVO.plAmount"></gr:hidden>
                                          <gr:hidden name="fwdContractVO.instructions"></gr:hidden>
                                          <gr:hidden name="fwdContractVO.leiNumber"></gr:hidden>
                                          <gr:hidden name="fwdContractVO.chargeAmount"></gr:hidden>
                                          <gr:hidden name="fwdContractVO.margin"></gr:hidden> --%>


                                    </div>
                              </div>

                        </div>

                        <gr:hidden id="screenType" name="fwdContractVO.screenType"
                              value="MakerBookingScreen"></gr:hidden>
                              <gr:hidden id="id" name="fwdContractVO.id"></gr:hidden>
                  </div>
            </div>
      </gr:form>
      <div id="footer">
            <%@ include file="/view/includes/FOOTER.jsp"%>
      </div>
</body>
</html>