package com.personal.format.ui;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.common.lib.SlidingTabLayout.TabColorizer;
import com.personal.format.R;
import com.common.lib.SlidingTabLayout;
import com.common.lib.ViewPagerAdapter;

public class SampleActivity extends ActionBarActivity implements TabColorizer,AdapterView.OnItemClickListener {

    private DrawerLayout mDrawerLayout;//侧滑菜单
    private ActionBarDrawerToggle drawerToggle;

    private ViewPager pager;//分页管理器
    private ViewPagerAdapter mPagerAdapter;
    private ListView mDrawerList;//侧滑菜单列表

    private String titles[];//导航栏菜单

    private Toolbar toolbar;//工具条，菜单颜色
    private SlidingTabLayout slidingTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        initialRunnableContextAndFindViews();

    }
    /**初始化程序的原型界面以及控件*/
    private void initialRunnableContextAndFindViews(){

        titles = new String[6];
        titles[0] = getString(R.string.format_audio_transform);
        titles[1] = getString(R.string.format_video_transform);
        titles[2] = getString(R.string.format_feedback);
        titles[3] = getString(R.string.format_setting);
        titles[4] = getString(R.string.format_about);
        titles[5] = getString(R.string.format_share);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.nav_drawer);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_ab_drawer);
        }

        pager = (ViewPager) findViewById(R.id.viewpager);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);

        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), titles);
        pager.setAdapter(mPagerAdapter);

        slidingTabLayout.setViewPager(pager);
        slidingTabLayout.setCustomTabColorizer(this);

        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(drawerToggle);

        String[] values = new String[6];
        values[0] = getString(R.string.format_audio_transform);
        values[1] = getString(R.string.format_video_transform);
        values[2] = getString(R.string.format_feedback);
        values[3] = getString(R.string.format_setting);
        values[4] = getString(R.string.format_about);
        values[5] = getString(R.string.format_share);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public int getIndicatorColor(int position) {
        return Color.CYAN;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setTitle(titles[position]);
        mDrawerLayout.closeDrawer(Gravity.START);
        switchDifferentFragment(position);
    }

    /**切换到不同的Fragment*/
    private void switchDifferentFragment(int position){
        pager.setCurrentItem(position);
        pager.requestLayout();
    }
}
