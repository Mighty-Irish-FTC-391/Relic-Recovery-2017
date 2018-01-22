// Some terrible code.

package org.firstinspires.ftc.robotA.teamcode;

import java.lang.*;
import com.qualcomm.robotcore.hardware.TouchSensorMultiplexer;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Robot A v1.0", group="Robot A")

public class aMain extends OpMode
{
    // Declare misc.
    private ElapsedTime runtime = new ElapsedTime();
    
    // Declare motor drives.
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private DcMotor armDrive = null;
    
    // Declare servos and its constants.
    private Servo servoArmsLeft = null;
    private Servo servoArmsRight = null;
    private Servo servoArmsCenter = null;
    private Servo servoLiftLeft = null;
    private Servo servoLiftRight = null;
    private CRServo servoArmsCenterLeft = null;
    private CRServo servoArmsCenterRight = null;
    private final double servoSpeed = 0.01d;
    private double servoPosSplit = 1.0d; //0.0 to 1.0 scale, starting servo Position (0.0 in, 1.0 out)
    private double servoPosLift = 1.0d;
    
    private boolean isDpadPressed = false;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftDrive  = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");
        armDrive = hardwareMap.get(DcMotor.class, "arm_drive");
        
        servoArmsRight = hardwareMap.get(Servo.class, "servo_arms_right");
        servoArmsLeft  = hardwareMap.get(Servo.class, "servo_arms_left");
        servoLiftLeft = hardwareMap.get(Servo.class, "servo_lift_left");
        servoLiftRight = hardwareMap.get(Servo.class, "servo_lift_right");
        servoArmsCenterLeft = hardwareMap.get(CRServo.class, "servo_arms_center1");
        servoArmsCenterRight = hardwareMap.get(CRServo.class, "servo_arms_center2");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);
        armDrive.setDirection(DcMotor.Direction.FORWARD);
        
        // Servo Directions
        servoArmsLeft.setDirection(Servo.Direction.REVERSE);
        servoArmsRight.setDirection(Servo.Direction.FORWARD);
        servoLiftLeft.setDirection(Servo.Direction.FORWARD);
        servoLiftRight.setDirection(Servo.Direction.REVERSE);
        

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {
        // Setup a variable for each drive wheel to save power level for telemetry
        double leftPower;
        double rightPower;
        double armPowerRaw;
        double armPower;

        //Drive Train section with a POV mode implemented
        double drive = -gamepad1.left_stick_y;
        double turn  =  gamepad1.right_stick_x;
        leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
        rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;

        //Send calculated power to wheels
        leftDrive.setPower(leftPower);
        rightDrive.setPower(rightPower);
        
        // Arm movement
        
        armPowerRaw = gamepad2.left_stick_y;
        if (armPowerRaw >= 0) {
            armDrive.setDirection(DcMotor.Direction.REVERSE);
            armPower = armPowerRaw / 4;
        } else {

                armDrive.setDirection(DcMotor.Direction.FORWARD);
                armPower = Math.abs(armPowerRaw) / 1.5;
            
        }
        armPosManagement(0.2, armPower);
        
        //Move the flipper servos.
        setArmServoPosition();
        
        if (gamepad2.dpad_up) {
            servoArmsCenterLeft.setDirection(CRServo.Direction.REVERSE);
            servoArmsCenterRight.setDirection(CRServo.Direction.FORWARD);
            servoArmsCenterLeft.setPower(0.3);
            servoArmsCenterRight.setPower(0.3);
        } else {
            if (gamepad2.dpad_down) {
                servoArmsCenterLeft.setDirection(CRServo.Direction.FORWARD);
                servoArmsCenterRight.setDirection(CRServo.Direction.REVERSE);
                servoArmsCenterLeft.setPower(0.4);
                servoArmsCenterRight.setPower(0.4);
            } else {
                servoArmsCenterLeft.setPower(0);
                servoArmsCenterRight.setPower(0);
            };
        };
        
        
        
        telemetry.addData("ArmPow", armDrive.getPower());
        telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
        
    }
    
    // Misc utility methods.
    public void setArmServoPosition() {
        if (gamepad2.right_bumper) {
            servoPosSplit += servoSpeed;
        }
        if (gamepad2.left_bumper) {
            servoPosSplit += -servoSpeed;
        }
        
        if (gamepad1.right_bumper) {
            servoPosLift += servoSpeed;
        }
        if (gamepad1.left_bumper) {
            servoPosLift += -servoSpeed;
        }

        telemetry.addData("ServoPOS", servoPosSplit);
        servoArmsLeft.setPosition(servoPosSplit);
        servoArmsRight.setPosition(servoPosSplit);
        servoLiftLeft.setPosition(servoPosLift);
        servoLiftRight.setPosition(servoPosLift);
        
    }
    
    public void armPosManagement(double stationaryPower, double inputPower) {
        if (gamepad1.dpad_up) {
            isDpadPressed = !isDpadPressed;
        };
        
        if (isDpadPressed) {
            if ((stationaryPower + inputPower) >= 1) {
                armDrive.setPower(1);
            } else {
                armDrive.setPower(stationaryPower + inputPower);
            };
        } else {
            armDrive.setPower(inputPower);
        };
        telemetry.addData("DAPD", isDpadPressed);
    }
}
