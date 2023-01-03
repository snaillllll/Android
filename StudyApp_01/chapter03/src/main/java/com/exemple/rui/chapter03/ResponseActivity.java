package com.exemple.rui.chapter03;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResponseActivity extends AppCompatActivity {

    private String yes_no;
    private TextView tv_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);

        Bundle bundle = getIntent().getExtras();
        yes_no = bundle.getString("tv_yes_no");
        tv_show = findViewById(R.id.tv_show);
        if ("yes".equals(yes_no)) {
            tv_show.setText("传递来了 yes");
        } else {
            tv_show.setText("传递来了 no");
        }
        findViewById(R.id.btn_01).setOnClickListener(
                v -> {
                    bundle.putString("response","不知道传什么");
                    Intent intent=new Intent();
                    intent.putExtras(bundle);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
        );
    }
}