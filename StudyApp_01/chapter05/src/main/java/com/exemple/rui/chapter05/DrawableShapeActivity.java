package com.exemple.rui.chapter05;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DrawableShapeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_01;
    private View btn_02;
    private View v_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable_shape);

        bind();
    }

    private void bind(){
        v_content = findViewById(R.id.v_content);
        btn_01 = findViewById(R.id.btn_01);
        btn_02 = findViewById(R.id.btn_02);

        btn_01.setOnClickListener(this);
        btn_02.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_01:
                v.setBackgroundResource(R.drawable.shape_oval_rose);
                v_content.setBackgroundResource(R.drawable.shape_oval_rose);
                break;
            case R.id.btn_02:
                v.setBackgroundResource(R.drawable.shape_rect_gold);
                v_content.setBackgroundResource(R.drawable.shape_rect_gold);
                break;
        }
    }
}