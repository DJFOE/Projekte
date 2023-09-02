<?php
session_start();

// Does IP Address match?
if ($_SERVER['REMOTE_ADDR'] != $_SESSION['ipaddress'])
    {
        session_unset();
session_destroy();
}

// Does user agent match?
if ($_SERVER['HTTP_USER_AGENT'] != $_SESSION['useragent'])
    {
        session_unset();
session_destroy();
}

// Is the last access over an hour ago?
if (time() > ($_SESSION['lastaccess'] + 3600))
    {
        session_unset();
session_destroy();
}
else
{
$_SESSION['lastaccess'] = time();
}