package com.victorpreston.morninggloryalarm.Items;

//import android.app.Fragment;
import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.LogPrinter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.victorpreston.morninggloryalarm.Database.AlarmCursor;
import com.victorpreston.morninggloryalarm.Database.AlarmDatabase;
import com.victorpreston.morninggloryalarm.R;
import com.victorpreston.morninggloryalarm.databinding.AlarmListBinding;
import com.victorpreston.morninggloryalarm.databinding.ActivityAlarmBinding;
//import com.victorpreston.morninggloryalarm.databinding.AlarmListBinding;

import java.util.ArrayList;

//import static com.victorpreston.morninggloryalarm.AlarmDatabase.AlarmDatabase.DATABASE_NAME;


public class AlarmActivity extends AppCompatActivity implements AlarmListFragment.TransmitAlarm {
    public static String TAG = "AlarmActivity";
    private AlarmDatabase mDb;
    private AlarmListFragment mAlarmListFragment;
    private int mListId = 0;
    private AlarmGenerator mAlarmAdd;
    private Alarm mSelectedAlarm = null;
    private ActivityAlarmBinding mActivityAlarmBinding;
    static final int GENERATE_NEW_ALARM = 1;

    private AlarmListBinding mAlarmListBinding;

    public AlarmActivity(){

        mAlarmAdd = null;
        Thread DBTHread = new Thread("Debug-Breaker") {
            @Override
            public void run() {
                while (true) {
                    try {
//                        if(mDb != null){
//                            mDb.getWritableDatabase();
//                        }
                        Thread.sleep(1000000);
                    } catch (InterruptedException e){ //ExceptionInInitializerError e
                        e.printStackTrace();
                    }

                }
            };
        };
        DBTHread.start();
    }
    @Override public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState ){
        Toast.makeText(this.getBaseContext(),"onPostCreate!",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){

        }

        LogPrinter logPrinter = new LogPrinter(Log.DEBUG,"Victor2Preston: ");
        getMainLooper().setMessageLogging(logPrinter);
        logPrinter.println("VP: Print a starter line.");

        try {
////          Only do this if the database changes:
            getBaseContext().deleteDatabase(AlarmDatabase.DATABASE_NAME);
            mDb = AlarmDatabase.getInstance(getBaseContext());
//          mDb.onUpgrade(mDb.getWritableDatabase(),1,2);

            mDb.getWritableDatabase();  // just to make sure onCreate is called

        //Add a default a couple temporary Alarms so the cursor's not empty and will show a list for initial tests...
            Cursor cursor = AlarmDatabase.getCursorOfList();
            if(0 == cursor.getCount()) {
                mDb.addAlarm("Temp Alarm");
                mDb.addAlarm("Temp Alarm too");
            }
        }catch(Exception e){
               Log.e(TAG,e.getMessage());
        }


        try {
            mActivityAlarmBinding = (ActivityAlarmBinding) DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_alarm, null, false);
            //mAlarmListBinding = (AlarmListBinding)DataBindingUtil.setContentView(this,R.layout.activity_alarm);
            setContentView(mActivityAlarmBinding.getRoot()); //(R.layout.activity_alarm);
        }
        catch(Exception e){
            Log.e("Binding Error",e.getMessage());
        }


//        Toolbar mainToolbar = mActivityAlarmBinding.mainToolbar;
//        setSupportActionBar(mainToolbar);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        // try adding a "normal" fragment
        AlarmListFragment alarmListFragment = new AlarmListFragment();

        fragmentTransaction.add(R.id.alarm_list_place_holder/* alarmList*/ /*(layout.alarm_list*/,alarmListFragment,"Add AlarmList");
//        fragmentTransaction.show(alarmListFragment);
        fragmentTransaction.addToBackStack("Added AlarmList");
        int mListId = fragmentTransaction.commit();
//        mActivityAlarmBinding = DataBindingUtil.inflate(inflater,R.layout.activity_alarm);


