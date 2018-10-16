package com.victorpreston.morninggloryalarm.Items;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.victorpreston.morninggloryalarm.Database.AlarmDatabase;
import com.victorpreston.morninggloryalarm.R;

import java.sql.Time;

import static com.victorpreston.morninggloryalarm.R.id.editName;

/**
 * Created by victorpreston on 10/9/2017.
 */

public class AlarmGenerator extends AppCompatActivity {

    private Time timeSetting;
    private String name;
    private Alarm mAlarm = null;
    private static final String TAG = "Alarm Generator";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_generator);
        int alarmPosition = 0;
        if (null != savedInstanceState) {
            mAlarm = savedInstanceState.getParcelable(Alarm.ALARM_KEY);
        } else {
            mAlarm = Alarm.CREATOR.newArray(1)[0];
        }

        TextView nameView = (TextView) findViewById(R.layout.alarm_generator / editName);
        if (nameView == null) {
            Log.d(TAG, "no name view!");
        }
        AlarmGeneratorFragment newAlarmFragment = new AlarmGeneratorFragment();
        newAlarmFragment.setArguments(savedInstanceState);
    }
    Alarm makeAlarm(String nm){
        Alarm generatee = new Alarm(getApplicationContext());

        generatee.setAlarmName(nm);

        return generatee;
    }
    Alarm getNewAlarm(){
        return mAlarm;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putParcelable(Alarm.ALARM_KEY,mAlarm);
    }
}
