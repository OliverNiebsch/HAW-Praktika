<?php
include_once 'session_info.php';

$db = new SQLite3("db/sqldb.db");

function getAllUserData()
{
    global $db;
    if (isAdmin()) {
        // alle User zurück geben
        $user_ary = array();
        //$users = $db->query("SELECT user.ID as id, mail, role, pass_changed, mail_auth.request_time as mail_time, pass_request.request_time as pass_time FROM user LEFT JOIN mail_auth ON user.ID==mail_auth.user_id LEFT JOIN pass_request ON user.ID==mail_auth.user_id"); // TODO: Status

        $users = $db->query("SELECT user.ID as id, mail, role, pass_changed FROM user");

        if (!$users) {
            echo $db->lastErrorMsg();
        } else {
            while ($row = $users->fetchArray(SQLITE3_ASSOC)) {
                $max_time = time() + 1800;
                $mail_status = false;
                $pass_status = false;

                $result = $db->query("SELECT request_time FROM mail_auth WHERE user_id=" . $row['id'])->fetchArray(SQLITE3_ASSOC);
                if (is_array($result) && $result['request_time'] < $max_time) {
                    $mail_status = true;
                }

                $result = $db->query("SELECT request_time FROM pass_request WHERE user_id=" . $row['id'])->fetchArray(SQLITE3_ASSOC);
                if (is_array($result) && $result['request_time'] < $max_time) {
                    $pass_status = true;
                }

                $role = $row['role'] == 2 ? "Admin" : "Benutzer";
                $user_ary[] = array('id' => $row['id'], 'mail' => $row['mail'], 'role' => $role, 'pass_changed' => $row['pass_changed'] == 1, 'mail_status' => $mail_status, 'pass_status' => $pass_status);
            }
        }
    }
    return $user_ary;
}

function changeUserData($values, $user_id) {
    global $db;
    if (!$db->query("UPDATE user SET " . $values . " WHERE ID=" . $user_id)) {
        return $db->lastErrorMsg();
    }

    return "";
}

function deleteUser($user_id) {
    global $db;
    if (!$db->query("DELETE FROM user WHERE id=" . $user_id)) {
        return $db->lastErrorMsg();
    }

    return "";
}

function insertData($table, $keys, $values) {
    global $db;
    if (!$db->query("INSERT INTO $table (" . join(',', $keys) . ") VALUES (" . join(',', $values) . ")")) {
        return $db->lastErrorMsg();
    }

    return "";
}

?>