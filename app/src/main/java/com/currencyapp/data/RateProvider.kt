package com.currencyapp.data

import com.currencyapp.model.Response
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Reader
import java.lang.StringBuilder
import java.net.URL
import java.net.URLConnection

class RateProvider {

    fun fetch(): Response {
        val result = CoroutineScope(Dispatchers.IO).runCatching {
            val connection = URL("https://revolut.duckdns.org/latest?base=EUR").openConnection()
            mapStreamToResponse(connection)
        }
        return result.getOrElse { error -> Response(error) }
    }

    private fun mapStreamToResponse(urlConnection: URLConnection?): Response {
        val bufferedInputStream = InputStreamReader(urlConnection?.getInputStream())
        val dataResponse = BufferedReader(bufferedInputStream as Reader?)
            .lineSequence()
            .fold(StringBuilder(), { builder, line -> builder.append(line) })
            .toString()

        return mapStringToResponse(dataResponse)
    }

    private fun mapStringToResponse(dataResponse: String): Response {
        val gson = Gson()
        return gson.fromJson(dataResponse, Response::class.java)
    }
}