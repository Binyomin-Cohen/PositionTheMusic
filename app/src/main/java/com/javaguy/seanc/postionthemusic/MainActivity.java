package com.javaguy.seanc.postionthemusic;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mOrientation;
    TextView tv;
    MediaPlayer mediaPlayer;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView)findViewById(R.id.tv);
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String theEvent = "x: " + event.values[0] + " y: " + event.values[1] + " z: " + event.values[2];
        tv.setText(theEvent);
        double zval = event.values[2];
        if(zval > 0.1 && zval < 0.9){
            if(zval < 0.4){
                mediaPlayer = MediaPlayer.create(context, R.raw.voice002);
            }
            else if(zval < 0.6){
                mediaPlayer = MediaPlayer.create(context, R.raw.voice003);
            }
            else if(zval < 0.9){
                mediaPlayer = MediaPlayer.create(context, R.raw.voice004);
            }

            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    mp.release();
                    mp = null;
                }
            });
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onResume(){
        super.onResume();
        mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}
