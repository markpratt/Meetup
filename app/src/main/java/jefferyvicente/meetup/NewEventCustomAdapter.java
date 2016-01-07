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


public class NewEventCustomAdapter extends ParseQueryAdapter<ParseObject>
{

    // inviteeList holds invitees
    private ArrayList<ParseObject> inviteeList = new ArrayList<ParseObject>();

    public NewEventCustomAdapter(Context context)
    {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>()
        {
            public ParseQuery create()
            {
                // Get all the friends associated with current User
                final ParseUser currentUser = ParseUser.getCurrentUser();
                ParseRelation relation = currentUser.getRelation("friends");
                ParseQuery query = relation.getQuery();
                return query;
            }
        });
    }


    @Override
    public View getItemView(final ParseObject object, View v, ViewGroup parent)
    {
        if(v == null){
            v = View.inflate(getContext(),R.layout.new_event_listview,null);
        }

        super.getItemView(object, v, parent);

        // If box is checked, add ParseUser to ArrayList; if not, remove
        final CheckBox checkBox = (CheckBox) v.findViewById(R.id.invitee_checkBox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(checkBox.isChecked())
                {
                    inviteeList.add(object);
                }
                else
                    inviteeList.remove(object);
            }
        });


        // Add the User's name to ListView
        TextView nameTextView = (TextView) v.findViewById(R.id.invitee_name);
        nameTextView.setText(object.getString("name"));

        return v;
    }

    public ArrayList<ParseObject> getInviteeList()
    {
        return inviteeList;
    }

}
