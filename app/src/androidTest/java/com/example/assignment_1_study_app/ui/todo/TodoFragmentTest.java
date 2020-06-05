package com.example.assignment_1_study_app.ui.todo;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;

import com.example.assignment_1_study_app.MainActivity;
import com.example.assignment_1_study_app.R;

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
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

public class TodoFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        activityActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
        onView(withId(R.id.btnShortcutTodo))
                .perform(click());
    }

    @After
    public void tearDown() throws Exception {
    }

    public void helperCreateTodo(String title) {
        // Helper to make creating todos in each test easier
        onView(withId(R.id.fabAddTodo))
                .perform(click());
        onView(withId(R.id.todoTitle))
                .perform(clearText())
                .perform(typeText(title))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.todoSave))
                .perform(click());
    }

    public void helperDeleteTodo(String todo) {
        // Helper to make deleting todos in each test easier
        onView(withId(R.id.recyclerview_todo))
                .perform(RecyclerViewActions.actionOnItem(withChild(withText(todo)), click()));
        onView(withId(R.id.btnDeleteTodo))
                .perform(click());
    }

    @Test
    public void testCreateTodo() {
        // Ensure the todo does not exist before creating it
        onView(allOf(withId(R.id.recyclerview_todo)))
                .check(matches(not(withChild(withChild(allOf(withId(R.id.listitem_todos_title), withText("Test Todo")))))));

        // Create new todo and return to deck view
        helperCreateTodo("Test Todo");

        // Check if the todo now exists
        onView(allOf(withId(R.id.recyclerview_todo)))
                .check(matches(withChild(withChild(allOf(withId(R.id.listitem_todos_title), withText("Test Todo"))))));

        // Clean up and delete the todo we created
        helperDeleteTodo("Test Todo");
    }

    @Test
    public void testEditTodo() {
        // Create deck to edit
        helperCreateTodo("Test Todo");

        // Check if the deck exists before editing it
        onView(allOf(withId(R.id.recyclerview_todo)))
                .check(matches(withChild(withChild(allOf(withId(R.id.listitem_todos_title), withText("Test Todo"))))));

        // Change title
        onView(withId(R.id.recyclerview_todo))
                .perform(RecyclerViewActions.actionOnItem(withChild(withText("Test Todo")), click()));
        onView(withId(R.id.todoTitle))
                .perform(clearText())
                .perform(typeText("Todo Test"));
        onView(withId(R.id.todoSave))
                .perform(closeSoftKeyboard())
                .perform(click());

        // Check deck has been renamed
        onView(allOf(withId(R.id.recyclerview_todo)))
                .check(matches(not(withChild(withChild(allOf(withId(R.id.listitem_todos_title), withText("Test Todo")))))));
        onView(allOf(withId(R.id.recyclerview_todo)))
                .check(matches(withChild(withChild(allOf(withId(R.id.listitem_todos_title), withText("Todo Test"))))));

        // Clean up and delete deck
        helperDeleteTodo("Todo Test");
    }

    @Test
    public void testDeleteTodo() {
        // Create a deck for deletion
        helperCreateTodo("Test Todo");

        // Check that the deck exists before deleting it
        onView(allOf(withId(R.id.recyclerview_todo)))
                .check(matches(withChild(withChild(allOf(withId(R.id.listitem_todos_title), withText("Test Todo"))))));

        // Delete a deck
        helperDeleteTodo("Test Todo");

        // Check the deck doesn't exist after deleting it
        onView(allOf(withId(R.id.recyclerview_todo)))
                .check(matches(not(withChild(withChild(allOf(withId(R.id.listitem_todos_title), withText("Test Todo")))))));
    }
}