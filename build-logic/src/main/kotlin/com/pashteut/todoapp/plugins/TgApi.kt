package com.pashteut.todoapp.plugins

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.escapeIfNeeded
import java.io.File

class TgApi(private val httpClient: HttpClient) {

    private val BASE_URL = "https://api.telegram.org"

    suspend fun sendFile(file: File, token: String, chatId: String): HttpResponse {
        return httpClient.post("$BASE_URL/bot$token/sendDocument") {
            parameter("chat_id", chatId)
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("document", file.readBytes(), Headers.build {
                            append(
                                HttpHeaders.ContentDisposition,
                                "filename=${file.name.escapeIfNeeded()}"
                            )
                        })
                    }
                )
            )
        }
    }

    suspend fun sendFileWithNewName(
        file: File,
        newFileName: String,
        token: String,
        chatId: String
    ): HttpResponse {
        return httpClient.post("$BASE_URL/bot$token/sendDocument") {
            parameter("chat_id", chatId)
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("document", file.readBytes(), Headers.build {
                            append(
                                HttpHeaders.ContentDisposition,
                                "form-data; name=\"file\"; filename=\"$newFileName\""
                            )
                        })
                    }
                )
            )
        }
    }

    suspend fun sendMessage(message: String, token: String, chatId: String): HttpResponse {
        return httpClient.post("$BASE_URL/bot$token/sendMessage") {
            parameter("chat_id", chatId)
            parameter("text", message)
        }
    }
}