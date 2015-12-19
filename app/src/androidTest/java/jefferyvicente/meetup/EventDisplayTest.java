package jefferyvicente.meetup;



import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;



/**

 /*
 Test Case 5:
 Create a event and see if the event refresh
 pass if user event have nothing

 */
public class EventDisplayTest extends ActivityInstrumentationTestCase2 <newEventInfo>
{
    public EventDisplayTest()
    {
        super(newEventInfo.class);
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        getActivity();
    }
    public void testEventDisplayed()
    {
        /*
        Insert some test value into views
         */
        onView(withId(R.id.name_editText)).perform(typeText("Brithday Party"));
        try
        {
            Thread.sleep(1000);
        }
        catch(InterruptedException ex)
        {
            System.out.println("InterruptedException occurred");
        }
        //onView(withId(R.id.details_editText)).perform(typeText("Free Pizza for all"));
        try
        {
            Thread.sleep(1000);
        }
        catch(InterruptedException ex)
        {
            System.out.println("InterruptedException occurred");
        }
        //onView(withId(R.id.location_editText)).perform(typeText("CECS 440 LAB"));
        try
        {
            Thread.sleep(1000);
        }
        catch(InterruptedException ex)
        {
            System.out.println("InterruptedException occurred");
        }
        // onView(withId(R.id.date_editText)).perform(typeText("11/30/2015"));

        try
        {
            Thread.sleep(1000);
        }
        catch(InterruptedException ex)
        {
            System.out.println("InterruptedException occurred");
        }
        //  onView(withId(R.id.time_editText)).perform(typeText("6:30"));

        // press the create and send to firends
        onView(withId(R.id.saveEvent_button)).perform(click());

        onView(withId(R.id.name_TextView)).check(doesNotExist());
        //  onView(withId(R.id.name_TextView)).check(matches(withText("Event Name: Brithday Party")));
        // onView(withId(R.id.date_TextView)).check(matches(withText("Event Date: 11/30/2015 at 6:30 ")));
        //onView(withId(R.id.location_TextView)).check(matches(withText("Location: CECS 440 LAB")));
        //onView(withId(R.id.time_TextView)).check(matches(withText("Event Time: 6:30")));
        //onView(withId(R.id.details_TextView)).check(matches(withText("Event Details: Free Pizza for all")));
    }
    /*

             create a empty event :
             fail if the event are created
             pass if event can't be create
     */

    public void CreateEmptyEvent()
    {
        onView(withId(R.id.name_editText)).perform(typeText(""));
        try
        {
            Thread.sleep(1000);
        }
        catch(InterruptedException ex)
        {
            System.out.println("InterruptedException occurred");
        }
        onView(withId(R.id.details_editText)).perform(typeText(""));
        try
        {
            Thread.sleep(1000);
        }
        catch(InterruptedException ex)
        {
            System.out.println("InterruptedException occurred");
        }
        onView(withId(R.id.location_editText)).perform(typeText(""));
        try
        {
            Thread.sleep(1000);
        }
        catch(InterruptedException ex)
        {
            System.out.println("InterruptedException occurred");
        }

        onView(withId(R.id.date_editText)).perform(typeText(""));

        try
        {
            Thread.sleep(1000);
        }
        catch(InterruptedException ex)
        {
            System.out.println("InterruptedException occurred");
        }
        onView(withId(R.id.time_editText)).perform(typeText(""));
        onView(withId(R.id.saveEvent_button)).perform(click());
        onView(withId(R.id.name_TextView)).check(doesNotExist());
        onView(withId(R.id.details_TextView)).check(doesNotExist());
        onView(withId(R.id.location_TextView)).check(doesNotExist());
        onView(withId(R.id.date_TextView)).check(doesNotExist());
        onView(withId(R.id.time_TextView)).check(doesNotExist());
    }
}

