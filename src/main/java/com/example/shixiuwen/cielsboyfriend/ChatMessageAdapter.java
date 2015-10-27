package com.example.shixiuwen.cielsboyfriend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shixiuwen.entities.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by shixiuwen on 15-10-27.
 */
public class ChatMessageAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ChatMessage> mDatas;

    //初始化一个时间
    String time = "";

    public ChatMessageAdapter(Context context,List<ChatMessage> mDatas){
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ChatMessage chatMessage = mDatas.get(position);
        ViewHolder viewHolder = null;
        //说明是第一次加载到ListView，没有缓存
        if(convertView == null){
            if(getItemViewType(position)==0){
                convertView = mInflater.inflate(R.layout.list_item_amos, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.mDates = (TextView) convertView.findViewById(R.id.id_from_msg_date);
                viewHolder.mMsg = (TextView) convertView.findViewById(R.id.id_from_msg_info);
            }else{
                convertView = mInflater.inflate(R.layout.list_item_myself, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.mDates = (TextView) convertView.findViewById(R.id.id_to_msg_date);
                viewHolder.mMsg = (TextView) convertView.findViewById(R.id.id_to_msg_info);
            }
            convertView.setTag(viewHolder);

        }else{
            //convertView不为空，说明有缓存，复用
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String format = df.format(chatMessage.getDate());
        if(format.equals(time)){
            viewHolder.mDates.setText("");
        }else{
            viewHolder.mDates.setText(format);
            time = format;
        }

        viewHolder.mMsg.setText(chatMessage.getMsg());
        return convertView;
    }

    //获取数据类型,判断是发送的还是接收的
    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = mDatas.get(position);
        if(chatMessage.getType()== ChatMessage.Type.INCOME){
            return 0;
        }
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    private final class ViewHolder{
        TextView mDates;
        TextView mMsg;
    }

}
