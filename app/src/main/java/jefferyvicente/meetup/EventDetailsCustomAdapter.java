package jefferyvicente.meetup;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import android.widget.Button;
import android.view.LayoutInflater;
import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;


public class EventDetailsCustomAdapter extends ParseQueryAdapter<ParseObject> {

    public EventDetailsCustomAdapter(Context context, final ParseObject event) {

        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {

                // Create query to get all attendees in the event
                ParseRelation relation = event.getRelation("attendees");
                ParseQuery query = relation.getQuery();
                return query;
            }
        });
    }


    @Override
    public View getItemView(final ParseObject object, View v, ViewGroup parent)
    {
        if(v == null){
            v = View.inflate(getContext(),R.layout.event_details_listview,null);
        }

        super.getItemView(object, v, parent);

        // Add the User's name to ListView
        TextView nameTextView = (TextView) v.findViewById(R.id.detail_invitee_name);
        nameTextView.setText(object.getString("name"));

        return v;
    }

}

