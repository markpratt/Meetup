package jefferyvicente.meetup;

/**
 * Created by Administrator on 9/21/15.
 */
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import java.util.Date;
import java.util.List;

/**
 * Shows the user profile. This simple activity can function regardless of whether the user
 * is currently logged in.
 */
public class SampleActivity extends Activity {
    private static final int LOGIN_REQUEST = 0;

    private TextView titleTextView;
    private TextView emailTextView;
    private TextView nameTextView;
    private Button loginOrLogoutButton;
    Button continue_button;

    private ParseUser currentUser;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //toEventView();
        getActionBar().hide();
        setContentView(R.layout.activity_login);
        titleTextView = (TextView) findViewById(R.id.profile_title);
        emailTextView = (TextView) findViewById(R.id.profile_email);
        nameTextView = (TextView) findViewById(R.id.profile_name);
        loginOrLogoutButton = (Button) findViewById(R.id.login_or_logout_button);
        titleTextView.setText("You are logged in as");
        continue_button = (Button)findViewById(R.id.continue_button);
        toEventView();
        loginOrLogoutButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null) {
                    // User clicked to log out.
                    ParseUser.logOut();
                    currentUser = null;
                    showProfileLoggedOut();
                } else {

                    // User clicked to log in.
                    ParseLoginBuilder loginBuilder = new ParseLoginBuilder(
                            SampleActivity.this);
                    startActivityForResult(loginBuilder.build(), LOGIN_REQUEST);

                }
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        currentUser = ParseUser.getCurrentUser();
        if (currentUser != null)
        {
            /*
                - Save installation data for push notifications.
                -
                Ideally, installation info would be saved once on signup.  As written, info may be saved
                every time Meetup runs.  However, Parse docs say that ParseInstallation is smart, and
                doesn't save if no change has been made to record, so current strategy may be ok.
                Maybe try later:  import other activities from ParseUILogin, modify SignUpFragment and
                try to get this app to to use the modified version.
            */
            ParseUser currentUser = ParseUser.getCurrentUser();
            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
            installation.put("name", currentUser.getString("name"));
            installation.put("email", currentUser.getEmail());
            installation.saveInBackground();

            // Get current date
            Date currentDate = new Date();

            // Obtain cutoff date by subtracting 86400000 milliseconds (1 day) from currentDate
            long curr_date_minus_1_day = currentDate.getTime() - 86400000;
            Date cutoffDate = new Date(curr_date_minus_1_day);

            // Retrieve events older than the cutoff date and delete them
            ParseQuery<ParseObject> query = ParseQuery.getQuery("event");
            query.whereLessThanOrEqualTo("createdAt", cutoffDate);
            query.findInBackground(new FindCallback<ParseObject>()
            {
                public void done(List<ParseObject> eventList, ParseException e)
                {
                    if (e == null)
                    {
                        Log.d("event", "Retrieved " + eventList.size() + " events");

                        /*  deleteAllInBackground is used because it's quicker than deleting each object individually.
                            Although slower, it may be better to use deleteEventually() instead, since it deletes even when
                            Parse currently inaccessible */
                        ParseObject.deleteAllInBackground(eventList);
                    }
                    else
                    {
                        Log.d("event", "Error: " + e.getMessage());
                    }
                }
            });

            showProfileLoggedIn();
        }
        else
        {
            showProfileLoggedOut();
        }
    }

    /**
     * Shows the profile of the given user.
     */
    private void showProfileLoggedIn() {
        // Necessary to have 2 references to continue button? I (Mark) consolidated into continue_button.
        //View hideButton = findViewById(R.id.continue_button);
        continue_button.setVisibility(View.VISIBLE);

        titleTextView.setText("You are logged in as ");
        emailTextView.setText(currentUser.getEmail());
        String fullName = currentUser.getString("name");
        if (fullName != null) {
            nameTextView.setText(fullName);
        }
        loginOrLogoutButton.setText("Log out");
    }

    public void toEventView(){

        final Context context = this;
        //button = (Button)findViewById(R.id.continue_button);
        continue_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                Intent intent = new Intent(context, eventView.class);
                startActivity(intent);
            }
        });

    }

    /**
     * Show a message asking the user to log in, toggle login/logout button text.
     */
    private void showProfileLoggedOut() {
        // User shouldn't be able to see continue button if they're not logged in.
        continue_button.setVisibility(View.GONE);
        titleTextView.setText("You must log in to view your profile");
        emailTextView.setText("");
        nameTextView.setText("");
        loginOrLogoutButton.setText("Log in ");
    }
}

