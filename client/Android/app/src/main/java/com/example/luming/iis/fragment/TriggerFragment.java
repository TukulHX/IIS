package com.example.luming.iis.fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luming.iis.R;
import com.example.luming.iis.activity.ManageActivity;
import com.example.luming.iis.adapter.ImageAdapter;
import com.example.luming.iis.base.BaseFragment;
import com.example.luming.iis.utils.MySocket;
import com.example.luming.iis.utils.SharedPreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TriggerFragment extends BaseFragment {
    private ListView lv_module;
    private List<String> list = new ArrayList<>();
    private BaseAdapter gv_adapter;
    private BaseAdapter lv_adapter;
    private GridView gv_item;
    private ArrayList<Map<String,Object>>  data = new ArrayList<>();
    private JSONObject config;
    private Button bt_start;
    private Button bt_refresh;
    private TextView tv_title;
    private String moduleName;
    private String user_id = SharedPreferenceUtils.getString(getContext(),"LoginInfo","-1");
    private String device_name;
    private static final String JSON = "json";
    private static final String START_CMD = "start";
    private static final String REFRESH_CMD = "fetch";

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            try {
                JSONObject json = ((JSONObject) msg.obj);
                int extraNum = json.getInt("content");
                if( extraNum > 0 ){
                    for(Integer i = 0; i < extraNum; ++i){
                        String name = json.getString("name" + i.toString());
                        //这里在目录下添加了/images/，主要是为了方便fileProvider获取路径方便。
                        String path = getContext().getFilesDir() + "/images/" + device_name + "/" + moduleName;
                        File dir = new File(path);
                        if(!dir.exists())
                            dir.mkdirs();
                        File file = new File(path + "/" + name);
                        file.createNewFile();
                        FileOutputStream fileOutputStream = new FileOutputStream(path + "/" + name);
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                        byte[] buffer = Base64.decode(json.getString( "extra" + i.toString()),Base64.DEFAULT);
                        bufferedOutputStream.write(buffer);
                        bufferedOutputStream.flush();
                    }
                }
                getData();
                gv_adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //databaseOperator.addData(user_id,moduleName, send_cmd, rec_value);
        }
    };


    @Override
    protected int getInflateView() {
        return R.layout.fragment_trigger;
    }

    @Override
    protected void initEvent() {
        device_name = getActivity().getIntent().getExtras().getString(ManageActivity.DEVICE_NAME,"-1");
        gv_adapter = new ImageAdapter(getContext(),data);
        lv_adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1, list);
        lv_module = getActivity().findViewById(R.id.trigger_list);
        lv_module.setAdapter(lv_adapter);
        gv_item = getActivity().findViewById(R.id.trigger_gridview);
        gv_item.setAdapter(gv_adapter);
        bt_start = getActivity().findViewById(R.id.trigger_start);
        bt_refresh = getActivity().findViewById(R.id.trigger_refresh);
        tv_title = getActivity().findViewById(R.id.trigger_title);

        //TODO 优化所有 Fragment 的添加模块逻辑
        try {
            config = new JSONObject((String) getActivity().getIntent().getExtras().get(JSON));
            Iterator<?> it = config.keys();
            String key = "";
            while (it.hasNext()) {//遍历JSONObject
                key = (String) it.next().toString();
                if (null != key && !"".equals(key)) {
                    JSONObject setting = new JSONObject(config.getString(key));
                    String type = setting.getString("type");
                    if (type.equals("trigger")) {
                        list.add(key);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        lv_module.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                moduleName = (String) adapterView.getItemAtPosition(i);
                try {
                    JSONObject setting = new JSONObject( config.getString( moduleName));
                    tv_title.setText(moduleName);
                    bt_start.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String cmd = ("{\"name\":\"" + moduleName + "\",\"value\":\"" + START_CMD + "\"}");
                            Toast.makeText(getActivity(), cmd, Toast.LENGTH_SHORT).show();
                            send(cmd);
                            receive();
                        }
                    });
                    bt_refresh.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String cmd = ("{\"name\":\"" + moduleName + "\",\"value\":\"" + REFRESH_CMD + "\"}");
                            send(cmd);
                            receive();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getData();
                gv_adapter.notifyDataSetChanged();
            }
        });
    }

    private void send(final String json) {
        new Thread() {
            @Override
            public void run() {
                try {
                    JSONObject j = new JSONObject(json);
                    OutputStream out = MySocket.getOut();
                    out.write(j.toString().getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void receive() {
        new Thread() {
            Message message = new Message();

            @Override
            public void run() {
                try {
                    DataInputStream input = new DataInputStream(MySocket.getIn());
                    int size = input.readInt();
                    int len = 0;
                    byte[] buffer = new byte[size];
                    while (len < size){
                        len += input.read(buffer,len,size-len);
                    }
                    JSONObject jsonObject = new JSONObject(new String(buffer));
                    message.obj = jsonObject;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public void getData(){
        data.clear();
        //TODO 文件名可能需要重新考虑统一，而不是动态.
        File dir = new File(getContext().getFilesDir() + "/images/" + device_name + "/" + moduleName);
        File[] files = dir.listFiles();
        for(int i = 0; files != null && i < files.length; i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("url",files[i].getPath());
            map.put("name",files[i].getName());
            data.add(map);
        }
    }
}
