package jefferyvicente.meetup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.support.v4.widget.DrawerLayout;

import android.widget.ArrayAdapter;


import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;
import com.parse.Parse.*;
import com.parse.ParseUser;
import com.parse.ParseQuery;
//import java.text.ParseException;
import com.parse.*;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

public class EventsAttending extends Activity
{
    private ListView listView;
    private EventsAttendingCustomAdapter adapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_attending);
        getActionBar().hide();

        adapter2 = new EventsAttendingCustomAdapter(this);
        adapter2.setTextKey("attendingEventName");

        ListView listView = (ListView) findViewById(R.id.attendingEvent_listView);
        listView.setAdapter(adapter2);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Get selected eventName from EventsInvitedToCustomAdapter
                ArrayList<String> eventNames = adapter2.getEventNames();
                String selectedName = eventNames.get(position);

                // Pass event data to EventDetails
                Intent myIntent = new Intent(EventsAttending.this, EventDetails.class);
                myIntent.putExtra("selectedName", selectedName);
                EventsAttending.this.startActivity(myIntent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
