<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="boe" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>Bill Of Entry Bulk Upload</title>
<jsp:include page="/view/common/header.jsp" />
<script type="text/javascript" src="js/boeBulkUpload_Maker.js"></script>
<script>
      $(document).ready(function() {
            $("#validate").click(function() {
                  var iSize = ($("#fileUpload")[0].files[0].size / 1024);
                  iSize = (Math.round((iSize / 1024) * 100) / 100)

                  if (iSize > 50.0) {
                        alert("Uploading File Size Exceeding 50 Mb");
                        return false;
                  }
            });
      });
      function getFileName() {
            var file = $("#fileUpload").val();  
            $("#fileNameRefId").val(file);
      }
</script>
</head>

<body class="body_bg">

      <jsp:include page="../view/includes/TITLE.jsp" />

      <div class="row">
            <div class="col-md-2">
                  <div class="side_nav" style="width: 215px;">
                        <ul class="nav nav-pills nav-stacked">
                              <li style="text-align: center;"><a href="makerProcess"><boe:text
                                                name="label.boe.close" /></a></li>

                              <li style="text-align: center;"><a href="BOEBlk"><boe:text
                                                name="Reset" /></a></li>
                        </ul>
                  </div>
                  <div class="side_nav"></div>
            </div>

            <div class="col-md-10 content_box">

                  <!-- Added on 08022017 To enable Error Description -->
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

                              <div class="table">
                                    <c:if test="${not empty alertMsgArray}">
                                          <a onclick="bulkerrorlistexcel()" style="margin-left: 92%;" id="excl"
                                                title="Export to Excel"><img src="images/excel-icon.jpg"
                                                alt="Export to Excel" width="30" height="30" /></a>
                                    </c:if>
                                    <table border="1px" align="left" id="errorAlert">
                                          <tbody>
                                                <tr>
                                                      <th style="text-align: left; width: 200px; padding: 5px;"><label>Severity</label></th>

                                                      <th style="text-align: left; width: 200px; padding: 5px;"><label>Description</label></th>

                                                      <th style="text-align: left; width: 200px; padding: 5px;"><label>Step</label></th>

                                                      <th style="text-align: left; width: 300px; padding: 5px;"><label>Error</label></th>

                                                      <th style="text-align: left; width: 200px; padding: 5px;"><label>Overridden</label></th>
                                                </tr>


                                                <boe:iterator value="alertMsgArray" id="alertMsgArray"
                                                      status="list">

                                                      <tr class="alertMsgArray" ondblclick="override(this)">
                                                            <td style="padding: 5px;">
                                                                  <div class="form-group" align="left">
                                                                        <boe:property value="errorId" />
                                                                        <div class="col-md-8 input-group input-group-md"></div>
                                                                  </div>
                                                            </td>
                                                            <td style="padding: 5px;">
                                                                  <div class="form-group" align="left">
                                                                        <boe:property value="errorDesc" />
                                                                        <div class="col-md-8 input-group input-group-md"></div>
                                                                  </div>
                                                            </td>
                                                            <td style="padding: 5px;">
                                                                  <div class="form-group" align="left">
                                                                        <boe:property value="errorCode" />
                                                                        <div class="col-md-8 input-group input-group-md"></div>
                                                                  </div>
                                                            </td>

                                                            <td style="padding: 5px;">
                                                                  <div class="form-group">
                                                                        <boe:property value="errorDetails" />
                                                                        <div class="col-md-8 input-group input-group-md"></div>
                                                                  </div>

                                                            </td>

                                                            <td style="padding: 5px;">
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
                  </div>
                  <br />

                  <!-- Added on 09022017 By New -->

                  <h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Bill
                        Of Entry Bulk Upload</h5>

                  <div id="userIdMessage" style="color: orange"></div>

                  <div class="row cont_colaps">

                        <div class="row page_content"></div>

                        <div class="col-md-12">

                              <div class="page_collapsible" id="body-section1">
                                    <span></span>
                                    <h5 style="font-weight: bold; font-size: 13px;">Input Details</h5>
                              </div>



                              <boe:form method="post" action="boeListofinvoice"
                                    enctype="multipart/form-data" id="bulkUpload" name="bulk_import">

                                    <div class="row page_content">

                                          <div class="col-md-6">
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Upload File</label>
                                                      <div class="col-md-5 input-group input-group-md">
                                                            <input type="file" id="fileUpload" onchange="getFileName()"
                                                                  name="inputFile" style="height: 25px;"
                                                                  accept="application/vnd.ms-excel" />
                                                      </div>
                                                      <boe:hidden id="fileNameRefId" name="fileNameRef" />  
                                                </div>
                                                <br />


                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"></label>
                                                      <div class="col-md-5 input-group input-group-md">
                                                            <boe:submit action="boeListofinvoice" name="validate"
                                                                  id="validate" style="height: 25px;" cssClass="button"
                                                                  value="Validate" />
                                                      </div>
                                                </div>
                                          </div>
                                    </div>
                              </boe:form>
                              <br />

                        </div>
                  </div>

                  <br />
                  <div class="row cont_colaps">
                        <div class="row page_content"></div>
                        <div class="col-md-12">
                              <div class="page_collapsible" id="body-section1">
                                    <span></span>
                                    <h5 style="font-weight: bold; font-size: 13px;">Bill Of Entry
                                          Bulk Upload</h5>
                              </div>
                              <boe:form method="post" id="myForm">
                                    <div class="col-md-6">
                                          <div class="form-group">
                                                <label class="col-md-4 Control-label"
                                                      style="font-weight: normal;">Batch Id </label>
                                                <div class="col-md-5 input-group input-group-md">
                                                      <boe:textfield id="batchID" name="batchId"
                                                            cssClass="form-control text_box" />
                                                </div>
                                          </div>
                                    </div>

                                    <div>
                                          <c:if test="${not empty boeBulkList}">
                                                <a onclick="exexcel()" style="margin-left: 32%;" id="excl"
                                                      title="Export to Excel"><img src="images/excel-icon.jpg"
                                                      alt="Export to Excel" width="30" height="30" /></a>
                                          </c:if>
                                          <div align="left" class="table">
                                                <table border="1px" align="left"
                                                      style="height: 55px; width: 1050px;">
                                                      <thead>
                                                            <tr>
                                                                  <th><label style="padding: 4px 5px;">Payment
                                                                              Reference Number</label></th>
                                                                  <th><label style="padding: 4px 5px;">Event
                                                                              Reference No</label></th>
                                                                  <th><label style="padding: 4px 5px;">Payment
                                                                              Amount</label></th>
                                                                  <th><label style="padding: 4px 5px;">Payment
                                                                              Amount Currency</label></th>
                                                                  <th><label style="padding: 4px 5px;">BillOfEntryNumber</label></th>
                                                                  <th><label style="padding: 4px 5px;">BillOfEntryDate</label></th>
                                                                  <th><label style="padding: 4px 5px;">PortOfDischarge</label></th>
                                                                  <th><label style="padding: 4px 5px;">BOE Amount</label></th>
                                                                  <th><label style="padding: 4px 5px;">BOE Amount
                                                                              Currency</label></th>
                                                                  <th><label style="padding: 4px 5px;">BOE Amount
                                                                              available for Endorsement</label></th>
                                                                  <th><label style="padding: 4px 5px;">BOE
                                                                              Allocated Amount in Payment Currency</label></th>
                                                                  <th><label style="padding: 4px 5px;">Change IE
                                                                              code</label></th>
                                                                  <th><label style="padding: 4px 5px;">BES Record
                                                                              Indicator</label></th>
                                                                  <th><label style="padding: 4px 5px;">Invoice Ser
                                                                              No</label></th>
                                                                  <th><label style="padding: 4px 5px;">Invoice No</label></th>
                                                                  <th><label style="padding: 4px 5px;">Real Amt</label></th>
                                                                  <!-- <th><label style=" padding:4px 5px; !important">Ex Rate</label></th>
                                                                  <th><label style=" padding:4px 5px; !important">Insu Ex Rate</label></th>
                                                                  <th><label style=" padding:4px 5px; !important">Fri Ex Rate</label></th> -->
                                                                  <th><label style="padding: 4px 5px;">Remarks</label></th>
                                                                  <th><label style="padding: 4px 5px;">ErrorDetails</label></th>
                                                                  <th><label style="padding: 4px 5px;">Status</label></th>
                                                                  <!-- <th><label style=" padding:4px 5px; !important">Closure Type</label></th>
                                                                  <th><label style=" padding:4px 5px; !important">ADJ Ind</label></th>
                                                                  <th><label style=" padding:4px 5px; !important">Approved By</label></th>      
                                                                  <th><label style=" padding:4px 5px; !important">Doc No</label></th>     
                                                                  <th><label style=" padding:4px 5px; !important">Doc Date</label></th>
                                                                  <th><label style=" padding:4px 5px; !important">Doc Port</label></th>
                                                                  <th><label style=" padding:4px 5px; !important">Letter No</label></th>
                                                                  <th><label style=" padding:4px 5px; !important">Letter Date</label></th>
                                                                  <th><label style=" padding:4px 5px; !important">ADJ Date</label></th> -->
                                                            </tr>
                                                      </thead>
                                                      <tbody>
                                                            <c:if test="${empty boeBulkList}">
                                                                  <tr>
                                                                        <td colspan="19">No records found</td>
                                                                  </tr>
                                                            </c:if>
                                                            <c:if test="${not empty boeBulkList}">
                                                                  <boe:iterator value="boeBulkList" id="boeBulkListId" status="list">
                                                                        <tr>
                                                                              <!-- Added on 10/02/2018 -->
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="paymentRefNo" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="eventRefNo" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <!-- Ends -->
                                                                              <!-- Added on 23/10/2018 -->
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="paymentAmnt" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="payAmntCurr" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>


                                                                              <!-- Ends -->
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="boeNo" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="boeDate" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="portCode" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <!-- Added on 23/10/2018 -->
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="boeAmnt" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="boeAmntCurr" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="boeAmntEndorse" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="boeAllocAmnt" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <!-- Ends -->
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="changeIeCode" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <!-- Added on 23/10/2018 -->
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="besRecInd" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <!-- Ends -->
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="invoiceSerNo" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="invoiceNo" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="invRealAmt" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <%-- <td style="text-align: left; padding:4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="exRate" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td> 
                                                                              <td style="text-align: left; padding:4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="insExRate" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td> 
                                                                              <td style="text-align: left; padding:4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="frExRate" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td> --%>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="remarks" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="errDesc" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="status" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              
                                                                              
                                                                              <!-- hidden -->
                                                                              <boe:hidden name="boeBulkList[%{#list.index}].paymentRefNo" value="%{#boeBulkListId.paymentRefNo}"/>
                                                                              <boe:hidden name="boeBulkList[%{#list.index}].eventRefNo" value="%{#boeBulkListId.eventRefNo}"/>
                                                                              <boe:hidden name="boeBulkList[%{#list.index}].paymentAmnt" value="%{#boeBulkListId.paymentAmnt}"/>
                                                                              <boe:hidden name="boeBulkList[%{#list.index}].payAmntCurr" value="%{#boeBulkListId.payAmntCurr}"/>
                                                                              <boe:hidden name="boeBulkList[%{#list.index}].boeNo" value="%{#boeBulkListId.boeNo}"/>
                                                                              <boe:hidden name="boeBulkList[%{#list.index}].boeDate" value="%{#boeBulkListId.boeDate}"/>
                                                                              <boe:hidden name="boeBulkList[%{#list.index}].portCode" value="%{#boeBulkListId.portCode}"/>
                                                                              <boe:hidden name="boeBulkList[%{#list.index}].boeAmnt" value="%{#boeBulkListId.boeAmnt}"/>
                                                                              <boe:hidden name="boeBulkList[%{#list.index}].boeAmntCurr" value="%{#boeBulkListId.boeAmntCurr}"/>
                                                                              <boe:hidden name="boeBulkList[%{#list.index}].boeAmntEndorse" value="%{#boeBulkListId.boeAmntEndorse}"/>
                                                                              <boe:hidden name="boeBulkList[%{#list.index}].boeAllocAmnt" value="%{#boeBulkListId.boeAllocAmnt}"/>
                                                                              <boe:hidden name="boeBulkList[%{#list.index}].changeIeCode" value="%{#boeBulkListId.changeIeCode}"/>
                                                                              <boe:hidden name="boeBulkList[%{#list.index}].besRecInd" value="%{#boeBulkListId.besRecInd}"/>
                                                                              <boe:hidden name="boeBulkList[%{#list.index}].invoiceSerNo" value="%{#boeBulkListId.invoiceSerNo}"/>
                                                                              <boe:hidden name="boeBulkList[%{#list.index}].invoiceNo" value="%{#boeBulkListId.invoiceNo}"/>
                                                                              <boe:hidden name="boeBulkList[%{#list.index}].invRealAmt" value="%{#boeBulkListId.invRealAmt}"/>
                                                                              <boe:hidden name="boeBulkList[%{#list.index}].remarks" value="%{#boeBulkListId.remarks}"/>
                                                                              <boe:hidden name="boeBulkList[%{#list.index}].errDesc" value="%{#boeBulkListId.errDesc}"/>
                                                                              <boe:hidden name="boeBulkList[%{#list.index}].status" value="%{#boeBulkListId.status}"/>
                                                                        </tr>
                                                                  </boe:iterator>
                                                            </c:if>
                                                      </tbody>
                                                </table>
                                          </div>

                                          <div class="col-md-12" style="width: 100%" align="center">
                                                <br />
                                                <%-- <boe:if test="%{makervo.state=='Success'}"> --%>

                                                <input type="button" value="Upload Success" class="button"
                                                      onclick="uploadAll()" /> &nbsp;&nbsp;

                                                <%-- </boe:if>                       --%>
                                                &nbsp;&nbsp; <input type="button" value="Reject All"
                                                      class="button" onclick="rejectAll()" />
                                          </div>


                                          <div id="dialogBOE" title="Error Message"></div>

                                    </div>
                                    <br />
                                    <br />
                                    <br />
                                    <br />
                                    <boe:iterator id="boevoList" value="boevoList">
                                          <boe:hidden name="chkList"
                                                value="%{#boevoList.paymentRefNo+':'+#boevoList.eventRefNo+':'+#boevoList.paymentAmnt+':'+#boevoList.payAmntCurr+':'+#boevoList.boeNo+':'+#boevoList.boeDate+':'+#boevoList.portCode+':'+#boevoList.boeAmnt+':'+#boevoList.boeAmntCurr+':'+#boevoList.boeAmntEndorse+':'+#boevoList.boeAllocAmnt+':'+#boevoList.changeIeCode+':'+#boevoList.besRecInd+':'+#boevoList.invoiceSerNo+':'+#boevoList.invoiceNo+':'+#boevoList.invRelAmt+':'+#boevoList.remarks+':'+#boevoList.errorDesc}">
                                          </boe:hidden>
                                    </boe:iterator>
                              </boe:form>

                        </div>
                  </div>
            </div>
      </div>

      <jsp:include page="../view/common/footer.jsp" />


</body>
</html>