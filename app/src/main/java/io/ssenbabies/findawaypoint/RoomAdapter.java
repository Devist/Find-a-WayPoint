package io.ssenbabies.findawaypoint;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {
    Context context;
    List<Room> rooms;
    int item_layout;

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
        final Room room = rooms.get(position);

        holder.roomTitle.setText(room.getRoomTitle());
        holder.appointmentPlaceContents.setText(room.getAppointmentPlaceContents());
        holder.arroundStationContents.setText(room.getArroundStationContents());
//        holder.cardview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return this.rooms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView roomTitle;
        TextView appointmentPlaceContents;
        TextView arroundStationContents;

        public ViewHolder(View itemView) {
            super(itemView);
            roomTitle = (TextView) itemView.findViewById(R.id.txtRoomTitle);
            appointmentPlaceContents = (TextView) itemView.findViewById(R.id.txtAppointmentPlaceContents);
            arroundStationContents = (TextView) itemView.findViewById(R.id.txtArroundStationContents);
        }
    }
}
