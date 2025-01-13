package com.example.tiksid.ui.screen

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiksid.ui.common.UiState
import com.example.tiksid.ui.component.GenreCard
import com.example.tiksid.ui.component.ScheduleTimeCard
import com.example.tiksid.ui.component.SeatCard
import com.example.tiksid.ui.component.TiksDropdown
import com.example.tiksid.ui.viewmodel.TiksViewModel
import org.json.JSONArray
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
    val movieSchedule by viewModel.movieSchedule.collectAsState()

    movie.let {
        when(it){
            is UiState.Loading->{
                CircularProgressIndicator()
                viewModel.getDetailMovie(movieId)
            }
            is UiState.Success->{
                DetailMovieContent(
                    modifier = modifier,
                    movie = it.data,
                    moviePoster = moviePoster,
                    movieSchedule = movieSchedule
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
    moviePoster: Bitmap?,
    movieSchedule: JSONArray
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
        if (movieSchedule.length() <= 0) ComingSoonCard()
        else{
            val theaters = mutableListOf<String>()
            for (i in 0 until movieSchedule.length()){
                val theater = movieSchedule.getJSONObject(i).getString("theaterName")
                theaters.add(theater)
            }
            var selectedTheaterIndex by remember { mutableIntStateOf(0) }
            var selectedDateIndex by remember { mutableIntStateOf(0) }
            var selectedTimeIndex by remember { mutableIntStateOf(0) }
            val theater = movieSchedule.getJSONObject(selectedTheaterIndex)
            val availableDate = theater.getJSONArray("availableDate")
            val availableTime = availableDate.getJSONObject(selectedDateIndex).getJSONArray("availableTime")
            val selectedTime = availableTime.getJSONObject(selectedTimeIndex)
            val filledSeat = selectedTime.getJSONArray("filledSeat")

            val section = theater.getInt("section")
            val row = theater.getInt("row")
            val column = theater.getInt("column")

            val listSelectedSeat = mutableListOf<String>()
            val listFilledSeat = mutableListOf<String>()
            for (i in 0 until filledSeat.length()){
                listFilledSeat.add(filledSeat.getString(i))
            }

            var alphabet = 'A'
            var number = 1

            Text(
                text = "Theater",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            TiksDropdown(
                modifier = Modifier.padding(horizontal = 16.dp),
                items = theaters
            ) {
                selectedTheaterIndex = it
                selectedDateIndex = 0
                selectedTimeIndex = 0

                alphabet = 'A'
                number = 1
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Date",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                modifier = Modifier,
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(availableDate.length()) {
                    val date = availableDate.getJSONObject(it)
                    val format = SimpleDateFormat("EEE\ndd MMM")
                    val parsingDate = SimpleDateFormat("yyyy-MM-dd").parse(date.getString("date"))

                    ScheduleTimeCard(
                        text = format.format(parsingDate),
                        isSelected = selectedDateIndex == it,
                        modifier = Modifier.clickable {
                            selectedDateIndex = it
                            selectedTimeIndex = 0

                            alphabet = 'A'
                            number = 1
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Available Time",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                modifier = Modifier,
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(availableTime.length()) {
                    val date = availableTime.getJSONObject(it)
                    val format = SimpleDateFormat("HH:mm")
                    val parsingTime = SimpleDateFormat("HH:mm:ss").parse(date.getString("time"))

                    ScheduleTimeCard(
                        text = format.format(parsingTime),
                        isSelected = selectedTimeIndex == it,
                        modifier = Modifier.clickable {
                            selectedTimeIndex = it

                            alphabet = 'A'
                            number = 1
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Choose Seat",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(section){ sectionIndex ->
                    alphabet = 'A'
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        for (columnIndex in 0 until column) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                for (rowIndex in 0 until row / section) {
                                    val seatNumber = "$alphabet$number"
                                    var isSelected by remember {
                                        mutableStateOf(false)
                                    }
                                    val isAvailable = !listFilledSeat.any { seatNumber == it }
                                    SeatCard(
                                        seatNumber = seatNumber,
                                        isSelected = isSelected,
                                        onSelected = {
                                            isSelected = !isSelected
                                            if (isSelected) listSelectedSeat.remove(it)
                                            else listSelectedSeat.add(it)
                                        },
                                        isAvailable = isAvailable
                                    )

                                    number++
                                }
                            }
                            number = 1
                            alphabet++
                        }
                    }
                }
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 500.dp)
                    ,
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

@Composable
fun ComingSoonCard(
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(76.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
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