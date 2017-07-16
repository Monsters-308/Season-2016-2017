<?php

$time = $_GET['time'];
$type = $_GET['type'];

$servername = "";
$username = "";
$password = "";
$dbname = "";

header('Content-Type: text/csv; charset=utf-8');
header('Content-Disposition: attachment; filename=data.csv');

// create a file pointer connected to the output stream
$output = fopen('php://output', 'w');

// Create connection
$conn = mysqli_connect($servername, $username, $password, $dbname);

if (!$conn) {
    die("Connection failed");
}


if($type == "MATCH"){
	if(isset($time)){
		$sql = "SELECT time_now, qrcode FROM scouting_qrcode WHERE type='MATCH' AND time_now > ".$time;
	}
	else{
		$sql = "SELECT time_now, qrcode FROM scouting_qrcode WHERE type='MATCH'";
	}
	
}
else if ($type == "PIT") {
	if(isset($time)){
		$sql = "SELECT time_now, qrcode FROM scouting_qrcode WHERE type='PIT' AND time_now > ".$time;
	}
	else{
		$sql = "SELECT time_now, qrcode FROM scouting_qrcode WHERE type='PIT'";
	}
}
else if ($type == "COMMENT"){
	if(isset($time)){
		$sql = "SELECT time_now, qrcode FROM scouting_qrcode WHERE type='COMMENT' AND time_now > ".$time;
	}
	else{
		$sql = "SELECT time_now, qrcode FROM scouting_qrcode WHERE type='COMMENT'";
	}
}
else{
	if(isset($time)){
		$sql = "SELECT time_now, qrcode FROM scouting_qrcode WHERE type='MATCH' AND time_now > ".$time;
	}
	else{
		$sql = "SELECT time_now, qrcode FROM scouting_qrcode WHERE type='MATCH'";
	}
}



//$sql = "SELECT time_now, qrcode FROM scouting_qrcode WHERE type='MATCH'";
$result = mysqli_query($conn, $sql);

if (mysqli_num_rows($result) > 0) {
    while($row = mysqli_fetch_assoc($result)) {
        echo $row["time_now"]. ";" . $row["qrcode"]."\n";
    }
} else {

}

mysqli_close($conn);
?>
