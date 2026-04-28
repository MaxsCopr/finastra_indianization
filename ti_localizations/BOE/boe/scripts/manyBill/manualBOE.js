$(document).ready(function() {

      $("#updateId").hide();

      $("#deleteId").hide();
      console.log("BOE Button Hide");

      var boestatus = document.getElementById("boestatus").value
      console.log("BOE STATUS ----->" + boestatus);

      var sysDate = $("#sysProcDate").val().split("-");

      var sysProcDate = new Date(sysDate[2], sysDate[1] - 1, sysDate[0]);
      if (boestatus != 'P' && boestatus != 'A') {
            $("#billEntryDateId").datepicker({
                  changeMonth : true,
                  changeYear : true,
                  maxDate : sysProcDate,
                  dateFormat : 'dd/mm/yy'
            });

            $("#igmDate").datepicker({
                  changeMonth : true,
                  changeYear : true,
                  maxDate : sysProcDate,
                  dateFormat : 'dd/mm/yy'
            });

            $("#hblDate").datepicker({
                  changeMonth : true,
                  changeYear : true,
                  maxDate : sysProcDate,
                  dateFormat : 'dd/mm/yy'
            });

            $("#mblDate").datepicker({
                  changeMonth : true,
                  changeYear : true,
                  maxDate : sysProcDate,
                  dateFormat : 'dd/mm/yy'
            });
      }

      // -------------------
      document.getElementById("insertInv").style.visibility = 'visible';

      var insertcount = document.getElementById("boeinsertstatus").value
      console.log("Insert Count ----->" + insertcount);

      if (insertcount != null && insertcount > 0) {
            document.getElementById("insertInv").style.visibility = 'hidden';
      } else if (insertcount != null && insertcount == 0) {
            document.getElementById("insertInv").style.visibility = 'visible';
      }
      //

      if (insertcount > 0 && insertcount != null && boestatus != 'R') {

            $("#formId :input").attr('readonly', true);

            $("#okId").hide();
      }

      if (boestatus == 'A') {
            $("#recInd").attr('disabled', true);

            $("#imAgency").attr('disabled', true);

            $("#govprv").attr('disabled', true);
      }

      if (boestatus == 'P') {
            $("#recInd").attr('disabled', true);

            $("#imAgency").attr('disabled', true);

            $("#govprv").attr('disabled', true);
      }

      if (boestatus == 'R') {
            $("#okId").hide();
            $("#updateId").show();
            $("#deleteId").show();

            $("#billRefNo").attr('readonly', true);
            $("#portcode").attr('readonly', true);
            $("#portsearch").hide();
      }

      $("#updateId").click(function() {
            $('#buttonType').val("UpdateEditedData");
            $('#formId').attr('action', 'updateEditedBOEData');
            $('#formId').submit();
      });

      $("#deleteId").click(function() {
            var tblLenInv = document.getElementById('invTable').rows.length - 1;
            if (tblLenInv != 0) {
                  $("#dialogBOE").html("Please Delete All the Invoice First");

                  $("#dialogBOE").dialog("open");

                  $("#dialogBOE").dialog({
                        autoOpen : false,
                        modal : true,
                        title : "Error Message",
                        buttons : {
                              Close : function() {
                                    $("#dialogBOE").dialog('close');
                              }
                        }
                  });

                  return false;
            } else {
                  $('#buttonType').val("DeleteBOEData");
                  $('#formId').attr('action', 'deleteBOEdata');
                  $('#formId').submit();
            }

      });

      // ---------------------

      $("#okId").click(function() {

            var tblLenInv = document.getElementById('invTable').rows.length - 1;

            if (tblLenInv == 0) {

                  $("#dialogBOE").html("Please Enter Atleast One Invoice");

                  $("#dialogBOE").dialog("open");

                  $("#dialogBOE").dialog({
                        autoOpen : false,
                        modal : true,
                        title : "Error Message",
                        buttons : {
                              Close : function() {
                                    $("#dialogBOE").dialog('close');
                              }
                        }
                  });

                  return false;

            }

            else {
                  $('#formId').attr('action', 'storeManualBOEData');
                  $('#formId').submit();
            }

      });

});

function getDischargePort() {
      var boestatus = document.getElementById("boestatus").value
      console.log("BOE STATUS ----->" + boestatus);

      if (boestatus != 'P' && boestatus != 'A') {
            $("#formId").attr("action", "getPortOfDischarge");
            $("#formId").submit();
      }
}

function getShipmentPort() {
      var boestatus = document.getElementById("boestatus").value
      console.log("BOE STATUS ----->" + boestatus);

      if (boestatus != 'P' && boestatus != 'A') {
            $("#formId").attr("action", "getPortOfShipment");
            $("#formId").submit();
      }
}

