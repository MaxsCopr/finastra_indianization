/**
 * This method is used to create XMLHttp object.
 *
 * @return
 */
function getXMLHttp() {

      var xmlHttp;
      try {
            // Firefox, Opera 8.0+, Safari
            xmlHttp = new XMLHttpRequest();
      } catch (e) { // Internet Explorer
            try {
                  xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
            } catch (e) {
                  try {
                        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
                  } catch (e) {
                        alert("Your browser does not support AJAX!");
                        return false;
                  }
            }
      }
      return xmlHttp;
}
function setFocus(elementId) {
      document.getElementById(elementId).focus();
}

function displayAtsErrorMessage(errorString, elementId) {
      document.getElementById(elementId).innerHTML = "<table align=\"center\" width=\"50px\" border=\"0\">\t<tr><td colspan=\"5\"><div class=\"roboError\">"
                  + errorString + "</div><br clear=\"all\"></td></tr></table>";
}

function getValueById(elementId) {
      return document.getElementById(elementId).value;
}
/**
 * This function is used to check wheather the data entered by the user is
 * numeric value or not. Returns true if the given value is numeric else returns
 * false.
 *
 * @param {}
 *            target
 * @return {Boolean}
 */
function numeric(target) {
      var numaric = target;
      if ((numaric != null) && (numaric != "")) {
            for ( var j = 0; j < numaric.length; j++) {
                  var alphaa = numaric.charAt(j);
                  var hh = alphaa.charCodeAt(0);
                  if ((hh > 47 && hh < 58)) {
                  } else {
                        return false;
                  }
            }
      } else {
            if (numaric == null || numaric == "") {
                  return false;
            }
      }
      return true;
}

/*
 * This function is used to check wheather the data entered by the user is
 * numeric value or not. Returns true if the given value is numeric else returns
 * false.
 *
 * @param {} target @return {Boolean}
 */
function numericWithDigit(target) {
      var numaric = target;
      if ((numaric != null) && (numaric != "")) {
            for ( var j = 0; j < numaric.length; j++) {
                  var alphaa = numaric.charAt(j);
                  var hh = alphaa.charCodeAt(0);
                  if (j == 0 && hh == 46) {
                        return false;
                  } else {
                        if (hh == 46 || (hh > 47 && hh < 58)) {
                        } else {
                              return false;
                        }
                  }
            }
      } else {
            if (numaric == null || numaric == "") {
                  return false;
            }
      }
      return true;
}

/**
 * This function is used to check wheather the date entered by the user is valid
 * date or not. It recives three arguments(day,month,year) and return true if
 * the given value is true otherwise returns false.
 *
 * @param {}
 *            Day
 * @param {}
 *            Mn
 * @param {}
 *            Yr
 * @return {Boolean}
 */
function isEnteredDateValid(day, month, year) {
      var DateVal = convertMonthNameToNumber(month) + "/" + day + "/" + year;
      var dt = new Date(DateVal);
      if (dt.getDate() != day
                  || dt.getMonth() != convertMonthNameToNumber(month) - 1
                  || dt.getFullYear() != year) {
            return (false);
      }
      return (true);
}
function convertMonthNameToNumber(monthName) {
      var myDate = new Date(monthName + " 1, 2000");
      var monthDigit = myDate.getMonth();
      return isNaN(monthDigit) ? 0 : (monthDigit + 1);
}
/**
 * This function is used to check wheather the data entered by the user is
 * Alphabet or not. Returns true if the given value is alphabet else returns
 * false.
 *
 * @param {}
 *            target
 * @return {Boolean}
 */
function alphabets(target) {
      var numaric = target;
      if ((numaric != null) || (numaric != "")) {
            for ( var j = 0; j < numaric.length; j++) {
                  var alphaa = numaric.charAt(j);
                  var hh = alphaa.charCodeAt(0);
                  if ((hh > 64 && hh < 91) || (hh > 96 && hh < 123) || hh == 32) {
                  } else {
                        return false;
                  }
            }
      }
      return true;
}
/**
 * This function is used to check wheather the data entered by the user is
 * Alphanumeric value or not. Returns true if the given value is alphabet or
 * numeric value else returns false.
 *
 * @param {}
 *            target
 * @param {}
 *            page
 * @return {Boolean}
 */
