package com.rui.workdemo2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rui.workdemo2.util.ToastUtil;

public class D5 extends AppCompatActivity {

    private Button btn_top_01;//左上角返回
    private Button btn_exc_01;//异常可恢复
    private Button btn_exc_02;//异常不可恢复
    private Button btn_conn;//正常连接
    private Button btn_conn_false;//连接失败

    private TextView tv_bottom_04;//初始可见  ，text=正在连接车辆...
    private TextView tv_bottom_03;//初始不可见，text=请选择当前泊车类型
    private Button btn_bottom_02;//底部自动泊入，初始不可见
    private Button btn_bottom_01;//底部退出泊车，还负责自动泊出



    private View view_custom;
    private View view_custom2;
    private Context mContext;
    private AlertDialog alert = null;
    private AlertDialog alert2 = null;
    private AlertDialog.Builder builder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rpa_d5_02);
        mContext = this;
        //dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        //视图绑定
        setBind();
        //视图监听
        setListener();


    }


    //视图绑定
    private void setBind(){

        btn_top_01 = findViewById(R.id.btn_top_01);
        btn_bottom_01 = findViewById(R.id.btn_bottom_01);
        btn_bottom_02 = findViewById(R.id.btn_bottom_02);
        tv_bottom_03=findViewById(R.id.tv_bottom_03);
        tv_bottom_04=findViewById(R.id.tv_bottom_04);

        btn_exc_01=findViewById(R.id.btn_exc_01);
        btn_exc_02=findViewById(R.id.btn_exc_02);
        btn_conn=findViewById(R.id.btn_conn);
        btn_conn_false=findViewById(R.id.btn_conn_false);

        //对自定义AlertDialog进行视图绑定
        //初始化Builder
        builder = new AlertDialog.Builder(mContext);
        //加载自定义的那个View,同时设置下
        final LayoutInflater inflater =getLayoutInflater();
        view_custom = inflater.inflate(R.layout.dialog_park_quit, null,false);
        builder.setView(view_custom);
        builder.setCancelable(false);
        alert = builder.create();
        alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
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


        //加载另一个弹出框
        view_custom2 = inflater.inflate(R.layout.dialog_park_out2in, null, false);
        builder.setView(view_custom2);
        alert2=builder.create();
        alert2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        view_custom2.findViewById(R.id.tv_dialog_out2in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert2.dismiss();
            }
        });

    }

    //设置监听
    private void setListener(){
        //左上角返回
        if(null!=btn_top_01){
            btn_top_01.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.show();
                }
            });
        }
        //底部退出泊车按钮，也是自动泊出按钮，返回首页
        if(null!=btn_bottom_01){
            btn_bottom_01.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str=((Button)v).getText().toString();
                    if("退出泊车".equals(str)){ //按钮是 退出泊车时
                        alert.show();
                    }else if("自动泊出".equals(str)){//按钮是 自动泊出时
                        alert2.show();
                    }else if("返回首页".equals(str)){//按钮是 返回首页时

                    }else{
                        Toast.makeText(mContext, "未知错误", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        //底部自动泊入按钮，也是重新连接按钮
        if(null!=btn_bottom_02){
            btn_bottom_02.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str=((Button)v).getText().toString();
                    if("重新连接".equals(str)){

                    }else if("自动泊入".equals(str)){

                    }
                }
            });
        }

        //模拟异常
        if(null!=btn_exc_01){
            btn_exc_01.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.makeText(mContext,"遇到异常，请稍后重试",Toast.LENGTH_LONG).show();
                }
            });
        }
        if(null!=btn_exc_02){
            btn_exc_02.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.makeText(mContext,"遇到异常，遥控泊车已退出",Toast.LENGTH_LONG).show();
                }
            });
        }
        //模拟连接成功
        if(null!=btn_conn){
            btn_conn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //修改可见性
                    tv_bottom_04.setVisibility(View.GONE);
                    tv_bottom_03.setVisibility(View.VISIBLE);
                    btn_bottom_02.setVisibility(View.VISIBLE);
                    //文本填充
                    tv_bottom_03.setText("请选择当前泊车类型");
                    btn_bottom_02.setText("自动泊入");
                    btn_bottom_01.setText("自动泊出"); //将btn_bottom_01 的Text变更为"自动泊出"
                }
            });
        }
        //模拟连接失败
        if(null!=btn_conn_false){
            btn_conn_false.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //提示框
                    ToastUtil.makeText(mContext,"车辆连接失败，请稍后重试",Toast.LENGTH_LONG).show();
                    //修改可见性
                    tv_bottom_04.setVisibility(View.INVISIBLE);
                    tv_bottom_03.setVisibility(View.VISIBLE);
                    btn_bottom_02.setVisibility(View.VISIBLE);
                    //修改文本
                    tv_bottom_03.setText("连接车辆失败");
                    btn_bottom_02.setText("重新连接");
                    btn_bottom_01.setText("返回首页");
                }
            });
        }
    }
}