<?php
/*
 * This file provides a web service to authenticate a user. Given "email" and "pwd",
 *  attempts to authenticate the user. If successful, returns the user ID. Else, returns
 *  an error message.
 *
 */

ini_set('display_errors', '1');
error_reporting(E_ALL);
// Connect to the Database
$dsn = 'mysql:host=cssgate.insttech.washington.edu;dbname=jonahkh';
$username = 'jonahkh';
$password = 'CaknyWek';
$command = $_GET['cmd'];

try {
    $db = new PDO($dsn, $username, $password);
    switch ($command) {
        case "login":
            //get input
            $email = isset($_GET['email']) ? $_GET['email'] : '';
            $pwd = isset($_GET['pwd']) ? $_GET['pwd'] : '';
            $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

            //validate input
            if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
                echo '{"result": "fail", "error": "Please enter a valid email."}';
            } else {
                //build query
                $sql = "SELECT email, password FROM Credentials ";
                $sql .= " WHERE email = '" . $email . "'";


                $q = $db->prepare($sql);
                $q->execute();
                $result = $q->fetch(PDO::FETCH_ASSOC);


                //check results
                if ($result != false) {
                    //on success, return the user id
                    if (strcmp($pwd, $result['password']) == 0)
                        echo '{"result": "success", "email": "' . $result['email'] . '"}';
                    else
                        echo '{"result": "fail", "error": "Incorrect password."}';
                } else {
                    echo '{"result": "fail", "error": "Incorrect email."}';
                }
            }
            break;
        case "register":
            $first = isset($_GET['first']) ? $_GET['first'] : '';
            $last = isset($_GET['last']) ? $_GET['last'] : '';
            $email = isset($_GET['email']) ? $_GET['email'] : '';
            $weight = isset($_GET['weight']) ? $_GET['weight'] : '';
            $gender = isset($_GET['gender']) ? $_GET['gender'] : '';
            $days = isset($_GET['days']) ? $_GET['days'] : '';

            if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
                echo '{"result": "fail", "error": "Please enter a valid email."}';
            }

            //$sql = "INSERT INTO USERS VALUES ('" . $email . "', '" . $first . "', '" . $last. "'", '" . $weight. "'", '" . $gender. "'", '" . $days. "'")";
            $sql = "INSERT INTO Users VALUES ('$email', '$first', '$last', '$weight', '$gender', '$days')";
            //	$q = $db->prepare($sql);
            //	$q->execute();
            //$result = $q->fetch(PDO::FETCH_ASSOC);
            //attempts to add record
            if ($db->query($sql)) {
                echo '{"result": "success"}';
                $db = null;
            } else {
                echo '{"result": "failure"}';
            }
            break;
        case "viewworkouts":
            $day = isset($_GET['day']) ? $_GET['day'] : '';

            $type = "SELECT WorkoutType FROM DailyWorkout WHERE DayOfWeek='$day'";
            $q = $db->prepare($type);
            $q->execute();
            $result = $q->fetch(PDO::FETCH_ASSOC);

            $new_type = $result['WorkoutType'];
            if (strcmp($result['WorkoutType'],'weight') == 0) {
                $new_type = 'WeightWorkout';
                $sql = "SELECT * FROM WeightWorkout WHERE WorkoutName = (SELECT WorkoutName FROM DailyWorkout WHERE DayOfWeek='$day')";
            } else {
                $new_type = 'CardioWorkout';
                $sql = "SELECT * FROM CardioWorkout WHERE WorkoutName = (SELECT WorkoutName FROM DailyWorkout WHERE DayOfWeek='$day')";
            }
            //echo $result;
            //$sql = "SELECT * FROM '$new_type' WHERE WorkoutName = (SELECT WorkoutName FROM DailyWorkout WHERE DayOfWeek='$day')";

            $q = $db->prepare($sql);
            $q->execute();
            $result = $q->fetchAll(PDO::FETCH_ASSOC);
            echo json_encode($result);
            break;
        case "logworkout":
            $name = isset($_GET['name']) ? $_GET['name'] : '';
            $email = isset($_GET['email']) ? $_GET['email'] : '';
            $num = isset($_GET['num']) ? $_GET['num'] : '';
            $sql = "INSERT INTO LoggedWorkout VALUES ('$num', '$email', '$name')";
            if ($db->query($sql)) {
                echo '{"result": "success"}';
                $db = null;
            } else {
                echo '{"result": "failure"}';
            }
            break;
        	case "addset":
                $email = isset($_GET['email']) ? $_GET['email'] : '';
                $name = isset($_GET['name']) ? $_GET['name'] : '';
                $weight = isset($_GET['weight']) ? $_GET['weight'] : '';
                $reps = isset($_GET['reps']) ? $_GET['reps'] : '';
                $num = isset($_GET['num']) ? $_GET['num'] : '';
                $set = isset($_GET['set']) ? $_GET['set'] : '';

                $sql = "INSERT INTO Exercise (Email, WorkoutNumber, Weight, Repetitions, ExerciseName) VALUES ('$set', ''$email', '$num', '$weight', '$reps', '$name')";
                if ($db->query($sql)) {
                    echo '{"result": "success"}';
                    $db = null;
                } else {
                    echo '{"result": "failure"}';
                }
                break;

            case "addexercise":
                $workout_name = isset($_GET['workout']) ? $_GET['workout'] : '';
                $exercise_name = isset($_GET['exercise']) ? $_GET['exercise'] : '';
                $sql = "INSERT INTO WeightWorkout (Email, WorkoutNumber, Weight, Repetitions, ExerciseName) VALUES ('$workout', '$exercise')";
                if ($db->query($sql)) {
                    echo '{"result": "success"}';
                    $db = null;
                } else {
                    echo '{"result": "failure"}';
                }
                break;

        case "addworkout":
            $day = isset($_GET['day']) ? $_GET['day'] : '';
            $name = isset($_GET['name']) ? $_GET['name'] : '';
            $type = isset($_GET['type']) ? $_GET['type'] : '';
            $table = "";
            if (strcmp($type, 'cardio') == 0) {
                $table = 'CardioWorkout';
            } else {
                $table = 'WeightWorkout';
            }
            $remove = "DELETE FROM '$table' WHERE WorkoutName = '$name'";
            $sql = "UPDATE DailyWorkout SET WorkoutName='$name', Type='$type' WHERE DayOfWeek='$day'";
            $message = "";
            if ($db->query($remove)) {
                $message = 'success';
            } else {
                $message = 'fail';
            }
            if ($db->query($sql)  && strcmp($message, 'success') == 0) {
                echo '{"result": "success"}';
                $db = null;
            } else {
                echo '{"result": "failure"}';
            }
            break;
        case "getworkoutnumber":
            $email = isset($_GET['email']) ? $_GET['email'] : '';
            $sql = "SELECT COUNT(WorkoutNumber) AS count FROM LoggedWorkout WHERE Email='$email'";
            $q = $db->prepare($sql);
            $q->execute();
            $result = $q->fetchAll(PDO::FETCH_ASSOC);
            echo json_encode($result);
            break;
        case "cardiosession": 
    	break;    
    
    }

}
catch (PDOException $e) {
    echo 'Error Number: ' . $e->getCode() . '<br>';
}


?>