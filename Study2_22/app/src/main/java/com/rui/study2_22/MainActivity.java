package com.rui.study2_22;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rui.study2_22.database.UserDBHelper;
import com.rui.study2_22.entity.User;
import com.rui.study2_22.util.ToastUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String dbName;
    private TextView tv_result;
    private UserDBHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_creat).setOnClickListener(this);
        findViewById(R.id.tv_delete).setOnClickListener(this);
        dbName = getFilesDir() + "/test.db";
        tv_result = findViewById(R.id.tv_result);

        findViewById(R.id.btn_add).setOnClickListener(this);
        findViewById(R.id.btn_delete).setOnClickListener(this);
        findViewById(R.id.btn_update).setOnClickListener(this);
        findViewById(R.id.btn_query).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_creat:
                SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
                String desc = String.format("数据库创建%s,路径%s", sqLiteDatabase != null ? "成功" : "失败", sqLiteDatabase.getPath());
                tv_result.setText(desc);
                break;
            case R.id.tv_delete:
                boolean b = deleteDatabase(dbName);
                desc = String.format("数据库删除%s", b ? "成功" : "失败");
                tv_result.setText(desc);
                break;
            case R.id.btn_add:
                User user = new User("jack", true);
                if (mHelper.insert(user) > 0) {
                    ToastUtil.show(this, "添加成功");
                } else {
                    ToastUtil.show(this, "添加失败");
                }
                break;
            case R.id.btn_delete:
                if (mHelper.deleteByName("jack") > 0) {
                    ToastUtil.show(this, "删除成功");
                } else {
                    ToastUtil.show(this, "删除失败");
                }
                break;
            case R.id.btn_update:
                user = new User("jack", false);
                if (mHelper.update(user) > 0) {
                    ToastUtil.show(this, "修改成功");
                } else {
                    ToastUtil.show(this, "修改失败");
                }
                break;
            case R.id.btn_query:
                List<User> list = mHelper.queryAll();
                for (User u : list) {
                    Log.d("rui",u.toString());
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //获取数据库帮助器的实例
        mHelper = UserDBHelper.getInstance(this);
        //打开数据库帮助器的读写连接
        mHelper.openWriteLink();
        mHelper.openReadLink();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //关闭数据库连接
        mHelper.closeLink();
    }
}