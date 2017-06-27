package com.example.adeogo.bakingapp.ui;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

import com.example.adeogo.bakingapp.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecordedStepActivityToExoTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void recordedStepActivityToExoTest() {
        ViewInteraction recyclerView = onView(
allOf(withId(R.id.menu_rv), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction recyclerView2 = onView(
allOf(withId(R.id.step_rv), isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));



        ViewInteraction frameLayout = onView(
allOf(withId(R.id.media_view),
childAtPosition(
allOf(withId(R.id.media_view),
childAtPosition(
IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
0)),
0),
isDisplayed()));
        frameLayout.check(matches(isDisplayed()));


        ViewInteraction textView = onView(
allOf(withId(R.id.full_description_tv), withText("Recipe Introduction"),
childAtPosition(
childAtPosition(
withId(R.id.container_exo),
0),
1),
isDisplayed()));
        textView.check(matches(withText("Recipe Introduction")));


        ViewInteraction appCompatButton = onView(
allOf(withId(R.id.nextButton), withText("Next"), isDisplayed()));
        appCompatButton.perform(click());

        }

        private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup)parent).getChildAt(position));
            }
        };
    }
    }
