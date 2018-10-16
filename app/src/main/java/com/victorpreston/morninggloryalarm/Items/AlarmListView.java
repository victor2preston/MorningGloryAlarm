package com.victorpreston.morninggloryalarm.Items;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;



public class AlarmListView extends ListView {

    private AlarmListView.OnItemTouchListener mOnItemTouchListener;
    public AlarmListView(Context context) {
        super(context);
    }
    public interface OnItemTouchListener {
        abstract public void OnTouchEvent(View v, int position);
    }

    public void setOnItemTouchEvent( AlarmListView.OnItemTouchListener onItemTouchEvent){
        mOnItemTouchListener = onItemTouchEvent;

    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
    }
}

