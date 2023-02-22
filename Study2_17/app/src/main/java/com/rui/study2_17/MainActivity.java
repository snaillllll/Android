package com.rui.study2_17;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String mDatabaseName;
    private TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_create = findViewById(R.id.btn_create);
        Button btn_drop = findViewById(R.id.btn_drop);
        tv_result = findViewById(R.id.tv_result);

        mDatabaseName = getFilesDir()+"/test.db";

        btn_create.setOnClickListener(this);
        btn_drop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_create:
                SQLiteDatabase db = openOrCreateDatabase(mDatabaseName, Context.MODE_PRIVATE, null);
                String desc=String.format("数据库%s创建%s",db.getPath(),(db!=null)?"成功":"失败");
                tv_result.setText(desc);
                break;
            case R.id.btn_drop:
                boolean is = deleteDatabase(mDatabaseName);
                desc = String.format("数据库%s删除%s", mDatabaseName, is ? "成功" : "失败");
                tv_result.setText(desc);
                break;
        }
    }
}