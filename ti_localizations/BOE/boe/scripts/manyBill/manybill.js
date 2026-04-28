$(document).ready(function() {            
      
      var sysDate =  $("#sysProcDate").val().split("-");
      var sysProcDate = new Date(sysDate[2], sysDate[1] - 1, sysDate[0]);
      
      $("#billEntryDateId").datepicker({
             changeMonth: true,
           changeYear: true,
             dateFormat : 'dd/mm/yy'
      });
      
            
      $("#transType").change(function() {       
            transTypeAction();
      });   

      $("#okId").click(function() {
            
            var checkBoxCount = $('input[type="checkbox"]:checked').length;

        if(checkBoxCount == 0){
            
            $("#dialogBOE").html("Please Select Atleast One Invoice");
            
            $("#dialogBOE").dialog("open");
            
            $("#dialogBOE").dialog({autoOpen: false,modal: true,title: "Error Message",
                  buttons: {
                    Close: function () {
                        $("#dialogBOE").dialog('close');
                    }
                }
            });
            
            return false;
        }else{

            var totalEndorseAmt = 0;
            
            var boeAllocAmt   = isValueCheck(document.getElementById("alloAmtId").value);
            
            var tbl_len = document.getElementById('invTable').rows.length - 1;      

            if (tbl_len > 0) {            

                  for ( var j = 1; j <= tbl_len; j++) {     
                        
                        var isEditVal = $("#chkId_"+j).prop('checked');       
                              
                        if(isEditVal == true){  
                              
                              var tempPayAmt          = isValueCheck(document.getElementById("realAmt_"+j).value);
                              
                        totalEndorseAmt   = parseFloat(totalEndorseAmt) +  parseFloat(tempPayAmt);
                        
                        totalEndorseAmt         = totalEndorseAmt.toFixed(2);
                              
                        }
                  }
            }
            
            
            if(parseFloat(boeAllocAmt) != parseFloat(totalEndorseAmt) ){
                  
                  $("#dialogBOE").html("Total Realized Invoice Amount Should Be Match With BOE Allocated Amount");
                  
                  $("#dialogBOE").dialog("open");
                  
                  $("#dialogBOE").dialog({autoOpen: false,modal: true,title: "Error Message",
                        buttons: {
                          Close: function () {
                              $("#dialogBOE").dialog('close');
                          }
                      }
                  });
                  
                  return false;
                  
            }else{
                  $('#formId').attr('action', 'storeBillData');
                  $('#formId').submit();
            }     
        }
            
      });
      
      
      /*Boe Modification OK Button Script*/
      
$("#modify").click(function() {
            
            var checkBoxCount = $('input[type="checkbox"]:checked').length;

        if(checkBoxCount == 0){
            
            $("#dialogBOE").html("Please Select Atleast One Invoice");
            
            $("#dialogBOE").dialog("open");
            
            $("#dialogBOE").dialog({autoOpen: false,modal: true,title: "Error Message",
                  buttons: {
                    Close: function () {
                        $("#dialogBOE").dialog('close');
                    }
                }
            });
            
            return false;
        }else{

            var totalEndorseAmt = 0;
            
            var boeAllocAmt   = isValueCheck(document.getElementById("alloAmtId").value);
            
            var tbl_len = document.getElementById('invTable').rows.length - 1;      

            if (tbl_len > 0) {            

                  for ( var j = 1; j <= tbl_len; j++) {     
                        
                        var isEditVal = $("#chkId_"+j).prop('checked');       
                              
                        if(isEditVal == true){  
                              
                              var tempPayAmt          = isValueCheck(document.getElementById("realAmt_"+j).value);
                              
                        totalEndorseAmt   = parseFloat(totalEndorseAmt) +  parseFloat(tempPayAmt);
                        
                        totalEndorseAmt         = totalEndorseAmt.toFixed(2);
                              
                        }
                  }
            }
            
            
            if(parseFloat(boeAllocAmt) != parseFloat(totalEndorseAmt) ){
                  
                  $("#dialogBOE").html("Total Realized Invoice Amount Should Be Match With BOE Allocated Amount");
                  
                  $("#dialogBOE").dialog("open");
                  
                  $("#dialogBOE").dialog({autoOpen: false,modal: true,title: "Error Message",
                        buttons: {
                          Close: function () {
                              $("#dialogBOE").dialog('close');
                          }
                      }
                  });
                  
                  return false;
                  
            }else{
                  $('#formId').attr('action', 'storeBillData_M');
                  $('#formId').submit();
            }     
        }
            
      });
      
      
      
      
      
      $("#alloAmtId").change(function(event) {              

            var allamt = isValueCheck($("#alloAmtId").val().replace(/,/g, ''));           
            
            var boeAmt = isValueCheck($("#bilEAId").val().replace(/,/g, ''));
            
            var actualAmount = isValueCheck($("#actualAmtId").val().replace(/,/g, ''));         
      
            var outStPayAmtId = isValueCheck($("#outStPayAmtId").val().replace(/,/g, ''));
            
            var outStPayAmtId_temp = isValueCheck($("#outStPayAmtId_temp").val().replace(/,/g, ''));        
            
            var exRate = isValueCheck(document.getElementById("boeExRate").value);
            
            var eqBoeAllocAmt = parseFloat(exRate) * parseFloat(allamt);
            
            var eqBoeAllocAmtRound = eqBoeAllocAmt.toFixed(2);
            
            $('#eqPaymentAmount').val(eqBoeAllocAmtRound);        
                                                                  
            if (parseFloat(allamt) <= parseFloat(outStPayAmtId_temp)) {
      
                  if (parseFloat(eqBoeAllocAmtRound) <= parseFloat(actualAmount)) {
      
                        var allamt = isValueCheck($("#alloAmtId").val().replace(/,/g, ''));
                        
                        var outamt = parseFloat(outStPayAmtId_temp) - parseFloat(allamt);
                        
                        if(outamt == "0.00"){
                              outamt = 0;
                        }
                        
                        $("#outStPayAmtId").val(outamt.toFixed(2));
      
                        if(outamt == 0.0 || outamt == 0){                           
                              $("#fullyAllocId").val("Y");
                              $("#fullyAllocId1").val("Y");
                        }else if(outamt != 0.0 || outamt != 0 ){
                              $("#fullyAllocId").val("N");
                              $("#fullyAllocId1").val("N");
                        }                   
                        
                  }else{
                        
                        $("#dialogBOE").html('Payment Amount In BOE Currency Should Not Exceed BOE Amount Available For Endorsement');
                  
                  $("#dialogBOE").dialog('open');
                  
                  $("#dialogBOE").dialog({autoOpen: false,modal: true,title: "Error Message",
                        buttons: {
                          Close: function () {
                              $(this).dialog('close');
                          }
                      }
                  });               
                  
            
                  $("#outStPayAmtId").val(outStPayAmtId_temp);                
                  
                  $("#eqPaymentAmount").val(0);
                        
                        $(this).val(0);
                        
                        return false;
                  }
                  
            }else{
                  
                  $("#dialogBOE").html('BOE Amount In Payment Currency Should Not Exceed Outstanding Payment Amount ');
            
            $("#dialogBOE").dialog('open')
        
                $("#dialogBOE").dialog({autoOpen: false,modal: true,title: "Error Message",
                  buttons: {
                       Close: function () {
                         $(this).dialog('close');
                       }
                   }
            });         
        
            
            $("#outStPayAmtId").val(outStPayAmtId_temp);          
            
            $("#eqPaymentAmount").val(0);
                  
                  $(this).val(0);
                  
                  return false;
            }
            
      });               
});





