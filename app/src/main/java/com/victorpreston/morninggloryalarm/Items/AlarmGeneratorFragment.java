package com.victorpreston.morninggloryalarm.Items;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.victorpreston.morninggloryalarm.Database.AlarmCursor;
import com.victorpreston.morninggloryalarm.Database.AlarmDatabase;
import com.victorpreston.morninggloryalarm.R;
import com.victorpreston.morninggloryalarm.databinding.AlarmGeneratorBinding;

import java.net.BindException;
import java.sql.DatabaseMetaData;
//import java.util.Calendar;

//import static com.victorpreston.morninggloryalarm.Items.AlarmCalendar.TAG;

/**
 * A placeholder fragment containing a simple view.
 * This will edit an alarm if th bundle has one, otherwise it will generate a new one.
 */
public class AlarmGeneratorFragment extends android.app.Fragment implements View.OnClickListener{

    private static final String TAG = "AlarmGeneratorFragment";
    private AlarmGeneratorBinding mAlarmGeneratorBinding = null;
    private Alarm mAlarm;
    private AlarmActivity mAlarmActivity;

    public AlarmGeneratorFragment() {
        mAlarm = null;
    }
    @Override
    public void setArguments(Bundle bundle){
        final Parcel alarmParcel = bundle.getParcelable(Alarm.ALARM_KEY);
        mAlarm = new Alarm(alarmParcel);

    }
    public void setAlarm(Alarm alarm){
        mAlarm = alarm;
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
//        mLocalCopyOfContext = context;
        mAlarmActivity = (AlarmActivity)getActivity();
    }
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // For now, don't do this:
        //mAlarm.setAlarmName(savedInstanceState.getString(localCopyOfContext.getResources().getString(R.string.AlarmNameString)));
        mAlarm = null;
        if(savedInstanceState != null) {
            mAlarm = savedInstanceState.getParcelable(Alarm.ALARM_KEY);
//            mAlarm.setAlarmName(savedInstanceState.getString("AlarmName"));
//            mAlarm.setAlarmActive((savedInstanceState.getBoolean("AlarmActive")));
        }
        else{
            mAlarm = mAlarmActivity.getSelectedAlarm();
            if(mAlarm == null) {
                mAlarm = new Alarm(mAlarmActivity.getBaseContext());
                mAlarm.setAlarmName("Alarm1");
                mAlarm.setAlarmActive(true);
                mAlarm.setAlarmHour(8, true);
                mAlarm.setVibrate(false);
            }
        }
        AlarmActivity alarmActivity = (AlarmActivity)getActivity();
//        alarmActivity.getAlarmList().add(mAlarm);
        mAlarmGeneratorBinding = (AlarmGeneratorBinding)DataBindingUtil.inflate(inflater,R.layout.alarm_generator,null,false);
//        alarmGeneratorBinding = DataBindingUtil.setContentView(getActivity(),R.layout.alarm_generator);
//        View view = inflater.inflate(R.layout.fragment_alarm, container, false);
        EditText editView = mAlarmGeneratorBinding.editName;
        if(editView == null){
            throw new NullPointerException("Bad AlarmGeneratorBinding");
        }
        editView.setText(mAlarm.getAlarmName());
        editView.setOnClickListener(this);

