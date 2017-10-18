package com.merlin.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.merlin.core.at.MustFragment;
import com.merlin.core.util.MLog;
import com.merlin.core.util.MUtil;
import com.merlin.view.bar.MBarView;

/**
 * Created by ncm on 2017/5/27.
 */

public class ContainerActivity extends AbstractActivity {

    public static void start(AbstractFragment srcFragment, @MustFragment Class<?> cls, Bundle bundle, int requestCode) {
        Intent it = new Intent(srcFragment.getContext(), ContainerActivity.class);
        it.putExtra("fragmentName", cls.getName());
        if (bundle != null) {
            it.putExtras(bundle);
        }
        srcFragment.startActivityForResult(it, requestCode);
    }

    public static void start(Activity activity, @MustFragment Class<?> cls, Bundle bundle, int requestCode) {
        Intent it = new Intent(activity, ContainerActivity.class);
        it.putExtra("fragmentName", cls.getName());
        if (bundle != null) {
            it.putExtras(bundle);
        }
        activity.startActivityForResult(it, requestCode);
    }

    public static void startNoAnim(AbstractFragment srcFragment, @MustFragment Class<?> cls, Bundle bundle, int requestCode) {
        Intent it = new Intent(srcFragment.getContext(), ContainerActivity.class);
        it.putExtra("fragmentName", cls.getName());
        if (bundle != null) {
            it.putExtras(bundle);
        }
        it.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        srcFragment.startActivityForResult(it, requestCode);
        srcFragment.getActivity().overridePendingTransition(0, 0);
    }

    public static void startNoAnim(Activity activity, @MustFragment Class<?> cls, Bundle bundle, int requestCode) {
        Intent it = new Intent(activity, ContainerActivity.class);
        it.putExtra("fragmentName", cls.getName());
        if (bundle != null) {
            it.putExtras(bundle);
        }
        it.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.startActivityForResult(it, requestCode);
        activity.overridePendingTransition(0, 0);
    }

    private AbstractFragment fragment;
    private MBarView mBarView;

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.container;
    }

    @Override
    public void initView() {
        super.initView();

        Intent it = getIntent();
        fragment = MUtil.loadClass(it.getStringExtra("fragmentName"));
        if (fragment == null) {
            MLog.e("not found this fragment -- " + it.getStringExtra("fragmentName"));
            finish();
        } else {
            if (it.getExtras() != null) {
                fragment.setArguments(it.getExtras());
            }

            mBarView = MUtil.view(this, R.id.mBarView);
            mBarView.onBackPressed(this);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_page, fragment)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void onBackPressed() {
        fragment.onBackPressed();
    }

    protected MBarView barView() {
        return mBarView;
    }


}
