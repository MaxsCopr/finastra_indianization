<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="gr" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
      <meta http-equiv="X-UA-Compatible" content="IE=edge" />
      <title>BOE Details</title>
      <jsp:include page="/view/common/header.jsp" />
      <script>
      
      $(document).ready(function() {
            
            $("#resetButton").click(function() {
                  $("#myForm").attr("action", "fetchCheckerManualBOE");
                  $("#myForm").submit();
            });
            
      });
      
      </script>   
</head>

<body class="body_bg">
      <%@ include file="/view/includes/TITLE.jsp"%>

      <gr:form method="post" id="myForm" name="form">
            <div class="row">
                  <div class="col-md-2">
                         <div class="side_nav" style="width: 200px;">
                              <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;"><a href="#" id="resetButton">Close</a></li>
                              </ul>
                        </div>
                        <br />      
                  </div>

                  <div class="col-md-10 content_box">
                        <h5 class="row fontcol">
                              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<gr:text name="Manual BOE Details" />
                        </h5>
                        <div id="userIdMessage" style="color: orange"></div>
                        <div class="row cont_colaps">
                              <div class="col-md-12">
                                    <div class="page_collapsible" id="body-section1">
                                          <span></span>
                                          <h5 style="font-weight: bold; font-size: 13px;">
                                                &nbsp;
                                                <gr:text name="BOE Details" />
                                          </h5>
                                    </div>
                                    <div class="row page_content">
                                          <div class="col-md-6">
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><gr:text name="BOE Number" /> </label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <gr:textfield id="billNo" name="boeVO.boeNo"
                                                                  readonly="true" cssClass="form-control text_box" maxlength="20" />
                                                      </div>
                                                </div>

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><gr:text name="Port Code" /> </label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <gr:textfield id="billPort" name="boeVO.portCode"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><gr:text name="Import Agency" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <gr:textfield id="billImportAgency"
                                                                  name="boeVO.imAgency" readonly="true"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><gr:text name="G P" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <gr:textfield id="billGp" name="boeVO.govprv" readonly="true"
                                                                  cssClass="form-control text_box" maxlength="1" />
                                                      </div>
                                                </div>
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><gr:text name="IE Name" />
                                                            </label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <gr:textfield id="billIeName" name="boeVO.iename"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><gr:text name="IE PAN No" />
                                                            </label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <gr:textfield id="billIePanNumber"
                                                                  name="boeVO.iepan" readonly="true"
                                                                  cssClass="form-control text_box" maxlength="10" />
                                                      </div>
                                                </div>
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><gr:text name="IGM No" />
                                                            </label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <gr:textfield id="billIgmNo" name="boeVO.igmNo"
                                                                  readonly="true" cssClass="form-control text_box" />
                                                      </div>
                                                </div>
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><gr:text name="HBL No" />
                                                            </label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <gr:textfield id="billHblNo" name="boeVO.hblNo"
                                                                  readonly="true" cssClass="form-control text_box"
                                                                  maxlength="10" />
                                                      </div>
                                                </div>
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><gr:text name="MBL No" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <gr:textfield id="billMblNo" name="boeVO.mblNo"
                                                                  readonly="true" cssClass="form-control text_box"
                                                                  maxlength="10" />
                                                      </div>
                                                </div>

                                          </div>

                                          <div class="col-md-6">
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><gr:text name="BOE Date" />
                                                            </label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <gr:textfield name="boeVO.boeDate" readonly="true"
                                                                  id="billDate" style="width: 138px;"
                                                                  cssClass="form-control text_box" />
                                                      </div>
                                                </div>


                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Port Of Shipment</label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <gr:textfield id="billPortOfShipment"
                                                                  name="boeVO.pordisc" readonly="true"
                                                                  cssClass="form-control text_box" maxlength="10" />
                                                      </div>
                                                </div>
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><gr:text name="AD Code" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <gr:textfield id="billAdCode" name="boeVO.adCode"
                                                                  readonly="true" cssClass="form-control text_box"
                                                                  maxlength="10" />

                                                      </div>
                                                </div>
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><gr:text name="IE Code" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <gr:textfield id="billIeCode" name="boeVO.ieCode"
                                                                  readonly="true" cssClass="form-control text_box"
                                                                  maxlength="10" />

                                                      </div>
                                                </div>
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><gr:text
                                                                  name="IE Address" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <gr:textfield id="billIeAddress" name="boeVO.ieadd"
                                                                  readonly="true" cssClass="form-control text_box"
                                                                  maxlength="10" />

                                                      </div>
                                                </div>
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><gr:text
                                                                  name="Record Indicator" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <gr:textfield id="billRecordIndicator"
                                                                  name="boeVO.recInd" readonly="true"
                                                                  cssClass="form-control text_box" maxlength="10" />

                                                      </div>
                                                </div>
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><gr:text name="IGM Date" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <gr:textfield id="billIgmDate" name="boeVO.igmDate"
                                                                  readonly="true" cssClass="form-control text_box"
                                                                  maxlength="10" />

                                                      </div>
                                                </div>
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><gr:text name="HBL Date" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <gr:textfield id="billHblDate" name="boeVO.hblDate"
                                                                  readonly="true" cssClass="form-control text_box"
                                                                  maxlength="10" />

                                                      </div>
                                                </div>
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"><gr:text name="MBL Date" /></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <gr:textfield id="billMblNo" name="boeVO.mblDate"
                                                                  readonly="true" cssClass="form-control text_box"
                                                                  maxlength="10" />                                                       
                                                      </div>
                                                </div>

                                          </div>
                                    </div>
                              </div>
                        </div>
                        <br />
                        <div class="form-group">
                              <div class="col-md-12">
                                    <div class="row page_content">
                                          <div class="page_collapsible" id="body-section1">
                                                <span></span>
                                                      <h5 style="font-weight: bold; font-size: 13px;">
                                                      &nbsp;
                                                      <gr:text name="Invoice Details" />
                                                </h5>
                                          </div>                        
                                          <div class="col-md-12">
                                             <div class="form-group">
                                                <div class="table">
                                                      <table border="1px" align="left" id="invList">
                                                            <tbody>
                                                                  <tr>
                                                                        <th style="text-align: left; width: 200px; padding: 4px 5px;"><label>Invoice Serial No</label></th>

                                                                        <th style="text-align: left; width: 200px; padding: 4px 5px;"><label>Invoice No</label></th>
                                                                        
                                                                        <th style="text-align: left; width: 100px; padding: 4px 5px;"><label>Terms of Invoice</label></th>                                                                                                                                              

                                                                        <th style="text-align: left; width: 200px; padding: 4px 5px;"><label>Invoice Amount</label></th>                                                            

                                                                        <th style="text-align: left; width: 100px; padding: 4px 5px;"><label>Invoice Currency</label></th>

                                                                        <th style="text-align: left; width: 200px; padding: 4px 5px;"><label>Freight Amount</label></th>

                                                                        <th style="text-align: left; width: 100px; padding: 4px 5px;"><label>Freight Currency</label></th>
                                                                        
                                                                        <th style="text-align: left; width: 200px; padding: 4px 5px;"><label>Insurance Amount</label></th>

                                                                        <th style="text-align: left; width: 100px; padding: 4px 5px;"><label>Insurance Currency</label></th>

                                                                        <th style="text-align: left; width: 200px; padding: 4px 5px;"><label>Agency Commission</label></th>

                                                                        <th style="text-align: left; width: 100px; padding: 4px 5px;"><label>Agency Currency</label></th>                 

                                                                        <th style="text-align: left; width: 200px; padding: 4px 5px;"><label>Discount Charges</label></th>

                                                                        <th style="text-align: left; width: 100px; padding: 4px 5px;"><label>Discount Currency</label></th>

                                                                        <th style="text-align: left; width: 200px; padding: 4px 5px;"><label>Misc Charges</label></th>

                                                                        <th style="text-align: left; width: 100px; padding: 4px 5px;"><label>Misc Currency</label></th>
                                                                        
                                                                        <th style="text-align: left; width: 200px; padding: 4px 5px;"><label>Supplier Name</label></th>

                                                                        <th style="text-align: left; width: 200px; padding: 4px 5px;"><label>Supplier Address</label></th>

                                                                        <th style="text-align: left; width: 100px; padding: 4px 5px;"><label>Supplier Country</label></th>

                                                                        <th style="text-align: left; width: 200px; padding: 4px 5px;"><label>Seller Name</label></th>

                                                                        <th style="text-align: left; width: 200px; padding: 4px 5px;"><label>Seller Address</label></th>

                                                                        <th style="text-align: left; width: 100px; padding: 4px 5px;"><label>Seller Country</label></th>

                                                                        <th style="text-align: left; width: 200px; padding: 4px 5px; "><label>Third Party Name</label></th>

                                                                        <th style="text-align: left; width: 200px; padding: 4px 5px;"><label>Third Party Address</label></th>

                                                                        <th style="text-align: left; width: 100px; padding: 4px 5px;"><label>Third Party Country</label></th>
                                                                        
                                                                  </tr>
                                                                  <c:if test="${empty boeInvoiceList}">
                                                                         <tr>
                                                                              <td colspan="24">No records found</td>
                                                                        </tr>
                                                                  </c:if>
                                                                  <c:if test="${not empty boeInvoiceList}">                                                             
                                                                        <gr:iterator value="boeInvoiceList" id="boeInvoiceList">
                                                                              <tr>
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="invoiceSerialNo"></gr:property>
                                                                                          </div>
                                                                                    </td>
      
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="invoiceNo"></gr:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
      
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="termsofInvoice"></gr:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>                                                                         
                                                                                    
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="invoiceAmt"></gr:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
      
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="invoiceCurrency"></gr:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                                    
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="freightAmount"></gr:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="freightCurrencyCode"></gr:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="insuranceAmount"></gr:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="insuranceCurrencyCode"></gr:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="agencyCommission"></gr:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="agencyCurrency"></gr:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="discountCharges"></gr:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="discountCurrency"></gr:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="miscellaneousCharges"></gr:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="miscellaneousCurrency"></gr:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                                    
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="supplierName"></gr:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="supplierAddress"></gr:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="supplierCountry"></gr:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="sellerName"></gr:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="sellerAddress"></gr:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="sellerCountry"></gr:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="thirdPartyName"></gr:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="thirdPartyAddress"></gr:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="thirdPartyCountry"></gr:property>
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                              </tr>
                                                                        </gr:iterator>
                                                                  </c:if>
                                                            </tbody>
                                                      </table>
                                                 </div>
                                                </div>                                                
                                                <gr:hidden name="boeSearchVO.paymentDateFrom" id="check1" />
                                                <gr:hidden name="boeSearchVO.paymentDateTo" id="check2" />
                                                <gr:hidden name="boeSearchVO.portCode" id="check3" />
                                                <gr:hidden name="boeSearchVO.boeNo" id="check4" />
                                          </div>
                                    </div>
                              </div>
                        </div>

                  </div>
            </div>
      </gr:form>
</body>
</html>