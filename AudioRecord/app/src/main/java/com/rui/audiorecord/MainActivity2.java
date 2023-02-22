package com.rui.audiorecord;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;


/**
 * 测试AudioTrack
 * */

public class MainActivity2 extends AppCompatActivity {

    private Button btn_play;
    private AudioTrackManager audioTrackManager;
    private Button btn_stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btn_play = findViewById(R.id.btn_play);
        btn_stop = findViewById(R.id.btn_stop);
        audioTrackManager=AudioTrackManager.getInstance();


        btn_play.setOnClickListener(
                v->{
                audioTrackManager.startPlay("/sdcard/Music/test.pcm");
                }
        );
        btn_stop.setOnClickListener(
                v->{
                    audioTrackManager.stopPlay();
                }
        );

    }
}