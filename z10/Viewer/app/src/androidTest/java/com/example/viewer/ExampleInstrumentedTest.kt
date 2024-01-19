package com.example.viewer

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.Called
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.verify
import org.hamcrest.Matchers.containsString
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith



@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @JvmField
    @Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    //blackbox ui tests
    @Test
    fun demoTest() {
        //demo
        //first - because setting mocking does not update lists on screen
        assert(connector.isMocked())
        //find product
        onView(withText(containsString("product1"))).perform(click())
        onView(withText(containsString("product2"))).perform(click())
        onView(withText(containsString("product3"))).perform(click())
        //find 2nd item in recyclerview - it has at least 3 items
        onView(ViewMatchers.withId(R.id.Items))
            .perform(RecyclerViewActions.actionOnItemAtPosition<ItemAdapter.ViewHolder>(2,
                click()
            ))
        //assert there is no 4th item - has less then 4 items
        assertThrows(Exception::class.java){
            onView(ViewMatchers.withId(R.id.Items))
                .perform(RecyclerViewActions.actionOnItemAtPosition<ItemAdapter.ViewHolder>(3,
                    click()
                ))
        }
        //ensure that item doesn't exist
        assertThrows(Exception::class.java){
            onView(withText(containsString("notUsedProductName"))).perform(click())
        }
    }
    //white box tests for ui - with correct data button will call connector.add
    //dealing with RecyclerView in espresso difficult, so this is actually useful
    @Test
    fun TestAdditionEdgeCases(){
        mockkObject(connector.Companion)
        var addCalled = false
        val s = slot<Product>()
        //7     correct addition
        every{connector.add(capture(s))} answers {addCalled=true;callOriginal()}
        onView(withId(R.id.name)).perform(replaceText("testName"))
        onView(withId(R.id.type)).perform(replaceText("type1"))
        onView(withId(R.id.used)).perform(replaceText("used"))
        onView(withId(R.id.button)).perform(click())
        assert(addCalled)
        onView(withId(R.id.name)).check(matches(withText("")))
        onView(withId(R.id.type)).check(matches(withText("")))
        onView(withId(R.id.used)).check(matches(withText("")))
        assert(s.captured.name=="testName")
        assert(s.captured.type=="type1")
        assert(s.captured.used)
        addCalled = false
        //3     empty name
        onView(withId(R.id.name)).perform(replaceText(""))
        onView(withId(R.id.type)).perform(replaceText("type1"))
        onView(withId(R.id.used)).perform(replaceText("used"))
        onView(withId(R.id.button)).perform(click())
        assert(!addCalled)
        onView(withId(R.id.type)).check(matches(withText("")))
        onView(withId(R.id.used)).check(matches(withText("")))
        addCalled = false
        //3     empty type
        onView(withId(R.id.name)).perform(replaceText("testName"))
        onView(withId(R.id.type)).perform(replaceText(""))
        onView(withId(R.id.used)).perform(replaceText("used"))
        onView(withId(R.id.button)).perform(click())
        assert(!addCalled)
        onView(withId(R.id.name)).check(matches(withText("")))
        onView(withId(R.id.used)).check(matches(withText("")))
        addCalled = false
        //2     empty name & type
        onView(withId(R.id.name)).perform(replaceText(""))
        onView(withId(R.id.type)).perform(replaceText(""))
        onView(withId(R.id.used)).perform(replaceText("used"))
        onView(withId(R.id.button)).perform(click())
        assert(!addCalled)
        onView(withId(R.id.used)).check(matches(withText("")))
        addCalled = false
        //5     used - interpretation
        onView(withId(R.id.name)).perform(replaceText("testname"))
        onView(withId(R.id.type)).perform(replaceText("type1"))
        onView(withId(R.id.used)).perform(replaceText("jytfiy"))
        onView(withId(R.id.button)).perform(click())
        assert(addCalled)
        onView(withId(R.id.name)).check(matches(withText("")))
        onView(withId(R.id.type)).check(matches(withText("")))
        onView(withId(R.id.used)).check(matches(withText("")))
        assert(!s.captured.used)
        addCalled = false
    }
    @Test
    fun testTest(){//is for testing the test enivrenment
        assert(false)
    }
}

