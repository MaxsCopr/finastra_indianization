$(document).ready(
		function() {

		    var nowTemp = new Date();
		    var now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0);
		    var checkin = $('#datepicker').datepicker({
		    onRender: function(date) {
		    //return date.valueOf() < now.valueOf() ? 'disabled' : '';
		    }
		    }).on('changeDate', function(ev) {
		    if (ev.date.valueOf() > checkout.date.valueOf()) {
		    var newDate = new Date(ev.date)
		    newDate.setDate(newDate.getDate() );
		    checkout.setValue(newDate);
 
		    }
 
		    }).data('datepicker');

});