function alphanumeric(target) {
      var numaric = target;
      if ((numaric != null) || (numaric != "") || (hh == 32)) {
            for ( var j = 0; j < numaric.length; j++) {
                  var alphaa = numaric.charAt(j);
                  var hh = alphaa.charCodeAt(0);
                  if ((hh > 47 && hh < 58) || (hh > 64 && hh < 91)
                              || (hh > 96 && hh < 123)) {
                  } else {
                        return false;
                  }
            }
      }
      return true;
}
function alphanumericAddress(target) {
      var numaric = target;
      if ((numaric != null) || (numaric != "")) {
            for ( var j = 0; j < numaric.length; j++) {
                  var alphaa = numaric.charAt(j);
                  var hh = alphaa.charCodeAt(0);
                  if ((hh > 64 && hh < 91) || (hh > 96 && hh < 123)
                              || (hh > 47 && hh < 58) || (hh == 32) || (hh == 35)
                              || (hh == 44) || (hh == 45) || (hh == 47) || (hh == 10)) {
                  } else {
                        return false;
                  }
            }
      }
      return true;
}
function iscomma(number) {
      var hasCommaInString = false;
      var numaric = number;
      if ((numaric != null) || (numaric != "")) {
            for ( var j = 0; j < numaric.length; j++) {
                  var alphaa = numaric.charAt(j);
                  var hh = alphaa.charCodeAt(0);
                  if (hh == 44) {
                        hasCommaInString = true;
                  }
                  if (hasCommaInString) {
                        break;
                  }
            }
      }
      return hasCommaInString;
}
function round_number(number, dec_places) {
      // Version 2.0 (c) Copyright 2008, Russell Walker, Netshine Software
      // Limited. www.netshinesoftware.com
      var new_number = "";
      var i = 0;
      var sign = "";
      number = number.toString();
      number = number.replace(/^\s+|\s+$/g, "");
      if (number.charCodeAt(0) == 45) {
            sign = "-";
            number = number.substr(1).replace(/^\s+|\s+$/g, "");
      }
      dec_places = dec_places * 1;
      dec_point_pos = number.lastIndexOf(".");
      if (dec_point_pos == 0) {
            number = "0" + number;
            dec_point_pos = 1;
      }
      if (dec_point_pos == -1 || dec_point_pos == number.length - 1) {
            if (dec_places > 0) {
                  new_number = number + ".";
                  for (i = 0; i < dec_places; i++) {
                        new_number += "0";
                  }
                  if (new_number == 0) {
                        sign = "";
                  }
                  return sign + new_number;
            } else {
                  return sign + number;
            }
      }
      var existing_places = (number.length - 1) - dec_point_pos;
      if (existing_places == dec_places) {
            return sign + number;
      }
      if (existing_places < dec_places) {
            new_number = number;
            for (i = existing_places; i < dec_places; i++) {
                  new_number += "0";
            }
            if (new_number == 0) {
                  sign = "";
            }
            return sign + new_number;
      }
      var end_pos = (dec_point_pos * 1) + dec_places;
      var round_up = false;
      if ((number.charAt(end_pos + 1) * 1) > 4) {
            round_up = true;
      }
      var digit_array = new Array();
      for (i = 0; i <= end_pos; i++) {
            digit_array[i] = number.charAt(i);
      }
      for (i = digit_array.length - 1; i >= 0; i--) {
            if (digit_array[i] == ".") {
                  continue;
            }
            if (round_up) {
                  digit_array[i]++;
                  if (digit_array[i] < 10) {
                        break;
                  }
            } else {
                  break;
            }
      }
      for (i = 0; i <= end_pos; i++) {
            if (digit_array[i] == "." || digit_array[i] < 10 || i == 0) {
                  new_number += digit_array[i];
            } else {
                  new_number += "0";
            }
      }
      if (dec_places == 0) {
            new_number = new_number.replace(".", "");
      }
      if (new_number == 0) {
            sign = "";
      }
      return sign + new_number;
}
/**
 * This method is used to insert comma between the numerical values.
 *
 * @param {}
 *            textObject
 */
