package onestar.calendar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.sql.Date;

public class DBHelper extends SQLiteOpenHelper {

    private Context context;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Schedule(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date DATE NOT NULL," +
                "schedule TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(int year, int month, int day, String schedule){
        SQLiteDatabase db = getWritableDatabase();

        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO Schedule(");
        sb.append("date,schedule)");
        sb.append("VALUES(?,?)");
        String strDate = ""+year+"-"+month+"-"+day;
        Date date = Date.valueOf(strDate);
        db.execSQL(sb.toString(), new Object[]{
                date,
                schedule
        });
    }

    public void update(DayInfo dayInfo){

    }

    public void delete(DayInfo dayInfo){

    }

    public ArrayList<DayInfo> getMonthResult(int year, int month, int lastday){
        int lastDate = 0;
        ArrayList<DayInfo> result;
        result = new ArrayList<DayInfo>();

        StringBuffer sb = new StringBuffer();
        sb.append("SELECT _id, date, schedule FROM Schedule WHERE date >= ? AND date <= ? ORDER BY date ASC");

        SQLiteDatabase db = getReadableDatabase();
        String _startDate = ""+year+"-"+month+"-1";
        String _lastDate = ""+year+"-"+month+"-"+lastday;

        Cursor cursor = db.rawQuery(sb.toString(), new String[]{
                Date.valueOf(_startDate).toString(),
                Date.valueOf(_lastDate).toString()
        });

        while(cursor.moveToNext()){
            String dateTime = cursor.getString(1);
            int _year = Integer.parseInt(dateTime.substring(0,4));
            int _month = Integer.parseInt(dateTime.substring(5,7));
            int _day = Integer.parseInt(dateTime.substring(8));

            if(lastDate != _day) {
                lastDate = _day;
                DayInfo dayInfo = new DayInfo();
                dayInfo.set_id(cursor.getInt(0));
                dayInfo.setYear(_year);
                dayInfo.setMonth(_month);
                dayInfo.setDate(_day);
                dayInfo.setSchedule(cursor.getString(2));
                result.add(dayInfo);

            }
            else{
                result.get(result.size()-1).set_id(cursor.getInt(0));
                result.get(result.size()-1).setSchedule(cursor.getString(2));
            }
        }
        db.close();
        cursor.close();
        return result;
    }

    public ArrayList<DayInfo> getWeekResult(int startYear, int startMonth, int startDay, int lastYear, int lastMonth, int lastDay){
        int lastDate = 0;
        ArrayList<DayInfo> result;
        result = new ArrayList<DayInfo>();

        StringBuffer sb = new StringBuffer();
        sb.append("SELECT _id, date, schedule FROM Schedule ");
        sb.append("WHERE date >= ? AND date <= ? ORDER BY date ASC");

        SQLiteDatabase db = getReadableDatabase();

        String _startDate = ""+startYear+"-"+startMonth+"-"+startDay;
        String _lastDate = ""+lastYear+"-"+lastMonth+"-"+lastDay;

        Cursor cursor = db.rawQuery(sb.toString(), new String[]{
                Date.valueOf(_startDate).toString(),
                Date.valueOf(_lastDate).toString()
        });

        while(cursor.moveToNext()){
            String dateTime = cursor.getString(1);
            int _year = Integer.parseInt(dateTime.substring(0,4));
            int _month = Integer.parseInt(dateTime.substring(5,7));
            int _day = Integer.parseInt(dateTime.substring(8));
            if(lastDate != _day) {
                lastDate = _day;
                DayInfo dayInfo = new DayInfo();
                dayInfo.set_id(cursor.getInt(0));
                dayInfo.setYear(_year);
                dayInfo.setMonth(_month);
                dayInfo.setDate(_day);
                dayInfo.setSchedule(cursor.getString(2));
                result.add(dayInfo);
            }
            else{
                result.get(result.size()-1).set_id(cursor.getInt(0));
                result.get(result.size()-1).setSchedule(cursor.getString(2));
            }
        }
        db.close();
        cursor.close();
        return result;
    }

    public DayInfo getDayResult(int year, int month, int day){
        DayInfo result;
        int count = 0;
        result = new DayInfo();

        StringBuffer sb = new StringBuffer();
        sb.append("SELECT _id, date, schedule FROM Schedule ");
        sb.append("WHERE date = ? ORDER BY _id ASC");

        SQLiteDatabase db = getReadableDatabase();

        String _Date = ""+year+"-"+month+"-"+day;

        Cursor cursor = db.rawQuery(sb.toString(), new String[]{
                Date.valueOf(_Date).toString()
        });

        while(cursor.moveToNext()){
            if(count == 0) {
                result.set_id(cursor.getInt(0));
                String dateTime = cursor.getString(1);
                int _year = Integer.parseInt(dateTime.substring(0,4));
                int _month = Integer.parseInt(dateTime.substring(5,7));
                int _day = Integer.parseInt(dateTime.substring(8));
                result.setYear(_year);
                result.setMonth(_month);
                result.setDate(_day);
                result.setSchedule(cursor.getString(2));
            }
            else{
                result.set_id(cursor.getInt(0));
                result.setSchedule(cursor.getString(2));
            }
            count++;
        }
        db.close();
        cursor.close();
        return result;
    }
}
