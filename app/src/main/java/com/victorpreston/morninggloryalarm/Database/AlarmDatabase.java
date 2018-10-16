
package com.victorpreston.morninggloryalarm.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

//7/26/import com.victorpreston.morninggloryalarm.Items.Alarm;
import com.victorpreston.morninggloryalarm.Items.Alarm;
import com.victorpreston.morninggloryalarm.Items.AlarmBuilder;
import com.victorpreston.morninggloryalarm.Items.AlarmBuilderImpl;

import java.util.ArrayList;

/**
 * Created by victorpreston on 10/10/2017.
 */

public class AlarmDatabase extends SQLiteOpenHelper { //implements SQLData {

    static String TAG = "AlarmDatabase";
    static AlarmDatabase mInstance = null;
//    static SQLiteDatabase database = null;

    static public final String DATABASE_NAME = "AlarmDb";
    static public final int DATABASE_VERSION = 1;
//    static public final String COLUMN_ALARM_ID = "_id";
//    static public final String COLUMN_ALARM_NAME = "alarm_name";
//    static public final String COLUMN_ALARM_ACTIVE = "alarm_active";
//    static public final String COLUMN_ALARM_HOUR = "alarm_hour";
//    static public final String COLUMN_ALARM_MINUTE = "alarm_minute";
//    static public final String COLUMN_ALARM_DAYS = "alarm_days";

    static public final String ALARM_TABLE = " alarm";
    static public enum alarmColumn {
        COLUMN_NUM_ID,
        COLUMN_NUM_NAME,
        COLUMN_NUM_HOUR,
        COLUMN_NUM_MINUTE,
        COLUMN_NUM_WEEKDAY,
        COLUMN_NUM_ACTIVE,
        COLUMN_NUM_TONE_PATH;

        private int value = 0;
        private void alarmColumn(int val){
            value = val;
        }
//        public int getValue(){
//            return value;
//        }
    }
//    public static int getColumnAsInt(alarmColumn columnId) {
//        switch (columnId ){
//            case COLUMN_NUM_ID:
//                return 0;
//            case COLUMN_NUM_NAME:
//                return 1;
//            case COLUMN_NUM_HOUR:
//                return 2;
//            case COLUMN_NUM_MINUTE:
//                return 3;
//            case COLUMN_NUM_WEEKDAY:
//                return 4;
//            case COLUMN_NUM_ACTIVE:
//                return 5;
//            case COLUMN_NUM_TONE_PATH:
//                return 6;
//        }
//        throw new IndexOutOfBoundsException("bad ColumnId");
//    }



    static public String getFieldName(alarmColumn columnNum){
        switch(columnNum){
            case COLUMN_NUM_ID:
                return "_id";
            case COLUMN_NUM_NAME:
                return "alarm_name";
            case COLUMN_NUM_HOUR:
                return "alarm_hour";
            case COLUMN_NUM_MINUTE:
                return "alarm_minute";
            case COLUMN_NUM_WEEKDAY:
                return "alarm_weekday";
            case COLUMN_NUM_ACTIVE:
                return "alarm_active";
            case COLUMN_NUM_TONE_PATH:
                return "alarm_tone_path";
//            case COLUMN_NUM_:
//                return "";
//            case COLUMN_NUM_:
//                return "";
//            case COLUMN_NUM_:
//                return "";
//            case COLUMN_NUM_:
//                return "";
        }
        return "";
    }
    static public String getFieldType(AlarmDatabase.alarmColumn column){
        switch(column){
            case COLUMN_NUM_ID:
            case COLUMN_NUM_HOUR:
            case COLUMN_NUM_MINUTE:
            case COLUMN_NUM_ACTIVE:
            case COLUMN_NUM_WEEKDAY:
                return " INTEGER ";
            case COLUMN_NUM_NAME:
            case COLUMN_NUM_TONE_PATH:
                return " TEXT ";
        }
        throw new StringIndexOutOfBoundsException("Asked for a non-existent field type");
    }

    //Makes a default Alarm using just the name
    public void addAlarm(String name){
        AlarmBuilderImpl cvBuilder = new AlarmBuilderImpl();
        Alarm.Day defaultDays[] = { Alarm.Day.Sunday,
                Alarm.Day.Monday,
                Alarm.Day.Tuesday,
                Alarm.Day.Wednesday,
                Alarm.Day.Thursday,
                Alarm.Day.Friday,
                Alarm.Day.Saturday };
        Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(getContext(),RingtoneManager.TYPE_RINGTONE);
        cvBuilder.build(name).setHour(8).setMinute(0).setWeekday(Alarm.intOfDays(defaultDays)).setActive(true).setAlarmToneUri(ringtoneUri);

        mInstance.getWritableDatabase().insert(ALARM_TABLE,null,cvBuilder.getContentValues());
    }
    private AlarmBuilder buildBuilder(Alarm alarm){
        AlarmBuilderImpl cvBuilder = new AlarmBuilderImpl();
        Alarm.Day days[] = {
                alarm.getSundayActive() ? Alarm.Day.Sunday : Alarm.Day.Empty,
                alarm.getMondayActive() ? Alarm.Day.Monday : Alarm.Day.Empty,
                alarm.getTuesdayActive() ? Alarm.Day.Tuesday : Alarm.Day.Empty,
                alarm.getWednesdayActive() ? Alarm.Day.Wednesday : Alarm.Day.Empty,
                alarm.getThursdayActive() ? Alarm.Day.Thursday : Alarm.Day.Empty,
                alarm.getFridayActive() ? Alarm.Day.Friday : Alarm.Day.Empty,
                alarm.getSaturdayActive() ? Alarm.Day.Saturday : Alarm.Day.Empty};
        cvBuilder.build(alarm.getAlarmName())
                .setHour(alarm.getAlarmHour())
                .setMinute(alarm.getAlarmMinutes())
                .setWeekday(Alarm.intOfDays(days))
                .setActive(alarm.getAlarmActive())
                .setAlarmTonePath(alarm.getAlarmTonePath());
        return cvBuilder;
    }
    public void addAlarm(Alarm alarm){
        AlarmBuilder cvBuilder = buildBuilder(alarm);
        mInstance.getWritableDatabase().insert(ALARM_TABLE,null,cvBuilder.getContentValues());
    }

