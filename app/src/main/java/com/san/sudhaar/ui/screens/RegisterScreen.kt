package com.san.sudhaar.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.san.sudhaar.ui.components.GoogleSignInButton
import com.san.sudhaar.ui.components.PrimaryButton
import com.san.sudhaar.ui.components.RoundedTextField
import com.san.sudhaar.ui.theme.DarkText

@Composable
fun RegisterScreen(
    onRegisterClick: (String, String, String) -> Unit,
    onLoginClick: () -> Unit,
    onGoogleSignInClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Register!",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText,
            fontFamily = MaterialTheme.typography.displayLarge.fontFamily
        )

        Spacer(modifier = Modifier.height(30.dp))

        RoundedTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = "Enter your name"
        )

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(16.dp))

        RoundedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = "Confirm your password",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Register",
            onClick = {
                if (password == confirmPassword && password.isNotEmpty()) {
                    onRegisterClick(name, email, password)
                }
            }
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
            text = "Back to Login",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText,
            modifier = Modifier
                .clickable { onLoginClick() }
                .padding(bottom = 16.dp),
            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
        )
    }
}