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

<!-- <script type="text/javascript" src="js/date_search.js"></script> -->
<script src="js/jquery-ui.js"></script>

<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/commonTiplus.js"></script>
<script type="text/javascript" src="js/forwardcontract.js"></script>

<!--
<link rel="stylesheet" href="css/jquery-ui.css" />
<link rel="stylesheet" href="css/style.css" />
<link href="css/datepicker.css" rel="stylesheet"></link>
<link type="text/css" rel="stylesheet" href="css/bootstrap.css" />
<link />
<link type="text/css" rel="stylesheet" href="css/bootstrap-dropdown.css" />
<link />
<link type="text/css" rel="stylesheet" href="css/headfoot.css" />
<link />
<link rel="stylesheet" href="css/commonTiplus.css"></link>
<link href="css/font-awesome.css" rel="stylesheet"></link>
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
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/date.js"></script>
<script type="text/javascript" src="js/forwardcontract.js"></script>
<link rel="stylesheet" href="css/commonTiplus.css"></link>
<link type="text/css" rel="stylesheet" href="boe/styles/manypayment.css" />
 -->

<!--[if IE 7]>
   <link rel="stylesheet" type="text/css" href="css/styleie7.css" />
 <![endif]-->
<!--  TODO changes starts here -->
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
      function fetchCheckerDetails() {
            $("#myForm").attr("action", "checkerProcess");
            $("#myForm").submit();
      }

      function fetchEnquiryFwdcontractDetails() {
            onChangeLoad();
            $("#myForm").attr("action", "fetchEnquiryFwdcontractDetails");
            $("#myForm").submit();
      }
</script>

<script>
      function enquiryProcessDetails(event) {
            $(".tableList").removeClass("highlighted");
            $(event).addClass("highlighted");

            var value1 = $(event).find("td").eq(0).text().trim();
            var value2 = $(event).find("td").eq(2).text().trim();

            var enquiryValue = value1 + ':' + value2

            $.ajaxSetup({
                  async : false
            });
            $('#idAndFwdContractNo').val(enquiryValue);

            var idAndFwdContractNo = $('#idAndFwdContractNo').val();

            if (idAndFwdContractNo != '') {
                  $('#myForm').attr('action', 'enquiryProcessDataView');
                  $('#myForm').submit();
            }

      }

      $(function() {
            $("#displayMessage").dialog({
                  title:"Message !!!"
            });
      });
