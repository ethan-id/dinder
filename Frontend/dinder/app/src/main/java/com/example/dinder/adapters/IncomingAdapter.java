package com.example.dinder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.dinder.R;
import com.example.dinder.VolleySingleton;
import com.example.dinder.websocket.WebSocketManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Adapter for the RecyclerView used on the Social Screen to display the user's
 * list of friends
 */
public class IncomingAdapter extends RecyclerView.Adapter<IncomingAdapter.FriendViewHolder> {

    /**
     * A list of strings containing the user's friends
     */
    private List<JSONObject> incomingRequests;
    private Context context;

    /**
     * Constructor that instantiates a FriendsAdapter; takes a list of strings as a friends list to use
     *
     * @param incomingRequests       A List of Strings representing a list of friends' names
     */
    public IncomingAdapter(List<JSONObject> incomingRequests, Context ctx) {
        this.incomingRequests = incomingRequests;
        this.context = ctx;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        JSONObject friend = incomingRequests.get(position);
        try {
            holder.friendName.setText(friend.getString("creator"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        holder.invite.setText("Accept");
        holder.invite.setOnClickListener(v -> {
            try {
                acceptFriendRequest(friend.getInt("id"), position);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void acceptFriendRequest(int requestId, int position) {
        RequestQueue queue = VolleySingleton.getInstance(context.getApplicationContext()).getRequestQueue();
        String url = "http://coms-309-055.class.las.iastate.edu:8080/request/accept/" + requestId;

        queue.add(new StringRequest(Request.Method.POST, url,
            response -> {
                incomingRequests.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, incomingRequests.size());
            }, Throwable::printStackTrace));
    }

    /**
     * Gets the size of the user's friends list
     *
     * @return The size of the user's friends list as an integer
     */
    @Override
    public int getItemCount() {
        return incomingRequests.size();
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
