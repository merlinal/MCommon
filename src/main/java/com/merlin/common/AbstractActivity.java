package com.merlin.common;

import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.merlin.core.base.IViewFlow;
import com.merlin.core.context.MContext;
import com.merlin.core.util.MUtil;

/**
 * Created by ncm on 16/11/30.
 */

public abstract class AbstractActivity<AbstractVM, Binding extends ViewDataBinding>
        extends AppCompatActivity
        implements IViewFlow {

    protected AbstractVM vm;
    protected Binding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int layoutId = getLayoutResId();
        if (layoutId != 0) {
            setContentView(layoutId);
        }
        MContext.inst().setActivity(this);

        handleParam();
        initData();
        initTool();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MContext.inst().setActivity(this);
    }

    protected abstract int getLayoutResId();

    @Override
    public void initTool() {
    }

    @Override
    public void handleParam() {
    }

    @Override
    public void initData() {
    }

    @Override
    public void initView() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        vm = null;
        binding = null;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            // 焦点是EditText且触摸点在EditText内，显示键盘；否则，隐藏键盘；
            if (MUtil.isInEditText(v, ev)) {
                MUtil.showSoftInput(this);
            } else {
                MUtil.hideSoftInput(this, v);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    protected void setStatusColor(int color) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //状态栏
                window.setStatusBarColor(color);
                //底部导航栏
                window.setNavigationBarColor(color);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
