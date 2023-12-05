package com.example.dinder.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.example.dinder.R;
import com.example.dinder.VolleySingleton;
import com.example.dinder.adapters.model.Restaurant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Adapter for the RecyclerView to display a list of liked restaurants.
 */
public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {

    /**
     * List of {@link Restaurant} objects that represent the user's recent likes
     *
     *
     */
    private Context context;


    private ArrayList<JSONObject> matches;

    public MatchAdapter(ArrayList<JSONObject> matches, Context ctx) {
        this.context = ctx;
        this.matches = matches;
    }

    private void sendImageRequest(String imageUrl) {

    }


    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method should update the contents of the ViewHolder to reflect the item at the given position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JSONObject restaurant = matches.get(position);
        Log.d("Match", restaurant.toString());
        try {
            holder.restaurantName.setText(restaurant.getString("name"));
            JSONObject location = restaurant.getJSONObject("location");
            holder.restaurantAddress.setText(location.getString("address1"));
            holder.starRating.setRating((float) restaurant.getDouble("rating"));

            String url = restaurant.getString("image_url");


            RequestQueue queue = VolleySingleton.getInstance(context.getApplicationContext()).getRequestQueue();
            queue.add(new ImageRequest(
                   url,
                    response -> {
                        holder.restaurantImage.setImageBitmap(response);
                    }, 0, 0, ImageView.ScaleType.CENTER_CROP, null,
                    Throwable::printStackTrace
            ));
        } catch (JSONException e) {
            Log.e("Error", e.toString());
//            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return matches.size();
    }

    /**
     * ViewHolder class for layout of each item in the RecyclerView.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView restaurantImage;
        TextView restaurantName, restaurantAddress;
        RatingBar starRating;

        /**
         * Constructs a ViewHolder for the item layout.
         *
         * @param itemView The View that you inflated in onCreateViewHolder().
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantImage = itemView.findViewById(R.id.matchImageView);
            restaurantName = itemView.findViewById(R.id.matchName);
            restaurantAddress = itemView.findViewById(R.id.matchAddress);
            starRating = itemView.findViewById(R.id.matchRating);
        }
    }

}