</script>
<!-- <script>
$('#poNumber').onkeyup(function checkkey(ev)
      {
            var iChars = "!@#$%^&*()+=-[]\\\';,./{}|\":<>?";
              for (var i = 0; i < ev.value.length; i++) {
                if (iChars.indexOf(e.value.charAt(i)) != -1) {
                  alert ("Please do not use special characters.");
                  ev.value = ev.value-1;
                  ev.focus();
                  return false;
      }
                });
</script> -->

<!-- <script>
checkkey()
{
      var a = document.getElementById('poNumber').value;
      var iChars = "!@#$%^&*()+=-[]\\\';,./{}|\":<>?";
       for (var i = 0; i < a.value.length; i++) {
             if (iChars.indexOf(a.value.charAt(i)) != -1) {
                   alert ("Please do not use special characters.");
                   a.value = a.value-1;
                   a.focus();
                   return false;
             }
       }
}
</script> -->
<!--  TODO changes ends here -->
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
                                                            <boe:textfield id="acctNumber" name="fwdContractVO.acctNumber"
                                                                  readonly="false" cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                <div class="form-group">
                                                      <div class="col-md-5 Control-label">
                                                            <label style="font-weight: normal;">Booking Date
                                                                  &nbsp;&nbsp;</label>
                                                      </div>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="bookingDate"
                                                                  name="fwdContractVO.bookingDate"
                                                                  cssClass="datepicker form-control text_box" readonly="true"
                                                                  style="text-align: left;" />
                                                      </div>
                                                </div>
                                                <div class="form-group">
                                                      <label class="col-md-5 Control-label"
                                                            style="font-weight: normal;">Status</label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:select id="enquiryStatus" list="statusList"
                                                                  listKey="key" listValue="value"
                                                                  headerValue="<-------------------->" headerKey=""
                                                                  name="fwdContractVO.status"
                                                                  style="width: 200px; height: 25px" cssClass="chosen">
                                                            </boe:select>
                                                      </div>
                                                </div>
                                          </div>
                                          <div class="col-md-6">
                                                <div class="form-group">
                                                      <label class="col-md-5 Control-label"
                                                            style="font-weight: normal;"> Sub Product</label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:select id="subProduct" list="subProductList"
                                                                  listKey="key" listValue="value"
                                                                  headerValue="<-------------------->" headerKey=""
                                                                  name="fwdContractVO.subProduct"
                                                                  style="width: 200px; height: 25px" cssClass="chosen">
                                                            </boe:select>
                                                      </div>
                                                </div>
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
                                                            <label style="font-weight: normal;"> Deal Currency<span
                                                                  class="searchBtn"
                                                                  style="color: #579EDC; font-weight: bold; cursor: pointer;"
                                                                  onclick="currencyStaticDetails()"> <img
                                                                        src="images/magnify.png" width="13%" height="13%" /></span></label>
                                                      </div>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="dealCurrency"
                                                                  name="fwdContractVO.dealCurrency" readonly="false"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                <div class="form-group">
                                                      <div class="col-md-5 Control-label">
                                                            <label style="font-weight: normal;"> Forward Contract
                                                                  Amount</label>
                                                      </div>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <boe:textfield id="fwdContractAmt" placeholder="Eg:1000 USD"
                                                                  name="fwdContractVO.fwdContractAmt"
                                                                  cssClass="form-control text_box" onblur="getValue()" />
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
                                                                                    <th style="text-align: center; width: 100px;"><label>ID</label></th>

                                                                                    <th style="text-align: center; width: 100px;"><label>Action</label></th>

                                                                                    <th style="text-align: center; width: 100px;"><label>Forward
                                                                                                Contract Number</label></th>

                                                                                    <th style="text-align: center; width: 100px;"><label>Category</label></th>

                                                                                    <th style="text-align: center; width: 100px;"><label>Sub
                                                                                                Product</label></th>

                                                                                    <th style="text-align: center; width: 100px;"><label>Customer
                                                                                                ID</label></th>

                                                                                    <th style="text-align: center; width: 100px;"><label>Branch
                                                                                    </label></th>

                                                                                    <th style="text-align: center; width: 100px;"><label>Account
                                                                                                Number</label></th>

                                                                                    <th style="text-align: center; width: 100px;"><label>Deal
                                                                                                Currency</label></th>

                                                                                    <th style="text-align: center; width: 100px;"><label>Forward
                                                                                                Contract Amount</label></th>
                                                                                    <th style="text-align: center; width: 100px;"><label>Booking
                                                                                                Date</label></th>
                                                                                    <!-- <th style="text-align: center; width: 100px;"><label>To
                                                                                                Currency</label></th> -->
                                                                                    <th style="text-align: center; width: 100px;"><label>To
                                                                                                Amount</label></th>
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
                                                                                    <th style="text-align: center; width: 100px;"><label>Status</label></th>



                                                                              </tr>


                                                                              <c:if test="${empty forwardContractList}">

                                                                                    <td colspan="18">No records found</td>

                                                                              </c:if>
                                                                              <c:if test="${not empty forwardContractList}">
                                                                                    <boe:iterator value="forwardContractList"
                                                                                          id="forwardContractList">

                                                                                          <tr class="tableList">
                                                                                                <!--  ondblclick="enquiryProcessDetails(this)"> -->

                                                                                                <!-- <td width="20%" style="text-align: left;"> -->
                                                                                                <td style="text-align: center;">
                                                                                                      <div class="form-group"
                                                                                                            style="width: 30px; margin: 5px">
                                                                                                            <boe:property value="id" />
                                                                                                      </div>
                                                                                                </td>
                                                                                                <td>
                                                                                                      <div class="form-group">
                                                                                                            <input type="button" class="viewButton" value="View"
                                                                                                                  style="width: 50px; margin: 5px" />
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
                                                                                                            <boe:property value="subProduct" />

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
                                                                                                            <boe:property value="acctNumber" />

                                                                                                            <div class="col-md-2 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>
                                                                                                <td>
                                                                                                      <div class="form-group" align="center">
                                                                                                            <boe:property value="dealCurrency" />

                                                                                                            <div class="col-md-2 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>
                                                                                                <td>
                                                                                                      <div class="form-group" align="center">
                                                                                                            <boe:property value="fwdContractAmt" />

                                                                                                            <div class="col-md-2 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>

                                                                                                <td>
                                                                                                      <div class="form-group" align="center">
                                                                                                            <boe:property value="bookingDate" />

                                                                                                            <div class="col-md-2 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>

                                                                                                <%-- <td>
                                                                                                      <div class="form-group" align="center">
                                                                                                            <boe:property value="toCurrency" />

                                                                                                            <div class="col-md-2 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td> --%>

                                                                                                <td>
                                                                                                      <div class="form-group" align="center">
                                                                                                            <boe:property value="toCurrencyAmt" />

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
                                                                                                            <boe:property value="status" />

                                                                                                            <div class="col-md-2 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>

                                                                                                <%--
                                                                                                <td style="text-align: center;">
                                                                                                      <div class="form-group">
                                                                                                            <boe:checkbox name="chkList"
                                                                                                                  fieldValue="%{#forwardContractList.exportOrderNumber+':'+#forwardContractList.cifID+':'+#forwardContractList.poValue}">
                                                                                                            </boe:checkbox>
                                                                                                            <div class="col-md-2 input-group input-group-md"></div>
                                                                                                      </div>

                                                                                                </td> --%>

                                                                                          </tr>
                                                                                    </boe:iterator>
                                                                              </c:if>
                                                                        </tbody>
                                                                  </table>
                                                                  <br /> <br />

                                                            </div>
                                                      </div>
                                                      <%-- <div align="center" style="margin-top: 20px;">

                                                            <label style="margin-right: 90px;">Remarks</label>
                                                            <div class="form-group" align="center">
                                                                  <boe:textarea name="remarks" id="remarks" cols="20" rows="5"></boe:textarea>

                                                                  <div class="col-md-2 input-group input-group-md"></div>
                                                            </div>
                                                      </div> --%>
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