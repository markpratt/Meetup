package jefferyvicente.meetup;

/**
 * Created by Administrator on 10/6/15.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import android.widget.Button;
import android.view.LayoutInflater;
import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;


public class friendManagerCustomAdapter extends ParseQueryAdapter<ParseObject> {

    private ArrayList<ParseObject> friendID = new ArrayList<ParseObject>();
    private ArrayList<String> friendname = new ArrayList<String>();

    public friendManagerCustomAdapter(Context context, final String friendSearch) {

        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {

                System.out.println("friendSearch: " + friendSearch);
                ParseQuery query = new ParseQuery("_User");
                //query.whereEqualTo("name", friendSearch);
                query.whereContains("name", friendSearch);
                //query.orderByDescending("createdAt");
                return query;
            }
        });
    }


    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent)
    {
        if(v == null){
            v = View.inflate(getContext(),R.layout.friend_manager_listview,null);
        }


        super.getItemView(object, v, parent);

        friendID.add(object);
        friendname.add(object.getString("name"));

        //Add the title view to
        TextView titleTextView = (TextView) v.findViewById(R.id.name_textView);
        titleTextView.setText("Name: " + object.getString("name"));


        //Add the email to the list view
        TextView email = (TextView) v.findViewById(R.id.email_textView);
        email.setText("Email: " + object.getString("email"));



        return v;
    }


    public ArrayList<ParseObject> getFriendID()
    {
        return friendID;

    }

    public ArrayList<String> getFriendName()
    {
        return friendname;

    }

}