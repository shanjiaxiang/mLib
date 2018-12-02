package com.mit.mlib;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mit.mylib.base.MBaseActivity;
import com.mit.mylib.base.MLog;
import com.mit.mylib.base.dialog.MSweetDialog;
import com.mit.mylib.eventbus.MEvent;
import com.mit.mylib.eventbus.MEventType;

import org.greenrobot.eventbus.EventBus;

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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MLog.d("bt1" + Thread.currentThread().getName() + ":" + Thread.currentThread().getId());
                        EventBus.getDefault().post(new MEvent(MEventType.MAIN_THREAD, "子线程发送"));
                    }
                }, "子线程1").start();
                break;
            case R.id.bt_2:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MLog.d("bt2" + Thread.currentThread().getName() + ":" + Thread.currentThread().getId());
                        EventBus.getDefault().post(new MEvent(MEventType.BACKGROUND, "主线程发送"));
                    }
                }, "子线程2").start();


                break;
            case R.id.bt_3:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MLog.d("bt3" + Thread.currentThread().getName() + ":" + Thread.currentThread().getId());
                        EventBus.getDefault().post(new MEvent(MEventType.POSTING, "子线程发送"));
                    }
                }, "子线程2").start();


                break;
            case R.id.bt_4:
                MLog.d("bt4" + Thread.currentThread().getName() + ":" + Thread.currentThread().getId());
                EventBus.getDefault().post(new MEvent(MEventType.ASYNC, "主线程发送bt4"));
                break;
        }
    }

    @Override
    public void onMEventMainThread(MEvent event) {
        super.onMEventMainThread(event);
        if (event.mEventType.equals(MEventType.MAIN_THREAD)) {
            MLog.d("After eventbus:" + Thread.currentThread().getName() + ":" + Thread.currentThread().getId());
        }

    }

    @Override
    public void onMEventAsyncThread(MEvent event) {
        super.onMEventAsyncThread(event);
        if (event.mEventType.equals(MEventType.ASYNC)) {
//            Toast.makeText(this, (String) event.mEventObject, Toast.LENGTH_SHORT).show();
            MLog.d("After eventbus:" + Thread.currentThread().getName() + ":" + Thread.currentThread().getId());
        }
    }


    @Override
    public void onMEventPostThread(MEvent event) {
        if (event.mEventType.equals(MEventType.POSTING)) {
            MLog.d("After eventbus:" + Thread.currentThread().getName() + ":" + Thread.currentThread().getId());
        }

    }

    @Override
    public void onMEventBackgroundEvent(MEvent event) {
        super.onMEventBackgroundEvent(event);
        if (event.mEventType.equals(MEventType.BACKGROUND)) {
//            Toast.makeText(this, (String) event.mEventObject, Toast.LENGTH_SHORT).show();
            MLog.d("After eventbus:" + Thread.currentThread().getName() + ":" + Thread.currentThread().getId());
        }
    }
}
