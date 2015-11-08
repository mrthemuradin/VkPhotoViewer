package com.mrmuradin.vk_photo_viewer.activity.acitivty.activity;


import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.mrmuradin.vk_photo_viewer.R;
import com.mrmuradin.vk_photo_viewer.activity.acitivty.fragment.LoginFragment;
import com.mrmuradin.vk_photo_viewer.activity.acitivty.fragment.PhotoListFragment;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

public class MainActivity extends AppCompatActivity {

    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        iv = (ImageView) findViewById(R.id.ivBackground);
        iv.setImageResource(R.drawable.back);

        if (savedInstanceState == null) {
            showFragmentWithToolbar(LoginFragment.newInstance(), false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean isResultForVKActivity = !VKSdk.onActivityResult(requestCode, resultCode, data, callbackForVkResult);
        if (isResultForVKActivity) {

            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showFragmentWithToolbar(Fragment fragment, boolean isToolbarShouldOnScreen) {
        if (isToolbarShouldOnScreen){
            getSupportActionBar().show();
        }
        else {
            getSupportActionBar().hide();
        }

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container, fragment)
                .commit();
    }

    public void onLogoutButtonPressed() {
        VKSdk.logout();
        showFragmentWithToolbar(LoginFragment.newInstance(), false);
    }

    public void onReturnButtonPressed() {
        showFragmentWithToolbar(PhotoListFragment.newInstance(), true);
    }

    @Override
    public void onBackPressed() {
        showFragmentWithToolbar(LoginFragment.newInstance(), false);
    }

    VKCallback<VKAccessToken> callbackForVkResult = new VKCallback<VKAccessToken>() {
        @Override
        public void onResult(VKAccessToken res) {
            showFragmentWithToolbar(PhotoListFragment.newInstance(), true);
        }

        @Override
        public void onError(VKError error) {

        }
    };
}
