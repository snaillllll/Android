package com.rui.workdemo;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airiche.adashmi.R;
import com.airiche.adashmi.client.HaiCarClient;
import com.airiche.adashmi.databinding.Fragment360HomeBinding;
import com.airiche.adashmi.databinding.FragmentMusicHomeBinding;
import com.airiche.adashmi.ui.activity.HomeActivity;
import com.airiche.arcbase.ui.BaseFragment;

import java.util.Objects;

public class Home360Fragment extends BaseFragment implements View.OnClickListener {
    Fragment360HomeBinding binding;

    public static final String GEAR_D = "D";
    public static final String GEAR_P = "P";
    public static final String GEAR_R = "R";
    public static final String GEAR_N = "N";

    //泊入泊出状态管理
    private Button apa_parkin;
    private Button apa_parkout;
    public static Boolean isParkin = true;
    public static Boolean isDoorLocked = true;
    public static String apaGear = "D";
    public static int apaSpeed = 3;
    public static int availableParkingSpaceNum = 0;
    public static int targetParkingSpaceNum = 0;
    public static pauseParkDialog pauseParkDialog;
    /**
     * 可供选择的泊出方向
     * 0:无可用方向
     * 1:仅左侧可泊出
     * 2:仅右侧可泊出
     * 3:两侧均可泊出
     */
    public static int availableParkOutDir = 3;
    /**
     * 最终选择的泊出方向
     */
    private int selectedParkOutDir = 1;

    private String apaMsgText = "";


    private TextView parking_current_speed;
    private TextView apa_gear;
    private TextView apa_msg;
    private FrameLayout apa_speed;
    private FrameLayout apa_park_dir;
    private ImageView apa_parkout_left;
    private ImageView apa_parkout_right;
    private ImageView apa_turnleft_signal;
    private ImageView apa_turnright_signal;
    private ImageView apa_state_icon;
    private FrameLayout apa_search_parking_space;
    private FrameLayout apa_parkin_selection;
    private FrameLayout apa_start_parkout;
    private TextView apa_start_parkout_text;
    private FrameLayout apa_pause_park;
    private ImageView apa_parkin_auto;
    private ImageView apa_parkin_remote;
    private View apa_parklot;
    private TextView apa_parkin_auto_text;
    private TextView apa_parkin_remote_text;

    private int effectId = -1;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = Fragment360HomeBinding.inflate(inflater, container, false);
        //在此页面不允许对小车进行操作
        HaiCarClient.getInstance().setTouchEnable(false);
        //控件绑定
        setBinding();
        //设置监听
        setListener();
        //隐藏所有控件
        inVisibleAll();
        //设置字体
        setTypeface();

        effectId = HaiCarClient.getInstance().createSceneEffect(0,0,0,1,1,1,0,0,0,"/scene/APA/APA000.png",50,true);
        HaiCarClient.getInstance().createSceneEffect(1,1,1,1,1,1,0,0,0,"/scene/APA/APA00.png",54,true);

