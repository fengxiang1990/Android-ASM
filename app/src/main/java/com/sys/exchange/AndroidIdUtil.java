package com.sys.exchange;

import android.content.ContentResolver;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import java.util.Objects;

public class AndroidIdUtil {


    static String androidId="";

    public static String getString(ContentResolver resolver, String name,String cla){
        Log.e("fxa","AndroidIdUtil->getString->cla->"+cla);
        if(Objects.equals(name, Settings.Secure.ANDROID_ID)){
            if(!TextUtils.isEmpty(androidId)){
                Log.e("fxa","androidId from cache");
                return androidId;
            }
            Log.e("fxa","androidId real get");
            androidId = Settings.Secure.getString(resolver,name);
            return androidId;
        }else {
            Log.e("fxa","others get");
            return Settings.Secure.getString(resolver,name);
        }
    }

}
