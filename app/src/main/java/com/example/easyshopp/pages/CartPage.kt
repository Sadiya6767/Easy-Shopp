package com.example.easyshopp.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easyshopp.components.CartItemView
import com.example.easyshopp.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.example.easyshopp.GlobalNavigation

@Composable
fun CartPage(modifier: Modifier = Modifier)
{


    val userModel = remember {
        mutableStateOf(UserModel())
    }

    DisposableEffect(Unit) {
        val listener = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .addSnapshotListener { it, _ ->
                if (it != null) {
                    val result = it.toObject(UserModel::class.java)
                    if (result != null) {
                        userModel.value = result
                    }
                }
            }
        onDispose { listener.remove() }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .statusBarsPadding()
            .padding(18.dp)
    ) {

        Text(
            text = "Your Cart",
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        )



        if(userModel.value.cartItem.isEmpty()){
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(
                    userModel.value.cartItem.toList(),
                    key = { it.first }
                ) { (productId, qty) ->
                    CartItemView(productId = productId, qty = qty)
                }
            }

            Button(
                onClick = {
                    GlobalNavigation.navController.navigate("checkout")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Checkout",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        else{
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "No Item Here",
                    fontSize = 32.sp)
            }

        }
    }
}
