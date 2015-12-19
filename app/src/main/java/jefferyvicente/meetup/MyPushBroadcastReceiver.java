/*
    This class is necessary because, when users click a notification, they are directed to
    the app's main activity by default.  MyPushBroadCastReceiver overrides getActivity to direct
    users to EventDetails.
*/

package jefferyvicente.meetup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.parse.ParsePushBroadcastReceiver;


public class MyPushBroadcastReceiver extends ParsePushBroadcastReceiver
{
    @Override
    protected Class<? extends Activity> getActivity(Context context, Intent intent)
    {
        Class<? extends Activity> cls = null;
        try
        {
            cls = (Class <? extends Activity>)Class.forName("jefferyvicente.meetup.EventsInvitedTo");
        }
        catch (ClassNotFoundException e)
        {
            // Should something else be done when class not found?
            System.out.println("ClassNotFoundException caught");
        }
        return cls;
    }
}
