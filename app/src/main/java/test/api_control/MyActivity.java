package test.api_control;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MyActivity extends Activity {
    int DIALOG_TIME = 1;
    static String TAG = "Testing";

    // на прошедшее время, это просто для каркаса что бы развить в будущем
    TextView start_time; // начальное время
    TextView timer;// таймер для каждого звока
    List<Date> time_list_most;//список времень будильника
    String output;//
    static String text = "ok";// штуковина для форматирования времни , внизу еесть объяснение
    private NotificationManager nm;
    static int chislo = 0;
    List<Time> alar_t;
    static int myHour = 0;
    static int myMinute = 0;
    static long curTime = System.currentTimeMillis();

    static Date myDate = new Date(curTime);//время будильника , внизу получит свои значения
    static Date Last_d = new Date(curTime);

    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Workoncre");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        start_time = (TextView) findViewById(R.id.tvTime);
        timer = (TextView) findViewById(R.id.Short_time);

        nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Log.i(TAG, "Work");
        Log.e(TAG, "Error onCreate");


    }

    public String formatTime(long millis)
    //метод для таймера времени (форматирует милли секунды в часы и минуты)
    // (статус:стырен)
    // проверял по отдельности (все работает)

    {
        Log.i(TAG, "Workformat");


        output = "";
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        seconds = seconds % 60;
        minutes = minutes % 60;
        hours = hours % 60;

        String secondsD = String.valueOf(seconds);
        String minutesD = String.valueOf(minutes);
        String hoursD = String.valueOf(hours);

        if (seconds < 10)
            secondsD = "0" + seconds;
        if (minutes < 10)
            minutesD = "0" + minutes;

        if (hours < 10)
            hoursD = "0" + hours;

        output = hoursD + " : " + minutesD + " : " + secondsD;

        return output;
    }


    // для часов
    public void onclick(View view) {
        showDialog(DIALOG_TIME);

    }

    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_TIME) {
            Log.d(TAG, "deb");
            Log.i(TAG, "Work");
            Log.e(TAG, "Error onCreateDialog");

            TimePickerDialog tpd = new TimePickerDialog(this, AlertDialog.THEME_HOLO_DARK, alarm_time, 13, 45, true);
            return tpd;
        }
        if (id == 42) {
            final String[] mLessons_num = {"1", "2", "3", "4"};
            Log.i(TAG, "Work ");
            Log.e(TAG, "Error onCreateDialog");

            AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
            builder.setTitle("Number of lessons "); // заголовок для диалога

            Log.e(TAG, "Error onCreateDialog");

            builder.setItems(mLessons_num, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    // TODO Auto-generated method stub
                    if (mLessons_num[item] == "1") {
                        chislo = 1;
                    } else if (mLessons_num[item] == "2") {
                        chislo = 2;
                    } else if (mLessons_num[item] == "3") {
                        chislo = 3;
                    } else {
                        chislo = 4;
                    }


                }
            });
            Log.e(TAG, "Error onCreateDialog");

            builder.setCancelable(false);
            return builder.create();
        }


        return super.onCreateDialog(id);
    }

    // внизу метод отвечаюший за переопроделение значений при каждос выборе времени
    // проверял по отдельности (все работает)
    OnTimeSetListener alarm_time = new OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Log.d("alarm_time", "start");
            Log.e(TAG, "Error alarm_time");
            if (hourOfDay>12){
                myHour = hourOfDay-12;
            }else {
            myHour = hourOfDay;}
            myMinute = minute;
            Time now = new Time();
            now.setToNow();
            now.hour=hourOfDay;
            now.minute=minute;
          //  now.set(0,minute,hourOfDay);
          //  myDate.setMinutes(minute);
           // myDate.setHours(myHour);

            start_time.setText("Start:" +now.format("%d.%m.%Y %H.%M.%S"));
            showDialog(42);
            Log.d("alarm_time", "end");

        }
    };


    public void Start(View view)//метод кнопки старта
    {
        Log.i(TAG, "Work");
        Log.e(TAG, "Error Start");


        int mytestHour = myHour + ((90 + 5 + 10) * chislo - 10 + myMinute) / 60;
        int mytestMinute = ((90 + 5 + 10) * chislo - 10 + myMinute) % 60;
        Last_d.setMinutes(mytestMinute);
        Last_d.setHours(mytestHour);
        String string1 = "" + mytestHour;
        Log.e(TAG, "Error Start");
         Date curDate = new Date(System.currentTimeMillis());//  время устройство
        curDate.setHours(curDate.getHours()-12);


        if (curDate.getTime() > Last_d.getTime()) {
            Toast.makeText(this, "cur" + formatTime(curDate.getTime()), Toast.LENGTH_SHORT).show();

            new Intent(getApplicationContext(), MyActivity.class);


            Toast.makeText(this, "please write current time", Toast.LENGTH_SHORT).show();
        } else {


            time_list_most = adventure_time(myHour, myMinute, chislo,curDate);// список времен звонков
            Log.e(TAG, "Error Start");

            time_calcul(time_list_most);//метод для звонка
            Log.e(TAG, "Error Start");
        }
    }


    public void time_calcul(final List<Date> list)
    //вычисление и подача сигнала сервису уведомленя
    // н совсем понятно работает ли корректно
    {
        Log.e(TAG, "Error time_calcul");


        if (list.size() == 0)// проблемная зона , эта часть если закоменнтировать то раблтает
        // если даже лист присвоит null все равно не раблотает вылетает с фатал еррором
        {
            Log.i(TAG, "Work");
            Log.e(TAG, "Error time_calcul");

            Toast.makeText(getApplicationContext(), "Dobby is free", Toast.LENGTH_LONG).show();

        } else {
            Log.i(TAG, "Work");
            Log.e(TAG, "Error time_calcul");

            long ch = list.get(0).getTime();

            java.text.DateFormat dateFormat =
                    android.text.format.DateFormat.getDateFormat(getApplicationContext());
            Toast.makeText(this, "time now" + dateFormat.format(list.get(0).getTime()), Toast.LENGTH_SHORT).show();
            AlarmManager am =  (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));
            PendingIntent pintent = PendingIntent.getBroadcast(this, 0, new Intent(getApplicationContext(), MyService.class), 0);
            am.set(AlarmManager.RTC, list.get(0).getTime(),pintent);
            list.remove(0);
            time_calcul(list);
//            new CountDownTimer(ch, 1000) {
//
//                public void onTick(long millisUntilFinished) {
//                    timer.setText(formatTime(millisUntilFinished));
//
//
//                }
//
//                public void onFinish() {
//
//                    startService(new Intent(getApplicationContext(), MyService.class));
//                    //класс для возврата из уведомления в основной активити (реализован: скоро покажу)
//                    list.remove(0);
//                    time_calcul(list);
//                }
//            }.start();

        }
    }


      public List<Date> adventure_time(int MyHour, int Myminute, int quant, Date trudate)
    //конверт минуты в милли секунды и количество пар
    // проверял по отдельности (нобез массива (все работало)
    {
        List<Date> dates = new ArrayList<Date>();


        Date first_time = new Date(System.currentTimeMillis());
        //  Toast.makeText(this, "first_time" + formatTime(first_time.getHours()), Toast.LENGTH_SHORT).show();

        first_time.setMinutes(Myminute);
        first_time.setHours(MyHour);
        dates.add((Date)first_time.clone());

        for (int i = 0; i < quant; i++) {

            first_time.setMinutes(first_time.getMinutes() + 45);

            dates.add( (Date) first_time.clone());

            first_time.setMinutes(first_time.getMinutes() + 5);
            dates.add((Date) first_time.clone());
            first_time.setMinutes(first_time.getMinutes() + 45);
            //   Toast.makeText(this, "time" + formatTime(dates.get(0).getTime()), Toast.LENGTH_SHORT ).show();
            dates.add((Date) first_time.clone());
            //   Toast.makeText(this, "time" + formatTime(dates.get(0).getTime()), Toast.LENGTH_SHORT ).show();
            if (i != quant-1) {
                first_time.setMinutes(first_time.getMinutes() + 10);
                dates.add((Date) first_time.clone());


            }
            //     Toast.makeText(this, "first_time" + formatTime(first_time.getTime()), Toast.LENGTH_SHORT).show();
        }
        int i = 0;
        while (i<dates.size()){
            if (trudate.getTime()>dates.get(0).getTime()){
                dates.remove(0);
                i++;
            }
            else {
                break;
            }
        }
        return dates;
    };

}