function removeComma(value) {
      value = value.replace(/\,/g, "");
      return value;
}


function removeZero(value) {
      var n = value.length;
      if(n>1){
            value = value.replace(/\b0+/g, '');
      }
      return value;
}     

function isValueEmpty(value) {
      
      if (trim(value) == "" || value == null){
                  return true;
      }else{
                  return false;
      }
}


function trim(str) {
      return str.replace(/^\s+|\s+$/g, "");
}

function intToFormat(nStr){
      nStr += '';
      x = nStr.split('.');
      x1 = x[0];
      x2 = x.length > 1 ? '.' + x[1] : '';
      var rgx = /(\d+)(\d{3})/;
      var z = 0;
      var len = String(x1).length;
      var num = parseFloat((len/2)-1);
      while (rgx.test(x1)) {
             if(z > 0){
                x1 = x1.replace(rgx, '$1' + ',' + '$2');
           }else{
                    x1 = x1.replace(rgx, '$1' + ',' + '$2');
                    rgx = /(\d+)(\d{2})/
           }
             z++;
           num--;
             if(num == 0){
                   break;
             }
    }
  return x1 + x2;
}     

$(function() {
      
      transTypeAction();
      
      $("#dialogBOE").dialog({                    
          autoOpen: false
      });
      
});

            
function retriveDataBasedOnBillNO() {
      
      var boeNo    = $('#billRefNo').val();
      
    var boeDate  = $('#billEntryDateId').val();
   
    var portCode = $('#portcode').val();
   
    if(boeNo == ''){

      $("#dialogBOE").html('Please Enter BOE Number');
      
      $("#dialogBOE").dialog('open')
 
          $("#dialogBOE").dialog({autoOpen: false,modal: true,title: "Error Message",
               buttons: {
             Close: function () {
                   $(this).dialog('close');
              }
              }
          });     
      
      return false;
      
    }else if(boeDate == ''){

        $("#dialogBOE").html('Please Enter BOE Date');
            
        $("#dialogBOE").dialog('open')
     
          $("#dialogBOE").dialog({autoOpen: false,modal: true,title: "Error Message",
               buttons: {
             Close: function () {
                   $(this).dialog('close');
              }
              }
          });     
       
        return false;
            
    }else if(portCode == ''){
      
      $("#dialogBOE").html('Please Enter Portcode');
      
        $("#dialogBOE").dialog('open')
     
          $("#dialogBOE").dialog({autoOpen: false,modal: true,title: "Error Message",
            buttons: {
                  Close: function () {
                  $(this).dialog('close');
                   }
                 }
             });  
       
        return false;
       
    }else{
      $('#formId').attr('action', 'retriveDataBasedOnBillNO');
      $('#formId').submit();
    }
      
}     