        TimePicker timeIs = mAlarmGeneratorBinding.timePicker;
        if(Build.VERSION.SDK_INT >= 23) {
            timeIs.setHour(mAlarm.getAlarmHour());
            timeIs.setMinute(mAlarm.getAlarmMinutes());
        }
        else {
            timeIs.setCurrentHour(mAlarm.getAlarmHour());
            timeIs.setCurrentMinute((mAlarm.getAlarmMinutes()));
        }
        timeIs.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mAlarm.setAlarmHour(hourOfDay,false);
                mAlarm.setAlarmMinutes(minute);
            }
        });
        timeIs.setOnClickListener(this);
        CheckBox isActiveBox = mAlarmGeneratorBinding.isActiveBox;
        isActiveBox.setOnClickListener(this);

        Button doneButton = mAlarmGeneratorBinding.doneButton;

        doneButton.setOnClickListener(this);



            //EditText editView = (EditText)view.findViewById(R.id.editName);
        //editView.setOnClickListener(this);
        return mAlarmGeneratorBinding.generatorLayout;
    }

    @Override
    public void onClick(View view){
        if(mAlarmGeneratorBinding == null){
            Log.e(TAG, "onClick: no binding", new BindException("AlarmGeneratorBinding"));
            return;
        }

        int id = view.getId();
        switch(id) { //view.getId()) {
            case R.id.editName:
                EditText textView = mAlarmGeneratorBinding.editName;
                mAlarm.setAlarmName(textView.getText().toString());
                break;
            case R.id.timePicker:

                TimePicker timePicker = mAlarmGeneratorBinding.timePicker;
                if(Build.VERSION.SDK_INT >= 23){
                    mAlarm.setAlarmHour(timePicker.getHour(),!timePicker.is24HourView());
                    mAlarm.setAlarmMinutes(timePicker.getMinute());
                }
                else{
                    mAlarm.setAlarmHour(timePicker.getCurrentHour(),!timePicker.is24HourView());
                    mAlarm.setAlarmMinutes(timePicker.getCurrentMinute());
                }

                break;
            case R.id.isActiveBox:
                mAlarm.setAlarmActive(mAlarmGeneratorBinding.isActiveBox.isChecked());
                //mAlarm.setAlarmActive(((CheckBox)view.findViewById(R.id.isActiveBox)).isChecked());
                break;
            case R.id.doneButton:
//                mAlarm.setAlarmName(mAlarmGeneratorBinding.editName.getText().toString());
//                mAlarm.setAlarmHour(mAlarmGeneratorBinding.timePicker
//                mAlarm.setAlarmActive(mAlarmGeneratorBinding.isActiveBox.isChecked());
                Activity activity = getActivity();
                if(activity instanceof AlarmActivity){
                    try {
                        AlarmDatabase mDb = ((AlarmActivity) activity).getAlarmDatabase();
                        if(0 == mAlarm.getId()){
                            mDb.addAlarm(mAlarm);
                        }
                        else {
//                            int position = mAlarm.getPosition();
//                            if(-1 == position){throw new CursorIndexOutOfBoundsException("alarm position implies this is a new alarm, not the one from the list");}
                            mDb.updateAlarm(mAlarm);
                        }
                        //mAlarmActivity.getContentResolver().registerContentObserver();
                        AlarmListFragment listFragment = (AlarmListFragment)getFragmentManager().findFragmentByTag("Add AlarmList");
                        if(null == listFragment) {
                            throw new NullPointerException("Could not find AlarmListFragment");
                        }
//                        //don't do this until we have a ContentManager
//                        //listFragment.getAlarmListAdapter().notifyDataSetChanged();
//                        //instead we want to re-instantiate the list:
//                        AlarmListAdapter adapter = listFragment.getAlarmListAdapter();
//                        if(null == adapter){
//                            throw new NullPointerException("Could not find AlarmListAdapter");
//                        }
                        ((AlarmActivity) activity).refreshDatabase();
                    }
                    catch(NullPointerException nullE){
                        Toast.makeText(mAlarmActivity,nullE.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(mAlarmActivity,"No alarm created!",Toast.LENGTH_SHORT).show();
                }
                getFragmentManager().popBackStack();
                break;
            default:

        }
    }

    public void onSavedInstanceState (Bundle bundle){
        bundle.putString(mAlarm.getAlarmName(),"AlarmName");
        bundle.putBoolean("AlarmActive", mAlarm.getAlarmActive());
    }
    @Override
    public void onPause(){
        super.onPause();
        Log.i(TAG, "onPause: End the AlarmGeneratorFragment");
    }

    public Alarm getAlarm(){ return mAlarm;}


}
