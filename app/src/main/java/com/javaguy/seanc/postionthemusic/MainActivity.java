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
    TextView tv2;
    MediaPlayer mediaPlayer;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView)findViewById(R.id.tv);
        tv2 = (TextView)findViewById(R.id.tv2);
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);


    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        tv.setText(" z:   " + event.values[2]);
        tv2.setText("x: " + event.values[0] + " y: " + event.values[1] );
        double zval = event.values[2];
        if(zval > 0.1 && zval < 0.9){
            if(zval < 0.15){
                mediaPlayer = MediaPlayer.create(context, R.raw.voice001);
            }
            else if(zval < 0.30){
                mediaPlayer = MediaPlayer.create(context, R.raw.voice002);
            }
            else if(zval < 0.45){
                mediaPlayer = MediaPlayer.create(context, R.raw.voice003);
            }
            else if(zval < 0.60){
                mediaPlayer = MediaPlayer.create(context, R.raw.voice004);
            }
            else if(zval < 0.80){
                mediaPlayer = MediaPlayer.create(context, R.raw.voice005);
            }
            else if(zval < 0.999){
                mediaPlayer = MediaPlayer.create(context, R.raw.voice006);
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
