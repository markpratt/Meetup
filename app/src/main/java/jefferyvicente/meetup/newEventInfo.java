package jefferyvicente.meetup;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.parse.Parse;
import com.parse.*;

import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class newEventInfo extends Activity {

    private Button button;
    private NewEventCustomAdapter adapter;
    private ParseObject eventinfo;
    private ParseGeoPoint point;
    private int yr;
    private int mOY;
    private int dOM;
    private Date chosenDate;

    private static final int REQUEST_PLACE_PICKER = 1;
    Context mContext = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_info);
        getActionBar().hide();

        // Set the ListView adapter
        adapter = new NewEventCustomAdapter(this);
        ListView listView = (ListView) findViewById(R.id.invitee_listView);
        listView.setAdapter(adapter);

        editTextLocationListener();
        editTextDateListener();
        editTextTimeListener();
        addListenerOnButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_event_info, menu);
        return true;
    }

    private void editTextLocationListener(){
        EditText locationText = (EditText) findViewById(R.id.location_editText);

        locationText.setInputType(InputType.TYPE_NULL);
        locationText.requestFocus();

        locationText.setClickable(true);
        locationText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                placepicker();
            }
        });
    }

    private void editTextDateListener()
    {
        final EditText dateText = (EditText) findViewById(R.id.date_editText);
        dateText.setInputType(InputType.TYPE_NULL);
        dateText.requestFocus();

        final DatePickerDialog DatePickerDialog;
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);

        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                yr = year;
                mOY = monthOfYear;
                dOM = dayOfMonth;
                newDate.set(year, monthOfYear, dayOfMonth);
                dateText.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        dateText.setClickable(true);
        // Display the DatePickerDialog when date_editText clicked
        dateText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {DatePickerDialog.show();}
        });
    }

    private void editTextTimeListener()
    {
        final EditText timeText = (EditText) findViewById(R.id.time_editText);
        timeText.setInputType(InputType.TYPE_NULL);
        timeText.requestFocus();

        final TimePickerDialog TimePickerDialog;
        final SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a", Locale.US);

        Calendar newCalendar = Calendar.getInstance();
        TimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener()
        {

            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(yr, mOY, dOM, hourOfDay, minute);
                chosenDate = newDate.getTime();
                timeText.setText(timeFormatter.format(chosenDate));
            }

        },newCalendar.get(Calendar.HOUR), newCalendar.get(Calendar.MINUTE), true);

        timeText.setClickable(true);
        // Display the TimePickerDialog when time_editText clicked
        timeText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {TimePickerDialog.show();
            }
        });
    }

    private void addListenerOnButton() {

        final Context context = this;

        button = (Button) findViewById(R.id.saveEvent_button);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                EditText name_editText = (EditText) findViewById(R.id.name_editText);
                String name = name_editText.getText().toString();

                EditText details_editText = (EditText) findViewById(R.id.details_editText);
                String details = details_editText.getText().toString();

                EditText location_editText = (EditText) findViewById(R.id.location_editText);
                String location = location_editText.getText().toString();

                EditText date_editText = (EditText) findViewById(R.id.date_editText);
                String date = date_editText.getText().toString();

                EditText time_editText = (EditText) findViewById(R.id.time_editText);
                String time = time_editText.getText().toString();

                // point == null MUST be checked, else eventinfo.put("placeLocation", point) may throw exception
                if(point == null)
                    Toast.makeText(getApplicationContext(), "You must choose a location", Toast.LENGTH_LONG).show();
                if(name.equals(""))
                    Toast.makeText(getApplicationContext(), "You must enter a name", Toast.LENGTH_LONG).show();
                if(location.equals(""))
                    Toast.makeText(getApplicationContext(), "You must choose a location", Toast.LENGTH_LONG).show();
                if(date.equals(""))
                    Toast.makeText(getApplicationContext(), "You must choose a date", Toast.LENGTH_LONG).show();
                if(time.equals(""))
                    Toast.makeText(getApplicationContext(), "You must choose a time", Toast.LENGTH_LONG).show();
                else
                {
                    eventinfo = new ParseObject("event");
                    eventinfo.put("eventName", name);
                    eventinfo.put("eventDetails", details);
                    eventinfo.put("eventLocationAddress", location);
                    eventinfo.put("placeLocation", point);
                    eventinfo.put("eventDate", chosenDate);
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    eventinfo.put("eventCreator", currentUser);

                    // Get the friends who were checked
                    ArrayList<ParseObject> inviteeList = adapter.getInviteeList();

                    ParseRelation<ParseUser> inv_relation = eventinfo.getRelation("invitees");
                    for (ParseObject invitee : inviteeList)
                    {
                        // Add each friend who was checked to invitees relation
                        ParseUser user = (ParseUser) invitee;
                        inv_relation.add(user);

                        // Send each invitee a push notification (email is assumed to be unique)
                        ParseQuery query2 = ParseInstallation.getQuery();
                        query2.whereEqualTo("email", user.getString("email"));
                        ParsePush.sendMessageInBackground("You've been invited to a Meetup Event!", query2);
                    }

                    // Add current user (the event creator) to attendees list
                    ParseRelation<ParseUser> att_relation = eventinfo.getRelation("attendees");
                    att_relation.add(ParseUser.getCurrentUser());

                    // Save collected data to event class
                    eventinfo.saveInBackground();

                    // After new event created, go back to eventView
                    Intent intent = new Intent(context, eventView.class);
                    startActivity(intent);
                }
            }

        });
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

    public void placepicker()
    {
        try{

            PlacePicker.IntentBuilder intentbuilder = new PlacePicker.IntentBuilder();
            Intent intent = intentbuilder.build(this);

            startActivityForResult(intent,REQUEST_PLACE_PICKER);

        }catch (GooglePlayServicesRepairableException e){
            System.out.println("Error");
        }catch (GooglePlayServicesNotAvailableException e){
            System.out.println("Error");
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PLACE_PICKER) {

            if (resultCode == RESULT_OK) {


                final Place place = PlacePicker.getPlace(data, mContext);

               // String toastMsg = String.format(place.getName());
                EditText finaladdress = (EditText)findViewById(R.id.location_editText);
                finaladdress.setText(place.getAddress());

                double temp = place.getLatLng().longitude;
                double temp2 = place.getLatLng().latitude;
                point = new ParseGeoPoint(temp2,temp);
                System.out.println(point);

                Toast.makeText(this, place.getAddress(), Toast.LENGTH_LONG).show();
            }
        }
    }



}
