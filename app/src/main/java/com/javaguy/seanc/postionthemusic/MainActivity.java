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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mOrientation;
    TextView tv;
    TextView tv2;
    MediaPlayer mediaPlayer;
    Context context = this;
    int currentResourceId;
    Integer[] soundFiles;
    List<Integer> soundFilesList;
    Double[] maxZvals;
    List<Double> maxZvalsList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentResourceId = R.raw.voice001;
        tv = (TextView)findViewById(R.id.tv);
        tv2 = (TextView)findViewById(R.id.tv2);
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        soundFiles = new Integer[]{R.raw.voice001,R.raw.voice002,R.raw.voice003, R.raw.voice004, R.raw.voice005, R.raw.voice006 };
        soundFilesList = new ArrayList<Integer>(Arrays.asList(soundFiles));
        maxZvals = new Double[]{0.2, 0.4, 0.6, 0.75, 0.85, 0.99};
        maxZvalsList = new ArrayList<Double>(Arrays.asList(maxZvals));

        mediaPlayer = MediaPlayer.create(context, soundFilesList.get(0));


    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        tv.setText(" z:   " + event.values[2]);
        tv2.setText("x: " + event.values[0] + " y: " + event.values[1] );
        double zval = event.values[2];

;
        for(int i = 0; i < maxZvalsList.size(); i++){
            if(zval < maxZvalsList.get(i)){{
                if(currentResourceId != soundFilesList.get(i)) {
                    currentResourceId = soundFilesList.get(i);
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mediaPlayer = MediaPlayer.create(context, soundFilesList.get(i));
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
                break;
            }

            }
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
