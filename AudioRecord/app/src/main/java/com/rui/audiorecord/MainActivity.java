package com.rui.audiorecord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private boolean isRecording = false;
    //图片轮播线程
    private Thread picThread = null;


    private Button startRecordBtn;
    private ImageView iv_left;
    private ImageView iv_right;
    private Context mContext;

    private AudioRecordManager audioRecordManager;
    private AudioTrackManager audioTrackManager;
    private ListView lv_show;
    String FileNamePath = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
            + File.separator;
    String fileName;
    File file;
    ArrayList<String> list = new ArrayList<String>();
    private int i = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //视图绑定
        mContext = this;
        //audioRecordManager = new AudioRecordManager(mContext);
        audioTrackManager=new AudioTrackManager();
        lv_show = findViewById(R.id.lv_show);
        startRecordBtn = findViewById(R.id.start_record_btn);//开始录音按钮
        iv_left = findViewById(R.id.iv_left);  //左侧视图
        iv_right = findViewById(R.id.iv_right);//右侧视图

        //ListView视图填充
        flushAdapter(list);
        //文件保存路径
        //private static final String FILE_NAME = Environment
        //        .getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
        //        + File.separator + "test.pcm";


        //录音按钮点击：1.图片轮播 2.录音开始/结束
        startRecordBtn.setOnClickListener(v -> {
            //点击改变状态
            if (isRecording) {
                isRecording = false;
                picThread = PicThread.getInstance(isRecording, iv_left, iv_right);
                startRecordBtn.setText("开始录音");
                startRecordBtn.setBackgroundResource(R.drawable.btn_record_normal);

                //停止录音
                audioRecordManager.stopRecord();

                flushAdapter(list);

            } else {
                isRecording = true;
                picThread = PicThread.getInstance(isRecording, iv_left, iv_right);
                picThread.start();
                startRecordBtn.setText("结束录音");
                startRecordBtn.setBackgroundResource(R.drawable.btn_record_clicked);

                //开启录音
                audioRecordManager=new AudioRecordManager(this);
                audioRecordManager.startRecord(fileName);
            }
        });

        lv_show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = list.get(position);
                audioTrackManager.startPlay(str);
                flushAdapter(list);
            }
        });
    }


    private void flushAdapter(ArrayList<String> list){
        list.clear();
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, list);
        int i = 1;
        do {
            fileName = FileNamePath + i++ + ".pcm";
            list.add(fileName);
            fileName = FileNamePath + i + ".pcm";
            file = new File(fileName);
        } while (file.exists());
        lv_show.setAdapter(adapter);
    }

}