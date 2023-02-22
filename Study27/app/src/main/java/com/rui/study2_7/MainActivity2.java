package com.rui.study2_7;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rui.study2_7.viewutil.ViewUtil;

import java.util.Random;

public class MainActivity2 extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private RadioGroup rg_login;
    private EditText et_password;
    private EditText et_phone;
    private TextView tv_password;
    private Button btn_forget_password;
    private CheckBox cb_remember_pwd;
    private Button btn_login;
    private RadioButton rb_password;
    private RadioButton rb_verifycode;
    private ActivityResultLauncher<Intent> register;
    private String mPassword="111111";
    private String mVerifyCode;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //登录方式
        rg_login = findViewById(R.id.rg_login);
        rg_login.setOnCheckedChangeListener(this);

        rb_password = findViewById(R.id.rb_password);
        rb_verifycode = findViewById(R.id.rb_verifycode);

        //登录信息
        et_phone = findViewById(R.id.et_phone);
        et_password = findViewById(R.id.et_password);
        tv_password = findViewById(R.id.tv_password);
        btn_forget_password = findViewById(R.id.btn_forget_password);
        cb_remember_pwd = findViewById(R.id.cb_remember_password);
        btn_login = findViewById(R.id.btn_login);

        //给et_phone添加文本变更监听器
        et_phone.addTextChangedListener(new HideTextWatcher(et_phone, 11));
        //给et_password添加文本变更监听器
        et_password.addTextChangedListener(new HideTextWatcher(et_password, 6));

        btn_forget_password.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        //得到一个register ，可以跳转页面
        register = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent intent = result.getData();
                if(intent!=null&&result.getResultCode()== Activity.RESULT_OK){
                    mPassword=intent.getStringExtra("new_password");
                }
            }
        });

        //取得preference实例
        pref = getSharedPreferences("config", Context.MODE_PRIVATE);
        //加载preference中的数据
        reload();

    }

    private void reload() {
        Long phone=pref.getLong("phone",0l);
        int pwd=pref.getInt("pwd",0);
        Boolean rem=pref.getBoolean("rem",false);
        if(phone!=0l){
            et_phone.setText(phone.toString());
        }
        if(pwd!=0){
            et_password.setText(pwd+"");
        }
        cb_remember_pwd.setChecked(rem);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_password:
                tv_password.setText("登录密码：");
                et_password.setHint("输入密码");
                btn_forget_password.setText("忘记密码");
                cb_remember_pwd.setVisibility(View.VISIBLE);

                break;
            case R.id.rb_verifycode:
                tv_password.setText("验  证  码：");
                et_password.setHint("输入验证码");
                btn_forget_password.setText("获取验证码");
                cb_remember_pwd.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        String phone = et_phone.getText().toString();
        //检查手机号的正确性
        if (phone.length() < 11) {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            //tv_password.setText("经过");
            return;
        }
        switch (v.getId()) {
            case R.id.btn_forget_password:
                //选择了密码方式，此时是点击了忘记密码
                if (rb_password.isChecked()) {
                    //一下携带手机号码跳转到找回密码页面
                    Intent intent = new Intent(this, MainActivity3.class);
                    intent.putExtra("phone", phone);
                    register.launch(intent);
                } else if (rb_verifycode.isChecked()) {
                    //生成六位数随机数字的验证码
                    mVerifyCode = String.format("%06d",new Random().nextInt(999999));
                    //以下弹出提醒对话框，提示用户记住六位验证码数字
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("记住验证码");
                    builder.setMessage("本次验证码是：" + mVerifyCode);
                    builder.setPositiveButton("好的",null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                break;
            case R.id.btn_login:
                //密码方式
                if(rb_password.isChecked()){
                    if(!mPassword.equals(et_password.getText().toString())){
                        Toast.makeText(this, "请输入正确的密码", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //记住密码
                    SharedPreferences.Editor edit = pref.edit();
                    if(cb_remember_pwd.isChecked()){
                        //String phone=et_phone.getText().toString();
                        String pwd=et_password.getText().toString();
                        edit.putLong("phone",Long.parseLong(phone));
                        edit.putInt("pwd",Integer.parseInt(pwd));
                        edit.putBoolean("rem",true);
                        edit.commit();
                        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                    }else{
                        edit.putLong("phone",0l);
                        edit.putInt("pwd",0);
                        edit.putBoolean("rem",false);
                        edit.commit();
                    }
                    //提示登陆成功
                    loginSuccess();
                }else if(rb_verifycode.isChecked()){
                    //验证码方式校验
                    if(!mVerifyCode.equals(et_password.getText().toString())){
                        Toast.makeText(this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //提示登陆成功
                    loginSuccess();
                }
                break;
        }
    }

    private void loginSuccess() {
        String desc = String.format("登陆成功");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("成功");
        builder.setMessage(desc);
        builder.setPositiveButton("确定",null);
        builder.create().show();
    }

    private class HideTextWatcher implements TextWatcher {
        private EditText mView;
        private int mMaxLength;

        public HideTextWatcher(EditText v, int maxLength) {
            this.mView = v;
            this.mMaxLength = maxLength;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() == mMaxLength) {
                ViewUtil.hideOneInputMethod(MainActivity2.this, mView);
            }
        }
    }
}