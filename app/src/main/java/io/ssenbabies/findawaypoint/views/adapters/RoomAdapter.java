package io.ssenbabies.findawaypoint.views.adapters;

import android.content.Context;
import android.content.Intent;
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
import io.ssenbabies.findawaypoint.views.MyLocationActivity;
import io.ssenbabies.findawaypoint.views.RoomActivity;
import io.ssenbabies.findawaypoint.views.widget.DetailDialog;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {
    Context context;
    List<Room> rooms;
    int item_layout;
    DBHelper dbHelper;

    DetailDialog detailDialog;


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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        dbHelper = new DBHelper(holder.itemView.getContext(), "MyInfo.db", null, 1);
        final Room room = rooms.get(position);
        Log.d("룸코드",room.getRoomID());
        holder.roomTitle.setText(room.getRoomTitle());
        holder.roomDate.setText(room.getRoomDate());
        if(room.getIsOnGoing()==1 )
            holder.txtIsOnGoing.setText("진행중 ");
        else
            holder.txtIsOnGoing.setVisibility(View.INVISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                if(room.getIsOnGoing()==1){
                    if(room.getAlreadyPick()==0){
                        intent = new Intent(v.getContext(), MyLocationActivity.class);
                        intent.putExtra("roomCode",room.getRoomID());
                        v.getContext().startActivity(intent);
                    }else if(room.getAlreadyPick()==1){
                        intent = new Intent(v.getContext(), RoomActivity.class);
                        intent.putExtra("roomCode",room.getRoomID());
                        intent.putExtra("room_code",room.getRoomID());
                        intent.putExtra("place",room.getRoomTitle());
                        v.getContext().startActivity(intent);
                    }
                }else{

                    Toast.makeText(context, room.getRoomID(), Toast.LENGTH_SHORT).show();
                    String[] result = dbHelper.getDetailAppointment(room.getRoomID());
                    detailDialog = new DetailDialog(holder.itemView.getRootView().getContext()
                            ,room.getRoomTitle(),room.getRoomDate(),result[5],result[2],result[3]);

                    detailDialog.show();
                    Log.d("로컬 디비 테스트", result[0]);
                    Log.d("로컬 디비 테스트", result[1]);
                    Log.d("로컬 디비 테스트", result[2]);
                    Log.d("로컬 디비 테스트", result[3]);
                    Log.d("로컬 디비 테스트", result[4]);
                    Log.d("로컬 디비 테스트", result[5]);
                    Log.d("로컬 디비 테스트", result[6]);
                }
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
