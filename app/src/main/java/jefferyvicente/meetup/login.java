package jefferyvicente.meetup;


import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseUser;


public class login extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Required - Initialize the Parse SDK
        Parse.initialize(this);


        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        //ParseFacebookUtils.initialize(this);

        // Optional - If you don't want to allow Twitter login, you can
        // remove this line (and other related ParseTwitterUtils calls)
       // ParseTwitterUtils.initialize(getString(R.string.twitter_consumer_key),getString(R.string.twitter_consumer_secret));
    }
}