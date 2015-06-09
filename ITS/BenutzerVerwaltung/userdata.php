<?php
include_once 'session_info.php';


switch ($_SERVER['REQUEST_METHOD']) {
    case 'POST':
        if (isset($_POST['user']) && !empty($_POST['user'])) {
            if (isAdmin()) {
                // Benutzerdaten sollen geändert werden

            }
        } else {
            // eigener Benutzer soll geändert werden
        }
        break;

    case 'GET':
        if (isset($_GET['user']) && !empty($_GET['user']) && isAdmin()) {
            // Benutzerdaten werden angefragt
        }
        break;
}

        header('Location: /');
?>