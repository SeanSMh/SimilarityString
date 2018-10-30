package com.example.sean.bydmarket;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;


public class TimeoutManage extends Application implements TimeoutInterface {
    @Override
    public void display() {
        Log.d("广播发送状况：", "发送失败");
        //Toast.makeText(this,"发送广播失败",Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "发送失败", Toast.LENGTH_SHORT).show();
    }
}
