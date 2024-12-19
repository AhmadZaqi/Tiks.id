package com.example.tiksid.ui.screen

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiksid.ui.common.UiState
import com.example.tiksid.ui.component.MovieDisplay
import com.example.tiksid.ui.viewmodel.TiksViewModel
import org.json.JSONArray
import org.json.JSONObject

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navToDetailScreen: (Int)-> Unit,
    viewModel: TiksViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val popularMovie by viewModel.popularMovie.collectAsState()
    val popularMoviePoster by viewModel.moviePoster.collectAsState()
    val listMovies by viewModel.listMovies.collectAsState()

    popularMovie.let {
        when(it){
            is UiState.Loading->{
                CircularProgressIndicator()
                viewModel.getAllMovies()
                viewModel.getPopularMovie()
            }
            is UiState.Success->{
                HomeContent(
                    popularMovie = it.data,
                    popularMoviePoster = popularMoviePoster,
                    listMovies = listMovies,
                    navToDetailScreen = navToDetailScreen
                )
            }
            is UiState.Error->{}
        }
    }
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    popularMovie: JSONObject,
    popularMoviePoster: Bitmap?,
    listMovies: JSONArray,
    navToDetailScreen: (Int) -> Unit
) {
    val scrollState = rememberScrollState()
    Box(modifier = modifier
        .fillMaxSize()
        .verticalScroll(scrollState)){
        Column(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(
                text = popularMovie.getString("status"),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navToDetailScreen(popularMovie.getInt("id"))
                    }
            ) {
                popularMoviePoster?.let {
                    Image(
                        bitmap = popularMoviePoster.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.FillWidth
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = popularMovie.getString("title"),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text(
                    text = "${popularMovie.getString("genre")} \u2022 ${popularMovie.getInt("duration")} minutes",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "New titles",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listMovies.length()){
                    val movie = listMovies.getJSONObject(it)
                    MovieDisplay(
                        id = movie.getInt("id"),
                        title = movie.getString("title"),
                        duration = movie.getInt("duration"),
                        genre = movie.getString("genre"),
                        modifier = Modifier
                            .clickable {
                                navToDetailScreen(movie.getInt("id"))
                            }
                    )
                }
            }
            Spacer(modifier = Modifier.height(76.dp))
        }
    }
}