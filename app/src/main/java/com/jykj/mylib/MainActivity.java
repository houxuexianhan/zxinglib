package com.jykj.mylib;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.jykj.zxinglib.android.CaptureActivity;
import com.jykj.zxinglib.bean.ZxingConfig;
import com.jykj.zxinglib.common.Constant;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private int REQUEST_CODE_SCAN = 111;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnScan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan();
            }
        });
    }
    private void scan(){
        AndPermission.with(this)
                .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                        /*ZxingConfig是配置类
                         *可以设置是否显示底部布局，闪光灯，相册，
                         * 是否播放提示音  震动
                         * 设置扫描框颜色等
                         * 也可以不传这个参数
                         * */
                        ZxingConfig config = new ZxingConfig();
                        config.setPlayBeep(true);//是否播放扫描声音 默认为true
                        config.setShake(true);//是否震动  默认为true
                        config.setDecodeBarCode(true);//是否扫描条形码 默认为true
                        config.setReactColor(R.color.colorWhite);//设置扫描框四个角的颜色 默认为淡蓝色
                        config.setFrameLineColor(R.color.colorWhite);//设置扫描框边框颜色 默认无色
                        config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
                        config.setTitle("微信扫码");
                        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                        startActivityForResult(intent, REQUEST_CODE_SCAN);
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        Uri packageURI = Uri.parse("package:" + getPackageName());
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);

                        Toast.makeText(MainActivity.this, "没有权限无法扫描呦", Toast.LENGTH_LONG).show();
                    }
                }).start();
    }
}