function insertComma(textObject) {
      var number = document.getElementById(textObject).value;
      number = number.split(" ").join("");
      number = decimalToInteger(number);
      if (!iscomma(number)) {
            number = "" + number;
            var len = number.length;
            var output = number;
            if (len > 3) {
                  for ( var i = len; i > 0; i = i - 2) {
                        if (i == len) {
                              i = i - 3;
                              var temp = output.substring(0, i);
                              var temp1 = output.substring(i);
                              output = temp + "," + temp1;
                        } else {
                              var temp = output.substring(0, i);
                              var temp1 = output.substring(i);
                              output = temp + "," + temp1;
                        }
                  }
                  document.getElementById(textObject).value = output;
            }
            var firstChar = number.charAt(0);
            var firstCharCode = firstChar.charCodeAt(0);
            if (firstCharCode == 45) {
                  output = 0;
            }
            if (output == 0) {
                  document.getElementById(textObject).value = "";
            } else {
                  if (output == "NaN") {
                        document.getElementById(textObject).value = "";
                  } else {
                        document.getElementById(textObject).value = output;
                  }
            }
      }
}
/**
 * This function checks wheather the user entered data contains comma or not.
 *
 * @param {}
 *            number
 * @return {}
 */
function isCommaExists(number) {
      var hasCommaInString = false;
      var numaric = number;
      if ((numaric != null) || (numaric != "")) {
            for ( var j = 0; j < numaric.length; j++) {
                  var alphaa = numaric.charAt(j);
                  var hh = alphaa.charCodeAt(0);
                  if (hh == 44) {
                        hasCommaInString = true;
                  }
                  if (hasCommaInString) {
                        break;
                  }
            }
      }
      return hasCommaInString;
}
/**
 * This function converts decimal value in to integer and also Round the value
 * to the nearest interger.
 *
 * @param {}
 *            value
 * @return {}
 */
function decimalToInteger(value) {
      value = value.replace(/\,/g, "");
      var wholeNumber = round_number(value, 0);
      value = wholeNumber;
      return value;
}
/**
 * This function checks the E-mail ID entered by the user is a valid one or not.
 * Returns true if the given mail id is in proper format otherwise it returns
 * false.
 *
 * @param {}
 *            str
 * @return {Boolean}
 */
function checkEmail(str) {
      var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
      if (reg.test(str) == false) {
            return false;
      } else {
            return true;
      }
}
/**
 * This function is used to remove the space before and after the data entered
 * by the user.
 *
 * @param {}
 *            str
 * @return {}
 */
function trim(str) {
      return str.replace(/^\s+|\s+$/g, "");
}
/**
 * This function is used to Submit the form by using three arguments(form
 * name,parameter,method to invoke from the Action class ).
 *
 *
 * @param {}
 *            parameter
 * @param {}
 *            target
 */
function formSubmit(formName, parameter, target) {

      document.getElementById(parameter).value = target;
      document.forms[formName].submit();

}
/**
 * This overloaded function is used to Submit the form by using four
 * arguments(form name,parameter,method to invoke from the Action
 * class,selectbuttonid).
 *
 * @param {}
 *            formName
 * @param {}
 *            parameter
 * @param {}
 *            target
 */
function formSubmitByButtonId(formName, parameter, target, selectedButtonob) {
      document.getElementById(parameter).value = target;
      document.forms[formName].SelectedButton.value = selectedButtonob;
      document.forms[formName].submit();
}
/**
 * This function is used to get the value entered by the user by using its
 * elemet ID.
 *
 * @param {}
 *            elementId
 */
function getValueById(elementId) {
      return document.getElementById(elementId).value;
}

/**
 * This function is used to get the value entered by the user by using its name
 *
 * @param elementId
 * @returns
 */
function getValueByName(elementId) {
      return document.getElementsByName(elementId)[0].value;
}
/**
 * This function is used to set the value entered by the user by using its
 * elemet ID.
 *
 * @param {}
 *            elementId
 * @param {}
 *            value
 */
function setValueById(elementId, value) {
      document.getElementById(elementId).value = value;
}
/**
 * This function is used to set focus of the cursor in a particular field by
 * using its elemet ID.
 *
 * @param {}
 *            elementId
 */
