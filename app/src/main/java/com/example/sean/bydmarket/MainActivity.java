package com.example.sean.bydmarket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Boolean IS_ACCEPT = false;  //判断广播是否发送成功
    private IntentFilter intentFilter;
    private MyReceiver myReceiver;
    private List<AppS> appSList = new ArrayList<>();
    private LinearLayout mProgressBar;
    private Timer timer;  //定时器实例
    private TimeoutManage timeoutManage;  //超时处理类的实例
    private int COUNT = 0;  //记录发送的广播次数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = (LinearLayout) findViewById(R.id.loading_progress);

        showProgress();
        AppS a1 = new AppS("百度云", R.drawable.baiducloud, 1, "近似结果");
        AppS a2 = new AppS("百度地", R.drawable.baidu, 2, "近似结果");
        AppS a3 = new AppS("百度地卖", R.drawable.baiduwaimai, 3, "近似结果");
        AppS a4 = new AppS("百度地图", R.drawable.baiduditu, 4, "近似结果");
        AppS a5 = new AppS("百度语音", R.drawable.baiduaudio, 5, "近似结果");
        AppS a6 = new AppS("百度翻译", R.drawable.baidufanyi, 6, "近似结果");

        appSList.add(a1);
        appSList.add(a2);
        appSList.add(a3);
        appSList.add(a4);
        appSList.add(a5);
        appSList.add(a6);

        String result = "百度地图";
        sortAppS(appSList, result);  //排序，按相似度排列

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);  //获得recycler view的控件
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager); //设置recycler view控件的布局方式
        AppSAdapter appSAdapter = new AppSAdapter(appSList, MainActivity.this);  //适配器加载数据
        recyclerView.setAdapter(appSAdapter);  //适配器与主界面关联

        /*
         * 注册并接收返回的广播
         * */
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.icoolme.android.BEST_WEATHER_REPLY_BROADCAST");
        myReceiver = new MyReceiver();
        registerReceiver(myReceiver, intentFilter);

    }

    public synchronized Boolean getIS_ACCEPT() {
        return IS_ACCEPT;
    }

    public synchronized void setIS_ACCEPT(Boolean IS_ACCEPT) {
        this.IS_ACCEPT = IS_ACCEPT;
    }


    //广播接收器
    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            /*
             * 如果接收到广播，则做下面的动作
             * */
            if (intent.getAction().equals("com.icoolme.android.BEST_WEATHER_REPLY_BROADCAST")) {
                Bundle bundle1 = intent.getBundleExtra("weatherWarningMsg");
                setIS_ACCEPT(bundle1.getBoolean("isAccept"));  //获取是否接收到的标志符
                Toast.makeText(MainActivity.this, "发送广播成功", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //展示加载进度
    private void showProgress() {

        mProgressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.GONE);
            }
        }, 2000);   //延时两秒
    }

    /*
     * 模拟最美天气发送广播
     * */
    public void send(String appName) {

        final Intent intent = new Intent(
                "com.icoolme.android.BEST_WEATHER_WARNING_BROADCAST"
        );

        Bundle bundle = new Bundle();
        bundle.putString("msgId", "1001");
        bundle.putString("sendResource", "BYDMarket");
        bundle.putString("appResult", "安装成功");
        bundle.putString("appName", appName);
        bundle.putString("sendTime", "2018-10-11-15:30");
        bundle.putString("errCode", "null");
        intent.putExtra("appIntallMsg", bundle);

        /*
         * 定时器
         * */
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    if (getIS_ACCEPT().equals(false)) {
                        if (COUNT >= 3) {  //如果广播发送超过3次，则进行超时处理
                            timer.cancel();
                            timeoutManage = new TimeoutManage();
                            timeoutManage.display();
                        }
                        //IS_ACCEPT = true;

                        sendBroadcast(intent);
                        COUNT += 1; //如果广播没有发送成功，COUNT+1
                        //Log.d("广播发送状态：","发送次数" + COUNT);
                    } else if (getIS_ACCEPT().equals(true)) {  //如果广播发送成功，取消定时器任务
                        timer.cancel();
                        //Log.d("广播发送状态：","发送成功," + "发送次数：" + COUNT);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task, 0, 3000);  //开启定时任务，每3秒查询一次广播是否发送成功
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
        timer.cancel();  //取消定时器
    }

    /*
     * 把结果按照最相似的顺序排序出来,因为这里的list是全局变量，所以能改变
     * */
    private void sortAppS(List<AppS> appSList1, String result) {   //缺点：result字符串要比需要比较的字符串长，才有明显效果
        for(int i=0; i<appSList1.size(); i++) {
            appSList1.get(i).setId(SimilarityUtil.getmInstance().Compare_Distance(result, appSList1.get(i).getName()));  //计算最小编辑距离
        }
        sortById(appSList1);

        //SimilarityUtil.getmInstance().Compare_Distance()

      /*  List<AppS> appSList2 = appSList1;
        String result1 = result;
        int alength = result1.length();
        //思路：用substring截取字符串
        for (int i = 0; i < alength; i++) {
            if (appSList2.get(i).getName().equals(result1)) {
                break;
            }
            for (int j = i + 1; j < appSList2.size(); j++) {
                if (appSList2.get(j).getName().equals(result1)) {
                    Collections.swap(appSList2, i, j);
                }
            }
            if (alength > 1) {
                result1 = result1.substring(0, alength - i - 1);  //
            }
        }*/

    }

    public void sortById(List<AppS> appSList) {
       Collections.sort(appSList);
    }

}
