package com.content.provider;

import android.util.Log;

public class ContentProviderHandler {

    public String echo(String value) {
        Log.i("Echo", value);
        return value;
    }
}
