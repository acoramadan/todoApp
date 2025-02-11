package com.dicoding.todoapp.ui.list

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.dicoding.todoapp.R
import com.dicoding.todoapp.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

//TODO 16 : Write UI test to validate when user tap Add Task (+), the AddTaskActivity displayed
@RunWith(AndroidJUnit4::class)
@LargeTest
class TaskActivityTest {
    @get:Rule
    val activity = ActivityScenarioRule(TaskActivity::class.java)

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @org.junit.Test

    fun openAndAddTaskSuccesfully() {
        onView(withId(R.id.fab)).perform(click())

        onView(withId(R.id.add_activity)).check(matches(isDisplayed()))
    }
}