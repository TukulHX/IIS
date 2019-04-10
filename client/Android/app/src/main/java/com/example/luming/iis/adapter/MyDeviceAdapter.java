package com.example.luming.iis.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.luming.iis.R;
import com.example.luming.iis.activity.DeviceActivity;
import com.example.luming.iis.bean.Device;
import com.example.luming.iis.database.DatabaseOperator;

import java.util.List;

/**
 * TODO 稍后替换UI效果，会更改为RecyclerView
 * Created by TukulHX on 2019/4/4
 */
public class MyDeviceAdapter extends BaseAdapter {

    private DeviceActivity context;
    private DatabaseOperator dbOperator;
    private List<Device> deviceList;
    private String user_id;

    public MyDeviceAdapter(DeviceActivity context, List<Device> deviceList, String user_id) {
        this.context = context;
        this.deviceList = deviceList;
        this.user_id = user_id;
        dbOperator = DatabaseOperator.getInstance(context);
    }

    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public Device getItem(int position) {
        return deviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Device device = getItem(position);
        final ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.device_list_item, null);
            vh = new ViewHolder();
            convertView.setTag(vh);
            vh.tv_deviceName = convertView.findViewById(R.id.tv_device_name);
            vh.tv_deviceIP = convertView.findViewById(R.id.tv_device_ip);
            vh.tv_devicePort = convertView.findViewById(R.id.tv_device_port);
            vh.conn_view = convertView.findViewById(R.id.conn_view);
            vh.ll_device_name = convertView.findViewById(R.id.ll_device_item);
            vh.iv_direction = convertView.findViewById(R.id.iv_direction);
            vh.bt_conn = convertView.findViewById(R.id.bt_conn);
            vh.bt_modify = convertView.findViewById(R.id.bt_modify);
            vh.bt_delete = convertView.findViewById(R.id.bt_delete);
            vh.cv_desc = convertView.findViewById(R.id.cv_desc);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.cv_desc.setVisibility(View.GONE);
        vh.bt_modify.setVisibility(View.GONE);
        vh.bt_delete.setVisibility(View.GONE);

        vh.tv_deviceName.setText(device.getName());
        vh.tv_deviceIP.setText(device.getIp());
        vh.tv_devicePort.setText(String.valueOf(device.getPort()));
        vh.iv_direction.setBackgroundResource(R.drawable.right);
        vh.bt_conn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.connection(device.getIp(), device.getPort());
            }
        });
        vh.bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbOperator.deleteDevice(user_id,device);
                context.onResume();
            }
        });
        vh.cv_desc.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //change res
                if (vh.bt_conn.getVisibility() == View.VISIBLE) {
                    vh.bt_conn.setVisibility(View.GONE);
                    vh.bt_delete.setVisibility(View.VISIBLE);
                } else {
                    vh.bt_delete.setVisibility(View.GONE);
                    vh.bt_conn.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });
        vh.ll_device_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vh.iv_direction.setBackgroundResource(R.drawable.right);
                if (device.isVisible()) {
                    //淡出
                    device.setVisible(false);
                    vh.cv_desc.animate().alpha(0).setDuration(500).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            vh.cv_desc.setVisibility(View.GONE);
                            vh.bt_modify.setVisibility(View.GONE);
                        }
                    });
                } else {
                    vh.iv_direction.setBackgroundResource(R.drawable.down);
                    //淡入
                    vh.cv_desc.setAlpha(0f);
                    vh.cv_desc.setVisibility(View.VISIBLE);
                    vh.cv_desc.animate().alpha(1f).setDuration(1000).setListener(null);
                    device.setVisible(true);
                    vh.bt_modify.setVisibility(View.VISIBLE);
                }
            }
        });
        return convertView;
    }


    static class ViewHolder {
        private TextView tv_deviceName;
        private TextView tv_deviceIP;
        private TextView tv_devicePort;
        private FrameLayout conn_view;
        private LinearLayout ll_device_name;
        private ImageView iv_direction;
        private CardView cv_desc;
        private Button bt_conn, bt_modify, bt_delete;

    }
}