function setFocus(elementId) {
      document.getElementById(elementId).focus();
}
/**
 * This function is used to check the maximum length of the value entered by the
 * user. receives two arguments (Element id,maximun length it accepts ).returns
 * true if the length not exceeds the Maximum length given otherwise returns
 * false.
 *
 * @param {}
 *            element
 * @param {}
 *            maxlength
 * @return {Boolean}
 */
function maxLength(number, maxlength) {
      // var number = getValueById(element);
      if (number.length > maxlength) {
            return false;
      } else {
            return true;
      }
}

function minLength(number, minlength) {
      if (number.length < minlength) {
            return false;
      } else {
            return true;
      }
}

function currentdate(number) {
      var currentTime = new Date();
      var currentdate = currentTime.getDate();
      return currentdate;
}

function currentyear(number) {
      var currentTime = new Date();
      var currentyear = currentTime.getFullYear();
      if (number > currentyear) {
            return false;
      } else {
            return true;
      }
}

function isDateValid(day, month, year) {
      var DateVal = month + "/" + day + "/" + year;
      var userDate = new Date(DateVal);

      // get today with no time
      var nowDate = new Date();
      nowDate.setHours(0);
      nowDate.setMinutes(0);
      nowDate.setSeconds(0);
      nowDate.setMilliseconds(0);

      if (userDate - 0 == nowDate - 0 || userDate < nowDate) // subracting zero
            // force correct ==
            // operator
            return true;
      else
            return false;
}

/**
 * Disable element
 *
 * @param element
 * @return
 */
function disableElement(elementId) {
      document.getElementById(elementId).disabled = true;
}

/**
 * Enable element
 *
 * @param element
 * @return
 */
function enableElement(elementId) {
      document.getElementById(elementId).disabled = false;
}

/**
 * This function disables the previous error message ,before displaying the
 * current error message.
 *
 * @param {}
 *            element
 */
function disableMessage(element) {
}
/**
 * This function is used to check wheather the particular check box is checked
 * or not.
 *
 * @param {}
 *            elementId
 *
 */
function isChecked(elementId) {
      if (document.getElementById(elementId).checked == true) {
            return true;
      } else {
            return false;
      }
}
/**
 *
 * This function is used to check the radio button by using its element Id.
 *
 * @param {}
 *            elementId
 */
function selectRadioButton(elementId) {
      document.getElementById(elementId).checked = true;
}
/**
 *
 * This function is used to de-select the the radio button by using its element
 * Id.
 *
 * @param {}
 *            elementId
 */
function unSelectRadioButton(elementId) {
      document.getElementById(elementId).checked = false;
}
/**
 * This function validates the user input and displays the error message. This
 * function receives the error string to be displayed and the corresponding
 * elementId.
 *
 * @param {}
 *            errorString
 * @param {}
 *            elementId
 */
function displayErrorMessage(errorString, elementId) {
      document.getElementById(elementId).innerHTML = errorString;
}
/**
 * This function receives the errorstring and current error,and appends the
 * error message to Error string and returns the error string.
 *
 * @param {}
 *            errorString
 * @param {}
 *            errorMessage
 * @return {} errorString
 */
function concateErrorMessage(errorString, errorMessage) {
      errorString = errorString + errorMessage;
      return errorString;
}

/**
 * This function is used to Hide a particular element.
 *
 * @param {}
 *            elementId
 */
function hideElement(elementId) {
      document.getElementById(elementId).style.display = "none";
}

/**
 * This function is used to Show a particular element.
 *
 * @param {}
 *            elementId
 */
function showElement(elementId) {
      document.getElementById(elementId).style.display = "block";
}

/**
 * This function removes the comma from the data entered by the user.
 *
 * @param {}
 *            value
 * @return {}
 */
function removeComma(value) {
      value = value.replace(/\,/g, "");
      return value;
}
/**
 *
 * This function is used to check the amount entered by the user. receives two
 * arguments (amount entered by the user,maximun length it accepts ).returns
 * true if the amount entered by the user not exceeds the Maximum amount given
 * otherwise returns false.
 *
 * @param {}
 *            target
 * @param {}
 *            amount
 * @return {Boolean}
 */
