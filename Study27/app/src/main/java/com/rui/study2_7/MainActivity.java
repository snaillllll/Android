package com.rui.study2_7;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    TextView tv_alert;
    Button btn_alert;
    private DatePicker dp_date;
    private TextView tv_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_alert = findViewById(R.id.btn_alert);
        tv_alert = findViewById(R.id.tv_alert);

        btn_alert.setOnClickListener(this);

        findViewById(R.id.btn_ok).setOnClickListener(this);
        dp_date = findViewById(R.id.dp_date);
        tv_date = findViewById(R.id.tv_date);

        findViewById(R.id.btn_date).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ok:
                String desc=String.format("你选择了%d年%d月%d日",
                        dp_date.getYear(),dp_date.getMonth()+1,dp_date.getDayOfMonth());
                tv_date.setText(desc);
                break;
            case R.id.btn_alert:{
                //创建提醒对话框的建造器
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                //设置对话框的标题文本
                builder.setTitle("尊敬的用户");
                //设置对话框的内容文本
                builder.setMessage("你真的要卸载吗？");
                //设置对话框的否定按钮及其监听器
                builder.setNegativeButton("我再想想", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_alert.setText("讓我在陪你三百六十五天");
                    }
                });
                //设置对话框的肯定按钮及其监听器
                builder.setPositiveButton("殘忍卸載",(dialog,witch)->{
                    tv_alert.setText("是时候离开了");
                });
                //根据建造器构建提醒对话框对象
                AlertDialog dialog = builder.create();
                // 显示提醒对话框
                dialog.show();
                break;
            }
            case R.id.btn_date:{
                DatePickerDialog dialog = new DatePickerDialog(this, this, 2077, 10, 14);
                dialog.show();

                break;
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String desc=String.format("你选择了%d年%d月%d日",
               year,month+1,dayOfMonth);
        tv_date.setText(desc);
    }
}