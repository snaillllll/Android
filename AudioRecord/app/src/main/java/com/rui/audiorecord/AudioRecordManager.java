package com.rui.audiorecord;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class AudioRecordManager {
    private AudioRecord mAudioRecord;
    private FileOutputStream os; //录音时的输出流
    private Thread mRecordThread;

    private int recordBufsize = 0;
    private boolean isRecording = false;

    //文件保存路径
    private String FILE_NAME;
    /*private static final String FILE_NAME = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
            + File.separator + "test.pcm";*/

    public AudioRecordManager(Context context){
        initData(context);
    }

    private void initData(Context context) {

        recordBufsize = AudioRecord
                .getMinBufferSize(44100,
                        AudioFormat.CHANNEL_IN_STEREO,
                        AudioFormat.ENCODING_PCM_16BIT);
        Log.i("audioRecordTest ", "size->" + recordBufsize);

        //创建audioRecord实例
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            /* TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
             for ActivityCompat#requestPermissions for more details.*/
            Log.i("Permission","Permission ！= Granted");
            Log.i("Permission","没有为audioRecord赋值");
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.RECORD_AUDIO}, 2000);
            //return;
        }
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                44100,
                AudioFormat.CHANNEL_IN_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                recordBufsize);
        Log.i("audioRecordTest","mAudioRecord->"+mAudioRecord);
    }



    /**
     * 销毁线程方法
     */
    private void destroyThread() {
        try {
            isRecording = false;
            if (null != mRecordThread && Thread.State.RUNNABLE == mRecordThread.getState()) {
                try {
                    Thread.sleep(500);
                    mRecordThread.interrupt();
                } catch (Exception e) {
                    mRecordThread = null;
                }
            }
            mRecordThread = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mRecordThread = null;
        }
    }

    /**
     * 启动录音线程
     */
    private void startThread() {
        destroyThread();
        if (mRecordThread == null) {
            mRecordThread = new Thread(recordRunnable);
            mRecordThread.start();
        }
    }

    /**
     * 录音线程
     */
    Runnable recordRunnable = new Runnable() {
        @Override
        public void run() {
            //设置线程的优先级
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

            if (isRecording) {
                return;
            }
            isRecording = true;
            if(mAudioRecord==null){
                Log.i("audioRecord","is null");
                return;
            }
            //initData();
            mAudioRecord.startRecording();
            Log.i("audioRecordTest", "开始录音");

            byte data[] = new byte[recordBufsize];
            File file = new File(FILE_NAME);
            Log.i("audioRecordTest","FilePath->"+file.getPath());

            try {
                if (!file.exists()) {
                    file.createNewFile();
                    Log.i("audioRecordTest", "创建录音文件->" + FILE_NAME);
                }
                Log.i("audioRecordTest", "os ->" + os);
            } catch (Exception e) {
                e.printStackTrace();
            }
            int read;
            if (os != null) {
                while (isRecording) {
                    read = mAudioRecord.read(data, 0, recordBufsize);
                    if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                        try {
                            os.write(data);
                            Log.i("audioRecordTest", "写录音数据->" + read+ ";   Data ->"+data[0]+"");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }

    };

    /**
     * 播放文件
     * @param path
     * @throws Exception
     */
    private void setPath(String path) throws Exception {
        File file = new File(path);
        FILE_NAME = path;
        os = new FileOutputStream(file);
    }


    //开启录音
    public void startRecord(String path) {
        try{
            setPath(path);
            startThread();
        }catch (Exception e){
            e.printStackTrace();
            Log.i("RecordThread","方法StartRecord()出错");
        }

    }

    //停止录音
    public void stopRecord() {
        try{
            destroyThread();
            if (mAudioRecord != null) {
                if(mAudioRecord.getState() == AudioRecord.STATE_INITIALIZED){//初始化成功
                    mAudioRecord.stop();//停止录音
                    Log.i("audioRecordTest","结束录音");
                }
                if(mAudioRecord!= null){
                    mAudioRecord.release();//释放audioRecord资源
                }
            }
            if(os!=null){
                os.close(); //关闭输出流
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.i("RecordThread","方法stopRecord()出错");
        }


    }

}