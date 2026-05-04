<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="boe" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
      <meta http-equiv="X-UA-Compatible" content="IE=edge" />
      <title>Outstanding ORM</title>
      <jsp:include page="/view/common/header.jsp" />  
      <script type="text/javascript" src="boe/scripts/manyBill/manualBOE.js"></script>
      
      <link rel="stylesheet" href="css/jquery-ui.css" />
<link rel="stylesheet" href="css/style.css" />
<link rel="stylesheet" href="css/datepicker.css"></link>
<link type="text/css" rel="stylesheet" href="css/bootstrap.css" />
<link />
<link type="text/css" rel="stylesheet" href="css/bootstrap-dropdown.css" />
<link />
<link type="text/css" rel="stylesheet" href="css/headfoot.css" />
<link />
<link href="css/font-awesome.css" rel="stylesheet"></link>
<link rel="stylesheet" href="css/commonTiplus.css"></link>


<script src="js/jquery-1.9.1.js"></script>
<script src="js/jquery-ui.js"></script>
<script src="js/bootstrap.js" type="text/javascript"></script>

<script type="text/javascript" src="js/macxdnie81.js"></script>
<script type="text/javascript" src="js/maxcdnie82.js"></script>

<script src="js/bootstrap-datepicker.js" type="text/javascript"></script>
<script type="text/javascript" src="js/jquery.cookie.min.js"></script>
<script type="text/javascript" src="js/jquery.collapsible.min.js"></script>
<script type="text/javascript" src="js/highlight.pack.js"></script>
      
      <script>
      
      function ormOutstandingdetails() {
            
            var customerCIF = $('#customerCIF').val();
            var boeIeCode = $('#boeIeCode').val();
            var ormNum = $('#ormNum').val();
            var boeFrmDate = $('#ormFrmDate').val();
            if(customerCIF == '' && boeIeCode == '' && ormNum == '' && boeFrmDate == ''){
                  alert('Please Enter Atleast One Value to Search');
          }
            else {
                  $("#formId").attr("action", "fetchoutstandingORM");
                  $("#formId").submit();
            }
            
            
      }
      function selectORM(event) {

            $(".customerlist").removeClass("highlighted");
            $(event).addClass("highlighted");
            var value = $(event).find("td").eq(0).text().trim();
            //alert(value);
            $("#paymentRefNo").val(value);
            $("#formId").attr("action", "outStandingORMToORM");
            $("#formId").submit();
            
      }

      function returnHome() {
            $("#formId").attr("action", "outStandingORMToORM");
            $("#formId").submit();
      }
      
</script>
</head>

