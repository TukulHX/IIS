package com.example.luming.iis.adapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.luming.iis.R;
import com.example.luming.iis.utils.GetImageByUrl;
import com.example.luming.iis.utils.OpenFileIntentUtils;

import java.util.List;
import java.util.Map;

/**
 *
 * @author LeoLeoHan
 * @modifier LuMing
 */
public class ImageAdapter extends BaseAdapter {
    // 要显示的数据的集合
    private List<Map<String, Object>> data;
    // 接受上下文
    private Context context;
    // 声明内部类对象
    private ViewHolder vh;

    /**
     * 构造函数
     *
     * @param context
     * @param data
     */
    public ImageAdapter(Context context, List<Map<String, Object>> data) {
        this.context = context;
        this.data = data;
    }

    // 返回的总个数
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    // 返回每个条目对应的数据
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    // 返回的id
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    // 返回这个条目对应的控件对象
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 判断当前条目是否为null
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = View.inflate(context, R.layout.data_grid_item, null);
            vh.iv_image = (ImageView) convertView
                    .findViewById(R.id.iv_grid_image);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        // 获取List集合中的map对象
        Map<String, Object> map = data.get(position);
        // 获取图片的url路径
        final String url = map.get("url").toString();
        // 这里调用了图片加载工具类的setImage方法将图片直接显示到控件上
        System.out.println("图片路径为：" + url);
        GetImageByUrl getImageByUrl = new GetImageByUrl();
        getImageByUrl.setImage(vh.iv_image, url);
        vh.iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(OpenFileIntentUtils.getImageFileIntent(context, url));
                System.out.println("成功打开");
            }
        });
        return convertView;
    }

    /**
     * 内部类 记录单个条目中所有属性
     *
     * @author LeoLeoHan
     *
     */
    //TODO 添加文字的显示
    class ViewHolder {
        public ImageView iv_image;
    }

}