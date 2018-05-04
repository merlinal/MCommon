package com.merlin.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.merlin.core.util.MUtil;
import com.merlin.view.bar.MBarView;

/**
 * activity中嵌入一个fragment
 * @author zal
 */

public class ContainerActivity extends ContainerFullActivity {

    public static void start(AbstractFragment srcFragment, Class<?> cls, Bundle bundle, int requestCode) {
        Intent it = new Intent(srcFragment.getContext(), ContainerActivity.class);
        it.putExtra(PARAM_FRAGMENT_NAME, cls.getName());
        if (bundle != null) {
            it.putExtras(bundle);
        }
        srcFragment.startActivityForResult(it, requestCode);
    }

    public static void start(Activity activity, Class<?> cls, Bundle bundle, int requestCode) {
        Intent it = new Intent(activity, ContainerActivity.class);
        it.putExtra(PARAM_FRAGMENT_NAME, cls.getName());
        if (bundle != null) {
            it.putExtras(bundle);
        }
        activity.startActivityForResult(it, requestCode);
    }

    public static void startNoAnim(AbstractFragment srcFragment, Class<?> cls, Bundle bundle, int requestCode) {
        Intent it = new Intent(srcFragment.getContext(), ContainerActivity.class);
        it.putExtra(PARAM_FRAGMENT_NAME, cls.getName());
        if (bundle != null) {
            it.putExtras(bundle);
        }
        it.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        srcFragment.startActivityForResult(it, requestCode);
        srcFragment.getActivity().overridePendingTransition(0, 0);
    }

    public static void startNoAnim(Activity activity, Class<?> cls, Bundle bundle, int requestCode) {
        Intent it = new Intent(activity, ContainerActivity.class);
        it.putExtra(PARAM_FRAGMENT_NAME, cls.getName());
        if (bundle != null) {
            it.putExtras(bundle);
        }
        it.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.startActivityForResult(it, requestCode);
        activity.overridePendingTransition(0, 0);
    }

    private MBarView mBarView;

    @Override
    protected int getLayoutResId() {
        return R.layout.m_container;
    }

    @Override
    public void initView() {
        super.initView();

        mBarView = MUtil.view(this, R.id.m_barView);
        mBarView.onBackPressed(this);
    }

    public MBarView barView() {
        return mBarView;
    }


}
