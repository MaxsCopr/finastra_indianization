<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="ebrc" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>Home Page</title>

<link rel="stylesheet" href="css/style.css" />
<link type="text/css" rel="stylesheet" href="css/bootstrap.css" />
<link type="text/css" rel="stylesheet" href="css/bootstrap-dropdown.css" />
<link type="text/css" rel="stylesheet" href="css/headfoot.css" />
<link rel="stylesheet" href="css/commonTiplus.css"></link>
<link href="css/font-awesome.css" rel="stylesheet" />
<link rel="stylesheet" href="css/jquery-ui.css"></link>

<script src="js/jquery-1.12.4.js" type="text/javascript"></script>
<script src="js/jquery-1.9.1.js"></script>
<script src="js/jquery-ui.js"></script>
<script src="js/bootstrap.js" type="text/javascript"></script>
<script type="text/javascript" src="js/macxdnie81.js"></script>
<script type="text/javascript" src="js/maxcdnie82.js"></script>
<script type="text/javascript" src="js/jquery.cookie.min.js"></script>
<script type="text/javascript" src="js/jquery.collapsible.min.js"></script>
<script type="text/javascript" src="js/highlight.pack.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/date.js"></script>
<script type="text/javascript" src="js/commonTiplus.js"></script>

<!--[if IE 7]>
      <link rel="stylesheet" type="text/css" href="css/styleie7.css" />
<![endif]-->
 
<script>
      
      function getModel() {
            $('body').modal({
                  show : 'false'
            });
            $('body').removeClass('removePageLoad');
            $('body').addClass('addPageLoad');
      }

      function reload() {
            window.location.href = "home";
      }
      
      function select(event) {
            $(".customerlist").removeClass("highlighted");
            $(event).addClass("highlighted");
            var value = $(event).find("td").eq(2).text().trim();
            $('#custCIFNo').val(value);
            $("#ebrcform").attr("action", "editEbrcData");
            $("#ebrcform").submit();
      }
      
       $(document).ready(function() {           
            
                  $("#dateFrom").datepicker({
                        changeMonth: true,
                        changeYear: true,
                        dateFormat : 'yy-mm-dd'
                  });   
                  
                  $("#dateTo").datepicker({
                        changeMonth: true,
                        changeYear: true,
                        dateFormat : 'yy-mm-dd'
                  });   
            });
       $(document).ready(function() {
             $("#disp").show();
                $("#div1").delay(8000).hide(100, function() {
                });
                
            });
      $(document).ready(
                  function() {
                        $("#getEbrcDataView").click(function() {
                              $("#ebrcform").attr("action", "getEbrcValue");
                              $("#ebrcform").submit();
                        });
                        $("#generateEBRC").click(function() {
                              
                              $("#ebrcform").attr("action", "generateEBRCXml");
                              $("#ebrcform").submit();
                              
                        });
                        $("#generateIFSC").click(function() {
                              $("#disp").show();
                              $("#ebrcform").attr("action", "generateIFSCXml");
                              $("#ebrcform").submit();
                        });
                        $("#submit").click(
                                    function() {
                                          var getstatus = $('#statustocheck').val();
                                          $("#tempStatus").val(getstatus);
                                          if (getstatus == 'Downloaded' || getstatus == 'F'
                                                      || getstatus == 'CU' || getstatus == 'CD' || getstatus == '-1') {
                                                return false;
                                          } else {
                                                getModel();
                                                $("#ebrcform").attr("action", "storeEbrcData");
                                                $("#ebrcform").submit();
                                          }

                                    });
                  });   
      
</script>
<script>
      $(document).ready(
                  function() {

                        function func1() {
                              var getstatus = $('#statustocheck').val();
                              var getbrcnum = $('#brcNumber').val();
                              if (getbrcnum != '') {
                                    if (getstatus == '000') {

                                    } else if (getstatus == 'E') {
                                          $("#statustocheck").attr("disabled", "disabled");
                                          $("#exportName").removeAttr("readonly");
                                          $("#shipPort").removeAttr("readonly");
                                          $("#ieCodeValue").removeAttr("readonly");
                                          $("#rmtBank").removeAttr("readonly");
                                          $("#rmtCity").removeAttr("readonly");
                                          $("#rmtCountry").removeAttr("readonly");
                                    } else if (getstatus == 'CS') {
                                          $("#statustocheck").attr("disabled", "disabled");
                                          $("#exportName").removeAttr("readonly");
                                          $("#shipPort").removeAttr("readonly");
                                          $("#ieCodeValue").removeAttr("readonly");
                                          $("#rmtBank").removeAttr("readonly");
                                          $("#rmtCity").removeAttr("readonly");
                                          $("#rmtCountry").removeAttr("readonly");
                                    } else if (getstatus == 'Downloaded'
                                                || getstatus == 'F' || getstatus == 'CU'
                                                || getstatus == 'CD' || getstatus == 'CS') {
                                          $("#statustocheck").attr("disabled", "disabled");
                                    }
                              } else {
                                    $('#statustocheck').val('-1');
                              }
                        }
                        window.onload = func1;

                  });