function removeComma(value) {
      value = value.replace(/\,/g, "");
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

function intToFormat(nStr) {
      nStr += '';
      x = nStr.split('.');
      x1 = x[0];
      x2 = x.length > 1 ? '.' + x[1] : '';
      var rgx = /(\d+)(\d{3})/;
      var z = 0;
      var len = String(x1).length;
      var num = parseFloat((len / 2) - 1);
      while (rgx.test(x1)) {
            if (z > 0) {
                  x1 = x1.replace(rgx, '$1' + ',' + '$2');
            } else {
                  x1 = x1.replace(rgx, '$1' + ',' + '$2');
                  rgx = /(\d+)(\d{2})/
            }
            z++;
            num--;
            if (num == 0) {
                  break;
            }
      }
      return x1 + x2;
}

$(function() {

      transTypeAction();

      $("#dialogBOE").dialog({
            autoOpen : false
      });

});

function transTypeAction() {

      var boeType = document.getElementById("boeType").value;

      if (boeType == 'RBI') {

            $("#insertInv").hide();

            $("#updateInv").hide();

            $("#deleteInv").hide();

      } else {

            $("#insertInv").show();

            $("#updateInv").show();

            $("#deleteInv").show();
      }
}

function isNumber(evt) {
      evt = (evt) ? evt : window.event;
      var charCode = (evt.which) ? evt.which : evt.keyCode;
      if (charCode > 31 && (charCode < 45 || charCode > 57)) {
            return false;
      }
      return true;
}

function isAlphaNumeric(evt) {
      evt = (evt) ? evt : window.event;
      var charCode = (evt.which) ? evt.which : evt.keyCode;
      if (charCode > 31 && (charCode < 45 || charCode > 57)
                  && (!(charCode > 64 && charCode < 91))
                  && (!(charCode > 96 && charCode < 123))) {
            return false;
      }
      return true;
}

function retriveBOEData() {

      var boeNo = $('#billRefNo').val();

      var boeDate = $('#billEntryDateId').val();

      var portCode = $('#portcode').val();

      if (boeNo == '') {

            $("#dialogBOE").html('Please Enter BOE Number');

            $("#dialogBOE").dialog('open')

            $("#dialogBOE").dialog({
                  autoOpen : false,
                  modal : true,
                  title : "Error Message",
                  buttons : {
                        Close : function() {
                              $(this).dialog('close');
                        }
                  }
            });

            return false;

      } else if (boeDate == '') {

            $("#dialogBOE").html('Please Enter BOE Date');

            $("#dialogBOE").dialog('open')

            $("#dialogBOE").dialog({
                  autoOpen : false,
                  modal : true,
                  title : "Error Message",
                  buttons : {
                        Close : function() {
                              $(this).dialog('close');
                        }
                  }
            });

            return false;

      } else if (portCode == '') {

            $("#dialogBOE").html('Please Enter Portcode');

            $("#dialogBOE").dialog('open')

            $("#dialogBOE").dialog({
                  autoOpen : false,
                  modal : true,
                  title : "Error Message",
                  buttons : {
                        Close : function() {
                              $(this).dialog('close');
                        }
                  }
            });

            return false;

      } else {
            $("#formId").attr("action", "retriveBOEData");
            $("#formId").submit();
      }
}

function insertInvoiceDatas() {

      $('#buttonType').val("Insert");

      var boeNo = $('#billRefNo').val();

      var boeDate = $('#billEntryDateId').val();

      var portCode = $('#portcode').val();

      if (boeNo == '') {

            $("#dialogBOE").html('Please Enter BOE Number');

            $("#dialogBOE").dialog('open')

            $("#dialogBOE").dialog({
                  autoOpen : false,
                  modal : true,
                  title : "Error Message",
                  buttons : {
                        Close : function() {
                              $(this).dialog('close');
                        }
                  }
            });

            return false;

      } else if (boeDate == '') {

            $("#dialogBOE").html('Please Enter BOE Date');

            $("#dialogBOE").dialog('open')

            $("#dialogBOE").dialog({
                  autoOpen : false,
                  modal : true,
                  title : "Error Message",
                  buttons : {
                        Close : function() {
                              $(this).dialog('close');
                        }
                  }
            });

            return false;

      } else if (portCode == '') {

            $("#dialogBOE").html('Please Enter Portcode');

            $("#dialogBOE").dialog('open')

            $("#dialogBOE").dialog({
                  autoOpen : false,
                  modal : true,
                  title : "Error Message",
                  buttons : {
                        Close : function() {
                              $(this).dialog('close');
                        }
                  }
            });

            return false;

      } else {

            var boeDetail = boeNo + ":" + boeDate + ":" + portCode;

            $.ajaxSetup({
                  async : false
            });

            $
                        .post(
                                    "checkBOEStatus" + "?boeData=" + boeDetail,
                                    function(data) {

                                          var check = data.boeStatus;

                                          if (parseFloat(check) > 0) {

                                                $("#dialogBOE")
                                                            .html(
                                                                        'This BOE Details are already utilized in Settlement.So you cannot create new one.');

                                                $("#dialogBOE").dialog('open')

                                                $("#dialogBOE").dialog({
                                                      autoOpen : false,
                                                      modal : true,
                                                      title : "Error Message",
                                                      buttons : {
                                                            Close : function() {
                                                                  $(this).dialog('close');
                                                            }
                                                      }
                                                });

                                                return false;

                                          } else {
                                                $("#formId").attr("action",
                                                            "manualInvoiceDatas");
                                                $("#formId").submit();
                                          }
                                    });
      }
}

function updateInvoiceDatas() {

      var invVal = $("#invValue").val();

      if (invVal == "") {

            $('#selectrow').css('display', 'block');
            return false;

      } else {

            $('#selectrow').css('display', 'none');

            $('#buttonType').val("Update");
            $("#formId").attr("action", "manualInvoiceDataupdates");
            $("#formId").submit();

            /*
             * var boeNo = $('#billRefNo').val();
             *
             * var boeDate = $('#billEntryDateId').val();
             *
             * var portCode = $('#portcode').val();
             *
             * var boeDetail = boeNo + ":" + boeDate + ":" + portCode;
             *
             * $.ajaxSetup({ async: false });
             *
             * $.post("checkBOEStatus"+"?boeData="+boeDetail,function(data) {
             *
             * var check = data.boeStatus;
             *
             * if(parseFloat(check) > 0){
             *
             * $("#dialogBOE").html('This BOE Details are already utilized in
             * Settlement.So you cannot update on this.');
             *
             * $("#dialogBOE").dialog('open')
             *
             * $("#dialogBOE").dialog({autoOpen: false,modal: true,title: "Error
             * Message", buttons: { Close: function () { $(this).dialog('close'); } }
             * });
             *
             * return false; }
             */

      }
      /*
       * }); }
       */

}

function deleteInvoiceData() {

      var invVal = $("#invValue").val();

      if (invVal == "") {

            $('#selectrow').css('display', 'block');
            return false;

      } else {

            $('#selectrow').css('display', 'none');

            $('#buttonType').val("Delete");
            $("#formId").attr("action", "manualInvoiceDatadelete");
            $("#formId").submit();

            /*
             * var boeNo = $('#billRefNo').val();
             *
             * var boeDate = $('#billEntryDateId').val();
             *
             * var portCode = $('#portcode').val();
             *
             * var boeDetail = boeNo + ":" + boeDate + ":" + portCode;
             *
             * $.ajaxSetup({ async: false });
             *
             * $.post("checkBOEStatus"+"?boeData="+boeDetail,function(data) {
             *
             * var check = data.boeStatus;
             *
             * if(parseFloat(check) > 0){
             *
             * $("#dialogBOE").html('This BOE Details are already utilized in
             * Settlement.So you cannot delete on this.');
             *
             * $("#dialogBOE").dialog('open')
             *
             * $("#dialogBOE").dialog({autoOpen: false,modal: true,title: "Error
             * Message", buttons: { Close: function () { $(this).dialog('close'); } }
             * });
             *
             * return false;
             *
             * }else{
             */

            /*
             * } });
             */
      }
}

function viewInvoiceDatas() {

      var invVal = $("#invValue").val();

      if (invVal == "") {

            $('#selectrow').css('display', 'block');
            return false;

      } else {

            $('#selectrow').css('display', 'none');

            $('#buttonType').val("View");

            $("#formId").attr("action", "manualInvoiceDatas");
            $("#formId").submit();

      }
}

function selectData(event) {

      $(".invList").removeClass("highlighted");
      $(event).addClass("highlighted");

      var value1 = $(event).find("td").eq(0).text().trim();
      var value2 = $(event).find("td").eq(1).text().trim();

      var invValue = value1 + ':' + value2;

      $("#invValue").val(invValue);

}

function isValueCheck(value) {

      if (trim(value) == "" || value == null) {
            return "0";
      } else {
            return value;
      }
}

function populateCIFDetails() {

      var ieCode = $("#ieCode").val();

      if (ieCode != '') {
            $("#formId").attr("action", "manualPopCIFDetails");
            $("#formId").submit();
      }
}

function editInvoiceDatas() {
      // alert("Inside editInvoiceDatas function");

      var invVal = $("#invValue").val();

      if (invVal == "") {

            $('#selectrow').css('display', 'block');
            return false;

      } else {

            $('#selectrow').css('display', 'none');

            $('#buttonType').val("Edit");

            $("#formId").attr("action", "editInvoiceDatas");
            $("#formId").submit();

      }
}