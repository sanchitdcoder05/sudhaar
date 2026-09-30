package com.san.sudhaar.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.san.sudhaar.ui.components.GoogleSignInButton
import com.san.sudhaar.ui.components.PrimaryButton
import com.san.sudhaar.ui.components.RoundedTextField
import com.san.sudhaar.ui.theme.DarkText

@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: () -> Unit,
    onGoogleSignInClick: () -> Unit,
    onForgotPwdClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "Login!",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText,
            fontFamily = MaterialTheme.typography.displayLarge.fontFamily
        )

        Spacer(modifier = Modifier.height(40.dp))

        RoundedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = "Enter your email"
        )

        Spacer(modifier = Modifier.height(16.dp))

        RoundedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = "Enter your password",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Forgot Password?",
            color = Color.Gray,
            modifier = Modifier
                .align(Alignment.End)
                .clickable { onForgotPwdClick() },
            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Login",
            onClick = { onLoginClick(email, password) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "or",
            fontSize = 16.sp,
            color = DarkText,
            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
        )

        Spacer(modifier = Modifier.height(16.dp))

        GoogleSignInButton(
            onClick = onGoogleSignInClick
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Register Now!",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText,
            modifier = Modifier
                .clickable { onRegisterClick() }
                .padding(bottom = 16.dp),
            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
        )
    }
}