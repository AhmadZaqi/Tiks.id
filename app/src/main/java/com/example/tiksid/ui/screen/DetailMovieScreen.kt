package com.example.tiksid.ui.screen

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiksid.ui.common.UiState
import com.example.tiksid.ui.component.GenreCard
import com.example.tiksid.ui.viewmodel.TiksViewModel
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DetailMovieScreen(
    modifier: Modifier = Modifier,
    movieId: Int,
    viewModel: TiksViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val movie by viewModel.detailMovie.collectAsState()
    val moviePoster by viewModel.moviePoster.collectAsState()

    movie.let {
        when(it){
            is UiState.Loading->{
                CircularProgressIndicator()
                viewModel.getDetailMovie(movieId)
            }
            is UiState.Success->{
                DetailMovieContent(
                    movie = it.data,
                    moviePoster = moviePoster
                )
            }
            is UiState.Error->{}
        }
    }
}

@Composable
fun DetailMovieContent(
    modifier: Modifier = Modifier,
    movie: JSONObject,
    moviePoster: Bitmap?
) {
    val scroll = rememberScrollState()
    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(bottom = 80.dp)
    ) {
        MovieDetail(
            movie = movie,
            moviePoster = moviePoster
        )
        Spacer(modifier = Modifier.height(32.dp))
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            shape = RoundedCornerShape(76.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Stay tuned for the movie schedule updates",
                    modifier = Modifier.weight(1f),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    modifier  =Modifier.size(32.dp),
                )
            }
        }
    }
}

@Composable
fun MovieDetail(
    modifier: Modifier = Modifier,
    movie: JSONObject,
    moviePoster: Bitmap?
) {
    Box(modifier = modifier
        .fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()) {
            moviePoster?.let {
                Image(
                    bitmap = moviePoster.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth,
                )
            }
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background,
                            )
                        )
                    )
            )
        }
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .align(Alignment.BottomCenter)
        ) {
            Text(
                text = movie.getString("title"),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            val releaseYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(movie.getString("releaseDate")))
            Text(
                text = "$releaseYear \u2022 ${movie.getInt("duration")} minutes"
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val genres = movie.getJSONArray("genre")
                items(genres.length()){
                    val genre = genres.getString(it)
                    GenreCard(text = genre)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = movie.getString("description"),
                fontSize = 14.sp,
            )
        }
    }
}