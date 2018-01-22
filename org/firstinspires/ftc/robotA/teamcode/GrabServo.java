
package org.firstinspires.ftc.robotA.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="grab_servo_A", group="Iterative Opmode")
public class GrabServo extends OpMode {
    private ElapsedTime runtime = new ElapsedTime();
    
    //---Equipment (servos, motos, controllers)---//
    
    private Servo servoArmsLeft = null;
    private Servo servoArmsRight = null;
    
    private DcMotor motorWheelsRight = null;
    private DcMotor motorWheelsLeft = null;
    
    private final double servoSpeed = 0.01d;
    private double servoPos = 1.0d;//0.0 to 1.0 scale, starting servo Position (0.0 in, 1.0 out)
    
    //---METHODS---//
    
    @Override
    public void init() {
        telemetry.addData("Status", "Initializing");

        //---HARDWARE VARIABLE SETTERS---//
        servoArmsRight = hardwareMap.get(Servo.class, "servo_arms_right");
        servoArmsLeft  = hardwareMap.get(Servo.class, "servo_arms_left");
        
        motorWheelsRight = hardwareMap.get(DcMotor.class, "motor_wheels_right");
        motorWheelsLeft = hardwareMap.get(DcMotor.class, "motor_wheels_left");

        //---HARDWARE INITIATION---//
        servoArmsLeft.setDirection(Servo.Direction.REVERSE);
        servoArmsRight.setDirection(Servo.Direction.FORWARD);
        
        
        
        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized!");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {}
    @Override
    /*
     * Code to run when start is hit
     */
    public void start() {
        telemetry.addData("Status", "Starting");
        runtime.reset();
    }
    
    
    
    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        
        //TODO: We may need to make a boolean to check if the robot
        //TODO: is currently autonomous.
        //if (!autonomous) {
        
        moveMotors();
        setArmPosition();
        
        //}
        
        telemetry.addData("Controller", "LB: " + gamepad1.left_bumper + "; RB:" + gamepad1.right_bumper);
        telemetry.addData("ServoPos", "R: " + servoArmsRight.getPosition() + "; L: " + servoArmsLeft.getPosition()); 
    }
    
    private void moveMotors() {//Mapped to one stick
        //Should move foward when stick is forward, back when stick is back
        //Spot turn left when stick left, spot turn right when stick right
        double leftPow = gamepad1.right_stick_y - gamepad1.right_stick_x ;
        double rightPow = gamepad1.right_stick_y + gamepad1.right_stick_x;
        
        if(leftPow>1.0){
            leftPow = 1.0;
        }else if(leftPow<-1.0){
            leftPow = -1.0;
        }
        
        if(rightPow>1.0){
            rightPow = 1.0;
        }else if(rightPow<-1.0){
            rightPow = -1.0;
        }
        
        motorWheelsRight.setPower(rightPow);
        motorWheelsLeft.setPower(leftPow);
    }
    
    private void setArmPosition() {
        
        if (gamepad1.right_bumper) {
            servoPos += servoSpeed;
        }
        if (gamepad1.left_bumper) {
            servoPos += -servoSpeed;
        }
        servoArmsLeft.setPosition(servoPos);
        servoArmsRight.setPosition(servoPos);
        
    }
    
    /*
     * Code to run ONCE after the driver hits STOP
     */
     
    @Override
    public void stop() {
    }
}
