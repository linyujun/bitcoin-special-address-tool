package com.bitcoin.linyujun;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final static String ACTION_RES = "com.linyujun.res";

    private EditText editText;
    private TextView textView;
    private TextView tvCount;
    private Button button;

    public static boolean stop = true;

    private List<String> keyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.edit);
        textView = findViewById(R.id.text);
        button = findViewById(R.id.button);
        tvCount = findViewById(R.id.tv_count);

        if (!stop) {
            button.setText("暂停");
        }

        //注册广播
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_RES);
        mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, intentFilter);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!stop) {
                    //暂停
                    button.setText("开始");
                    Intent intent = new Intent(MainActivity.this.getApplicationContext(), KeepAliveService.class);
                    stopService(intent);
                } else {
                    //开始
                    String keys = editText.getText().toString().trim();
                    keyList = splitStringBySpace(keys);
                    if (keyList.size() == 0) {
                        Toast.makeText(MainActivity.this, "关键词不能为空", Toast.LENGTH_LONG).show();
                        return;
                    }
                    button.setText("暂停");
                    ArrayList k = new ArrayList<String>();
                    k.addAll(keyList);
                    Intent intent = new Intent(MainActivity.this.getApplicationContext(), KeepAliveService.class);
                    intent.putStringArrayListExtra("keys", k);
                    startService(intent);
                }
                stop = !stop;

            }
        });
    }

    private LocalBroadcastManager mLocalBroadcastManager;
    private MyBroadcastReceiver mBroadcastReceiver;
    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_RES.equals(intent.getAction())) {
                String key = intent.getStringExtra("key");
                if ("res".equals(key)) {
                    textView.setText(intent.getStringExtra(key));
                } else if ("count".equals(key)) {
                    tvCount.setText(intent.getStringExtra(key));
                }
            }
        }
    }

    public List<String> splitStringBySpace(String str) {
        List<String> arrayList = new ArrayList<>();
        if (!TextUtils.isEmpty(str)) {
            String[] params = str.split(" ");
            arrayList = Arrays.asList(params);
        }
        return arrayList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销广播
        mLocalBroadcastManager.unregisterReceiver(mBroadcastReceiver);
    }
}