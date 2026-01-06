/**
* @author Administrator
*/
/* <![CDATA[ */
            jQuery(function(){
                jQuery("#txtname").validate({
                    expression: "if (VAL) return true; else return false;",
                    message: "Enter the name"
                });
				jQuery("#fileupload").validate({
                    expression: "if (VAL) return true; else return false;",
                    message: "Browse the logo"
                });
				jQuery("#selecttheme").validate({
                    expression: "if (VAL != '0') return true; else return false;",
                    message: "Please make a selection"
                });
				jQuery("#txtusername").validate({
                    expression: "if (VAL) return true; else return false;",
                    message: "Enter the username"
                });

                jQuery("#txtEmail").validate({
                    expression: "if (VAL.match(/^[^\\W][a-zA-Z0-9\\_\\-\\.]+([a-zA-Z0-9\\_\\-\\.]+)*\\@[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*\\.[a-zA-Z]{2,4}$/)) return true; else return false;",
                    message: "Enter a valid email id"
                });
                jQuery("#txtpassword").validate({
                    expression: "if (VAL.length > 5 && VAL) return true; else return false;",
                    message: "Enter a valid password"
                });
                jQuery("#txtConfirmPassword").validate({
                    expression: "if ((VAL == jQuery('#txtpassword').val()) && VAL) return true; else return false;",
                    message: "Confirm password doesn't match the password"
                });

            });
            /* ]]> */