//        ImageButton fab = mActivityAlarmBinding.newButton;
//        //ImageButton fabFromID = (ImageButton)findViewById(R.id.newButton);
//        fab.setOnClickListener( new View.OnClickListener(){
//            @Override
//                    public void onClick(View view){
//                        switch(view.getId()){
//                        case R.id.newButton:
//                            FragmentManager fragmentManager = getFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            AlarmGeneratorFragment newAlarmFragment = new AlarmGeneratorFragment();
//                            fragmentTransaction.add(R.id.action_new_alarm,newAlarmFragment,"newAlarmTag");
//                            fragmentTransaction.addToBackStack("newAlarmTag");
//                            fragmentTransaction.commit();
//                            break;
//                    }
//
//            }
//        });
    //            ImageButton newAlarmButton = activityAlarmBinding.newButton;
    //        newAlarmButton.setOnClickListener( (View view) ->
    //                getFragmentManager().beginTransaction());

//        ImageButton imageButton = (ImageButton)findViewById(R.id.newButton);
//        imageButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//                public void onClick(View view){
//                int x = view.getId();
//                    switch(x){
//                    default:
//                        AlarmGeneratorFragment newAlarmFragment = new AlarmGeneratorFragment();
//                        getFragmentManager().beginTransaction().replace(R.id.generator_layout,newAlarmFragment,"newAlarmTag");
//                }
//            }
//        });
        //alarmList = new ArrayList<Alarm>();
        //alarmAddView = findViewById(R.id.action_new_alarm);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i(TAG, "My info in AlarmActivity:onResume: " + this.toString());
    }

    @Nullable
    @Override
    public ActionBar getActionBar() {
        return super.getActionBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm, menu);
        return true;
    }
    AlarmDatabase getAlarmDatabase() {
        return mDb;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()){
            case R.id.action_new_alarm:
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                AlarmGeneratorFragment newAlarmFragment = new AlarmGeneratorFragment();
                fragmentTransaction.add(R.id.generator_layout,newAlarmFragment,"newAlarmTag");
                fragmentTransaction.addToBackStack("newAlarmTag");
                fragmentTransaction.commit();


//                Intent intendNew = new Intent(this,AlarmGenerator.class);
//                intendNew.putExtra("alarmBundle",new Alarm(getApplicationContext()));
//                Bundle bundleNewAlarm = intendNew.getExtras();
//                AlarmGeneratorFragment alarmfragment = (AlarmGeneratorFragment)getFragmentManager().getFragment(bundleNewAlarm, "Alarm");
//                startActivityForResult(intendNew,GENERATE_NEW_ALARM);

            case R.id.action_settings:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public void onActivityResult(int requestCode, int resultCode, Intent intentData){

        if(requestCode == GENERATE_NEW_ALARM){
            if(resultCode == RESULT_OK){
                //7/26Alarm newAlarm = intentData.getParcelableExtra("Alarm");

                //7/26getAlarmList().add(newAlarm);
                //Parcel resultParcel = intentData.getData();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //this.finish(); // What - are you crazy??? This just ends everything everytime "back" is pressed!
    }

    //7/26    List<Alarm> getAlarmList() {
//        return mAlarmList;
//    }
    //7/20 AlarmListAdapter getAlarmListAdapter() { return this.mAlarmListAdapter; }
//    ViewGroup getParentView() { return this.;}
    public ArrayList<String> getFieldNames() { return mDb.getAllFieldNames(); }
    public Cursor getCursor() { return mDb.getCursor();}



    @Override
    public Alarm getSelectedAlarm() {
        return mSelectedAlarm;
    }
    public void setSelectedAlarm(Alarm alarm){
        mSelectedAlarm = alarm;
    }

    public AlarmListBinding getAlarmListBinding() {
        return mAlarmListBinding;
    }
    public int getListId(){
        return mListId;
    }

    public void refreshDatabase(){
        AlarmListFragment alarmListFragment = (AlarmListFragment)getFragmentManager().findFragmentByTag("Add AlarmList");
        if(null ==alarmListFragment){
            throw new ClassCastException("not an AlarmListragment");
        }
        alarmListFragment.refresh();

    }
}
