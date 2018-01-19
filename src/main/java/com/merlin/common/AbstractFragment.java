package com.merlin.common;

import android.app.Activity;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.merlin.core.base.IViewFlow;
import com.merlin.core.tool.IClick;
import com.merlin.core.tool.IHandler;
import com.merlin.core.tool.SafeHandle;
import com.merlin.core.tool.SafeOnClickListener;
import com.merlin.view.bar.MBarView;

/**
 * @author merlin
 */

public abstract class AbstractFragment<AbstractVM, Binding extends ViewDataBinding>
        extends Fragment
        implements IViewFlow, IClick, IHandler {

    protected AbstractVM vm;
    protected Binding binding;
    protected View mRoot;
    private SafeOnClickListener mOnClickListener;
    private SafeHandle mHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleParam();
        initTool();
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = getLayoutView(inflater, container);
        initView();
        return mRoot;
    }

    protected abstract View getLayoutView(LayoutInflater inflater, @Nullable ViewGroup container);

    @Override
    public void initTool() {
        mOnClickListener = new SafeOnClickListener(this);
        mHandler = new SafeHandle<AbstractFragment>(this, this);
    }

    protected void setOnClickListener(View view) {
        view.setOnClickListener(mOnClickListener);
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
    public void onClickView(View view) {
    }

    @Override
    public void onHandleMessage(Message message) {
    }

    public void onBackPressed() {
        finishActivity();
    }

    public void finishActivity() {
        Activity activity = getActivity();
        if (activity != null) {
            activity.setResult(Activity.RESULT_OK);
            activity.finish();
        }
    }

    protected MBarView barView() {
        if (getActivity() instanceof ContainerActivity) {
            return ((ContainerActivity) getActivity()).barView();
        }
        return null;
    }

}
