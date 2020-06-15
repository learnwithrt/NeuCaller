package com.neusoft.neucaller.ui.logs;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.neusoft.neucaller.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CallLogsFragment extends Fragment {

    private CallLogsViewModel dashboardViewModel;
    private ListView mCalls;
    private ArrayList<String> mLogs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(CallLogsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_logs, container, false);
        mCalls = root.findViewById(R.id.call_list);
        mLogs = readLogs();
        if(mLogs!=null){
            ArrayAdapter<String> adapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,
                    mLogs);
            mCalls.setAdapter(adapter);
        }
        return root;
    }

    private ArrayList<String> readLogs() {
        ArrayList<String> logs = new ArrayList<>();
        //Read the call log from the device
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        Cursor c = getContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null,
                CallLog.Calls.DATE + " DESC");
        if(c.getCount()>0){//check if any logs are available
            c.moveToFirst();
            do{
                String callerNumber=c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
                int callType=c.getInt(c.getColumnIndex(CallLog.Calls.TYPE));
                //GET INFORMATION LIKE DATE TIME OF CALL, DURATION, CALLER ID
                switch(callType){
                    case CallLog.Calls.INCOMING_TYPE:{
                        logs.add("Incoming Call from "+callerNumber);
                        break;
                    }
                    case CallLog.Calls.OUTGOING_TYPE:{
                        logs.add("Outgoing Call to "+callerNumber);
                        break;
                    }
                    case CallLog.Calls.MISSED_TYPE:{
                        logs.add("Missed Call from "+callerNumber);
                        break;
                    }
                }
            }while(c.moveToNext());//until a next log is available
        }
        return logs;
    }
}