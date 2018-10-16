package com.victorpreston.morninggloryalarm.Items;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.provider.Settings;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.victorpreston.morninggloryalarm.Database.AlarmDatabase;
import com.victorpreston.morninggloryalarm.R;
import com.victorpreston.morninggloryalarm.databinding.AlarmListItemBinding;

import static android.app.PendingIntent.getActivity;


/**
 * Created by victorpreston on 9/21/2017.
 */

public class AlarmListAdapter extends SimpleCursorAdapter {

    @Override
    public Cursor getCursor() {
        return super.getCursor();
    }

    static class AlarmViewHolder extends RecyclerView.ViewHolder {
        public TextView mItemName;
        public TextView mItemHour;
        private int mPosition;

        public AlarmViewHolder(View view, AlarmListItemBinding binder) {
            super(view);
            mItemName= binder.itemName;
            mItemHour = binder.itemTime;
            mPosition = -1;
        }
        public AlarmViewHolder(View view){
            super(view);
            mItemName = (TextView)view.findViewById(R.id.itemName);
            mItemHour = (TextView)view.findViewById(R.id.itemTime);
            mPosition = -1;
        }
        public void setPosition(int position){
            mPosition = position;
        }
        public int getAlarmPosition(){
            return mPosition;
        }
//        public TextView getItemName() { return mItemName;}
    }

//    public static String dbColumnNames[] ={AlarmDatabase.COLUMN_ALARM_NAME,
//            AlarmDatabase.COLUMN_ALARM_ID,
//            AlarmDatabase.COLUMN_ALARM_ACTIVE,
//            AlarmDatabase.COLUMN_ALARM_HOUR,
//            AlarmDatabase.COLUMN_ALARM_MINUTE,
//            AlarmDatabase.COLUMN_ALARM_DAYS,
//            AlarmDatabase.COLUMN_ALARM_DAYS
//    };
    public static String TAG = "AlarmListAdapter";
    public static int dbDestinations[] = {0,1,2,3,4,5};


    private AlarmActivity mAlarmActivity;
    private Context mContext;
    private int mResource;
//    private Activity mActivity;

    public AlarmListAdapter(Context context, int resource, Cursor c, String[] columnNames, int[] toViews, int flags){
        super(context, resource,c, columnNames, toViews, flags);
        mContext = context;
        mResource = resource;
        mAlarmActivity = (AlarmActivity)context; //null; //getActivity(context,0,null,0);
        if(null == mAlarmActivity){
            throw new NullPointerException("context is not an AlarmActivity");
        }

    }
//    public void add( Alarm alarm){
//        Cursor c = getCursor();
//        mAlarms.add(alarm);
//        notifyDataSetChanged();
//    }
    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public int getCount() {
        return getCursor().getCount();
    }

//    @Override
//    public Alarm getItem(int position) {
//        return getCursor().
//        return mAlarms.get(position);
//    }

//    @Override
//    public long getItemId(int position) {
//
//        return mAlarms.get(position).getId();
//    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        AlarmViewHolder holder = null;
        if(null == view){



            //7/23 view = LayoutInflater.from(mContext).inflate(R.layout.alarm_list_item,parent);
            try {
                AlarmListItemBinding alarmListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.alarm_list_item, parent, false);
                TextView itemName = alarmListItemBinding.itemName;
                view = alarmListItemBinding.itemLayout;
                holder = new AlarmViewHolder(view,alarmListItemBinding);
                view.setTag(holder);
            }
            catch(Exception e){
                Log.e(TAG, "getView: failed to create alarmListItemBinding" + e.getMessage(),null );
                throw new NullPointerException("No binding created.");
            }
//            return view;
        }
        else {
            holder = (AlarmViewHolder) view.getTag();
        }
        holder.setPosition(position);
        view.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        AlarmListAdapter.AlarmViewHolder holder = (AlarmListAdapter.AlarmViewHolder)v.getTag();
                        if(null == holder){
                            throw new NullPointerException("getTag in this view does not return the correct object.");
                        }

                        FragmentManager fragmentManager = mAlarmActivity.getFragmentManager();
                        AlarmListFragment currentFragment = (AlarmListFragment)fragmentManager.findFragmentByTag("Add AlarmList");
                        if(null == currentFragment){
                            throw new NullPointerException("Not an AlarmListfragment");
                        }
                        if(null == v){
                            Toast.makeText(v.getContext(),"view not defined here",Toast.LENGTH_SHORT);
                        }
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        AlarmGeneratorFragment editAlarmFragment = new AlarmGeneratorFragment();
                        //Uri uri = RingtoneManager.getActualDefaultRingtoneUri(mAlarmActivity.getBaseContext(),RingtoneManager.TYPE_RINGTONE);
                        Context context = mAlarmActivity.getBaseContext();
                        Alarm alarm = new Alarm(context,getCursor(),holder.mPosition );
                        mAlarmActivity.setSelectedAlarm(alarm);

                        editAlarmFragment.setAlarm(alarm);
                        fragmentTransaction.add(R.id.alarm_list_place_holder, editAlarmFragment, "Add AlarmGeneratorFragment"); // tru replace instead of add - should call onCreateView, but every time!
                        fragmentTransaction.addToBackStack("Added AlarmGenerator");
                        fragmentTransaction.commit();
                        break;
                    case MotionEvent.ACTION_UP:
                        Toast.makeText(mContext, "Touch  ACTION is \"UP\"", Toast.LENGTH_SHORT).show();
                                break;
                    default:
                        Toast.makeText(mContext, "Touch  ACTION not \"DOWN\", instead its " + event.getAction(), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        Cursor cursor = getCursor();
        if(0 < cursor.getColumnCount()) {
            cursor.moveToPosition(position);
//            cursor.moveToFirst();
            {
                holder.mItemName.setText(cursor.getString(AlarmDatabase.alarmColumn.COLUMN_NUM_NAME.ordinal()));
                StringBuilder displayedTime = new StringBuilder();  //the default size (16) should be fine for this.
                String ampm = Settings.System.getString(mContext.getContentResolver(),Settings.System.TIME_12_24);
                Log.w(TAG, "getView: TIME_12_24 string is: " + ampm);

                int hour = cursor.getInt(AlarmDatabase.alarmColumn.COLUMN_NUM_HOUR.ordinal());
                String ampmStr = "";
                if(null == ampm || ampm.equals("12")){
                    if(hour > 12){
                        ampmStr = "PM";
                        hour = hour - 12;
                    }
                    else {
                        ampmStr = "AM";
                    }
                    if(hour > 12 || hour < 0) throw new IndexOutOfBoundsException("Inappropriate time");
                }
                displayedTime.append(hour);
                displayedTime.append(":");
                int minutes = cursor.getInt(AlarmDatabase.alarmColumn.COLUMN_NUM_MINUTE.ordinal());
                if(minutes < 10) //then they're only one digit:
                    displayedTime.append(0);
                displayedTime.append(minutes);
                if(!ampmStr.isEmpty())
                    displayedTime.append(ampmStr);
                Log.w(TAG, "getView: Minutes are " + minutes + ", resulting in this string: " + displayedTime );
                holder.mItemHour.setText(displayedTime);
            }while(cursor.moveToNext());
        }

        return view;
    }

    @Override
    protected void onContentChanged() {
        super.onContentChanged();

    }

    @Override
    public void bindView(View view,Context context,Cursor c) {
        super.bindView(view, context, c); // don't do anything!
    }

//    public void setActivity(AlarmActivity alarmActivity){
//        mAlarmActivity = alarmActivity;
//    }
}
