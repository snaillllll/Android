package com.rui.me;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv_01 = findViewById(R.id.tv_01);
        tv_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_01.setText("fuck you");
                System.out.println("执行了监听事件");
                tv_01.setTextColor(Color.parseColor("#0000ff"));
            }
        });
    }
}