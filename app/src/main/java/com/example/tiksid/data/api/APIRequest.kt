package com.example.tiksid.data.api

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class APIRequest(private val url: String, private val method: String = "GET") {
    private val conn = URL("http://10.0.2.2:5000/api/$url").openConnection() as HttpURLConnection

    init {
        conn.requestMethod = method
        conn.setRequestProperty("Content-Type", "application/json")
    }

    suspend fun execute(body: String? = null): APIResponse {
        return withContext(Dispatchers.IO) {
            body?.let {
                val writer = DataOutputStream(conn.outputStream).apply {
                    writeBytes(body)
                }
            }
            val reader = when(conn.responseCode) {
                in 200 until 300 -> BufferedReader(InputStreamReader(conn.inputStream))
                else -> BufferedReader(InputStreamReader(conn.errorStream))
            }
            val builder = StringBuilder().append(reader.readLine())
            APIResponse(builder.toString(), conn.responseCode)
        }
    }

    suspend fun getImage(): Bitmap?{
        return withContext(Dispatchers.IO){
            try {
                BitmapFactory.decodeStream(conn.inputStream)
            }
            catch (e:Exception){
                null
            }
        }
    }
}