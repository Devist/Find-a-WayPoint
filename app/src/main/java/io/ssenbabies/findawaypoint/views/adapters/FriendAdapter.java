package io.ssenbabies.findawaypoint.views.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.ssenbabies.findawaypoint.R;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    Context context;
    List<Friend> friends;
    int item_layout;

    public FriendAdapter(Context context, List<Friend> friends, int item_layout) {
        this.context = context;
        this.friends = friends;
        this.item_layout = item_layout;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, null);
        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.MATCH_PARENT));
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Friend friend = friends.get(position);

        holder.userName.setText(friend.getUserName());
        if(friend.getIsDone()==0 ){
            holder.userName.setBackgroundResource(R.drawable.tv_friend_yet);
            holder.userName.setTextColor(Color.WHITE);
            holder.imgDone.setVisibility(View.INVISIBLE);
        }


    }


    @Override
    public int getItemCount() {
        return this.friends.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgDone;
        public TextView userName;

        public ViewHolder(View itemView) {
            super(itemView);

            imgDone = (ImageView) itemView.findViewById(R.id.horizon_icon);
            userName = (TextView) itemView.findViewById(R.id.horizon_description);

        }
    }
}

