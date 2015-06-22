<?php
include_once 'session_info.php';
?>

<!DOCTYPE html>
<html>
<head>
    <title>IT-Sicherheit Admin Tool</title>
    <meta charset="UTF-8">

    <!-- stylesheet -->
    <link rel="stylesheet" href="css/style.css"/>
</head>
<body>
<form id="changePassword" action="userdata.php" method="POST">
    <script type="text/javascript">
        function compare() {
            var form = document.getElementById("changePassword");
            var inputs = form.querySelectorAll('input[type="password"]');
            if (inputs[0].value == inputs[1].value) {
                form.submit();
            } else {
                form.querySelector('span[name="error_msg"]').className = "error";
            }
        }
    </script>

    <span class="hide" name="error_msg">Die beiden Passw&ouml;rter sind nicht gleich!</span>

    <p>
        <label>Neues Passwort:</label>
        <input type="password" value=""/>
    </p>

    <p>
        <label>Neues Passwort wiederholen:</label>
        <input type="password" name="new_password" value=""/>
    </p>
    <input class="hide" type="text" name="action" value="pw"/>
    <input type="button" value="Passwort &auml;ndern" onclick="compare()"/>
</form>
</body>
</html>