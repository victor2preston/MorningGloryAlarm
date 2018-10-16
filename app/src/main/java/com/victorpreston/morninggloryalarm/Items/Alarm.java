package com.victorpreston.morninggloryalarm.Items;

import android.content.Context;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.victorpreston.morninggloryalarm.Database.AlarmCursor;
import com.victorpreston.morninggloryalarm.Database.AlarmDatabase;

/**
 * Created by victorpreston on 9/20/2017.
 */


public class Alarm implements Parcelable {
    private static final String TAG = "Alarm Class";
    public final static String ALARM_KEY = "Alarm";
    public enum Day {
        Empty,
        Sunday,
        Monday,
        Tuesday,
        Wednesday,
        Thursday,
        Friday,
        Saturday;

//        private int value = 0;
//        private void Day(int val){
//            value = val;
//        }
//        public int getValue(){
//            return value;
//        }
    }
    static public Integer intOfDays(Day[] days){
        Integer N = new Integer(0);
        for(Day day : days){
            int n = day.ordinal();
            N = N^(1 << n);
        }
        return N;
    }
    static public Boolean getDayFromInt(int weekdayAsInt,Day day){
        return ((1 << day.ordinal()) & weekdayAsInt) > 0;
    }

    private static int mBaseID = 0;
    private Boolean mSunday = false;
    private Boolean mMonday = false;
    private Boolean mTuesday = false;
    private Boolean mWednesday = false;
    private Boolean mThursday = false;
    private Boolean mFriday = false;
    private Boolean mSaturday = false;

    private Boolean mIs24Hour = false;

    private Boolean mAlarmActive = true;

    private Boolean mVibrate = true;

    // @parcel
    private String mAlarmName = "Alarm";
    private static final long mSerialVersionID = 0;
    private int mId;
    private Uri mAlarmTonePath;

    private int mAlarmHour = 8; // defaults to 8am, is always 24-hour
    private int mAlarmMinutes = 0;

    private int mPosition = -1; // If selected from a list this is set to this alarm's position in the list, otherwise meaningless

    @Override
    @NonNull
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(super.toString());
        stringBuilder.append(mId);
        if(mAlarmActive)
            stringBuilder.append(" Active on");
        if(mSunday)
            stringBuilder.append(" Sunday");
        if(mMonday)
            stringBuilder.append(" Monday");
        if(mTuesday)
            stringBuilder.append(" Tuesday");
        if(mWednesday)
            stringBuilder.append(" Wednesday");
        if(mThursday)
            stringBuilder.append(" Thursday");
        if(mFriday)
            stringBuilder.append(" Friday");
        if(mSaturday)
            stringBuilder.append(" Saturday");
        if(mVibrate)
            stringBuilder.append(" vibrate on");
        else
            stringBuilder.append(" vibrate off");


        stringBuilder.append( " time " + getAlarmHour());
        return stringBuilder.toString();
    }


//    private int days[] = {0,1,2,3,4,5,6}; //Not using enums so its Parcelable//Day.Sunday, Day.Monday, Day.Tuesday, Day.Wednesday, Day.Thursday, Day.Friday, Day.Saturday};

//    public int getAsInt(AlarmDatabase.alarmColumn column){
//            return 0;
//    }
    public int getId(){
        return mId;
    }

    public long getSerialVersion(){
        return mSerialVersionID;
    }

    public Boolean getAlarmActive() {
        return mAlarmActive;
    }
    public void setAlarmActive(Boolean active){ mAlarmActive = active;}

    public Boolean getSundayActive() {
        return mSunday;
    }
    public void setSundayActive(Boolean active){ mSunday = active;}
    public Boolean getMondayActive() {
        return mMonday;
    }
    public void setMondayActive(Boolean active){ mMonday = active;}
    public Boolean getTuesdayActive() {
        return mTuesday;
    }
    public void setTuesdayActive(Boolean active){ mTuesday = active;}
    public Boolean getWednesdayActive() {
        return mWednesday;
    }
    public void setWednesdayActive(Boolean active){ mWednesday = active;}
    public Boolean getThursdayActive() {
        return mThursday;
    }
    public void setThursdayActive(Boolean active){ mThursday = active;}
    public Boolean getFridayActive() {
        return mFriday;
    }
    public void setFridayActive(Boolean active){ mFriday = active;}
    public Boolean getSaturdayActive() {
        return mSaturday;
    }
    public void setSaturdayActive(Boolean active){ mSaturday = active;}

