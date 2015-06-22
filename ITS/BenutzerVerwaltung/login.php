<?php
$fail = "?login=failed";
if ($_SERVER['REQUEST_METHOD'] == 'POST' && isset($_POST['email']) && !empty($_POST['email']) && isset($_POST['password']) && !empty($_POST['password'])) {
    $mail = $_POST['email'];
    $pw = $_POST['password'];

    $db = new SQLite3("db/sqldb.db");
    $result = $db->query("SELECT ID, pass, role FROM user WHERE mail='$mail'");
    $row = $result->fetchArray(SQLITE3_ASSOC);

    if (password_verify($pw, $row['pass'])) {
        // Login korrekt
        $fail = "";

        session_start();
        $_SESSION['user_id'] = $row['ID'];
        $_SESSION['user'] = $mail;
        $_SESSION['admin'] = $row['role'] == 2;
    }
}

// redirect zur Hauptseite
header("Location: index.php" . $fail);
?>