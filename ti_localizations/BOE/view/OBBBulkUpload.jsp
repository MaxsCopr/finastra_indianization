<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="boe" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>OBB Bulk Upload</title>
<jsp:include page="/view/common/header.jsp" />
<script type="text/javascript" src="js/OBBBulkUpload_Maker.js"></script>
<script type="text/javascript">
$(document).ready(function() {
         $("#validate").click(function (){
                var ext = $('#inputFile').val().split('.').pop().toLowerCase();

            if($.inArray(ext, ['csv']) == -1) {
                        alert("File format is wrong; kindly check the file");
                        return false;
                  }
      //alert("After file check");
      });
});
function getFileName() {
      var file = $("#inputFile").val();   
      $("#fileNameRefId").val(file);
}
</script>
</head>
<body class="body_bg">
      <jsp:include page="includes/TITLE.jsp" />
      <div class="row">
            <div class="col-md-2">
                  <div class="side_nav" style="width: 215px;">
                        <ul class="nav nav-pills nav-stacked">
                              <li style="text-align: center;"><a href="closeBOEWindow"><boe:text
                                                name="label.boe.close" /></a></li>
                              <li style="text-align: center;"><a href="OBBBulkUpload"><boe:text
                                                name="Reset" /></a></li>
                        </ul>
                  </div>
                  <div class="side_nav"></div>
            </div>
            <div class="col-md-10 content_box">
                  <!-- Added on 13012017 To enable Error Description -->
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
                  <!-- Added on 13012017 By New -->
                  <h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;OBB
                        Bulk Upload</h5>
                  <div id="userIdMessage" style="color: orange"></div>
                  <div class="row cont_colaps">
                        <div class="row page_content"></div>
                        <div class="col-md-12">
                              <div class="page_collapsible" id="body-section1">
                                    <span></span>
                                    <h5 style="font-weight: bold; font-size: 13px;">Input Details</h5>
                              </div>
                              <boe:form method="post" action="OBBlistofinvoice"
                                    enctype="multipart/form-data" id="bulkUpload" name="bulk_import">
                                    <div class="row page_content">
                                          <div class="col-md-6">
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Upload File</label>
                                                      <div class="col-md-5 input-group input-group-md">
                                                            <input type="file" name="inputFile" id="inputFile"
                                                                  style="height: 25px;" onchange="getFileName()"
                                                                  accept=".csv" />
                                                      </div>
                                                      <boe:hidden id="fileNameRefId" name="fileNameRef" />
                                                </div>
                                                <br />
                                                
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"></label>
                                                      <div class="col-md-5 input-group input-group-md">
                                                            <boe:submit action="OBBlistofinvoice" name="validate"
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
                                    <h5 style="font-weight: bold; font-size: 13px;">OBB Bulk
                                          Upload</h5>
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
                                          <div align="left" class="table">
                                                <c:if test="${not empty boeList}">
                                                      <a onclick="OBBexexcel()" style="margin-left: 92%;" id="excl"
                                                            title="Export to Excel"><img src="images/excel-icon.jpg"
                                                            alt="Export to Excel" width="30" height="30" /></a>
                                                </c:if>
                                                <table border="1px" align="left"
                                                      style="height: 55px; width: 1050px;">
                                                      <thead>
                                                            <tr>
                                                                  <th><label style="padding: 4px 5px;">BillOfEntryNumber
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">BillOfEntryDate
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">PortOfDischarge
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">ImportAgency
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">ADCode </label></th>
                                                                  <th><label style="padding: 4px 5px;">IECode </label></th>
                                                                  <th><label style="padding: 4px 5px;">IEName </label></th>
                                                                  <th><label style="padding: 4px 5px;">ErrorDetails
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">Status </label></th>
                                                            </tr>
                                                      </thead>
                                                      <tbody>
                                                            <c:if test="${empty boeList}">
                                                                  <tr>
                                                                        <td colspan="45">No records found</td>
                                                                  </tr>
                                                            </c:if>
                                                            <c:if test="${not empty boeList}">
                                                                  <boe:iterator value="boeList" id="boeListId" status="list">
                                                                        <tr>
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
                                                                              <td style="text-align: right; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="impagc" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: right; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="adcode" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <!--td style="text-align: left; padding:4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="govprv" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td-->
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="ieCode" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="iename" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="ErrDesc" />
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
                                                                              <boe:hidden name="boeList[%{#list.index}].boeNo" value="%{#boeListId.boeNo}"/>
                                                                              <boe:hidden name="boeList[%{#list.index}].boeDate" value="%{#boeListId.boeDate}"/>
                                                                              <boe:hidden name="boeList[%{#list.index}].portCode" value="%{#boeListId.portCode}"/>
                                                                              <boe:hidden name="boeList[%{#list.index}].impagc" value="%{#boeListId.impagc}"/>
                                                                              <boe:hidden name="boeList[%{#list.index}].adcode" value="%{#boeListId.adcode}"/>
                                                                              <boe:hidden name="boeList[%{#list.index}].ieCode" value="%{#boeListId.ieCode}"/>
                                                                              <boe:hidden name="boeList[%{#list.index}].iename" value="%{#boeListId.iename}"/>
                                                                              <boe:hidden name="boeList[%{#list.index}].ErrDesc" value="%{#boeListId.ErrDesc}"/>
                                                                              <boe:hidden name="boeList[%{#list.index}].status" value="%{#boeListId.status}"/>
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
                                                      onclick="uploadAll()" /> &nbsp;&nbsp; &nbsp;&nbsp; <input
                                                      type="button" value="Reject All" class="button"
                                                      onclick="rejectAll()" />
                                          </div>
                                          <div id="dialogBOE" title="Error Message"></div>
                                    </div>
                                    <br />
                                    <br />
                                    <br />
                                    <br />
                                    <boe:iterator id="obbvoList" value="obbvoList">
                                          <boe:hidden name="chkList"
                                                value="%{#obbvoList.boeNo+':'+#obbvoList.boeDate+':'+#obbvoList.portCode+':'+#obbvoList.imAgency+':'+#obbvoList.adCode+':'+#obbvoList.ieCode+':'+#obbvoList.ieName+':'+#obbvoList.errorDesc}">
                                          </boe:hidden>
                                    </boe:iterator>
                              </boe:form>
                        </div>
                  </div>
            </div>
      </div>
      <jsp:include page="common/footer.jsp" />
</body>
</html>