package com.exemple.rui.chapter03;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.exemple.rui.chapter03.databinding.ActivityRequestBinding;

public class RequestActivity extends AppCompatActivity {

    private TextView tv_yes_no;
    private TextView tv_response;
    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        tv_yes_no = findViewById(R.id.tv_yes_no);
        tv_response = findViewById(R.id.tv_response);
        tv_yes_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("yes".equals(((TextView) v).getText())){
                    ((TextView) v).setText("no");
                }else{
                    ((TextView) v).setText("yes");
                }
            }
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result!=null){
                            Intent intent = result.getData();
                            Bundle extras = intent.getExtras();
                            String desc = extras.getString("response");
                            tv_response.setText(desc);
                        }
                    }
                }

        );


        findViewById(R.id.btn_request).setOnClickListener(
                v-> {
                Bundle bundle=new Bundle();
                bundle.putString("tv_yes_no",tv_yes_no.getText().toString());
                Intent intent=new Intent(this,ResponseActivity.class);
                intent.putExtras(bundle);
                launcher.launch(intent);
            }
        );
    }

}