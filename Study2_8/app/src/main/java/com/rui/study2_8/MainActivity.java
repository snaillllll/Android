package com.rui.study2_8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_commit;
    private EditText et_weight;
    private EditText et_height;
    private EditText et_name;
    private EditText et_age;
    private SharedPreferences preferences;
    private CheckBox cb_marry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);
        et_height = findViewById(R.id.et_height);
        et_weight = findViewById(R.id.et_weight);
        btn_commit = findViewById(R.id.btn_commit);
        cb_marry = findViewById(R.id.cb_marry);
        preferences = getSharedPreferences("config",Context.MODE_PRIVATE);
        reload();


        btn_commit.setOnClickListener(this);

    }

    private void reload() {
        String name=preferences.getString("name","");
        int age = preferences.getInt("age", 0);
        float height = preferences.getFloat("height", 0f);
        float weight = preferences.getFloat("weight", 0f);
        boolean marry = preferences.getBoolean("marry", false);
        if(name!=null){
            et_name.setText(name);
        }
        if(age!=0){
            et_age.setText(String.valueOf(age));
        }
        if(height!=0f){
            et_height.setText(String.valueOf(height));
        }
        if(weight!=0f){
            et_weight.setText(String.valueOf(weight));
        }
        cb_marry.setChecked(marry);
    }

    @Override
    public void onClick(View v) {
        String name = et_name.getText().toString();
        String age = et_age.getText().toString();
        String height = et_height.getText().toString();
        String weight = et_weight.getText().toString();


        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name",name);
        editor.putInt("age",Integer.parseInt(age));
        editor.putFloat("height",Float.parseFloat(height));
        editor.putFloat("weight",Float.parseFloat(weight));
        editor.putBoolean("marry",cb_marry.isChecked());
        editor.commit();
    }
}