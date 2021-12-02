package org.d3ifcool.aspirin.ui.authentication

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.d3ifcool.aspirin.MainActivity
import org.d3ifcool.aspirin.R
import org.d3ifcool.aspirin.utils.waitForView
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4ClassRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class AuthenticationTest {

    companion object {
        data class User(
            val username: String,
            val email: String,
            val password: String,
        )

        private val USER_DUMMY = User(
            "Pramudia Putra",
            "pramudiaputr${Math.random()}@gmail.com",
            "qwerty123"
        )
    }

    @Test
    //Register
    fun testA() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        //Navigate to RegisterFragment
        onView(withId(R.id.to_register)).perform(click())

        //Input Register Data
        onView(withId(R.id.edt_username)).perform(
            typeText(USER_DUMMY.username)
        )
        onView(withId(R.id.edt_email)).perform(
            typeText(USER_DUMMY.email)
        )
        onView(withId(R.id.edt_password)).perform(
            typeText(USER_DUMMY.password)
        )

        //Closed softKeyboard
        pressBack()

        //Submit Register
        onView(withId(R.id.button_register)).perform(click())

        //Waiting for Login Fragment
        onView(isRoot()).perform(waitForView(R.id.login_fragment, 10000))

        //Check Test Result
        onView(withId(R.id.login_fragment)).check(matches(isDisplayed()))

        //Test Finnish
        scenario.close()
    }


    @Test
    //Login
    fun testB() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        //Input Login Data
        onView(withId(R.id.edt_email)).perform(
            typeText(USER_DUMMY.email)
        )
        onView(withId(R.id.edt_password)).perform(
            typeText(USER_DUMMY.password)
        )

        //Closed softKeyboard
        pressBack()

        //Submit Register
        onView(withId(R.id.button_login)).perform(click())

        //Waiting for Story Fragment
        onView(isRoot()).perform(waitForView(R.id.story_fragment, 10000))

        //Check Test Result
        onView(withId(R.id.story_fragment)).check(matches(isDisplayed()))

        //Test Finnish
        scenario.close()
    }

    @Test
    //Logout
    fun testC() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        //Open Setting
        onView(withId(R.id.account_icon)).perform(click())

        //Do Logout
        onView(withId((R.id.button_keluar))).perform(click())

        //Waiting for Login Fragment
        onView(isRoot()).perform(waitForView(R.id.login_fragment, 10000))

        //Check Test Result
        onView(withId(R.id.login_fragment)).check(matches(isDisplayed()))

        //Test Finnish
        scenario.close()
    }
}
