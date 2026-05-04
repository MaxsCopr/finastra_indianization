$(document).ready(function() {
      // $('.datepicker').datepicker();

      $("#div1").delay(8000).hide(100, function() {
      });
      $("#submit").click(function() {

            $('#myForm').attr('action', 'insertExport');
            $('#myForm').submit();

      });

      $("#validate").click(function() {

            $('#myForm').attr('action', 'validateExportDetails');
            $('#myForm').submit();

      });

      /*
       * function copyPurchaseDetails(event){ alert('jo');
       * $(".poList").removeClass("highlighted");
       * $(event).addClass("highlighted"); var poNo =
       * $(event).find("td").eq(0).text().trim(); var cifId =
       * $(event).find("td").eq(2).text().trim();
       *
       * $("#amountFrom").val(poNo); $("#transRefNo").val(cifId);
       * $("#myForm").attr("action", "fetchPurchaseOrder"); $("#myForm").submit(); }
       */

      

      $("#ok").click(function() {
            $("#check").val("approve");
            var remark = $.trim($('#remarks').val());
            if (remark.length == 0) {
                  alert("Remarks must be filled out");
                  return false;
            }
            $('#myForm').attr('action', 'updateStatusAction');
            $('#myForm').submit();

      });

      $("#reject").click(function() {
            $("#check").val("reject");
            var remark = $.trim($('#remarks').val());
            if (remark.length == 0) {
                  alert("Remarks must be filled out");
                  return false;
            }
            $('#myForm').attr('action', 'updateStatusAction');
            $('#myForm').submit();

      });

});

function searchPurchaseOrder() {
      
      
      console.log('-----------------------Search Purchase Order Method-----------------------')
      var po_amt=document.getElementById("povalue").value;
      console.log('po_amt----'+po_amt);
      
      
      

      var vals=getNumbers(po_amt);
      console.log('@@@@@@@@@@@@@@@@@@------------------->'+vals)
      function getNumbers(inputString){
          var regex=/\d+\.\d+|\.\d+|\d+/g,
              results = [],
              n;

          while(n = regex.exec(inputString)) {
              results.push(parseFloat(n[0]));
          }

          return results;
      }

      
      
      var margin_value=document.getElementById("margin").value;
      
      console.log('margin_value--'+margin_value)
      
      var eligible_amt=vals*margin_value/100;
      console.log('eligible_amt----'+eligible_amt)
      
      $('#eligibleamount').val(eligible_amt)

      $('#myForm').attr('action', 'searchpurchaseorder');
      $('#myForm').submit();
}

function gotoPurchaseOrderScreen() {
      var status = document.getElementById("status").value;
      if (status == "REJECTED") {
            $('#myForm').attr('action', 'gotoPurchaseOrderScreen');
            $('#myForm').submit();
      } else {
            alert('You can Edit Only Rejected Purchase Order');
      }

}
function doLoad() {
      $('body').modal("hide");
      $('body').css('display', 'block');
      $('body').removeClass('removePageLoad');
      $('body').addClass('removePageLoad');
}

function onChangeLoad() {
      $('body').modal({
            show : 'false'
      });
      $('body').removeClass('removePageLoad');
      $('body').addClass('addPageLoad');
}

function customerCifCode() {
      $("#myForm").attr("action", "customerCifCode");
      $("#myForm").submit();
}

function incoTerms() {
      $("#myForm").attr("action", "incoTerms");
      $("#myForm").submit();
}

function goodsCode() {
      $("#myForm").attr("action", "goodsCode");
      $("#myForm").submit();
}

function fetchGoodsCode() {
      $("#myForm").attr("action", "fetchGoodsCode");
      $("#myForm").submit();
}
function goToInwardPage() {

      $("#myForm").attr("action", "goToInwardPage");
      $("#myForm").submit();
}

function productType() {

      $("#myForm").attr("action", "productType");
      $("#myForm").submit();
}

function chargeType() {
      $("#myForm").attr("action", "chargeType");
      $("#myForm").submit();
}

