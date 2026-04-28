<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="boe" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>Forward Contract Enquiry</title>

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
<script src="js/jquery-ui.js"></script>

<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/commonTiplus.js"></script>
<script type="text/javascript" src="js/forwardcontract.js"></script>

<script>
      $(document).ready(function() {
            $("#bookingDate").datepicker({
                  changeMonth : true,
                  changeYear : true,
                  dateFormat : 'dd/mm/yy'
            });
            $("#fromDate").datepicker({
                  dateFormat : 'dd/mm/yy'
            });
            $("#toDate").datepicker({
                  dateFormat : 'dd/mm/yy'
            });

      });

      function fetchEnquiryFwdcontractDetails() {
            
                  var customerID = $.trim($('#customerID').val());
                  var fwdContractNo =  $.trim($('#fwdContractNo').val());
                  
                  if (customerID.length == 0 && fwdContractNo.length == 0) {
                        alert("Customer ID or Forward Contract Number is Mandatory !!!"); 
                  } else {
                        onChangeLoad();
                        $("#myForm").attr("action", "fetchEnquiryFWCDetails");
                        $("#myForm").submit();
                  }     
      }
      
</script>

</head>

<body class="body_bg" onload="display_ct()">
      <%-- <%@ include file="/view/includes/TITLE.jsp"%> --%>
      <img src="images/FTI-UBI.png" width="100%" />
      <boe:form method="post" id="myForm" name="form">


            <div class="row">
                  <div class="col-md-2">
                        <div class="side_nav" style="width: 215px;">
                              <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;">
                                          <a href="home">Close</a>
                                    </li>
                              </ul>
                        </div>
                        <br />
                  </div>

                  <div class="col-md-10 content_box">

                        <h5 class="row fontcol" style="color: #527BB8">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                              Forward Contract - Enquiry Process</h5>
                        <br /> <br />

                        <div id="userIdMessage" style="color: #527BB8"></div>
                        <div class="row cont_colaps">

                              <div class="col-md-12">

                                    <div class="row page_content">

                                          <div class="col-md-6">
                                                <div class="form-group">
                                                      <div class="col-md-5 Control-label">
                                                            <label style="font-weight: normal;">Forward Contract
                                                                  Number</label>
                                                      </div>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="fwdContractNo"
                                                                  name="fwdContractVO.fwdContractNo"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                </div>


                                                <div class="form-group">
                                                      <div class="col-md-5 Control-label">
                                                            <label style="font-weight: normal;">Valid From Date
                                                                  &nbsp;&nbsp;</label>
                                                      </div>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="validFrom" name="fwdContractVO.validFrom"
                                                                  cssClass="datepicker form-control text_box" readonly="true"
                                                                  style="text-align: left;" />
                                                      </div>
                                                </div>

                                                <div class="form-group">
                                                      <div class="col-md-5 Control-label">
                                                            <label style="font-weight: normal;">Customer ID&nbsp;<span
                                                                  class="searchBtn"
                                                                  style="color: #579EDC; font-weight: bold; cursor: pointer;"
                                                                  onclick="customerStaticDetails()"> <img
                                                                        src="images/magnify.png" width="13%" height="13%" /></span></label>
                                                      </div>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="customerID" name="fwdContractVO.customerID"
                                                                  readonly="false" cssClass="form-control text_box" />
                                                      </div>
                                                </div>

                                          </div>
                                          <div class="col-md-6">

                                                <div class="form-group">
                                                      <div class="col-md-5 Control-label">
                                                            <label style="font-weight: normal;"> Branch Code<span
                                                                  class="searchBtn"
                                                                  style="color: #579EDC; font-weight: bold; cursor: pointer;"
                                                                  onclick="branchStaticDetails()"> <img
                                                                        src="images/magnify.png" width="13%" height="13%" /></span></label>
                                                      </div>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="branchCode" name="fwdContractVO.branchCode"
                                                                  readonly="false" cssClass="form-control text_box" />
                                                      </div>
                                                </div>

                                                <div class="form-group">
                                                      <div class="col-md-5 Control-label">
                                                            <label style="font-weight: normal;">Valid To Date
                                                                  &nbsp;&nbsp;</label>
                                                      </div>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="validTo" name="fwdContractVO.validTo"
                                                                  cssClass="datepicker form-control text_box" readonly="true"
                                                                  style="text-align: left;" />
                                                      </div>
                                                </div>

                                                <div class="col-md-3" style="margin-left: 39%;">
                                                      <input style="width: 50px" type="button" value="Search"
                                                            class="button" onclick="fetchEnquiryFwdcontractDetails()" />
                                                </div>

                                          </div>

                                    </div>
                                    <br /> <br />

                                    <!--  TODO changes End here -->

                              </div>



                              <boe:if test="hasActionMessages()">
                                    <div id="displayMessage"
                                          style="color: green; font-weight: bold; cursor: pointer;">
                                          <boe:actionmessage />
                                    </div>
                              </boe:if>
                              <!-- ends here -->

                              <div class="row page_content">
                                    <div class="col-md-12">
                                          <div class="col-md-12">

                                                <div class="col-md-12">
                                                      <div class="form-group">
                                                            <div class="table">


                                                                  <table border="1px" align="center" id="forwardContractList">
                                                                        <tbody>
                                                                              <tr>
                                                                              <th style="text-align: center; width: 100px;"><label>ID
                                                                                                </label></th>
                                                                                    <th style="text-align: center; width: 100px;"><label>Action
                                                                                                </label></th>
                                                                                    <th style="text-align: center; width: 100px;"><label>Forward
                                                                                                Contract Number</label></th>

                                                                                    <th style="text-align: center; width: 100px;"><label>Category</label></th>


                                                                                    <th style="text-align: center; width: 100px;"><label>Customer
                                                                                                ID</label></th>

                                                                                    <th style="text-align: center; width: 100px;"><label>Branch
                                                                                    </label></th>

                                                                                    <th style="text-align: center; width: 100px;"><label>Buy
                                                                                                Or Sell </label></th>

                                                                                    <th style="text-align: center; width: 100px;"><label>Buy
                                                                                                Amount</label></th>
                                                                                    <th style="text-align: center; width: 100px;"><label>Sell
                                                                                                Amount</label></th>

                                                                                    <th style="text-align: center; width: 100px;"><label>Transaction
                                                                                                Type</label></th>

                                                                                    <th style="text-align: center; width: 100px;"><label>Deal
                                                                                                Valid From</label></th>
                                                                                    <th style="text-align: center; width: 100px;"><label>Deal
                                                                                                Valid To</label></th>
                                                                                    <th style="text-align: center; width: 100px;"><label>Treasury
                                                                                                Reference Number</label></th>
                                                                                    <th style="text-align: center; width: 100px;"><label>Treasury
                                                                                                Rate</label></th>
                                                                                    <th style="text-align: center; width: 100px;"><label>Outstanding
                                                                                                Amount</label></th>

                                                                                    <th style="text-align: center; width: 100px;"><label>Bill
                                                                                                Id </label></th>

                                                                                    <th style="text-align: center; width: 100px;"><label>Status</label></th>



                                                                              </tr>


                                                                              <c:if test="${empty forwardContractList}">

                                                                                    <td colspan="17">No records found</td>

                                                                              </c:if>
                                                                              <c:if test="${not empty forwardContractList}">
                                                                                    <boe:iterator value="forwardContractList"
                                                                                          id="forwardContractList">

                                                                                          <tr class="tableList">
                                                                                                <td style="text-align: center;" >
                                                                                                      <div class="form-group" style="width: 30px; margin: 5px">
                                                                                                            <boe:property value="id" />
                                                                                                      </div>
                                                                                                </td>
                                                                                                <td>
                                                                                                      <div class="form-group">
                                                                                                            <c:choose>
                                                                                                                  <c:when
                                                                                                                        test="${category=='FWCBOOK' || category=='FWCCANCEL'}">
                                                                                                                        <input type="button" class="viewButton" value="View"
                                                                                                                              style="width: 50px; margin: 5px" />
                                                                                                                  </c:when>
                                                                                                            </c:choose>
                                                                                                      </div>
                                                                                                </td>
                                                                                                <td style="text-align: center;">
                                                                                                      <div class="form-group">
                                                                                                            <boe:property value="fwdContractNo" />

                                                                                                            <div class="col-md-2 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>

                                                                                                <td style="text-align: center;">
                                                                                                      <div class="form-group">
                                                                                                            <boe:property value="category" />

                                                                                                            <div class="col-md-2 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>


                                                                                                <td>
                                                                                                      <div class="form-group" align="center">
                                                                                                            <boe:property value="customerID" />

                                                                                                            <div class="col-md-2 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>


                                                                                                <td>
                                                                                                      <div class="form-group" align="center">
                                                                                                            <boe:property value="branchCode" />

                                                                                                            <div class="col-md-2 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>

                                                                                                <td>
                                                                                                      <div class="form-group" align="center">
                                                                                                            <boe:property value="buyOrSell" />

                                                                                                            <div class="col-md-2 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>

                                                                                                <td>
                                                                                                      <div class="form-group" align="center">
                                                                                                            <boe:property value="buyAmount" />

                                                                                                            <div class="col-md-2 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>
                                                                                                <td>
                                                                                                      <div class="form-group" align="center">
                                                                                                            <boe:property value="sellAmount" />

                                                                                                            <div class="col-md-2 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>
                                                                                                <td>
                                                                                                      <div class="form-group" align="center">
                                                                                                            <boe:property value="tranType" />

                                                                                                            <div class="col-md-2 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>


                                                                                                <td>
                                                                                                      <div class="form-group" align="center">
                                                                                                            <boe:property value="dealValidFromDate" />

                                                                                                            <div class="col-md-2 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>

                                                                                                <td>
                                                                                                      <div class="form-group" align="center">
                                                                                                            <boe:property value="dealValidToDate" />

                                                                                                            <div class="col-md-2 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>

                                                                                                <td>
                                                                                                      <div class="form-group" align="center">
                                                                                                            <boe:property value="treasuryRefNo" />

                                                                                                            <div class="col-md-2 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>

                                                                                                <td>
                                                                                                      <div class="form-group" align="center">
                                                                                                            <boe:property value="treasuryRate" />

                                                                                                            <div class="col-md-2 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>

                                                                                                <td>
                                                                                                      <div class="form-group" align="center">
                                                                                                            <boe:property value="outstandingAmt" />

                                                                                                            <div class="col-md-2 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>

                                                                                                <td>
                                                                                                      <div class="form-group" align="center">
                                                                                                            <boe:property value="billId" />

                                                                                                            <div class="col-md-2 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>

                                                                                                <td>
                                                                                                      <div class="form-group" align="center">
                                                                                                            <boe:property value="status" />

                                                                                                            <div class="col-md-2 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>

                                                                                          </tr>
                                                                                    </boe:iterator>
                                                                              </c:if>
                                                                        </tbody>
                                                                  </table>
                                                                  <br /> <br />

                                                            </div>
                                                      </div>

                                                      <input type="hidden" id="check" name="check" /> <input
                                                            type="hidden" id="idAndFwdContractNo"
                                                            name="idAndFwdContractNo" />
                                                      <boe:hidden id="screenType" name="fwdContractVO.screenType"
                                                            value="EnquiryScreen"></boe:hidden>


                                                </div>
                                          </div>
                                    </div>
                              </div>

                              <br />
                        </div>
                  </div>
                  <div height="15px">&nbsp;</div>
      </boe:form>
      <div id="footer">
            <%@ include file="/view/includes/FOOTER.jsp"%>
      </div>
</body>
</html>