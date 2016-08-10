package com.rodibot.rodroid;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AccelerometerActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {

    private Transport transport = new Transport();
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private TextView title;
    private ImageView mTopView;
    private ImageView mDownView;
    private ImageView mRightView;
    private ImageView mLeftView;
    private Button mStopView;
    private Button mStartView;

    private long lastUpdate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mTopView = (ImageView) findViewById(R.id.top);
        mDownView = (ImageView) findViewById(R.id.down);
        mLeftView = (ImageView) findViewById(R.id.left);
        mRightView = (ImageView) findViewById(R.id.right);

        mStopView = (Button) findViewById(R.id.stop);
        mStopView.setOnClickListener(this);

        mStartView= (Button) findViewById(R.id.start);
        mStartView.setOnClickListener(this);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        /**
         * The system's sensors are incredibly sensitive. When holding a device in your hand,
         * it is constantly in motion, no matter how steady your hand is
         *
         * We store the system's current time (in milliseconds) store it in curTime and check
         * whether more than 1000 milliseconds have passed since the last time onSensorChanged was invoked.
         */
        long curTime = System.currentTimeMillis();

        // 1 second = 1000milliseconds
        if ((curTime - lastUpdate) > 800) {
            lastUpdate = curTime;

            if (Math.abs(x) > Math.abs(y)) {
                if (x < -2) {
                    mRightView.setVisibility(View.VISIBLE);
                    mLeftView.setVisibility(View.INVISIBLE);
                    mTopView.setVisibility(View.INVISIBLE);
                    mDownView.setVisibility(View.INVISIBLE);
                    transport.moveRight();
                }
                if (x > 2) {
                    mRightView.setVisibility(View.INVISIBLE);
                    mLeftView.setVisibility(View.VISIBLE);
                    mTopView.setVisibility(View.INVISIBLE);
                    mDownView.setVisibility(View.INVISIBLE);
                    transport.moveLeft();
                }
            } else {
                if (y < -2) {
                    mRightView.setVisibility(View.INVISIBLE);
                    mLeftView.setVisibility(View.INVISIBLE);
                    mTopView.setVisibility(View.VISIBLE);
                    mDownView.setVisibility(View.INVISIBLE);
                    transport.moveForward();
                }
                if (y > 2) {
                    mRightView.setVisibility(View.INVISIBLE);
                    mLeftView.setVisibility(View.INVISIBLE);
                    mTopView.setVisibility(View.INVISIBLE);
                    mDownView.setVisibility(View.VISIBLE);
                    transport.moveReverse();
                }
            }
            if (x > (-2) && x < (2) && y > (-2) && y < (2)) {
                mRightView.setVisibility(View.INVISIBLE);
                mLeftView.setVisibility(View.INVISIBLE);
                mTopView.setVisibility(View.VISIBLE);
                mDownView.setVisibility(View.INVISIBLE);
                transport.restoreState();
            }
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.stop:
                mSensorManager.unregisterListener(this);
                transport.stop();
                break;
            case R.id.start:
                mSensorManager.registerListener(this, mAccelerometer,
                        SensorManager.SENSOR_DELAY_NORMAL);
                break;
        }

    }
}