//    public AlarmCalendar getAlarmTime() {return alarmTime;    }
    public int getAlarmHour() { return !mIs24Hour && mAlarmHour > 12 ? mAlarmHour - 12 : mAlarmHour; }
    public void setAlarmHour(int hour, Boolean isPm) {
        if(!mIs24Hour && isPm){
            hour += 12;
        }
        if(24 < hour || 0 > hour) throw new IndexOutOfBoundsException("Inapproriate number of hours");
        mAlarmHour = hour;
    }
    public int getAlarmMinutes() { return mAlarmMinutes; }
    public void setAlarmMinutes(int minutes){
        mAlarmMinutes = minutes;
    }

    public String getAlarmTonePath(){
        return mAlarmTonePath.toString();
    }
    public void setAlarmTonePath(String path){
        mAlarmTonePath = Uri.parse(path);
    }

    public String getTimeString() {
        StringBuilder timeStr = new StringBuilder();
        timeStr.append(getAlarmHour());
        timeStr.append(":");
        timeStr.append(getAlarmMinutes());
        return timeStr.toString();
    }

//    private void setAlarmTimeToTheSecond(int seconds){
//        alarmTime = seconds;
//    }

//    public int getHourOfDay(Context context) {
//        int hour = alarmTime / 3600;
//        if (!DateFormat.is24HourFormat(context)){
//            if(hour > 12)
//                hour = hour - 12;
//        }
//        return hour;
//    }
    // The private field for Days[] is actually an array of ints so that Parcelable works, so
    //we translate it here into the enums. So outside the AlarmCalendar object all we see is enums,
//    public Day[] getDays() {
//        Day theEnums[days.length];
//        for(int i = 0; i < days.length; i++){
//            theEnums[i] = Day.fromInt(days[i]);
//        }
//        return theEnums;
//    }
//    public int[] getRawDays() {
//        return days;
//    }
//    public void setDays(Day[] daySetting){
//        days = new int[daySetting.length];
//        for(int i = 0; i < daySetting.length; i++){
//            days[i] = daySetting[i].ordinal();
//        }
//    }

    public Boolean getVibrate() {
        return mVibrate;
    }
    public void setVibrate(Boolean shake) { mVibrate = shake;}

    public String getAlarmName() {
        return mAlarmName;
    }
    public void setAlarmName(String name) {

        mAlarmName = name;
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //OK, let's order these things from smallest to largest:

        writeBooleanToParcel(dest,mAlarmActive);
        writeBooleanToParcel(dest,mVibrate);

        writeBooleanToParcel(dest,mSunday);
        writeBooleanToParcel(dest,mMonday);
        writeBooleanToParcel(dest,mTuesday);
        writeBooleanToParcel(dest,mWednesday);
        writeBooleanToParcel(dest,mThursday);
        writeBooleanToParcel(dest,mFriday);
        writeBooleanToParcel(dest,mSaturday);

        dest.writeInt(mId);

        dest.writeString(mAlarmName);

        dest.writeString(mAlarmTonePath.toString());

//        dest.writeParcelable(AlarmCalendar.getInstance(),0);
    }

    public static Parcelable.Creator<Alarm> CREATOR
            = new Parcelable.Creator<Alarm>() {
        public Alarm createFromParcel(Parcel in){
            return new Alarm(in);
        }
        public Alarm[] newArray(int size){
            return new Alarm[size];
        }
    };

    public Alarm(Parcel in){
        mAlarmActive = readBooleanFromParcel(in);
        mVibrate = readBooleanFromParcel(in);

        mSunday = readBooleanFromParcel(in);
        mMonday = readBooleanFromParcel(in);
        mTuesday= readBooleanFromParcel(in);
        mWednesday = readBooleanFromParcel(in);
        mThursday = readBooleanFromParcel(in);
        mFriday = readBooleanFromParcel(in);
        mSaturday = readBooleanFromParcel(in);

//        Byte isActiveVal = in.readByte();
//        if(isActiveVal == 1)
//            alarmActive = true;
//        else
//            alarmActive = false;

//
//        Byte isVibrateVal = in.readByte();
//        if(isVibrateVal == 1)
//            vibrate = true;
//        else
//            vibrate = false;

        mId = in.readInt();

//        in.readIntArray(days);

        mAlarmName = in.readString();

        mAlarmTonePath = Uri.parse(in.readString());


        mAlarmHour = in.readInt();
        mAlarmMinutes = in.readInt();

    }
    //from the database:
