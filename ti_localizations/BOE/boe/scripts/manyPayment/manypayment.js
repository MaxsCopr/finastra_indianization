$(document).ready(function() {      
      
      var sysDate =  $("#sysProcDate").val().split("-");
      
      var sysProcDate = new Date(sysDate[2], sysDate[1] - 1, sysDate[0]);
      
      $("#billEntryDateId").datepicker({
             changeMonth: true,
           changeYear: true,
             dateFormat : 'dd/mm/yy'
      });   
      
            
      $("#paymentDateFrom").datepicker({
            changeMonth: true,
          changeYear: true,
            maxDate : sysProcDate,
            dateFormat : 'dd/mm/yy'
      });
      
      $("#paymentDateTo").datepicker({
            changeMonth: true,
          changeYear: true,
            maxDate : sysProcDate,
            dateFormat : 'dd/mm/yy'
      });
            
      $("#transType").change(function() {       
            transTypeAction();            
      });   
      
      $("#paymentFetch").click(function(){
            
            var cifID    = $('#cifId').val();
        
          if(cifID == ''){

            $("#dialogBOE").html('Please Enter CIF No');
            
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
            
            doload();
            
            $('#formId').attr('action','fetchPaymentData');
                  $('#formId').submit();
          }     
            
      });
      
      $("#okId").click(function() {
            
            var checkBoxCount = $('input[type="checkbox"]:checked').length;   
            
            var invCount = $('input[type="radio"]:checked').length;
            
            if(checkBoxCount == 0){
            
            $("#dialogBOE").html("Please Select Atleast One Bill");
            
            $("#dialogBOE").dialog("open");
            
            $("#dialogBOE").dialog({autoOpen: false,modal: true,title: "Error Message",
                  buttons: {
                    Close: function () {
                        $("#dialogBOE").dialog('close');
                    }
                }
            });   
            
            return false;
            
         }else if(invCount == 0){
            
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
            
            doload();
            
            $('#formId').attr('action','billNoToManyPayAction');
            $('#formId').submit();              
        }   

      });
      
});



function doload(){
       $('body').modal({
            show : 'false'
       });
       $('body').removeClass('removePageLoad');
       $('body').addClass('addPageLoad');
}


function fetchBOEDetails(){
      
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
      
      doload();
      $('#btnModify').val('');
      $('#formId').attr('action', 'fetchBOEDetails');
      $('#formId').submit();
    }
}

function removeComma(value1) {
      var value = value1.replace(/\,/g, "");
      return value;
}

function removeZero(value) {
      var n = value.length;
      if (n > 1) {
            value = value.replace(/\b0+/g, '');
      }
      return value;
}

function isValueEmpty(value) {
      if (trim(value) == "" || value == null) {
            return true;
      } else {
            return false;
      }
}

function trim(str) {
      return str.replace(/^\s+|\s+$/g, "");
}


$(function() {
      
      transTypeAction();
      
      $("#dialogBOE").dialog({                    
          autoOpen: false
      });
      
});


function isNumber(evt) {
      evt = (evt) ? evt : window.event;
      var charCode = (evt.which) ? evt.which : evt.keyCode;
      if (charCode > 31 && (charCode < 45 || charCode > 57)) {
            return false;
      }
      return true;
}

function intToFormat(nStr) {

      nStr += '';
      x = nStr.split('.');
      x1 = x[0];
      x2 = x.length > 1 ? '.' + x[1] : '';
      var rgx = /(\d+)(\d{3})/;
      var z = 0;
      var len = String(x1).length;
      var num = parseInt((len / 2) - 1);

      while (rgx.test(x1)) {
            if (z > 0) {
                  x1 = x1.replace(rgx, '$1' + ',' + '$2');
            } else {
                  x1 = x1.replace(rgx, '$1' + ',' + '$2');
                  rgx = /(\d+)(\d{2})/;
            }
            z++;
            num--;
            if (num == 0) {
                  break;
            }
      }
      return x1 + x2;
}

function isValueCheck(value) {
      
      if (trim(value) == "" || value == null){
                  return "0";
      }else{
                  return value;
      }
}