function fetchAllocateInvoice() {
      $('#formId').attr('action', 'fetchAllocateInvoice');
      $('#formId').submit();
}


function fetchAllocateInvoice1() {
      $('#formId').attr('action', 'fetchAllocateInvoice_M');
      $('#formId').submit();
}



function isNumber(evt) {
      evt = (evt) ? evt : window.event;
      var charCode = (evt.which) ? evt.which : evt.keyCode;
      if (charCode > 31 && (charCode < 45 || charCode > 57)) {
            return false;
      }
      return true;
}


function amountCalculation() {
      
      var exRate = isValueCheck(document.getElementById("boeExRate").value);
      
      var tempExRate = isValueCheck(document.getElementById("exchangeRate").value);
      
      if (exRate != '') {
            
            var ormAllocAmt = isValueCheck(document.getElementById("alloAmtId").value.replace(/,/g, ''));
            
            var eqBoeAllocAmt = parseFloat(exRate) * parseFloat(ormAllocAmt);
            
            var eqBoeAllocAmtRound = eqBoeAllocAmt.toFixed(2);
            
            if(eqBoeAllocAmtRound == "0.00") {
                  eqBoeAllocAmtRound = 0;
            }
            
            $('#eqPaymentAmount').val(eqBoeAllocAmtRound);
      }
      
      var actualAvlAmt = isValueCheck(document.getElementById("actualAmtId").value.replace(/,/g, ''));
      
      var eqBOEAmt = isValueCheck($('#eqPaymentAmount').val());
      
    if (parseFloat(actualAvlAmt) < parseFloat(eqBOEAmt)) {
            
            $("#dialogBOE").html('Payment Amount In BOE Currency Should Not Exceed BOE Amount Available For Endorsement');
            
              $("#dialogBOE").dialog('open')
            
            $("#dialogBOE").dialog({autoOpen: false,modal: true,title: "Error Message",
                  buttons: {
                        Close: function () {
                        $(this).dialog('close');
                         }
                       }
                   });    
              
              
              var ormAllocAmt = isValueCheck(document.getElementById("alloAmtId").value.replace(/,/g, ''));         
              
              $('#boeExRate').val(tempExRate);
                  
                  var eqBoeAllocAmt = parseFloat(tempExRate) * parseFloat(ormAllocAmt);
                  
                  var eqBoeAllocAmtRound = eqBoeAllocAmt.toFixed(2);
                  
                  if(eqBoeAllocAmtRound == "0.00") {
                        eqBoeAllocAmtRound = 0;
                  }
                  
                  $('#eqPaymentAmount').val(eqBoeAllocAmtRound);
                  
                  return false;
              
        }   
   
}


