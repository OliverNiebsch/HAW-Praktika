<!DOCTYPE html>
<html>
<head>
    <title>IT-Sicherheit Login</title>
    <meta charset="UTF-8">

    <!-- stylesheet -->
    <link rel="stylesheet" href="css/style.css"/>

</head>
<body>
<form name='login' action='login.php' method='post'>
    <?php
        if (isset($failed) && $failed) {
            echo '<div class="error">Login fehlgeschlagen! E-Mail oder Passwort falsch.</div>';
        }
    ?>
    <p>
        <label>E-Mail:</label>
        <input type='text' name='email'/>
    </p>

    <p>
        <label>Passwort:</label>
        <input type='password' name='password'/>
    </p>

    <p>
        <label>&nbsp;</label>
        <input type='submit' value='Login' class='submit'/>
    </p>
</form>
</body>
</html>