function newChargeSchedule() {
      var code = $("#transRefNo").val();
      var code2 = $("#Charge").val();
      if (code != null && code != "" && code2 != null && code2 != "") {
            onChangeLoad();
            alert("CIF is assigned succesfully ");
            $("#myForm").attr("action", "newChargeSchedule");
            $("#myForm").submit();
      } else {
            $("#myForm").attr("action", "newChargeSchedule");
            $("#myForm").submit();
      }
}

function fetchChargeScheduleList() {
      onChangeLoad();
      $("#myForm").attr("action", "fetchChargeScheduleList");
      $("#myForm").submit();
}

function fetchPurchaseOrder() {
      onChangeLoad();
      $("#myForm").attr("action", "fetchPurchaseOrder");
      $("#myForm").submit();
}

function updateChargeSchedule() {
      var code = $("#updateCIFId").val();
      if (code != null && code != "") {
            onChangeLoad();
            alert("Successfully updated");
            $("#myForm").attr("action", "updateChargeSchedule");
            $("#myForm").submit();

      } else {
            $("#GreenTextID").css("font-weight", "Bold");
            $("#GreenTextID").css('color', 'red');
            $("#selectRow").show();

      }
}

function deleteChargeSchedule() {

      var code = $("#updateCIFId").val();
      if (code != null && code != "") {
            onChangeLoad();
            alert("Successfully deleted");
            $("#myForm").attr("action", "deleteChargeSchedule");
            $("#myForm").submit();
      } else {
            $("#GreenTextID").css("font-weight", "Bold");
            $("#GreenTextID").css('color', 'red');
            $("#selectRow").show();

      }
}

function select(event) {
      $(".customerlist").removeClass("highlighted");
      $(event).addClass("highlighted");
      var cif = $(event).find("td").eq(0).text().trim();
      var charge = $(event).find("td").eq(2).text().trim();
      var key97 = $(event).find("td").eq(4).text().trim();
      $("#updateCIFId").val(cif);
      $("#updateChargeId").val(charge);
      $("#updateKey97Id").val(key97);

}
function closeSubmit() {
      $("#myForm").attr("action", "close");
      $("#myForm").submit();
}

// Internal Conversion

function submitForm() {
      $("#myForm").submit();
}
function homeSubmit() {
      var value = $("#transRefNo").val();
      $("#myForm").attr("action", "home");
      $("#myForm").submit();
}
function closeSubmit() {
      $("#myForm").attr("action", "close");
      $("#myForm").submit();
}

function fromAccTypeHideAndShow(id) {
      var value = $(id).val();
      if (value.toLowerCase() === "customer") {
            $("#fromAccTypeCodeId").show();
      } else {
            $("#fromAccTypeCodeId").hide();
      }

}
function debitAccTypeHideAndShow(id) {
      var value = $(id).val();
      if (value.toLowerCase() === "customer") {
            $("#debitAccTypeCodeId").show();
      } else {
            $("#debitAccTypeCodeId").hide();
      }

}
function creditAccTypeHideAndShow(id) {
      var value = $(id).val();
      if (value.toLowerCase() === "customer") {
            $("#creditAccTypeCodeId").show();
      } else {
            $("#creditAccTypeCodeId").hide();
      }

}
function subProductActionChange() {
      onChangeLoad();
      $("#myForm").attr("action", "subProductAction");
      $("#myForm").submit();
}

function subProduct() {
      onChangeLoad();
      $("#myForm").attr("action", "subProduct");
      $("#myForm").submit();
}

function postingEntries() {
      $("#myForm").attr("action", "postingEntries");
      $("#myForm").submit();
}

function fxConversion() {
      $("#myForm").attr("action", "fxConversion");
      $("#myForm").submit();
}
function addFXConversion(position) {

      var posFx = position;
      $("#fxPosition").val(posFx);

      $("#myForm").attr("action", "addFXConversion");
      $("#myForm").submit();
}

