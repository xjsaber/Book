﻿<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="闭包.aspx.cs" Inherits="javascript王者.闭包" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
</head>
<body>
    <form id="form1" runat="server">
    <div>
    
    </div>
    </form>
    <script type="text/javascript">
        <!--
//        function f1() {
//            var n = 999;
//            function f2() {
//                alert(n);
//            }

//            return f2;
//        }

//        var result = f1();
        //        result();

        function f1() {
            var n = 999;
            nAdd = function () { n += 1 }
            function f2() {
                alert(n);
            }

            return f2;
        }

        var result = f1();
        result(); //999
        nAdd();
        result();// 1000
        -->       
    </script>
</body>
</html>
