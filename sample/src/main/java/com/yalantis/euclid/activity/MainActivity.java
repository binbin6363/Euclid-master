package com.yalantis.euclid.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.yalantis.euclid.sample.R;
import com.yalantis.euclid.sample.TabMessage;
import com.yalantis.euclid.sample.TaskInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by iiro on 7.6.2016.
 */
@Deprecated
public class MainActivity extends AppCompatActivity {
    private BottomBar mBottomBar;
    private TextView mMessageView;

    private ListView listView;
    private List<TaskInfo> taskList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_basic);
        //setContentView(com.yalantis.euclid.library.R.layout.activity_euclid);

        initTaskListView();

        //mMessageView = (TextView) findViewById(R.id.messageView);

        mBottomBar = BottomBar.attach(this, savedInstanceState,
                ContextCompat.getColor(this, R.color.white),// Background Color
                ContextCompat.getColor(this, R.color.blue), // Tab Item Color
                0.25f);
        mBottomBar.setItems(R.menu.bottombar_menu_three_items);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                //mMessageView.setText(TabMessage.get(menuItemId, false));
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
    }

    private void initTaskListView() {
        setContentView(R.layout.task_list);
        taskList = new ArrayList<TaskInfo>();

        for (int i = 0; i < 20; ++i) {
            TaskInfo t = new TaskInfo();
            if (i == 0) {
                t.taskSelectFlag = true;
            } else {
                t.taskSelectFlag = false;
            }
            t.id = 1;
            t.taskName = "task_"+i;
            t.taskAddr = "addr_"+i;
            t.taskNum = "100";
            t.taskTotalNum = "10000";
            t.taskUint = "1";
            t.taskPrice = "2.0元";
            t.timeStart = (int)System.currentTimeMillis()/1000;
            taskList.add(t);
        }


        listView = (ListView) findViewById(R.id.task_listview);
        LocationListViewAdapter adapter = new LocationListViewAdapter(taskList,this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.putExtra("json", taskList.get(position).toString());
                setResult(102, intent);
                finish();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }


    class LocationListViewAdapter extends BaseAdapter {

        private List<TaskInfo> taskList;
        private Context mContext;

        public LocationListViewAdapter(List<TaskInfo> taskList, Context context) {
            this.taskList = taskList;
            mContext =context;
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return taskList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return taskList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TaskHolder view = null;
            TaskInfo taskInfo = taskList.get(position);
            if(convertView !=null){
                view = (TaskHolder) convertView.getTag();
            }else{
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.taskinfo_item2, null);

                view = new TaskHolder();
                view.user_avatar = (ImageView) convertView.findViewById(R.id.iv_icon);
                view.task_name = (TextView) convertView.findViewById(R.id.tv_name);
                view.task_earn = (TextView) convertView.findViewById(R.id.tv_ern);
                view.task_earn_unit = (TextView) convertView.findViewById(R.id.tv_ern_unit);
                view.task_num = (TextView) convertView.findViewById(R.id.tv_num);
                view.task_total_num = (TextView) convertView.findViewById(R.id.tv_num_total);

                view.task_name.setText(taskInfo.taskName);
                view.task_earn.setText(taskInfo.taskPrice);
                view.task_earn_unit.setText(taskInfo.taskUint);
                view.task_num.setText(taskInfo.taskNum);
                view.task_total_num.setText(taskInfo.taskTotalNum);
                convertView.setTag(view);
            }

            return convertView;
        }
    }

    class TaskHolder {
        public ImageView user_avatar;
        public TextView task_name;
        public TextView task_earn;
        public TextView task_earn_unit;
        public TextView task_num;
        public TextView task_total_num;
    }
}
