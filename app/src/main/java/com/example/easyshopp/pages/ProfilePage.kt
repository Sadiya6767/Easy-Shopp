package com.example.easyshopp.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easyshopp.AppUtil
import com.example.easyshopp.GlobalNavigation
import com.example.easyshopp.R
import com.example.easyshopp.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun ProfilePage(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    var userModel by remember {
        mutableStateOf(UserModel())
    }

    var addressInput by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .get()
            .addOnSuccessListener {
                val result = it.toObject(UserModel::class.java)
                if (result != null) {
                    userModel = result
                    addressInput = result.address
                }
            }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .statusBarsPadding()
            .padding(18.dp)
    ) {

        Text(
            text = "Your Profile",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Image(
            painter = painterResource(id = R.drawable.img),
            contentDescription = "profile icon",
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        )

        Text(
            text = userModel.name,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Address :",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        TextField(
            value = addressInput,
            onValueChange = { addressInput = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                if (addressInput.isNotEmpty()) {
                    Firebase.firestore.collection("users")
                        .document(FirebaseAuth.getInstance().currentUser?.uid!!)
                        .update("address", addressInput)
                        .addOnSuccessListener {
                            AppUtil.showToast(context, "Address updated successfully")
                        }
                } else {
                    AppUtil.showToast(context, "Address can't be empty")
                }
            }),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Email :",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Text(text = userModel.email)

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "My Orders",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    GlobalNavigation.navController.navigate("orders")
                }
                .padding(16.dp)
        )

        TextButton(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                GlobalNavigation.navController.navigate("auth") {
                    popUpTo(0)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Sign out", fontSize = 18.sp)
        }
    }
}
