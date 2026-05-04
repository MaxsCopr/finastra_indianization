//used to determine if IE browser
function tbl_isIE() {
	if (navigator.appName == "Microsoft Internet Explorer") {
		return true;
	}
	return false;
}
// gets the form id (normally "_id3")
function tbl_getCurrentFormId() {
	var m = top.MAIN;
	if (m) {
		var f = m.document.forms[0];
		if (f) {
			return f.id + ":";
		}
	}
	return false;
}
 
// onLoad tasks for tables
function onLoadForTable(tableId, hasTableAltered) {
	onLoadSelectRow(tableId, hasTableAltered);
	onLoadSetScrollPos(tableId);
}
 
// gets the js_inputHidden element for the required table
function getJsHidden(formId, name) {
	var result = document.getElementById(formId + "js_inputHidden_" + name);
	if(!result) {
		// try for extension control
		var inputs, index, eId;
		inputs = document.getElementsByTagName('input');
		for (index = 0; index < inputs.length; ++index) {
		    eId = inputs[index].id;
		    var x = eId.lastIndexOf("js_inputHidden_" + name);
		    if(x != -1) {
		    	result = inputs[index];
		    	break;
		    }
		}	
	}
	return result;
}
 
// highlight the selected table row
// @since TI-11208 - test for 'false' added
function onLoadSelectRow(name, hasTableAltered) {
	var formId = tbl_getCurrentFormId();
	if (formId) {
		if (hasTableAltered) {
			if (hasTableAltered == 'false') {
				return;
			}
		}
		var v1 = getJsHidden(formId, name);
		if (!v1) {
			return;
		}
		var previousSelectedRow = v1.value;
		var previousSelectedRowName = null;
		if (previousSelectedRow == "-1") {
			return false;
		}
		previousSelectedRowName = "tr" + name + "_" + previousSelectedRow;
		var v2 = document.getElementById(previousSelectedRowName);
		if (v2) {
			// save the previous class into the hidden input field
			var prevClass = v2.className;
			v2.className = "highlighted";
			v1.className = prevClass;
		}
	}
}
 
/* set the scroll pos for a table
* @since artf1069674 a test is made on array length to stop exception and if
* zero uses table id
*/
function onLoadSetScrollPos(tableId) {
	var formId = tbl_getCurrentFormId();
	if (formId) {
		var scrollPosElem = document.getElementById(formId + tableId
				+ "_currentScrollPos");
		if (scrollPosElem) {
			var scrollPosElemVal = scrollPosElem.value;
			var elemToScroll = null;
			if (tbl_isIE()) {
				elemToScroll = tableId + "_div";
			} else // if FF/NN
			{
				var table = document.getElementById(formId + tableId);
				if (table) {
					var tbodyArr = table.getElementsByTagName('tbody');
					if (tbodyArr) {
						if (tbodyArr.length > 0) {
							elemToScroll = tbodyArr[0].id;
						} else {
							elemToScroll = formId + tableId;
						}
					}
				}
			}
			if (elemToScroll) {
				var scrollElem = document.getElementById(elemToScroll);
				if (scrollElem) {
					scrollElem.scrollTop = scrollPosElemVal;
				}
			}
		}
	}
}
 
/**
* 
* @param trEl
*            the tr element
*/
function selectRow(trEl) {
	var formId = tbl_getCurrentFormId();
	if (formId) {
		// mainName is a part of the table's name that one is going to
		// be used to get the inputhidden's name
		// currentSelectedRow is the selected row that one is going to
		// be used to get the inputhidden's value
		// previousSelectedRow is the previous selected row number
		// previousSelectedRowName is the previous selected row name
		var name = trEl.id;
		var currentSelectedRow = name.substring(name.lastIndexOf("_") + 1);
		// alert("Clicked the row:" + currentSelectedRow);
		var ctl = name.substring(2, name.lastIndexOf("_"));
		var v1 = getJsHidden(formId, ctl);
 
		var previousSelectedRow = v1 ? v1.value : -1;
		if (v1 != null) {
			var prevStyle = v1.className;
		}
		if (previousSelectedRow != "-1") {
			var previousSelectedRowName = "tr" + ctl + "_"
					+ previousSelectedRow;
 
			// R.S. 16194, set style to that saved in the inputhidden's
			// style value
			var v2 = document.getElementById(previousSelectedRowName);
			// 16846 - defensive code if selected row does not exist
			// following change
			// of table contents
			// Reviewed by R.S.
			if (v2) {
				v2.className = v1.className;
			}
		}
 
		// It will change the selected row'style
		var v4 = document.getElementById(name);
		// save the current style
		var curStyle = v4.className;
		// set the style to highlighted
		v4.className = "highlighted";
		// Update the the inputhidden's value in order to keep
		// the selected row number
		// var v5 =
		// document.getElementById(currentMainFormId+js_inputHidden"+mainName);
		v1.value = currentSelectedRow;
 
		// also update its class style to the previous style of the row
		v1.className = curStyle;
	}
}
 
function clickExpandCollapseBtn(btnId, elem) {
	var p = elem.parentNode;
	while (p.nodeName != "TR") {
		p = p.parentNode;
	}
	selectRow(p);
	btn = document.getElementById(btnId);
	if (btn) {
		btn.click();
	}
}
 
// save the scroll pos for the table
function saveScrollPos(elem, pos) {
	var formId = tbl_getCurrentFormId();
	if (formId) {
		var divId = elem.id;
		var x = 0;
		var ie = true;
		if (tbl_isIE()) {
			x = divId.lastIndexOf('_div'); // if IE scroll the div
		} else // if NN scroll the tbody
		{
			x = divId.lastIndexOf(':tbody_element');
			ie = false;
		}
		var tableId = (ie) ? formId + divId.substring(0, x) : divId.substring(
				0, x);
		var scrollPosElem = document.getElementById(tableId
				+ "_currentScrollPos");
		if (scrollPosElem) {
			scrollPosElem.value = pos;
		}
	}
}