        return binding.getRoot();
    }

    private void setTypeface(){
        //1.得到AssetManager 2.根据路径得到Typeface 3.设置字体
        AssetManager mgr = this.getActivity().getAssets();
        Typeface tf_HelveticaNeue = Typeface.createFromAsset(mgr, "fonts/HelveticaNeueLTPro-Lt.otf");
        Typeface tf_BebasNeue = Typeface.createFromAsset(mgr, "fonts/BebasNeue-Regular.otf");
        parking_current_speed.setTypeface(tf_BebasNeue);
    }

    private void setBinding(){
        parking_current_speed = binding.parkingCurrentSpeed;
        apa_speed = binding.apaSpeed;
        apa_parkin = binding.parkin360;
        apa_parkout = binding.parkout360;
        apa_gear = binding.apaGear;
        apa_msg = binding.apaMsg;
        apa_park_dir = binding.apaParkDir;
        apa_parkout_left = binding.apaParkoutLeft;
        apa_parkout_right = binding.apaParkoutRight;
        apa_turnleft_signal = binding.apaTurnleftSignal;
        apa_turnright_signal = binding.apaTurnrightSignal;
        apa_state_icon = binding.apaStateIcon;
        apa_search_parking_space = binding.apaSearchParkingSpace;
        apa_parkin_selection = binding.apaParkinSelection;
        apa_parkin_auto = binding.apaParkinAuto;
        apa_parkin_remote = binding.apaParkinRemote;
        apa_parklot = binding.apaParklot;
        apa_start_parkout = binding.apaStartParkout;
        apa_start_parkout_text = binding.apaStartParkoutText;
        apa_pause_park = binding.apaPausePark;
        apa_parkin_auto_text = binding.apaParkinAutoText;
        apa_parkin_remote_text = binding.apaParkinRemoteText;
        pauseParkDialog = new pauseParkDialog(this.requireContext());
    }

    private void setListener(){
        //模拟泊出按钮监听
        if (null != apa_parkin){
            apa_parkin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //先重置Apa页面，包括销毁或隐藏模型，自车模型隐藏，其他销毁
                    inVisibleAll();
                    //基础控件展示
                    apaMsgText = getString(R.string.apa_click_to_search);
                    apa_msg.setText(apaMsgText);
                    apa_msg.setVisibility(View.VISIBLE);
                    apa_msg.setTextColor(getResources().getColor(R.color.apa_msg_nor));
                    apa_search_parking_space.setVisibility(View.VISIBLE);
                    apa_speed.setVisibility(View.VISIBLE);
                }
            });
        }
        //泊入-搜索车位按钮监听
        if (null != apa_search_parking_space){
            apa_search_parking_space.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //检查是否在D档且速度不为零
                    if (!apaGear.equals(GEAR_D)||(apaSpeed == 0)){
                        //提示挂D挡
                        apaMsgText = getString(R.string.apa_need_d_gear);
                        apa_msg.setText(apaMsgText);
                        apa_msg.setVisibility(View.VISIBLE);
                        apa_search_parking_space.setVisibility(View.INVISIBLE);
                        apa_speed.setVisibility(View.VISIBLE);
                    } else {
                        //进入搜索
                        apaMsgText = getString(R.string.apa_search_parking_space);
                        apa_msg.setText(apaMsgText);
                        apa_msg.setVisibility(View.VISIBLE);
                        apa_parkin_selection.setVisibility(View.VISIBLE);
                        apa_parkin_auto.setBackground(getResources().getDrawable(R.mipmap.btn_360_apa_dis));
                        apa_parkin_auto_text.setTextColor(getResources().getColor(R.color.apa_button_text_dis));
                        apa_parkin_remote_text.setTextColor(getResources().getColor(R.color.apa_button_text_dis));
                        apa_parkin_remote.setBackground(getResources().getDrawable(R.mipmap.btn_360_rpa_dis));
                        apa_search_parking_space.setVisibility(View.INVISIBLE);
                        apa_speed.setVisibility(View.VISIBLE);
                    }


                }
            });
        }
        //模拟泊出按钮监听
        if (null != apa_parkout){
            apa_parkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //先重置Apa页面，包括销毁或隐藏模型，自车模型隐藏，其他销毁
                    inVisibleAll();

                    //检查可供泊出的方向
                    if (availableParkOutDir == 0){
                        //无可用方向
                        apaMsgText = getString(R.string.apa_path_not_accessible);
                        apa_msg.setTextColor(getResources().getColor(R.color.apa_warning));
                        apa_start_parkout_text.setTextColor(getResources().getColor(R.color.apa_cannot_start_park));
                        apa_start_parkout.setBackground(getResources().getDrawable(R.mipmap.btn_360_apa_search_parkinglot_nor));
                        apa_start_parkout.setVisibility(View.VISIBLE);
                        apa_parkout_left.setBackground(getResources().getDrawable(R.mipmap.btn_360_apa_parkout_direction_left_dis));
                        apa_parkout_right.setBackground(getResources().getDrawable(R.mipmap.btn_360_apa_parkout_direction_right_dis));
                    } else if (availableParkOutDir == 1) {
                        //系统自动选择左侧
                        apaMsgText = getString(R.string.apa_auto_select_to_park);
                        apa_start_parkout.setVisibility(View.VISIBLE);
                        apa_parkout_left.setBackground(getResources().getDrawable(R.mipmap.btn_360_apa_parkout_direction_left_selected));
                        apa_parkout_right.setBackground(getResources().getDrawable(R.mipmap.btn_360_apa_parkout_direction_right_dis));
                        apa_speed.setVisibility(View.VISIBLE);
                    } else if (availableParkOutDir == 2) {
                        //系统自动选择右侧
                        apaMsgText = getString(R.string.apa_auto_select_to_park);
                        apa_start_parkout.setVisibility(View.VISIBLE);
                        apa_parkout_left.setBackground(getResources().getDrawable(R.mipmap.btn_360_apa_parkout_direction_left_dis));
                        apa_parkout_right.setBackground(getResources().getDrawable(R.mipmap.btn_360_apa_parkout_direction_right_selected));
                        apa_speed.setVisibility(View.VISIBLE);
                    } else if (availableParkOutDir == 3) {
                        //手动选择
                        apaMsgText = getString(R.string.apa_select_dir_to_parkout);
                        apa_start_parkout.setVisibility(View.VISIBLE);
                        apa_parkout_left.setBackground(getResources().getDrawable(R.mipmap.btn_360_apa_parkout_direction_left_selected));
                        apa_parkout_right.setBackground(getResources().getDrawable(R.mipmap.btn_360_apa_parkout_direction_right_nor));
                        apa_speed.setVisibility(View.VISIBLE);
                        apa_park_dir.setVisibility(View.VISIBLE);

                    }
                    apa_msg.setText(apaMsgText);
                    apa_msg.setVisibility(View.VISIBLE);

                }
            });
        }
        //泊出-左侧泊出按钮监听
        if (null != apa_parkout_left){
            apa_parkout_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    apa_parkout_left.setBackground(getResources().getDrawable(R.mipmap.btn_360_apa_parkout_direction_left_selected));
                    apa_parkout_right.setBackground(getResources().getDrawable(R.mipmap.btn_360_apa_parkout_direction_right_nor));
                    selectedParkOutDir = 1;
                }
            });
        }
        //泊出-右侧泊出按钮监听
        if (null != apa_parkout_right){
            apa_parkout_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    apa_parkout_left.setBackground(getResources().getDrawable(R.mipmap.btn_360_apa_parkout_direction_left_nor));
                    apa_parkout_right.setBackground(getResources().getDrawable(R.mipmap.btn_360_apa_parkout_direction_right_selected));
                    selectedParkOutDir = 2;
                }
            });
        }
        //泊入-自动泊车按钮监听
        if (null != apa_parkin_auto){
            apa_parkin_auto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        //泊入-遥控泊车按钮监听
        if (null != apa_parkin_remote){
            apa_parkin_remote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        //泊出-开始泊车按钮监听
        if (null != apa_start_parkout){
            apa_start_parkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    apa_start_parkout.setVisibility(View.INVISIBLE);
                    apa_park_dir.setVisibility(View.INVISIBLE);
                    apa_pause_park.setVisibility(View.VISIBLE);
                    if (selectedParkOutDir==1){
                        apa_turnleft_signal.setVisibility(View.VISIBLE);
                    } else {
                        apa_turnright_signal.setVisibility(View.VISIBLE);
                    }
                    apaMsgText = getString(R.string.apa_parking_on);
                    apa_msg.setText(apaMsgText);
                    apa_msg.setVisibility(View.VISIBLE);
                }
            });
        }

        //暂停泊车监听
        if (null != apa_pause_park){
            apa_pause_park.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pauseParkDialog.show();
                }
            });
        }
    }

    private void inVisibleAll(){

        if (null != apa_msg){
            apa_msg.setVisibility(View.INVISIBLE);
        }
        if (null != apa_park_dir){
            apa_park_dir.setVisibility(View.INVISIBLE);
        }
        if (null != apa_turnleft_signal){
            apa_turnleft_signal.setVisibility(View.INVISIBLE);
        }
        if (null != apa_turnright_signal){
            apa_turnright_signal.setVisibility(View.INVISIBLE);
        }
        if (null != apa_state_icon){
            apa_state_icon.setVisibility(View.INVISIBLE);
        }
        if (null != apa_parklot){
            apa_parklot.setVisibility(View.INVISIBLE);
        }
        if (null != apa_parkin_selection){
            apa_parkin_selection.setVisibility(View.INVISIBLE);
        }
        if (null != apa_pause_park){
            apa_pause_park.setVisibility(View.INVISIBLE);
        }
        if (null != apa_start_parkout){
            apa_start_parkout.setVisibility(View.INVISIBLE);
        }
        if (null != apa_speed){
            apa_speed.setVisibility(View.INVISIBLE);
        }
        if (null != apa_search_parking_space){
            apa_search_parking_space.setVisibility(View.INVISIBLE);
        }
        if (effectId > 0){
            HaiCarClient.getInstance().destroySceneEffect(effectId);
        }
    }

    private void checkGearD(){

    }

    private void parkOutFinish(){
        //泊车结束
        inVisibleAll();
        apaMsgText = getString(R.string.apa_parking_finish);
        apa_msg.setText(apaMsgText);
        apa_msg.setVisibility(View.VISIBLE);
    }

    //泊车暂停-退出泊车
    private void parkStopError(){
        inVisibleAll();
        apaMsgText = getString(R.string.apa_system_error);
        apa_msg.setText(apaMsgText);
        apa_msg.setVisibility(View.VISIBLE);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    @Override
    public void onClick(View view) {
    }


    @Override
    public void onDestroy() {
        binding = null;
        super.onDestroy();
    }

    class pauseParkDialog extends Dialog {

        private ImageView apa_pause_continue;
        private ImageView apa_pause_stop;

        public pauseParkDialog(@NonNull Context context) {
            super(context, R.style.Dialog_Msg);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_pause_park);
            //按空白处不能取消动画
            setCanceledOnTouchOutside(false);
            initView();
            initEvent();
        }

        @Override
        public void show() {
            super.show();
        }

        @Override
        public void hide() {
            super.hide();
        }

        private void initView(){
            apa_pause_continue = findViewById(R.id.apa_pause_continue);
            apa_pause_stop = findViewById(R.id.apa_pause_stop);
        }

        private void initEvent(){
            if (null != apa_pause_continue) {
                apa_pause_continue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //继续泊车
                        hide();
                    }
                });
            }
            if (null != apa_pause_stop) {
                apa_pause_stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //退出泊车
                        hide();
                        parkStopError();
                    }
                });
            }
        }
    }
}
