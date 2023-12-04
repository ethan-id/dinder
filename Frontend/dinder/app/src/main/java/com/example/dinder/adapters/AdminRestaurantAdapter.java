package com.example.dinder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dinder.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.List;

public class AdminRestaurantAdapter extends RecyclerView.Adapter<AdminRestaurantAdapter.RestaurantCardHolder> {

    private List<JSONObject> restaurants;

    public AdminRestaurantAdapter(List<JSONObject> list) {
        this.restaurants = list;
    }

    @NonNull
    @Override
    public RestaurantCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item, parent, false);
        return new AdminRestaurantAdapter.RestaurantCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminRestaurantAdapter.RestaurantCardHolder holder, int position) {
        JSONObject restaurant = restaurants.get(position);
        try {
            holder.restaurantName.setText(restaurant.getString("name"));
            holder.ratingCount.setText(MessageFormat.format("{0}({1})", restaurant.getString("rating"), restaurant.getString("review_count")));

            JSONObject location = restaurant.getJSONObject("location");
            holder.priceAndType.setText(MessageFormat.format("{0} • {1}", restaurant.getString("price"), location.getString("address1")));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the size of the user's friends list
     *
     * @return The size of the user's friends list as an integer
     */
    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    /**
     * A FriendViewHolder used to populate the friends list
     */
    class RestaurantCardHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView restaurantName, ratingCount, priceAndType, description;
        RatingBar starRating;

        /**
         * Constructor for a FriendViewHolder that takes in a View to populate the FriendViewHolder
         * @param itemView      A View containing the elements to be used in the FriendViewHolder
         */
        public RestaurantCardHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.imageViewThumbnail);
            restaurantName = itemView.findViewById(R.id.restCardName);
            starRating = itemView.findViewById(R.id.restItemRating);
            ratingCount = itemView.findViewById(R.id.restRatingCount);
            priceAndType = itemView.findViewById(R.id.restPriceAndType);
            description = itemView.findViewById(R.id.restDescription);
        }
    }
}
