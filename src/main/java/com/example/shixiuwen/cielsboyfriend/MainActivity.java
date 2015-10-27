package com.example.shixiuwen.cielsboyfriend;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shixiuwen.entities.ChatMessage;
import com.example.shixiuwen.httputil.HttpUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private List<ChatMessage> mDatas;
    private ListView mMsgs;
    private ChatMessageAdapter mAdapter;

    private EditText mInputMsg;
    private Button mSendMsg;
    private Date date = null;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //等待接受，子线程完成数据的返回
            ChatMessage fromMessage = (ChatMessage) msg.obj;
            mDatas.add(fromMessage);
            mAdapter.notifyDataSetChanged();
            mMsgs.setSelection(mDatas.size() - 1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initDatas();
        //初始化事件

        initListener();


    }

    //判断是否有网络连接
    private boolean isConnect(){
        boolean flag = false;
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo().isAvailable()){
            flag = true;
        }
        return flag;
    }

    private void initView() {
        mMsgs = (ListView) findViewById(R.id.id_listview_msgs);
        mInputMsg = (EditText) findViewById(R.id.id_input_mag);
        mSendMsg = (Button) findViewById(R.id.id_send_msg);


    }

    private void initDatas() {
        mDatas = new ArrayList<ChatMessage>();
        mDatas.add(new ChatMessage("Ciel你好，你又来看我了~ ^_^", ChatMessage.Type.INCOME, new Date()));
        mAdapter = new ChatMessageAdapter(this, mDatas);
        mMsgs.setAdapter(mAdapter);
    }

    private void initListener() {
        mSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String toMsg = mInputMsg.getText().toString();
                if (TextUtils.isEmpty(toMsg)) {
                    Toast.makeText(MainActivity.this, "你还什么都没说哦~", Toast.LENGTH_SHORT).show();
                    return;
                }
                ChatMessage toMessage = new ChatMessage();
                toMessage.setDate(new Date());
                toMessage.setMsg(toMsg);
                toMessage.setType(ChatMessage.Type.OUTCOME);
                mDatas.add(toMessage);
                mAdapter.notifyDataSetChanged();
                mMsgs.setSelection(mDatas.size() - 1);

                mInputMsg.setText("");

                new Thread() {
                    @Override
                    public void run() {
                        HttpUtils hpp = new HttpUtils();
                        if(hpp!=null){
                            ChatMessage fromMessage = hpp.sendMessage(toMsg);
                            Message m = new Message();
                            m.obj = fromMessage;
                            mHandler.sendMessage(m);
                        }else{
                            Toast.makeText(MainActivity.this,"没网的话我拒绝回答任何问题",Toast.LENGTH_SHORT).show();
                        }

                    }
                }.start();
            }
        });
    }
}
