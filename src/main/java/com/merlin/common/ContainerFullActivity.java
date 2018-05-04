package com.merlin.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.merlin.core.util.MLog;
import com.merlin.core.util.MUtil;

/**
 * @author zal
 */

public class ContainerFullActivity extends AbstractActivity {

    public final static String PARAM_FRAGMENT_NAME = "fragmentName";

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

    private AbstractFragment fragment;

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.m_container_full;
    }

    @Override
    public void initView() {
        super.initView();

        Intent it = getIntent();
        if (it.getStringExtra(PARAM_FRAGMENT_NAME) != null) {
            setFragment(it.getStringExtra(PARAM_FRAGMENT_NAME), it.getExtras());
        }
    }

    @Override
    public void onBackPressed() {
        if (fragment != null) {
            fragment.onBackPressed();
        }
    }

    protected void initFragment(Class<?> cls, Bundle bundle) {
        setFragment(cls.getName(), bundle);
    }

    private void setFragment(String fragmentName, Bundle bundle) {
        if (fragmentName == null) {
            return;
        }
        fragment = MUtil.loadInstance(fragmentName);
        if (fragment == null) {
            MLog.e("not found this fragment -- " + fragmentName);
            finish();
        } else {
            if (bundle != null) {
                fragment.setArguments(bundle);
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.m_container, fragment)
                    .commitAllowingStateLoss();
        }
    }

}
