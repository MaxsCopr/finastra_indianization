function eventType() {
    var id = document.getElementById("billRefNo").value;
    $.getJSON("getEventReferenceJson", { billRefNo: id }, function(details) {
        updateComboBox(details.eventList, "idEventRefNo");
    });
}

$(function() {
    $(".datepicker").datepicker({ format: "dd/mm/yyyy" });

    $("#validate").click(function() {
        $("#myForm").attr("action", "fetchAdTransfer?mode=validate");
        $("#myForm").submit();
    });

    $("#submit").click(function() {
        $("#myForm").attr("action", "fetchAdTransfer?mode=store");
        $("#myForm").submit();
    });

    $("#fetch").click(function() {
        $("#myForm").attr("action", "fetchAdTransfer");
        $("#myForm").submit();
    });

    $("#div1").delay(8000).hide(100);
});

function reload() {
    window.location.href = "adTransfer";
}
