package org.firstinspires.ftc.robotA.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="central_servo_A", group="Iterative Opmode")
public class CentralServo extends OpMode {
    private ElapsedTime runtime = new ElapsedTime();
    
    //---Equipment (servos, motos, controllers)---//
    
    private Servo servoCenter = null;
    
    private final double servoSpeed = 0.01d;
    private double servoPos = 1.0d;//0.0 to 1.0 scale, starting servo Position (0.0 in, 1.0 out)
    
    //---METHODS---//
    
    @Override
    public void init() {
        telemetry.addData("Status", "Initializing");

        //---HARDWARE VARIABLE SETTERS---//
        servoCenter = hardwareMap.get(Servo.class, "servo_center");


        //---HARDWARE INITIATION---//
        servoCenter.setDirection(Servo.Direction.FORWARD);
        
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

        setArmPosition();
        
        
        //}
        
        // telemetry.addData("Controller", "LB: " + gamepad1.left_bumper + "; RB:" + gamepad1.right_bumper);
        telemetry.addData("ServoPos", ": " + servoCenter.getPosition()); 
    }
    
    private void setArmPosition() {
        
        if (gamepad1.right_bumper) {
            servoPos += servoSpeed;
        }
        if (gamepad1.left_bumper) {
            servoPos += -servoSpeed;
        }
        servoCenter.setPosition(servoPos);
        //servoArmsRight.setPosition(servoPos);
        
    }
    
    /*
     * Code to run ONCE after the driver hits STOP
     */
     
    @Override
    public void stop() {
    }
}
