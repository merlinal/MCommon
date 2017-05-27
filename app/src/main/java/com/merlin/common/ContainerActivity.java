package com.merlin.common;

import android.content.Intent;
import android.os.Bundle;

import com.merlin.core.at.MustFragment;
import com.merlin.core.util.Util;
import com.merlin.view.bar.MBarView;
import com.merlin.view.bar.model.Bar;

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

    public static void start(AbstractActivity activity, @MustFragment Class<?> cls, Bundle bundle, int requestCode) {
        Intent it = new Intent(activity, ContainerActivity.class);
        it.putExtra("fragmentName", cls.getName());
        if (bundle != null) {
            it.putExtras(bundle);
        }
        activity.startActivityForResult(it, requestCode);
    }

    private AbstractFragment fragment;
    private Bar bar;
    private MBarView mBarView;

    @Override
    public void initData() {
        super.initData();
        bar = new Bar.Builder().setActivity(this).build();
    }

    @Override
    public void handleParam() {
        super.handleParam();
        Intent it = getIntent();
        fragment = Util.loadClass(it.getStringExtra("fragmentName"));
        if (it.getExtras() != null) {
            fragment.setArguments(it.getExtras());
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.container;
    }

    @Override
    public void initView() {
        super.initView();

        mBarView = Util.view(this, R.id.mBarView);
        mBarView.apply(bar = new Bar.Builder().setActivity(this).build());

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_page, fragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        fragment.onBackPressed();
    }

    protected MBarView bar() {
        return mBarView;
    }

}