function addLegFXConversion() {
      $("#myForm").attr("action", "addLegFXConversion");
      $("#myForm").submit();
}
function accTypeOnChange(id) {
      var accountType = 0;
      var account = 0;

      onChangeLoad();

      if (id == 'fromAccountType') {
            accountType = 'fromAccount';
            account = $("#" + id).val();

            $("#cusAccType").val(accountType);
            $("#cusAccount").val(account);

      } else if (id == 'debitAccListId') {
            accountType = 'debitAccount';
            account = $("#" + id).val();

            $("#cusAccType").val(accountType);
            $("#cusAccount").val(account);

      } else if (id == 'creditAccListId') {
            accountType = 'creditAccount';
            account = $("#" + id).val();

            $("#cusAccType").val(accountType);
            $("#cusAccount").val(account);
      }

      $("#myForm").attr(
                  "action",
                  "getAccount?" + "accountType=" + accountType + "&" + "account="
                              + account);
      $("#myForm").attr("action", "getAccount");
      $("#myForm").submit();
}
function hideFXConversion() {

      $("#fxConversionId").hide();

      var value = $(id).val();
      if (value.toLowerCase() === "customer") {
            $("#creditAccTypeCodeId").show();
      } else {
            $("#creditAccTypeCodeId").hide();
      }

      $("#myForm").attr("action", "fxConversion");
      $("#myForm").submit();

      // $('#transAmtId').prop('readonly', true);

      $.ajaxSetup({
            async : false
      });
      $.getJSON("fxConversion", function(data) {
            $("#fxConversionId").show();
      });
      $(function() {
            $('form[name=icForm]').submit(function() {
                  $.post($(this).attr('action'), $(this).serialize(), function(json) {
                        alert(json);
                  }, 'json');
                  return false;
            });
      });

      $('#myForm').ajaxForm({
            url : 'fxConversion', // or whatever
            dataType : 'json',
            success : function(response) {
                  alert("The server says: " + response);
            }
      });

      $.ajax({
            url : "fxConversion",
            data : $("#myForm").serialize(),
            type : "post",
            dataType : "json",
            success : function(data) {
                  alert("data" + data);
            }
      });

      var values = [];
      values = data.icVo.errorList;
      // $("#target").html(values);
      $.each(values, function(index, value) {
            // document.getElementById("errorId").value ="value.errorDetails";
            alert(index + ": " + value);
      });

      // alert("not null "+data.icVo.errorList[0].errorDetails);
      // alert("not null "+data.icVo.errorList.size());
      // icVo.errorList=data.icVo.errorList;

      $.ajaxSetup({
            async : false
      });
      $.ajax({
            url : "fxConversion",
            data : $("#myForm").serialize(),
            type : "post",
            dataType : "json",
            success : function(data) {
                  // var errDetls=data.icVo.errorList[0].errorDetails;
                  // alert("ha" +data.icVo.errorList[0].errorDetails);
                  if (data.icVo.errorList == null) {

                  } else {

                        $("#trData").show();
                        // $("#errDetail").val(errDetls);

                        $.each(data.icVo.errorList, function(index, errorList) {
                              $("#errId").val(data.icVo.errorList[index].errorId);
                              $("#errDesc").val(data.icVo.errorList[index].errorDesc);
                              $("#errCode").val(data.icVo.errorList[index].errorCode);
                              $("#errDetail")
                                          .val(data.icVo.errorList[index].errorDetails);
                              $("#errMsg").val(data.icVo.errorList[index].errorMsg);
                              $("#trData").append();
                              $("#errId").val(data.icVo.errorList[index].errorId);
                              $("#errDesc").val(data.icVo.errorList[index].errorDesc);
                              $("#errCode").val(data.icVo.errorList[index].errorCode);
                              $("#errDetail")
                                          .val(data.icVo.errorList[index].errorDetails);
                              $("#errMsg").val(data.icVo.errorList[index].errorMsg);

                        });
                        $.each(data.icVo.errorList, function(index, errorList) {

                        });
                  }
            }
      });

}
function cusSearch(value) {
      $("#cusAccType").val(value);
      $("#myForm").attr("action", "cusSearch?" + "accountType=" + value);
      $("#myForm").attr("action", "cusSearch");
      $("#myForm").submit();
}
function finishFXConversion() {
      $("#myForm").attr("action", "finishFXConversion");
      $("#myForm").submit();
}

