package jp.ac.asojuku.st.batterywarning;

import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private MyBroadcastReceiver mReceiver;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mReceiver,filter);
    }

    @Override protected void onPause(){
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent){

            if(intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)){
                int scale = intent.getIntExtra("scale",0);
                int level = intent.getIntExtra("level",0);
                int status = intent.getIntExtra("status",0);
                String statusString = "";
                switch(status){
                    case BatteryManager.BATTERY_STATUS_UNKNOWN:
                        statusString = "unknown";
                        break;
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        statusString = "charging";
                        break;
                    case BatteryManager.BATTERY_STATUS_DISCHARGING:
                        statusString = "discharging";
                        break;
                    case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                        statusString = "not charging";
                        break;
                    case BatteryManager.BATTERY_STATUS_FULL:
                        statusString = "full";
                        break;
                }
                Notification.Builder builder = new Notification.Builder(getApplicationContext());
                builder.setSmallIcon(R.drawable.icon);

                builder.setContentTitle("バッテリー監視"); // 1行目
                builder.setContentText("バッテリーが15%以下です！"); // 2行目

                builder.setTicker("Battery Warning"); // 通知到着時に通知バーに表示(4.4まで)
// 5.0からは表示されない

                NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());

                String title = "Battery Watch";
                String msg = statusString + " " + level + "/" + scale;
                Log.v(title, msg);


                if(level <= 20){
                    manager.notify(1, builder.build());
                }

                Activity mainActivity = (Activity)context;
                TextView tvtitle =  (TextView) mainActivity.findViewById(R.id.tvtitle);
                tvtitle.setText(title);

                TextView tvMsg = (TextView) mainActivity.findViewById(R.id.tvMsg);
                tvMsg.setText(msg);

                Toast.makeText(mainActivity,msg,Toast.LENGTH_SHORT).show();
            }
        }
    };
}
