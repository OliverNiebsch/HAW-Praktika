<?php
include_once 'helpers.php';

switch ($_SERVER['REQUEST_METHOD']) {
    case 'GET':
        $db = new SQLite3("db/sqldb.db");
        if (isset($_GET['confirmMail']) && !empty($_GET['confirmMail'])) {
            $res = $db->query("SELECT * FROM mail_auth WHERE phrase='" . $_GET['confirmMail'] . "'");
            $vals = $res->fetchArray(SQLITE3_ASSOC);

            if (!$vals) {
                echo $db->lastErrorMsg();
            } elseif ($vals === true) {
                echo 'nichts gefunden in der DB';
                return;
            } else {
                if ($vals['request_time'] > time() + 1800) {
                    // timeout
                    http_response_code(404);
                    echo 'Link ist zu alt';
                } else {
                    if (changeUserData("mail='" . $vals['new_mail'] . "'", $vals['user_id']) == "") {
                        $db->query("DELETE FROM mail_auth WHERE ID=" . $vals['id']);

                        echo '<span>Mail erfolgreich geändert</span>';
                        echo '<a href="/">Zur&uuml;ck</a>';
                    }
                }
            }
        } elseif (isset($_GET['confirmPass']) && !empty($_GET['confirmPass'])) {
            $res = $db->query("SELECT * FROM pass_request WHERE phrase='" . $_GET['confirmPass'] . "'");
            $vals = $res->fetchArray(SQLITE3_ASSOC);

            if (!$vals) {
                echo $db->lastErrorMsg();
            } elseif ($vals === true) {
                echo 'nichts gefunden in der DB';
                return;
            } else {
                if ($vals['request_time'] > time() + 1800) {
                    // timeout
                    http_response_code(404);
                    echo 'Link ist zu alt';
                } else {
                    include_once 'change_pw.php';
                    return;
                }
            }
        }
        break;
    case 'POST':
        $user = null;
        if (isset($_GET['user']) && !empty($_GET['user'])) {
            if (isAdmin()) {
                // Benutzerdaten sollen geändert werden
                $user = $_GET['user'];
            } else {
                http_response_code(403);
                echo "not allowed";
            }
        } else {
            // eigener Benutzer soll geändert werden
            $user = getLoggedInUserId();
        }

        $res = "";
        if (!(isset($_POST['action']) && !empty($_POST['action']))) {
            http_response_code(404);
            echo "neeeeee";
            break;
        }

        $action = $_POST['action'];
        $rnd = sha1("lpon" . rand());
        $time = time();
        if ($action == "delete" && isAdmin()) {
            $res = deleteUser($user);
        } elseif ($action == "forgot_pw") {
            $link = "https://itsec-support.informatik.haw-hamburg.de:51006/userdata.php?confirmPass=" . $rnd;
            $res = insertData("pass_request", array('user_id', 'phrase', 'request_time'), array($user, "'" . $rnd . "'", $time));
            echo $link;

        } elseif ($action == "pw" && isset($_POST['new_password']) && !empty($_POST['new_password'])) {
            $res = changeUserData("pass='" . password_hash($_POST['new_password'], PASSWORD_BCRYPT) . "', pass_changed=1", $user);

        } elseif ($action == "mail" && isset($_POST['new_mail']) && !empty($_POST['new_mail'])) {
            $link = "https://itsec-support.informatik.haw-hamburg.de:51006/userdata.php?confirmMail=" . $rnd;
            insertData("mail_auth", array('user_id', 'request_time', 'phrase', 'new_mail'), array($user, $time, "'" . $rnd . "'", "'" . $_POST['new_mail']. "'"));
            echo $link;

        } else {
            http_response_code(404);
            echo "neeeeee";
        }

        if ($res != "") {
            echo 'Fehler: ' . $res;
        }

        break;
}
?>