function greaterAmount(target, amount) {
      if ((target == null) || (target == "") || (target < amount)) {
            return true;
      } else {
            return false;
      }
}
/**
 *
 * This function is used to check the given PAN number is valid or not.
 *
 * @param {}
 *            target
 * @return {Boolean}
 */
function verifyPANNumber(panCardNO) {
      var alpha1 = "", alpha2 = "", number1 = "", char = "";
      if (panCardNO.length == 10) {
            alpha1 = panCardNO.substring(0, 5);
            alpha2 = panCardNO.substring(5, 9);
            number1 = panCardNO.substring(9, 10);
            char = panCardNO.charAt(3);
      }
      if (panCardNO.length != 0 && panCardNO.length != 10) {
            return false;
      } else {
            if (panCardNO.length != 0
                        && !(alphabets(alpha1) && numeric(alpha2) && alphabets(number1))) {
                  return false;
            } else {
                  if (char.length != 0 && char != "p" && char != "P") {
                        return false;
                  } else {
                        return true;
                  }
            }
      }
}

function verifyPANNumbernewformat(panCardNO) {
      var alpha1 = "", alpha2 = "", number1 = "";
      if (panCardNO.length == 10) {
            alpha1 = panCardNO.substring(0, 5);
            alpha2 = panCardNO.substring(5, 9);
            number1 = panCardNO.substring(9, 10);
      }
      if (panCardNO.length != 0 && panCardNO.length != 10) {
            return false;
      } else {
            if (panCardNO.length != 0
                        && !(alphabets(alpha1) && numeric(alpha2) && alphabets(number1))) {
                  return false;
            } else {
                  return true;
            }
      }
}
/**
 *
 * This function is used to check value is available or not in the field.
 *
 * @param {}
 *            target
 * @return {Boolean}
 */
function isFieldEmpty(target) {
      if (document.getElementById(target).value == "") {
            return true;
      } else {
            return false;
      }
}
function post_to_url(path, params, method) {
      method = method || "post"; // Set method to post by default, if not
      // specified.

      // The rest of this code assumes you are not using a library.
      // It can be made less wordy if you use one.
      var form = document.createElement("form");
      form.setAttribute("method", method);
      form.setAttribute("action", path);
      for ( var key in params) {
            var hiddenField = document.createElement("input");
            hiddenField.setAttribute("type", "hidden");
            hiddenField.setAttribute("name", key);
            hiddenField.setAttribute("value", params[key]);
            form.appendChild(hiddenField);
      }
      document.body.appendChild(form); // Not entirely sure if this is
      // necessary
      form.submit();
}

function submitLogoutForm(actionURL, paramName, paramValue) {

      document.logoutForm.action = actionURL;
      document.getElementById("logoutParamId").name = paramName;
      document.getElementById("logoutParamId").value = paramValue;
      document.logoutForm.submit();
}
/**
 *
 * This function is used to check whether the element is available or not.
 *
 * @param {}
 *            elementId
 * @return {Boolean}
 */
function isElementExist(elementId) {
      if (document.getElementById(elementId) == null) {
            return false;
      } else {
            return true;
      }
}

/**
 * This method will tell whether element hidden or not.
 *
 * @param elementId
 * @return
 */
function isElementHidden(elementId) {
      if (document.getElementById(elementId).style.display == 'none')
            return true;
      return false;
}
/**
 *
 * This function is used to check whether the element is disabled or not.
 *
 * @param {}
 *            elementId
 * @return {Boolean}
 */
function isElementDisabled(elementId) {
      if (document.getElementById(elementId).disabled == true) {
            return true;
      } else {
            return false;
      }
}
/**
 *
 * This function is used to check whether the value is numeric amount or not.
 *
 * @param {}
 *            elementId
 * @return {Boolean}
 */
function numericAmt(target) {
      var numaric = target;
      if ((numaric != null) || (numaric != "")) {
            if (numaric != 0) {
                  var a = 0;
                  for ( var j = 0; j < numaric.length; j++) {
                        var alphaa = numaric.charAt(j);
                        var hh = alphaa.charCodeAt(0);
                        if ((hh > 47 && hh < 58)) {
                        } else {
                              if (hh == 46) {
                                    a = a + 1;
                                    if (a > 1) {
                                          return false;
                                    }
                              } else {
                                    return false;
                              }
                        }
                  }
            }
      }
      return true;
}
/**
 *
 * This function is used to get the Faq answer for the particular question.
 *
 * @param {}
 *            selectedButtonOb
 */
