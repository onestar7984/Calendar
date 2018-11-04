package onestar.calendar;

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

import java.util.ArrayList;
import java.util.Calendar;

public class Week extends Fragment implements View.OnClickListener, View.OnTouchListener{

    private Calendar thisMonthCalendar;
    private DayInfo dayInfo;
    private ArrayList<DayInfo> daylist;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TextView tv_title;
    private TextView tv_title2;
    private Button leftButton;
    private Button rightButton;
    private WeekAdapter weekAdapter;
    private float firstX;
    private float lastX;
    private float firstY;
    private float lastY;
    private boolean isFlick = false;
    private DBHelper dbHelper;
    private ArrayList<DayInfo> resultDayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_week, container, false);
    }
    @Override
    public void onResume(){
        super.onResume();
        recyclerView = (RecyclerView) getView().findViewById(R.id.gridview_weekly);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getView().getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setOnTouchListener(this);
        tv_title = (TextView) getView().findViewById(R.id.fw_Title_date1);
        tv_title2 = (TextView) getView().findViewById(R.id.fw_Title_date2);
        leftButton = (Button) getView().findViewById(R.id.fw_leftButton);
        rightButton = (Button) getView().findViewById(R.id.fw_rightButton);
        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        daylist = new ArrayList<DayInfo>();
        resultDayList = new ArrayList<DayInfo>();
        thisMonthCalendar = Calendar.getInstance();
        getCalendar(thisMonthCalendar);
    }
    private void getCalendar(Calendar calendar){
        daylist.clear();
        int dayNum = -(calendar.get(Calendar.DAY_OF_WEEK));
        calendar.add(Calendar.DAY_OF_WEEK, (dayNum+1));
        int year = (int)calendar.get(Calendar.YEAR);
        int month = (int)calendar.get(Calendar.MONTH);
        int date = (int)calendar.get(Calendar.DATE);
        if(dbHelper == null){
            dbHelper = new DBHelper(getView().getContext(), "Schedule", null, 1);
        }
        Calendar tmp = Calendar.getInstance();
        tmp.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        tmp.add(Calendar.DATE, +6);
        dbHelper = new DBHelper(getView().getContext(), "Schedule", null, 1);
        resultDayList = dbHelper.getWeekResult(year, month+1 ,date , tmp.get(Calendar.YEAR),tmp.get(Calendar.MONTH)+1,tmp.get(Calendar.DATE));
        String title;
        int resultPoint = 0;
        tv_title.setText("" + year);
        for(int y = 0; y < 7; y++){
            dayInfo = new DayInfo();
            dayInfo.setYear(calendar.get(Calendar.YEAR));
            dayInfo.setMonth(calendar.get(Calendar.MONTH));
            dayInfo.setDate(calendar.get(Calendar.DATE));
            dayInfo = chkSolarHoliday(dayInfo);
            dayInfo = chklunarHoliday(dayInfo);
            if(resultPoint < resultDayList.size()) {
                if (resultDayList.get(resultPoint).getDate() == dayInfo.getDate()) {
                    dayInfo.set_id(resultDayList.get(resultPoint).get_id());
                    dayInfo.setSchedule(resultDayList.get(resultPoint).getSchedule());
                    resultPoint++;
                }
            }
            daylist.add(dayInfo);
            if(y < 6) calendar.add(Calendar.DATE, +1);
        }
        thisMonthCalendar = calendar;
        tv_title2.setText(""+(month+1)+". "+date+". ~ "+(calendar.get(Calendar.MONTH)+1)+ ". "+ calendar.get(Calendar.DATE) + ".");
        dbHelper.close();
        weekAdapter = new WeekAdapter(daylist);
        recyclerView.setAdapter(weekAdapter);
    }

    private Calendar getLastWeekCalendar(Calendar calendar){
        int dayNum = -7;
        calendar.add(Calendar.DAY_OF_WEEK, dayNum);
        return calendar;
    }

    private Calendar getNextWeekCalendar(Calendar calendar){
        int dayNum = 7;
        calendar.add(Calendar.DAY_OF_WEEK, dayNum);
        return calendar;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fw_leftButton:
                thisMonthCalendar = getLastWeekCalendar(thisMonthCalendar);
                getCalendar(thisMonthCalendar);
                break;
            case R.id.fw_rightButton:
                thisMonthCalendar = getNextWeekCalendar(thisMonthCalendar);
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
                    firstY = event.getY();
                }
                break;
            case MotionEvent.ACTION_UP:
                if(isFlick){
                    isFlick = false;
                    lastX = event.getX();
                    lastY = event.getY();
                }

                boolean isOverFilckLength = Math.abs(lastX - firstX) > 300;
                boolean isOverFilckY = Math.abs(lastY - firstY) < 200;
                boolean isRightFlick = lastX - firstX > 0;
                if(isOverFilckY) {
                    if (isOverFilckLength) {
                        if (isRightFlick) {
                            thisMonthCalendar = getLastWeekCalendar(thisMonthCalendar);
                            getCalendar(thisMonthCalendar);
                        } else {
                            thisMonthCalendar = getNextWeekCalendar(thisMonthCalendar);
                            getCalendar(thisMonthCalendar);
                        }
                        return true;
                    }
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
                "12-30"
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
}
