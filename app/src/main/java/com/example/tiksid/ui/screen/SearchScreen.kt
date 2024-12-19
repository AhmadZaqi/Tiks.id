package com.example.tiksid.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiksid.ui.component.MovieDisplay
import com.example.tiksid.ui.viewmodel.TiksViewModel
import org.json.JSONArray

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    navToDetailScreen: (Int)-> Unit,
    viewModel: TiksViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val listMovies by viewModel.listMovies.collectAsState()
    LaunchedEffect(key1 = Unit) {
        viewModel.getAllMovies()
    }
    SearchContent(
        listMovies = listMovies,
        modifier = modifier,
        navToDetailScreen = navToDetailScreen
    )
}

@Composable
fun SearchContent(
    modifier: Modifier = Modifier,
    listMovies: JSONArray,
    navToDetailScreen: (Int) -> Unit
) {
    Column(
        modifier.fillMaxSize()
    ) {
        Text(
            text = "All Movies",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(bottom = 80.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            items(listMovies.length()){
                val movie = listMovies.getJSONObject(it)
                MovieDisplay(
                    id = movie.getInt("id"),
                    title = movie.getString("title"),
                    duration = movie.getInt("duration"),
                    genre = movie.getString("genre"),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navToDetailScreen(movie.getInt("id"))
                        }
                )
            }
        }
    }
}