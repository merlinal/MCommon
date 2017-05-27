package com.merlin.common;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.merlin.common.databinding.ContainerBinding;
import com.merlin.core.at.MustFragment;
import com.merlin.core.base.AbstractActivity;
import com.merlin.core.base.AbstractFragment;
import com.merlin.core.base.AbstractVM;
import com.merlin.core.util.Util;
import com.merlin.view.bar.model.Bar;

/**
 * Created by ncm on 2017/5/27.
 */

public class ContainerActivity extends AbstractActivity<AbstractVM, ContainerBinding> {

    public static void start(AbstractFragment srcFragment, @MustFragment Class<?> cls, Bundle bundle, int requestCode) {
        Intent it = new Intent(srcFragment.getContext(), ContainerActivity.class);
        it.putExtra("fragmentName", cls.getName());
        it.putExtras(bundle);
        srcFragment.startActivityForResult(it, requestCode);
    }

    public static void start(AbstractActivity activity, @MustFragment Class<?> cls, Bundle bundle, int requestCode) {
        Intent it = new Intent(activity, ContainerActivity.class);
        it.putExtra("fragmentName", cls.getName());
        it.putExtras(bundle);
        activity.startActivityForResult(it, requestCode);
    }

    private AbstractFragment fragment;
    private Bar bar;

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
    }

    @Override
    protected int getLayoutResId() {
        binding = DataBindingUtil.setContentView(this, R.layout.container);
        binding.setBarModel(bar);
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_page, fragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        fragment.onBackPressed();
    }

    protected Bar bar() {
        return bar;
    }

}
