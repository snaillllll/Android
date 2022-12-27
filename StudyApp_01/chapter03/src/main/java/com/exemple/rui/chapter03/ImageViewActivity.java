package com.exemple.rui.chapter03;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ImageViewActivity extends AppCompatActivity {

    private ImageView iv_01;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_image);
        iv_01 = findViewById(R.id.iv_01);
        iv_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_01.setImageResource(R.drawable.dog);
            }
        });
    }
}
