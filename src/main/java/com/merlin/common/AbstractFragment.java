package com.merlin.common;

import android.app.Activity;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.merlin.core.base.IViewFlow;
import com.merlin.view.bar.MBarView;

/**
 * Created by ncm on 16/11/30.
 */

public abstract class AbstractFragment<AbstractVM, Binding extends ViewDataBinding>
        extends Fragment
        implements IViewFlow {

    protected AbstractVM vm;
    protected Binding binding;
    protected View mRoot;

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
