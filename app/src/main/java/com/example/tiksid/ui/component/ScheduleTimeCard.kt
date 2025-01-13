package com.example.tiksid.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiksid.ui.theme.TiksidTheme

@Composable
fun ScheduleTimeCard(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean
) {
    Card(
        modifier = modifier.border(
            width = 2.dp,
            color = MaterialTheme.colorScheme.primary,
            shape = MaterialTheme.shapes.small
        ),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = if (!isSelected) MaterialTheme.colorScheme.secondaryContainer
            else MaterialTheme.colorScheme.primary,
        )
    ){
        Text(
            text = text,
            modifier = Modifier.padding(8.dp),
            lineHeight = 14.sp,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ScheduleTimeCardPreview() {
    TiksidTheme {
        ScheduleTimeCard(text = "Tes", isSelected = true)
    }
}