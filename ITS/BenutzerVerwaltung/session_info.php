<?php
session_start();

function isUserLoggedIn()
{
    return isset($_SESSION['user']) && !empty($_SESSION['user']);
}

function isAdmin()
{
    return isset($_SESSION['user']) && !empty($_SESSION['user']) && isset($_SESSION['admin']) && !empty($_SESSION['admin']) && $_SESSION['admin'] == true;
}

function getLoggedInUser()
{
    return $_SESSION['user'];
}

?>