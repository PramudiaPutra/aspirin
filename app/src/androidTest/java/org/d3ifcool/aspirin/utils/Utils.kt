package org.d3ifcool.aspirin.utils

import android.view.View
import androidx.test.espresso.util.HumanReadables
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.util.TreeIterables
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Matcher
import java.util.concurrent.TimeoutException
import android.os.IBinder

import android.view.WindowManager
import androidx.test.espresso.Root
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher


fun waitForView(viewId: Int, millis: Long): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return isRoot()
        }

        override fun getDescription(): String {
            return "wait for a specific view with id <$viewId> during $millis millis."
        }

        override fun perform(uiController: UiController, view: View?) {
            uiController.loopMainThreadUntilIdle()
            val startTime = System.currentTimeMillis()
            val endTime = startTime + millis
            val viewMatcher: Matcher<View> = withId(viewId)
            do {
                for (child in TreeIterables.breadthFirstViewTraversal(view)) {
                    // found view with required ID
                    if (viewMatcher.matches(child)) {
                        return
                    }
                }
                uiController.loopMainThreadForAtLeast(50)
            } while (System.currentTimeMillis() < endTime)
            throw PerformException.Builder()
                .withActionDescription(this.description)
                .withViewDescription(HumanReadables.describe(view))
                .withCause(TimeoutException())
                .build()
        }
    }
}