<body class="body_bg">

      <%@ include file="/view/includes/TITLE.jsp"%>

      <boe:form method="post" id="formId" name="form">
      
            <div class="row">
            
                  <div class="col-md-2">
                  
                        <div class="side_nav">
                              <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;"><a href="#" onclick="returnHome()">Close</a></li>
                              </ul>
                        </div>
                        <br />
                        
                        <!-- <div class="side_nav">
                              <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;"><a href="#" id="okId">OK</a></li>
                                    <li style="text-align: center;"><a href="manualBOE" id="Reset">Reset</a></li>
                              </ul>
                        </div>
                        <br /> -->

                  </div>

                  <div class="col-md-10 content_box">

                        <h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ORM Outstanding Search</h5>                     

                        <div id="userIdMessage" style="color: orange"></div>
                        
                        <div class="row cont_colaps">


                              <!-- First Block is Started  -->

                              <div class="col-md-12">
                                    <!-- <div class="page_collapsible" id="body-section1">
                                          <span></span>
                                          <h5 style="font-weight: bold; font-size: 13px;">Bill Of Entry</h5>
                                    </div> -->

                                    <div class="row page_content">
                                    
                                          <div class="col-md-12">
                                                <div class="col-md-4">
                                                      <div class="form-group">
                                                            <label class="col-md-5 Control-label" style="font-weight: normal;">CIF</label>
                                                            <div class="col-md-5 input-group input-group-md">
                                                                  <boe:textfield type="text" name="boeVO.customerCIF" value="" id="customerCIF" class="form-control text_box" />
                                                            </div>
                                                      </div>
                                                </div>
                                                <div class="col-md-4">
                                                      <div class="form-group">
                                                            <label class="col-md-5 Control-label" style="font-weight: normal;">IE Code</label>
                                                            <div class="col-md-5 input-group input-group-md">
                                                                  <boe:textfield type="text" name="boeVO.boeIeCode" value="" id="boeIeCode" class="form-control text_box" />
                                                            </div>
                                                      </div>
                                                </div>

                                                <div class="col-md-4">
                                                      <div class="form-group">
                                                            <label class="col-md-6 Control-label" style="font-weight: normal;">Payment Reference No</label>
                                                            <div class="col-md-5 input-group input-group-md">
                                                                  <boe:textfield name="boeVO.ormNum" value="" id="ormNum" class="form-control text_box"/>
                                                            </div>
                                                      </div>
                                                </div>
                                                
                                          </div>
                                          
                                          <div class="col-md-12">
                                                
                                                
                                                <div class="col-md-4">
                                                      <div class="form-group">
                                                            <label class="col-md-5 Control-label"
                                                                  style="font-weight: normal;">FROM DATE</label>
                                                            <div class="col-md-5 input-group input-group-md">
                                                                  <boe:textfield id="ormFrmDate" name="boeVO.ormFrmDate"
                                                                        cssClass="datepicker form-control text_box" placeholder="dd/mm/yyyy">
                                                                  </boe:textfield>
                                                            </div>
                                                      </div>
                                                </div>
                                                <div class="col-md-4">
                                                      <div class="form-group">
                                                            <label class="col-md-5 Control-label"
                                                                  style="font-weight: normal;">TO DATE</label>
                                                            <div class="col-md-5 input-group input-group-md">
                                                                  <boe:textfield id="ormToDate" name="boeVO.ormToDate"
                                                                        cssClass="datepicker form-control text_box" placeholder="dd/mm/yyyy">
                                                                  </boe:textfield>
                                                            </div>
                                                      </div>
                                                </div>
                                                
                                                <div class="col-md-4">
                                                      
                                                      <div class="form-group">
                                                            <div class="col-md-5 input-group input-group-md" style="padding-left: 15px; padding-right: 15px;">
                                                                  <input type="button" value="Refresh" class="button"
                                                                  onclick="ormOutstandingdetails()" />
                                                            </div>
                                                      </div>
                                                      
                                                </div>
                                                
                                                
                                          </div>
                                          
                                    </div>
                              

                                    <div style="clear: both; height: 20px;"></div>


                        <div class="row page_content">

                              <div class="col-md-12">
                                    <div class="row page_content">

                                          <div class="page_collapsible" id="body-section1">
                                                <span></span>
                                                <h5 style="font-weight: bold; font-size: 13px; color: #527BB8;">&nbsp;
                                                      <boe:text name="ORM OutStanding List" />
                                                </h5>
                                          </div>
                                          <div class="col-md-12">
                                                <!-- <div class="table"> -->
                                                <table border="1px" align="left">
                                                      <tbody>
                                                            <tr>
                                                                  <th align="left" style="width: 400px; padding: 4px 5px;">Payment Reference</th>
                                                                  <th align="left" style="width: 400px; padding: 4px 5px;">Event Reference</th>
                                                                  <th align="left" style="width: 400px; padding: 4px 5px;">Payment date</th>
                                                                  <th align="left" style="width: 400px; padding: 4px 5px;">Outstanding Amount</th>
                                                                  <th align="left" style="width: 400px; padding: 4px 5px;">ACK/NACK</th>
                                                            </tr>

                                                      
                                                            <boe:iterator value="ormList" id="ormList" status="list">
                                                                  <tr ondblclick="selectORM(this)">
                                                                        <td style="padding: 4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <boe:property value="payRef"/>
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding: 4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <boe:property value="eveRef"/>
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding: 4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <boe:property value="txnDate" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding: 4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <boe:property value="osAmt" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td style="padding: 4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <boe:property value="ackNackStatus" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                  </tr>
                                                            </boe:iterator>

                                                      </tbody>
                                                </table>
                                                <br /> <br />
                                                <!-- </div> -->
                                          </div>
                                    </div>
                              </div>

                        </div>
                        <div>
                              <boe:hidden id="paymentRefNo" name="boeVO.paymentRefNo"></boe:hidden>
                              <boe:hidden id="paymentDatIdd" name="boeVO.paymentDatIdd"></boe:hidden>
                              <boe:hidden id="customerIdd" name="boeVO.customerIdd"></boe:hidden>
                              <boe:hidden id="benefName" name="boeVO.benefName"></boe:hidden>
                              <boe:hidden id="payCurrIdd" name="boeVO.payCurrIdd"></boe:hidden>
                              <boe:hidden id="osAmt" name="boeVO.osAmt"></boe:hidden>
                              <boe:hidden id="partPaymentSlNo" name="boeVO.partPaymentSlNo"></boe:hidden>
                              <boe:hidden id="cifNoIdd" name="boeVO.cifNoIdd"></boe:hidden>
                              <boe:hidden id="iecodeIdd" name="boeVO.iecodeIdd"></boe:hidden>
                              <boe:hidden id="benefCountry" name="boeVO.benefCountry"></boe:hidden>
                              <boe:hidden id="payAmountIdd" name="boeVO.payAmountIdd"></boe:hidden>
                              <boe:hidden id="fullyAlloc" name="boeVO.fullyAlloc"></boe:hidden>
                        </div>

                              </div>
                        </div>
                  </div>
                  </div>
                                    
                  <div id="dialogBOE" title="Error Message"></div>
      </boe:form>

</body>
</html>