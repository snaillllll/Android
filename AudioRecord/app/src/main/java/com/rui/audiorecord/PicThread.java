package com.rui.audiorecord;

import android.widget.ImageView;

public class PicThread extends Thread{
    private static Thread mThread;
    private static boolean isRecording;
    private static ImageView iv_left;
    private static ImageView iv_right;
    //图片资源
    private static int[] run = {R.mipmap.run_6,R.mipmap.run_7,R.mipmap.run_8,R.mipmap.run_9,R.mipmap.run_10};
    private PicThread(boolean is, ImageView left, ImageView right){
        isRecording=is;
        iv_left=left;
        iv_right=right;
    }
    public static Thread getInstance(boolean is, ImageView left, ImageView right){
        if(null == mThread){
            mThread=new PicThread(is,left,right);
        }else if(is != isRecording){
            mThread = new PicThread(is,left,right);
        }
        return mThread;
    }
    @Override
    public void run() {
        int i=0;
        int j=0;
        while(isRecording){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            iv_left.setBackgroundResource(run[i++%5]);
            iv_right.setBackgroundResource(run[j++%5]);
        }
    }
}
