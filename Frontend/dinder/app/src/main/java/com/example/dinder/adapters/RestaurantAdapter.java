package com.example.dinder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dinder.R;
import com.example.dinder.adapters.model.Restaurant;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    private List<Restaurant> likes;

    public RestaurantAdapter(List<Restaurant> likes) {
        this.likes = likes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.like_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurant like = likes.get(position);
        // Bind user data to card view UI components, e.g., holder.nameTextView.setText(user.getName());
        holder.restaurantName.setText(like.getName());
    }

    @Override
    public int getItemCount() {
        return likes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Declare your card's UI components
        TextView restaurantName;
        ImageView heartIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantName = itemView.findViewById(R.id.textView);
            heartIcon = itemView.findViewById(R.id.recentLikeHeart);
        }
    }

}
