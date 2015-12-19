package jefferyvicente.meetup;


import android.test.ActivityInstrumentationTestCase2;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.doubleClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 Make sure that user who decline event can no longer see the event
 */
public class EventDetailTest extends ActivityInstrumentationTestCase2 <EventDetails>
{

    public EventDetailTest()
    {
        super(EventDetails.class);
    }



    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        getActivity();
    }


    public void testEventDetailDisplayed()
    {
        onView(withId(R.id.decline_invite_button)).perform(click());
        // decline a event when a user invite you to an event

        try
        {
            Thread.sleep(4000);
        }
        catch(InterruptedException ex)
        {
            System.out.println("InterruptedException occurred");
        }

        onView(withId(R.id.name_TextView)).check(doesNotExist());
        // the user shouldn't  name of the event
    }
}

