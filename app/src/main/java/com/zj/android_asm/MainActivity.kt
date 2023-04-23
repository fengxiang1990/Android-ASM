package com.zj.android_asm

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.zj.android_asm.databinding.ActivityMainBinding
import java.io.FileReader

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        test(100)
        sum(1, 2)
        doInBackGround()
        binding.btnAndroidid.setOnClickListener{
            val androidid = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
            Log.e("fxa","andoidid->$androidid")
        }
        binding.btnInputmethod.setOnClickListener{
            val inputMethod = Settings.Secure.getString(contentResolver, Settings.Secure.DEFAULT_INPUT_METHOD)
            Log.e("fxa","inputMethod->$inputMethod")
        }

        try{
            FileReader("xxxxxxx")
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun doInBackGround(){
        Thread {
            Log.e("fxa","doInBackGround ${Thread.currentThread().name}")
        }.start()

        Thread {
            Log.e("fxa","doInBackGround1 ${Thread.currentThread().name}")
        }.start()

        Thread {
            Log.e("fxa","doInBackGround2 ${Thread.currentThread().name}")
        }.start()
    }

    private fun test(time: Long) {
        Thread.sleep(time)
    }

    private fun sum(i: Int, j: Int): Int {
        return i + j
    }
}