package com.example.luming.iis.fragment;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Base64;

import com.example.luming.iis.R;
import com.example.luming.iis.base.BaseFragment;
import com.example.luming.iis.database.DatabaseOperator;
import com.example.luming.iis.utils.MySocket;
import com.example.luming.iis.utils.SharedPreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TriggerFragment extends BaseFragment {
    private ListView listView;
    private List<String> list = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private JSONObject config;
    private Button bt_start;
    private Button bt_refresh;
    private TextView tv_title;
    private String moduleName;
    private DatabaseOperator databaseOperator;
    private String user_id = SharedPreferenceUtils.getString(getContext(),"LoginInfo","-1");

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
                        FileOutputStream fileOutputStream = getActivity().openFileOutput(name, Context.MODE_PRIVATE);
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                        byte[] buffer = Base64.decode(json.getString( "extra" + i.toString()),Base64.DEFAULT);
                        bufferedOutputStream.write(buffer);
                        bufferedOutputStream.flush();
                    }
                }
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
        databaseOperator = DatabaseOperator.getInstance(getContext());
        listView = getActivity().findViewById(R.id.trigger_list);
        bt_start = getActivity().findViewById(R.id.trigger_start);
        bt_refresh = getActivity().findViewById(R.id.trigger_refresh);
        tv_title = getActivity().findViewById(R.id.trigger_title);
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
        adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
}
