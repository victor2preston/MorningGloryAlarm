package com.victorpreston.morninggloryalarm;

import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    public static final String TAG = "ExampleUnitTest";

    @Test
    public void addition_isCorrect() throws Exception {
        Log.d(TAG,"addition_is...");
        assertEquals(4, 2 + 2);
    }
}