package com.example.luming.iis.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.luming.iis.R;
import com.example.luming.iis.activity.DeviceActivity;
import com.example.luming.iis.bean.Device;
import com.example.luming.iis.database.DatabaseOperator;

import java.util.List;

/**TODO 稍后替换UI效果，会更改为RecyclerView
 * Created by TukulHX on 2019/4/4
 */
public class MyDeviceAdapter extends BaseAdapter {

    private DeviceActivity context;
    private DatabaseOperator dbOperator;
    private List<Device> deviceList;

    public MyDeviceAdapter(DeviceActivity context, List<Device> deviceList) {
        this.context = context;
        this.deviceList = deviceList;
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
            vh.bt_device_conn = convertView.findViewById(R.id.bt_device_conn);
            vh.ll_device_name = convertView.findViewById(R.id.ll_device_item);
            vh.cv_desc = convertView.findViewById(R.id.cv_desc);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.cv_desc.setVisibility(View.GONE);
        vh.tv_deviceName.setText(device.getName());
        vh.tv_deviceIP.setText(device.getIp());
        vh.tv_devicePort.setText(String.valueOf(device.getPort()));
        vh.bt_device_conn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do connection
                context.connection(device.getIp(), device.getPort());
            }
        });
        vh.cv_desc.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // do delete
                //TODO 这里需要增加弹出选项：1：修改  2：删除
                dbOperator.deleteDevice(device);
                context.onResume();
                return true;
            }
        });
        vh.ll_device_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (device.isVisible()) {
                    //淡出
                    device.setVisible(false);
                    vh.cv_desc.animate().alpha(0).setDuration(500).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            vh.cv_desc.setVisibility(View.GONE);
                        }
                    });
                } else {
                    //淡入
                    vh.cv_desc.setAlpha(0f);
                    vh.cv_desc.setVisibility(View.VISIBLE);
                    vh.cv_desc.animate().alpha(1f).setDuration(1000).setListener(null);
                    device.setVisible(true);
                }
            }
        });
        return convertView;
    }

    static class ViewHolder{
        private TextView tv_deviceName;
        private TextView tv_deviceIP;
        private TextView tv_devicePort;
        private Button bt_device_conn;
        private LinearLayout ll_device_name;
        private CardView cv_desc;

    }
}
