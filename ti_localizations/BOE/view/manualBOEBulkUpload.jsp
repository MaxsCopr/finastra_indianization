<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="boe" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>Manual Bill Of Entry Bulk Upload</title>
<jsp:include page="/view/common/header.jsp" />
<script type="text/javascript" src="js/bulkUpload_Maker.js"></script>
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

      <jsp:include page="includes/TITLE.jsp" />

      <div class="row">
            <div class="col-md-2">
                  <div class="side_nav" style="width: 215px;">
                        <ul class="nav nav-pills nav-stacked">
                              <li style="text-align: center;"><a href="home"><boe:text
                                                name="label.boe.close" /></a></li>

                              <li style="text-align: center;"><a href="manualBOEBulkUpload"><boe:text
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
                                    <c:if test="${not empty alertMsgArray}">
                                          <a onclick="manualerrorlistexcel()" style="margin-left: 92%;" id="excl"
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

                  <!-- Added on 13012017 By New -->

                  <h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Manual
                        Bill Of Entry Bulk Upload</h5>

                  <div id="userIdMessage" style="color: orange"></div>

                  <div class="row cont_colaps">

                        <div class="row page_content"></div>

                        <div class="col-md-12">

                              <div class="page_collapsible" id="body-section1">
                                    <span></span>
                                    <h5 style="font-weight: bold; font-size: 13px;">Input Details</h5>
                              </div>



                              <boe:form method="post" action="listofinvoice"
                                    enctype="multipart/form-data" id="bulkUpload" name="bulk_import">

                                    <div class="row page_content">

                                          <div class="col-md-6">
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Upload File</label>
                                                      <div class="col-md-5 input-group input-group-md">
                                                            <input type="file" name="inputFile" style="height: 25px;"
                                                                  id="fileUpload" onchange="getFileName()"
                                                                  accept="application/vnd.ms-excel" />
                                                      </div>
                                                      <boe:hidden id="fileNameRefId" name="fileNameRef" />  
                                                </div>
                                                <br />


                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"></label>
                                                      <div class="col-md-5 input-group input-group-md">
                                                            <boe:submit action="listofinvoice" name="validate"
                                                                  id="validate"
                                                                  style="height: 25px;" cssClass="button" value="Validate" />
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
                                    <h5 style="font-weight: bold; font-size: 13px;">Manual Bill
                                          Of Entry Bulk Upload</h5>
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
                                          <c:if test="${not empty invoiceList}">
                                                <a onclick="exexcel()" style="margin-left: 32%;" id="excl"
                                                      title="Export to Excel"><img src="images/excel-icon.jpg"
                                                      alt="Export to Excel" width="30" height="30" /></a>
                                          </c:if>
                                          <div align="left" class="table">
                                                <table border="1px" align="left"
                                                      style="height: 55px; width: 1050px;">
                                                      <thead>
                                                            <tr>
                                                                  <th><label style="padding: 4px 5px;">BillOfEntryNumber
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">BillOfEntryDate
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">PortCode </label></th>
                                                                  <th><label style="padding: 4px 5px;">ImportAgency
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">RecIndicator
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">ADCode </label></th>
                                                                  <th><label style="padding: 4px 5px;">G-P </label></th>
                                                                  <th><label style="padding: 4px 5px;">IECode </label></th>

                                                                  <!-- <th><label style=" padding:4px 5px; !important">IEName
                                                                  </label></th>
                                                                  <th><label style=" padding:4px 5px; !important">IEAddress
                                                                  </label></th>
                                                                  <th><label style=" padding:4px 5px; !important">IE-PAN-Number
                                                                  </label></th>
                                                                  <th><label style=" padding:4px 5px; !important">CIF
                                                                  </label></th> -->


                                                                  <th><label style="padding: 4px 5px;">PortOfShipment
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">IGMNo </label></th>

                                                                  <th><label style="padding: 4px 5px;">IGMDate </label></th>
                                                                  <th><label style="padding: 4px 5px;">MAWB-MBLNo
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">MAWB-MBLDate
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">HAWB-HBLNo
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">HAWB-HBLDate
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">InvoiceSerialNo
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">InvoiceNo </label></th>

                                                                  <th><label style="padding: 4px 5px;">TermsOfServices
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">InvoiceAmount
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">InvoiceCurrency
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">FreightAmount
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">FreightCurrencyCode
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">InsuranceAmount
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">InsuranceCurrencyCode
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">AgencyCommission
                                                                  </label></th>

                                                                  <th><label style="padding: 4px 5px;">AgencyCurrency
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">DiscountCharges
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">DiscountCurrency
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">MiscellaneousCharges
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">MiscellaneousCurrency
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">SupplierName
                                                                  </label></th>

                                                                  <th><label style="padding: 4px 5px;">SupplierAddress
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">SupplierCountry
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">SellerName
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">SellerAddress
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">SellerCountry
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">ThirdPartyName
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">ThirdPartyAddress
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">ThirdPartyCountry
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">ErrorDetails
                                                                  </label></th>
                                                                  <th><label style="padding: 4px 5px;">Status </label></th>
                                                            </tr>
                                                      </thead>
                                                      <tbody>
                                                            <c:if test="${empty invoiceList}">
                                                                  <tr>
                                                                        <td colspan="41">No records found</td>
                                                                  </tr>
                                                            </c:if>
                                                            <c:if test="${not empty invoiceList}">
                                                                  <boe:iterator value="invoiceList" id="invoiceListId" status="list">
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
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="RecordInd" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: right; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="adcode" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="govprv" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="ieCode" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>


                                                                              <%-- <td style="text-align: left; padding:4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="iename" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding:4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="ieadd" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding:4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="iepan" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding:4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="cifNo" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td> --%>



                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="porshp" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="igmNumber" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="igmDate" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="hblNumber" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="hblDate" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="mblNumber" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="mblDate" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="invSno" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="invNo" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="termsofInvoice" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="invAmt" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="invCurr" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="freightAmount" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="freightCurrencyCode" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="insuranceAmount" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="insuranceCurrencyCode" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="agencyCommission" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>

                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="agencyCurrency" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="discountCharges" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="discountCurrency" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="miscellaneousCharges" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="miscellaneousCurrency" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="supplierName" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="supplierAddress" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="supplierCountry" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="sellerName" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="sellerAddress" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="sellerCountry" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="thirdPartyName" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="ThirdPartyAddress" />
                                                                                          <div class="col-md-8 input-group input-group-md"></div>
                                                                                    </div>
                                                                              </td>
                                                                              <td style="text-align: left; padding: 4px 5px;">
                                                                                    <div class="form-group">
                                                                                          <boe:property value="thirdPartyCountry" />
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
                                                                              <boe:hidden name="invoiceList[%{#list.index}].boeNo" value="%{#invoiceListId.boeNo}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].boeDate" value="%{#invoiceListId.boeDate}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].portCode" value="%{#invoiceListId.portCode}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].impagc" value="%{#invoiceListId.impagc}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].RecordInd" value="%{#invoiceListId.RecordInd}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].adcode" value="%{#invoiceListId.adcode}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].govprv" value="%{#invoiceListId.govprv}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].ieCode" value="%{#invoiceListId.ieCode}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].porshp" value="%{#invoiceListId.porshp}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].igmNumber" value="%{#invoiceListId.igmNumber}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].igmDate" value="%{#invoiceListId.igmDate}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].hblNumber" value="%{#invoiceListId.hblNumber}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].hblDate" value="%{#invoiceListId.hblDate}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].mblNumber" value="%{#invoiceListId.mblNumber}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].mblDate" value="%{#invoiceListId.mblDate}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].invSno" value="%{#invoiceListId.invSno}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].invNo" value="%{#invoiceListId.invNo}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].termsofInvoice" value="%{#invoiceListId.termsofInvoice}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].invAmt" value="%{#invoiceListId.invAmt}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].invCurr" value="%{#invoiceListId.invCurr}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].freightAmount" value="%{#invoiceListId.freightAmount}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].freightCurrencyCode" value="%{#invoiceListId.freightCurrencyCode}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].insuranceAmount" value="%{#invoiceListId.insuranceAmount}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].insuranceCurrencyCode" value="%{#invoiceListId.insuranceCurrencyCode}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].agencyCommission" value="%{#invoiceListId.agencyCommission}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].agencyCurrency" value="%{#invoiceListId.agencyCurrency}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].discountCharges" value="%{#invoiceListId.discountCharges}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].discountCurrency" value="%{#invoiceListId.discountCurrency}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].miscellaneousCharges" value="%{#invoiceListId.miscellaneousCharges}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].miscellaneousCurrency" value="%{#invoiceListId.miscellaneousCurrency}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].supplierName" value="%{#invoiceListId.supplierName}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].supplierAddress" value="%{#invoiceListId.supplierAddress}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].supplierCountry" value="%{#invoiceListId.supplierCountry}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].sellerName" value="%{#invoiceListId.sellerName}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].sellerAddress" value="%{#invoiceListId.sellerAddress}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].sellerCountry" value="%{#invoiceListId.sellerCountry}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].thirdPartyName" value="%{#invoiceListId.thirdPartyName}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].ThirdPartyAddress" value="%{#invoiceListId.ThirdPartyAddress}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].thirdPartyCountry" value="%{#invoiceListId.thirdPartyCountry}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].errDesc" value="%{#invoiceListId.errDesc}"/>
                                                                              <boe:hidden name="invoiceList[%{#list.index}].status" value="%{#invoiceListId.status}"/>
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
                                    <boe:iterator id="manualBoevoList" value="manualBoevoList">
                                          <boe:hidden name="mchkList"
                                                value="%{#manualBoevoList.boeNo+':'+#manualBoevoList.boeDate+':'+#manualBoevoList.portCode+':'+#manualBoevoList.ieCode+':'+#manualBoevoList.adCode+':'+#manualBoevoList.igmNo+':'+#manualBoevoList.igmDate+':'+#manualBoevoList.hblNo+':'+#manualBoevoList.hblDate+':'+#manualBoevoList.mblNo+':'+#manualBoevoList.mblDate+':'+#manualBoevoList.imAgency+':'+#manualBoevoList.recInd+':'+#manualBoevoList.govprv+':'+#manualBoevoList.pos+':'+#manualBoevoList.invoiceSerialNo+':'+#manualBoevoList.invoiceNo+':'+#manualBoevoList.termsofInvoice+':'+#manualBoevoList.invoiceAmt+':'+#manualBoevoList.invoiceCurrency+':'+#manualBoevoList.freightAmount+':'+#manualBoevoList.freightCurrencyCode+':'+#manualBoevoList.insuranceAmount+':'+#manualBoevoList.insuranceCurrencyCode+':'+#manualBoevoList.agencyCommission+':'+#manualBoevoList.agencyCurrency+':'+#manualBoevoList.discountCharges+':'+#manualBoevoList.discountCurrency+':'+#manualBoevoList.miscellaneousCharges+':'+#manualBoevoList.miscellaneousCurrency+':'+#manualBoevoList.supplierName+':'+#manualBoevoList.supplierAddress+':'+#manualBoevoList.supplierCountry+':'+#manualBoevoList.sellerName+':'+#manualBoevoList.sellerAddress+':'+#manualBoevoList.sellerCountry+':'+#manualBoevoList.thirdPartyName+':'+#manualBoevoList.thirdPartyAddress+':'+#manualBoevoList.thirdPartyCountry+':'+#manualBoevoList.errorDesc}">
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