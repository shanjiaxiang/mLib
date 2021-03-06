package com.mit.mylib.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mit.mylib.base.dialog.MSweetDialog;
import com.mit.mylib.eventbus.MEvent;
import com.mit.mylib.util.MPermissionUtil;
import com.tbruyelle.rxpermissions.Permission;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.functions.Action1;

/**
 * Created by shanjiaxiang on 2018\11\30 0030.
 */

public abstract class MBaseActivity extends AppCompatActivity {

    public Context mContext = null;
    public Activity mActivity = null;
    public View mEmptyView;

    public MBaseActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        this.mActivity = this;
        MAppManager.getInstance().addActivity(this);

    }

    public <T extends View> T myFindViewById(int resId) {
        return this.findViewById(resId);
    }

    public void requestPermissions(String... permissions) {
        MPermissionUtil.getRxPermission(this)
                .requestEach(permissions)
                .subscribe(new Action1<Permission>() {
                    @Override
                    public void call(Permission permission) {
                        if (permission.granted) {
                            MBaseActivity.this.onPermissionGranted(permission);
                            MLog.d(this, "同意：" + permission.name);
                        } else {
                            MBaseActivity.this.onPermissionDenied(permission);
                            MLog.d(this, "拒绝:" + permission.name);
                        }
                    }
                });
    }

    public boolean checkPermission(String permission) {
        return MPermissionUtil.checkPermission(this, permission);
    }

    private void onPermissionDenied(Permission permission) {
    }

    private void onPermissionGranted(Permission permission) {
    }

    public abstract void myFindView();

    protected void myOnClick() {
    }

    protected void showDialog(String msg) {
        MSweetDialog.showDialog(this, msg);
    }

    protected void showDialog(String msg, SweetAlertDialog.OnSweetClickListener confirmListener) {
        MSweetDialog.showDialog(this, msg, confirmListener, (SweetAlertDialog.OnSweetClickListener) null);
    }

    protected void showNetErrorDialog() {
        MSweetDialog.showNetErrorDialog(this, (SweetAlertDialog.OnSweetClickListener) null);
    }

    protected void showNetErrorDialog(SweetAlertDialog.OnSweetClickListener confirmListener) {
        MSweetDialog.showNetErrorDialog(this, confirmListener);
    }

    protected void dismissDialog() {
        MSweetDialog.dismissDialog();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    //处理函数的线程在主线程(UI)线程
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMEventMainThread(MEvent event) {

    }

    //事件处理函数的线程在后台线程，因此不能进行UI操作.如果发布事件的线程是主线程(UI线程)，
    // 那么事件处理函数将会开启一个后台线程，如果果发布事件的线程是在后台线程，那么事件处理函数就使用该线程
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMEventBackgroundEvent(MEvent event) {

    }

    //处理函数的线程跟发布事件的线程在同一个线程
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMEventPostThread(MEvent event) {

    }

    //表示无论事件发布的线程是哪一个，事件处理函数始终会新建一个子线程运行，
    // 同样不能进行UI操作
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMEventAsyncThread(MEvent event) {

    }
}