//    Alarm(Context context,AlarmDatabase db,int position) {
//        try {
//            Cursor alarmList = db.getCursorOfList();
//            if (null == alarmList) {
//                Log.e(TAG, "Alarm: Tried to move to bad database position");
//            }
//        }
//    }
    public Alarm(Context context,Cursor alarmList,int position){
        try {
            if (null == alarmList) {
                Log.e(TAG, "Alarm: Tried to move to bad database position");
            }
            alarmList.moveToPosition(position);
            mAlarmName = alarmList.getString(AlarmDatabase.alarmColumn.COLUMN_NUM_NAME.ordinal());
            mAlarmHour= alarmList.getInt(AlarmDatabase.alarmColumn.COLUMN_NUM_HOUR.ordinal());
            mAlarmMinutes = alarmList.getInt(AlarmDatabase.alarmColumn.COLUMN_NUM_MINUTE.ordinal());
            mId = alarmList.getInt(AlarmDatabase.alarmColumn.COLUMN_NUM_ID.ordinal());
            mVibrate = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("vibrate",false);

            int weekday = alarmList.getInt(AlarmDatabase.alarmColumn.COLUMN_NUM_WEEKDAY.ordinal());
            mSunday = Alarm.getDayFromInt(weekday,Day.Sunday);
            mMonday = Alarm.getDayFromInt(weekday,Day.Monday);
            mTuesday= Alarm.getDayFromInt(weekday,Day.Tuesday);
            mWednesday = Alarm.getDayFromInt(weekday,Day.Wednesday);
            mThursday = Alarm.getDayFromInt(weekday,Day.Thursday);
            mFriday = Alarm.getDayFromInt(weekday,Day.Friday);
            mSaturday = Alarm.getDayFromInt(weekday,Day.Saturday);


            mAlarmTonePath = Uri.parse(alarmList.getString(AlarmDatabase.alarmColumn.COLUMN_NUM_TONE_PATH.ordinal()));
//            mPosition = position;

        }
        catch(Exception e) {
            Log.e(TAG, "Alarm: (constructed from database): " + e.getMessage());
        }

    }
    // a default alarm:
    public Alarm(Context context){
        mAlarmActive = true;
        mVibrate = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("vibrate",false);
        mId = mBaseID++;
        mSunday = true;
        mMonday = true;
        mTuesday= true;
        mWednesday = true;
        mThursday = true;
        mFriday = true;
        mSaturday = true;

        mId = mBaseID++;

        mAlarmName = "TempAlarmName";

        mAlarmTonePath = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        mAlarmHour = 0;
        mAlarmMinutes = 0;


    }
    public Boolean readBooleanFromParcel(Parcel in){
        int isTrue = in.readInt();
        if(isTrue == 1)
            return true;
        else
            return false;
    }
    public void writeBooleanToParcel(Parcel dest,Boolean flag) {
        if (flag)
            dest.writeInt(1);
        else
            dest.writeInt(0);
    }
//    public int getPosition(){
//        return mPosition;
//    }
}
