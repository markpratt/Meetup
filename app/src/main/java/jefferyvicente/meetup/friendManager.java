package jefferyvicente.meetup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import 	java.lang.String;
import java.util.ArrayList;

public class friendManager extends Activity {

    private friendManagerCustomAdapter adapter;
    final Context context = this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_manager);
        getActionBar().hide();
        toFriendManger();





    }

    public void toFriendManger(){

        final Context context = this;
        Button button = (Button)findViewById(R.id.search_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                EditText editText = (EditText) findViewById(R.id.friendSeach_editText);
                //String searchfeild  = editText.getText().toString();
                if (editText.getText().toString().trim().length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            "Search Feild is Empty", Toast.LENGTH_LONG).show();
                    ListView listView = (ListView) findViewById(R.id.friendManager_ListView);
                    listView.setAdapter(null);
                } else {

                    querySearch();

                }


            }
        });

    }


    public void querySearch(){
        EditText editText = (EditText) findViewById(R.id.friendSeach_editText);
        String searchfeild  = editText.getText().toString();
        System.out.println(searchfeild);

        /*

        //Querying and database conncetion test
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("name", searchfeild);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    System.out.println("Query FAILED");
                } else {
                    System.out.println("Query Successful");
                }
            }
        });

        */



        adapter = new friendManagerCustomAdapter(this,searchfeild);
        //adapter.setTextKey("name");

        ListView listView = (ListView) findViewById(R.id.friendManager_ListView);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Get selected name from friendManagerCustomAdapter
                ArrayList<ParseObject> friendID = adapter.getFriendID();
                final ParseObject selectedFriendId = friendID.get(position);

                ArrayList<String> friendName = adapter.getFriendName();
                String selectedfriendName = friendName.get(position);

                //My test cases to validate
                System.out.println(position);
                System.out.println(selectedFriendId);
                System.out.println(ParseUser.getCurrentUser());
                System.out.println(selectedfriendName);

                final ParseUser currentUser = ParseUser.getCurrentUser();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setTitle("Confirm");

                alertDialogBuilder.setMessage("Do you want to be friends with " + selectedfriendName).setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.out.println("Yes is pressed!!");


                                ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                                query.whereEqualTo("username", currentUser.getUsername());
                                query.getFirstInBackground(new GetCallback<ParseObject>() {
                                    public void done(final ParseObject object, ParseException e) {
                                        if (object == null) {
                                            System.out.println("Failed");
                                        } else {
                                            System.out.println("Pass");

                                            ParseRelation<ParseObject> relation = object.getRelation("friends");
                                            relation.add(selectedFriendId);
                                            object.saveInBackground();

                                        }
                                    }
                                });

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.out.println("No is pressed!!");

                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend_manager, menu);
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
