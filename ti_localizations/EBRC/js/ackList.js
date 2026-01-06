$(function(){
	$('.datepicker').datepicker({format: 'dd/mm/yyyy'} );
});
 
 
 
function rodReload(){
	window.location.href = "rodAckAction";
}
function penReload(){
	window.location.href = "penAckAction";
}
function prnReload(){
	window.location.href = "prnAckAction";
}
function trrReload(){
	window.location.href = "trrAckAction";
}
function wsnReload(){
	window.location.href = "wsnAckAction";
}
 
	 
$(document).ready(function() {		
 
		$("#rodfetch").click(function(){
			 $("#myForm").attr("action","rodAckAction");
			  $("#myForm").submit();
		});
		$("#penFetch").click(function(){
			 $("#myForm").attr("action","penAckAction");
			  $("#myForm").submit();
		});
		$("#prnFetch").click(function(){
			 $("#myForm").attr("action","prnAckAction");
			  $("#myForm").submit();
		});
		$("#trrFetch").click(function(){
			 $("#myForm").attr("action","trrAckAction");
			  $("#myForm").submit();
		});
		$("#wsnFetch").click(function(){
			 $("#myForm").attr("action","wsnAckAction");
			  $("#myForm").submit();
		});
});

$(document).ready(function() {
	    $("#div1").delay(8000).hide(100, function() {
	    });
	});