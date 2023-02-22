package com.rui.me;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnFocusChangeListener {


    private EditText et_zh;
    private EditText et_cs;
    private TextView tv_result;
    private Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_zh = findViewById(R.id.et_zh);
        et_cs = findViewById(R.id.et_cs);
        tv_result = findViewById(R.id.tv_result);
        btn_submit = findViewById(R.id.btn_submit);

        et_zh.setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus){
            String zh=et_zh.getText().toString();
            if(TextUtils.isEmpty(zh)||zh.length()<4){
                //et_zh.requestFocus();
                tv_result.setText("结果：提前不通过");
                et_cs.clearFocus();
                Toast.makeText(this, "规范输入", Toast.LENGTH_SHORT).show();
            }else{
                tv_result.setText("结果：可能通过");
            }
        }
    }
}