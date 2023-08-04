package com.fourdevs.diuquestionbank.utilities

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalCoroutinesApi::class)
suspend inline fun <reified T> HttpResponse.await(): T {

    val body = body<T>()
    val status = call.response.status.value

    return suspendCancellableCoroutine { cont ->
        if(status == 200) {
            cont.resume(body, null)
        } else {
            cont.resumeWithException(Exception(body.toString()))
        }

    }
}