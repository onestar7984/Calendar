package onestar.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemWeekAdapter extends RecyclerView.Adapter<ItemWeekAdapter.ItemWeekHolder> {
    private final ArrayList<ItemWeek> itemWeeks;
    private Context context;

    public ItemWeekAdapter(ArrayList<ItemWeek> itemWeeks){
        this.itemWeeks = itemWeeks;
    }

    @NonNull
    @Override
    public ItemWeekHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_week_schedule,viewGroup,false);
        context = viewGroup.getContext();
        return new ItemWeekHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemWeekHolder viewHolder, int position) {
        ItemWeek itemWeek = itemWeeks.get(position);

        if(itemWeek != null){
            viewHolder.textView.setText(itemWeek.getStr_schedule());
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return itemWeeks.size();
    }

    public class ItemWeekHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public ItemWeekHolder(View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.tv_cell_week_schedule);
        }
    }
}
