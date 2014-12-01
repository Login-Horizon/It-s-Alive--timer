package test.api_control;

/**
 * Created by Home on 03.11.2014.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.concurrent.TimeUnit;

public class MyService extends Service //какая та магия . вроде понял как работает , но сразу не смогу объяснить
{
    NotificationManager nm;

    @Override
    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            TimeUnit.SECONDS.sleep(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendNotif();
        return super.onStartCommand(intent, flags, startId);
    }

    void sendNotif() {
        // 1-я часть
        Notification notif = new Notification(R.drawable.timert, "Time",
                System.currentTimeMillis());

        // 3-я часть
        Intent intent =new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
                .setComponent(getPackageManager().getLaunchIntentForPackage(getPackageName()).getComponent());
        // new Intent(this, MyActivity.class);

        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // 2-я часть

        notif.setLatestEventInfo(this, "Звонок", "Zvonok", pIntent);

        // ставим флаг, чтобы уведомление пропало после нажатия
        notif.flags |= Notification.FLAG_AUTO_CANCEL;



        // отправляем
        nm.notify(1, notif);

    }

    public IBinder onBind(Intent arg0) {
        return null;
    }
}
