package com.merlin.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.merlin.view.bar.MBarView;

import java.util.List;

/**
 * activity中嵌入多个fragment
 * Created by Administrator on 2017/10/24.
 */

public class ContainersActivity extends AbstractActivity {

    private FragmentManager fm;
    private MBarView mBarView;

    @Override
    protected int getLayoutResId() {
        return R.layout.container;
    }

    @Override
    public void initTool() {
        super.initTool();
        fm = getSupportFragmentManager();
    }

    @Override
    public void initView() {
        super.initView();
        mBarView = (MBarView) findViewById(R.id.barView);
        mBarView.onBackPressed(this);
    }

    @Override
    public void onBackPressed() {
        if (fm.getBackStackEntryCount() > 1) {
            back();
        } else {
            ((AbstractFragment) fm.getFragments().get(0)).onBackPressed();
        }
    }

    public MBarView barView() {
        return mBarView;
    }

    /**
     * 后退至
     *
     * @param tag
     */
    public void backTo(String tag) {
        fm.popBackStack(tag, 0);
        show();
    }

    /**
     * 后退
     */
    public void back() {
        fm.popBackStackImmediate();
        show();
    }

    /**
     * @param fragment
     * @param tag      唯一性
     */
    public void add(Fragment fragment, String tag) {
        FragmentTransaction ft = fm.beginTransaction();
        hide(ft);
        ft.add(R.id.container, fragment, tag);
        ft.addToBackStack(tag);
        ft.commitAllowingStateLoss();
    }

    private void show() {
        FragmentTransaction ft = fm.beginTransaction();
        ft.show(fm.getFragments().get(fm.getBackStackEntryCount() - 1));
        ft.commitAllowingStateLoss();
    }

    private void hide(FragmentTransaction ft) {
        List<Fragment> fragmentList = fm.getFragments();
        for (Fragment fragment : fragmentList) {
            ft.hide(fragment);
        }
    }

}
