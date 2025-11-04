package com.example.smartalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventsRecycleAdapter extends RecyclerView.Adapter<EventsRecycleAdapter.MyviewHolder>  {
    Context context;
    ArrayList<Events>arrayList ;
    DBOpenHelper dbOpenHelper;
     public EventsRecycleAdapter(Context context,ArrayList<Events>arrayList){
         this.context = context;
         this.arrayList = arrayList;
     }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_rowlayout,parent,false);
        return new MyviewHolder(view);
    }
    @Override
            public void onBindViewHolder(final MyviewHolder holder,final int position){

        //db.openDatabase();
       final Events events = arrayList.get(position);
        holder.Event.setText(events.getEVENT());
        holder.DateTxt.setText(events.getDATE());
        holder.Time.setText(events.getTIME());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCalendarEvent(events.getEVENT(),events.getDATE(), events.getTIME());
                arrayList.remove(position);
                notifyDataSetChanged();


            }
        });

        if(isAlarm(events.getDATE(),events.getEVENT(),events.getTIME())){
            holder.setAlarm.setImageResource(R.drawable.ic_action_action_notification_active);
           // notifyDataSetChanged();
        }
        else {

            holder.setAlarm.setImageResource(R.drawable.ic_action_notification_off);
            //notifyDataSetChanged();
        }
        Calendar datecalendar = Calendar.getInstance();
        datecalendar.setTime(ConvertStringToDate(events.getDATE()));
        int alarmYear = datecalendar.get(Calendar.YEAR);
        int alarmMonth = datecalendar.get(Calendar.MONTH);
        int alarmDay = datecalendar.get(Calendar.DAY_OF_MONTH);

        Calendar timecalendar = Calendar.getInstance();
        timecalendar.setTime(ConvertStringToTime(events.getTIME()));
        int alarmHour = timecalendar.get(Calendar.HOUR_OF_DAY);
        int alarmMinute = timecalendar.get(Calendar.MINUTE);
        holder.setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAlarm(events.getDATE(),events.getEVENT(),events.getTIME())){
                    holder.setAlarm.setImageResource(R.drawable.ic_action_notification_off);
                    cancelAlarm(getRequestCode(events.getDATE(),events.getEVENT(), events.getTIME()));
                    updateEvent(events.getDATE(),events.getEVENT(), events.getTIME(),"off");
                    notifyDataSetChanged();

                }
                else{
                    holder.setAlarm.setImageResource(R.drawable.ic_action_action_notification_active);
                    Calendar alarmCalendar = Calendar.getInstance();
                    alarmCalendar.set(alarmYear,alarmMonth,alarmDay,alarmHour,alarmMinute);
                    setAlarm(alarmCalendar,events.getEVENT(),events.getDATE(),getRequestCode(events.getDATE(),
                            events.getEVENT(), events.getTIME()));
                    updateEvent(events.getDATE(),events.getEVENT(), events.getTIME(),"on");
                    notifyDataSetChanged();



                }


            }
        });

    }
    @Override
       public int getItemCount(){
        return arrayList.size();
    }


    public class MyviewHolder extends RecyclerView.ViewHolder{
        TextView DateTxt,Event,Time;
        Button delete;
       // TextView eventDate,eventName,eventime;
        ImageButton setAlarm;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            DateTxt =(TextView)itemView.findViewById(R.id.eventDate);
            Event = (TextView)itemView.findViewById(R.id.eventName);
            Time = (TextView)itemView.findViewById(R.id.eventime);
            delete = itemView.findViewById(R.id.delete);
            setAlarm = itemView.findViewById(R.id.alarmmeBtn);
            //eventDate =(TextView)itemView.findViewById(R.id.eventDate);
            //eventName = (TextView)itemView.findViewById(R.id.eventName);
           // eventime = (TextView)itemView.findViewById(R.id.eventime);
        }
    }

    private Date ConvertStringToDate(String eventDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = null;
        try{
            date = format.parse(eventDate);
        }
        catch (ParseException e){
            e.printStackTrace();

        }
        return date;
    }

    private Date ConvertStringToTime(String eventDate){
        SimpleDateFormat format = new SimpleDateFormat("kk:mm", Locale.ENGLISH);
        Date date = null;
        try{
            date = format.parse(eventDate);
        }
        catch (ParseException e){
            e.printStackTrace();

        }
        return date;
    }



    private void deleteCalendarEvent(String event,String date,String time){
         dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.deleteEvent(event,date,time,database);
        dbOpenHelper.close();
    }
    private boolean isAlarm(String date,String event,String time)
    {
        boolean alarmed = false;
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadIDEvents(date,event,time,database);
        while (cursor.moveToNext()){
            String notify = cursor.getString(cursor.getColumnIndex(DBStructure.Notify));
            if(notify.equals("on")){
                alarmed = true;

            }
            else{
                alarmed = false;
            }
        }
        cursor.close();
        dbOpenHelper.close();
        return  alarmed;

    }
    private void setAlarm(Calendar calendar,String event,String time,int RequestCode)
    {
        Intent intent = new Intent(context.getApplicationContext(),AlarmReceiver.class);
        intent.putExtra("event",event);
        intent.putExtra("time",time);
        intent.putExtra("id",RequestCode);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,RequestCode,intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager)context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);



    }
    private void cancelAlarm( int RequestCode)
    {
        Intent intent = new Intent(context.getApplicationContext(),AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,RequestCode,intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager)context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);



    }
    private int getRequestCode(String date,String event,String time)
    {
        int code = 0;
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadIDEvents(date,event,time,database);
        while (cursor.moveToNext()){
            code = cursor.getInt(cursor.getColumnIndex(DBStructure.ID));
        }
        cursor.close();
        dbOpenHelper.close();
        return code;


    }
    private void updateEvent(String date,String event,String time,String notify){
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.updateEvent(date,event,time,notify,database);
        dbOpenHelper.close();

    }


}
