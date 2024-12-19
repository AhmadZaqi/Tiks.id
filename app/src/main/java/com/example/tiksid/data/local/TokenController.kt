package com.example.tiksid.data.local

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TokenController(private val context: Context) {
    private val auth = context.getSharedPreferences("Token", Context.MODE_PRIVATE)

    fun getToken(): String = auth.getString("token", "null").toString()

    fun setToken(token: String, exp: String){
        auth.edit()
            .putString("token", token)
            .putString("exp", exp)
            .apply()
    }

    fun isExpired(): Boolean{
        val exp = auth.getString("exp", "null")
        return if (exp == "null") true
        else{
            val expDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(exp!!)
            expDate!!.before(Calendar.getInstance().time)
        }
    }
}