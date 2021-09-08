package com.example.assignment_1_study_app.ui.timetable;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;

import com.example.assignment_1_study_app.MainActivity;
import com.example.assignment_1_study_app.R;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

public class TimetableFragmentTest {
    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        activityActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
        onView(withId(R.id.btnShortcutTimetable))
                .perform(click());
    }

    @After
    public void tearDown() throws Exception {

    }

    public void helperCreateSession(String name) {
        // Helper to make creating decks in each test easier
        onView(withId(R.id.fabAddTimetable))
                .perform(click());
        onView(withId(R.id.timetable_edit_title))
                .perform(clearText())
                .perform(typeText(name))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.timetable_edit_save))
                .perform(click());
    }

    public void helperDeleteSession(String name) {
        // Helper to make deleting decks in each test easier
        onView(allOf(withId(R.id.recyclerview_timetable), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItem(withChild(withText(name)), click()));
        onView(withId(R.id.btnDeleteTimetable))
                .perform(click());
    }

    @Test
    public void testCreateSession() {
        // Ensure session doesn't exist before creating it
        onView(allOf(withId(R.id.recyclerview_timetable), isDisplayed()))
                .check(matches(not(withChild(withChild(allOf(withId(R.id.listitem_timetables_title), withText("Test Class")))))));

        // Create new session and return to timetable view
        helperCreateSession("Test Class");

        // Check the session now exists
        onView(allOf(withId(R.id.recyclerview_timetable), isDisplayed()))
                .check(matches(withChild(withChild(allOf(withId(R.id.listitem_timetables_title), withText("Test Class"))))));

        // Clean up and delete the session
        helperDeleteSession("Test Class");
    }

    @Test
    public void testEditSessionTitle() {
        // Create deck to edit
        helperCreateSession("Test Class");

        // Check if the deck exists before editing it
        onView(allOf(withId(R.id.recyclerview_timetable), isDisplayed()))
                .check(matches(withChild(withChild(allOf(withId(R.id.listitem_timetables_title), withText("Test Class"))))));

        // Change title
        onView(allOf(withId(R.id.recyclerview_timetable), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItem(withChild(withText("Test Class")), click()));
        onView(withId(R.id.timetable_edit_title))
                .perform(clearText())
                .perform(typeText("Class Test"));
        onView(withId(R.id.timetable_edit_save))
                .perform(closeSoftKeyboard())
                .perform(click());

        // Check deck has been renamed
        onView(allOf(withId(R.id.recyclerview_timetable), isDisplayed()))
                .check(matches(not(withChild(withChild(allOf(withId(R.id.listitem_timetables_title), withText("Test Class")))))));
        onView(allOf(withId(R.id.recyclerview_timetable), isDisplayed()))
                .check(matches(withChild(withChild(allOf(withId(R.id.listitem_timetables_title), withText("Class Test"))))));

        // Clean up and delete deck
        helperDeleteSession("Class Test");
    }

    @Test
    public void testDeleteSession() {
        // Create a deck for deletion
        helperCreateSession("Test Class");

        // Check that the deck exists before deleting it
        onView(allOf(withId(R.id.recyclerview_timetable), isDisplayed()))
                .check(matches(withChild(withChild(allOf(withId(R.id.listitem_timetables_title), withText("Test Class"))))));

        // Delete a deck
        helperDeleteSession("Test Class");

        // Check the deck doesn't exist after deleting it
        onView(allOf(withId(R.id.recyclerview_timetable), isDisplayed()))
                .check(matches(not(withChild(withChild(allOf(withId(R.id.listitem_timetables_title), withText("Test Class")))))));
    }
}
