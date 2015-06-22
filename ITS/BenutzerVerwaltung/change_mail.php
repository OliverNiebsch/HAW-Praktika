<?php
    include_once 'session_info.php';
?>
<form id="changeMail" action="userdata.php" method="post">
    <p>
        <label>Neue E-Mail-Adresse:</label>
        <input name="new_mail" type="email" value="" />
    </p>

    <input class="hide" type="text" name="action" value="mail" />
    <input type="submit" value="E-Mail &auml;ndern" />
</form>