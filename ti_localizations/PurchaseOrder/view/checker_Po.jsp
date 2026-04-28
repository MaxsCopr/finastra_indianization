<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="boe" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>Checker Bill of Entry</title>

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
<script type="text/javascript" src="js/chargeSchedule.js"></script>

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
<script type="text/javascript" src="js/chargeSchedule.js"></script>
<link rel="stylesheet" href="css/commonTiplus.css"></link>
<link type="text/css" rel="stylesheet" href="boe/styles/manypayment.css" />
 -->

<!--[if IE 7]>
   <link rel="stylesheet" type="text/css" href="css/styleie7.css" />
 <![endif]-->
<!--  TODO changes starts here -->
<script>
      $(document).ready(function() {
            $("#fromDate").datepicker({
                  dateFormat : 'dd/mm/yy'
            });
            $("#toDate").datepicker({
                  dateFormat : 'dd/mm/yy'
            });

      });
      function fetchPO() {
            $("#myForm").attr("action", "checkerProcess");
            $("#myForm").submit();
      }
</script>

<script>
      function checkerProcessDetails(event) {
            $(".checkerList").removeClass("highlighted");
            $(event).addClass("highlighted");
            var value1 = $(event).find("td").eq(0).text().trim();
            var value2 = $(event).find("td").eq(2).text().trim();

            var enquiryValue = value1 + ':' + value2

            $.ajaxSetup({
                  async : false
            });
            $('#poNocifID').val(enquiryValue);

            var poNoCIF = $('#poNocifID').val();

            if (poNoCIF != '') {
                  $('#myForm').attr('action', 'checkerProcessDataView');
                  $('#myForm').submit();
            }

      }
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
      <img src="images/FusionBanking.png" width="100%" />
      <boe:form method="post" id="myForm" name="form">


            <div class="row">
                  <div class="col-md-2">
                        <div class="side_nav" style="width: 200px;">
                              <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;"><a href="home">Close</a></li>
                              </ul>
                        </div>
                        <br />
                        <div class="side_nav">
                              <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;"><a href="#" id="ok">Approve</a></li>
                                    <li style="text-align: center;"><a href="#" id="reject">Reject</a></li>
                              </ul>
                        </div>
                        <br />

                  </div>

                  <div class="col-md-10 content_box">

                        <h5 class="row fontcol" style="color: #527BB8">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                              Purchase Order - Checker Process</h5>
                        <br /> <br />
                        <div id="userIdMessage" style="color: #527BB8"></div>
                        <div class="row cont_colaps">

                              <!-- starts here -->
                              <div class="row page_content">
                                    <!--  TODO changes starts here -->
                                    <div class="col-md-12">
                                          <div class="col-md-5">
                                                <div class="form-group">
                                                      <label class="col-md-5 Control-label"
                                                            style="font-weight: normal;">PO Number</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <boe:textfield name="chargeVO.poNo" id="poNumber"
                                                            style="width: 200px" class="form-control text_box" />
                                                            <!-- <input type="text" name="chargeVO.poNo" value="" id="poNumber"
                                                                  class="form-control text_box" /> -->      <%-- onBlur="checkkey()" --%>
                                                      </div>
                                                </div>
                                          </div>
                                          <div class="col-md-5">
                                                <div class="form-group">
                                                      <label class="col-md-5 Control-label"
                                                            style="font-weight: normal;">CIF ID</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <boe:textfield name="chargeVO.poCif" id="poCif"
                                                                  style="width: 200px" class="form-control text_box" />
                                                            <!-- <input type="text" name="chargeVO.poCif" value="" id="poCif"
                                                                  class="form-control text_box" /> -->
                                                      </div>
                                                </div>
                                          </div>
                                    </div>
                                    <div class="col-md-12">
                                          <div class="col-md-5">
                                                <div class="form-group">
                                                      <label class="col-md-5 Control-label"
                                                            style="font-weight: normal;">Amount</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <boe:textfield name="chargeVO.poAmtValue" id="poAmtValue"
                                                                  style="width: 200px" class="form-control text_box" />
                                                            <!-- <input type="text" name="chargeVO.poAmtValue" value=""
                                                                  id="poAmtValue" class="form-control text_box" />-->
                                                      </div>
                                                </div>
                                          </div>
                                          <div class="col-md-5">
                                                <div class="form-group">
                                                      <label class="col-md-5 Control-label"
                                                            style="font-weight: normal;">Beneficiary Name</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <boe:textfield name="chargeVO.beneficiaryName"
                                                                  id="beneficiaryName" style="width: 200px"
                                                                  class="form-control text_box" />
                                                            <!-- <input type="text" name="chargeVO.beneficiaryName" value=""
                                                                  id="beneficiaryName" class="form-control text_box" /> -->
                                                      </div>
                                                </div>
                                          </div>

                                    </div>

                                    <div class="col-md-12">
                                          <div class="col-md-5">
                                                <div class="form-group">
                                                      <label class="col-md-5 Control-label"
                                                            style="font-weight: normal;">From Date</label>
                                                      <div class="col-md-4 input-group input-group-md">


                                                            <boe:textfield id="fromDate" name="chargeVO.fromDate"
                                                                  readonly="true" cssClass="datepicker form-control text_box" />
                                                      </div>
                                                </div>
                                          </div>
                                          <div class="col-md-5">
                                                <div class="form-group">
                                                      <label class="col-md-5 Control-label"
                                                            style="font-weight: normal;">To Date</label>
                                                      <div class="col-md-4 input-group input-group-md">

                                                            <boe:textfield id="toDate" name="chargeVO.toDate"
                                                                  readonly="true" cssClass="datepicker form-control text_box" />
                                                      </div>
                                                </div>
                                          </div>

                                    </div>

                                    <div class="col-md-12">
                                          <div class="col-md-5">
                                                <div class="form-group">
                                                      <label class="col-md-5 Control-label"
                                                            style="font-weight: normal;"></label>
                                                      <div class="col-md-4 input-group input-group-md">

                                                            <!-- <input type="button" value="Search" class="button" id="fetchPO" onclick="fetchPO()" style="margin-top: 5px;" /> -->
                                                            <input style="width: 50px" type="button" value="Search"
                                                                  class="button" onclick="fetchPO()" />

                                                      </div>
                                                </div>
                                          </div>
                                          <br /> <br /> <br /> <br />
                                    </div>
                                    <!--  TODO changes End here -->

                              </div>
                              <!-- ends here -->

                              <div class="row page_content">
                                    <div class="col-md-12">
                                          <div class="col-md-12">

                                                <div class="col-md-12">
                                                      <div class="form-group">
                                                            <div class="table">


                                                                  <table border="1px" align="left" id="chargeScheduleList">
                                                                        <tbody>
                                                                              <tr>
                                                                                    <th style="text-align: left; width: 200px;"><label>EXPORT
                                                                                                ORDER NUMBER </label></th>

                                                                                    <th style="text-align: left; width: 200px;"><label>EXPORT
                                                                                                ORDER DATE </label></th>

                                                                                    <th style="text-align: left; width: 250px;"><label>CIF
                                                                                                ID</label></th>

                                                                                    <th style="text-align: left; width: 200px;"><label>PO
                                                                                                VALUE </label></th>

                                                                                    <th style="text-align: left; width: 200px;"><label>EXPIRY_DATE
                                                                                    </label></th>

                                                                                    <th style="text-align: left; width: 200px;"><label>LAST
                                                                                                SHIPMENT DATE </label></th>

                                                                                    <th style="text-align: left; width: 200px;"><label>ELIGIBLE
                                                                                                AMOUNT</label></th>


                                                                                    <th style="text-align: left; width: 200px;"><label>STATUS</label></th>


                                                                              </tr>


                                                                              <c:if test="${empty multiPaymentReferenceList}">

                                                                                    <td colspan="8">No records found</td>

                                                                              </c:if>
                                                                              <c:if test="${not empty multiPaymentReferenceList}">
                                                                                    <boe:iterator value="multiPaymentReferenceList"
                                                                                          id="multiPaymentReferenceList">

                                                                                          <tr class="checkerList"
                                                                                                ondblclick="checkerProcessDetails(this)">

                                                                                                <!-- <td width="20%" style="text-align: left;"> -->
                                                                                                <td style="text-align: left; width: 200px;">
                                                                                                      <div class="form-group">
                                                                                                            <boe:property value="exportOrderNumber" />


                                                                                                            <div class="col-md-8 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>
                                                                                                <td>
                                                                                                      <div class="form-group" align="left">
                                                                                                            <boe:property value="exporterOrderDate" />

                                                                                                            <div class="col-md-8 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>
                                                                                                <td>
                                                                                                      <div class="form-group" align="left">
                                                                                                            <boe:property value="cifID" />

                                                                                                            <div class="col-md-8 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>
                                                                                                <td>
                                                                                                      <div class="form-group" align="left">
                                                                                                            <boe:property value="poValue" />

                                                                                                            <div class="col-md-8 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>
                                                                                                <td>
                                                                                                      <div class="form-group" align="left">
                                                                                                            <boe:property value="exportexpiryDate" />

                                                                                                            <div class="col-md-8 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>
                                                                                                <td>
                                                                                                      <div class="form-group" align="left">
                                                                                                            <boe:property value="lastShipmentDate" />

                                                                                                            <div class="col-md-8 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>
                                                                                                <td>
                                                                                                      <div class="form-group" align="left">
                                                                                                            <boe:property value="eligibleAmount" />

                                                                                                            <div class="col-md-8 input-group input-group-md"></div>
                                                                                                      </div>
                                                                                                </td>



                                                                                                <td style="text-align: center;">
                                                                                                      <div class="form-group">
                                                                                                            <boe:checkbox name="chkList"
                                                                                                                  fieldValue="%{#multiPaymentReferenceList.exportOrderNumber+':'+#multiPaymentReferenceList.cifID+':'+#multiPaymentReferenceList.poValue}">
                                                                                                            </boe:checkbox>
                                                                                                            <div class="col-md-8 input-group input-group-md"></div>
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
                                                      <div align="left" style="margin-top: 20px;">

                                                            <label style="margin-right: 90px;">Remarks</label>
                                                            <div class="form-group" align="left">
                                                                  <boe:textarea name="remarks" id="remarks" cols="20" rows="5"></boe:textarea>

                                                                  <div class="col-md-8 input-group input-group-md"></div>
                                                            </div>
                                                      </div>
                                                      <input type="hidden" id="check" name="check" /> <input
                                                            type="hidden" id="poNocifID" name="poNocifID" />

                                                </div>
                                          </div>
                                    </div>
                              </div>
                              <br />
                        </div>
                  </div>
                  <div height="15px">&nbsp;</div>
                  <boe:token/>
      </boe:form>
      <div id="footer">
            <%@ include file="/view/includes/FOOTER.jsp"%>
      </div>
</body>
</html>