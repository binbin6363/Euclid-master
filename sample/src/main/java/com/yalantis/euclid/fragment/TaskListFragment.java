package com.yalantis.euclid.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.euclid.sample.R;
import com.yalantis.euclid.sample.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bbwang on 2016/8/18.
 */
public class TaskListFragment extends Fragment {
    private ListView listView;
    private List<TaskInfo> taskList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        //return inflater.inflate(R.layout.task_list, container, false);
        View view = inflater.inflate(R.layout.task_list, container, false);
        initTaskListView(view);
        return view;
    }


    private void initTaskListView(View view) {
        taskList = new ArrayList<>();

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


        listView = (ListView) view.findViewById(R.id.task_listview);
        LocationListViewAdapter adapter = new LocationListViewAdapter(taskList,view.getContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(view.getContext(), "click task item", Toast.LENGTH_SHORT).show();
            }
        });
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
                // 需要赋值，否则会导致数据错乱
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
