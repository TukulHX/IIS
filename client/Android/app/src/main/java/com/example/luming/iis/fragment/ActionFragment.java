package com.example.luming.iis.fragment;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.luming.iis.R;
import com.example.luming.iis.base.BaseFragment;
import com.example.luming.iis.database.DatabaseOperator;
import com.example.luming.iis.utils.MySocket;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * TODO 稍后重构
 */
public class ActionFragment extends BaseFragment {
    private ListView listView;
    private List<String> list = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private JSONObject config;
    private Button button;
    private EditText editText;
    private String moduleName;
    private String send_cmd;
    private String rec_value;
    private DatabaseOperator databaseOperator;
    private String user_id = "-1";


    private static final String JSON = "json";
    public static final String ACTION = "action";
    public static final String SETTER = "setter";

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            try {
                Toast.makeText(getActivity(), ((JSONObject) msg.obj).getString("content"), Toast.LENGTH_SHORT).show();
                rec_value = ((JSONObject) msg.obj).getString("content");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            databaseOperator.addData(user_id,moduleName, send_cmd, rec_value);
        }
    };


    @Override
    protected int getInflateView() {
        return R.layout.fragment_action;
    }

    /**
     * TODO 待重构内容
     */
    @Override
    protected void initEvent() {
        databaseOperator = DatabaseOperator.getInstance(getContext());
        listView = getActivity().findViewById(R.id.action_list);
        try {
            config = new JSONObject((String) getActivity().getIntent().getExtras().get(JSON));
            Iterator<?> it = config.keys();
            String key = "";
            while (it.hasNext()) {//遍历JSONObject
                key = (String) it.next().toString();
                if (null != key && !"".equals(key)) {
                    JSONObject setting = new JSONObject(config.getString(key));
                    String type = setting.getString("type");
                    if (type.equals("action") || type.equals("setter")) {
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
                button = getActivity().findViewById(R.id.action_button);
                editText = getActivity().findViewById(R.id.action_text);
                moduleName = (String) adapterView.getItemAtPosition(i);
                try {
                    JSONObject setting = new JSONObject(config.getString(moduleName));
                    switch (setting.getString("type")) {
                        case ACTION:
                            Toast.makeText(getActivity(), "ACTION", Toast.LENGTH_SHORT).show();
                            editText.setFocusableInTouchMode(false);
                            editText.setFocusable(false);
                            editText.setText(moduleName + "：执行");
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String str = new String("{\"name\":\"" + moduleName + "\"}");
                                    Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
                                    send(str);
                                    receive();
                                }
                            });
                            break;
                        case SETTER:
                            Toast.makeText(getContext(), "SETTER", Toast.LENGTH_SHORT).show();
                            editText.setFocusableInTouchMode(true);
                            editText.setFocusable(true);
                            editText.setText("");
                            editText.setHint("请输入控制量");
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    send_cmd = editText.getText().toString().trim();
                                    String str = ("{\"name\":\"" + moduleName + "\",\"value\":\"" + send_cmd + "\"}");
                                    Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
                                    send(str);
                                    receive();
                                }
                            });
                            break;
                    }
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
                    out.close();
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
                    byte[] buffer = new byte[1024];
                    InputStream is = MySocket.getIn();
                    is.read(buffer);
                    JSONObject jsonObject = new JSONObject(new String(buffer));
                    message.obj = jsonObject;
                    handler.sendMessage(message);
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
