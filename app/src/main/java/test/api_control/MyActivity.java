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
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MyActivity extends Activity {

    int DIALOG_TIME = 1;
    static String TAG = "Testing";
    static Time now = new Time();

    // на прошедшее время, это просто для каркаса что бы развить в будущем
    TextView start_time; // начальное время
    TextView timer;// таймер для каждого звока
    String output;//
    static String text = "ok";// штуковина для форматирования времни , внизу еесть объяснение
    private NotificationManager nm;
    static int chislo = 0;
    static int myHour = 0;
    static int myMinute = 0;
    Calendar calNow = Calendar.getInstance();
    Calendar calSet = (Calendar) calNow.clone();
    List<Calendar> timeList;



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

  static   public String formatTime(long millis)
    {
        Log.i(TAG, "Workformat");


        String output = "";
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

            myHour = hourOfDay;
            myMinute = minute;

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);

            //  now.set(0,minute,hourOfDay);
            //  myDate.setMinutes(minute);
            // myDate.setHours(myHour);

            start_time.setText("Start:" + calSet.getTime());
            showDialog(42);
            Log.d("alarm_time", "end");

        }
    };


    public void Start(View view)//метод кнопки старта
    {
        Log.i(TAG, "Work");
        Log.e(TAG, "Error Start");



        Log.e(TAG, "Error Start");



        if (back_time_list(myHour,myMinute,chislo).size() == 0) {


            Intent refresh = new Intent(this, MyActivity.class);
            startActivity(refresh);
            this.finish(); //


            Toast.makeText(this, "please write current time", Toast.LENGTH_SHORT).show();
        } else {


            // список времен звонков
            Log.e(TAG, "Error Start");

            time_calcul(back_time_list(myHour, myMinute, chislo));//метод для звонка
            Log.e(TAG, "Error Start");
        }
    }

    public void time_calcul(final List<Calendar> list)
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
            Calendar myDate = Calendar.getInstance();

            long ch = list.get(0).getTimeInMillis() -  myDate.getTimeInMillis() ;


        //    Toast.makeText(this, "time now" + formatTime(dates.get(0).getTime()), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "time now" + formatTime(myDate.getTimeInMillis()), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "time set" + formatTime(ch), Toast.LENGTH_SHORT).show();

         new CountDownTimer(ch, 1000) {

                public void onTick(long millisUntilFinished) {
                    timer.setText(formatTime(millisUntilFinished));


                }

                public void onFinish() {

                    startService(new Intent(getApplicationContext(), MyService.class));
                    //класс для возврата из уведомления в основной активити (реализован: скоро покажу)
                    list.remove(0);
                    time_calcul(list);
                }
            }.start();

        }
    }



    private List<Calendar> back_time_list( int sethours, int setminute, int mquant) {
        Calendar mcalNow = Calendar.getInstance();
        Calendar mcalSet = (Calendar) mcalNow.clone();

        mcalSet.set(Calendar.HOUR_OF_DAY, sethours);
        mcalSet.set(Calendar.MINUTE, setminute);
        mcalSet.set(Calendar.SECOND, 0);
        mcalSet.set(Calendar.MILLISECOND, 0);

        List<String> mTime_list = new ArrayList<String>();
        List<Calendar> quasi_date = new ArrayList<Calendar>();





        long util;




        quasi_date.add((Calendar) mcalSet.clone());


        for (int i = 0; i < mquant; i++) { //add alarm_time in  list


            mcalSet.add(Calendar.MINUTE, +5);

            quasi_date.add((Calendar)mcalSet.clone());
            mcalSet.add(Calendar.MINUTE, +1);

            quasi_date.add((Calendar)mcalSet.clone());
            mcalSet.add(Calendar.MINUTE, +5);

            quasi_date.add((Calendar)mcalSet.clone());
            if (i < mquant-1) { //add large break time
                mcalSet.add(Calendar.MINUTE, +1);

                quasi_date.add((Calendar)mcalSet.clone());

            }
            Toast.makeText(this,quasi_date.get(quasi_date.size()-1).getTime().toString(),Toast.LENGTH_SHORT);


        }






        int i = 0;
        while (i<quasi_date.size()){
            if (mcalNow.compareTo(quasi_date.get(0))<0){
                quasi_date.remove(0);
                i++;
            }
            else {
                break;
            }
        }
        return quasi_date;    };


    ;

}