package com.mrmuradin.vk_photo_viewer.activity.acitivty.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.mrmuradin.vk_photo_viewer.R;
import com.mrmuradin.vk_photo_viewer.activity.acitivty.activity.MainActivity;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;

public class LoginFragment extends Fragment implements View.OnClickListener {

    public static Fragment newInstance() {
        return new LoginFragment();
    }

    public LoginFragment() {
    }

    private final String[] loginScope = new String[] {
            VKScope.PHOTOS
    };

    private Button btnLogin;
    private Button btnLogout;
    private Button btnReturn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        btnLogout = (Button) view.findViewById(R.id.btnLogout);
        btnReturn =  (Button) view.findViewById(R.id.btnReturn);

        btnLogout.setOnClickListener(this);
        btnReturn.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        if (VKSdk.isLoggedIn()) {
            btnLogin.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);
            btnReturn.setVisibility(View.VISIBLE);
        } else {
            btnLogin.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);
            btnReturn.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                VKSdk.login(getActivity(), loginScope);
                break;
            case R.id.btnLogout:
                VKSdk.logout();
                ((MainActivity)getActivity()).onLogoutButtonPressed();
                break;
            case R.id.btnReturn:
                ((MainActivity)getActivity()).onReturnButtonPressed();
                break;

        }
    }
}

