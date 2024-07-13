package com.pashteut.todoapp.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideNetworkClient(): HttpClient =
        HttpClient(Android) {
            expectSuccess = true
            install(ContentNegotiation) {
                json(Json {
                    encodeDefaults = false
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    explicitNulls = false
                })
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
            install(HttpRequestRetry) {
                retryOnException(maxRetries = 2)
                exponentialDelay()
                retryOnExceptionIf { _, throwable ->
                    when (throwable) {
                        is ClientRequestException -> {
                            !(throwable.response.status == HttpStatusCode.Unauthorized ||
                                    throwable.response.status == HttpStatusCode.NotFound)
                        }

                        else -> true
                    }
                }
            }
        }
}