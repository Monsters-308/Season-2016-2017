#pragma config(Sensor, in1,    pot,            sensorPotentiometer)
#pragma config(Motor,  port2,           motorLeft1,    tmotorVex393_MC29, openLoop)
#pragma config(Motor,  port3,           motorLeft2,    tmotorVex393_MC29, openLoop)
#pragma config(Motor,  port4,           motorRigth1,   tmotorVex393_MC29, openLoop, reversed)
#pragma config(Motor,  port5,           motorRigth2,   tmotorVex393_MC29, openLoop, reversed)
#pragma config(Motor,  port6,           motorTurn,     tmotorVex393_MC29, openLoop)
//*!!Code automatically generated by 'ROBOTC' configuration wizard               !!*//

//Author: Alexander Kaschta

//Robot C is executing the task main only once by itself. Write your code inside of it to make it execute
task main()
{
	//This while-loop is helping to loop thru a piece of code
	//The loop is looking for a condition, that is equals to 'true', alternative you also can use '1 == 1'
	while(true){

		//We are now in the loop , where the code get's repeated
		
		//Information:
		//The potentiometer is returning a value between 3750 and 0
		//We are trying to be able to move the axis, but avoid it to hit the chasis
		
		
		//Check if the value of the potentiometer is bigger than 3500
		if(SensorValue[pot] > 3500){
			//If this is the case, we want to stop it from moving into the direction, where the chasis is
			
			//If the user is still pulling on the joystick to move the wheels into this direction or he put it to rest,
			//we want to make sure, that we stop the motor from moving
			if(vexRT[Ch1] < 15){
				//This is gives the turn motor the signal, to set it's power to 0
				motor[motorTurn] = 0;
			}
			//If this is not the case, we want to turn the wheels with 1/3 power
			else{
				//We are setting now the power of the turn motor to 1/3 of the user input
				motor[motorTurn] = vexRT[Ch1] / 3;
			}

		}
		//We also want to make that check for the other direction.
		//So we are now checking, if the value of the potentiometer is smaller than 2300
		else if(SensorValue[pot] < 2300){
			//As above we want to stop if from hitting, so we check the state of the joystick
			if(vexRT[Ch1] >  -15){
				//If we need to stop the motor, we set his power to 0
				motor[motorTurn] = 0;
			}else{
				//Else we let the user turn with 1/3 of the power from his input
				motor[motorTurn] = vexRT[Ch1] / 3;
			}
		}
		//If the user is not hitting the chasis, we still want to let the user controll the turning
		else{
			//As above, this is 1/3 of the user input 
			motor[motorTurn] = vexRT[Ch1] / 3;
		}
		
		//To controll driving forward and backward, we use another joystick
		//We always set the motor value to the value of the joystick (So these are able to reach 100% power, while the onces above only could 33%)
		//The values are negative, because we need this in this case. It always depends on the robot, if you need to inverse it or not
		
		motor[motorLeft1] = -vexRT[Ch3];
		motor[motorLeft1] = -vexRT[Ch3];
		motor[motorRigth1] = -vexRT[Ch3];
		motor[motorRigth2] = -vexRT[Ch3];
	}
}