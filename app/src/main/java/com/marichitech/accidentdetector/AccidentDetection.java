package com.marichitech.accidentdetector;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class AccidentDetection extends AppCompatActivity implements Animation.AnimationListener,SensorEventListener {
    ImageView logo;
    Animation animation;

    private SensorManager mSensorManager;
    private Sensor mSensorAccelerometer;
    private float[] mAccelerometerData = new float[3];
    private float[] mAccelerometerData_last = new float[3];
    private boolean firstChange = true;
    private String phoneNumber ;
    private int warningBoundary = 500;
    private double changeAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_detection);
        logo = (ImageView) findViewById(R.id.imageView);
        animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate);
        animation.setAnimationListener(AccidentDetection.this);
        logo.setVisibility(View.VISIBLE);
        logo.startAnimation(animation);

        // Get accelerometer and magnetometer sensors from the sensor manager.
        // The getDefaultSensor() method returns null if the sensor
        // is not available on the device.
        mSensorManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);
        mSensorAccelerometer = mSensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);

    }

    @Override
    public void onAnimationStart(Animation animation) {
    }
    @Override
    public void onAnimationEnd(Animation animation) {
    }
    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        int sensorType = event.sensor.getType();
        if(sensorType == Sensor.TYPE_ACCELEROMETER) {
            mAccelerometerData_last[0] = mAccelerometerData[0];
            mAccelerometerData_last[1] = mAccelerometerData[1];
            mAccelerometerData_last[2] = mAccelerometerData[2];

            mAccelerometerData[0] = event.values[0];
            mAccelerometerData[1] = event.values[1];
            mAccelerometerData[2] = event.values[2];

            changeAmount = Math.pow((mAccelerometerData[0] - mAccelerometerData_last[0]), 2) +
                    Math.pow((mAccelerometerData[1] - mAccelerometerData_last[1]), 2) +
                    Math.pow((mAccelerometerData[2] - mAccelerometerData_last[2]), 2);

            String notificationMethod;
            if (!firstChange && changeAmount >= warningBoundary) {
                /*phoneNumber = sharedPreferences.getString(phoneNumber_key, "");
                notificationMethod = sharedPreferences.getString(notificationMethod_key, "SMS");

                if (notificationMethod.equals("SMS"))
                    sendSMS(phoneNumber);
                else if (notificationMethod.equals("phone"))
                    makePhoneCall(phoneNumber);*/

                Intent intent = new Intent(this, FallDetected.class);
                startActivity(intent);

            }
            firstChange = false;
        }
    }

    @Override
    protected void onResume() {
        mSensorManager.registerListener(this, mSensorAccelerometer, mSensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    /**
     * Must be implemented to satisfy the SensorEventListener interface;
     * unused in this app.
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }


}
