package com.example.bacquet.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.example.bacquet.myapplication.clickCounting.ActionBegin;
import com.example.bacquet.myapplication.clickCounting.ActionEnd;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @ActionBegin(name = "Test")
    public void AAction(View view) {
        Intent intent = new Intent(this,BActivity.class);
        startActivity(intent);
    }

    @ActionEnd(name = "Test")
    public void EndAAction(View view) {
    }
}
