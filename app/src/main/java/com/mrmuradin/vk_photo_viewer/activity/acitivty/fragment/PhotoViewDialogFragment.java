package com.mrmuradin.vk_photo_viewer.activity.acitivty.fragment;


import android.os.Bundle;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mrmuradin.vk_photo_viewer.R;



public class PhotoViewDialogFragment extends DialogFragment  {

    ImageView ivBig;


    public static PhotoViewDialogFragment newInstance(String  pictureUrl) {
        PhotoViewDialogFragment fragment = new PhotoViewDialogFragment();
        Bundle args = new Bundle();
        args.putString("picture", pictureUrl);
        fragment.setArguments(args);
        return fragment;
    }

    public PhotoViewDialogFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_photo, container, false);
        ivBig = (ImageView) v.findViewById(R.id.iv_big);
        Glide.with(this)
                .load(getArguments().getString("picture"))
                .into(ivBig);

        return v;
    }


}
