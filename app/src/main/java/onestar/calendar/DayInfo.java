package onestar.calendar;

import java.util.ArrayList;

public class DayInfo {
    private ArrayList<Integer> _id;
    private int year;
    private int month;
    private int date;
    private int holiday;
    private ArrayList<String> schedule;
    private boolean chkSchedule;

    DayInfo(){
        _id = new ArrayList<Integer>();
        schedule = new ArrayList<String>();
        date = 0;
        holiday = 0;
        chkSchedule = false;
    }
    public ArrayList<Integer> get_id() {return _id;}

    public void set_id(ArrayList<Integer> _id) {
        for(int x = 0; x < _id.size(); x++) {
            this._id.add(_id.get(x));
        }
    }

    public void set_id(int _id) {
        this._id.add(_id);
    }

    public int getYear(){
        return year;
    }

    public void setYear(int year){
        this.year = year;
    }

    public int getMonth(){
        return month;
    }

    public void setMonth(int month){
        this.month = month;
    }

    public int getDate(){
        return date;
    }

    public void setDate(int date){
        this.date = date;
    }

    public int getHoliday() { return holiday;}

    public void setHoliday(int holiday){ this.holiday = holiday;}

    public ArrayList<String> getSchedule(){
        return schedule;
    }

    public void setSchedule(ArrayList<String> schedule){
        for(int x = 0; x < schedule.size(); x++) {
            this.schedule.add(schedule.get(x));
        }
    }

    public void setSchedule(String schedule){
        this.schedule.add(schedule);
    }

    public boolean checkSchedule(){
        return schedule.size() > 0;
    }
}