function callFaq(selectedButtonOb) {
      var category = getValueById("category");
      window.open("UniversalHelp.do?universalhelpparam=init&questionId="
                  + selectedButtonOb.id + "&category=" + category, "faq",
                  "menubar=1,resizable=1,scrollbars=1,width=750,height=400");
}
function callHelpMe(selectedButtonOb) {
      var category = "helpme";
      window.open("UniversalHelp.do?universalhelpparam=init&helpMeUrl="
                  + selectedButtonOb.id + "&category=" + category, "helpme",
                  "menubar=1,resizable=1,scrollbars=1,width=750,height=400");
}
/**
 *
 * This function is used to fetch the corresponding page when the user click the
 * particular link
 *
 * @param {}
 *            selectedButtonOb
 */
function callForum(selectedButtonOb) {
      window.open(selectedButtonOb.id, "Forum",
                  "menubar=1,resizable=1,scrollbars=1,width=480,height=400");
}
/**
 * This method is used to highlight the toppanel for the particular page
 */
function highlightHeaderTop(linkid, spanid) {
      document.getElementById(linkid).className = "aero selected";
      document.getElementById(spanid).className = "selected";
}

function disableEnterKey(e) {
      var key;

      if (window.event)
            key = window.event.keyCode; // IE
      else
            key = e.which; // firefox

      if (key == 13)
            return false;
      else
            return true;
}

function noenter(field, e) {
      var eve = e || window.event;
      var keycode = eve.keyCode || eve.which || eve.charCode;
      if (keycode == 13) {
            eve.cancelBubble = true;
            eve.returnValue = false;
            if (eve.stopPropagation) {
                  eve.stopPropagation();
                  eve.preventDefault();
            }
            return false;
      }
}
function formSubmitEdit(formName, parameter, selectedButtonObj, target) {
      var selectedButtonIndex = selectedButtonObj.id;
      document.getElementById(parameter).value = target;
      document.forms[formName].selectedIndex.value = selectedButtonIndex;
      document.forms[formName].submit();
}

/**
 * If you want to set the radio button or other button able or disabled pass the
 * styleId and boolean type
 */
function setBoolean(element, booleanType) {
      document.getElementById(element).disabled = booleanType;
}
function setHeight() {
      var h;
      var fr1 = document.getElementById("tabcontent");
      if (document.all) {
            h = fr1.contentWindow.document.body.scrollHeight;
      } else {
            h = fr1.contentWindow.document.body.offsetHeight;
      }
      fr1.setAttribute("height", h + 10);
}
function addJavascript(jsname, pos) {
      var th = document.getElementsByTagName(pos)[0];
      var s = document.createElement("script");
      s.setAttribute("type", "text/javascript");
      s.setAttribute("src", jsname);
      th.appendChild(s);
}
function alltrim(str) {
      return str.replace(/^\s+|\s+$/g, "");
}
function isFloat(str) {
      str = alltrim(str);
      return /^[-+]?[0-9]+(\.\d{1,3})?$/.test(str);
}

function callErrorDisplay(errorString, errorDivId) {
      if (isElementExist(errorDivId)) {
            document.getElementById(errorDivId).style.visibility = "hidden";
      }
      errorString = "<ul>" + errorString + "</ul>";
      displayErrorMessage(errorString, "errors");
}

/**
 * This is to Trim and check for empty
 */
function isEmpty(target) {

      if (trim(getValueById(target)) == "" || getValueById(target) == null) {
            return true;
      } else {
            return false;
      }
}

/**
 * This is to Trim and check for empty
 */
function isValueEmpty(value) {

      if (trim(value) == "" || value == null) {
            return true;
      } else {
            return false;
      }
}

