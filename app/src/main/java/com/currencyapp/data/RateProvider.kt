package com.currencyapp.data

import com.currencyapp.model.Response
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.URL

class RateProvider {

    private var asyncResponse: Deferred<Response>? = null

    suspend fun fetch(): Response {
        asyncResponse = CoroutineScope(Dispatchers.IO).async {
            val connection = URL("https://revolut.duckdns.org/latest?base=EUR").openConnection()
            mapStreamToResponse(connection.getInputStream())
        }
        return asyncResponse!!.await()
    }

    private fun mapStreamToResponse(inputStream: InputStream?): Response {
        val bufferedInputStream = InputStreamReader(inputStream)
        val dataResponse = BufferedReader(bufferedInputStream)
            .lineSequence()
            .fold(StringBuilder(), { builder, line -> builder.append(line) })
            .toString()

        return mapStringToResponse(dataResponse)
    }

    private fun mapStringToResponse(dataResponse: String): Response {
        val gson = Gson()
        return gson.fromJson(dataResponse, Response::class.java)
    }

    fun cancel() {
        asyncResponse?.cancel()
    }
}