function viewInvoiceData() {
      
       var invVal = $("#invValue").val();
      
       if (invVal == "" ) {
          $('#selectrow').css('display', 'block');
          return false;
       }else {    
          $('#selectrow').css('display', 'none');
          
          $('#buttonType').val("View");   

            $("#formId").attr("action", "insertInvoiceData");
            $("#formId").submit();      
       }    
}

function exRateInvoiceData() {
      
       var invVal = $("#invValue").val();
      
       if (invVal == "" ) {
          $('#selectrow').css('display', 'block');
          return false;
       }else {    
          $('#selectrow').css('display', 'none');
          
            $("#formId").attr("action", "crossCurrencyPage");
            $("#formId").submit();      
       }    
}


function selectData(event){     
      
       $(".invList").removeClass("highlighted");
       $(event).addClass("highlighted");  
      
       var value1 = $(event).find("td").eq(0).text().trim();
       var value2 = $(event).find("td").eq(1).text().trim();

       var invValue = value1 + ':' + value2;    
      
       $("#invValue").val(invValue);            
      
}

function getInvoiceList(event){

      var closeId = $(event).attr('id');
      var test = closeId.split('_');
      var cVal = test[1];     
      
      var allocAmt = $("#alloAmtId").val().replace(/,/g, '');
      
      var utilityRefNo = document.getElementById("chkId_"+cVal).value;
      
      var realAmt   = isValueCheck(document.getElementById("realAmt_"+cVal).value); 
      
      var realAmtIC = isValueCheck(document.getElementById("realAmtIC_"+cVal).value);     
      
      var outAmtIC = isValueCheck(document.getElementById("totalOutAmtIC_"+cVal).value);        
      
      var condVal = $(event).prop('checked');
      
      if(condVal == true){
            
            var group = document.getElementsByName('chkInvlist');       
            
            var sumOfRealAmt = 0;
            
            var j = 0;

          for ( var i = 0; i < group.length; i++) {
            
            j = j + 1;
                  
            if(group.item(i).checked == true){              
                  
                  var tempRealAmt = isValueCheck(document.getElementById("realAmt_"+j).value);  
      
                  sumOfRealAmt = parseFloat(sumOfRealAmt) +  parseFloat(tempRealAmt);     
            }           
          }
          
          sumOfRealAmt = sumOfRealAmt.toFixed(2);
                
          if (parseFloat(allocAmt) < parseFloat(sumOfRealAmt)) {
            
            $("#dialogBOE").html('Realized Amount Should Not Exceed Endorse Amount');
            
              $("#dialogBOE").dialog('open')
            
            $("#dialogBOE").dialog({autoOpen: false,modal: true,title: "Error Message",
                  buttons: {
                        Close: function () {
                        $(this).dialog('close');
                         }
                       }
                   });    
              
              $('#chkId_'+cVal).attr('checked', false);
              
              return false;     
              
          }else if (parseFloat(outAmtIC) < parseFloat(realAmtIC)) {
            
            $("#dialogBOE").html('Realized AmountIC Should Not Exceed OutStanding AmountIC');
            
              $("#dialogBOE").dialog('open')
            
            $("#dialogBOE").dialog({autoOpen: false,modal: true,title: "Error Message",
                  buttons: {
                        Close: function () {
                        $(this).dialog('close');
                         }
                       }
                   });  
              
              $('#chkId_'+cVal).attr('checked', false);
              
              return false;    
              
          }else if(parseFloat(realAmt) == 0){
            
            $("#dialogBOE").html('Please Enter the Realized Amount');
            
              $("#dialogBOE").dialog('open')
            
            $("#dialogBOE").dialog({autoOpen: false,modal: true,title: "Error Message",
                  buttons: {
                        Close: function () {
                        $(this).dialog('close');
                         }
                       }
                   });  
              
              $('#chkId_'+cVal).attr('checked', false);
              
              return false;    
            
          }else{

            $("#realAmt_"+cVal).attr("readonly","readonly");
            
                  var tempVal = utilityRefNo + ':' + realAmt + ':' + realAmtIC;     
                  
                  document.getElementById("chkId_"+cVal).value = tempVal;                 
            }
          
      }else{
            
            $("#realAmt_"+cVal).removeAttr("readonly");     
            
            var temp_refNo = utilityRefNo.split(':');
            
            var tempData = temp_refNo[0] + ':' + temp_refNo[1] + ':' + temp_refNo[2];
            
            document.getElementById("chkId_"+cVal).value = tempData;    
      }     
}

