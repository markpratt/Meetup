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
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;

/**
 * Created by Jack on 2015/11/5.
 */
public class eventViewTesting extends ActivityInstrumentationTestCase2<eventView>
{

    public eventViewTesting()
    {
        super(eventView.class);
    }


    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        getActivity();
    }

    public void testButtonsAreClickAblel()
    {
        onView(withId(R.id.search_button)).perform(click());
        try {
            Thread.sleep(4000);
        }
        catch(InterruptedException ex)
        {
            System.out.println("InterruptedException occurred");
        }
        onView(withId(R.id.createEventButton)).perform(click());
        try {
            Thread.sleep(4000);
        }
        catch(InterruptedException ex)
        {
            System.out.println("InterruptedException occurred");
        }
        onView(withId(R.id.invitedToButton)).perform(click());
        try {
            Thread.sleep(4000);
        }
        catch(InterruptedException ex)
        {
            System.out.println("InterruptedException occurred");
        }
        onView(withId(R.id.attendingButton)).perform(click());

        try
        {
            Thread.sleep(4000);
        }
        catch(InterruptedException ex)
        {
            System.out.println("InterruptedException occurred");
        }
        onView(withId(R.id.search_button)).check(matches(isClickable()));
        onView(withId(R.id.createEventButton)).check(matches(isClickable()));
        onView(withId(R.id.invitedToButton)).check(matches(isClickable()));
        onView(withId(R.id.attendingButton)).check(matches(isClickable()));
    }

}

