package onestar.calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MonthAdapter extends BaseAdapter {
    private final ArrayList<DayInfo> daylist;
    private Context context;
    private LayoutInflater layoutInflater;

    public MonthAdapter(Context context, ArrayList<DayInfo> daylist){
        this.daylist = daylist;
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return daylist.size();
    }
    @Override
    public Object getItem(int position){
        return daylist.get(position);
    }
    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        DayInfo dayInfo = daylist.get(position);
        DayViewHolder dayViewHolder;

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.cell_month, parent, false);
            convertView.setLayoutParams(new GridView.LayoutParams(getCellWidth(), getCellHeight()));
            dayViewHolder = new DayViewHolder();
            dayViewHolder.textView = (TextView)convertView.findViewById(R.id.cell_monthly_day1);
            dayViewHolder.imageView = (ImageView)convertView.findViewById(R.id.ig_month);
            convertView.setTag(dayViewHolder);
        }
        else{
            dayViewHolder = (DayViewHolder)convertView.getTag();
        }
        if(dayInfo != null){
            if(dayInfo.getDate() == 0) dayViewHolder.textView.setText("");
            else dayViewHolder.textView.setText(""+dayInfo.getDate());
            if(position % 7 == 0 || dayInfo.getHoliday() == 1){
                dayViewHolder.textView.setTextColor(Color.RED);
            }
            else if(position % 7 == 6){
                dayViewHolder.textView.setTextColor(Color.BLUE);
            }
            else{
                dayViewHolder.textView.setTextColor(Color.BLACK);
            }
            if(dayInfo.checkSchedule()) dayViewHolder.imageView.setImageResource(R.drawable.red_circle);
        }

        return convertView;
    }

    public class DayViewHolder{
        public TextView textView;
        public ImageView imageView;
    }

    public int getCellWidth(){
        int width = context.getResources().getDisplayMetrics().widthPixels;
        return width/7;
    }

    public int getCellHeight(){
        int height = context.getResources().getDisplayMetrics().heightPixels;
        return height/10;
    }
}