function setRealizedIC(event){

      var closeId = $(event).attr('id');
      var test = closeId.split('_');
      var cVal = test[1];     
      
      var exRate    = isValueCheck(document.getElementById("boeExRate").value);
      
      var realAmt   = isValueCheck(document.getElementById("realAmt_"+cVal).value); 
      
      var realAmtIC = parseFloat(realAmt) * parseFloat(exRate);
      
      document.getElementById("realAmtIC_"+cVal).value = realAmtIC.toFixed(2);
      
}


function transTypeAction(){
      
      var mpdValue = document.getElementById("mpdValue").value;
      
      if(mpdValue == 'Y'){
            
            $("#exInv").hide();           
            
      }
}


function isValueCheck(value) {
      
      if (trim(value) == "" || value == null){
                  return "0";
      }else{
                  return value;
      }
}


function overRide(event) {

      $(".boeErrorMsg").removeClass("highlighted");
      $(event).addClass("highlighted");   
      
      var value1 = $(event).find("td").eq(4).text().trim();

      if (value1 == 'N') {
            
            document.getElementById("overridStatus").value = 'Y'; 
            $("#hideFromWarning").val("Y");
            $('#formId').attr('action', 'updateWarning');
            $('#formId').submit();
            
      }else if(value1 == 'Y') {

            document.getElementById("overridStatus").value = 'N';
            $("#hideFromWarning").val("Y");
            $('#formId').attr('action', 'updateWarning');
            $('#formId').submit();
            
      }else {
            
            $("#dialogBOE").html('Errors Cannot be Overridden');
      
        $("#dialogBOE").dialog('open')
     
      $("#dialogBOE").dialog({autoOpen: false,modal: true,title: "Error Message",
            buttons: {
                  Close: function () {
                  $(this).dialog('close');
                   }
                 }
             });  
            
        return false;
      }           
      
}




// Added on 28122017 to validate Alpha and Numeric values
function alphanumeric(val) {
      val = (val) ? val : window.event;
      var charCode = (val.which) ? val.which : val.keyCode;
      if ((charCode > 47 && charCode < 58) || (charCode > 64 && charCode < 91)
                  || (charCode > 96 && charCode < 123)) {
            return true;
      }
      return false;
}