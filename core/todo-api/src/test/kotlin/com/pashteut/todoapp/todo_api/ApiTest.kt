package com.pashteut.todoapp.todo_api

import android.util.Log
import com.pashteut.todoapp.common.AppDispatchers
import com.pashteut.todoapp.todo_api.dtos.ElementDTO
import com.pashteut.todoapp.todo_api.dtos.PriorityDTO
import com.pashteut.todoapp.todo_api.dtos.TodoResponseElementDTO
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ApiTest {

    private lateinit var api: TodoApi
    private lateinit var mockHttpClient: HttpClient
    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.isLoggable(any(), any()) } returns false
        val element = ElementDTO(
            id = "1",
            text = "Test Element",
            importance = PriorityDTO.DEFAULT,
            done = false,
            createdAt = System.currentTimeMillis(),
            changedAt = System.currentTimeMillis(),
            deadline = null,
            color = null,
            lastUpdatedBy = "me"
        )
        mockHttpClient = HttpClient(MockEngine) {
            engine {
                addHandler {
                    val responseJson = Json.encodeToString(
                        TodoResponseElementDTO(
                            status = "success",
                            element = element,
                            revision = 2
                        )
                    )
                    respond(
                        responseJson,
                        HttpStatusCode.OK,
                        headersOf("Content-Type" to listOf("application/json"))
                    )
                }
            }
        }
        val mockDispatchers = Mockito.mock(AppDispatchers::class.java).apply {
            Mockito.`when`(io).thenReturn(testDispatcher)
        }
        api = TodoApi(mockHttpClient, mockDispatchers)
    }

    @Test
    fun `test addElement`() = runTest {
        val element = ElementDTO(
            id = "1",
            text = "Test Element",
            importance = PriorityDTO.DEFAULT,
            done = false,
            createdAt = System.currentTimeMillis(),
            changedAt = System.currentTimeMillis(),
            deadline = null,
            color = null,
            lastUpdatedBy = "me"
        )
        val lastKnownRevision = 1
        every { Log.isLoggable(any(), any()) } returns false
        val response = api.addElement(element, lastKnownRevision)

        val expectedResponse = TodoResponseElementDTO(
            status = "success",
            element = element,
            revision = 2
        )

        assertEquals(expectedResponse, response)
    }
}