function transTypeAction(){
      
      var mpdValue = document.getElementById("mpdValue").value;
      
      if(mpdValue == 'Y'){
            
            $("#exInv").hide();
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

            $("#formId").attr("action", "insertInvoiceManyBill");
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
          
            $("#formId").attr("action", "crossCurrencyManyBill");
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

function amountCalculation(value){
      
      
      
      var closeId = $(value).attr('id');
      console.log('id----------->'+closeId)
      var test = closeId.split('_');
      var cVal = test[1];     
      
      var eqBoeAllocAmt =0;
      
      
      var allocAmt                  = isValueCheck(document.getElementById("allocAmt_"+cVal).value);
      
       $("#endorseAmtTemp_"+cVal).val('0');

      
      var exRate                    = isValueCheck(document.getElementById("exRate_"+cVal).value);    
      
      var endorseAmt                = isValueCheck(document.getElementById("endorseAmtTemp_"+cVal).value);
      
      
      var outAmt                    = isValueCheck(document.getElementById("outstandingAmtTemp_"+cVal).value);    
      
      var boeAvailAmt         = isValueCheck(document.getElementById("actualAmountId").value);
      
//    alert('boeAvailAmt---------->'+boeAvailAmt)
      
      var actualBoeAvailAmt   = isValueCheck(document.getElementById("billEAId").value);
      
      eqBoeAllocAmt = parseFloat(exRate) * parseFloat(allocAmt);
      
//    console.log('Original Multiplication------------------>'+eqBoeAllocAmt)
      
      var eqBoeAllocAmtRound = eqBoeAllocAmt.toFixed(2);    
      
      /*document.getElementById("allocAmt_").value = "";*/
      
      
//    console.log(' After fixed multiplication----------------->'+eqBoeAllocAmtRound)
      
//    console.log('boeAvailAmt------------------>'+boeAvailAmt)
      
      $("#boeAllocAmt_"+cVal).val(eqBoeAllocAmtRound);      
      
      var balOutAmt = parseFloat(outAmt) - parseFloat(allocAmt);
      
      $("#outstandingAmt_"+cVal).val(balOutAmt.toFixed(2));
      
      var totalEndAmt = parseFloat(endorseAmt) + parseFloat(allocAmt);
      
      $("#endorseAmt_"+cVal).val(totalEndAmt.toFixed(2));
      
      if(parseFloat(balOutAmt) == 0){
            $("#fullyAlloc_"+cVal).val("Y");
            $("#isEditField_"+cVal).val("Y");
      }else{
            $("#fullyAlloc_"+cVal).val("N");
            $("#isEditField_"+cVal).val("Y");
      }     
      
      var totalAllocateAmt = 0;
      
      var tblLenInv = document.getElementById('invoiceListTable').rows.length - 1;

      if (tblLenInv > 0) {          

            for ( var i = 1; i <= tblLenInv; i++) {                     
                        
               var tempInvAmtIC = isValueCheck(document.getElementById("alRealiedAmtIC_"+i).value);
            
               totalAllocateAmt = parseFloat(totalAllocateAmt) +  parseFloat(tempInvAmtIC);  
              
               totalAllocateAmt = totalAllocateAmt.toFixed(2);
                        
            }
      }           
      
      
      var totalEndorseAmt = 0;
      
      var tbl_len = document.getElementById('paymentListTable').rows.length - 1;

      if (tbl_len > 0) {            

            for ( var j = 1; j <= tbl_len; j++) {                 
                  
                  var isEditVal = $("#isEditField_"+j).val();                 
                        
                  if(isEditVal == "Y"){               
                        
                  var tempPayAmt = isValueCheck(document.getElementById("boeAllocAmt_"+j).value);
            
                  totalEndorseAmt = parseFloat(totalEndorseAmt) +  parseFloat(tempPayAmt);
                  
                  totalEndorseAmt = totalEndorseAmt.toFixed(2);
                }       
            }
      }     
            
      if(parseFloat(boeAvailAmt) < parseFloat(eqBoeAllocAmtRound)){
            
            /*$("#dialogBOE").html('Allocated Amount In BOE Currency  Should Not Exceed BOE Amount Available For Endorsement');*/
            $("#dialogBOE").html(' Payment Endorsed Amount / Allocated Amount In BOE Currency  Should Not Exceed BOE Amount Available For Endorsement');
      
        $("#dialogBOE").dialog('open')
     
      $("#dialogBOE").dialog({autoOpen: false,modal: true,title: "Error Message",
            buttons: {
                  Close: function () {
                  $(this).dialog('close');
                   }
                 }
             });
       
        $("#allocAmt_"+cVal).val('0');
       
        $("#boeAllocAmt_"+cVal).val('0');
       
        $("#outstandingAmt_"+cVal).val(outAmt);        
       
        $("#endorseAmt_"+cVal).val(endorseAmt);
       
        $("#fullyAlloc_"+cVal).val("N");        
       
        var totalEdAmt = 0;
      
      var tbl_len = document.getElementById('paymentListTable').rows.length - 1;

      if (tbl_len > 0) {            

            for ( var j = 1; j <= tbl_len; j++) {                 
                  
                  var isEditVal = $("#isEditField_"+j).val();                 
                        
                  if(isEditVal == "Y"){               
                        
                  var tempPayAmt = isValueCheck(document.getElementById("boeAllocAmt_"+j).value);
            
                  totalEdAmt = parseFloat(totalEdAmt) +  parseFloat(tempPayAmt);
                  
                  totalEdAmt = totalEdAmt.toFixed(2);
                }       
            }
      }     
       
        var totalAvailAmt = parseFloat(totalAllocateAmt) + parseFloat(totalEdAmt);
            
        totalAvailAmt = totalAvailAmt.toFixed(2);
            
            var currBoeAvlAmt = parseFloat(actualBoeAvailAmt) - parseFloat(totalAvailAmt);
                  
            $("#actualAmountId").val(currBoeAvlAmt.toFixed(2));
       
        return false;   
       
      }else{
            
            var totalAllAmt = parseFloat(totalAllocateAmt) + parseFloat(totalEndorseAmt);
            
            totalAllAmt = totalAllAmt.toFixed(2);
            
            var currentBoeAvlAmt = parseFloat(actualBoeAvailAmt) - parseFloat(totalAllAmt);
                  
            $("#actualAmountId").val(currentBoeAvlAmt.toFixed(2));
            
      }

      if(parseFloat(outAmt) < parseFloat(allocAmt)){
                  
                  $("#dialogBOE").html('Allocated Amount In Payment Currency Should Not Exceed Bill Outstanding Amount');
            
              $("#dialogBOE").dialog('open')
            
            $("#dialogBOE").dialog({autoOpen: false,modal: true,title: "Error Message",
                  buttons: {
                        Close: function () {
                        $(this).dialog('close');
                         }
                       }
                   });
              
              $("#allocAmt_"+cVal).val('0');
              
              $("#boeAllocAmt_"+cVal).val('0');
              
              $("#outstandingAmt_"+cVal).val(outAmt);        
              
              $("#endorseAmt_"+cVal).val(endorseAmt);
              
              $("#fullyAlloc_"+cVal).val("N");      
              
              var totalEdAmt = 0;
            
            var tbl_len = document.getElementById('paymentListTable').rows.length - 1;

            if (tbl_len > 0) {            

                  for ( var j = 1; j <= tbl_len; j++) {                 
                        
                        var isEditVal = $("#isEditField_"+j).val();                 
                              
                        if(isEditVal == "Y"){               
                              
                        var tempPayAmt = isValueCheck(document.getElementById("boeAllocAmt_"+j).value);
                  
                        totalEdAmt = parseFloat(totalEdAmt) +  parseFloat(tempPayAmt);
                        
                        totalEdAmt = totalEdAmt.toFixed(2);
                      }       
                  }
            }     
              
              var totalAvailAmt = parseFloat(totalAllocateAmt) + parseFloat(totalEdAmt);
                  
              totalAvailAmt = totalAvailAmt.toFixed(2);
                  
                  var currBoeAvlAmt = parseFloat(actualBoeAvailAmt) - parseFloat(totalAvailAmt);
                        
                  $("#actualAmountId").val(currBoeAvlAmt.toFixed(2));
              
              return false;               
       }
      
      eqBoeAllocAmt="";
      
}



 /*$('#allocAmt').change(function(){
      
      
      var closeId = $(event).attr('id');
      var test = closeId.split('_');
      var cVal = test[1];     
      
       $.ajaxSetup({
             async: false
                              
      
       });
       var allocAmt=0
      
        allocAmt              = $(this).document.getElementById("allocAmt_"+cVal).value;
//    var allocAmt                  = isValueCheck(document.getElementById("allocAmt_"+cVal).value);
      
      
      
      var exRate                    = isValueCheck(document.getElementById("exRate_"+cVal).value);    
      
      var endorseAmt                = isValueCheck(document.getElementById("endorseAmtTemp_"+cVal).value);  
      
      var outAmt                    = isValueCheck(document.getElementById("outstandingAmtTemp_"+cVal).value);    
      
      var boeAvailAmt         = isValueCheck(document.getElementById("actualAmountId").value);
      
//    alert('boeAvailAmt---------->'+boeAvailAmt)
      
      var actualBoeAvailAmt   = isValueCheck(document.getElementById("billEAId").value);
      
      var eqBoeAllocAmt = parseFloat(exRate) * parseFloat(allocAmt);
      
//    console.log('Original Multiplication------------------>'+eqBoeAllocAmt)
      
      var eqBoeAllocAmtRound = eqBoeAllocAmt.toFixed(2);
      
      
//    console.log(' After fixed multiplication----------------->'+eqBoeAllocAmtRound)
      
//    console.log('boeAvailAmt------------------>'+boeAvailAmt)
      
      $("#boeAllocAmt_"+cVal).val(eqBoeAllocAmtRound);      
      
      var balOutAmt = parseFloat(outAmt) - parseFloat(allocAmt);
      
      $("#outstandingAmt_"+cVal).val(balOutAmt.toFixed(2));
      
      
      
      var totalEndAmt = parseFloat(endorseAmt) + parseFloat(allocAmt);
      
      $("#endorseAmt_"+cVal).val(totalEndAmt.toFixed(2));
      
      if(parseFloat(balOutAmt) == 0){
            $("#fullyAlloc_"+cVal).val("Y");
            $("#isEditField_"+cVal).val("Y");
      }else{
            $("#fullyAlloc_"+cVal).val("N");
            $("#isEditField_"+cVal).val("Y");
      }
      
      
      var totalAllocateAmt = 0;
      
      var tblLenInv = document.getElementById('invoiceListTable').rows.length - 1;

      if (tblLenInv > 0) {          

            for ( var i = 1; i <= tblLenInv; i++) {                     
                        
               var tempInvAmtIC = isValueCheck(document.getElementById("alRealiedAmtIC_"+i).value);
            
               totalAllocateAmt = parseFloat(totalAllocateAmt) +  parseFloat(tempInvAmtIC);  
              
               totalAllocateAmt = totalAllocateAmt.toFixed(2);
                        
            }
      }           
      
      
      var totalEndorseAmt = 0;
      
      var tbl_len = document.getElementById('paymentListTable').rows.length - 1;

      if (tbl_len > 0) {            

            for ( var j = 1; j <= tbl_len; j++) {                 
                  
                  var isEditVal = $("#isEditField_"+j).val();                 
                        
                  if(isEditVal == "Y"){               
                        
                  var tempPayAmt = isValueCheck(document.getElementById("boeAllocAmt_"+j).value);
            
                  totalEndorseAmt = parseFloat(totalEndorseAmt) +  parseFloat(tempPayAmt);
                  
                  totalEndorseAmt = totalEndorseAmt.toFixed(2);
                }       
            }
      }     
            
      if(parseFloat(boeAvailAmt) < parseFloat(eqBoeAllocAmtRound)){
            
            $("#dialogBOE").html('Allocated Amount In BOE Currency Should Not Exceed BOE Amount Available For Endorsement');
      
        $("#dialogBOE").dialog('open')
     
      $("#dialogBOE").dialog({autoOpen: false,modal: true,title: "Error Message",
            buttons: {
                  Close: function () {
                  $(this).dialog('close');
                   }
                 }
             });
       
        $("#allocAmt_"+cVal).val('0');
       
        $("#boeAllocAmt_"+cVal).val('0');
       
        $("#outstandingAmt_"+cVal).val(outAmt);        
       
        $("#endorseAmt_"+cVal).val(endorseAmt);
       
        $("#fullyAlloc_"+cVal).val("N");        
       
        var totalEdAmt = 0;
      
      var tbl_len = document.getElementById('paymentListTable').rows.length - 1;

      if (tbl_len > 0) {            

            for ( var j = 1; j <= tbl_len; j++) {                 
                  
                  var isEditVal = $("#isEditField_"+j).val();                 
                        
                  if(isEditVal == "Y"){               
                        
                  var tempPayAmt = isValueCheck(document.getElementById("boeAllocAmt_"+j).value);
            
                  totalEdAmt = parseFloat(totalEdAmt) +  parseFloat(tempPayAmt);
                  
                  totalEdAmt = totalEdAmt.toFixed(2);
                }       
            }
      }     
       
        var totalAvailAmt = parseFloat(totalAllocateAmt) + parseFloat(totalEdAmt);
            
        totalAvailAmt = totalAvailAmt.toFixed(2);
            
            var currBoeAvlAmt = parseFloat(actualBoeAvailAmt) - parseFloat(totalAvailAmt);
                  
            $("#actualAmountId").val(currBoeAvlAmt.toFixed(2));
       
        return false;   
       
      }else{
            
            var totalAllAmt = parseFloat(totalAllocateAmt) + parseFloat(totalEndorseAmt);
            
            totalAllAmt = totalAllAmt.toFixed(2);
            
            var currentBoeAvlAmt = parseFloat(actualBoeAvailAmt) - parseFloat(totalAllAmt);
                  
            $("#actualAmountId").val(currentBoeAvlAmt.toFixed(2));
            
      }

      if(parseFloat(outAmt) < parseFloat(allocAmt)){
                  
                  $("#dialogBOE").html('Allocated Amount In Payment Currency Should Not Exceed Bill Outstanding Amount');
            
              $("#dialogBOE").dialog('open')
            
            $("#dialogBOE").dialog({autoOpen: false,modal: true,title: "Error Message",
                  buttons: {
                        Close: function () {
                        $(this).dialog('close');
                         }
                       }
                   });
              
              $("#allocAmt_"+cVal).val('0');
              
              $("#boeAllocAmt_"+cVal).val('0');
              
              $("#outstandingAmt_"+cVal).val(outAmt);        
              
              $("#endorseAmt_"+cVal).val(endorseAmt);
              
              $("#fullyAlloc_"+cVal).val("N");      
              
              var totalEdAmt = 0;
            
            var tbl_len = document.getElementById('paymentListTable').rows.length - 1;

            if (tbl_len > 0) {            

                  for ( var j = 1; j <= tbl_len; j++) {                 
                        
                        var isEditVal = $("#isEditField_"+j).val();                 
                              
                        if(isEditVal == "Y"){               
                              
                        var tempPayAmt = isValueCheck(document.getElementById("boeAllocAmt_"+j).value);
                  
                        totalEdAmt = parseFloat(totalEdAmt) +  parseFloat(tempPayAmt);
                        
                        totalEdAmt = totalEdAmt.toFixed(2);
                      }       
                  }
            }     
              
              var totalAvailAmt = parseFloat(totalAllocateAmt) + parseFloat(totalEdAmt);
                  
              totalAvailAmt = totalAvailAmt.toFixed(2);
                  
                  var currBoeAvlAmt = parseFloat(actualBoeAvailAmt) - parseFloat(totalAvailAmt);
                        
                  $("#actualAmountId").val(currBoeAvlAmt.toFixed(2));
              
              return false;
      
       }
      
});
*/



function getPaymentList(event){

      var closeId = $(event).attr('id');
      var test = closeId.split('_');
      var cVal = test[1];     
      
      var utilityTransNo            =     document.getElementById("chkPayId_"+cVal).value;

      var allocAmt                  =     isValueCheck(document.getElementById("allocAmt_"+cVal).value);
      
      var exRate                    =     isValueCheck(document.getElementById("exRate_"+cVal).value);      
      
      var condVal = $(event).prop('checked');
      
      if(condVal == true){
            
            if(parseFloat(allocAmt) > 0){
                  
                   $("#allocAmt_"+cVal).attr("readonly","readonly");
                      
                   $("#exRate_"+cVal).attr("readonly","readonly");
                        
                   var tempVal = utilityTransNo + ':' + allocAmt + ':' + exRate;
                  
                   document.getElementById("chkPayId_"+cVal).value = tempVal; 
                  
            }else{
                  
                  $("#dialogBOE").html('Please Enter the BOE Allocated Amount in Payment currency');
            
              $("#dialogBOE").dialog('open')
            
            $("#dialogBOE").dialog({autoOpen: false,modal: true,title: "Error Message",
                  buttons: {
                        Close: function () {
                        $(this).dialog('close');
                         }
                       }
                   });    
              
              $('#chkPayId_'+cVal).attr('checked', false);
              
              return false;         
            }     
          
      }else{
            
            $("#allocAmt_"+cVal).removeAttr("readonly");    
            
            $("#exRate_"+cVal).removeAttr("readonly");      
            
            var temp_refNo = utilityTransNo.split(':');
            
            var tempData = temp_refNo[0] + ':' + temp_refNo[1];

            document.getElementById("chkPayId_"+cVal).value = tempData;
            return false;
      }     
      
}


function getInvoiceList(event){

      var closeId = $(event).attr('id');
      var test = closeId.split('_');
      var cVal = test[1];           
      
      var totInvAmt           = isValueCheck(document.getElementById("totalInvAmt_"+cVal).value);
      
      var paidRealizedIC      = isValueCheck(document.getElementById("alRealiedAmtIC_"+cVal).value);
      
      var outAmtIC            = isValueCheck(document.getElementById("totalOutAmtIC_"+cVal).value);
      
      var tblInvLen           = document.getElementById('invoiceListTable').rows.length - 1;    
      
      
      
      if (tblInvLen > 0) {          

            for ( var i = 1; i <= tblInvLen; i++) {                     
            
                  document.getElementById("realAmt_"+i).value      = '0.0';
                  
                  document.getElementById("realAmtIC_"+i).value    = '0.0';
            }
      }     
      
      var totalEndorseAmt = 0;
      
      var totalEndorseAmtIC = 0;
      
      var tbl_len = document.getElementById('paymentListTable').rows.length - 1;    

      if (tbl_len > 0) {            

            for ( var j = 1; j <= tbl_len; j++) {     
                  
                  var isEditVal = $("#chkPayId_"+j).prop('checked');          
                        
                  if(isEditVal == true){              
                        
                  var tempPayAmt          = isValueCheck(document.getElementById("allocAmt_"+j).value);
            
                  totalEndorseAmt   = parseFloat(totalEndorseAmt) +  parseFloat(tempPayAmt);
                  
                  totalEndorseAmt         = totalEndorseAmt.toFixed(2);
                  
                  var exRate              = isValueCheck(document.getElementById("exRate_"+j).value); 
                  
                  tempPayAmtIC            = parseFloat(tempPayAmt) *  parseFloat(exRate);
                  
                  totalEndorseAmtIC       = parseFloat(totalEndorseAmtIC) +  parseFloat(tempPayAmtIC);
                  
                  totalEndorseAmtIC = totalEndorseAmtIC.toFixed(2);                 
                }       
            }
      }     

      
      if(parseFloat(totalEndorseAmt) > 0
                  && parseFloat(totalEndorseAmtIC) > 0 ){         
            
            if(parseFloat(outAmtIC) < parseFloat(totalEndorseAmtIC)){
                  
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
              
            }else{

                  document.getElementById("realAmt_"+cVal).value   = totalEndorseAmt;
                  
                  document.getElementById("realAmtIC_"+cVal).value = totalEndorseAmtIC;
                  
/* Code Added to get Invoice Details for Invoice Amount Modification Starts on 26042018 */
                  var inVoiceSerNo  = isValueCheck(document.getElementById("invoiceSerialNumber_"+cVal).value);

                  var inVoiceNo           = isValueCheck(document.getElementById("invoiceNumber_"+cVal).value);

                  var realizedAmt         = isValueCheck(document.getElementById("realAmt_"+cVal).value);
                        
                  var vTempVal = inVoiceSerNo + ' : ' + inVoiceNo + ' : ' + realizedAmt;

                  $("#invoicVal").val(vTempVal);
/* Ends */
            }           
            
      }else{
            
            $("#dialogBOE").html('Please Select Atleast One Bill');
      
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
      }     
}

// Added to validate Alpha and Numeric values
function alphanumeric(val) {
      val = (val) ? val : window.event;
      var charCode = (val.which) ? val.which : val.keyCode;
      if ((charCode > 47 && charCode < 58) || (charCode > 64 && charCode < 91)
                  || (charCode > 96 && charCode < 123)) {
            return true;
      }
            return false;
}
