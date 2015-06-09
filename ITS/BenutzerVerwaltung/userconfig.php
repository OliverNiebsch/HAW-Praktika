<!DOCTYPE html>
<html>
<head>
    <title>IT-Sicherheit Benutzer Einstellungen</title>
    <meta charset="UTF-8">

    <!-- stylesheet -->
    <link rel="stylesheet" href="css/style.css"/>

</head>
<body>

<?php
    include 'change_pw.php';
?>

<form id="change_mail" action="userdata.php" method="post">
    <p>
        <label>Neue E-Mail-Adresse:</label>
        <input type="email" value="" />
    </p>

    <input class="hide" type="text" name="type" value="email" />
    <input type="submit" value="E-Mail ändern" />
</form>
</body>
</html>