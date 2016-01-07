
package jefferyvicente.meetup;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.app.Activity;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import android.widget.EditText;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toast;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class EventsInvitedToCustomAdapter extends ParseQueryAdapter<ParseObject>
{
    private ArrayList<String> events = new ArrayList<String>();

    public EventsInvitedToCustomAdapter(Context context) {

        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {

                // Get all events where the current user is on the invitees list
                ParseQuery<ParseObject> query = ParseQuery.getQuery("event");
                query.whereEqualTo("invitees", ParseUser.getCurrentUser());
                query.orderByDescending("createdAt");
                return query;
            }
        });
    }

    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent)
    {
        if(v == null){
            v = View.inflate(getContext(),R.layout.listview,null);
        }

        super.getItemView(object, v, parent);

        events.add(object.getObjectId());

        // Display the event name
        TextView titleTextView = (TextView) v.findViewById(R.id.eventName_textView);
        titleTextView.setText("Name: " + object.getString("eventName"));

        // Display the event date and time
        TextView eventTime = (TextView) v.findViewById(R.id.date_textView);
        SimpleDateFormat ft = new SimpleDateFormat ("MMMM dd, yyyy 'at' hh:mm a");
        eventTime.setText("Date: " + ft.format(object.getDate("eventDate")));

        // Display the event location
        TextView eventLocation = (TextView) v.findViewById(R.id.location_textView);
        eventLocation.setText("Location: " + object.getString("eventLocationAddress"));

        return v;
    }

    public ArrayList<String> getEvents() {return events;}

}