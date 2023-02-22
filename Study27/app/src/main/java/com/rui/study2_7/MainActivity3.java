package com.rui.study2_7;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rui.study2_7.viewutil.ViewUtil;

import java.util.Random;

public class MainActivity3 extends AppCompatActivity implements View.OnClickListener {

    private EditText et_new_pwd;
    private EditText et_confirm;
    private EditText et_ver_code;
    private Button btn_get_code;
    private Button btn_confirm;
    private String mVerifyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        et_new_pwd = findViewById(R.id.et_new_pwd);
        et_confirm = findViewById(R.id.et_confirm);
        et_ver_code = findViewById(R.id.et_ver_code);
        btn_get_code = findViewById(R.id.btn_get_code);
        btn_confirm = findViewById(R.id.btn_confirm);

        et_new_pwd.addTextChangedListener(new HideTextWatcher(et_new_pwd,6));
        et_confirm.addTextChangedListener(new HideTextWatcher(et_confirm,6));
        et_ver_code.addTextChangedListener(new HideTextWatcher(et_ver_code,6));
        btn_get_code.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_get_code:
                //生成六位数随机数字的验证码
                mVerifyCode = String.format("%06d",new Random().nextInt(999999));
                //以下弹出提醒对话框，提示用户记住六位验证码数字
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("记住验证码");
                builder.setMessage("本次验证码是：" + mVerifyCode);
                builder.setPositiveButton("好的",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
            case R.id.btn_confirm:

                if(mVerifyCode!=null&&mVerifyCode.equals(et_ver_code.getText().toString())
                &&et_new_pwd.getText().toString().equals(et_confirm.getText().toString())){
                    Toast.makeText(this, "验证成功", Toast.LENGTH_SHORT).show();
                    //以下把修改号的新密码返回给上一个页面
                    Intent intent=new Intent();
                    intent.putExtra("new_password",et_new_pwd.getText().toString());
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                    break;

                }else{
                    Toast.makeText(this, "验证失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private class HideTextWatcher implements TextWatcher {
        private EditText mView;
        private int mMaxLength;
        public HideTextWatcher(EditText v, int maxLength) {
            this.mMaxLength=maxLength;
            this.mView=v;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.toString().length()==mMaxLength){
                ViewUtil.hideOneInputMethod(MainActivity3.this,mView);

                if(mView.getId()==R.id.et_confirm){
                    if(!et_new_pwd.getText().toString().equals(et_confirm.getText().toString())){
                        Toast.makeText(MainActivity3.this,
                                "两次输入不一致，第一次输入为"+et_new_pwd.getText(),
                                Toast.LENGTH_SHORT).show();
                    }
                }if(mView.getId()==R.id.et_ver_code){
                    if(!et_ver_code.getText().toString().equals(mVerifyCode)){
                        Toast.makeText(MainActivity3.this,
                                "验证码错误，验证码为"+mVerifyCode,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }
    }
}