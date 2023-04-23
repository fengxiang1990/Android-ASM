package com.zj.android_asm

import android.util.Log
import java.util.concurrent.Executors

//
//class AsmThread : java.lang.Thread() {
//
//    override fun run() {
//        super.run()
//        Log.e("fxa","java.lang.Thread modify to AsmThread")
//    }
//}

class AsmThread : java.lang.Thread() {


    var runnable:Runnable? = null


//    override fun run() {
//        Log.e("fxa","java.lang.Thread modify to AsmThread")
//    }


    override fun start(){
        Log.e("fxa","AsmThread start")
        val field = this.javaClass.superclass.getDeclaredField("target")
        field.isAccessible = true//压制java检查机制
        runnable = field.get(this) as Runnable?
//        MyThreadPool.fixPool.execute(runnable)
        MyThreadPool.singlePool.execute(runnable)
    }
}