package com.pashteut.todoapp.todo_api

import com.pashteut.todoapp.common.AppDispatchers
import com.pashteut.todoapp.todo_api.dtos.ElementDTO
import com.pashteut.todoapp.todo_api.dtos.TodoRequestElementDTO
import com.pashteut.todoapp.todo_api.dtos.TodoRequestListDTO
import com.pashteut.todoapp.todo_api.dtos.TodoResponseElementDTO
import com.pashteut.todoapp.todo_api.dtos.TodoResponseListDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Handles all network operations related to ToDo items by communicating with a remote server.
 *
 * This class encapsulates the API calls to a remote server for performing CRUD operations on ToDo items.
 * It uses Ktor client for making HTTP requests and handles serialization and deserialization of request
 * and response data using Kotlin serialization.
 *
 * @property client The HTTP client used for making network requests.
 * @property dispatchers Provides Coroutine Dispatchers for executing operations in the appropriate threads.
 * @constructor Creates an instance of TodoApi with an injected HttpClient and AppDispatchers.
 */

class TodoApi @Inject constructor(
    private val client: HttpClient,
    private val dispatchers: AppDispatchers,
) {
    private val baseUrl = "https://hive.mrdekk.ru/todo"
    private var authorization = ""

    fun setOAuthToken(token: String) {
        authorization = token
    }

    suspend fun getList(): TodoResponseListDTO =
        withContext(dispatchers.io) {
            val response = client.get("$baseUrl/list") {
                headers {
                    append("Authorization", authorization)
                }
            }
            return@withContext response.body()
        }

    suspend fun getElementById(id: String): TodoResponseElementDTO =
        withContext(dispatchers.io) {
            val response = client.get("$baseUrl/list/$id") {
                headers {
                    append("Authorization", authorization)
                }
            }
            return@withContext response.body()
        }

    suspend fun changeList(list: List<ElementDTO>, lastKnownRevision: Int): TodoResponseListDTO =
        withContext(dispatchers.io) {
            val response = client.patch("$baseUrl/list") {
                headers {
                    append("Authorization", authorization)
                    append("X-Last-Known-Revision", lastKnownRevision.toString())
                }
                contentType(ContentType.Application.Json)
                setBody(TodoRequestListDTO(list))
            }
            return@withContext response.body()
        }

    suspend fun changeElement(element: ElementDTO, lastKnownRevision: Int): TodoResponseElementDTO =
        withContext(dispatchers.io) {
            val response = client.put("$baseUrl/list/${element.id}") {
                headers {
                    append("Authorization", authorization)
                    append("X-Last-Known-Revision", lastKnownRevision.toString())
                }
                contentType(ContentType.Application.Json)
                setBody(TodoRequestElementDTO(element))
            }
            return@withContext response.body()
        }

    suspend fun addElement(element: ElementDTO, lastKnownRevision: Int): TodoResponseElementDTO =
        withContext(dispatchers.io) {
            val response = client.post("$baseUrl/list") {
                headers {
                    append("Authorization", authorization)
                    append("X-Last-Known-Revision", lastKnownRevision.toString())
                }
                contentType(ContentType.Application.Json)
                setBody(TodoRequestElementDTO(element))
            }
            return@withContext response.body()
        }

    suspend fun deleteElementById(id: String, lastKnownRevision: Int): TodoResponseElementDTO =
        withContext(dispatchers.io) {
            val response = client.delete("$baseUrl/list/$id") {
                headers {
                    append("Authorization", authorization)
                    append("X-Last-Known-Revision", lastKnownRevision.toString())
                }
            }
            return@withContext response.body()
        }

}