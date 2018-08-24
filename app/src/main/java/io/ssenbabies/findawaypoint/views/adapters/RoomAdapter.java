package io.ssenbabies.findawaypoint.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.ssenbabies.findawaypoint.R;
import io.ssenbabies.findawaypoint.databases.DBHelper;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {
    Context context;
    List<Room> rooms;
    int item_layout;
    DBHelper dbHelper;

    public RoomAdapter(Context context, List<Room> rooms, int item_layout) {
        this.context = context;
        this.rooms = rooms;
        this.item_layout = item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, null);
        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        dbHelper = new DBHelper(holder.itemView.getContext(), "MyInfo.db", null, 1);
        final Room room = rooms.get(position);

        holder.roomTitle.setText(room.getRoomTitle());
        holder.roomDate.setText(room.getRoomDate());
        if(room.getIsOnGoing()==1 )
            holder.txtIsOnGoing.setText("진행중 ");
        else
            holder.txtIsOnGoing.setVisibility(View.INVISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, room.getRoomID(), Toast.LENGTH_SHORT).show();
                //String[] result = dbHelper.getDetailAppointment(room.getRoomID());
                //Log.d("로컬 디비 테스트", result.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.rooms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView roomTitle;
        TextView roomDate;
        TextView txtIsOnGoing;

        public ViewHolder(View itemView) {
            super(itemView);
            roomTitle = (TextView) itemView.findViewById(R.id.txtRoomTitle);
            roomDate = (TextView) itemView.findViewById(R.id.txtRoomDate);
            txtIsOnGoing = (TextView) itemView.findViewById(R.id.txtIsOnGoing);

        }
    }
}
