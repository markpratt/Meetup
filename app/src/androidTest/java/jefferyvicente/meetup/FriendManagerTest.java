package jefferyvicente.meetup;



import android.test.ActivityInstrumentationTestCase2;

import jefferyvicente.meetup.friendManager;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;



public class FriendManagerTest extends ActivityInstrumentationTestCase2<friendManager> {

    public FriendManagerTest() {
        super(friendManager.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testNameDisplayed()
    {
        // locate the view with id "friendSeach_editText" and type the text "Mark"
        onView(withId(R.id.friendSeach_editText)).perform(typeText("Mark"));

        // locate the view with id "search_button" and click on it
        onView(withId(R.id.search_button)).perform(click());

        // To leave time for query to execute, sleep for 4 seconds
        try
        {
            Thread.sleep(4000);
        }
        catch(InterruptedException ex)
        {
            System.out.println("InterruptedException occurred");
        }

        // locate the view with id "name_textView" and check its text is equal with "Mark"
        onView(withId(R.id.name_textView)).check(matches(withText("Name: Mark")));

        // locate the view with id "email_textView" and check its text is equal with "prattmk2010@gmail.com"
        onView(withId(R.id.email_textView)).check(matches(withText("Email: prattmk2010@gmail.com")));
    }
}