function fxAmountOnChange(id, position) {
      var amount = $("#" + id).val();
      var eRate = $("#eRateID" + position).val();
      var eRateValue;
      var inrRate = $("#inrRateID" + position).val();
      var inrRateAmount;
      var toCurrency = $("#toCurrencyID" + position).val();
      if (amount != null) {
            if (eRate != null && eRate != "") {
                  eRateValue = amount * eRate;
                  $("#eRateValueID" + position).val(eRateValue);
            } else {
                  $("#eRateValueID" + position).val("");
            }
            if (inrRate != null && inrRate != "") {
                  inrRateAmount = amount * inrRate;
                  $("#inrAmountID" + position).val(inrRateAmount);
            } else {
                  $("#inrAmountID" + position).val("");
            }
      }
}
function eRateOnChange(id, position) {
      var amount = $("#amountID" + position).val();
      var eRate = $("#" + id).val();
      var eRateValue;
      var inrRate = $("#inrRateID" + position).val();
      var inrRateAmount;
      var toCurrency = $("#toCurrencyID" + position).val();
      if (amount != null) {
            if (eRate != null && eRate != "") {
                  eRateValue = amount * eRate;
                  $("#eRateValueID" + position).val(eRateValue);
                  if (toCurrency == "INR") {
                        $("#inrRateID" + position).val(eRate);
                        $("#inrAmountID" + position).val(eRateValue);
                  }
            } else {
                  $("#eRateValueID" + position).val("");
            }
      }
}
function inrRateOnChange(id, position) {
      var amount = $("#amountID" + position).val();
      var eRate = $("#eRateID" + position).val();
      var eRateValue;
      var inrRate = $("#" + id).val();
      var inrRateAmount;
      var toCurrency = $("#toCurrencyID" + position).val();
      if (amount != null) {
            if (inrRate != null && inrRate != "") {
                  inrRateAmount = amount * inrRate;
                  $("#inrAmountID" + position).val(inrRateAmount);
                  if (toCurrency == "INR") {
                        eRateValue = amount * eRate;
                        $("#inrRateID" + position).val(eRate);
                        $("#inrAmountID" + position).val(eRateValue);
                  }
            } else {
                  $("#inrAmountID" + position).val("");
            }
      }
}
function fxToCurrOnChange(id, position) {
      var amount = $("#amountID" + position).val();
      var eRate = $("#eRateID" + position).val();
      var eRateValue;
      var toCurrency = $("#" + id).val();
      var inrRate = $("#inrRateID" + position).val();
      var inrRateAmount;
      if (toCurrency == "INR") {
            $('#inrRateID' + position).prop('readonly', true);
            if (amount != null) {
                  if (eRate != null && eRate != "") {
                        eRateValue = amount * eRate;
                        $("#eRateValueID" + position).val(eRateValue);
                        $("#inrRateID" + position).val(eRate);
                        $("#inrAmountID" + position).val(eRateValue);
                  }
            }

      } else {
            $('#inrRateID' + position).prop('readonly', false);
      }
}

function onlyNumbers(evt) {
      var e = (window.event) ? event : evt; // for cross browser compatibility
      var charCode = e.which || e.keyCode;
      if (charCode > 31 && (charCode < 48 || charCode > 57)) {
            return false;
      }
      return true;
}
function numberAndDot(evt) {
      var e = (window.event) ? event : evt; // for cross browser compatibility
      var charCode = e.which || e.keyCode;
      if (charCode == 46) {
            return true;
      } else if (charCode > 31 && (charCode < 48 || charCode > 57)) {
            return false;
      }
      return true;
}