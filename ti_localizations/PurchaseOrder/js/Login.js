/**
* @author Administrator
*/
/* <![CDATA[ */
            jQuery(function(){
                jQuery("#txtusername").validate({
                    expression: "if (VAL) return true; else return false;",
                    message: "Enter the username"
                });
                jQuery("#txtpassword").validate({
                    expression: "if (VAL.length > 5 && VAL) return true; else return false;",
                    message: "Enter valid password"
                });
            });
            /* ]]> */