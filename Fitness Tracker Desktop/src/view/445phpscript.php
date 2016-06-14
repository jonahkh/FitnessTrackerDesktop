<?php
/*
 * This file is to fetch and store data to the mysql database using a webservice.
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
        case "login":	// verify login
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
        case "adminlogin":	// administrator login
            $email = isset($_GET['email']) ? $_GET['email'] : '';
            $pwd = isset($_GET['pwd']) ? $_GET['pwd'] : '';
            $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

            //validate input
            if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
                echo '{"result": "fail", "error": "Please enter a valid email."}';
            } else {
                //build query
                $sql = "SELECT Email from GymAdministrator WHERE Email='$email'";
                $q = $db->prepare($sql);
                $q->execute();
                $result = $q->fetch(PDO::FETCH_ASSOC);
                if ($result) {
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
	                }
                } else {
                    echo '{"result": "fail", "error": "Not admin email."}';
                }
            }
        	break;
        case "register":	// register a user
            $first = isset($_GET['first']) ? $_GET['first'] : '';
            $last = isset($_GET['last']) ? $_GET['last'] : '';
            $email = isset($_GET['email']) ? $_GET['email'] : '';
            $weight = isset($_GET['weight']) ? $_GET['weight'] : '';
            $gender = isset($_GET['gender']) ? $_GET['gender'] : '';
            $days = isset($_GET['days']) ? $_GET['days'] : '';
			$first = str_replace('_', ' ', $first);
			$last = str_replace('_', ' ', $last);
			$pwd = isset($_GET['pwd']) ? $_GET['pwd'] : '';
            if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
                echo '{"result": "fail", "error": "Please enter a valid email."}';
            }
            $sql = "INSERT INTO Users VALUES ('$email', '$first', '$last', '$weight', '$gender', '$days')";
            $credentials = "INSERT INTO Credential VALUES ('$email', '$pwd')";
            //attempts to add record
            if ($db->query($sql) && $db->query($credentials)) {
                echo '{"result": "success"}';
                $db = null;
            } else {
                echo '{"result": "failure"}';
            }
            break;
        case "viewworkouts":	// view all of the workouts
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
            $q = $db->prepare($sql);
            $q->execute();
            $result = $q->fetchAll(PDO::FETCH_ASSOC);
            echo json_encode($result);
            break;
        case "logworkout":	// log a workout
            $name = isset($_GET['name']) ? $_GET['name'] : '';
            $name = str_replace('_', ' ', $name);
            $email = isset($_GET['email']) ? $_GET['email'] : '';
            $num = isset($_GET['num']) ? $_GET['num'] : '';
           	$name = str_replace('_', ' ', $name);
            $sql = "INSERT INTO LoggedWorkout VALUES ('$num', '$email', '$name')";
           
            if ($db->query($sql)) {
                echo '{"result": "success"}';
                $db = null;
            } else {
                echo '{"result": "failure"}';
            }
            break;
    	case "addset":	// add a set
            $email = isset($_GET['email']) ? $_GET['email'] : '';
            $name = isset($_GET['name']) ? $_GET['name'] : '';
            $weight = isset($_GET['weight']) ? $_GET['weight'] : '';
            $reps = isset($_GET['reps']) ? $_GET['reps'] : '';
            $num = isset($_GET['num']) ? $_GET['num'] : '';
            $set = isset($_GET['set']) ? $_GET['set'] : '';
           	$name = str_replace('_', ' ', $name);
            $sql = "INSERT INTO Exercise VALUES ('$set', '$email', '$num', '$weight', '$reps', '$name')";
            if ($db->query($sql)) {
                echo '{"result": "success"}';
                $db = null;
            } else {
                echo '{"result": "failure"}';
            }
            break;

        case "addexercise":	// add an exercise
            $workout_name = isset($_GET['workout']) ? $_GET['workout'] : '';
            $workout_name = str_replace('_', ' ', $workout_name);
            $exercise_name = isset($_GET['exercise']) ? $_GET['exercise'] : '';
            $sql = "INSERT INTO WeightWorkout (Email, WorkoutNumber, Weight, Repetitions, ExerciseName) VALUES ('$workout', '$exercise')";
            if ($db->query($sql)) {
                echo '{"result": "success"}';
                $db = null;
            } else {
                echo '{"result": "failure"}';
            }
            break;

        case "addworkout":	// Called when the admin adds a workout
            $day = isset($_GET['day']) ? $_GET['day'] : '';
            $name = isset($_GET['name']) ? $_GET['name'] : '';
            $type = isset($_GET['type']) ? $_GET['type'] : '';
            $table = "";
            $sql = "SELECT WorkoutName, WorkoutType FROM DailyWorkout WHERE DayOfWeek='$day'";
            $deleteName = "";
            $q = $db->prepare($sql);
            $q->execute();
            $result = $q->fetchAll(PDO::FETCH_ASSOC);
            // Get the previous workout for the day being modified
        	if ($result) {
        		$table = $result['0']['WorkoutType'];
        		if (strcmp($table, 'cardio') == 0) {
        			$table = 'CardioWorkout';
        		} else {
        			$table = 'WeightWorkout';
        		}
        		$deleteName = $result['0']['WorkoutName'];
    		} else {
       			echo '{"result": "failure"}';
    		}
            if (strcmp($type, 'cardio') == 0) {	// new workout is cardio workout
            	$desc = isset($_GET['desc']) ? $_GET['desc'] : '';
            	$sql = "INSERT INTO CardioWorkout VALUES ('$name', '$desc')";
				if (!$db->query($sql)) {
   					echo '{"result": "failure"}';
    			}
            
            } else {	// new workout isw weight workout
                if (!$db->query($sql)) {
           			echo '{"result": "failure"}';
        		}
        		$ex1 = isset($_GET['ex1']) ? $_GET['ex1'] : '';
        		$ex2 = isset($_GET['ex2']) ? $_GET['ex2'] : '';
        		$ex3 = isset($_GET['ex3']) ? $_GET['ex3'] : '';
        		$ex4 = isset($_GET['ex4']) ? $_GET['ex4'] : '';
        		$array = array($ex1, $ex2, $ex3, $ex4);
        		foreach ($array as $value) { // add all exercises
    				$sql = "INSERT INTO WeightWorkout VALUES('$name', '$value')";
    				if (!$db->query($sql)) {
           				echo '{"result": "failure"}';
        			}
				}
            }
            // Delete the old workout and update the Daily Workout
            $remove = "DELETE FROM $table WHERE WorkoutName = '$deleteName'";
            $sql = "UPDATE DailyWorkout SET WorkoutName='$name', WorkoutType='$type' WHERE DayOfWeek='$day'";
            $message = "";
            if ($db->query($remove)) {
            	$message  = 'success';
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
        case "getworkoutnumber":	// fetch current workout number
            $email = isset($_GET['email']) ? $_GET['email'] : '';
            $sql = "SELECT COUNT(WorkoutNumber) AS count FROM LoggedWorkout WHERE Email='$email'";
            $q = $db->prepare($sql);
            $q->execute();
            $result = $q->fetchAll(PDO::FETCH_ASSOC);
            echo json_encode($result);
            break;
        case "cardiosession": 	// log a cardio session
        	$email = isset($_GET['email']) ? $_GET['email'] : '';
            $num = isset($_GET['num']) ? $_GET['num'] : '';
            $dur = isset($_GET['dur']) ? $_GET['dur'] : '';
            $int= isset($_GET['int']) ? $_GET['int'] : '';
            $name = isset($_GET['name']) ? $_GET['name'] : '';
            $sql = "INSERT INTO CardioSession VALUES ('$email', '$name', '$num', '$dur', '$int')";
            $sql2 = "INSERT INTO LoggedWorkout VALUES ('$num', '$email', '$name')";
			if ($db->query($sql2)) {
				$db->query($sql); 
                echo '{"result": "success"}';
                $db = null;
                } else {
                    echo '{"result": "failure"}';
                }
    		break;    
		case "viewsupplements":	// fetch a list of all supplements including their average rating
			$sql = "SELECT *, SUM(Protein * 4 + Fat * 9 + Carbs * 4) AS Calories FROM Supplement LEFT JOIN " 
					. "(SELECT SupplementName AS DEL, AVG(Rating) AS Rating FROM SupplementRating GROUP BY SupplementName) "
					. "AS newSup ON Supplement.SupplementName = newSup.DEL GROUP BY Supplement.SupplementName";
			$q = $db->prepare($sql);
            $q->execute();
            $result = $q->fetchAll(PDO::FETCH_ASSOC);
            echo json_encode($result);
			break;
		case "ratesupplements":	// rate a supplement
        	$email = isset($_GET['email']) ? $_GET['email'] : '';
        	$rate= isset($_GET['rate']) ? $_GET['rate'] : '';
        	$name = isset($_GET['name']) ? $_GET['name'] : '';
        	$name = str_replace('_', ' ', $name);
			$sql = "INSERT INTO SupplementRating VALUES ('$name', '$email', '$rate')";
			if ($db->query($sql)) {
                echo '{"result": "success"}';
                $db = null;
            } else {
                echo '{"result": "failure"}';
            }
            break;
        case "rateworkout":	// rate a workout
        	$email = isset($_GET['email']) ? $_GET['email'] : '';
        	$rate= isset($_GET['rate']) ? $_GET['rate'] : '';
        	$name = isset($_GET['name']) ? $_GET['name'] : '';
        	$name = str_replace('_', ' ', $name);
			$sql = "INSERT INTO WorkoutRating VALUES ('$name', '$email', '$rate')";
			if ($db->query($sql)) {
                echo '{"result": "success"}';
                $db = null;
            } else {
                echo '{"result": "failure"}';
            }
            break;
        case "viewloggedworkout":	// fetch all of the logged workouts
        	$email = isset($_GET['email']) ? $_GET['email'] : '';
        	$sql = "SELECT * from LoggedWorkout WHERE Email = '$email'";
        	$q = $db->prepare($sql);
            $q->execute();
            $result = $q->fetchAll(PDO::FETCH_ASSOC);
            echo json_encode($result);
            break;
        case "getallexercises": 	// fetch all exercises for a particular workout number
        	$email = isset($_GET['email']) ? $_GET['email'] : '';
            $num = isset($_GET['num']) ? $_GET['num'] : '';
            $sql = "SELECT * FROM CardioSession WHERE Email = '$email' AND WorkoutNumber = '$num'";
            $q = $db->prepare($sql);
            $q->execute();
            $result = $q->fetchAll(PDO::FETCH_ASSOC);
            if ($result) {
            	echo json_encode($result);
            } else {
	         	$sql = "SELECT * FROM Exercise WHERE Email = '$email' AND WorkoutNumber = '$num'";
	            $q = $db->prepare($sql);
	            $q->execute();
	            $result = $q->fetchAll(PDO::FETCH_ASSOC);
	            echo json_encode($result);
            }
            break;
        case "getsupplementnames": 
        	$sql = "SELECT SupplementName FROM Supplement";
        	$q = $db->prepare($sql);
            $q->execute();
            $result = $q->fetchAll(PDO::FETCH_ASSOC);
            if ($result) {
            	echo json_encode($result);
            }
        	break;
    	case "deletesupplement": 
    		$supp = isset($_GET['supp']) ? $_GET['supp'] : '';
    		$sql = "DELETE FORM Supplement WHERE SupplementName = '$supp'";
    		$sql2 = "DELETE FROM SupplementRating WHERE SupplementName = '$supp'";
    		if ($db->query($sql) && $db->query($sql2)) {
                echo '{"result": "success"}';
                $db = null;
            } else {
                echo '{"result": "failure"}';
            }
            break;
        case "addsupplement":
        	$supp = isset($_GET['supp']) ? $_GET['supp'] : '';
        	$fat = isset($_GET['fat']) ? $_GET['fat'] : '';
        	$carbs = isset($_GET['carbs']) ? $_GET['carbs'] : '';
        	$pro = isset($_GET['pro']) ? $_GET['pro'] : '';
        	$price = isset($_GET['price']) ? $_GET['price'] : '';
        	
        	$sql = "INSERT INTO Supplement VALUES ('$supp', '$price', '$fat', '$carbs', '$pro')";
        	if ($db->query($sql)) {
                echo '{"result": "success"}';
                $db = null;
            } else {
                echo '{"result": "failure"}';
            }
    }

}
catch (PDOException $e) {
    echo 'Error Number: ' . $e->getCode() . '<br>';
}
?>