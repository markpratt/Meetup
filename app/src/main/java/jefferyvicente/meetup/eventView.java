package jefferyvicente.meetup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class eventView extends Activity {

    private ListView listView;
    private CustomAdapter adapter2;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);
        getActionBar().setTitle("Events You've Created");

        adapter2 = new CustomAdapter(this);

        listView = (ListView) findViewById(R.id.event_listView);
        listView.setAdapter(adapter2);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                /*
                    Ideally, list of event objects would be obtained from CustomAdapter, and
                    this list would be passed to EventDetails, where data could be extracted from
                    the ParseObject.  However, intent.putExtra doesn't work if second arg is
                    object, unless that object implements Parcelable.  I don't know how to redefine
                    ParseObject to implement Parcelable.  Instead, I will get objectIds String list
                    and send that list to EventDetails.  EventDetails will then have to query
                    database to get event data.
                 */

                // Get selected events from CustomAdapter
                ArrayList<String> events = adapter2.getEvents();
                String selectedEventId = events.get(position);

                // Pass event data to EventDetails
                Intent myIntent = new Intent(eventView.this, EventDetails.class);
                myIntent.putExtra("eventId", selectedEventId);
                eventView.this.startActivity(myIntent);

            }
        });

/************* The rest of onCreate is code to build and handle navigation drawer ************/

        final String[] mActivityTitles = {"Create Event", "Events Invited To", "Events Attending",
                "Add/Delete Friends"};

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                mActivityTitles));

        // Set the drawer list's click listener
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent;
                switch(position)
                {
                    case 0: intent = new Intent(eventView.this, newEventInfo.class);
                            startActivity(intent);
                            mDrawerLayout.closeDrawer(mDrawerList);
                            break;
                    case 1: intent = new Intent(eventView.this, EventsInvitedTo.class);
                            startActivity(intent);
                            mDrawerLayout.closeDrawer(mDrawerList);
                            break;
                    case 2: intent = new Intent(eventView.this, EventsAttending.class);
                            startActivity(intent);
                            mDrawerLayout.closeDrawer(mDrawerList);
                            break;
                    case 3: intent = new Intent(eventView.this, friendManager.class);
                            startActivity(intent);
                            mDrawerLayout.closeDrawer(mDrawerList);
                            break;
                    default:    // stay in eventView
                            break;
                }
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description */
                R.string.navigation_drawer_close  /* "close drawer" description */
        )
        {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

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