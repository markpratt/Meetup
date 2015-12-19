package jefferyvicente.meetup;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import java.util.List;

public class EventDetails extends Activity {
    private EventDetailsCustomAdapter adapter;
    private String eventName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        getActionBar().hide();

        // Get extras from eventView or EventsInvitedTo activity
        Bundle myInput = this.getIntent().getExtras();
        if (myInput == null)
            Log.d("debug", "Intent was null");
        else {
            Log.d("debug", "Intent was ok");

            final TextView ntv = (TextView) this.findViewById(R.id.name_TextView);
            final TextView dtv = (TextView) this.findViewById(R.id.details_TextView);
            final TextView ltv = (TextView) this.findViewById(R.id.location_TextView);
            final TextView datv = (TextView) this.findViewById(R.id.date_TextView);
            final TextView ttv = (TextView) this.findViewById(R.id.time_TextView);

            /*  Assume eventName is unique. Perform query to get other data for event and then set
                TextViews, ListView, and Buttons.
             */
            ParseQuery<ParseObject> query = ParseQuery.getQuery("event");
            query.whereEqualTo("eventName", myInput.getString("selectedName"));
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(final ParseObject object, ParseException e) {
                    if (object == null) {
                        Log.d("event", "The getFirst request failed.");
                    } else {
                        Log.d("event", "Retrieved the object.");

                        eventName = object.getString("eventName");
                        String eventDate = object.getString("eventDate");
                        String eventTime = object.getString("eventTime");
                        String eventDetails = object.getString("eventDetails");
                        String eventLocationAddress = object.getString("eventLocationAddress");
                        ntv.setText(eventName);
                        datv.setText(eventDate);
                        ttv.setText(eventTime);
                        dtv.setText(eventDetails);
                        ltv.setText(eventLocationAddress);

                        // Set the ListView adapter
                        adapter = new EventDetailsCustomAdapter(EventDetails.this, object);
                        ListView listView = (ListView) findViewById(R.id.attendee_listView);
                        listView.setAdapter(adapter);

                        Button accept_button = (Button) findViewById(R.id.accept_invite_button);
                        Button decline_button = (Button) findViewById(R.id.decline_invite_button);
                        Button map_button = (Button) findViewById(R.id.view_map_button);

                        //  Set accept/decline buttons to visible only if current User is on the invitee list
                        ParseQuery inv_query = object.getRelation("invitees").getQuery();
                        inv_query.whereEqualTo("name", ParseUser.getCurrentUser().getString("name"));
                        try
                        {
                            if(!inv_query.find().isEmpty())
                            {
                                accept_button.setVisibility(View.VISIBLE);
                                decline_button.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                accept_button.setVisibility(View.GONE);
                                decline_button.setVisibility(View.GONE);
                            }
                        }
                        catch(ParseException ex)
                        {
                            System.out.println("Query didn't work");
                            ex.printStackTrace();
                        }

                        //  Set View map button to visible only if current User is on the attendee list
                        ParseQuery att_query = object.getRelation("attendees").getQuery();
                        inv_query.whereEqualTo("name", ParseUser.getCurrentUser().getString("name"));
                        try
                        {
                            if(!att_query.find().isEmpty())
                            {
                                map_button.setVisibility(View.VISIBLE);
                                // Start tracking user
                                GPSTracker gps = new GPSTracker(EventDetails.this);
                            }
                            else
                                map_button.setVisibility(View.GONE);
                        }
                        catch(ParseException ex)
                        {
                            System.out.println("Query didn't work");
                            ex.printStackTrace();
                        }

                        accept_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {

                                // Add current user to attendees relation of event class
                                ParseRelation<ParseUser> att_relation = object.getRelation("attendees");
                                att_relation.add(ParseUser.getCurrentUser());

                                // Remove current user from invitees relation of event class
                                ParseRelation<ParseUser> inv_relation = object.getRelation("invitees");
                                inv_relation.remove(ParseUser.getCurrentUser());
                                object.saveInBackground();

                                // Go to eventView
                                Intent intent = new Intent(EventDetails.this, eventView.class);
                                startActivity(intent);
                            }
                        });

                        decline_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {

                                // Remove current user from invitees relation of event class
                                ParseRelation<ParseUser> inv_relation = object.getRelation("invitees");
                                inv_relation.remove(ParseUser.getCurrentUser());
                                object.saveInBackground();

                                // Go to eventView
                                Intent intent = new Intent(EventDetails.this, eventView.class);
                                startActivity(intent);
                            }
                        });

                        map_button.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View arg0)
                            {
                                // Pass event name to MapActivity and go there
                                Intent myIntent = new Intent(EventDetails.this, MapActivity.class);
                                myIntent.putExtra("eventName", eventName);
                                EventDetails.this.startActivity(myIntent);

                            }
                        });
                    }
                }
            });

        }
    }
}

