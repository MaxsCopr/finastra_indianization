/**
* @author Administrator
*/
/* <![CDATA[ */
            jQuery(function(){
                jQuery("#txtverificationcode").validate({
                    expression: "if (VAL) return true; else return false;",
                    message: "Enter verification code"
                });
                jQuery("#txtemail").validate({
                    expression: "if (VAL.match(/^[^\\W][a-zA-Z0-9\\_\\-\\.]+([a-zA-Z0-9\\_\\-\\.]+)*\\@[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*\\.[a-zA-Z]{2,4}$/)) return true; else return false;",
                    message: "Enter a valid email id"
                });
                jQuery("#txtnewpassword").validate({
                    expression: "if (VAL.length > 5 && VAL) return true; else return false;",
                    message: "Enter a valid password"
                });
                jQuery("#txtretypepassword").validate({
                    expression: "if ((VAL == jQuery('#txtnewpassword').val()) && VAL) return true; else return false;",
                    message: "Retype password doesn't match the new password"
                });

            });
            /* ]]> */
			function changepassword(){
				alert("Your password has been changed sucessfully")
			}
			function continueemail(){
				alert("Enter the Email id first")
			}