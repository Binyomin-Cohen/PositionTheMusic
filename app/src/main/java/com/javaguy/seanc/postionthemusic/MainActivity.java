package com.javaguy.seanc.postionthemusic;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

        soundFiles = new Integer[]{R.raw.voice001,R.raw.voice002,R.raw.voice003, R.raw.voice004, R.raw.voice005,
                R.raw.voice006, R.raw.voice007,R.raw.voice008,R.raw.voice009, R.raw.voice010, R.raw.voice011, R.raw.voice012};
        soundFilesList = new ArrayList<Integer>(Arrays.asList(soundFiles));
        int numOfSounds = soundFiles.length;
        Log.d("numOfSounds", ((Integer)numOfSounds).toString());
        double rangeOfNote = 2.0 / numOfSounds;
        Log.d("rangeOfNote", ((Double)rangeOfNote).toString());
        maxZvalsList = new ArrayList<Double>();
        double maxZVal = -0.99 + rangeOfNote;
        for(int i = 0; i < numOfSounds; i ++ ){
            maxZvalsList.add(maxZVal);
            maxZVal += rangeOfNote;
        }
        Log.d("ZValList", maxZvalsList.toString());

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
                    if(mediaPlayer != null){
                        try {
                            mediaPlayer.reset();
                            mediaPlayer.release();
                        }
                        catch(IllegalStateException ise){
                            ise.printStackTrace();
                        }
                    }
                    mediaPlayer = MediaPlayer.create(context, soundFilesList.get(i));
                    mediaPlayer.seekTo(400);
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.reset();
                            mp.release();
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
