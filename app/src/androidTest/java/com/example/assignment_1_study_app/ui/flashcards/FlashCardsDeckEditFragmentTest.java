package com.example.assignment_1_study_app.ui.flashcards;

import android.view.View;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;

import com.example.assignment_1_study_app.MainActivity;
import com.example.assignment_1_study_app.R;

import org.hamcrest.Matcher;
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

public class FlashCardsDeckEditFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    public void helperCreateDeck(String name) {
        // Helper to make creating decks in each test easier
        onView(withId(R.id.fabAddDeck))
                .perform(click());
        onView(withId(R.id.deckTitle))
                .perform(clearText())
                .perform(typeText(name));
        onView(withId(R.id.btnSaveDeck))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.btnSaveDeck))
                .perform(click());
        onView(withId(R.id.recyclerview_flashcards_deck))
                .perform(RecyclerViewActions.actionOnItem(withChild(withText(name)), click()));
    }

    public void helperDeleteDeck(String name) {
        // Helper to make deleting decks in each test easier
        //onView(withId(R.id.recyclerview_flashcards_deck))
        //        .perform(RecyclerViewActions.actionOnItem(withChild(withText(name)), click()));
        onView(withId(R.id.btnDeleteDeck))
                .perform(click());
    }

    public void helperCreateCard(String front, String back) {
        onView(withId(R.id.fabAddCard))
                .perform(click());
        onView(withId(R.id.card_edit_front))
                .perform(clearText(), typeText(front), closeSoftKeyboard());
        onView(withId(R.id.card_edit_back))
                .perform(clearText(), typeText(back), closeSoftKeyboard());
        onView(withId(R.id.btnSave))
                .perform(click());
    }

    public void helperDeleteCard(String front) {
        onView(withId(R.id.recyclerview_flashcards_card))
                .perform(RecyclerViewActions.actionOnItem(withChild(withText(front)), click()));
        onView(withId(R.id.btnDelete))
                .perform(click());
    }

    @Before
    public void setUp() throws Exception {
        activityActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
        onView(withId(R.id.btnShortcutFlashcards))
                .perform(click());
        helperCreateDeck("Card Test Deck");
    }

    @Test
    public void testCreateCard() {
        // Ensure the card doesn't exist before creating
        onView(allOf(withId(R.id.recyclerview_flashcards_card)))
                .check(matches(not(withChild(withChild(allOf(withId(R.id.listitem_cards_title), withText("Test Card Front Text")))))));

        // Create the card
        helperCreateCard("Test Card Front Text", "Test Card Back Text");

        // Check the card now exists
        onView(allOf(withId(R.id.recyclerview_flashcards_card)))
                .check(matches(withChild(withChild(allOf(withId(R.id.listitem_cards_title), withText("Test Card Front Text"))))));

        // Clean up
        helperDeleteCard("Test Card Front Text");
    }

    @Test
    public void testDeleteCard() {
        // Create card for deletion
        helperCreateCard("Test Card Front Text Delete", "Test Card Back Text Delete");

        // Ensure card exists before deleting
        onView(allOf(withId(R.id.recyclerview_flashcards_card)))
                .check(matches(withChild(withChild(allOf(withId(R.id.listitem_cards_title), withText("Test Card Front Text Delete"))))));

        // Delete card
        helperDeleteCard("Test Card Front Text Delete");

        // Check card no longer exists
        onView(allOf(withId(R.id.recyclerview_flashcards_card)))
                .check(matches(not(withChild(withChild(allOf(withId(R.id.listitem_cards_title), withText("Test Card Front Text Delete")))))));
    }

    @Test
    public void testEditCard() {
        // Create card to edit
        helperCreateCard("Test Card Front Text Edit", "Test Card Back Text Edit");

        // Ensure the card exists and a card with the new name does not
        onView(allOf(withId(R.id.recyclerview_flashcards_card)))
                .check(matches(withChild(withChild(allOf(withId(R.id.listitem_cards_title), withText("Test Card Front Text Edit"))))));
        onView(allOf(withId(R.id.recyclerview_flashcards_card)))
                .check(matches(not(withChild(withChild(allOf(withId(R.id.listitem_cards_title), withText("New Test Card Front Text Edit")))))));

        // Edit card front text
        onView(withId(R.id.recyclerview_flashcards_card))
                .perform(RecyclerViewActions.actionOnItem(withChild(withText("Test Card Front Text Edit")), click()));
        onView(withId(R.id.card_edit_front))
                .perform(clearText())
                .perform(typeText("New Test Card Front Text Edit"));
        onView(withId(R.id.btnSave))
                .perform(closeSoftKeyboard())
                .perform(click());

        // Check old name doesn't exist but new name does
        onView(allOf(withId(R.id.recyclerview_flashcards_card)))
                .check(matches(not(withChild(withChild(allOf(withId(R.id.listitem_cards_title), withText("Test Card Front Text Edit")))))));
        onView(allOf(withId(R.id.recyclerview_flashcards_card)))
                .check(matches(withChild(withChild(allOf(withId(R.id.listitem_cards_title), withText("New Test Card Front Text Edit"))))));

        // Clean up card
        helperDeleteCard("New Test Card Front Text Edit");
    }

    @After
    public void tearDown() throws Exception {
        helperDeleteDeck("Card Test Deck");
    }
}