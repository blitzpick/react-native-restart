package com.avishayil.rnrestart;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import android.app.Activity;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Avishay on 7/17/16.
 */
public class ReactNativeRestart extends ReactContextBaseJavaModule {

    private static final String REACT_APPLICATION_CLASS_NAME = "com.facebook.react.ReactApplication";
    private static final String REACT_NATIVE_HOST_CLASS_NAME = "com.facebook.react.ReactNativeHost";


    public ReactNativeRestart(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    private void loadBundleLegacy() {
        final Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            // The currentActivity can be null if it is backgrounded / destroyed, so we simply
            // no-op to prevent any null pointer exceptions.
            return;
        }

        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.recreate();
            }
        });
    }

    private void loadBundle() {
        try {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    loadBundleLegacy();
                }
            });
        } catch (Exception e) {
            loadBundleLegacy();
        }
    }

    private void triggerRebirth() {
        final ReactApplicationContext reactContext = this.getReactApplicationContext();
        Intent i = reactContext
                .getPackageManager()
                .getLaunchIntentForPackage(reactContext.getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        reactContext.startActivity(i);
        System.exit(0);
    }

    @ReactMethod
    public void Restart() {
        this.triggerRebirth();
    }

    @Override
    public String getName() {
        return "RNRestart";
    }

}
