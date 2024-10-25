package com.example.cryptotracker.core.data.network

import com.example.cryptotracker.core.domain.util.NetworkError
import com.example.cryptotracker.core.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url

suspend inline fun <reified Response: Any> HttpClient.get(
    endpoint: Endpoint,
    params: Map<String, Any?> = mapOf()
): Result<Response, NetworkError> {
    return safeCall {
        get {
            url(endpoint.url)
            params.forEach { (k, v) ->
                parameter(key = k, value = v)
            }
        }
    }
}

suspend inline fun <reified Response: Any, reified Request: Any> HttpClient.post(
    endpoint: Endpoint,
    body: Request
): Result<Response, NetworkError> {
    return safeCall {
        post {
            url(endpoint.url)
            setBody(body)
        }
    }
}