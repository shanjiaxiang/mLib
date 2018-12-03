package com.mit.mlib;

import android.Manifest;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mit.mylib.base.MBaseActivity;
import com.mit.mylib.base.MLog;
import com.mit.mylib.base.dialog.MSweetDialog;
import com.mit.mylib.eventbus.MEvent;
import com.mit.mylib.eventbus.MEventType;
import com.mit.mylib.util.MAppMetaData;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends MBaseActivity implements View.OnClickListener {

    private Button button1, button2, button3, button4;

    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MLog.enableLogging();
        MLog.d("Logging is enable>>>>>>>>>>>>>>>>>>>>>");

        PackageManager packageManager = this.getPackageManager();
        String resultData = null;
        if (packageManager != null) {
            ApplicationInfo applicationInfo = null;
            try {
                applicationInfo = packageManager.getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
                MLog.d("get applicationInfo");
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if (applicationInfo != null && applicationInfo.metaData != null) {
                Set <String> set = applicationInfo.metaData.keySet();
                for (String key : set) {
                    MLog.d(key);
                    resultData = (String) applicationInfo.metaData.get(key);
                    MLog.d(resultData);
                }

            } else
                MLog.d("application == null");
        }

        myFindView();

        myOnClick();
    }

    @Override
    public void myFindView() {
        button1 = myFindViewById(R.id.bt_1);
        button2 = myFindViewById(R.id.bt_2);
        button3 = myFindViewById(R.id.bt_3);
        button4 = myFindViewById(R.id.bt_4);
    }

    @Override
    protected void myOnClick() {
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_1:
                break;
            case R.id.bt_2:


                break;
            case R.id.bt_3:

                break;
            case R.id.bt_4:

                break;
        }
    }
}
