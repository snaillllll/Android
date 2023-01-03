package com.exemple.rui.chapter03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.exemple.rui.chapter03.util.DateUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_tianqi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_hello).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this,
                                TextViewActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

                    }
                });
        findViewById(R.id.btn_dial).setOnClickListener(this);
        findViewById(R.id.btn_sms).setOnClickListener(this);
        findViewById(R.id.btn_my).setOnClickListener(this);
        tv_tianqi = findViewById(R.id.tv_tianqi);
        findViewById(R.id.btn_send).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String phoneNo = "54321";
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_dial:
                //设置意图动作 准备拨号
                intent.setAction(Intent.ACTION_DIAL);
                //声明一个拨号的Uri
                Uri uri = Uri.parse("tel:" + phoneNo);
                intent.setData(uri);
                startActivity(intent);
                break;
            case R.id.btn_sms:
                //设置意图动作 准备发短信
                intent.setAction(Intent.ACTION_SENDTO);
                //声明一个发短信的Uri
                Uri uri2 = Uri.parse("smsto:" + phoneNo);
                intent.setData(uri2);
                startActivity(intent);
                break;
            case R.id.btn_my:
                //设置意图动作 准备跳转app
                intent.setAction("android.intent.action.zhang");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                startActivity(intent);
                break;
            case R.id.btn_send:
                Bundle bundle=new Bundle();
                bundle.putString("time", DateUtil.getNowTime());
                bundle.putString("text", tv_tianqi.getText().toString());
                intent.setClass(this,TextViewActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
        }
    }
}