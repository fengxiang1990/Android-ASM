package com.zj.android_asm;

import android.util.Log;

public class TestThread {

    public void doInBackGround(){
        new Thread() {
            @Override
            public void run() {
                Log.e("fxa","doInBackGround");
            }
        }.start();
    }

}
