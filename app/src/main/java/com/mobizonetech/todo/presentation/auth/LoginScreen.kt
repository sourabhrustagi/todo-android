package com.mobizonetech.todo.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobizonetech.todo.presentation.common.RandomLogo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    isLoading: Boolean = false,
    error: String? = null
) {
    var selectedTab by remember { mutableStateOf(LoginMethod.PHONE) }
    var phone by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var otpError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var otpRequested by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Random Logo at the top
        RandomLogo(
            modifier = Modifier.padding(bottom = 32.dp),
            size = 100,
            showAppName = false
        )
        
        Text("Sign In", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        TabRow(selectedTabIndex = selectedTab.ordinal) {
            Tab(
                selected = selectedTab == LoginMethod.PHONE,
                onClick = { selectedTab = LoginMethod.PHONE },
                text = { Text("Phone/OTP") }
            )
            Tab(
                selected = selectedTab == LoginMethod.EMAIL,
                onClick = { selectedTab = LoginMethod.EMAIL },
                text = { Text("Email/Password") }
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        when (selectedTab) {
            LoginMethod.PHONE -> {
                OutlinedTextField(
                    value = phone,
                    onValueChange = {
                        phone = it
                        phoneError = null
                    },
                    label = { Text("Phone Number") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    isError = phoneError != null,
                    supportingText = phoneError?.let { { Text(it) } },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = if (otpRequested) ImeAction.Next else ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                if (otpRequested) {
                    OutlinedTextField(
                        value = otp,
                        onValueChange = {
                            otp = it
                            otpError = null
                        },
                        label = { Text("OTP") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        isError = otpError != null,
                        supportingText = otpError?.let { { Text(it) } },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
                Button(
                    onClick = {
                        if (!phone.matches(Regex("^\\+[1-9]\\d{1,14}$"))) {
                            phoneError = "Enter a valid phone number (e.g. +1234567890)"
                            return@Button
                        }
                        if (!otpRequested) {
                            otpRequested = true
                        } else {
                            if (otp.length < 4) {
                                otpError = "Enter a valid OTP"
                                return@Button
                            }
                            // Simulate successful login
                            onLoginSuccess()
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(if (!otpRequested) "Request OTP" else "Login")
                }
            }
            LoginMethod.EMAIL -> {
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = null
                    },
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Default.MailOutline, contentDescription = null) },
                    isError = emailError != null,
                    supportingText = emailError?.let { { Text(it) } },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = null
                    },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    isError = passwordError != null,
                    supportingText = passwordError?.let { { Text(it) } },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            emailError = "Enter a valid email address"
                            return@Button
                        }
                        if (password.length < 6) {
                            passwordError = "Password must be at least 6 characters"
                            return@Button
                        }
                        // Simulate successful login
                        onLoginSuccess()
                    },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text("Login")
                }
            }
        }
        if (error != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(error, color = MaterialTheme.colorScheme.error)
        }
    }
}

enum class LoginMethod { PHONE, EMAIL }

@Preview(showBackground = true, name = "Phone/OTP Login")
@Composable
fun PreviewLoginScreenPhone() {
    LoginScreen()
}

@Preview(showBackground = true, name = "Email/Password Login")
@Composable
fun PreviewLoginScreenEmail() {
    var selectedTab by remember { mutableStateOf(LoginMethod.EMAIL) }
    LoginScreen(
        onLoginSuccess = {},
        isLoading = false,
        error = null
    )
} 