function disableEnterKey(e) {
      var key;
      if (window.event)
            key = window.event.keyCode;
      else
            key = e.which;
      return (key != 13);
}
function numericWithSpecialChar(target) {
      var numaric = target;
      if ((numaric != null) || (numaric != "")) {
            for ( var j = 0; j < numaric.length; j++) {
                  var alphaa = numaric.charAt(j);
                  var hh = alphaa.charCodeAt(0);
                  if ((hh > 41 && hh < 58) || (hh == 32)) {
                  } else {
                        return false;
                  }
            }
      }
      return true;
}

function currentmonth(number) {
      if (number == Jan) {
            return Jan_value;
      } else if (number == Feb) {
            return Feb_value;
      } else if (number == Mar) {
            return Mar_value;
      } else if (number == Apr) {
            return Apr_value;
      } else if (number == May) {
            return May_value;
      } else if (number == Jun) {
            return Jun_value;
      } else if (number == Jul) {
            return Jul_value;
      } else if (number == Aug) {
            return Aug_value;
      } else if (number == Sep) {
            return Sep_value;
      } else if (number == Oct) {
            return Oct_value;
      } else if (number == Nov) {
            return Nov_value;
      } else {
            return Dec_value;
      }
}

/**
 * myshare card validation
 *
 * @param Luhn
 * @returns {Number}
 */
function Calculate(Luhn) {
      var sum = 0;
      var i;
      for (i = 0; i < Luhn.length; i++) {
            sum += parseInt(Luhn.substring(i, i + 1));
      }
      var delta = new Array(0, 1, 2, 3, 4, -4, -3, -2, -1, 0);
      for (i = Luhn.length - 1; i >= 0; i -= 2) {
            var deltaIndex = parseInt(Luhn.substring(i, i + 1));
            var deltaValue = delta[deltaIndex];
            sum += deltaValue;
      }
      var mod10 = sum % 10;
      mod10 = 10 - mod10;
      if (mod10 == 10) {
            mod10 = 0;
      }
      return mod10;
}
/**
 * Validate myshare card
 *
 * @param Luhn
 * @returns {Boolean}
 */
function validCard(Luhn) {
      var LuhnDigit = parseInt(Luhn.substring(Luhn.length - 1, Luhn.length));
      var LuhnLess = Luhn.substring(0, Luhn.length - 1);
      if (Calculate(LuhnLess) == parseInt(LuhnDigit)) {
            return true;
      }
      return false;
}

/**
 * Custom UI for checkbox function
 */
/*
 * $(function() { $('input').checkBox();
 *
 * });
 */

function isDateGreater(startDate, endDate) {
      var startDateCheck = startDate;
      var endDateCheck = endDate;

      var startDate = Date.parse(startDateCheck);
      var endDate = Date.parse(endDateCheck);
      if (startDate > endDate) {
            return true;
      } else
            return false;
}

function popup() {
      var url = "static-transaction?param=latestfive";
      window
                  .open(
                              url,
                              "_blank",
                              "directories=no,resizable=yes,scrollbars=yes, status=no,width=700, height=500,top=0,left=0");
}

function resetValues() {
      setValueById("customerId", "");
      setValueById("receiptNumber", "");
      setValueById("cardNumber", "");
      setValueById("firstName", "");
      setValueById("lastName", "");
      setValueById("emailId", "");
      setValueById("mobileNumber", "");
      setValueById("city", "");
      setValueById("country", "");
      setValueById("pincode", "");
      setValueById("ngoName", "");
      setValueById("myshareCardNumber", "");
      setValueById("dob", "");
      setValueById("rid", "");
      setValueById("modeOfRegistration", "");
      setValueById("modeOfPayment", "");
      setValueById("paymentStatus", "");
      setValueById("reportrange", "");
      setValueById("reportrange1", "");
      setValueById("minor", "");
      setValueById("eightygApplied", "");
      setValueById("receiptno", "");
      setValueById("shippingstatus", "");
      setValueById("shippingstatus", "");
      setValueById("orderId", "");
      setValueById("incomplete", "");
      setValueById("entityname", "");
      // setValueById("activecustomers","");
}
/* addJavascript('scripts/commonMessages.js', 'head'); */

/** Retailer Module Common methods */

/**
 * Update combo box
 */
