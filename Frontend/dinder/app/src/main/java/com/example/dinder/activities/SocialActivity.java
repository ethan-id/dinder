package com.example.dinder.activities;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.dinder.R;
import com.example.dinder.adapters.FriendsAdapter;
import com.example.dinder.websocket.WebSocketListener;
import org.java_websocket.handshake.ServerHandshake;
import java.util.Arrays;
import java.util.List;

/**
 * The Social Screen displays the user's friends list, allows the user to invite friends to their group, and allows
 * the user to send friend requests to other users.
 */
public class SocialActivity extends AppCompatActivity implements WebSocketListener {
    /**
     * Search bar used for the user to send friend requests
     */
    SearchView searchBar;
    /**
     * Button used for the user to go back to the home screen
     */
    ImageButton back;
    /**
     * RecyclerView used to dynamically display the user's friends
     */
    RecyclerView friendsRecyclerView;
    private View bottomNavigationView;

    /**
     * Initializes the SocialActivity. This method:
     * <ul>
     *     <li>Sets the content view layout.</li>
     *     <li>Initializes UI components including the search bar and friends' RecyclerView.</li>
     *     <li>Populates the friends' RecyclerView with a static list of friend names.</li>
     *     <li>Retrieves user ID from the intent.</li>
     *     <li>Handles the back button click event, which navigates to the UserHomeActivity and passes the user ID to it.</li>
     * </ul>
     *
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down, this Bundle contains the data it most recently
     * supplied in {@link #onSaveInstanceState}. Otherwise, it is null.
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.social);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;

                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    // Start the HomeActivity
                    intent = new Intent(SocialActivity.this, UserHomeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                } else if (itemId == R.id.match) {
                    // Start the MatchActivity
                    intent = new Intent(SocialActivity.this, MatchesScreen.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                } else if (itemId == R.id.social) {
                    // You're already on this page, so no need to do anything here.
                    return true;
                } else if (itemId == R.id.userprofile) {
                    // Start the UserProfileActivity
                    intent = new Intent(SocialActivity.this, UserProfileActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                }
                return false;
            }
        });



        searchBar = findViewById(R.id.search);
        back = findViewById(R.id.backBtn);

        friendsRecyclerView = findViewById(R.id.friendsRecyclerView);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> friendsList = Arrays.asList("John", "Jane", "Doe", "Smith");
        FriendsAdapter adapter = new FriendsAdapter(friendsList);
        friendsRecyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        back.setOnClickListener(v -> {
            Intent homeScreen = new Intent(SocialActivity.this, UserHomeActivity.class);
            homeScreen.putExtra("id", id);
            homeScreen.putExtra("connected", true);
            startActivity(homeScreen);
        });
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakeData) {

    }

    @Override
    public void onWebSocketMessage(String message) {

    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onWebSocketError(Exception ex) {

    }
}
