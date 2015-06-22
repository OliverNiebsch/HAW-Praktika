<!DOCTYPE html>
<html>
<head>
    <title>IT-Sicherheit Admin Tool</title>
    <meta charset="UTF-8">

    <!-- stylesheet -->
    <link rel="stylesheet" href="css/style.css"/>

    <script type="text/javascript">
        function markAndShow(nr, what, user) {
            var trs = document.querySelectorAll(".users tr");
            for (var i = 0; i < trs.length; i++) {
                trs[i].className = "";
            }

            document.querySelectorAll(".users tbody tr")[nr].className = "marked";

            document.getElementById("mail").className = "";
            document.getElementById("changeMail").action = "userdata.php?user=" + user;
        }
    </script>
</head>
<body>
    <span>Du bist im Admin Tool</span>

    <div class="users">
        <table>
        <?php
        include_once 'helpers.php';
        $users = getAllUserData();

        $head = "";
        // table header
        foreach ($users[0] as $key => $val) {
            if ($key == "id")
                continue;
            $head .= "<th>$key</th>";
        }

        echo "<thead><tr>" . $head . "<th></th><th></th><th></th></tr><trhead>";
        echo "<tbody>";

        for ($i = 0; $i < count($users); $i++) {
            $row = "<tr>";

            foreach ($users[$i] as $key => $val) {
                if ($key == "id")
                    continue;

                if ($val === true || $val === false) {
                    $val = $val ? "wahr" : "falsch";
                }
                $row .= "<td>$val</td>";
            }

            $id = $users[$i]['id'];

            $btn = '<td><form action="userdata.php?user=' . $id . '" method="POST"><input class="hide" type="text" name="action" value="forgot_pw" /><input class="button" type="submit" value="Passwort &auml;ndern" /></form></td>';
            $btn .= '<td><input type="button" value="Mail &auml;ndern" onclick="markAndShow(' . $i . ', ' . $id . ')"/></td>';

            $btn .= '<td><form id="delete_user" action="userdata.php?user=' . $id . '" method="POST"><input class="hide" type="text" name="action" value="delete" /><input class="button" type="submit" value="Benutzer l&ouml;schen" /></form></td>';
            echo $row . $btn . "</tr>";
        }
        echo "</tbody>";
        ?>
        </table>
    </div>
    <div class="hide" id="mail">
        <?php include_once 'change_mail.php' ?>
    </div>
</body>
</html>