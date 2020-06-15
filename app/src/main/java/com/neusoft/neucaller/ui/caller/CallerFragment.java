package com.neusoft.neucaller.ui.caller;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.neusoft.neucaller.R;

public class CallerFragment extends Fragment {

    private CallerViewModel homeViewModel;
    private EditText mPhone;
    private Button mCallButton;
    private final String[] PERMISSIONS={
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.WRITE_CALL_LOG
    };
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(CallerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_caller, container, false);
        mPhone=root.findViewById(R.id.phone_number);
        mCallButton=root.findViewById(R.id.call_button);
        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callNumber();
            }
        });
                return root;
    }

    private void callNumber() {
        String phone=mPhone.getText().toString();
        //To call I will create an intent which says you want to make a call
        Intent intentToCall=new Intent(Intent.ACTION_CALL);
        intentToCall.setData(Uri.parse("tel:"+phone));//convert this string to URI
        //WHAT TO DO IF THE APP DOESN'T HAVE PERMISSION TO MAKE A CALL
        if(ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),PERMISSIONS,1234);
        }
        startActivity(intentToCall);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1234){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                return;
            }
        }
    }
}