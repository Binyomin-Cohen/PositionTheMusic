package com.javaguy.seanc.postionthemusic;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mOrientation;
    private Sensor mAcceleration;
    TextView tv;
    TextView tv2;
    MediaPlayer mediaPlayer;
    Context context = this;

    int currentResourceId;
    Integer[] soundFiles;
    List<Integer> soundFilesList;
    List<Double> maxZvalsList;
    ImageView imageView;
    ObjectAnimator animator;

    private float accelerometerZVal = 0;
    MediaPlayer[] mediaPlayers ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentResourceId = R.raw.snare;
        imageView = (ImageView)findViewById(R.id.noteCircle);
        tv = (TextView)findViewById(R.id.tv);
        tv2 = (TextView)findViewById(R.id.tv2);
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        mediaPlayers = new MediaPlayer[10];

        Log.d("isLinearAccelartionNull", ((Boolean)(mAcceleration == null)).toString());

        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        Log.d("deviceSensors", deviceSensors.toString());

      //  soundFiles = new Integer[]{R.raw.voice012,R.raw.voice011,R.raw.voice010, R.raw.voice009, R.raw.voice008,
       //         R.raw.voice007, R.raw.voice006,R.raw.voice005,R.raw.voice004, R.raw.voice003, R.raw.voice002, R.raw.voice001};

        soundFiles = new Integer[]{R.raw.bassdrum, R.raw.floortom, R.raw.snare, R.raw.ridecym};
        soundFilesList = new ArrayList<Integer>(Arrays.asList(soundFiles));
        int numOfSounds = soundFiles.length;
        Log.d("numOfSounds", ((Integer)numOfSounds).toString());
        double rangeOfNote = 2.0 / numOfSounds;
        //double rangeOfNote = 0.25;
        Log.d("rangeOfNote", ((Double)rangeOfNote).toString());
        maxZvalsList = new ArrayList<Double>();
        double maxZVal = -0.99 + rangeOfNote;
        for(int i = 0; i < numOfSounds; i ++ ){
            maxZvalsList.add(maxZVal);
            maxZVal += rangeOfNote;
        }



        Log.d("ZValList", maxZvalsList.toString());



    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){
            tv.setText(" rotation value:   " + (event.values[2] + 1) * 10 );
            double zval = event.values[2];


            float howMuch = 180 * ( (float)zval + 1 );
            animator = ObjectAnimator.ofFloat(imageView, "rotation", howMuch);
            animator.setDuration(10);
            animator.start();


            ;
            for(int i = 0; i < maxZvalsList.size(); i++){
                if(zval < maxZvalsList.get(i)){{
                    setCurrentResourceId(soundFilesList.get(i));
                    break;
                }

                }
            }
        }
        else {
            float previousAccelerometerZ = getAccelerometerZVal();
            setAccelerometerZVal(event.values[2]);
            if(previousAccelerometerZ > -3 && getAccelerometerZVal() < -3){
                for(int i = 0; i < mediaPlayers.length; ++i){
                    if(mediaPlayers[i] == null){
                        mediaPlayer = MediaPlayer.create(context, getCurrentResourceId());
                        mediaPlayer.start();
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mp.reset();
                                mp.release();
                                mp = null;
                            }
                        });
                        break;
                    }
                }

            }



            tv2.setText("accelerometer value  " + event.values[2]);
        }



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onResume(){
        super.onResume();
        mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mAcceleration, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public int getCurrentResourceId() {
        return currentResourceId;
    }

    public void setCurrentResourceId(int currentResourceId) {
        this.currentResourceId = currentResourceId;
    }

    public float getAccelerometerZVal() {
        return accelerometerZVal;
    }

    public void setAccelerometerZVal(float accelerometerZVal) {
        this.accelerometerZVal = accelerometerZVal;
    }
}
