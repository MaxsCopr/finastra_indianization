<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="firc" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<!-- <meta http-equiv="Cache-control" content="no-cache" />
<meta http-equiv="Expires" content="-1" /> -->

<title>IRM Closure Search</title>

<jsp:include page="/view/common/header.jsp" />

<script type="text/javascript">

      $(document).ready(function() {
            
             $("#Search").click(function(){
                   $("#form1").attr("action","irmAdjSearch");
                   $("#form1").submit();
            });   
            
             $("#dateFrom").datepicker({
                   changeMonth: true,
                   changeYear: true,
                   dateFormat : 'dd-mm-y'
            });   
                  
            $("#dateTo").datepicker({
                  changeMonth: true,
                  changeYear: true,
                  dateFormat : 'dd-mm-y'
            });
            
      });
                  
      </script>
 
</head>

<body class="body_bg" onload="display_ct()">
<%@ include file="/view/includes/TITLE.jsp" %>  

  <firc:form method="post" id="form1" name="form">
      <div class="row">
            <div class="col-md-2">
                  <div class="side_nav"  style="width: 200px; ">
                        <ul class="nav nav-pills nav-stacked">
                              <li style="text-align: center;"><a href="home">Close</a></li>
                        </ul>
                  </div>
                  <br />
                  <div class="side_nav">
                              <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;"><a href="irmExSearch" id="reset">Reset</a></li>
                              </ul>
                  </div>
                  <br />
                  
            </div>
                  
            <div class="col-md-10 content_box">

            <h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;IRM Adjustment/Closure</h5>
                  <div id="userIdMessage" style="color: orange"></div>
                        <div class="row cont_colaps">
                              <div class="col-md-12">
                                    <div class="page_collapsible " id="body-section1">
                                          <span></span>
                                          <h5 style="font-weight: bold; font-size: 13px;">&nbsp;Input Details</h5>
                                    </div>
                                    <div class="row page_content">
                                          <div class="col-md-6">              
                                          
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">IRM Closure Created From </label>

                                                      <div class="col-md-4 input-group input-group-md" style="float:left;">
                                                            <firc:textfield id="dateFrom" name="issuanceVO.dataFrom" readonly="true"
                                                                   cssClass="datepicker form-control text_box" />
                                                      </div>

                                                      <label class="col-md-1 Control-label"
                                                            style="font-weight: normal;">To</label>

                                                      <div class="col-md-3 input-group input-group-md">
                                                            <firc:textfield id="dateTo" name="issuanceVO.dataTo" readonly="true"
                                                                   cssClass="datepicker1 form-control text_box" />
                                                      </div>                                                      

                                                </div> <br/>                        
                                                
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Status</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:select list="irmStatus" name="issuanceVO.status"  id="status"
                                                                  headerValue = "<----->" headerKey=""      style="width: 180px; height: 25px"/>
                                                      </div>
                                                </div>      <br/>
                                                

                                          </div>
                                          
                                          
                                          
                                          <div class="col-md-6">  
                                          
                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;">Master Reference No.</label>
                                                      <div class="col-md-4 input-group input-group-md">
                                                            <firc:textfield id="transRefNo" name="issuanceVO.transrefno"
                                                                  cssClass="form-control text_box"  />
                                                      </div>
                                                </div>      <br/>                                           

                                                <div class="form-group">
                                                      <label class="col-md-4 Control-label"
                                                            style="font-weight: normal;"></label>
                                                      <div class="col-md-3 input-group input-group-md">
                                                            <input type="button" value="Search" class="button" id="Search" style="margin-top:5px;"/>
                                                      </div>
                                                </div>      <br/>                                                             

                                          </div>

                                    </div>
                              
                              <div class="col-md-12">
                                    <div class="row page_content">
                                          <div class="page_collapsible" id="body-section1">
                                                <span></span>
                                                <h5 style="font-weight: bold; font-size: 13px;">&nbsp;IRM Adjustment/Closure</h5>
                                          </div>
                                          <div class="form-group">
                                                <div class="table">
                                                      <table border="1px" align="left">
                                                               <tbody>
                                                                   <tr>                                                                   
                                                                        
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label>Master Reference No</label></th>
                                                                        
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label>ADCode</label></th>                                                                                                         
                                                                        
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label>IECode</label></th>
                                                                        
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label>Currency</label></th>
                                                                        
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label>Adjusted Amount</label></th>
                                                                        
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label>Approved by</label></th>

                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label>Letter No</label></th>
                                                                        
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label>Adjustment Date</label></th>
                                                                        
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label>Reason for Adjustment</label></th>
                                                                        
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label>Document No</label></th>
                                                                                    
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label>Document Date</label></th>
                                                                        
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label>Port of Receiving</label></th>
                                                                        
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label>Record Indicator</label></th>
                                                                        
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label>Closure Sequence No</label></th>                                                        
                                                                        
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label>Remarks</label></th>
                                                                                                            
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label>Status</label></th>
                                                                        
                                                                         <th style="text-align: left; width:100px; padding:4px 5px;"><label>ACK Status</label></th>
                                                                        
                                                                     </tr>
                                                                      
                                                                      <c:if test="${empty irmClList}">
                                                                    <tr>
                                                                  <td colspan="17">No records found</td>
                                                                   </tr>
                                                                   </c:if>
                                                                  <c:if test="${not empty irmClList}">
                                                                  
                                                                  <firc:iterator value="irmClList" id="irmClList">
                                                                  
                                                                  <tr class="iClList">
                                                                  
                                                                      <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                <firc:property value="transrefno"/>
                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="adCode"/>                                                                                   
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="ieCode"/>                                                                                   
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="currency"/>                                                                                 
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="amount"/>                                                                                   
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="extInd"/>                                                                                   
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>       
                                                                                                                        
                                                                        
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="letterNo"/>   
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>       
                                                                                                
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="adjDate"/>    
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        
                                                                  
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                              <firc:property value="reason"/>                                                                             
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                              <firc:property value="docNo"/>                                                                              
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                              <firc:property value="docDate"/>                                                                            
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                              <firc:property value="docPort"/>                                                                            
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="recInd"/>                                                                                   
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                              <firc:property value="closureSeqNo"/>                                                                             
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="remarks"/>                                                                                  
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                                    <firc:property value="status"/>                                                                                   
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        
                                                                        <td style="padding:4px 5px;">
                                                                              <div class="form-group" align="left">
                                                                              <firc:property value="ackStatus"/>                                                                          
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>                                                                                                                   
                                                                  
                                                                  </tr>
                                                                  </firc:iterator>
                                                                  </c:if>
                                                                  
                                                                  </tbody>
                                                            </table><br /><br />
                                                </div>
                                          </div>
                                    </div>
                              </div>
                              </div>
                        </div>
                  </div>
            </div>
      </firc:form>
</body>
</html>