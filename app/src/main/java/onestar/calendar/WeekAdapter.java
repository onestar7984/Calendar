package onestar.calendar;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.DayViewHolder> {
    private final ArrayList<DayInfo> daylist;
    private Context context;
    private RecyclerView.LayoutManager layoutManager;

    public WeekAdapter(ArrayList<DayInfo> daylist){
        this.daylist = daylist;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_week,viewGroup,false);
        context = viewGroup.getContext();
        return new DayViewHolder(view);
    }


    @Override
    public void onBindViewHolder(DayViewHolder viewHolder, int position) {

        viewHolder.schedule.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        viewHolder.schedule.setLayoutManager(layoutManager);
        DayInfo dayInfo = daylist.get(position);
        ItemWeekAdapter itemWeekAdapter;
        ArrayList<ItemWeek> itemWeeks;

        if(dayInfo != null){
            String day = Integer.toString(dayInfo.getDate());
            String yearmonth = Integer.toString(dayInfo.getYear())+". "+Integer.toString(dayInfo.getMonth()+1)+".";
            viewHolder.day.setText(day);
            viewHolder.yearmonth.setText(yearmonth);
            if(position % 7 == 6 ){
                viewHolder.dayofweek.setText("토요일");
                viewHolder.dayofweek.setBackgroundResource(R.drawable.mydayofweekblue);
                viewHolder.dayofweek.setTextColor(Color.WHITE);
            }
            else if(position % 7 == 1){
                viewHolder.dayofweek.setText("월요일");
                viewHolder.dayofweek.setBackgroundResource(R.drawable.mydayofweek);
                viewHolder.dayofweek.setTextColor(Color.WHITE);
            }
            else if(position % 7 == 2){
                viewHolder.dayofweek.setText("화요일");
                viewHolder.dayofweek.setBackgroundResource(R.drawable.mydayofweek);
                viewHolder.dayofweek.setTextColor(Color.WHITE);
            }
            else if(position % 7 == 3){
                viewHolder.dayofweek.setText("수요일");
                viewHolder.dayofweek.setBackgroundResource(R.drawable.mydayofweek);
                viewHolder.dayofweek.setTextColor(Color.WHITE);
            }
            else if(position % 7 == 4){
                viewHolder.dayofweek.setText("목요일");
                viewHolder.dayofweek.setBackgroundResource(R.drawable.mydayofweek);
                viewHolder.dayofweek.setTextColor(Color.WHITE);
            }
            else if(position % 7 == 5){
                viewHolder.dayofweek.setText("금요일");
                viewHolder.dayofweek.setBackgroundResource(R.drawable.mydayofweek);
                viewHolder.dayofweek.setTextColor(Color.WHITE);
            }
            else{
                viewHolder.dayofweek.setText("일요일");
                viewHolder.dayofweek.setBackgroundResource(R.drawable.mydayofweekred);
                viewHolder.dayofweek.setTextColor(Color.WHITE);
            }
            if(dayInfo.getHoliday() == 1) viewHolder.dayofweek.setBackgroundResource(R.drawable.mydayofweekred);
            if(dayInfo.checkSchedule()){
                itemWeeks = new ArrayList<ItemWeek>();
                for(int x = 0; x < dayInfo.getSchedule().size(); x++){
                    ItemWeek itemWeek = new ItemWeek();
                    itemWeek.setStr_schedule(dayInfo.getSchedule().get(x));
                    itemWeeks.add(itemWeek);
                }
                itemWeekAdapter = new ItemWeekAdapter(itemWeeks);
                viewHolder.schedule.setAdapter(itemWeekAdapter);
            }
            else{
                viewHolder.schedule.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return daylist.size();
    }

    class DayViewHolder extends RecyclerView.ViewHolder{
        public TextView day;
        public TextView yearmonth;
        public TextView dayofweek;
        public RecyclerView schedule;

        public DayViewHolder(View itemView) {
            super(itemView);
            day = (TextView)itemView.findViewById(R.id.tv_week_day);
            yearmonth = (TextView)itemView.findViewById(R.id.tv_week_yearmonth);
            dayofweek = (TextView)itemView.findViewById(R.id.tv_week_dayofweek);
            schedule = (RecyclerView)itemView.findViewById(R.id.rcview_week_schedule);
        }
    }
}
