package com.aditya.chalchitra.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aditya.chalchitra.R;
import com.aditya.chalchitra.base.BaseAppCompat;

public class SignInActivity extends BaseAppCompat {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }
}