</script>
</head>

<body class="body_bg" onload="display_ct()">
      <%@ include file="/view/includes/TITLE.jsp"%>

      <ebrc:form method="post" id="ebrcform" name="form">
            <div class="row">
                  <div class="col-md-2">
                        <div class="side_nav" style="width: 200px;">
                              <ul class="nav nav-pills nav-stacked">                
                              
                              <li style="text-align: center;"><a href="close">Close</a></li>                
                                     <!-- <li style="text-align: center;"><a href="https://10.10.20.183:443/tiplus2-global">Close</a></li>       -->  
                                    <!--  <li style="text-align: center;"><a href="https://10.10.20.165:443/tiplus2-global">Close</a></li> -->  
                              </ul>
                        </div>
                        <br />
                        <div class="side_nav">
                              <ul class="nav nav-pills nav-stacked">
                                    <li><a href="#" id="submit" style="text-align: center;"><ebrc:text
                                                      name="Ok" /></a></li>
                                    <li><a href="#" onclick="reload()"
                                          style="text-align: center;"><ebrc:text name="Cancel" /></a></li>
                                    <li><a href="#" id="generateEBRC" style="text-align: center;">Generate
                                                EBRC </a></li>
                                    <!-- <li><a href="#" id="generateIFSC" style="text-align: center;">Generate
                                                IFSC </a></li> -->
                              </ul>
                        </div>
                        <br />
                  </div>

                  <div class="col-md-10 content_box">

                        <h5 class="row fontcol">
                              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                              <ebrc:text name="EBRC Process" />
                        </h5>
                  <div align="center" id="div1">
                              <ebrc:if test="hasActionErrors()" >
                                    <div class="error" align="left" style="display: none;font-weight: bold;font-size: 14px; color: Blue;" id="disp">
                                          <ebrc:actionerror />
                                    
                                    </div>
                              </ebrc:if>
                              <ebrc:if test="hasActionMessages()">
                                    <div class="welcome" align="left" style="display: none;font-weight: bold;font-size: 14px; color: Blue;" id="disp">
                                          <ebrc:actionmessage />
                                    </div>
                              </ebrc:if>
                        </div>
                  <%--  <table align="center">
                              <tr>
                                    <td><ebrc:actionmessage id="messages"></ebrc:actionmessage></td>
                              </tr>
                        </table> --%>
                        <!-- <div align="left" style="display: none;" id="disp">
                              <p style="font-weight: bold; font-size: 14px; color: Blue;">
                                    PLEASE WAIT - XML Files Download</p>
                        </div> -->
                        <div class="row page_content">
                              <div class="col-md-12">
                                    <div class="page_collapsible" id="body-section1">
                                          <span></span>
                                          <h5 style="font-weight: bold; font-size: 13px;">
                                                &nbsp;
                                                <ebrc:text name="Errors and warning messages" />
                                          </h5>
                                    </div>
                                    <div class="form-group">
                                          <div class="table">
                                                <table border="1px" align="left" id="errorList">
                                                      <tbody>
                                                            <tr>
                                                                  <th style="text-align: left; width: 200px;"><label><ebrc:text
                                                                                    name="Severity" /></label></th>

                                                                  <th style="text-align: left; width: 200px;"><label><ebrc:text
                                                                                    name="Description" /></label></th>

                                                                  <th style="text-align: left; width: 200px;"><label><ebrc:text
                                                                                    name="Steps" /></label></th>

                                                                  <th style="text-align: left; width: 300px;"><label><ebrc:text
                                                                                    name="Details" /></label></th>

                                                                  <th style="text-align: left; width: 300px;"><label><ebrc:text
                                                                                    name="Overridden" /></label></th>
                                                            </tr>

                                                            <ebrc:iterator value="errorList" id="errorList">
                                                                  <tr>
                                                                        <td style="background-color: #a00000;">
                                                                              <div class="form-group" align="left" style="color: white;">
                                                                                    <ebrc:property value="errorId" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>

                                                                        </td>
                                                                        <td style="background-color: #a00000;">
                                                                              <div class="form-group" align="left" style="color: white;">
                                                                                    <ebrc:property value="errorDesc" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>

                                                                        </td>
                                                                        <td style="background-color: #a00000;">
                                                                              <div class="form-group" align="left" style="color: white;">
                                                                                    <ebrc:property value="errorCode" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="background-color: #a00000;">
                                                                              <div class="form-group" align="left" style="color: white;">
                                                                                    <ebrc:property value="errorDetails" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="background-color: #a00000;">
                                                                              <div class="form-group" align="left" style="color: white;">
                                                                                    <ebrc:property value="errorMsg" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                  </tr>
                                                            </ebrc:iterator>
                                                      </tbody>
                                                </table>
                                          </div>
                                    </div>
                              </div>
                        </div>
                        <div id="userIdMessage" style="color: orange"></div>
                        <div class="row cont_colaps">
                              <div class="col-md-12">
                                    <div class="page_collapsible " id="body-section1">
                                          <span></span>
                                          <h5 style="font-weight: bold; font-size: 13px;">
                                                &nbsp;
                                                <ebrc:text name="Bank Realisation" />
                                          </h5>
                                    </div>
                                    <div class="row page_content">
                                          <div class="col-md-6">
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><ebrc:text
                                                                  name="Bill Reference Number" /></label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <ebrc:textfield id="billRefNo" name="ebrcVO.billRefNumber"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                </div>

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><ebrc:text
                                                                  name="BRC Number" /></label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <ebrc:textfield id="brcNo" name="ebrcVO.brcNumber"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                </div>

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">From Date</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                                  <ebrc:textfield id="dateFrom" name="ebrcVO.fromDate"
                                                                  cssClass="form-control text_box" readonly="true" />                                                               
                                                      </div>
                                                </div>

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><ebrc:text
                                                                  name="Status Of BRC" /></label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <ebrc:select id="brcStatus" name="ebrcVO.ebrcStatus"
                                                                  list="ebrcStatus" listKey="key" listValue="value"
                                                                  headerValue="<----->" headerKey="-1"
                                                                  style="width: 180px; height: 25px">
                                                            </ebrc:select>

                                                            <br /> <br />
                                                            
                                                      </div>
                                                </div>
                                          </div>

                                          <div class="col-md-6">
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><ebrc:text
                                                                  name="Event Reference Number" /></label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <ebrc:textfield id="eventRefNo" name="ebrcVO.eventRefNumber"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><ebrc:text name="IE Code" /></label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <ebrc:textfield id="ieCode" name="ebrcVO.ebrcIECode"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">To Date</label>                                                    
                                                            <div class="col-md-4 input-group input-group-md">
                                                                  <ebrc:textfield id="dateTo" name="ebrcVO.toDate"
                                                                        cssClass="form-control text_box" readonly="true"  />
                                                                  
                                                            </div>
                                                </div>
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"></label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <input type="button" value=" Search "
                                                                  class="button" id="getEbrcDataView" />
                                                      </div>
                                                </div>
                                          </div>
                                    </div>

                                    <div class="col-md-12">
                                          <div class="row page_content">
                                                <div class="page_collapsible" id="body-section1">
                                                      <span></span>
                                                      <h5 style="font-weight: bold; font-size: 13px;">
                                                            &nbsp;
                                                            <ebrc:text name="EBRC Details" />
                                                      </h5>
                                                </div>
                                                <div class="form-group">
                                                      <div class="table">
                                                            <table border="1px" align="left" id="tiPlusList">
                                                                  <tbody>
                                                                        <tr>
                                                                              
                                                                              <th
                                                                                    style="text-align: left; width: 100px; padding: 2px 4px;"><label><ebrc:text
                                                                                                name="Bill Reference Number" /></label></th>

                                                                              <th
                                                                                    style="text-align: left; width: 100px; padding: 2px 4px;"><label><ebrc:text
                                                                                                name="Event Reference Number" /></label></th>

                                                                              <th
                                                                                    style="text-align: left; width: 100px; padding: 2px 4px;"><label><ebrc:text
                                                                                                name="BRC No" /></label></th>

                                                                              <th
                                                                                    style="text-align: left; width: 100px; padding: 2px 4px;"><label><ebrc:text
                                                                                                name="BRC Date" /></label></th>

                                                                              <th
                                                                                    style="text-align: left; width: 100px; padding: 2px 4px;"><label><ebrc:text
                                                                                                name="Status Of BRC" /></label></th>

                                                                              <th
                                                                                    style="text-align: left; width: 100px; padding: 2px 4px;"><label><ebrc:text
                                                                                                name="IE Code" /></label></th>

                                                                              <th
                                                                                    style="text-align: left; width: 100px; padding: 2px 4px;"><label><ebrc:text
                                                                                                name="Exporter Name" /></label></th>

                                                                              <th
                                                                                    style="text-align: left; width: 100px; padding: 2px 4px;"><label><ebrc:text
                                                                                                name="IFSC Code" /></label></th>

                                                                              <th
                                                                                    style="text-align: left; width: 100px; padding: 2px 4px;"><label><ebrc:text
                                                                                                name="Bill Id" /></label></th>

                                                                              <th
                                                                                    style="text-align: left; width: 100px; padding: 2px 4px;"><label><ebrc:text
                                                                                                name="Shipping Bill No" /></label></th>

                                                                              <th
                                                                                    style="text-align: left; width: 100px; padding: 2px 4px;"><label><ebrc:text
                                                                                                name="Shipping Bill Port" /></label></th>

                                                                              <th
                                                                                    style="text-align: left; width: 100px; padding: 2px 4px;"><label><ebrc:text
                                                                                                name="Shipping Bill Date" /></label></th>

                                                                              <th
                                                                                    style="text-align: left; width: 100px; padding: 2px 4px;"><label><ebrc:text
                                                                                                name="Shipping Bill Curr Code" /></label></th>

                                                                              <th
                                                                                    style="text-align: left; width: 100px; padding: 2px 4px;"><label><ebrc:text
                                                                                                name="Shipping Bill Value" /></label></th>

                                                                              <th
                                                                                    style="text-align: left; width: 100px; padding: 2px 4px;"><label><ebrc:text
                                                                                                name="Realisation Currency" /></label></th>

                                                                              <th
                                                                                    style="text-align: left; width: 100px; padding: 2px 4px;"><label><ebrc:text
                                                                                                name="Total Realised Value" /></label></th>

                                                                              <th
                                                                                    style="text-align: left; width: 100px; padding: 2px 4px;"><label><ebrc:text
                                                                                                name="Realisation Date" /></label></th>

                                                                              <th
                                                                                    style="text-align: left; width: 100px; padding: 2px 4px;"><label><ebrc:text
                                                                                                name="Total Realisation in INR" /></label></th>

                                                                              <th
                                                                                    style="text-align: left; width: 100px; padding: 2px 4px;"><label><ebrc:text
                                                                                                name="Name of Remitting bank" /></label></th>

                                                                              <th
                                                                                    style="text-align: left; width: 100px; padding: 2px 4px;"><label><ebrc:text
                                                                                                name="City of Remitting bank" /></label></th>

                                                                              <th
                                                                                    style="text-align: left; width: 100px; padding: 2px 4px;"><label><ebrc:text
                                                                                                name="Country of Remitting bank" /></label></th>

                                                                        </tr>

                                                                        <c:if test="${empty tiList}">
                                                                              <tr>
                                                                                    <td colspan="21">No records found</td>
                                                                              </tr>
                                                                        </c:if>
                                                                        <c:if test="${not empty tiList}">
                                                                              <ebrc:iterator value="tiList" id="tiList">
                                                                                    <tr class="customerlist" ondblclick="select(this)">
                                                                                          <td style="padding: 2px 4px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <ebrc:property value="billRefNumber" />
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>
                                                                                          <td style="padding: 2px 4px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <ebrc:property value="eventRefNumber" />
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>
                                                                                          <td style="padding: 2px 4px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <ebrc:property value="brcNo" />
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>
                                                                                          <td style="padding: 2px 4px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <ebrc:property value="brcDate" />
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>
                                                                                          <td style="padding: 2px 4px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <ebrc:property value="status" />
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>
                                                                                          <td style="padding: 2px 4px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <ebrc:property value="ieCode" />
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>

                                                                                          <td style="padding: 2px 4px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <ebrc:property value="exportName" />
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>

                                                                                          </td>
                                                                                          <td style="padding: 2px 4px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <ebrc:property value="codeIFSC" />
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>
                                                                                          <td style="padding: 2px 4px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <ebrc:property value="billID" />
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>
                                                                                          <td style="padding: 2px 4px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <ebrc:property value="shipNo" />
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>

                                                                                          <td style="padding: 2px 4px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <ebrc:property value="shipPort" />
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>

                                                                                          <td style="padding: 2px 4px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <ebrc:property value="shipDate" />
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>

                                                                                          <td style="padding: 2px 4px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <ebrc:property value="shipCurr" />
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>

                                                                                          <td style="padding: 2px 4px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <ebrc:property value="shipValue" />
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>

                                                                                          <td style="padding: 2px 4px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <ebrc:property value="realCurr" />
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>
                                                                                          <td style="padding: 2px 4px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <ebrc:property value="realValue" />
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>
                                                                                          <td style="padding: 2px 4px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <ebrc:property value="realDate" />
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>
                                                                                          <td style="padding: 2px 4px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <ebrc:property value="totalRealValue" />
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>
                                                                                          <td style="padding: 2px 4px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <ebrc:property value="rmtBank" />
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>
                                                                                          <td style="padding: 2px 4px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <ebrc:property value="rmtCity" />
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>
                                                                                          <td style="padding: 2px 4px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <ebrc:property value="rmtCountry" />
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>

                                                                                    </tr>
                                                                              </ebrc:iterator>
                                                                        </c:if>
                                                                  </tbody>
                                                            </table>
                                                      </div>
                                                </div>
                                          </div>
                                          <div class="row page_content">
                                                <div class="page_collapsible " id="body-section1">
                                                      <span></span>
                                                      <h5 style="font-weight: bold; font-size: 13px;">
                                                            &nbsp;
                                                            <ebrc:text name="Bank Realisation Details" />
                                                      </h5>
                                                </div>
                                                <div class="row page_content">
                                                      <div class="col-md-6">

                                                            <div class="form-group">
                                                                  <label class="col-md-4 Control-label"
                                                                        style="font-weight: normal;"><ebrc:text
                                                                              name="Bill Reference Number" /></label>
                                                                  <div class="col-md-4 input-group input-group-md">
                                                                        <ebrc:textfield id="billRefNunmber" name="ebrcVO.billRefNo"
                                                                              cssClass="form-control text_box" readonly="true" />
                                                                  </div>
                                                            </div>

                                                            <div class="form-group">
                                                                  <label class="col-md-4 Control-label"
                                                                        style="font-weight: normal;"><ebrc:text
                                                                              name="RBI Reference Number" /></label>
                                                                  <div class="col-md-4 input-group input-group-md">
                                                                        <ebrc:textfield id="rbiRefNumber" name="ebrcVO.rbiRefNo"
                                                                              cssClass="form-control text_box" readonly="true" />
                                                                  </div>
                                                            </div>

                                                            <div class="form-group">
                                                                  <label class="col-md-4 Control-label"
                                                                        style="font-weight: normal;"><ebrc:text
                                                                              name="BRC Number" /></label>
                                                                  <div class="col-md-4 input-group input-group-md">
                                                                        <ebrc:textfield id="brcNumber" name="ebrcVO.brcNo"
                                                                              cssClass="form-control text_box" readonly="true" />
                                                                  </div>
                                                            </div>

                                                            <div class="form-group">
                                                                  <label class="col-md-4 Control-label"
                                                                        style="font-weight: normal;"><ebrc:text
                                                                              name="BRC Date" /></label>
                                                                  <div class="col-md-4 input-group input-group-md">
                                                                        <ebrc:textfield id="brcDate" name="ebrcVO.brcDate"
                                                                              cssClass="form-control text_box" readonly="true" />                                                                     
                                                                  </div>
                                                            </div>

                                                            <div class="form-group">
                                                                  <label class="col-md-4 Control-label"
                                                                        style="font-weight: normal;"><ebrc:text
                                                                              name="Status of BRC" /></label>
                                                                  <div class="col-md-4 input-group input-group-md">

                                                                        <ebrc:if test="%{#session.count == 0}">
                                                                              <ebrc:select id="statustocheck" name="ebrcVO.status"
                                                                                    list="ebrcStatus1" listKey="key" listValue="value"
                                                                                    onChange="getStatus()" headerValue="<----->"
                                                                                    headerKey="-1" style="width: 180px; height: 25px">
                                                                              </ebrc:select>
                                                                        </ebrc:if>
                                                                        <ebrc:else>
                                                                              <ebrc:select id="statustocheck" name="ebrcVO.status"
                                                                                    list="ebrcStatus" listKey="key" listValue="value"
                                                                                    onChange="getStatus()" headerValue="<----->"
                                                                                    headerKey="-1" style="width: 180px; height: 25px">
                                                                              </ebrc:select>
                                                                        </ebrc:else>

                                                                  </div>
                                                            </div>

                                                            <div class="form-group">
                                                                  <label class="col-md-4 Control-label"
                                                                        style="font-weight: normal;"><ebrc:text
                                                                              name="IE Code" /></label>
                                                                  <div class="col-md-4 input-group input-group-md">
                                                                        <ebrc:textfield id="ieCodeValue" name="ebrcVO.ieCode"
                                                                              cssClass="form-control text_box" readonly="true" />
                                                                  </div>
                                                            </div>
                                                            <div class="form-group">
                                                                  <label class="col-md-4 Control-label"
                                                                        style="font-weight: normal;"><ebrc:text
                                                                              name="Exporter Name" /></label>
                                                                  <div class="col-md-4 input-group input-group-md">
                                                                        <ebrc:textfield id="exportName" name="ebrcVO.exportName"
                                                                              cssClass="form-control text_box" readonly="true" />
                                                                  </div>
                                                            </div>

                                                            <div class="form-group">
                                                                  <label class="col-md-4 Control-label"
                                                                        style="font-weight: normal;"><ebrc:text
                                                                              name="IFSC Code" /></label>
                                                                  <div class="col-md-4 input-group input-group-md">
                                                                        <ebrc:textfield id="codeIFSC" name="ebrcVO.codeIFSC"
                                                                              cssClass="form-control text_box" readonly="true" />
                                                                  </div>
                                                            </div>
                                                            <div class="form-group">
                                                                  <label class="col-md-4 Control-label"
                                                                        style="font-weight: normal;"><ebrc:text
                                                                              name="Shipping Bill No" /></label>
                                                                  <div class="col-md-4 input-group input-group-md">
                                                                        <ebrc:textfield id="shipNo" name="ebrcVO.shipNo"
                                                                              cssClass="form-control text_box" readonly="true" />
                                                                  </div>
                                                            </div>

                                                            <div class="form-group">
                                                                  <label class="col-md-4 Control-label"
                                                                        style="font-weight: normal;"><ebrc:text
                                                                              name="Shipping Bill Port" /></label>
                                                                  <div class="col-md-4 input-group input-group-md">
                                                                        <ebrc:textfield id="shipPort" name="ebrcVO.shipPort"
                                                                              cssClass="form-control text_box" readonly="true" />
                                                                  </div>
                                                            </div>
                                                            <div class="form-group">
                                                                  <label class="col-md-4 Control-label"
                                                                        style="font-weight: normal;"><ebrc:text
                                                                              name="Shipping Bill Curr Code" /></label>
                                                                  <div class="col-md-4 input-group input-group-md">
                                                                        <ebrc:textfield id="shipCurr" name="ebrcVO.shipCurr"
                                                                              cssClass="form-control text_box" readonly="true" />
                                                                  </div>
                                                            </div>


                                                      </div>

                                                      <div class="col-md-6">
                                                            <div class="form-group">
                                                                  <label class="col-md-4 Control-label"
                                                                        style="font-weight: normal;"><ebrc:text
                                                                              name="Event Reference Number" /></label>
                                                                  <div class="col-md-4 input-group input-group-md">
                                                                        <ebrc:textfield id="eventRefNumber" name="ebrcVO.eventRefNo"
                                                                              cssClass="form-control text_box" readonly="true" />
                                                                  </div>
                                                            </div>
                                                            <div class="form-group">
                                                                  <label class="col-md-4 Control-label"
                                                                        style="font-weight: normal;">Shipping Bill Date</label>

                                                                  <div class="col-md-4 input-group input-group-md">
                                                                        <ebrc:textfield id="shipDate" name="ebrcVO.shipDate"
                                                                              cssClass="form-control text_box" readonly="true" />
                                                                  </div>
                                                            </div>
                                                            <div class="form-group">
                                                                  <label class="col-md-4 Control-label"
                                                                        style="font-weight: normal;"><ebrc:text
                                                                              name="Shipping Bill Value" /></label>
                                                                  <div class="col-md-4 input-group input-group-md">
                                                                        <ebrc:textfield id="shipValue" name="ebrcVO.shipValue"
                                                                              cssClass="form-control text_box" readonly="true" />
                                                                  </div>
                                                            </div>
                                                            <div class="form-group">
                                                                  <label class="col-md-4 Control-label"
                                                                        style="font-weight: normal;"><ebrc:text
                                                                              name="Realisation Currency" /></label>
                                                                  <div class="col-md-4 input-group input-group-md">
                                                                        <ebrc:textfield id="realCurr" name="ebrcVO.realCurr"
                                                                              cssClass="form-control text_box" readonly="true" />
                                                                  </div>
                                                            </div>
                                                            <div class="form-group">
                                                                  <label class="col-md-4 Control-label"
                                                                        style="font-weight: normal;"><ebrc:text
                                                                              name="Total Realised Value" /></label>
                                                                  <div class="col-md-4 input-group input-group-md">
                                                                        <ebrc:textfield id="realValue" name="ebrcVO.realValue"
                                                                              cssClass="form-control text_box" readonly="true" />
                                                                  </div>
                                                            </div>
                                                            <div class="form-group">
                                                                  <label class="col-md-4 Control-label"
                                                                        style="font-weight: normal;">Realisation Date</label>

                                                                  <div class="col-md-4 input-group input-group-md">
                                                                        <ebrc:textfield id="realDate" name="ebrcVO.realDate"
                                                                              cssClass="form-control text_box" readonly="true" />
                                                                  </div>

                                                            </div>
                                                            <div class="form-group">
                                                                  <label class="col-md-4 Control-label"
                                                                        style="font-weight: normal;"><ebrc:text
                                                                              name="Total Realisation in INR" /></label>
                                                                  <div class="col-md-4 input-group input-group-md">
                                                                        <ebrc:textfield id="totalRealValue"
                                                                              name="ebrcVO.totalRealValue"
                                                                              cssClass="form-control text_box" readonly="true" />
                                                                  </div>
                                                            </div>
                                                            <div class="form-group">
                                                                  <label class="col-md-4 Control-label"
                                                                        style="font-weight: normal;"><ebrc:text
                                                                              name="Name of remitting bank" /></label>
                                                                  <div class="col-md-4 input-group input-group-md">
                                                                        <ebrc:textfield id="rmtBank" name="ebrcVO.rmtBank"
                                                                              cssClass="form-control text_box" readonly="true" />
                                                                  </div>
                                                            </div>
                                                            <div class="form-group">
                                                                  <label class="col-md-4 Control-label"
                                                                        style="font-weight: normal;"><ebrc:text
                                                                              name="City of remitting bank" /></label>
                                                                  <div class="col-md-4 input-group input-group-md">
                                                                        <ebrc:textfield id="rmtCity" name="ebrcVO.rmtCity"
                                                                              cssClass="form-control text_box" readonly="true" />
                                                                  </div>
                                                            </div>
                                                            <div class="form-group">
                                                                  <label class="col-md-4 Control-label"
                                                                        style="font-weight: normal;"><ebrc:text
                                                                              name="Country of remitting bank" /></label>
                                                                  <div class="col-md-4 input-group input-group-md">
                                                                        <ebrc:textfield id="rmtCountry" name="ebrcVO.rmtCountry"
                                                                              cssClass="form-control text_box" readonly="true" />
                                                                  </div>
                                                                  <ebrc:textfield id="custCIFNo" name="ebrcVO.brcNo1"
                                                                        cssClass="form-control text_box" style="display:none" />
                                                                  <ebrc:hidden name="ebrcVO.tempStatus" id="tempStatus" />
                                                            </div>

                                                            <div class="form-group">
                                                                  <label class="col-md-4 Control-label"
                                                                        style="font-weight: normal;">Error Description</label>
                                                                  <div class="col-md-4 input-group input-group-md">
                                                                        <ebrc:textarea name="ebrcVO.errorDesc" id="errorDesc"
                                                                              cols="30" rows="5" readonly="true" />
                                                                  </div>
                                                            </div>
                                                      </div>
                                                </div>
                                          </div>
                                    </div>
                              </div>
                        </div>
                        <br />
                        <br />
                  </div>
                  
            </div>
            <br />
            <br />
      </ebrc:form>
</body>
</html>