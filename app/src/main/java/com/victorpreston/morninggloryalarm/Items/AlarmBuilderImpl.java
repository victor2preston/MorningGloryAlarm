package com.victorpreston.morninggloryalarm.Items;

import android.content.ContentValues;
import android.net.Uri;

import com.victorpreston.morninggloryalarm.Database.AlarmDatabase;

import static android.net.Uri.parse;


/**
 * Created by victorpreston on 7/26/2018.
 */

public class AlarmBuilderImpl implements AlarmBuilder {
    private ContentValues builtCv = null;

//    static public Integer intOfDays(Day[] days){
//        Integer N = new Integer(0);
//        for(Day day : days){
//            N = N^(1 << day.getValue());
//        }
//        return N;
//    }
    @Override
    public AlarmBuilder build(String name){
        builtCv = new ContentValues();
        builtCv.put(AlarmDatabase.getFieldName(AlarmDatabase.alarmColumn.COLUMN_NUM_NAME),name);
        return this;
    }
    @Override
    public AlarmBuilder setHour(int hour){
        if(null == builtCv){
            throw new NullPointerException("AlarmBuilder used incorrectly");
        }
        builtCv.put(AlarmDatabase.getFieldName(AlarmDatabase.alarmColumn.COLUMN_NUM_HOUR),hour);
        return this;
    }
    @Override
    public AlarmBuilder setMinute(int minute){
        if(null == builtCv){
            throw new NullPointerException("AlarmBuilder used incorrectly");
        }
        builtCv.put(AlarmDatabase.getFieldName(AlarmDatabase.alarmColumn.COLUMN_NUM_MINUTE),minute);
        return this;
    }
    @Override
    public AlarmBuilder setWeekday(int days){
        if(null == builtCv){
            throw new NullPointerException("AlarmBuilder used incorrectly");
        }
        builtCv.put(AlarmDatabase.getFieldName(AlarmDatabase.alarmColumn.COLUMN_NUM_WEEKDAY),days);
        return this;
    }
    @Override
    public AlarmBuilder setActive(Boolean active){
        if(null == builtCv){
            throw new NullPointerException("AlarmBuilder used incorrectly");
        }
        builtCv.put(AlarmDatabase.getFieldName(AlarmDatabase.alarmColumn.COLUMN_NUM_ACTIVE),active);
        return this;
    }
    @Override
    public AlarmBuilder setAlarmTonePath(String path){
        if(null == builtCv){
            throw new NullPointerException("AlarmBuilder used incorrectly");
        }
        Uri.parse(path); // also throws a NullPointerException if it fails
        builtCv.put(AlarmDatabase.getFieldName(AlarmDatabase.alarmColumn.COLUMN_NUM_TONE_PATH),path);
        return this;

    }

    @Override
    public AlarmBuilder setAlarmToneUri(Uri ringtoneUri){
        if(null == builtCv){
            throw new NullPointerException("AlarmBuilder used incorrectly");
        }
        builtCv.put(AlarmDatabase.getFieldName(AlarmDatabase.alarmColumn.COLUMN_NUM_TONE_PATH),ringtoneUri.toString());
        return this;

    }
    @Override
    public ContentValues getContentValues(){
        return builtCv;
    }
}
