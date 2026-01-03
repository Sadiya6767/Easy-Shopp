package com.example.easyshopp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.easyshopp.AppUtil
import com.example.easyshopp.R
import com.example.easyshopp.viewmodel.AuthViewModel

@Composable
fun SignupScreen(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel= viewModel()) {

    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    var context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ---------- TITLE ----------
        Text(
            text = "Hello There!!",
            style = TextStyle(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Create an Account",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ---------- IMAGE ----------
        Image(
            painter = painterResource(id = R.drawable.login_img),
            contentDescription = "Signup Illustration",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ---------- INPUT FIELDS ----------
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email Address") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Full Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ---------- SIGNUP BUTTON ----------
        Button(
            onClick = {
                isLoading = true
                authViewModel.signup(email, name, password){success, errorMessage->
                    if (success){
                        isLoading = false
                        navController.navigate("home"){
                            popUpTo("auth"){inclusive= true}
                        }
                    }
                    else{
                        isLoading = false
                        AppUtil.showToast(context, errorMessage?:"Something Went Wrong")
                    }
                }
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Text(text = if (isLoading)"Creating Account" else "Signup", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))





        // ---------- NAVIGATION TO LOGIN ----------
        TextButton(onClick = {
            navController.navigate("login")
        }) {
            Text(text = "Already have an account? Login")
        }
    }
}
