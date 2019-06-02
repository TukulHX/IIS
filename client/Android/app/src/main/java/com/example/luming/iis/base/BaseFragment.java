package com.example.luming.iis.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TukulHX on 2019/4/5
 */
public abstract class BaseFragment extends Fragment {

    protected List<String> list = new ArrayList<>();
    protected JSONObject config;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getInflateView(), container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initEvent();
    }
    public void addKey(String key){
        list.add(key);
    }
    public void setConfig(JSONObject config){
        this.config = config;
    }
    /**
     * 获得inflate的view
     * @return
     */
    protected abstract int getInflateView();

    /**
     * do something
     */
    protected abstract void initEvent();
}
