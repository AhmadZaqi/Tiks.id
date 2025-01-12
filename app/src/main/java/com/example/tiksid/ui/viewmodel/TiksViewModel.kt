package com.example.tiksid.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiksid.data.api.APIRequest
import com.example.tiksid.data.local.TokenController
import com.example.tiksid.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class TiksViewModel: ViewModel() {
    private val _popularMovie = MutableStateFlow<UiState<JSONObject>>(UiState.Loading)
    private val _moviePoster = MutableStateFlow<Bitmap?>(null)
    private val _listMovies = MutableStateFlow(JSONArray())
    private val _detailMovie = MutableStateFlow<UiState<JSONObject>>(UiState.Loading)
    private val _movieSchedule = MutableStateFlow(JSONArray())

    val popularMovie = _popularMovie.asStateFlow()
    val moviePoster = _moviePoster.asStateFlow()
    val listMovies = _listMovies.asStateFlow()
    val detailMovie = _detailMovie.asStateFlow()
    val movieSchedule = _movieSchedule.asStateFlow()

    suspend fun postLogin(
        email: String,
        password: String,
        context: Context
    ): Int {
        val post = APIRequest("Auth/Login", "POST").execute(
            JSONObject()
                .put("email", email)
                .put("password", password)
                .toString()
        )
        if (post.code in 200 until 300) {
            val data = JSONObject(post.data)

            TokenController(context).setToken(data.getString("token"), data.getString("expiredAt"))
        }
        return post.code
    }

    suspend fun getMovieImage(id: Int): Bitmap?{
        val image = APIRequest("Movie/$id/Poster").getImage()
        return image
    }

    fun getPopularMovie(){
        viewModelScope.launch {
            val req = APIRequest("Movie/Popular").execute()
            val popularMovie = JSONObject(req.data)
            _popularMovie.value = UiState.Success(popularMovie)

            val image = getMovieImage(popularMovie.getInt("id"))
            _moviePoster.value = image

        }
    }

    fun getAllMovies(){
        viewModelScope.launch {
            val req = APIRequest("Movie").execute()
            val movies = JSONArray(req.data)
            _listMovies.value = movies
        }
    }

    fun getDetailMovie(id: Int){
        viewModelScope.launch {
            val req = APIRequest("Movie/$id").execute()
            val movie = JSONObject(req.data)
            _detailMovie.value = UiState.Success(movie)

            val image = getMovieImage(movie.getInt("id"))
            _moviePoster.value = image
        }
    }

    fun getMovieSchedule(id: Int){
        viewModelScope.launch {
            val req = APIRequest("Schedule/$id").execute()
            val schedule = JSONArray(req.data)
            _movieSchedule.value = schedule
        }
    }
}