package com.rui.workdemo2.util;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rui.workdemo2.R;

public class ToastUtil {

    private Toast mToast;
    private ToastUtil(Context context, CharSequence text, int duration) {
        View v = LayoutInflater.from(context).inflate(R.layout.toast_exception, null);
        TextView textView = (TextView) v.findViewById(R.id.tv_toast);
        textView.setText(text);
        //textView.setTextColor(Color.parseColor("#EBEFFA"));
        //textView.setTextSize(16);
        mToast = new Toast(context);
        mToast.setDuration(duration);
        mToast.setGravity(Gravity.CENTER,0,0);
        mToast.setView(v);
    }
    public static ToastUtil makeText(Context context, CharSequence text, int duration) {
        return new ToastUtil(context, text, duration);
    }
    public void show() {
        if (mToast != null) {
            mToast.show();
        }
    }
    /*
    api30后 toast.getView() 会返回null
    public static void show(Context ctx,String msg){
        Toast toast = Toast.makeText(ctx, msg, Toast.LENGTH_LONG);
        LinearLayout layout = (LinearLayout)toast.getView();
        //设置背景
        layout.setBackgroundResource(R.drawable.toast_rpa);
        TextView tv = (TextView) layout.getChildAt(0);
        //设置字体大小
        tv.setTextSize(15);
        //设置字体颜色
        tv.setTextColor(Integer.parseInt("#EBEFFA"));
        //显示的位置
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }*/
}
