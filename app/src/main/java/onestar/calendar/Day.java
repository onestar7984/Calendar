package onestar.calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ibm.icu.util.ChineseCalendar;

import java.util.Calendar;

public class Day extends Fragment implements View.OnClickListener, View.OnTouchListener{
    private Calendar thisMonthCalendar;
    private RecyclerView recyclerView;
    private TextView tv_day;
    private TextView tv_yearmonth;
    private TextView tv_dayofweek;
    private Button leftButton;
    private Button rightButton;
    private DayAdapter dayAdapter;
    private float firstX;
    private float lastX;
    private boolean isFlick = false;
    private DBHelper dbHelper;
    private DayInfo resultDayInfo;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day, container, false);
    }

    @Override
    public void onResume(){
        super.onResume();
        dbHelper = new DBHelper(getView().getContext(), "Schedule", null, 1);
        recyclerView = (RecyclerView) getView().findViewById(R.id.rcview_daily);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getView().getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setOnTouchListener(this);
        tv_day = (TextView) getView().findViewById(R.id.tv_day_day);
        tv_yearmonth = (TextView) getView().findViewById(R.id.tv_day_yearmonth);
        tv_dayofweek = (TextView) getView().findViewById(R.id.tv_day_dayofweek);
        leftButton = (Button) getView().findViewById(R.id.fd_leftButton);
        rightButton = (Button) getView().findViewById(R.id.fd_rightButton);
        resultDayInfo = new DayInfo();
        thisMonthCalendar = Calendar.getInstance();
        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        getCalendar(thisMonthCalendar);
    }

    private void getCalendar(Calendar calendar){
        int year = (int)calendar.get(Calendar.YEAR);
        int month = (int)calendar.get(Calendar.MONTH);
        int day = (int)calendar.get(Calendar.DAY_OF_MONTH);
        tv_day.setText(""+day);
        tv_yearmonth.setText(""+year+". "+(month+1)+".");
        setKDayOfWeek(tv_dayofweek,calendar.get(Calendar.DAY_OF_WEEK));
        if(dbHelper == null){
            dbHelper = new DBHelper(getView().getContext(), "Schedule", null, 1);
        }
        resultDayInfo = dbHelper.getDayResult(year,month+1,day);
        resultDayInfo = chkSolarHoliday(resultDayInfo);
        resultDayInfo = chklunarHoliday(resultDayInfo);
        if(resultDayInfo.getHoliday() == 1) tv_dayofweek.setBackgroundResource(R.drawable.mydayofweekred);
        dayAdapter = new DayAdapter(resultDayInfo.getSchedule());
        recyclerView.setAdapter(dayAdapter);
    }

    private Calendar getLastDayCalendar(Calendar calendar){
        calendar.add(Calendar.DATE, -1);
        return calendar;
    }

    private Calendar getNextDayCalendar(Calendar calendar){
        calendar.add(Calendar.DATE, +1);
        return calendar;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fd_leftButton:
                thisMonthCalendar = getLastDayCalendar(thisMonthCalendar);
                getCalendar(thisMonthCalendar);
                break;
            case R.id.fd_rightButton:
                thisMonthCalendar = getNextDayCalendar(thisMonthCalendar);
                getCalendar(thisMonthCalendar);
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(!isFlick) {
                    isFlick = true;
                    firstX = event.getX();
                }
                break;
            case MotionEvent.ACTION_UP:
                if(isFlick){
                    isFlick = false;
                    lastX = event.getX();
                }

                boolean isOverFilckLength = Math.abs(lastX - firstX) > 100;
                boolean isRightFlick = lastX - firstX > 0;
                if(isOverFilckLength){
                    if(isRightFlick){
                        thisMonthCalendar = getLastDayCalendar(thisMonthCalendar);
                        getCalendar(thisMonthCalendar);
                    }
                    else{
                        thisMonthCalendar = getNextDayCalendar(thisMonthCalendar);
                        getCalendar(thisMonthCalendar);
                    }
                    return true;
                }
                break;
        }

        return false;
    }

    public DayInfo chkSolarHoliday(DayInfo dayinfo){
        int holiday = 0;

        String[] arrSolar = {
                "1-1",
                "3-1",
                "5-5",
                "6-6",
                "8-15",
                "10-3",
                "10-9",
                "12-25",
        };

        String[] arrSolarStr = {
                "신정",
                "삼일절",
                "어린이날",
                "개천절",
                "광복절",
                "제헌절",
                "한글날",
                "크리스마스"
        };

        int syear = dayinfo.getYear();
        int smonth = dayinfo.getMonth()+1;
        int sdate = dayinfo.getDate();

        for(int x = 0; x < arrSolar.length; x++){
            String tmp = arrSolar[x];
            if(tmp.equals(""+smonth+"-"+sdate)){
                dayinfo.setHoliday(1);
                dayinfo.setSchedule(arrSolarStr[x]);
                break;
            }
        }
        return dayinfo;
    }

    public DayInfo chklunarHoliday(DayInfo dayInfo){

        int holiday = 0;

        String[] arrLunar = {
                "1-1",
                "1-2",
                "4-8",
                "8-14",
                "8-15",
                "8-16",
                "12-31"
        };

        String[] arrLunarStr = {
                "설",
                "",
                "부처님 오신날",
                "",
                "추석",
                "",
                ""
        };
        Calendar calendar = Calendar.getInstance();
        calendar.set(dayInfo.getYear(), dayInfo.getMonth(),dayInfo.getDate());
        ChineseCalendar chineseCalendar = new ChineseCalendar();
        chineseCalendar.setTimeInMillis(calendar.getTimeInMillis());
        int cyear = chineseCalendar.get(ChineseCalendar.EXTENDED_YEAR) - 2637;
        int cmonth = chineseCalendar.get(ChineseCalendar.MONTH) + 1;
        int cdate = chineseCalendar.get(ChineseCalendar.DAY_OF_MONTH);

        for(int x = 0; x < arrLunar.length; x++){
            String tmp = arrLunar[x];
            if(tmp.equals(""+cmonth+"-"+cdate)){
                dayInfo.setHoliday(1);
                if(arrLunarStr[x] != "") {
                    dayInfo.setSchedule(arrLunarStr[x]);
                }
                break;
            }
        }
        return dayInfo;
    }

    public void setKDayOfWeek(TextView tv, int dayofweek){
        if(dayofweek == 6 ){
            tv.setText("토요일");
            tv.setBackgroundResource(R.drawable.mydayofweekblue);
            tv.setTextColor(Color.WHITE);
        }
        else if(dayofweek == 1){
            tv.setText("월요일");
            tv.setBackgroundResource(R.drawable.mydayofweek);
            tv.setTextColor(Color.WHITE);
        }
        else if(dayofweek == 2){
            tv.setText("화요일");
            tv.setBackgroundResource(R.drawable.mydayofweek);
            tv.setTextColor(Color.WHITE);
        }
        else if(dayofweek == 3){
            tv.setText("수요일");
            tv.setBackgroundResource(R.drawable.mydayofweek);
            tv.setTextColor(Color.WHITE);
        }
        else if(dayofweek == 4){
            tv.setText("목요일");
            tv.setBackgroundResource(R.drawable.mydayofweek);
            tv.setTextColor(Color.WHITE);
        }
        else if(dayofweek == 5){
            tv.setText("금요일");
            tv.setBackgroundResource(R.drawable.mydayofweek);
            tv.setTextColor(Color.WHITE);
        }
        else{
            tv.setText("일요일");
            tv.setBackgroundResource(R.drawable.mydayofweekred);
            tv.setTextColor(Color.WHITE);
        }
    }
}