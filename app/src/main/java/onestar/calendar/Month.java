package onestar.calendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.ibm.icu.util.ChineseCalendar;

import java.util.ArrayList;
import java.util.Calendar;

public class Month extends Fragment implements View.OnClickListener, View.OnTouchListener{
    private Calendar thisMonthCalendar;
    private DayInfo dayInfo;
    private ArrayList<DayInfo> daylist;
    private GridView gridView;
    private TextView tv_title;
    private Button leftButton;
    private Button rightButton;
    private MonthAdapter monthAdapter;
    private float firstX;
    private float lastX;
    private boolean isFlick = false;
    private DBHelper dbHelper;
    private ArrayList<DayInfo> resultDayList;
    private int targetYear;
    private int targetMonth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_month, container, false);
    }

    @Override
    public void onResume(){
        super.onResume();
        dbHelper = new DBHelper(getView().getContext(), "Schedule", null, 1);
        gridView = (GridView) getView().findViewById(R.id.gridview_monthly);
        gridView.setOnTouchListener(this);
        tv_title = (TextView) getView().findViewById(R.id.fm_Title);
        leftButton = (Button) getView().findViewById(R.id.fm_leftButton);
        rightButton = (Button) getView().findViewById(R.id.fm_rightButton);
        daylist = new ArrayList<DayInfo>();
        resultDayList = new ArrayList<DayInfo>();
        thisMonthCalendar = Calendar.getInstance();
        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        getCalendar(thisMonthCalendar);
    }

    private void getCalendar(Calendar calendar){
        daylist.clear();
        int year = (int)calendar.get(Calendar.YEAR);
        int month = (int)calendar.get(Calendar.MONTH);
        tv_title.setText(""+year+". "+(month+1)+".");
        calendar.set(year, month, 1);
        int dayNum = calendar.get(Calendar.DAY_OF_WEEK);
        for(int x = 1; x < dayNum; x++){
            dayInfo = new DayInfo();
            daylist.add(dayInfo);
        }
        if(dbHelper == null){
            dbHelper = new DBHelper(getView().getContext(), "Schedule", null, 1);
        }
        resultDayList = dbHelper.getMonthResult(year,month+1,calendar.getMaximum(Calendar.DAY_OF_MONTH));
        int resultPoint = 0;
        for(int y = 0; y < calendar.getActualMaximum(Calendar.DAY_OF_MONTH); y++){
            dayInfo = new DayInfo();
            dayInfo.setYear(year);
            dayInfo.setMonth(month);
            dayInfo.setDate(y+1);
            dayInfo = chkSolarHoliday(dayInfo);
            dayInfo = chklunarHoliday(dayInfo);
            if(resultPoint < resultDayList.size()) {
                if (resultDayList.get(resultPoint).getDate() == y + 1) {
                    dayInfo.set_id(resultDayList.get(resultPoint).get_id());
                    dayInfo.setSchedule(resultDayList.get(resultPoint).getSchedule());
                    resultPoint++;
                }
            }
            daylist.add(dayInfo);
        }
        monthAdapter = new MonthAdapter(getContext(), daylist);
        gridView.setAdapter(monthAdapter);
    }

    private Calendar getLastMonthCalendar(Calendar calendar){
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, -1);
        return calendar;
    }

    private Calendar getNextMonthCalendar(Calendar calendar){
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, +1);
        return calendar;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fm_leftButton:
                thisMonthCalendar = getLastMonthCalendar(thisMonthCalendar);
                getCalendar(thisMonthCalendar);
                break;
            case R.id.fm_rightButton:
                thisMonthCalendar = getNextMonthCalendar(thisMonthCalendar);
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
                        thisMonthCalendar = getLastMonthCalendar(thisMonthCalendar);
                        getCalendar(thisMonthCalendar);
                    }
                    else{
                        thisMonthCalendar = getNextMonthCalendar(thisMonthCalendar);
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
}
