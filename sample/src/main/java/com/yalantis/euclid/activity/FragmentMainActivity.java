package com.yalantis.euclid.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import android.widget.Toolbar;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.yalantis.euclid.sample.R;
import com.yalantis.euclid.sample.TabMessage;

/**
 * Created by bbwang on 2016/8/18.
 */
public class FragmentMainActivity extends FragmentActivity {

    private BottomBar mBottomBar;
    public static Fragment[] mFragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.fragment_activity_main);
        setFragment(0);
        setBottomBar(savedInstanceState);

    }

    private void setBottomBar(@Nullable Bundle savedInstanceState) {

        mBottomBar = BottomBar.attach(this, savedInstanceState,
                ContextCompat.getColor(this, R.color.white),// Background Color
                ContextCompat.getColor(this, R.color.darkgreen), // Tab Item Color
                0.25f);
        mBottomBar.setItems(R.menu.bottombar_menu_three_items);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (mFragments == null) {
                    return;
                }
                getSupportFragmentManager().beginTransaction()
                        .hide(mFragments[0])
                        .hide(mFragments[1])
                        .hide(mFragments[2])
                        .hide(mFragments[3])
                        .show(mFragments[TabMessage.res2index(menuItemId)])
                        .commit();

            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                Toast.makeText(getApplicationContext(), TabMessage.get(menuItemId, true), Toast.LENGTH_SHORT).show();
            }
        });

        int redColor = Color.parseColor("#FF0000");

        // We want the nearbyBadge to be always shown, except when the Favorites tab is selected.
        BottomBarBadge nearbyBadge = mBottomBar.makeBadgeForTabAt(1, redColor, 6);
        nearbyBadge.setAutoShowAfterUnSelection(true);

        //
    }

    private void setFragment(int whichIsDefault) {
        mFragments = new Fragment[4];
        mFragments[0] = getSupportFragmentManager().findFragmentById(R.id.fragment_task_list);
        mFragments[1] = getSupportFragmentManager().findFragmentById(R.id.fragment_task_publish);
        mFragments[2] = getSupportFragmentManager().findFragmentById(R.id.fragment_task_mgr);
        mFragments[3] = getSupportFragmentManager().findFragmentById(R.id.fragment_about_mine);
        getSupportFragmentManager().beginTransaction()
                .hide(mFragments[0])
                .hide(mFragments[1])
                .hide(mFragments[2])
                .hide(mFragments[3])
                .show(mFragments[whichIsDefault])
                .commit();

    }
}
