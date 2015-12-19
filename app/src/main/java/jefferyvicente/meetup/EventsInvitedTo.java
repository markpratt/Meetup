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

public class EventsInvitedTo extends Activity
{
    private ListView listView;
    private EventsInvitedToCustomAdapter adapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_invited_to);
        getActionBar().hide();

        adapter2 = new EventsInvitedToCustomAdapter(this);
        adapter2.setTextKey("invitedToEventName");

        ListView listView = (ListView) findViewById(R.id.invitedToEvent_listView);
        listView.setAdapter(adapter2);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Get selected eventName from EventsInvitedToCustomAdapter
                ArrayList<String> eventNames = adapter2.getEventNames();
                String selectedName = eventNames.get(position);

                // Pass event data to EventDetails
                Intent myIntent = new Intent(EventsInvitedTo.this, EventDetails.class);
                myIntent.putExtra("selectedName", selectedName);
                EventsInvitedTo.this.startActivity(myIntent);

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
