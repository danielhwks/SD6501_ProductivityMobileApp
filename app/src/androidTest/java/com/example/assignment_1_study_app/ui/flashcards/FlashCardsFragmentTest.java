package com.example.assignment_1_study_app.ui.flashcards;

import android.view.View;

import androidx.fragment.app.FragmentFactory;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.example.assignment_1_study_app.MainActivity;
import com.example.assignment_1_study_app.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

public class FlashCardsFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        activityActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
        onView(withId(R.id.btnShortcutFlashcards))
                .perform(click());
    }

    @After
    public void tearDown() throws Exception {
    }

    public void helperCreateDeck(String name) {
        // Helper to make creating decks in each test easier
        onView(withId(R.id.fabAddDeck))
                .perform(click());
        onView(withId(R.id.deckTitle))
                .perform(clearText())
                .perform(typeText(name))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.btnSaveDeck))
                .perform(click());
    }

    public void helperDeleteDeck(String name) {
        // Helper to make deleting decks in each test easier
        onView(withId(R.id.recyclerview_flashcards_deck))
                .perform(RecyclerViewActions.actionOnItem(withChild(withText(name)), click()));
        onView(withId(R.id.btnDeleteDeck))
                .perform(click());
    }

    @Test
    public void testCreateDeck() {
        // Ensure the deck does not exist before creating it
        onView(allOf(withId(R.id.recyclerview_flashcards_deck)))
                .check(matches(not(withChild(withChild(allOf(withId(R.id.listitem_decks_title), withText("Test Deck")))))));

        // Create new deck and return to deck view
        helperCreateDeck("Test Deck");

        // Check if the deck now exists
        onView(allOf(withId(R.id.recyclerview_flashcards_deck)))
                .check(matches(withChild(withChild(allOf(withId(R.id.listitem_decks_title), withText("Test Deck"))))));

        // Clean up and delete the deck we created
        helperDeleteDeck("Test Deck");
    }

    @Test
    public void testEditTitleDeck() {
        // Create deck to edit
        helperCreateDeck("Test Deck");

        // Check if the deck exists before editing it
        onView(allOf(withId(R.id.recyclerview_flashcards_deck)))
                .check(matches(withChild(withChild(allOf(withId(R.id.listitem_decks_title), withText("Test Deck"))))));

        // Change title
        onView(withId(R.id.recyclerview_flashcards_deck))
                .perform(RecyclerViewActions.actionOnItem(withChild(withText("Test Deck")), click()));
        onView(withId(R.id.deckTitle))
                .perform(clearText())
                .perform(typeText("Deck Test"));
        onView(withId(R.id.btnSaveDeck))
                .perform(closeSoftKeyboard())
                .perform(click());

        // Check deck has been renamed
        onView(allOf(withId(R.id.recyclerview_flashcards_deck)))
                .check(matches(not(withChild(withChild(allOf(withId(R.id.listitem_decks_title), withText("Test Deck")))))));
        onView(allOf(withId(R.id.recyclerview_flashcards_deck)))
                .check(matches(withChild(withChild(allOf(withId(R.id.listitem_decks_title), withText("Deck Test"))))));

        // Clean up and delete deck
        helperDeleteDeck("Deck Test");
    }

    @Test
    public void testDeleteDeck() {
        // Create a deck for deletion
        helperCreateDeck("Test Deck");

        // Check that the deck exists before deleting it
        onView(allOf(withId(R.id.recyclerview_flashcards_deck)))
                .check(matches(withChild(withChild(allOf(withId(R.id.listitem_decks_title), withText("Test Deck"))))));

        // Delete a deck
        helperDeleteDeck("Test Deck");

        // Check the deck doesn't exist after deleting it
        onView(allOf(withId(R.id.recyclerview_flashcards_deck)))
                .check(matches(not(withChild(withChild(allOf(withId(R.id.listitem_decks_title), withText("Test Deck")))))));
    }
}