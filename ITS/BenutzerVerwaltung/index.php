<?php
    include_once 'session_info.php';

    if (isUserLoggedIn()) {
        // Benutzer ist bereits angemeldet
        if (isAdmin()) {
            // ein Admin - juhu!
            include "admintool.php";
        } else {
            // ein normaler Nutzer ...
            include "userconfig.php";
        }
    } else {
        $failed = isset($_GET['login']) && $_GET['login'] == 'failed';
        // noch nicht angemeldet -> Login-Seite
        include "login_site.php";
    }
?>
