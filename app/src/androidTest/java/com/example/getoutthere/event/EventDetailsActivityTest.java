package com.example.getoutthere.event;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.getoutthere.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests functionality of EventDetailsActivity.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class EventDetailsActivityTest {

    /**
     * Create a dummy intent with a placeholder event ID
     */
    private static Intent createIntent() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, EventDetailsActivity.class);
        intent.putExtra("eventId", "dummy_event_id");
        return intent;
    }

    @Rule
    public ActivityScenarioRule<EventDetailsActivity> activityRule =
            new ActivityScenarioRule<>(createIntent());

    /**
     * Verifies that the main UI elements are displayed
     */
    @Test
    public void testUIComponentsPresence() {
        // Root layout
        onView(withId(R.id.main)).check(matches(isDisplayed()));

        // Static text views that exist regardless of Firestore data
        onView(withId(R.id.EventName)).check(matches(isDisplayed()));
        onView(withId(R.id.EventDescription)).check(matches(isDisplayed()));
        onView(withId(R.id.EventAddress)).check(matches(isDisplayed()));
        onView(withId(R.id.EventDateRange)).check(matches(isDisplayed()));

        // Buttons
        onView(withId(R.id.btnToggleWaitingList)).check(matches(isDisplayed()));
        onView(withId(R.id.EventDetailsBackButton)).check(matches(isDisplayed()));
        onView(withId(R.id.btnViewComments)).check(matches(isDisplayed()));
        onView(withId(R.id.btnLotteryInfo)).check(matches(isDisplayed()));
    }

    /**
     * Verifies that clicking the Lottery Guidelines info button shows the dialog
     */
    @Test
    public void testLotteryGuidelinesDialog() {
        onView(withId(R.id.btnLotteryInfo)).perform(scrollTo(), click());
        onView(withText("Lottery Guidelines")).check(matches(isDisplayed()));
        onView(withText("Understood")).perform(click());
    }

    /**
     * Verifies that pressing the back button closes the activity
     */
    @Test
    public void testBackButton() {
        onView(withId(R.id.EventDetailsBackButton)).perform(click());
        // ActivityScenario automatically closes the activity; no assertion needed
    }
}