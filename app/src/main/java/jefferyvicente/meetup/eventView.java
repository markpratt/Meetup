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

public class eventView extends Activity {

    private ParseQueryAdapter<ParseObject> mainAdapter;
    private ListView listView;
    private CustomAdapter adapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);
        getActionBar().hide();
        toNewEventInfo();
        toFriendManger();
        toEventsInvitedTo();
        toEventsAttending();
        final ParseUser currentUser = ParseUser.getCurrentUser();

        Intent i = getIntent();
        System.out.println("Intent: " + i);

        adapter2 = new CustomAdapter(this);

        ListView listView = (ListView) findViewById(R.id.event_listView);
        listView.setAdapter(adapter2);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /*
                    Ideally, list of event objects would be obtained from CustomAdapter, and
                    this list would be passed to EventDetails, where data could be extracted from
                    the ParseObject.  However, intent.putExtra doesn't work if second arg is
                    object, unless that object implements Parcelable.  I don't know how to redefine
                    ParseObject to implement Parcelable.  Instead, I will get eventNames String list
                    (and assume that each eventName is unique) and send that list to EventDetails.
                    EventDetails will then have to query database to get event data.
                 */

                /* Update 12/22:  use objectIds instead of names.  */

                // Get selected events from CustomAdapter
                ArrayList<String> events = adapter2.getEvents();
                String selectedEventId = events.get(position);

                // Pass event data to EventDetails
                Intent myIntent = new Intent(eventView.this, EventDetails.class);
                myIntent.putExtra("eventId", selectedEventId);
                eventView.this.startActivity(myIntent);

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


    public void toNewEventInfo(){

        final Context context = this;
        Button button = (Button)findViewById(R.id.createEventButton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                Intent intent = new Intent(context, newEventInfo.class);
                startActivity(intent);
            }
        });

    }

    public void toFriendManger() {

        final Context context = this;
        Button button = (Button) findViewById(R.id.friendManageButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, friendManager.class);
                startActivity(intent);
            }
        });

    }

    public void toEventsInvitedTo(){

        final Context context = this;
        Button button = (Button)findViewById(R.id.invitedToButton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                Intent intent = new Intent(context, EventsInvitedTo.class);
                startActivity(intent);
            }
        });

    }

    public void toEventsAttending(){

        final Context context = this;
        Button button = (Button)findViewById(R.id.attendingButton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                Intent intent = new Intent(context, EventsAttending.class);
                startActivity(intent);
            }
        });

    }
}