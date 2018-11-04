package onestar.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {
    private final ArrayList<String> schedule;
    private ItemWeek itemWeek;
    private Context context;

    public DayAdapter(ArrayList<String> schedule){
        this.schedule = schedule;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_day,viewGroup,false);
        context = viewGroup.getContext();
        return new DayViewHolder(view);
    }


    @Override
    public void onBindViewHolder(DayViewHolder viewHolder, int position) {
        String strSchedule = schedule.get(position);
        if(strSchedule != null){
            viewHolder.schedule.setText(strSchedule);
        }
    }

    @Override
    public int getItemCount() {
        return schedule.size();
    }

    class DayViewHolder extends RecyclerView.ViewHolder{
        public TextView schedule;

        public DayViewHolder(View itemView) {
            super(itemView);
            schedule = (TextView)itemView.findViewById(R.id.tv_cell_day_schedule);
        }
    }
}
