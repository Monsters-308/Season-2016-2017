<?php
 
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['type']) && isset($_POST['qrcode'])) {
 
    $type = $_POST['type'];
    $currentTime = time();
    $code = $_POST['qrcode'];
 
    $conn = new mysqli("", "", "", "");

    if(!$conn){
        
        $response["success"] = 0;
        $response["message"] = "Oops! An connection error occurred.";
 
        // echoing JSON response
        echo json_encode($response);
        die();
    }

    $result = $conn->query("INSERT INTO scouting_qrcode (id, time_now, type, qrcode) VALUES (NULL, '$currentTime', '$type', '$code')");
 
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Successfully created.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
 
        // echoing JSON response
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>