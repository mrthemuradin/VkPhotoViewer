package com.mrmuradin.vk_photo_viewer.activity.acitivty;

import android.app.Application;

import com.vk.sdk.VKSdk;

/**
 * Created by Muradin on 02.11.2015.
 */
public class VKPhotoViewApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);
    }
}
