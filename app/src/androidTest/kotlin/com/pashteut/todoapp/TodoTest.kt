package com.pashteut.todoapp

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.filters.LargeTest
import com.pashteut.todoapp.ui_kit.ToDoAppTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.UUID

@HiltAndroidTest
@LargeTest
class TodoTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    private var mockWebServer: MockWebServer = MockWebServer()

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun addTodoItem() {
        mockWebServer.enqueue(MockResponse().setResponseCode(200))

        composeTestRule.setContent {
            ToDoAppTheme {
                Navigation()
            }
        }

        composeTestRule.onNodeWithContentDescription("Новое").performClick()

        val itemDescription = UUID.randomUUID().toString()
        composeTestRule.onNodeWithTag("description text field").performTextInput(itemDescription)

        composeTestRule.onNodeWithText("Сохранить").performClick()

        composeTestRule.onNodeWithText(itemDescription).assertExists()

        val request = mockWebServer.takeRequest()
    }
}