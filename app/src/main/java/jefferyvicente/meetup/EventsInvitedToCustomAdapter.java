
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

import java.util.ArrayList;


public class EventsInvitedToCustomAdapter extends ParseQueryAdapter<ParseObject>
{
    private ArrayList<String> eventNames = new ArrayList<String>();

    public EventsInvitedToCustomAdapter(Context context) {

        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {

                ParseQuery<ParseObject> query = ParseQuery.getQuery("event");
                query.whereEqualTo("invitees", ParseUser.getCurrentUser());
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

        eventNames.add(object.getString("eventName"));

        TextView titleTextView = (TextView) v.findViewById(R.id.eventName_textView);
        titleTextView.setText("Name: " + object.getString("eventName"));

        TextView eventTime = (TextView) v.findViewById(R.id.date_textView);
        eventTime.setText("Date: " + object.getString("eventDate") + " at " + object.getString("eventTime"));

        TextView eventLocation = (TextView) v.findViewById(R.id.location_textView);
        eventLocation.setText("Location: " + object.getString("eventLocationAddress"));

        return v;
    }

    public ArrayList<String> getEventNames() {return eventNames;}

}