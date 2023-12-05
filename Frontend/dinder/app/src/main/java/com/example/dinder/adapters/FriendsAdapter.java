package com.example.dinder.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.example.dinder.R;
import com.example.dinder.VolleySingleton;
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
    private Context ctx;
    private String username;

    public interface AdapterCallback {
        void onFriendsListChanged();
    }

    private AdapterCallback callback;
    /**
     * Constructor that instantiates a FriendsAdapter; takes a list of strings as a friends list to use
     *
     * @param friendsList       A List of Strings representing a list of friends' names
     */
    public FriendsAdapter(List<String> friendsList, Context context, String username, AdapterCallback callback) {
        this.friendsList = friendsList;
        this.ctx = context;
        this.username = username;
        this.callback = callback;
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

        holder.invite.setOnClickListener(v -> {
            WebSocketManager.getInstance().sendMessage("invite@" + friendName);
            Toast.makeText(holder.itemView.getContext(), "Invite sent to " + friendName, Toast.LENGTH_SHORT).show();
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
        builder.setTitle("Remove friend");
        builder.setMessage("Are you sure you want to remove " + friendName + " from your friends list?");
        builder.setPositiveButton("Un-Friend", (dialog, id) -> {
            // User clicked the "Remove" button, so remove the friend.
            RequestQueue queue = VolleySingleton.getInstance(ctx).getRequestQueue();
            String url = "http://coms-309-055.class.las.iastate.edu:8080/friend/" + this.username + "/remove/" + friendName;

            queue.add(new StringRequest(Request.Method.PUT, url,
                    response -> {
                        friendsList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, friendsList.size());
                        if (callback != null) {
                            callback.onFriendsListChanged(); // Notify the activity that the list has changed
                        }
                        Toast.makeText(ctx, "Removed " + friendName, Toast.LENGTH_LONG).show();
                    }, error -> {
                        Log.e("Error while removing friend", error.toString());
                        Toast.makeText(ctx, "Error while removing " + friendName, Toast.LENGTH_LONG).show();
                    }));
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();

        holder.remove.setOnClickListener(v -> dialog.show());
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
        ImageView remove;

        /**
         * Constructor for a FriendViewHolder that takes in a View to populate the FriendViewHolder
         * @param itemView      A View containing the elements to be used in the FriendViewHolder
         */
        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            friendName = itemView.findViewById(R.id.friendName);
            friendImage = itemView.findViewById(R.id.friendImage);
            invite = itemView.findViewById(R.id.inviteBtn);
            remove = itemView.findViewById(R.id.removeFriendIcon);
        }
    }
}
