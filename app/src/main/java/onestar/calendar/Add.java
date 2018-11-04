package onestar.calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aigestudio.wheelpicker.widgets.WheelDatePicker;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

public class Add extends Dialog implements WheelDatePicker.OnDateSelectedListener, View.OnClickListener{
    private WheelDatePicker wheelDatePicker;
    private TextView tv_day;
    private TextView tv_yearmonth;
    private TextView tv_dayofweek;
    private String EDayOfWeek;
    private EditText et_schedule;
    private Button bt_ok;
    private Button bt_cancle;
    private TextWatcher etTextWatcher;
    private int year;
    private int month;
    private int day;
    private Context context;

    public Add(Context context){
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_add);
        setUI();
        setWheelDatePicker();
        tv_day.setText(""+wheelDatePicker.getCurrentDay());
        tv_yearmonth.setText(""+wheelDatePicker.getCurrentYear()+". "+wheelDatePicker.getCurrentMonth()+".");
        EDayOfWeek = wheelDatePicker.getCurrentDate().toString().substring(0,3);
        getKDayOfWeek(tv_dayofweek,EDayOfWeek);
        bt_ok.setOnClickListener(this);
        bt_cancle.setOnClickListener(this);
        setEt_schedule();
    }

    @Override
    public void onDateSelected(WheelDatePicker picker, Date date) {
        year = picker.getCurrentYear();
        month = picker.getCurrentMonth();
        day = picker.getCurrentDay();
        tv_day.setText(""+day);
        tv_yearmonth.setText(""+year+". "+month+".");
        EDayOfWeek = picker.getCurrentDate().toString().substring(0,3);
        getKDayOfWeek(tv_dayofweek,EDayOfWeek);
    }

    public void getKDayOfWeek(TextView tv, String string){
        String result = "";
        switch (string){
            case "Mon":
                result = " 월요일";
                tv.setText(result);
                tv.setBackgroundResource(R.drawable.mydayofweek);
                tv.setTextColor(Color.WHITE);
                break;
            case "Tue":
                result = " 화요일";
                tv.setText(result);
                tv.setBackgroundResource(R.drawable.mydayofweek);
                tv.setTextColor(Color.WHITE);
                break;
            case "Wed":
                result = " 수요일";
                tv.setText(result);
                tv.setBackgroundResource(R.drawable.mydayofweek);
                tv.setTextColor(Color.WHITE);
                break;
            case "Thu":
                result = " 목요일";
                tv.setText(result);
                tv.setBackgroundResource(R.drawable.mydayofweek);
                tv.setTextColor(Color.WHITE);
                break;
            case "Fri":
                result = " 금요일";
                tv.setText(result);
                tv.setBackgroundResource(R.drawable.mydayofweek);
                tv.setTextColor(Color.WHITE);
                break;
            case "Sat":
                result = " 토요일";
                tv.setText(result);
                tv.setBackgroundResource(R.drawable.mydayofweekblue);
                tv.setTextColor(Color.WHITE);
                break;
            case "Sun":
                result = " 일요일";
                tv.setText(result);
                tv.setBackgroundResource(R.drawable.mydayofweekred);
                tv.setTextColor(Color.WHITE);
                break;
        }
    }

    public int getDayOfWeekColor(String string){
        int result = Color.BLACK;
        switch (string){
            case "Sat":
                result = Color.BLUE;
                break;
            case "Sun":
                result = Color.RED;
                break;
        }
        return result;
    }

    public void setUI(){
        tv_day = (TextView)findViewById(R.id.tv_add_day);
        tv_yearmonth = (TextView)findViewById(R.id.tv_add_yearmonth);
        tv_dayofweek = (TextView)findViewById(R.id.tv_add_dayofweek);
        wheelDatePicker = (WheelDatePicker)findViewById(R.id.wheel_picker_date);
        et_schedule = (EditText)findViewById(R.id.et_schedule);
        bt_ok = (Button)findViewById(R.id.bt_ok);
        bt_cancle = (Button)findViewById(R.id.bt_cancle);
    }

    public void setWheelDatePicker(){
        wheelDatePicker.setCurved(true);
        wheelDatePicker.setCyclic(true);
        wheelDatePicker.setSelectedItemTextColor(Color.BLACK);
        wheelDatePicker.getTextViewYear().setText("년");
        wheelDatePicker.getTextViewYear().setTextSize(18);
        wheelDatePicker.getTextViewYear().setTextColor(Color.BLACK);
        wheelDatePicker.getTextViewMonth().setText("월");
        wheelDatePicker.getTextViewMonth().setTextSize(18);
        wheelDatePicker.getTextViewMonth().setTextColor(Color.BLACK);
        wheelDatePicker.getTextViewDay().setText("일");
        wheelDatePicker.getTextViewDay().setTextSize(18);
        wheelDatePicker.getTextViewDay().setTextColor(Color.BLACK);
        wheelDatePicker.setOnDateSelectedListener(this);
    }

    public void setEt_schedule(){
        etTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    if(!bt_ok.isEnabled()) bt_ok.setEnabled(true);
                }
                else{
                    if(bt_ok.isEnabled()) bt_ok.setEnabled(false);
                }
            }
        };
        et_schedule.addTextChangedListener(etTextWatcher);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_ok:
                AlertDialog.Builder okalertDialog = new AlertDialog.Builder(getContext());
                okalertDialog.setTitle("Add Schedule");
                okalertDialog.setMessage("스케줄을 추가 하시겠습니까?");
                okalertDialog.setPositiveButton("예", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBHelper dbHelper = new DBHelper(getContext(), "Schedule", null, 1);
                        dbHelper.insert(wheelDatePicker.getCurrentYear(),wheelDatePicker.getCurrentMonth(),wheelDatePicker.getCurrentDay(), et_schedule.getText().toString());
                        dbHelper.close();
                        dialog.cancel();
                        Add.this.dismiss();
                        ((CalendarActivity)context).refreshFragment();
                    }});
                okalertDialog.setNegativeButton("아니오", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }});
                okalertDialog.show();
                break;
                case R.id.bt_cancle:
                AlertDialog.Builder canclealertDialog = new AlertDialog.Builder(getContext());
                canclealertDialog.setTitle("Exit Add Schedule");
                canclealertDialog.setMessage("스케줄 추가를 종료하시겠습니까 ?");
                    canclealertDialog.setPositiveButton("예", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Add.this.dismiss();
                            ((CalendarActivity)context).refreshFragment();
                        }
                    });
                canclealertDialog.setNegativeButton("아니요", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }});
                canclealertDialog.show();
                break;
        }
    }
}
