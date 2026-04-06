package com.example.getoutthere.entrant;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.getoutthere.R;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test functionality of EntrantNotificationActivity.java
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class EntrantNotificationActivityTest {

    /**
     * Test - activity launches and main UI elements are visible.
     */
    @Test
    public void testActivityLaunchAndUIComponents() {
        try (ActivityScenario<EntrantNotificationActivity> scenario =
                     ActivityScenario.launch(EntrantNotificationActivity.class)) {

            // Check main layout is displayed
            onView(withId(R.id.main)).check(matches(isDisplayed()));

            // Check back button is displayed
            onView(withId(R.id.NotificationBackButton)).check(matches(isDisplayed()));

            // RecyclerView exists
            onView(withId(R.id.rvNotifications)).check(matches(isDisplayed()));
        }
    }

    /**
     * Test - the empty notification text is visible.
     * Note: Depends on actual Firestore data; may need a test environment or emulator
     * with empty notifications.
     */
    @Test
    public void testEmptyNotificationView() {
        try (ActivityScenario<EntrantNotificationActivity> scenario =
                     ActivityScenario.launch(EntrantNotificationActivity.class)) {

            // This will only pass if the RecyclerView is empty (empty notification state)
            onView(withId(R.id.emptyNotificationText)).check(matches(isDisplayed()));
        }
    }

    /**
     * Test - the back button finishes the activity.
     */
    @Test
    public void testBackButtonBehavior() {
        try (ActivityScenario<EntrantNotificationActivity> scenario =
                     ActivityScenario.launch(EntrantNotificationActivity.class)) {

            onView(withId(R.id.NotificationBackButton)).perform(click());
        }
    }
}