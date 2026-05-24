package com.example.moviebrowser

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moviebrowser.ui.splash.SplashActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(SplashActivity::class.java)

    @Test
    fun emptyEmail_showsError() {
        // Изчакваме splash да свърши
        Thread.sleep(2500)

        onView(withId(R.id.btn_login)).perform(click())
        onView(withId(R.id.til_email))
            .check(matches(hasDescendant(withText("Въведи имейл"))))
    }

    @Test
    fun emptyPassword_showsError() {
        Thread.sleep(2500)

        onView(withId(R.id.et_email)).perform(typeText("test@test.com"), closeSoftKeyboard())
        onView(withId(R.id.btn_login)).perform(click())
        onView(withId(R.id.til_password))
            .check(matches(hasDescendant(withText("Въведи парола"))))
    }

    @Test
    fun wrongCredentials_showsErrorMessage() {
        Thread.sleep(2500)

        onView(withId(R.id.et_email)).perform(typeText("wrong@test.com"), closeSoftKeyboard())
        onView(withId(R.id.et_password)).perform(typeText("wrongpass"), closeSoftKeyboard())
        onView(withId(R.id.btn_login)).perform(click())

        Thread.sleep(500)

        onView(withId(R.id.tv_error)).check(matches(isDisplayed()))
    }
}