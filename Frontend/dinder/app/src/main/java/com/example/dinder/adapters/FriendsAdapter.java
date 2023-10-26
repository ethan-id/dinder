package com.example.dinder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dinder.R;
import com.example.dinder.websocket.WebSocketManager;

import java.util.List;

/**
 * Adapter for the RecyclerView used on the Social Screen to display the user's
 * list of friends
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendViewHolder> {

    /**
     * A list of strings containing the user's friends
     */
    private List<String> friendsList;

    /**
     * Constructor that instantiates a FriendsAdapter; takes a list of strings as a friends list to use
     *
     * @param friendsList       A List of Strings representing a list of friends' names
     */
    public FriendsAdapter(List<String> friendsList) {
        this.friendsList = friendsList;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        String friendName = friendsList.get(position);
        holder.friendName.setText(friendName);
        // Here, you can also set image resources, click listeners, etc.
        holder.invite.setOnClickListener(v -> {
            WebSocketManager.getInstance().sendMessage("invite@" + friendName);
        });
    }

    /**
     * Gets the size of the user's friends list
     *
     * @return The size of the user's friends list as an integer
     */
    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    /**
     * A FriendViewHolder used to populate the friends list
     */
    class FriendViewHolder extends RecyclerView.ViewHolder {
        /**
         * TextView used to hold the friend's name
         */
        TextView friendName;

        /**
         * ImageView used to hold the friend's profile picture/icon
         */
        ImageView friendImage;

        /**
         * A Button for sending an invite to the friend to join the user's group
         */
        Button invite;

        /**
         * Constructor for a FriendViewHolder that takes in a View to populate the FriendViewHolder
         * @param itemView      A View containing the elements to be used in the FriendViewHolder
         */
        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            friendName = itemView.findViewById(R.id.friendName);
            friendImage = itemView.findViewById(R.id.friendImage);
            invite = itemView.findViewById(R.id.inviteBtn);
        }
    }
}
