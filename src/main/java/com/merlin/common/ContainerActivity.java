package com.merlin.common;

import com.merlin.core.util.MUtil;
import com.merlin.view.bar.MBarView;

/**
 * activity中嵌入一个fragment
 * @author zal
 */

public class ContainerActivity extends ContainerFullActivity {

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
