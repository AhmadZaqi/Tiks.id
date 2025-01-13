package com.example.tiksid.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SeatCard(
    modifier: Modifier = Modifier,
    seatNumber: String,
    isSelected: Boolean,
    onSelected: (String)-> Unit,
    isAvailable: Boolean
) {
    val backgroundColor = when{
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.primaryContainer
    }

    Box(
        modifier = modifier
            .size(50.dp)
            .background(
                color = backgroundColor,
                shape = MaterialTheme.shapes.small
            )
            .alpha(if (isAvailable) 1f else 0.4f)
            .clickable(enabled = isAvailable) {
                onSelected(seatNumber)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = seatNumber,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}