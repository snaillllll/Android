package com.exemple.rui.chapter03;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.exemple.rui.chapter03.util.DateUtil;

public class TextViewActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_result2;
    private Button btn_click_single;
    private Button btn_click_public;
    private Button btn_long_click;
    private Button btn_on;
    private Button btn_off;
    private Button btn_run;
    private TextView tv_result3;
    private Button btn_fanhui;
    private TextView tv_receive;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_button);
        tv_result=findViewById(R.id.tv_result);
        tv_result2 = findViewById(R.id.tv_result2);
        btn_click_single = findViewById(R.id.btn_click_single);
        btn_click_single.setOnClickListener(new MyOnClickListener(tv_result2));
        btn_click_public = findViewById(R.id.btn_click_public);
        btn_click_public.setOnClickListener(this);
        btn_long_click = findViewById(R.id.btn_long_click);
        btn_long_click.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String desc = String.format("%s 您点击了按钮 %s", DateUtil.getNowTime(),((Button)v).getText());
                tv_result2.setText(desc);
                return true;
            }
        });
        btn_on = findViewById(R.id.btn_on);
        btn_off = findViewById(R.id.btn_off);
        btn_run = findViewById(R.id.btn_run);
        tv_result3 = findViewById(R.id.tv_result3);
        btn_fanhui = findViewById(R.id.btn_fanhui);
        btn_fanhui.setOnClickListener(this);
        btn_on.setOnClickListener(this);
        btn_off.setOnClickListener(this);
        btn_run.setOnClickListener(this);
        tv_receive = findViewById(R.id.tv_receive);


        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            String time = bundle.getString("time");
            String text = bundle.getString("text");
            String desc=String.format("接收到Intent消息：\n时间：%s\n消息：%s",time,text);
            tv_receive.setText(desc);
        }

    }

    //onClick 属性 绑定的 doClick方法
    TextView tv_result;
    public void doClick(View view){
        String desc = String.format("%s 您点击了按钮 %s", DateUtil.getNowTime(),((Button)view).getText());
        tv_result.setText(desc);
    }

    //自己实现 监听器类
    static class MyOnClickListener implements View.OnClickListener{
        private final TextView tv_result;
        public MyOnClickListener(TextView tv_result2) {
            this.tv_result=tv_result2;
        }
        @Override
        public void onClick(View v) {
            String desc = String.format("%s 您点击了按钮 %s", DateUtil.getNowTime(),((Button)v).getText());
            tv_result.setText(desc);
        }
    }

    //也是自己实现 ，但是 直接使用activity 类去实现 ，重写onclick方法
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_click_public){
            String desc = String.format("%s 您点击了按钮 %s", DateUtil.getNowTime(),((Button)v).getText());
            tv_result2.setText(desc);
        }else if(v.getId()==R.id.btn_on){
            if(btn_run.isEnabled()==false){
                btn_run.setEnabled(true);
                btn_run.setTextColor(Color.RED);
            }
        }else if(v.getId()==R.id.btn_off){
            if(btn_run.isEnabled()==true){
                btn_run.setEnabled(false);
                btn_run.setTextColor(Color.GRAY);
            }
        }else if(v.getId()==R.id.btn_run){
            String desc = String.format("%s 您点击了按钮 %s", DateUtil.getNowTime(),((Button)v).getText());
            tv_result3.setText(desc);
        }else if(v.getId()==R.id.btn_fanhui){
            startActivity(new Intent(this,MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
    }
}
