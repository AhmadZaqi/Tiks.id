package com.example.tiksid.ui.component

import android.widget.Button
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RoundedButton(
    modifier: Modifier = Modifier,
    onclick: ()-> Unit,
    text: String,
    isEnabled: Boolean = true
) {
    Button(
        onClick = onclick,
        modifier = modifier,
        enabled = isEnabled
    ) {
        Text(text = text)
    }
}