    public void updateAlarm(Alarm alarm){
        AlarmBuilder cvBuilder = buildBuilder(alarm);
        Integer id = alarm.getId();
        String [] args = {id.toString()};
        String whereClause = getFieldName(alarmColumn.COLUMN_NUM_ID) + " = ?";
        mInstance.getWritableDatabase().update(ALARM_TABLE,cvBuilder.getContentValues(),whereClause,args);
    }



    static public ArrayList<String> getAllFieldNames() {
        //Using COLUMN_NUM_LAST tells us how large to make the array, so all new
        // entries to the columns table should go just before it
        // (but after all th others so the database changes are minimal)
        ArrayList<String> strArray = new ArrayList<String>((int)alarmColumn.values().length);
        for(AlarmDatabase.alarmColumn alarmColumns : AlarmDatabase.alarmColumn.values()){
            strArray.add(getFieldName(alarmColumns));
        }
        return strArray;
    }


    static public String[] getAllFieldNamesAsStringArray() {
        ArrayList<String> arrayList = getAllFieldNames();
        String[] arr = new String[arrayList.size()];
        return arrayList.toArray(arr);
    }


    private Context mLocalCopyOfContext;

//    public static void init(Context context){
//        if(instance == null) {
//            instance = new AlarmDatabase(context);
//        }
//    }
    public static synchronized AlarmDatabase getInstance(Context context){
        if(mInstance == null){
            mInstance = new AlarmDatabase(context);
        }
        return mInstance;
    }
    private AlarmDatabase(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        mLocalCopyOfContext = context;
    }

    public Context getContext() {
        return mLocalCopyOfContext;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + ALARM_TABLE);
        onCreate(db);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        StringBuilder creationTableBuilder = new StringBuilder(256); //hopefully big enough!
        creationTableBuilder.append("CREATE TABLE IF NOT EXISTS " + AlarmDatabase.ALARM_TABLE + " ( ");
        for(AlarmDatabase.alarmColumn columnList: alarmColumn.values()) {
            if (columnList == alarmColumn.COLUMN_NUM_ID) {
                creationTableBuilder.append(getFieldName(columnList));
                creationTableBuilder.append(" INTEGER primary key autoincrement, ");
            } else {
                creationTableBuilder.append(getFieldName(columnList));
                creationTableBuilder.append(getFieldType(columnList));
                creationTableBuilder.append("NOT NULL, ");
            }
        }
        creationTableBuilder.deleteCharAt(creationTableBuilder.length() - 2);//remove the final comma! requires 'length() - TWO', the string has a '32' at length() - 1
        creationTableBuilder.append(");");
//        AlarmCursor alarmCursor = new AlarmCursor();
//        SQLiteDatabase.CursorFactory alarmCursor = newCursor(this,);
        Log.w(TAG, "onCreate: " + creationTableBuilder.toString());
        db.execSQL(creationTableBuilder.toString());

    }

    public static Cursor getCursorOfList(){
        return  mInstance.getReadableDatabase().query(
                ALARM_TABLE,
                getAllFieldNamesAsStringArray(),
                null,
                null,
                null,
                null,
                null);
//        if(null == alarmCursor){
//            throw new ClassCastException("Not an AlarmCursor");
//        }
//        return alarmCursor;
    }
    public Cursor getCursor() {
        return mInstance.getCursor();
    }

    public Alarm getAlarm(int position){
        Alarm alarm = new Alarm(mLocalCopyOfContext,getCursorOfList(),position);
        return alarm;
    }

//    public void addAlarm(Alarm alarm){
//        ContentValues cv = new ContentValues();
//
//        for(AlarmDatabase.alarmColumn column : AlarmDatabase.alarmColumn.values()){
//
//            cv.put(getFieldName(column),alarm.getId());
//        }
//
//        cv.put(getFieldName(AlarmColumn .COLUMN_NUM_ID),alarm.getAlarmName());
//        cv
//        cv.put(COLUMN_ALARM_ID,alarm.getId());
//        cv.put(COLUMN_ALARM_ACTIVE,alarm.getAlarmActive());
//        cv.put(COLUMN_ALARM_HOUR,alarm.getHourOfDay(mMyCopyOfContext));
//        cv.put(COLUMN_ALARM_MINUTE,alarm.getMinutes());
//        cv.put(COLUMN_ALARM_DAYS,alarm.getSundayActive());
//        cv.put(COLUMN_ALARM_DAYS,alarm.getMondayActive());
//        cv.put(COLUMN_ALARM_DAYS,alarm.getTuesdayActive());
//        cv.put(COLUMN_ALARM_DAYS,alarm.getWednesdayActive());
//        cv.put(COLUMN_ALARM_DAYS,alarm.getThursdayActive());
//        cv.put(COLUMN_ALARM_DAYS,alarm.getFridayActive());
//        cv.put(COLUMN_ALARM_DAYS,alarm.getSaturdayActive());
//
//        mInstance.getWritableDatabase().insert(ALARM_TABLE,null,cv);
//    }


//7/26    public int getColumnIndex(String columnName){
//7/26    }
}
