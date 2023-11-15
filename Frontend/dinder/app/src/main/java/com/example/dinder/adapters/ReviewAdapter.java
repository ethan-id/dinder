package com.example.dinder.adapters;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.example.dinder.R;
import com.example.dinder.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private ArrayList<JSONObject> reviews;

    // Provide a suitable constructor
    public ReviewAdapter(ArrayList<JSONObject> myDataset) {
        reviews = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get element from your dataset at this position
        // Replace the contents of the view with that element
        JSONObject item = reviews.get(position);
        try {
            String reviewText = item.getString("text");
            String timeCreated = item.getString("time_created");
            String formattedTime = formatDateString(timeCreated);
            JSONObject user = item.getJSONObject("user");
            String name = user.getString("name");
            String imageUrl = user.getString("image_url"); // Replace with the correct JSON path

            holder.user_review.setText(reviewText);
            holder.rating_time.setText("Posted on: " + formattedTime);

            if (imageUrl != null && !imageUrl.isEmpty()) {
                ImageRequest imageRequest = new ImageRequest(
                        imageUrl,
                        response -> {
                            holder.userImage.setImageBitmap(response);
                        }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,
                        error -> Log.e("Image Load Error", "Error loading image: " + error.getMessage())
                );
                // Add the request to the RequestQueue.
                VolleySingleton.getInstance(holder.userImage.getContext()).addToRequestQueue(imageRequest);
            } else {
                // Set a default image or clear the current image
                holder.userImage.setImageResource(R.drawable.account_circle); // Replace with your default image resource
            }

            holder.userName.setText(name);
            holder.user_review.setText(reviewText);
            holder.rating_time.setText("Posted on: " + formattedTime);

        } catch (JSONException e) {
            Log.e("Error", e.toString());
        }
    }

    /**
     * Converts the JSON date string to a more readable format.
     *
     * @param dateString The date string from the JSON object.
     * @return A formatted date string.
     */
    private String formatDateString(String dateString) {
        try {
            SimpleDateFormat jsonDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat displayFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());

            return displayFormat.format(jsonDateFormat.parse(dateString));
        } catch (ParseException e) {
            Log.e("Date Parsing", "Error parsing date: " + e.getMessage());
            return dateString; // return the original string in case of parsing error
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return reviews.size();
    }

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView userImage;
        public TextView userName;
        public TextView user_review;
        public TextView rating_time;

        public ViewHolder(View view) {
            super(view);
            userImage = view.findViewById(R.id.image_user);
            userName = view.findViewById(R.id.text_user_name);
            user_review = view.findViewById(R.id.text_review);
            rating_time = view.findViewById(R.id.text_rating_time);
        }
    }}
