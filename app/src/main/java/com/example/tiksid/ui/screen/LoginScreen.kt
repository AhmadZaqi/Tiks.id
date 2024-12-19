package com.example.tiksid.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tiksid.R
import com.example.tiksid.ui.component.RoundedButton
import com.example.tiksid.ui.component.TextBox
import com.example.tiksid.ui.viewmodel.TiksViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: TiksViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navToMainScreen: ()-> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var email by remember {
        mutableStateOf("alice.johnson@example.com")
    }
    var password by remember {
        mutableStateOf("password123")
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.55f)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.FillWidth
            )
            Spacer(modifier = Modifier.height(48.dp))
            Text(text = "Email")
            Spacer(modifier = Modifier.height(8.dp))
            TextBox(
                value = email,
                onValueChange = {email = it},
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Password")
            Spacer(modifier = Modifier.height(8.dp))
            TextBox(
                value = password,
                onValueChange = {password = it},
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(24.dp))
            RoundedButton(
                onclick = {
                    if (email.isEmpty() || password.isEmpty()){
                        Toast.makeText(context, "All field must be filled", Toast.LENGTH_SHORT).show()
                        return@RoundedButton
                    }
                    scope.launch {
                        val code = viewModel.postLogin(email, password, context)
                        when (code){
                            in 200 until 300 -> navToMainScreen()
                            400 -> Toast.makeText(context, "Wrong password", Toast.LENGTH_SHORT).show()
                            else -> Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                text = "Login",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LoginPreview() {
    LoginScreen(navToMainScreen = {})
}