package com.rui.workdemo2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class D5 extends AppCompatActivity {

    private Button btn_park_quit;
    private TextView tv_connect_info;
    private ImageView iv_icon_return;

    private View view_custom;
    private Context mContext;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d5);
        mContext = this;
        //视图绑定
        setBind();
        //视图监听
        setListener();

        //初始化Builder
        builder = new AlertDialog.Builder(mContext);
        //加载自定义的那个View,同时设置下
        final LayoutInflater inflater =getLayoutInflater();
        view_custom = inflater.inflate(R.layout.dialog_park_quit, null,false);
        builder.setView(view_custom);
        builder.setCancelable(false);
        alert = builder.create();

        view_custom.findViewById(R.id.dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        view_custom.findViewById(R.id.dialog_quit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
    }


    //视图绑定
    private void setBind(){

        iv_icon_return = findViewById(R.id.iv_icon_return);
        tv_connect_info = findViewById(R.id.tv_connect_info);
        btn_park_quit = findViewById(R.id.btn_park_quit);
    }

    //设置监听
    private void setListener(){
        if(null!=iv_icon_return){
            iv_icon_return.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        if(null!=btn_park_quit){
            btn_park_quit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.show();
                }
            });
        }
    }
}