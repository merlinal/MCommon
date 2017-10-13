package com.merlin.common;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.merlin.core.base.AbstractVM;
import com.merlin.core.base.IViewFlow;
import com.merlin.core.util.MUtil;

/**
 * Created by ncm on 16/11/30.
 */

public abstract class AbstractActivity<VM extends AbstractVM, Binding extends ViewDataBinding>
        extends AppCompatActivity
        implements IViewFlow {

    protected VM vm;
    protected Binding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int layoutId = getLayoutResId();
        if (layoutId != 0) {
            setContentView(layoutId);
        }

        handleParam();
        initData();
        initTool();
        initView();
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


}
