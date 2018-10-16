package com.victorpreston.morninggloryalarm.Items;

/**
 * Created by victorpreston on 7/26/2018.
 */

import android.content.ContentValues;
import android.net.Uri;

/**
 * Created by victorpreston on 9/20/2017.
 */


public interface AlarmBuilder {

//    public enum Day {
//        Sunday,
//        Monday,
//        Tuesday,
//        Wednesday,
//        Thursday,
//        Friday,
//        Saturday;
//
//        private int value = 0;
//        private void Day(int val){
//            value = val;
//        }
//        public int getValue(){
//            return value;
//        }
//    }
    public AlarmBuilder build(String name);
    public AlarmBuilder setHour(int hour);
    public AlarmBuilder setMinute(int minute);
    public AlarmBuilder setWeekday(int days);
    public AlarmBuilder setActive(Boolean active);
    public AlarmBuilder setAlarmTonePath(String path);
    public AlarmBuilder setAlarmToneUri(Uri ringtoneUri);

    public ContentValues getContentValues();
}