function updateComboBox(data, id, clas,select) {
      var list="";
      //alert('data='+data);
      if(!select)
      {
      list = "<option value='<----->'><-----></option>";
      }

      $.each(data, function(key, etype) {
            list += '<option value="' + etype.value + '">' + etype.value + '</option>';
      });
      
      if(list=="")
      {
            list = "<option value='<----->'><-----></option>";
      }
      if (!clas)
            {
            $('#' + id).html(list);
            }
      else
            {
            $('.' + id).html(list);
            }
};


/**
 * Auto close alert in modals
 *
 * @param $modal
 * @param message
 */
function createAutoClosingAlert($modal, message) {
      $modal
                  .modal('loading')
                  .find('.modal-body')
                  .prepend(
                              '<div class="alert alert-info fade in">'
                                          + message
                                          + '<button type="button" class="close" data-dismiss="alert">&times;</butto>'
                                          + '</div>');
      setTimeout(function() {
            $(".alert").alert('close');
      }, 1500);
}
/**
 * lanchModal
 *
 * @param buttonId
 * @param modalId
 * @param url
 * @param callback
 * @returns
 */
function launchModal(buttonId, modalId, url, callback) {
      var $modal = $('#' + modalId);
      $(buttonId).live('click', function(e) {
            e.preventDefault();
            // create the backdrop and wait for next modal to be triggered
            $('body').modalmanager('loading');
            $modal.load(url, function() {
                  $modal.modal();
                  callback();
            });
      });
      return $modal;
}

function isNumberKey(evt)
{
   var charCode = (evt.which) ? evt.which : event.keyCode;
   if (charCode != 46 && charCode > 31
     && (charCode < 48 || charCode > 57))
      return false;

   return true;
}
/**
 * First letter in caps
 * @param str
 * @returns
 */
function ucwords (str) {
        return (str + '').replace(/^([a-z\u00E0-\u00FC])|\s+([a-z\u00E0-\u00FC])/g, function ($1) {
          return $1.toUpperCase();
        });
      }

function isValidDate(dateStr) {
    var currVal = dateStr;
    if(currVal == '')
        return false;
   
    var rxDatePattern = /^(\d{1,2})(\/|-)(\d{1,2})(\/|-)(\d{4})$/; //Declare Regex
    //var rxDatePattern='-';
    var dtArray = currVal.match(rxDatePattern); // is format OK?
    //alert(dtArray);
    if (dtArray == null)
        return false;
   
    //Checks for mm/dd/yyyy format.
    dtMonth = dtArray[3];
    dtDay= dtArray[1];
    dtYear = dtArray[5];        
   
    if (dtMonth < 1 || dtMonth > 12)
        return false;
    else if (dtDay < 1 || dtDay> 31)
        return false;
    else if ((dtMonth==4 || dtMonth==6 || dtMonth==9 || dtMonth==11) && dtDay ==31)
        return false;
    else if (dtMonth == 2)
    {
        var isleap = (dtYear % 4 == 0 && (dtYear % 100 != 0 || dtYear % 400 == 0));
        if (dtDay> 29 || (dtDay ==29 && !isleap))
                return false;
    }
    return true;
      
      }

function isDateCompare(startDate, endDate) {
      
      var chequeDate = startDate;
      
      var ackDate = endDate;
   
    var rxDatePattern = /^(\d{1,2})(\/|-)(\d{1,2})(\/|-)(\d{4})$/; //Declare Regex
    //var rxDatePattern='-';
    var chequeDateArray = chequeDate.match(rxDatePattern); // is format OK?
       
    //Checks for mm/dd/yyyy format.
    dtMonth = chequeDateArray[3];
    dtDay= chequeDateArray[1];
    dtYear = chequeDateArray[5];  
   
    var dateOne = new Date(dtYear, dtMonth, dtDay);
   
    var ackDateArray = ackDate.match(rxDatePattern); // is format OK?
   
    //Checks for mm/dd/yyyy format.
    dtMonth = ackDateArray[3];
    dtDay= ackDateArray[1];
    dtYear = ackDateArray[5];  
    var dateTwo = new Date(dtYear, dtMonth, dtDay);
   
    if (dateOne > dateTwo) {
       return false;
    }else {
       return true;
    }
}