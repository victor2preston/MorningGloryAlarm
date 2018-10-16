package com.victorpreston.morninggloryalarm.Database;

import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteQuery;

import com.victorpreston.morninggloryalarm.Items.Alarm;

import static com.victorpreston.morninggloryalarm.Database.AlarmDatabase.ALARM_TABLE;

/**
 * Created by victorpreston on 7/30/2018.
 */

public class AlarmCursor extends SQLiteCursor {
    AlarmCursor(SQLiteCursorDriver SQLitedriver,
                SQLiteQuery query) {
        super(SQLitedriver,
                ALARM_TABLE,
                query);
        }
    // encourage the use of enums when accessing the database
    public String getString(AlarmDatabase.alarmColumn column) { return getString( column.ordinal()); }
    public int getInt(AlarmDatabase.alarmColumn column) { return getInt( column.ordinal()); }
    public byte[] getBlob(AlarmDatabase.alarmColumn column) { return getBlob( column.ordinal()); }
    public Double getDouble(AlarmDatabase.alarmColumn column) { return getDouble( column.ordinal()); }
    public Float getFloat(AlarmDatabase.alarmColumn column) { return getFloat( column.ordinal()); }
    public Short getShort(AlarmDatabase.alarmColumn column) { return getShort( column.ordinal()); }

    @Override
    public void registerContentObserver(ContentObserver observer) {
        super.registerContentObserver(observer);
    }

    @Override
    public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer){
        // destroys what's in the buffer now..., assuming it's a normal pointer
        String contentsOfColumn = getString(columnIndex);
        buffer.sizeCopied = contentsOfColumn.length();
        buffer.data = contentsOfColumn.toCharArray();
    }

    @Override
    public void unregisterContentObserver(ContentObserver observer) {
        this.registerContentObserver(observer);
    }

}
