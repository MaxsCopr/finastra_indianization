<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="boe" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/199xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>Manual BOE</title>
<jsp:include page="/view/common/header.jsp" />
<script type="text/javascript" src="boe/scripts/manyBill/manualBOE.js"></script>
<!--  <script>
            $(document).ready(function () {
              $("#refreshBtn").click(function () {
                  //$("#dialog").dialog({modal: true, height: 590, width: 1005 });
                  var w = window.open("", "popupWindow", "width=600, height=400, scrollbars=yes");
                  var $w = $(w.document.body);
                  $w.html("<textarea></textarea>");
              });
          });
      
      </script> -->
<script>
function windowclose() {      
      top.close();      
}

</script>
</head>

<body class="body_bg">

      <%@ include file="/view/includes/TITLE.jsp"%>

      <boe:form method="post" id="formId" name="form">

            <div class="row">

                  <div class="col-md-2">

                        <div class="side_nav">
                              <!-- <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;"><a href="makerProcess">Close</a></li>
                              </ul> -->
                              <ul class="nav nav-pills nav-stacked">
                              <boe:if test="%{#session.xEvtRefNum.length() > 0}">
                                          <li style="text-align: center;"><a href="#" onclick="windowclose()">Close</a></li>
                                    </boe:if>         
                                    <boe:else>
                                          <li style="text-align: center;"><a href="makerProcess">Close</a></li>
                                    </boe:else>
                                    </ul>
                        </div>
                        <br />

                        <div class="side_nav">
                              <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;"><a href="#" id="okId">Submit
                                                BOE Details</a></li>
                                    <li style="text-align: center;"><a href="#" id="updateId">Update
                                                BOE Details</a></li>
                                    <li style="text-align: center;"><a href="#" id="deleteId">Delete
                                                BOE Details</a></li>
                                    <li style="text-align: center;"><a href="manualBOE"
                                          id="Reset">Reset</a></li>
                              </ul>
                        </div>
                        <br />

                  </div>

                  <div class="col-md-10 content_box">

                        <h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Manual
                              Bill Of Entry</h5>

                        <div id="userIdMessage" style="color: orange"></div>

                        <div class="row cont_colaps">

                              <!-- Error table begin -->

                              <div class="col-md-12">

                                    <div class="page_collapsible" id="body-section1">
                                          <span></span>
                                          <h5 style="font-weight: bold; font-size: 13px;">Error
                                                Description</h5>
                                    </div>

                                    <div class="form-group">

                                          <div align="center" id="div1">
                                                <boe:if test="hasActionErrors()">
                                                      <div class="errors">
                                                            <boe:actionerror />
                                                      </div>
                                                </boe:if>
                                                <boe:if test="hasActionMessages()">
                                                      <div class="welcome">
                                                            <boe:actionmessage />
                                                      </div>
                                                </boe:if>
                                          </div>
                                          <br />

                                          <div class="error_table">

                                                <table border="1px" align="left" id="errorAlert">
                                                      <tbody>
                                                            <tr>
                                                                  <th style="text-align: left; width: 200px;"><label><boe:text
                                                                                    name="label.boe.Severity" /></label></th>

                                                                  <th style="text-align: left; width: 200px;"><label><boe:text
                                                                                    name="label.boe.Description" /></label></th>

                                                                  <th style="text-align: left; width: 200px;"><label><boe:text
                                                                                    name="label.boe.Steps" /></label></th>

                                                                  <th style="text-align: left; width: 300px;"><label><boe:text
                                                                                    name="label.boe.Details" /></label></th>

                                                                  <th style="text-align: left; width: 200px;"><label><boe:text
                                                                                    name="label.boe.Overridden" /></label></th>
                                                            </tr>

                                                            <boe:iterator value="alertMsgArray" id="alertMsgArray">
                                                                  <tr>
                                                                        <td>
                                                                              <div class="form-group" align="left">
                                                                                    <boe:property value="errorId" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td>
                                                                              <div class="form-group" align="left">
                                                                                    <boe:property value="errorDesc" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td>
                                                                              <div class="form-group" align="left">
                                                                                    <boe:property value="errorCode" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td>
                                                                              <div class="form-group">
                                                                                    <boe:property value="errorDetails" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td>
                                                                              <div class="form-group" align="left">
                                                                                    <boe:property value="errorMsg" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                  </tr>
                                                            </boe:iterator>
                                                      </tbody>
                                                </table>
                                          </div>
                                    </div>
                                    <br />
                              </div>

                              <br />
                              <!-- Error table End -->


                              <!-- First Block is Started  -->

                              <div class="col-md-12">
                                    <div class="page_collapsible" id="body-section1">
                                          <span></span>
                                          <h5 style="font-weight: bold; font-size: 13px;">Bill Of
                                                Entry</h5>
                                    </div>

                                    <div class="row page_content">

                                          <div class="col-md-6">

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"> <boe:text
                                                                  name="label.boe.BillOfEntryNo" /></label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <boe:textfield id="billRefNo" name="boeVO.boeNo"
                                                                  onkeypress="return isNumber(event)" maxlength="7"
                                                                  cssClass="form-control text_box">
                                                            </boe:textfield>
                                                      </div>
                                                </div>

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"> <boe:text
                                                                  name="label.boe.BillofEntryDate" /></label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <boe:textfield id="billEntryDateId" name="boeVO.boeDate"
                                                                  readonly="true" cssClass="datepicker form-control text_box"
                                                                  style="text-align:left;" />
                                                      </div>
                                                </div>

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"> <boe:text
                                                                  name="label.boe.IECode" /></label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <boe:textfield id="ieCode" name="boeVO.ieCode" maxlength="10"
                                                                  onkeypress="return isAlphaNumeric(event)"
                                                                  onchange="populateCIFDetails()"
                                                                  cssClass="form-control text_box" readonly="false" />
                                                      </div>
                                                </div>
                                                <br /> <br /> <br /> <br />

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"> <boe:text name="AD Code" /></label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <boe:textfield id="adCode" name="boeVO.adCode" maxlength="7"
                                                                  onkeypress="return isNumber(event)" readonly="false"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                </div>

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"> <boe:text
                                                                  name="label.boe.igmNo" /></label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <boe:textfield id="igmNo" name="boeVO.igmNo" readonly="false"
                                                                  cssClass="form-control text_box" maxlength="30" />
                                                      </div>
                                                </div>
                                                <br />

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"> <boe:text
                                                                  name="label.boe.hblNo" /></label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <boe:textfield id="hblNo" name="boeVO.hblNo" readonly="false"
                                                                  cssClass="form-control text_box" maxlength="30" />
                                                      </div>
                                                </div>
                                                <br />

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"> <boe:text
                                                                  name="label.boe.mblNo" /></label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <boe:textfield id="mblNo" name="boeVO.mblNo" readonly="false"
                                                                  cssClass="form-control text_box" maxlength="30" />
                                                      </div>
                                                </div>
                                                <br />

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"> <boe:text
                                                                  name="Import Agency" /></label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <boe:select id="imAgency" list="importAgencyList"
                                                                  listKey="key" listValue="value" headerValue="<--->"
                                                                  headerKey="" name="boeVO.imAgency"
                                                                  style="width: 182px; height: 25px" cssClass="chosen">
                                                            </boe:select>
                                                      </div>
                                                </div>
                                                <br />

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"> <boe:text name="GP" /></label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <boe:select id="govprv" list="gpTypeList" listKey="key"
                                                                  listValue="value" headerValue="<--->" headerKey=""
                                                                  name="boeVO.govprv" style="width: 182px; height: 25px"
                                                                  cssClass="chosen">
                                                            </boe:select>
                                                      </div>
                                                </div>
                                                <br />

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"> <boe:text
                                                                  name="Checker Remarks" /></label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <boe:textarea name="boeVO.remarks" id="remarks" cols="26"
                                                                  rows="5" readonly="true" style="width:182px;"></boe:textarea>
                                                      </div>
                                                </div>
                                                <br />


                                          </div>

                                          <div class="col-md-6">

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"> <boe:text
                                                                  name="label.boe.portcode" />&nbsp;<a href="#"
                                                            onclick="getDischargePort()"> <img id="portsearch"
                                                                  src="images/magnify.png" width="13%" height="13%" /></a></label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <boe:textfield id="portcode" name="boeVO.portCode"
                                                                  readonly="false" cssClass="form-control text_box"
                                                                  maxlength="6" />
                                                      </div>
                                                </div>
                                                <%-- <div class="form-group">
                                                      <label class="col-md-4 Control-label" style="font-weight: normal;">
                                                            <boe:text name="label.boe.portcode"/></label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <boe:textfield id="portcode" name="boeVO.portCode"  
                                                              readonly="false"      cssClass="form-control text_box" maxlength="6" />
                                                      </div>
                                                </div> --%>


                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"> <boe:text
                                                                  name="label.boe.CustomerName" /></label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <boe:textfield id="custName" name="boeVO.custName"
                                                                  cssClass="form-control text_box" readonly="false"
                                                                  maxlength="100" />
                                                      </div>
                                                </div>


                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"> <boe:text
                                                                  name="IE Address" /></label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <boe:textarea id="ieadd" name="boeVO.ieadd" maxlength="500"
                                                                  readonly="false" cols="26" rows="5" />
                                                      </div>
                                                </div>

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"> <boe:text
                                                                  name="IE PAN Number" /></label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <boe:textfield id="iepan" name="boeVO.iepan" maxlength="10"
                                                                  cssClass="form-control text_box" readonly="false" />
                                                      </div>
                                                </div>

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"> <boe:text
                                                                  name="label.boe.igmDate" /></label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <boe:textfield id="igmDate" name="boeVO.igmDate"
                                                                  cssClass="datepicker form-control text_box"
                                                                  style="text-align:left;" readonly="true" />
                                                      </div>
                                                </div>
                                                <br />

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"> <boe:text
                                                                  name="label.boe.hblDate" /></label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <boe:textfield id="hblDate" name="boeVO.hblDate"
                                                                  readonly="true" cssClass="datepicker form-control text_box"
                                                                  style="text-align: left;" />
                                                      </div>
                                                </div>
                                                <br />


                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"> <boe:text
                                                                  name="label.boe.mblDate" /></label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <boe:textfield id="mblDate" name="boeVO.mblDate"
                                                                  readonly="true" cssClass="datepicker form-control text_box"
                                                                  style="text-align: left;" />
                                                      </div>
                                                </div>
                                                <br />

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"> <boe:text
                                                                  name="Record Indicator" /></label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <boe:select id="recInd" list="recordInd" listKey="key"
                                                                  listValue="value" headerValue="<--->" headerKey=""
                                                                  name="boeVO.recInd" style="width: 182px; height: 25px"
                                                                  cssClass="chosen">
                                                            </boe:select>
                                                      </div>
                                                </div>
                                                <br />

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"> <boe:text
                                                                  name="Port of Shipment" />&nbsp;<a href="#"
                                                            onclick="getShipmentPort()"> <img src="images/magnify.png"
                                                                  width="13%" height="13%" /></a></label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <%-- <boe:textfield id="pos" name="boeVO.sPortCode" readonly="false"
                                                                  maxlength="50" cssClass="form-control text_box" />&nbsp; --%>
                                                            <boe:textfield id="pos" name="boeVO.pos" readonly="false"
                                                                  maxlength="50" cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                <br />

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"> </label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <input type="button" value="<boe:text name="Search" />"
                                                                  onclick="retriveBOEData()" class="button" id="searchButton" />
                                                            <br />
                                                      </div>
                                                </div>
                                          </div>
                                    </div>

                              </div>

                              <!-- First Block is Ended  -->

                              <!-- Invoice Block is started -->

                              <div class="row page_content">
                                    <div class="col-md-12">
                                          <div class="col-md-12">
                                                <div class="page_collapsible" id="body-section1">
                                                      <span></span>
                                                      <h5 style="font-weight: bold; font-size: 11px;">
                                                            <boe:text name="label.boe.InvoiceDetails" />
                                                      </h5>
                                                </div>
                                                <div class="col-md-6">
                                                      <div id="selectrow"
                                                            style="display: none; color: #FF0000; font-weight: bold; font-size: 16px;">
                                                            Please select a row</div>
                                                      <div class="row page_content">
                                                            <div class="table">
                                                                  <table border="1px" align="left" id="invTable">
                                                                        <thead>
                                                                              <tr>
                                                                                    <th
                                                                                          style="text-align: left; width: 200px; padding: 4px 5px; !important"><label>
                                                                                                <boe:text name="label.boe.InvoiceSerialNumber" />
                                                                                    </label></th>

                                                                                    <th
                                                                                          style="text-align: left; width: 200px; padding: 4px 5px; !important"><label>
                                                                                                <boe:text name="label.boe.InvoiceNumber" />
                                                                                    </label></th>

                                                                                    <th
                                                                                          style="text-align: left; width: 100px; padding: 4px 5px; !important"><label>
                                                                                                <boe:text name="Terms Of Invoice" />
                                                                                    </label></th>

                                                                                    <th
                                                                                          style="text-align: left; width: 200px; padding: 4px 5px; !important"><label>
                                                                                                <boe:text name="Total Invoice Amount" />
                                                                                    </label></th>

                                                                                    <th
                                                                                          style="text-align: left; width: 200px; padding: 4px 5px; !important"><label>
                                                                                                <boe:text name="label.boe.InvoiceAmount" />
                                                                                    </label></th>

                                                                                    <th
                                                                                          style="text-align: left; width: 100px; padding: 4px 5px; !important"><label>
                                                                                                <boe:text name="Invoice Currency" />
                                                                                    </label></th>

                                                                                    <th
                                                                                          style="text-align: left; width: 200px; padding: 4px 5px; !important"><label>
                                                                                                <boe:text name="Freight Amount" />
                                                                                    </label></th>

                                                                                    <th
                                                                                          style="text-align: left; width: 100px; padding: 4px 5px; !important"><label>
                                                                                                <boe:text name="Freight Currency" />
                                                                                    </label></th>

                                                                                    <th
                                                                                          style="text-align: left; width: 200px; padding: 4px 5px; !important"><label>
                                                                                                <boe:text name="Insurance Amount" />
                                                                                    </label></th>

                                                                                    <th
                                                                                          style="text-align: left; width: 100px; padding: 4px 5px; !important"><label>
                                                                                                <boe:text name="Insurance Currency" />
                                                                                    </label></th>

                                                                              </tr>
                                                                        </thead>

                                                                        <tbody>

                                                                              <boe:iterator value="invoiceList" status="invList"
                                                                                    id="listItem">

                                                                                    <tr class="invList" onclick="selectData(this)">

                                                                                          <td style="padding: 4px 5px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <boe:property value="invoiceSerialNumber"></boe:property>
                                                                                                </div>
                                                                                          </td>

                                                                                          <td style="padding: 4px 5px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <boe:property value="invoiceNumber"></boe:property>
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>

                                                                                          <td style="padding: 4px 5px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <boe:property value="termsofInvoice"></boe:property>
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>

                                                                                          <td style="padding: 4px 5px;">
                                                                                                <div class="form-group" align="right">
                                                                                                      <boe:property value="totalInvAmt"></boe:property>
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>

                                                                                          <td style="padding: 4px 5px;">
                                                                                                <div class="form-group" align="right">
                                                                                                      <boe:property value="invoiceAmount"></boe:property>
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>

                                                                                          <td style="padding: 4px 5px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <boe:property value="invoiceCurr"></boe:property>
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>


                                                                                          <td style="padding: 4px 5px;">
                                                                                                <div class="form-group" align="right">
                                                                                                      <boe:property value="freightAmount"></boe:property>
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>

                                                                                          <td style="padding: 4px 5px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <boe:property value="freightCurrencyCode"></boe:property>
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>

                                                                                          <td style="padding: 4px 5px;">
                                                                                                <div class="form-group" align="right">
                                                                                                      <boe:property value="insuranceAmount"></boe:property>
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>

                                                                                          <td style="padding: 4px 5px;">
                                                                                                <div class="form-group" align="left">
                                                                                                      <boe:property value="insuranceCurrencyCode"></boe:property>
                                                                                                      <div class="col-md-8 input-group input-group-md"></div>
                                                                                                </div>
                                                                                          </td>
                                                                                    </tr>
                                                                              </boe:iterator>
                                                                        </tbody>
                                                                  </table>
                                                                  <br></br>
                                                            </div>
                                                      </div>
                                                </div>
                                          </div>
                                    </div>
                                    <br />
                              </div>
                              <br />

                              <div id="dialog" title="Error Message"></div>
                              <!-- Invoice Block is Ended -->

                              <div align="center">

                                    <input type="button" id="insertInv" value="Insert" class="button"
                                          onclick="insertInvoiceDatas()" style="margin-left: 10px;" />


                                    <boe:if
                                          test='%{boeVO.boeNo!=null &&  boeVO.boeDate!=null && boeVO.portCode!=null && boeVO.boestatus.equals("R")}'>
                                          <input type="button" id="updateInv" value="Update" class="button"
                                                onclick="updateInvoiceDatas()" style="margin-left: 10px;" />

                                          <input type="button" id="deleteInv" value="Delete" class="button"
                                                onclick="deleteInvoiceData()" style="margin-left: 10px;" />
                                    </boe:if>

                                    <input type="button" id="viewInv" value="View" class="button"
                                          onclick="viewInvoiceDatas()" style="margin-left: 10px;" />


                                    <!-- ******************************************************* -->
                                    <boe:if
                                          test='%{boeVO.boeNo!=null &&  boeVO.boeDate!=null && boeVO.portCode!=null && boeVO.recordCount==0 }'>
                                          <input type="button" id="editInv" value="Edit" class="button"
                                                onclick="editInvoiceDatas()" style="margin-left: 10px;" />
                                    </boe:if>
                                    <!-- ******************************************************* -->

                              </div>
                              <br /> <br />

                        </div>
                  </div>
            </div>
            <boe:hidden name="boeVO.insertCount" id="boeinsertstatus"></boe:hidden>
            <boe:hidden name="boeVO.boestatus" id="boestatus" />
            <boe:hidden name="boeVO.buttonType" id="buttonType" />
            <boe:hidden name="boeVO.invValue" id="invValue" />
            <boe:hidden name="boeVO.boeType" id="boeType" />
            <%-- <boe:hidden id="billRefNo" name="boeVO.boeNo"></boe:hidden> --%>
            <%-- <boe:hidden id="pos" name="boeVO.pos"></boe:hidden>     --%>
            <div id="dialogBOE" title="Error Message"></div>
      </boe:form>
 


</body>
</html>