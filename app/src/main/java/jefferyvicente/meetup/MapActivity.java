package jefferyvicente.meetup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



/*import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import org.apache.http.params.HttpParams;
import java.lang.Thread;*/

public class MapActivity extends FragmentActivity implements OnMapReadyCallback
{
    private GoogleMap mMap;
    GPSTracker gps;
    double userLatitude;
    double userLongitude;
    LatLng curr_user_location;
    List<ParseObject> attendeeList;
    ArrayList<LatLng> attendees_locations;
    String eventName;

    ArrayList<LatLng> temp;

    LatLng locationlatLng;
    double locationLatitude;
    double locationLongitude;
    String jsonObjectString = "fail";
    JSONObject t;

    private static final String TAG_CONTACTS = "contacts";

    private static final String TAG_NAME = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getActionBar().hide();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        gps = new GPSTracker(MapActivity.this);
        // check if GPS enabled
        if(gps.canGetLocation())
        {
            set_curr_user_location();
        }
        else
        {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettings();
        }

        // Get eventName from EventDetails
        Bundle myInput = this.getIntent().getExtras();
        if (myInput == null)
            Log.d("debug", "Intent was null");
        else
        {
            Log.d("debug", "Intent was ok");
            eventName = myInput.getString("eventName");
            build_attendees_locations_list();


            // Retrieve locationLatitude and locationLongitude for this event
            ParseQuery<ParseObject> query = ParseQuery.getQuery("event");
            query.whereEqualTo("eventName", eventName);
            try
            {
                // Perform query in main thread
                ParseObject event = query.getFirst();
                ParseGeoPoint locationpoint = event.getParseGeoPoint("placeLocation");
                System.out.println("GeoPoint " + locationpoint);
                locationLatitude = locationpoint.getLatitude();
                locationLongitude = locationpoint.getLongitude();

                System.out.println("GeoPoint " + locationLatitude + locationLongitude);

                locationlatLng = new LatLng(locationLatitude, locationLongitude);
                System.out.println("location latlng: " + locationlatLng);
            }
            catch(ParseException exception)
            {
                System.out.println("The getFirst request failed.");
            }
        }

                /*
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
            // handle the exception...
            // For example consider calling Thread.currentThread().interrupt(); here.
        }
*/

        //distanceParse();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        final String startingDestination ="";

        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        add_attendee_markers();
        mMap.addMarker(new MarkerOptions().position(locationlatLng).title("Destination"));
        move_camera_to_curr_user(10);

        // When update button clicked, replot markers and move camera again
        Button update_button = (Button) findViewById(R.id.update_button);
        update_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                mMap.clear();
                build_attendees_locations_list();
                add_attendee_markers();

                mMap.addMarker(new MarkerOptions().position(locationlatLng).title("Destination"));

                set_curr_user_location();
                move_camera_to_curr_user(10);

            }
        });
    }

    public String distanceParse(LatLng orgin, LatLng destination){

        System.out.println("orgin "+orgin);
        System.out.println("destination " + destination);

        double orgin_lat = orgin.latitude;
        double orgin_lon = orgin.longitude;
        double dest_lat = destination.latitude;
        double dest_lon = destination.longitude;


        //String URL ="https://maps.googleapis.com/maps/api/distancematrix/json?origins=Vancouver+BC&destinations=Victoria+BC&mode=bicycling&language=fr-FR";
        String URL ="https://maps.googleapis.com/maps/api/distancematrix/json?origins="+orgin_lat+","+orgin_lon+"&destinations="+dest_lat+","+dest_lon+"&mode=driving&language=fr-EN";
        System.out.println("URL: "+ URL);
        JsonServiceHandler handler = new JsonServiceHandler();

        String jsonData = handler.makeServiceCall(URL, JsonServiceHandler.GET);

        System.out.println(jsonData);


        try {
            JSONObject jsonRootObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonRootObject.optJSONArray("rows");

            System.out.println("Rows: " + jsonArray);

            JSONObject jsonObj = new JSONObject(jsonData);

            // Getting JSON Array node
            JSONArray contacts = jsonObj.getJSONArray("rows");

            System.out.println("contacts: "+contacts + "Length: "+contacts.length());
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject c = contacts.getJSONObject(i);

                JSONArray name2 = c.getJSONArray("elements");
                System.out.println("Elements: "+name2);

                for(int x = 0; x< name2.length(); x++)
                {
                    JSONObject y = name2.getJSONObject(x);
                    System.out.println("Distance 2" + y);

                    JSONObject phone = y.getJSONObject("duration");
                    String mobile = phone.getString("text");
                    System.out.println("Text: " + mobile);
                    jsonObjectString = mobile.toString();

                }

            }

        }catch (JSONException e) {e.printStackTrace();}

        System.out.println("jsonObjectString"+jsonObjectString);


        return jsonObjectString;
    }

    /******************Below are helper methods for common tasks performed above******************/

    public void move_camera_to_curr_user(int zoomLevel)
    {
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(curr_user_location, zoomLevel);
        mMap.animateCamera(update);
    }

    public void set_curr_user_location()
    {
        userLatitude = gps.getLatitude();
        userLongitude = gps.getLongitude();
        curr_user_location = new LatLng(userLatitude, userLongitude);
    }

    public void add_attendee_markers()
    {
        // Add a marker for each attendee
        for(int i = 0; i<attendees_locations.size(); i++)
        {
            String dataDistanceParse = distanceParse(attendees_locations.get(i), locationlatLng);

            mMap.addMarker(new MarkerOptions().position(attendees_locations.get(i)).
                    title(attendeeList.get(i).getString("name"))
                    .snippet("Drive Time: " + dataDistanceParse));
        }
    }

    public void build_attendees_locations_list()
    {
        //  Assume eventName is unique. Perform query to get event's attendees
        ParseQuery<ParseObject> query = ParseQuery.getQuery("event");
        query.whereEqualTo("eventName", eventName);
        try
        {
            // Get attendees list for this event
            ParseRelation relation = query.getFirst().getRelation("attendees");
            ParseQuery list_query = relation.getQuery();
            attendeeList = list_query.find();
            // Get attendees' locations
            attendees_locations = new ArrayList<LatLng>();
            for (int i = 0; i < attendeeList.size(); i++)
            {
                try
                {
                    double lat = (double) attendeeList.get(i).getNumber("Latitude");
                    double lon = (double) attendeeList.get(i).getNumber("Longitude");
                    LatLng attendeeLoc = new LatLng(lat, lon);
                    attendees_locations.add(attendeeLoc);
                }
                // If an attendee's coordinates are currently undefined, set them to (0,0)
                catch(Exception ex)
                {
                    System.out.println("Attendee's coordinates are currently undefined.");
                    double lat = 0;
                    double lon = 0;
                    LatLng attendeeLoc = new LatLng(lat, lon);
                    attendees_locations.add(attendeeLoc);
                }
            }
        }
        catch(ParseException exception)
        {
            System.out.println("query.find() didn't work");
        }
    }

}

