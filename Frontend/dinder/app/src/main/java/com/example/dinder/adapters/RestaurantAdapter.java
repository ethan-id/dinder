package com.example.dinder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dinder.R;
import com.example.dinder.adapters.model.Restaurant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Adapter for the RecyclerView to display a list of liked restaurants.
 */
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    /**
     * List of {@link Restaurant} objects that represent the user's recent likes
     */
    private List<Restaurant> likes;

    /**
     * Constructs a RestaurantAdapter with a list of liked restaurants.
     *
     * @param likes A list of Restaurant objects that have been liked.
     */
    public RestaurantAdapter(List<Restaurant> likes) {
        this.likes = likes;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.like_item, parent, false);
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
        Restaurant like = likes.get(position);
        holder.restaurantName.setText(like.getName());
        try {
            holder.restaurantAddress.setText(like.getLocation().getString("address1"));
            holder.ratingBar.setRating((float) like.getRating());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return likes.size();
    }

    /**
     * ViewHolder class for layout of each item in the RecyclerView.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * The name of the restaurant and the address of the restaurant
         */
        TextView restaurantName, restaurantAddress;
        /**
         * ImageView widget for the heart icon
         */
        ImageView heartIcon;
        /**
         * A rating bar widget for displaying the restaurant's rating
         */
        RatingBar ratingBar;

        /**
         * Constructs a ViewHolder for the item layout.
         *
         * @param itemView The View that you inflated in onCreateViewHolder().
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantName = itemView.findViewById(R.id.likeName);
            restaurantAddress = itemView.findViewById(R.id.likeAddress);
            heartIcon = itemView.findViewById(R.id.recentLikeHeart);
            ratingBar = itemView.findViewById(R.id.likeRatingBar);
        }
    }

}
