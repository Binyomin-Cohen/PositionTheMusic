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
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mOrientation;
    private Sensor mAcceleration;
    private Sensor mProximitySensor;

    TextView tv;

    MediaPlayer mediaPlayer;
    Context context = this;

    int currentResourceId;
    Integer[] soundFiles;
    List<Integer> soundFilesList;
    List<Double> maxZvalsList;
    ImageView imageView;
    ObjectAnimator animator;

    private float accelerometerZVal = 0;


    private float proximityVal = 0;

    MediaPlayer[] mediaPlayers ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentResourceId = R.raw.floortom;
        imageView = (ImageView)findViewById(R.id.drumset);
        tv = (TextView)findViewById(R.id.tv);


        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
       // mProximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        mediaPlayers = new MediaPlayer[10];

        Log.d("isLinearAccelartionNull", ((Boolean)(mAcceleration == null)).toString());

        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        Log.d("deviceSensors", deviceSensors.toString());

      //  soundFiles = new Integer[]{R.raw.voice012,R.raw.voice011,R.raw.voice010, R.raw.voice009, R.raw.voice008,
       //         R.raw.voice007, R.raw.voice006,R.raw.voice005,R.raw.voice004, R.raw.voice003, R.raw.voice002, R.raw.voice001};

        soundFiles = new Integer[]{ R.raw.floortom,  R.raw.ridecymbeg, R.raw.snarebeg, R.raw.bassdrumbeg};
        soundFilesList = new ArrayList<Integer>(Arrays.asList(soundFiles));
        int numOfSounds = soundFiles.length;
        Log.d("numOfSounds", ((Integer)numOfSounds).toString());

        //the rotation vector values have a range of 2, ie from -1 to 1
        //double rangeOfNote = 2.0 / numOfSounds;  //this would divide the whole range evenly
        double rangeOfNote = 1.0 / numOfSounds; // the divides half the rotation range, a more compact instrument set
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
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

  /*      if(event.sensor.getType() == Sensor.TYPE_PROXIMITY && getCurrentResourceId() > 0) {
            tv2.setText(" " + event.values[0] + " , " +  + event.values[1] + " , "  + event.values[2] + "  " );
            if(event.values[0] < 1 && getProximityVal() >=1) {

                for (int i = 0; i < mediaPlayers.length; ++i) {
                    if (mediaPlayers[i] == null) {
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
            setProximityVal(event.values[0]);
        }*/
        //else
        if(event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){

            double zval = event.values[2];

            tv.setText(" " + 180 * (1 + zval)  );


            float howMuch = 180 * ( (float)zval + 1 ) - 90;
            animator = ObjectAnimator.ofFloat(imageView, "rotation", howMuch);
            animator.setDuration(10);
            animator.start();


            ;
            boolean withinRangeOfOfZvals = false;
            for(int i = 0; i < maxZvalsList.size(); i++){
                if(zval < maxZvalsList.get(i)){{
                    setCurrentResourceId(soundFilesList.get(i));
                    withinRangeOfOfZvals = true;
                    break;
                }

                }
            }
            if(!withinRangeOfOfZvals){
                setCurrentResourceId(0);
            }
        }
        else {
            float previousAccelerometerZ = getAccelerometerZVal();
            setAccelerometerZVal(event.values[2]);
            if( currentResourceId > 0 &&  previousAccelerometerZ > -7 && getAccelerometerZVal() < -7){
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



           // tv2.setText("accelerometer value  " + event.values[2]);
        }



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onResume(){
        super.onResume();
        mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mAcceleration, SensorManager.SENSOR_DELAY_FASTEST);
      //  mSensorManager.registerListener(this, mProximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
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

    public float getProximityVal() {
        return proximityVal;
    }

    public void setProximityVal(float proximityVal) {
        this.proximityVal = proximityVal;
    }
}
