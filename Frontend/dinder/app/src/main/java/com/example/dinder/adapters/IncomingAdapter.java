package com.example.dinder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    public interface AdapterCallback {
        void onListChanged();
    }

    private AdapterCallback callback;

    public IncomingAdapter(List<JSONObject> incomingRequests, Context ctx, AdapterCallback callback) {
        this.incomingRequests = incomingRequests;
        this.context = ctx;
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
        JSONObject friend = incomingRequests.get(position);
        try {
            holder.friendName.setText(friend.getString("creator"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        holder.invite.setText("Accept");
        holder.invite.setOnClickListener(v -> {
            try {
                acceptFriendRequest(friend.getInt("id"), position, friend.getString("parameter"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
        holder.close.setOnClickListener(v -> {
            incomingRequests.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, incomingRequests.size());
            if (callback != null) {
                callback.onListChanged(); // Notify the activity that the list has changed
            }
        });
    }

    public void acceptFriendRequest(int requestId, int position, String type) {
        RequestQueue queue = VolleySingleton.getInstance(context.getApplicationContext()).getRequestQueue();

        if (type.equals("friend")) {
            String url = "http://coms-309-055.class.las.iastate.edu:8080/request/accept/" + requestId;

            queue.add(new StringRequest(Request.Method.POST, url,
                response -> {
                    incomingRequests.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, incomingRequests.size());
                    if (callback != null) {
                        callback.onListChanged(); // Notify the activity that the list has changed
                    }
                    Toast.makeText(context, "Added Friend", Toast.LENGTH_SHORT).show();
                }, Throwable::printStackTrace));
        }

        if (type.equals("group")) {
            WebSocketManager.getInstance().sendMessage("accept@" + requestId);
            incomingRequests.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, incomingRequests.size());
            if (callback != null) {
                callback.onListChanged(); // Notify the activity that the list has changed
            }
            Toast.makeText(context, "Joined Group", Toast.LENGTH_SHORT).show();
        }
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
        String type;
        /**
         * TextView used to hold the friend's name
         */
        TextView friendName;

        /**
         * ImageView used to hold the friend's profile picture/icon
         */
        ImageView friendImage;
        ImageView close;

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
            close = itemView.findViewById(R.id.removeFriendIcon);

        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
