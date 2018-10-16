package com.victorpreston.morninggloryalarm.Items;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import com.victorpreston.morninggloryalarm.Database.AlarmDatabase;
import com.victorpreston.morninggloryalarm.R;
import com.victorpreston.morninggloryalarm.databinding.AlarmListBinding;

//import static com.victorpreston.morninggloryalarm.Items.AlarmCalendar.TAG;
//import com.victorpreston.morninggloryalarm.databinding.AlarmListBinding;


/**
 * Created by victorpreston on 4/3/2018.
 */

public class AlarmListFragment extends ListFragment { //implements View.OnClickListener {

    private static final String TAG = "AlarmListFragment";
//    private Context mLocalCopyOfContext;
    //private List<Alarm> mAlarmList;
    private AlarmActivity mAlarmActivity = null;
    private AlarmDatabase mAlarmDatabase;
    private AlarmListAdapter mAlarmListAdapter;
    private AlarmListBinding mAlarmListBinding;
    private int mToViewIds[] = { R.id.itemName, R.id.itemTime};

    public interface TransmitAlarm{
        public Alarm getSelectedAlarm();
        public void setSelectedAlarm(Alarm alarm);
    }
    public AlarmListFragment() {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        mLocalCopyOfContext = context;
//        try {
//            mAlarmActivity = (AlarmActivity) getActivity();
//
////            TransmitAlarm transission = (TransmitAlarm)mAlarmActivity;
//        }
//        catch (ClassCastException e){
//            throw new ClassCastException("Activity does not support TransmitAlarm");
//        }

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlarmActivity = (AlarmActivity)getActivity();
        if (null == mAlarmActivity) {
            throw new ActivityNotFoundException("AlarmActivity");
        }


        //7/20 mAlarmListAdapter = alarmActivity.getAlarmListAdapter();
        if (savedInstanceState != null) {
//            for (savedInstanceState.getCharArray("AlarmList").:
//                 ) {
//           }
        } else {


        }
        mAlarmDatabase = mAlarmActivity.getAlarmDatabase();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savesInstanceState) {
        try {
            mAlarmListBinding = DataBindingUtil.inflate(inflater, R.layout.alarm_list, container, false);
        }
        catch (Exception e){
            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

//        mAlarmListBinding = DataBindingUtil.setContentView(getActivity(),R.layout.alarm_list);

        if (savesInstanceState != null) {

            // Maybe get a list of alarms?
        }else {
        }

        mAlarmListAdapter = new AlarmListAdapter(getActivity(),
                R.layout.alarm_list_item,
                mAlarmDatabase.getCursorOfList(), //mAlarmActivity.getAlarmDatabase().getCursorOfList(),
                mAlarmDatabase.getAllFieldNamesAsStringArray(),
                mToViewIds,
                0);
//        mAlarmListAdapter.setActivity(mAlarmActivity);

        ListView listView = mAlarmListBinding.alarmList;
//        AlarmListView alarmListView = (AlarmListView)listView;
//        if(!(alarmListView instanceof ListView)){
//            throw new NullPointerException("not an AlarmListView");
//        }

        try {
            ViewParent listParent = listView.getParent();
            if(null != listParent){
                ((ViewGroup)listParent).removeView(listView);
            }

            //7/20 View itemView = DataBindingUtil.inflate(inflater,R.layout.alarm_list_item,null,false);

            //7/20 mAlarmListAdapter.bindView(itemView,mLocalCopyOfContext,mAlarmListAdapter.getCursor());
            listView.setAdapter(mAlarmListAdapter);
//            listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    onItemEvent(parent,view,position);
//                }
//
//
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//                    Toast.makeText(mLocalCopyOfContext,"Nothing selected!",Toast.LENGTH_SHORT).show();
//                }
//            });
            listView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Toast.makeText(getActivity(),"touchEvent",Toast.LENGTH_SHORT).show();
                    //onItemEvent(parent,view,position);
                    return false;
                }
            });
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onItemEvent(parent,view,position);
                }
//                listView.setOnItemViewClickListene(new OnItemViewClickedListener
//                @Override
//                public void onItemClicked(){
//                    Toast.makeText(mLocalCopyOfContext,"OnItemClicked",Toast.LENGTH_SHORT).show();
//
//                }
            });
            ImageButton fab = (ImageButton) mAlarmListBinding.newButton;
            //ImageButton fabFromID = (ImageButton)findViewById(R.id.newButton);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.newButton:
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            AlarmGeneratorFragment newAlarmFragment = new AlarmGeneratorFragment();
                            fragmentTransaction.hide(fragmentManager.findFragmentById(getAlarmActivity().getListId()));
                            fragmentTransaction.add(R.id.action_new_alarm, newAlarmFragment, "Add AlarmGenerator by new button");
                            //fragmentTransaction.show(newAlarmFragment);
                            fragmentTransaction.addToBackStack("Added AlarmGenerator by new button");
                            fragmentTransaction.commit();
                            break;
                    }

                }
            });
        }
        catch(Exception e){
            Log.e(TAG, "onCreateView: " + e.getMessage() );
        }
            return listView;


        }

        public void onItemEvent(AdapterView<?> parent, View view, int position){
            AlarmGeneratorFragment alarmGeneratorFragment = new AlarmGeneratorFragment();
            Alarm alarm = new Alarm(getActivity(),mAlarmListAdapter.getCursor(),position);
            Bundle selectedAlarm = new Bundle();
            selectedAlarm.putParcelable("selectedAlarm",alarm);
//                    alarm.writeToParcel(selectedAlarm);
            alarmGeneratorFragment.setArguments(selectedAlarm);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.itemLayout, alarmGeneratorFragment, "Add Alarm Editor from List");
            fragmentTransaction.commit();

        }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.i(TAG, "My onListItemClick: " + v.toString());
    }


    @Nullable
    @Override
    public View getView() {
        View view  = super.getView();
        Log.i(TAG, "My msg in getView: " + view.toString());
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: ");
    }

//    @Override
//    public void onClick(View view) {
//        int id = view.getId();
//        switch (id) {
//            case R.id.appName:
//
//                break;
//        }
//    }
    public void onSavedInstanceState(Bundle bundle){
        bundle.putString("SavedInstance","What should I put here?");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.i(TAG, "onPause: End the AlarmListFragment");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public AlarmActivity getAlarmActivity() {
        return mAlarmActivity;
    }

    public AlarmListAdapter getAlarmListAdapter() {
        return mAlarmListAdapter;
    }
    public void refresh() {
        AlarmListAdapter refreshedAlarmListAdapter = new AlarmListAdapter(
                getActivity(),
                R.layout.alarm_list_item,
                mAlarmDatabase.getCursorOfList(), //mAlarmActivity.getAlarmDatabase().getCursorOfList(),
                mAlarmDatabase.getAllFieldNamesAsStringArray(),
                mToViewIds,
                0
                );
        ListView listView = mAlarmListBinding.alarmList;
        listView.setAdapter(refreshedAlarmListAdapter);
    }

}


