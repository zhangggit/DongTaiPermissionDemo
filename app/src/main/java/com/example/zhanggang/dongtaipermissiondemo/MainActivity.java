package com.example.zhanggang.dongtaipermissiondemo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 请求权限和打开相机
     *
     * @param view
     */
    public void onClick(View view) {
        //检测是否授权
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "检测，用户已授权相机权限。。。");
            startCamera(); //打开相机
        } else {
            // 参数二:权限不一定只有一个，所以是个数组
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100); //设值为100
            Log.e(TAG, "检测，用户没有相机权限。。。");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //第二个参数:是权限的数组  第三个参数:是用户是否同意授权的值   两个数组是对应的
        switch (requestCode) {
            case 100: //回调判断设置的值
                if (permissions[0].equals(Manifest.permission.CAMERA)) {  //判断权限数组下标0 是否是相机权限
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { //判断用户是否授权的数组 判断是否授权
                        Log.e(TAG, "权限回调，用户同意了授权。。");
                        startCamera();
                    } else {

                        Log.e(TAG, "权限回调，用户拒绝了授权。。");
                        /**
                         * 如果用户勾选了不再提示，系统就不会再解释权限了（系统不解释权限，我们自己解释）
                         */
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) { //判断用户是否勾选了不再提示
                            Log.e(TAG, "shouldShowRequestPermissionRationale返回值为true");
                            //返回true因为系统刚刚有提示，所以不用自己提示，直接跳转设置页
                            showGoSetting();
                        } else {
                            Log.e(TAG, "shouldShowRequestPermissionRationale返回值为false");
                            //返回false用户勾选了不再提示，系统不会再弹出对话框提示，所以要自己提示
                            showGoJieShi();
                        }

                    }
                }
                break;
        }
    }

    //弹出提示窗  显示提示信息
    private void showGoJieShi() {
        new AlertDialog.Builder(this)
                .setTitle("说明")
                .setMessage("需要打开相机权限，才能打开相机")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showGoSetting();  //跳转设置权限页
                    }
                }).setNegativeButton("取消", null)
                .show();
    }

    //跳转设置权限页
    private void showGoSetting() {
        new AlertDialog.Builder(this)
                .setTitle("需要打开相机权限")
                .setMessage("在手机--设置--权限--中打开权限")
                .setPositiveButton("立即设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        settingMethod(); //跳转设置界面
                    }
                }).setNegativeButton("取消", null)
                .show();
    }

    //跳转到设置权限页面
    private void settingMethod() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 10); //回调值为10
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG, "设置页面返回后--再次检测权限--用户已授权。");
                    startCamera(); //已经授权 打开相机
                } else {
                    Log.e(TAG, "设置页面返回后--再次检测权限--用户已拒绝。");
                }
                break;
        }

    }

    //打开相机方法
    private void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    //打电话 看log日志 在一个危险权限表单中的危险权限，有一个呗=被授权，这个表单都将被授权
    public void call(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "检测权限，用户已拥有打电话权限。。。");
        } else {
            Log.e(TAG, "检测权限，用户未授权打电话权限。。。");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1000);
        }
    }

    public void readstate(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "检测权限，用户已拥有读取电话权限。。。");
        } else {
            Log.e(TAG, "检测权限，用户未授权读取电话权限。。。");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1000);
        }
    }
}
